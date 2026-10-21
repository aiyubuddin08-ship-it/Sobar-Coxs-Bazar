package com.example.ui.screens.route

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.LeafletRouteMapView
import com.example.ui.components.LocationManagerHelper
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSystemScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val destinations by viewModel.destinations.collectAsState()
    val startingPoints by viewModel.startingPoints.collectAsState()
    val currentOrigin by viewModel.currentOrigin.collectAsState()
    val selectedDestination by viewModel.selectedRouteDestination.collectAsState()
    val routeState by viewModel.routeCalculationState.collectAsState()
    val isLiveNavActive by viewModel.isLiveNavigationActive.collectAsState()
    val currentStepIdx by viewModel.currentStepIndex.collectAsState()

    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showOriginDialog by remember { mutableStateOf(false) }
    var showAllDestinationsList by remember { mutableStateOf(false) }
    var locationErrorSnack by remember { mutableStateOf<String?>(null) }
    var isGpsFetching by remember { mutableStateOf(false) }

    // Runtime Permission Launcher for GPS Location
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            isGpsFetching = true
            LocationManagerHelper.requestCurrentGpsLocation(
                context = context,
                onSuccess = { lat, lng ->
                    isGpsFetching = false
                    viewModel.setUserGpsLocation(lat, lng)
                },
                onError = { err ->
                    isGpsFetching = false
                    locationErrorSnack = err
                }
            )
        } else {
            locationErrorSnack = "অবস্থান জানার অনুমতি বাতিল করা হয়েছে। অনুগ্রহ করে নিচে থেকে প্রারম্ভিক স্থান নির্বাচন করুন।"
        }
    }

    val categories = listOf(
        "All" to "সব গন্তব্য",
        "পর্যটন স্পট" to "সৈকত ও স্পট",
        "দ্বীপ ও নৌপথ" to "মহেশখালী ও দ্বীপ",
        "হোটেল ও রিসোর্ট" to "হোটেল ও অন্যান্য"
    )

    val filteredDestinations = destinations.filter { dest ->
        val matchesCategory = when (selectedCategoryFilter) {
            "All" -> true
            "পর্যটন স্পট" -> dest.category.contains("স্পট") || dest.category.contains("সৈকত")
            "দ্বীপ ও নৌপথ" -> dest.category.contains("দ্বীপ") || dest.category.contains("নৌ") || dest.requiresFerry
            "হোটেল ও রিসোর্ট" -> !dest.category.contains("স্পট") && !dest.category.contains("সৈকত") && !dest.category.contains("দ্বীপ")
            else -> dest.category == selectedCategoryFilter
        }
        val matchesSearch = if (searchQuery.isBlank()) true else {
            dest.nameBn.contains(searchQuery, ignoreCase = true) ||
            dest.nameEn.contains(searchQuery, ignoreCase = true) ||
            dest.address.contains(searchQuery, ignoreCase = true)
        }
        matchesCategory && matchesSearch
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "লোকেশন ও রুট সিস্টেম",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "কক্সবাজার ও আশপাশের ভ্রমণ রুট",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.calculateRoute() },
                        modifier = Modifier.testTag("refresh_route_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh Route")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Destination Search Input (Top-level, always accessible)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("গন্তব্য খুঁজুন (যেমন: ইনানী, হিমছড়ি, হোটেল...)", fontSize = 13.sp) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .height(48.dp)
                    .testTag("destination_search_input")
            )

            // Origin Location Banner with GPS Button
            OriginSelectorCard(
                currentOrigin = currentOrigin,
                isGpsFetching = isGpsFetching,
                onRequestGps = {
                    if (LocationManagerHelper.hasLocationPermission(context)) {
                        isGpsFetching = true
                        LocationManagerHelper.requestCurrentGpsLocation(
                            context = context,
                            onSuccess = { lat, lng ->
                                isGpsFetching = false
                                viewModel.setUserGpsLocation(lat, lng)
                            },
                            onError = { err ->
                                isGpsFetching = false
                                locationErrorSnack = err
                            }
                        )
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                onChangeOrigin = { showOriginDialog = true }
            )

            // Location Error Banner if any
            if (locationErrorSnack != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = locationErrorSnack ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { locationErrorSnack = null },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Dismiss", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // Main Content Area: LazyColumn with details panel when destination selected, or list of destinations
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("route_system_scroll_container"),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // If a destination is currently active and selected, display Route Details Panel at top!
                if (selectedDestination != null) {
                    item(key = "active_destination_panel") {
                        val activeDest = selectedDestination!!
                        ActiveRouteDetailsPanel(
                            destination = activeDest,
                            currentOrigin = currentOrigin,
                            routeState = routeState,
                            isLiveNavActive = isLiveNavActive,
                            currentStepIdx = currentStepIdx,
                            onStartNav = { viewModel.startLiveNavigation() },
                            onStopNav = { viewModel.stopLiveNavigation() },
                            onNextStep = { viewModel.nextNavigationStep() },
                            onPrevStep = { viewModel.prevNavigationStep() },
                            onRetryRoute = { viewModel.calculateRoute() },
                            onChangeDestination = { viewModel.openRouteForDestination(activeDest) }
                        )
                    }
                }

                // Destination Selection & Search Section
                if (selectedDestination != null) {
                    val remainingDestinations = filteredDestinations.filter { it.id != selectedDestination?.id }

                    if (searchQuery.isNotBlank()) {
                        // When user is actively searching: Show matching results cleanly
                        item {
                            Text(
                                text = "অনুসন্ধান ফলাফল (${remainingDestinations.size} টি স্থান)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        if (remainingDestinations.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "এই নামে কোনো গন্তব্য পাওয়া যায়নি।",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        } else {
                            items(remainingDestinations, key = { it.id }) { dest ->
                                DestinationSelectionCard(
                                    destination = dest,
                                    isSelected = false,
                                    onSelect = {
                                        searchQuery = ""
                                        viewModel.openRouteForDestination(dest)
                                    }
                                )
                            }
                        }
                    } else {
                        // When search is blank: Show a clean expandable bar instead of dumping 20+ huge cards
                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                    .clickable { showAllDestinationsList = !showAllDestinationsList }
                                    .testTag("toggle_other_destinations_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Explore,
                                            contentDescription = null,
                                            tint = OceanPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (showAllDestinationsList) "অন্যান্য গন্তব্যের তালিকা সংক্ষেপ করুন" else "অন্যান্য আকর্ষণীয় স্থান দেখুন (${destinations.size - 1} টি)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Icon(
                                        imageVector = if (showAllDestinationsList) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        // Expanded other destinations
                        if (showAllDestinationsList) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 4.dp)
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    categories.forEach { (catKey, label) ->
                                        val isSelected = selectedCategoryFilter == catKey
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { selectedCategoryFilter = catKey },
                                            label = { Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                            shape = RoundedCornerShape(16.dp),
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = OceanPrimary,
                                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                            )
                                        )
                                    }
                                }
                            }

                            items(remainingDestinations, key = { it.id }) { dest ->
                                DestinationSelectionCard(
                                    destination = dest,
                                    isSelected = false,
                                    onSelect = {
                                        viewModel.openRouteForDestination(dest)
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // No destination selected yet: Show destination catalog with category filters
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "গন্তব্য নির্বাচন করুন",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            // Category Pills
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                categories.forEach { (catKey, label) ->
                                    val isSelected = selectedCategoryFilter == catKey
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedCategoryFilter = catKey },
                                        label = { Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                        shape = RoundedCornerShape(16.dp),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = OceanPrimary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }

                    if (filteredDestinations.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "কোনো গন্তব্য পাওয়া যায়নি। ফিল্টার বা সার্চ পরিবর্তন করুন।",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        items(filteredDestinations, key = { it.id }) { dest ->
                            DestinationSelectionCard(
                                destination = dest,
                                isSelected = false,
                                onSelect = {
                                    viewModel.openRouteForDestination(dest)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Dialog to Select Origin Starting Point
    if (showOriginDialog) {
        OriginSelectionDialog(
            startingPoints = startingPoints,
            currentOrigin = currentOrigin,
            onSelectPoint = { pt ->
                viewModel.setStartingPoint(pt)
                showOriginDialog = false
            },
            onDismiss = { showOriginDialog = false }
        )
    }
}

@Composable
fun OriginSelectorCard(
    currentOrigin: StartingPoint,
    isGpsFetching: Boolean,
    onRequestGps: () -> Unit,
    onChangeOrigin: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("origin_selector_card")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (currentOrigin.isGps) OceanPrimary else SunsetAmber,
                shape = CircleShape,
                modifier = Modifier.size(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (currentOrigin.isGps) Icons.Default.MyLocation else Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "শুরু: ${currentOrigin.nameBn}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            TextButton(
                onClick = onChangeOrigin,
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("বদলান", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.width(4.dp))

            FilledTonalButton(
                onClick = onRequestGps,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(32.dp)
                    .testTag("detect_gps_button"),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                if (isGpsFetching) {
                    CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 1.5.dp)
                } else {
                    Icon(imageVector = Icons.Default.GpsFixed, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("GPS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ActiveRouteDetailsPanel(
    destination: RouteDestination,
    currentOrigin: StartingPoint,
    routeState: RouteCalculationState,
    isLiveNavActive: Boolean,
    currentStepIdx: Int,
    onStartNav: () -> Unit,
    onStopNav: () -> Unit,
    onNextStep: () -> Unit,
    onPrevStep: () -> Unit,
    onRetryRoute: () -> Unit,
    onChangeDestination: () -> Unit
) {
    val isVerified = destination.verificationStatus.contains("যাচাইকৃত", ignoreCase = true) ||
                     destination.verificationStatus.contains("Verified", ignoreCase = true)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("active_route_details_panel")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Destination Name, Brief Address, Verified Badge (only if verified), and Change option
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = destination.nameBn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (isVerified) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color(0xFF2E7D32).copy(alpha = 0.12f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "যাচাইকৃত",
                                        tint = Color(0xFF2E7D32),
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "যাচাইকৃত",
                                        color = Color(0xFF2E7D32),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = "📍 ${destination.address}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                TextButton(
                    onClick = onChangeDestination,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("বদলান", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // State Handling: Loading, Error, or Success
            when (routeState) {
                is RouteCalculationState.Idle, is RouteCalculationState.Loading -> {
                    // Map preview with origin & destination pins while route calculates
                    LeafletRouteMapView(
                        startLat = currentOrigin.latitude,
                        startLng = currentOrigin.longitude,
                        destLat = destination.latitude,
                        destLng = destination.longitude,
                        geoJsonCoordinates = emptyList(),
                        startNameBn = currentOrigin.nameBn,
                        destNameBn = destination.nameBn,
                        requiresFerry = destination.requiresFerry,
                        ferryGhatLat = destination.ferryGhatLat,
                        ferryGhatLng = destination.ferryGhatLng,
                        ferryGhatNameBn = destination.ferryGhatNameBn,
                        modifier = Modifier.height(230.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Small clean loading indicator (no technical jargon)
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "রুট ও দূরত্ব গণনা করা হচ্ছে...",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                is RouteCalculationState.Error -> {
                    // Map without route polyline
                    LeafletRouteMapView(
                        startLat = currentOrigin.latitude,
                        startLng = currentOrigin.longitude,
                        destLat = destination.latitude,
                        destLng = destination.longitude,
                        geoJsonCoordinates = emptyList(),
                        startNameBn = currentOrigin.nameBn,
                        destNameBn = destination.nameBn,
                        requiresFerry = destination.requiresFerry,
                        ferryGhatLat = destination.ferryGhatLat,
                        ferryGhatLng = destination.ferryGhatLng,
                        ferryGhatNameBn = destination.ferryGhatNameBn,
                        modifier = Modifier.height(230.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Concise clear error message
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "সড়ক রুট পাওয়া যায়নি। নেটওয়ার্ক চেক করুন।",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = onRetryRoute,
                                modifier = Modifier.testTag("retry_route_button"),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("পুনরায় চেষ্টা", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                is RouteCalculationState.Success -> {
                    var showAllSteps by remember(destination.id, currentOrigin) { mutableStateOf(false) }

                    // Embedded Map (showing route polyline, current location pin, destination pin, live GPS)
                    LeafletRouteMapView(
                        startLat = currentOrigin.latitude,
                        startLng = currentOrigin.longitude,
                        destLat = destination.latitude,
                        destLng = destination.longitude,
                        geoJsonCoordinates = routeState.coordinates,
                        startNameBn = currentOrigin.nameBn,
                        destNameBn = destination.nameBn,
                        requiresFerry = routeState.requiresFerry,
                        ferryGhatLat = destination.ferryGhatLat,
                        ferryGhatLng = destination.ferryGhatLng,
                        ferryGhatNameBn = destination.ferryGhatNameBn,
                        modifier = Modifier.height(240.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Total Distance and Travel Duration Strip (একক ও পুনরাবৃত্তিহীন)
                    Surface(
                        color = OceanPrimary.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Straighten,
                                    contentDescription = null,
                                    tint = OceanPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "মোট দূরত্ব",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = routeState.distanceFormattedBn,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanPrimary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(28.dp)
                                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = OceanPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "আনুমানিক সময়",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = routeState.durationFormattedBn,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Ferry advisory if island/ferry route
                    if (routeState.requiresFerry) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsBoat,
                                    contentDescription = null,
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "নৌপথ সংযোগ: ${routeState.ferryGhatName ?: "কস্তুরী ঘাট"} (${routeState.ferryDurationBn ?: "স্পিডবোট/ট্রলার"})",
                                    fontSize = 11.sp,
                                    color = Color(0xFF5D4037),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Next Turn Guidance Note (if steps present)
                    if (routeState.steps.isNotEmpty()) {
                        val nextStep = routeState.steps.getOrNull(currentStepIdx) ?: routeState.steps.first()
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = getManeuverIcon(nextStep.maneuverType, nextStep.modifier),
                                    contentDescription = null,
                                    tint = OceanPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "পরবর্তী মোড়: ${nextStep.instructionBn}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${nextStep.distanceTextBn} পরে • ${nextStep.streetName}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (routeState.steps.size > 1) {
                                    IconButton(
                                        onClick = onNextStep,
                                        enabled = currentStepIdx < routeState.steps.size - 1,
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next turn", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Separate Button to View All Directions
                        OutlinedButton(
                            onClick = { showAllSteps = !showAllSteps },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("toggle_all_steps_button"),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = OceanPrimary
                            )
                        ) {
                            Icon(
                                imageVector = if (showAllSteps) Icons.Default.ExpandLess else Icons.Default.FormatListNumbered,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (showAllSteps) "রাস্তার নির্দেশনা সংক্ষেপ করুন" else "সম্পূর্ণ রাস্তার নির্দেশনা দেখুন (${routeState.steps.size} টি মোড়)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Expandable Step List
                        AnimatedVisibility(
                            visible = showAllSteps,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                            ) {
                                Text(
                                    text = "🛣️ সম্পূর্ণ রাস্তার নির্দেশনা তালিকা:",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                routeState.steps.forEachIndexed { idx, step ->
                                    val isCurrent = isLiveNavActive && idx == currentStepIdx
                                    TurnByTurnStepRow(step = step, index = idx + 1, isCurrent = isCurrent)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                TextButton(
                                    onClick = { showAllSteps = false },
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    Icon(imageVector = Icons.Default.ExpandLess, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("লুকিয়ে রাখুন", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TurnByTurnStepRow(
    step: RouteStepItem,
    index: Int,
    isCurrent: Boolean
) {
    Surface(
        color = if (isCurrent) OceanPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
        border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.dp, OceanPrimary) else null,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (isCurrent) OceanPrimary else MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = getManeuverIcon(step.maneuverType, step.modifier),
                        contentDescription = null,
                        tint = if (isCurrent) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$index. ${step.instructionBn}",
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${step.distanceTextBn} (${step.streetName})",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DestinationSelectionCard(
    destination: RouteDestination,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val isVerified = destination.verificationStatus.contains("যাচাইকৃত", ignoreCase = true) ||
                     destination.verificationStatus.contains("Verified", ignoreCase = true)

    Card(
        onClick = onSelect,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) OceanPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, OceanPrimary) else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.dp)
            .testTag("destination_card_${destination.id}")
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .defaultMinSize(minHeight = 48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (destination.requiresFerry) SunsetAmber.copy(alpha = 0.15f) else OceanPrimary.copy(alpha = 0.10f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (destination.requiresFerry) Icons.Default.DirectionsBoat else getCategoryIcon(destination.category),
                        contentDescription = null,
                        tint = if (destination.requiresFerry) SunsetAmber else OceanPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = destination.nameBn,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "যাচাইকৃত",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = destination.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (destination.requiresFerry) {
                    Text(
                        text = "নৌপথ প্রয়োজন",
                        color = Color(0xFFE65100),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            FilledTonalButton(
                onClick = onSelect,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("রুট দেখুন", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun OriginSelectionDialog(
    startingPoints: List<StartingPoint>,
    currentOrigin: StartingPoint,
    onSelectPoint: (StartingPoint) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "শুরুর অবস্থান নির্বাচন করুন",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp)
            ) {
                Text(
                    text = "GPS ছাড়া দূরত্ব হিসাবের জন্য নিচের যেকোনো একটি প্রারম্ভিক স্থান বেছে নিন:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(startingPoints) { pt ->
                        val isSelected = currentOrigin.nameBn == pt.nameBn
                        Surface(
                            color = if (isSelected) OceanPrimary.copy(alpha = 0.12f) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectPoint(pt) }
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (pt.isGps) Icons.Default.MyLocation else Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = if (isSelected) OceanPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = pt.nameBn,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) OceanPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বন্ধ করুন")
            }
        }
    )
}

fun getManeuverIcon(type: String, modifier: String?): ImageVector {
    return when (type) {
        "depart" -> Icons.Default.TripOrigin
        "arrive" -> Icons.Default.PinDrop
        "turn" -> when (modifier) {
            "right", "sharp right", "slight right" -> Icons.Filled.TurnRight
            "left", "sharp left", "slight left" -> Icons.Filled.TurnLeft
            else -> Icons.Default.Straight
        }
        "new name", "continue" -> Icons.Default.Straight
        "roundabout", "rotary" -> Icons.Default.Cached
        else -> Icons.Default.Navigation
    }
}

fun getCategoryIcon(cat: String): ImageVector {
    return when {
        cat.contains("দ্বীপ") || cat.contains("নৌ") -> Icons.Default.DirectionsBoat
        cat.contains("স্পট") || cat.contains("সৈকত") -> Icons.Default.BeachAccess
        cat.contains("হোটেল") -> Icons.Default.Hotel
        cat.contains("রেস্তোরাঁ") -> Icons.Default.Restaurant
        cat.contains("হাসপাতাল") || cat.contains("জরুরি") -> Icons.Default.LocalHospital
        cat.contains("শপিং") -> Icons.Default.ShoppingBag
        else -> Icons.Default.Place
    }
}
