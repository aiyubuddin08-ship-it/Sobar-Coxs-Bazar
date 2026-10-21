package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import org.json.JSONArray
import java.util.Locale
import kotlin.math.*

enum class MapRendererType {
    NATIVE_OSM,
    LEAFLET_WEBVIEW
}

/**
 * High-performance, crash-resilient OpenStreetMap Route Map.
 * Defaults to Native OSM Tile & Vector Canvas (eliminating Chromium renderer crashes),
 * while preserving optional Leaflet WebView rendering.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LeafletRouteMapView(
    startLat: Double,
    startLng: Double,
    destLat: Double,
    destLng: Double,
    geoJsonCoordinates: List<List<Double>>, // [[lng, lat], ...]
    startNameBn: String,
    destNameBn: String,
    requiresFerry: Boolean = false,
    ferryGhatLat: Double? = null,
    ferryGhatLng: Double? = null,
    ferryGhatNameBn: String? = null,
    currentLiveLat: Double? = null,
    currentLiveLng: Double? = null,
    isLiveTracking: Boolean = false,
    modifier: Modifier = Modifier
) {
    var rendererType by remember { mutableStateOf(MapRendererType.NATIVE_OSM) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isWebMapLoaded by remember { mutableStateOf(false) }
    var webViewCrashed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE8F0FE))
            .border(1.dp, Color(0xFFCFD8DC), RoundedCornerShape(16.dp))
            .testTag("leaflet_route_map_preview")
    ) {
        if (rendererType == MapRendererType.NATIVE_OSM || webViewCrashed) {
            NativeOsmRouteCanvas(
                startLat = startLat,
                startLng = startLng,
                destLat = destLat,
                destLng = destLng,
                geoJsonCoordinates = geoJsonCoordinates,
                startNameBn = startNameBn,
                destNameBn = destNameBn,
                requiresFerry = requiresFerry,
                ferryGhatLat = ferryGhatLat,
                ferryGhatLng = ferryGhatLng,
                ferryGhatNameBn = ferryGhatNameBn,
                currentLiveLat = currentLiveLat,
                currentLiveLng = currentLiveLng,
                isLiveTracking = isLiveTracking,
                onSwitchToWebView = {
                    webViewCrashed = false
                    rendererType = MapRendererType.LEAFLET_WEBVIEW
                }
            )
        } else {
            // Leaflet WebView with crash-safe configuration
            SafeLeafletWebView(
                startLat = startLat,
                startLng = startLng,
                destLat = destLat,
                destLng = destLng,
                geoJsonCoordinates = geoJsonCoordinates,
                startNameBn = startNameBn,
                destNameBn = destNameBn,
                requiresFerry = requiresFerry,
                ferryGhatLat = ferryGhatLat,
                ferryGhatLng = ferryGhatLng,
                ferryGhatNameBn = ferryGhatNameBn,
                currentLiveLat = currentLiveLat,
                currentLiveLng = currentLiveLng,
                onRenderCrashed = {
                    webViewCrashed = true
                    rendererType = MapRendererType.NATIVE_OSM
                },
                onSwitchToNative = {
                    rendererType = MapRendererType.NATIVE_OSM
                }
            )
        }
    }
}

/**
 * 100% Native Compose OpenStreetMap Route Canvas.
 * Computes Mercator projections, renders OSM tile grid, road polylines, ferry routes,
 * origin/dest pins, and live GPS dot without relying on Chromium.
 */
@Composable
private fun NativeOsmRouteCanvas(
    startLat: Double,
    startLng: Double,
    destLat: Double,
    destLng: Double,
    geoJsonCoordinates: List<List<Double>>,
    startNameBn: String,
    destNameBn: String,
    requiresFerry: Boolean,
    ferryGhatLat: Double?,
    ferryGhatLng: Double?,
    ferryGhatNameBn: String?,
    currentLiveLat: Double?,
    currentLiveLng: Double?,
    isLiveTracking: Boolean,
    onSwitchToWebView: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    // Calculate bounding box
    val allLats = mutableListOf(startLat, destLat)
    val allLngs = mutableListOf(startLng, destLng)
    if (requiresFerry && ferryGhatLat != null && ferryGhatLng != null) {
        allLats.add(ferryGhatLat)
        allLngs.add(ferryGhatLng)
    }
    if (currentLiveLat != null && currentLiveLng != null) {
        allLats.add(currentLiveLat)
        allLngs.add(currentLiveLng)
    }
    for (pt in geoJsonCoordinates) {
        if (pt.size >= 2) {
            allLngs.add(pt[0])
            allLats.add(pt[1])
        }
    }

    val minLat = allLats.minOrNull() ?: startLat
    val maxLat = allLats.maxOrNull() ?: destLat
    val minLng = allLngs.minOrNull() ?: startLng
    val maxLng = allLngs.maxOrNull() ?: destLng

    val initialCenterLat = (minLat + maxLat) / 2.0
    val initialCenterLng = (minLng + maxLng) / 2.0

    // Automatic Zoom calculation
    val latSpan = max(0.005, maxLat - minLat)
    val lngSpan = max(0.005, maxLng - minLng)
    val span = max(latSpan, lngSpan)
    val baseZoom = when {
        span < 0.03 -> 14
        span < 0.08 -> 13
        span < 0.18 -> 12
        span < 0.40 -> 11
        else -> 10
    }

    var zoomLevel by remember(startLat, destLat) { mutableIntStateOf(baseZoom) }
    var panOffsetX by remember(startLat, destLat) { mutableFloatStateOf(0f) }
    var panOffsetY by remember(startLat, destLat) { mutableFloatStateOf(0f) }
    var currentCenterLat by remember(startLat, destLat) { mutableDoubleStateOf(initialCenterLat) }
    var currentCenterLng by remember(startLat, destLat) { mutableDoubleStateOf(initialCenterLng) }
    var isMapLoaded by remember(zoomLevel, currentCenterLat, currentCenterLng) { mutableStateOf(false) }
    var hasTileError by remember(zoomLevel, currentCenterLat, currentCenterLng) { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    panOffsetX += pan.x
                    panOffsetY += pan.y
                    if (zoom > 1.25f && zoomLevel < 17) {
                        zoomLevel++
                    } else if (zoom < 0.8f && zoomLevel > 8) {
                        zoomLevel--
                    }
                }
            }
    ) {
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        // Determine tiles covering the view
        val centerTileX = lon2tile(currentCenterLng, zoomLevel)
        val centerTileY = lat2tile(currentCenterLat, zoomLevel)

        val tilesRange = -1..1

        // Render OSM Tile Grid
        Box(modifier = Modifier.fillMaxSize()) {
            for (dx in tilesRange) {
                for (dy in tilesRange) {
                    val tx = centerTileX + dx
                    val ty = centerTileY + dy
                    val tileUrl = "https://tile.openstreetmap.org/$zoomLevel/$tx/$ty.png"

                    // Calculate tile screen position
                    val tileOriginX = ((tx - lon2tileD(currentCenterLng, zoomLevel)) * 256.0).toFloat() + widthPx / 2f + panOffsetX
                    val tileOriginY = ((ty - lat2tileD(currentCenterLat, zoomLevel)) * 256.0).toFloat() + heightPx / 2f + panOffsetY

                    val isCenterTile = (dx == 0 && dy == 0)

                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(tileUrl)
                            .addHeader("User-Agent", "CoxBazarTourGuideApp/1.0")
                            .crossfade(true)
                            .apply {
                                if (isCenterTile) {
                                    listener(
                                        onSuccess = { _, _ ->
                                            isMapLoaded = true
                                            hasTileError = false
                                        },
                                        onError = { _, _ ->
                                            hasTileError = true
                                        }
                                    )
                                }
                            }
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(256.dp)
                            .offset {
                                IntOffset(tileOriginX.roundToInt(), tileOriginY.roundToInt())
                            }
                    )
                }
            }
        }

        // Draw Road Geometry & Markers on Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height

            fun projectToScreen(lat: Double, lng: Double): Offset {
                val tx = lon2tileD(lng, zoomLevel)
                val ty = lat2tileD(lat, zoomLevel)
                val cx = lon2tileD(currentCenterLng, zoomLevel)
                val cy = lat2tileD(currentCenterLat, zoomLevel)

                val px = ((tx - cx) * 256.0).toFloat() + canvasW / 2f + panOffsetX
                val py = ((ty - cy) * 256.0).toFloat() + canvasH / 2f + panOffsetY
                return Offset(px, py)
            }

            // 1. Draw Road Polyline
            if (geoJsonCoordinates.isNotEmpty()) {
                val roadPath = Path()
                var first = true
                for (pt in geoJsonCoordinates) {
                    if (pt.size >= 2) {
                        val pos = projectToScreen(pt[1], pt[0])
                        if (first) {
                            roadPath.moveTo(pos.x, pos.y)
                            first = false
                        } else {
                            roadPath.lineTo(pos.x, pos.y)
                        }
                    }
                }

                // Road outer border / glow
                drawPath(
                    path = roadPath,
                    color = Color(0x660288D1),
                    style = Stroke(
                        width = 9.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Road inner core
                drawPath(
                    path = roadPath,
                    color = Color(0xFF0288D1),
                    style = Stroke(
                        width = 5.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // 2. Ferry Route Dashed Line if applicable
            if (requiresFerry && ferryGhatLat != null && ferryGhatLng != null) {
                val ghatPos = projectToScreen(ferryGhatLat, ferryGhatLng)
                val destPos = projectToScreen(destLat, destLng)

                val ferryPath = Path().apply {
                    moveTo(ghatPos.x, ghatPos.y)
                    lineTo(destPos.x, destPos.y)
                }

                drawPath(
                    path = ferryPath,
                    color = Color(0xFF00ACC1),
                    style = Stroke(
                        width = 3.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f),
                        cap = StrokeCap.Round
                    )
                )
            }

            // 3. Markers
            val startPos = projectToScreen(startLat, startLng)
            val destPos = projectToScreen(destLat, destLng)

            // Start Pin (Blue)
            drawCircle(color = Color.White, radius = 13.dp.toPx(), center = startPos)
            drawCircle(color = Color(0xFF0288D1), radius = 10.dp.toPx(), center = startPos)
            drawCircle(color = Color.White, radius = 4.dp.toPx(), center = startPos)

            // Dest Pin (Red)
            drawCircle(color = Color.White, radius = 14.dp.toPx(), center = destPos)
            drawCircle(color = Color(0xFFE53935), radius = 11.dp.toPx(), center = destPos)
            drawCircle(color = Color.White, radius = 4.5.dp.toPx(), center = destPos)

            // Ferry Ghat Pin (Orange)
            if (requiresFerry && ferryGhatLat != null && ferryGhatLng != null) {
                val ghatPos = projectToScreen(ferryGhatLat, ferryGhatLng)
                drawCircle(color = Color.White, radius = 12.dp.toPx(), center = ghatPos)
                drawCircle(color = Color(0xFFFB8C00), radius = 9.dp.toPx(), center = ghatPos)
                drawCircle(color = Color.White, radius = 3.5.dp.toPx(), center = ghatPos)
            }

            // Live GPS Dot
            if (currentLiveLat != null && currentLiveLng != null) {
                val livePos = projectToScreen(currentLiveLat, currentLiveLng)
                drawCircle(color = Color(0x4D2E7D32), radius = 18.dp.toPx(), center = livePos)
                drawCircle(color = Color.White, radius = 9.dp.toPx(), center = livePos)
                drawCircle(color = Color(0xFF2E7D32), radius = 7.dp.toPx(), center = livePos)
            }
        }

        // Live Tracking Badge
        if (isLiveTracking) {
            Surface(
                color = Color(0xFF1B5E20).copy(alpha = 0.9f),
                shape = RoundedCornerShape(bottomEnd = 10.dp),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Surface(
                        color = Color(0xFF69F0AE),
                        shape = CircleShape,
                        modifier = Modifier.size(7.dp)
                    ) {}
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "লাইভ জিপিএস সক্রিয়",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Controls Column (Zoom In, Zoom Out, Recenter, Fit)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                onClick = { if (zoomLevel < 18) zoomLevel++ },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                shadowElevation = 2.dp,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Zoom In", modifier = Modifier.size(18.dp))
                }
            }

            Surface(
                onClick = { if (zoomLevel > 7) zoomLevel-- },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                shadowElevation = 2.dp,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.ZoomOut, contentDescription = "Zoom Out", modifier = Modifier.size(18.dp))
                }
            }

            Surface(
                onClick = {
                    panOffsetX = 0f
                    panOffsetY = 0f
                    zoomLevel = baseZoom
                    currentCenterLat = initialCenterLat
                    currentCenterLng = initialCenterLng
                },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                shadowElevation = 2.dp,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.CropFree, contentDescription = "Fit Route", modifier = Modifier.size(17.dp))
                }
            }
        }

        // Small loading indicator while map tiles load (dismissed immediately on load)
        if (!isMapLoaded && !hasTileError) {
            Surface(
                color = Color.White.copy(alpha = 0.92f),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("map_loading_indicator")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(13.dp),
                        strokeWidth = 2.dp,
                        color = OceanPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ম্যাপ লোড হচ্ছে...",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else if (hasTileError && !isMapLoaded) {
            // Concise clear error message if map fails to load
            Surface(
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.95f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .testTag("map_error_banner")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ম্যাপ লোড করা যায়নি। ইন্টারনেট সংযোগ চেক করুন।",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        // Bottom Bar: Clean standard attribution only
        Surface(
            color = Color.Black.copy(alpha = 0.60f),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text(
                text = "© OpenStreetMap contributors",
                color = Color.White,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

/**
 * Crash-safe Leaflet WebView implementation.
 * Ensures proper view hierarchy detachment before destruction, disables software layer forcing,
 * and traps render process crashes cleanly.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun SafeLeafletWebView(
    startLat: Double,
    startLng: Double,
    destLat: Double,
    destLng: Double,
    geoJsonCoordinates: List<List<Double>>,
    startNameBn: String,
    destNameBn: String,
    requiresFerry: Boolean,
    ferryGhatLat: Double?,
    ferryGhatLng: Double?,
    ferryGhatNameBn: String?,
    currentLiveLat: Double?,
    currentLiveLng: Double?,
    onRenderCrashed: () -> Unit,
    onSwitchToNative: () -> Unit
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isMapLoaded by remember { mutableStateOf(false) }
    var isMapError by remember { mutableStateOf(false) }

    val roadPointsJson = remember(geoJsonCoordinates) {
        val arr = JSONArray()
        for (pt in geoJsonCoordinates) {
            if (pt.size >= 2) {
                val pair = JSONArray()
                pair.put(pt[1])
                pair.put(pt[0])
                arr.put(pair)
            }
        }
        arr.toString()
    }

    val htmlContent = remember(
        startLat, startLng, destLat, destLng, roadPointsJson,
        requiresFerry, ferryGhatLat, ferryGhatLng, startNameBn, destNameBn, ferryGhatNameBn
    ) {
        buildLeafletHtml(
            startLat = startLat,
            startLng = startLng,
            destLat = destLat,
            destLng = destLng,
            roadPointsJson = roadPointsJson,
            startNameBn = escapeJsString(startNameBn),
            destNameBn = escapeJsString(destNameBn),
            requiresFerry = requiresFerry,
            ferryGhatLat = ferryGhatLat,
            ferryGhatLng = ferryGhatLng,
            ferryGhatNameBn = escapeJsString(ferryGhatNameBn ?: "নৌঘাট")
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                InterceptTouchWebView(ctx).apply {
                    // Do NOT force LAYER_TYPE_SOFTWARE as it triggers Chromium compositor assert failures
                    setLayerType(View.LAYER_TYPE_HARDWARE, null)
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.cacheMode = WebSettings.LOAD_DEFAULT

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isMapLoaded = true
                            isMapError = false
                            if (currentLiveLat != null && currentLiveLng != null) {
                                val latStr = String.format(Locale.US, "%.6f", currentLiveLat)
                                val lngStr = String.format(Locale.US, "%.6f", currentLiveLng)
                                view?.evaluateJavascript("updateLiveUserPosition($latStr, $lngStr);", null)
                            }
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            errorCode: Int,
                            description: String?,
                            failingUrl: String?
                        ) {
                            super.onReceivedError(view, errorCode, description, failingUrl)
                            isMapError = true
                        }

                        override fun onRenderProcessGone(
                            view: WebView?,
                            detail: RenderProcessGoneDetail?
                        ): Boolean {
                            onRenderCrashed()
                            try {
                                (view?.parent as? ViewGroup)?.removeView(view)
                                view?.destroy()
                            } catch (_: Exception) {}
                            webViewRef = null
                            return true
                        }
                    }

                    tag = htmlContent
                    loadDataWithBaseURL("https://www.openstreetmap.org", htmlContent, "text/html", "UTF-8", null)
                    webViewRef = this
                }
            },
            update = { view ->
                webViewRef = view
                val currentTag = view.tag as? String
                if (currentTag != htmlContent) {
                    view.tag = htmlContent
                    view.loadDataWithBaseURL("https://www.openstreetmap.org", htmlContent, "text/html", "UTF-8", null)
                }
            },
            onRelease = { view ->
                try {
                    (view.parent as? ViewGroup)?.removeView(view)
                    view.stopLoading()
                    view.loadUrl("about:blank")
                    view.destroy()
                } catch (_: Exception) {}
            }
        )

        // Small loading indicator while map page loads
        if (!isMapLoaded && !isMapError) {
            Surface(
                color = Color.White.copy(alpha = 0.92f),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag("map_loading_indicator")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(13.dp),
                        strokeWidth = 2.dp,
                        color = OceanPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ম্যাপ লোড হচ্ছে...",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else if (isMapError && !isMapLoaded) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.95f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .testTag("map_error_banner")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ম্যাপ লোড করা যায়নি। ইন্টারনেট সংযোগ চেক করুন।",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        // Clean standard attribution
        Surface(
            color = Color.Black.copy(alpha = 0.60f),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text(
                text = "© OpenStreetMap contributors",
                color = Color.White,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

// Coordinate to Mercator Tile math helpers
private fun lon2tile(lon: Double, zoom: Int): Int = floor((lon + 180.0) / 360.0 * (1 shl zoom)).toInt()
private fun lat2tile(lat: Double, zoom: Int): Int {
    val latRad = Math.toRadians(lat)
    return floor((1.0 - ln(tan(latRad) + 1.0 / cos(latRad)) / Math.PI) / 2.0 * (1 shl zoom)).toInt()
}
private fun lon2tileD(lon: Double, zoom: Int): Double = (lon + 180.0) / 360.0 * (1 shl zoom)
private fun lat2tileD(lat: Double, zoom: Int): Double {
    val latRad = Math.toRadians(lat)
    return (1.0 - ln(tan(latRad) + 1.0 / cos(latRad)) / Math.PI) / 2.0 * (1 shl zoom)
}

private class InterceptTouchWebView(context: Context) : WebView(context) {
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> parent?.requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent?.requestDisallowInterceptTouchEvent(false)
        }
        return super.onTouchEvent(event)
    }
}

private fun escapeJsString(str: String): String {
    return str.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("'", "\\'")
        .replace("`", "\\`")
        .replace("\n", " ")
        .replace("\r", "")
}

private fun buildLeafletHtml(
    startLat: Double,
    startLng: Double,
    destLat: Double,
    destLng: Double,
    roadPointsJson: String,
    startNameBn: String,
    destNameBn: String,
    requiresFerry: Boolean,
    ferryGhatLat: Double?,
    ferryGhatLng: Double?,
    ferryGhatNameBn: String
): String {
    val sLat = String.format(Locale.US, "%.6f", startLat)
    val sLng = String.format(Locale.US, "%.6f", startLng)
    val dLat = String.format(Locale.US, "%.6f", destLat)
    val dLng = String.format(Locale.US, "%.6f", destLng)
    val gLat = String.format(Locale.US, "%.6f", ferryGhatLat ?: 0.0)
    val gLng = String.format(Locale.US, "%.6f", ferryGhatLng ?: 0.0)

    return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <style>
        html, body {
            margin: 0;
            padding: 0;
            width: 100%;
            height: 100%;
            background-color: #E8F0FE;
            overflow: hidden;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
        }
        #map { width: 100%; height: 100%; }
        .custom-pin {
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            font-size: 13px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.35);
            border: 2px solid #ffffff;
        }
        .pin-start { background-color: #0288D1; color: #fff; width: 24px; height: 24px; }
        .pin-dest { background-color: #E53935; color: #fff; width: 26px; height: 26px; }
        .pin-ghat { background-color: #FB8C00; color: #fff; width: 24px; height: 24px; }
    </style>
</head>
<body>
    <div id="map"></div>
    <script>
        try {
            var map = L.map('map', {
                zoomControl: false,
                attributionControl: false
            }).setView([$sLat, $sLng], 13);

            L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '&copy; OpenStreetMap contributors'
            }).addTo(map);

            var boundsGroup = L.featureGroup();

            var startIcon = L.divIcon({ className: 'custom-pin pin-start', html: '📍', iconSize: [26, 26] });
            var startMarker = L.marker([$sLat, $sLng], { icon: startIcon }).addTo(map);
            boundsGroup.addLayer(startMarker);

            var destIcon = L.divIcon({ className: 'custom-pin pin-dest', html: '🎯', iconSize: [28, 28] });
            var destMarker = L.marker([$dLat, $dLng], { icon: destIcon }).addTo(map);
            boundsGroup.addLayer(destMarker);

            var requiresFerry = $requiresFerry;
            var ghatLat = $gLat;
            var ghatLng = $gLng;

            if (requiresFerry && ghatLat !== 0.0) {
                var ghatIcon = L.divIcon({ className: 'custom-pin pin-ghat', html: '⚓', iconSize: [26, 26] });
                var ghatMarker = L.marker([ghatLat, ghatLng], { icon: ghatIcon }).addTo(map);
                boundsGroup.addLayer(ghatMarker);

                var waterLine = L.polyline([[ghatLat, ghatLng], [$dLat, $dLng]], {
                    color: '#00ACC1',
                    weight: 4,
                    dashArray: '8, 8'
                }).addTo(map);
                boundsGroup.addLayer(waterLine);
            }

            try {
                var roadCoords = $roadPointsJson;
                if (roadCoords && roadCoords.length > 0) {
                    var roadPolyline = L.polyline(roadCoords, {
                        color: '#0288D1',
                        weight: 5
                    }).addTo(map);
                    boundsGroup.addLayer(roadPolyline);
                }
            } catch(e) {}

            function fitBoundsToRoute() {
                if (boundsGroup && boundsGroup.getLayers().length > 0) {
                    map.fitBounds(boundsGroup.getBounds(), { padding: [35, 35] });
                }
            }

            setTimeout(fitBoundsToRoute, 300);
        } catch(err) {}
    </script>
</body>
</html>
    """.trimIndent()
}
