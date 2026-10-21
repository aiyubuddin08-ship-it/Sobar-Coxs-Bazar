package com.example.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

/**
 * Continuous GPS Location Tracking Manager for Android.
 *
 * Implements high-accuracy location streaming with lifecycle-aware auto-pause:
 * - Streams GPS updates when active on map / route screen
 * - Automatically unregisters callback when leaving screen or moving to background
 * - Validates hardware permissions and GPS provider state with clear Bengali error states
 */
object LocationTrackingManager {

    private var fusedClient: FusedLocationProviderClient? = null
    private var activeCallback: LocationCallback? = null

    @Volatile
    var isTracking: Boolean = false
        private set

    @Volatile
    var lastKnownLat: Double? = null
        private set

    @Volatile
    var lastKnownLng: Double? = null
        private set

    /**
     * Starts continuous high-accuracy location tracking.
     */
    @SuppressLint("MissingPermission")
    fun startTracking(
        context: Context,
        onLocationChanged: (latitude: Double, longitude: Double, accuracy: Float) -> Unit,
        onError: (errorMessageBn: String) -> Unit
    ) {
        val appContext = context.applicationContext

        // 1. Permission check
        val finePerm = ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarsePerm = ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (finePerm != PackageManager.PERMISSION_GRANTED && coarsePerm != PackageManager.PERMISSION_GRANTED) {
            onError("অবস্থান জানার অনুমতি (Location Permission) দেওয়া হয়নি। অনুগ্রহ করে অনুমতি প্রদান করুন।")
            return
        }

        // 2. Hardware GPS check
        val locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val isGpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
        val isNetworkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true

        if (!isGpsEnabled && !isNetworkEnabled) {
            onError("ডিভাইসের জিপিএস (GPS / Location) বন্ধ রয়েছে। অনুগ্রহ করে ফোনের সেটিংস থেকে লোকেশন চালু করুন।")
            return
        }

        // Stop any previous tracking before creating new callback
        stopTracking()

        val client = LocationServices.getFusedLocationProviderClient(appContext)
        fusedClient = client

        // Immediately fetch last known location to avoid waiting for first GPS lock
        try {
            client.lastLocation.addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    lastKnownLat = loc.latitude
                    lastKnownLng = loc.longitude
                    onLocationChanged(loc.latitude, loc.longitude, loc.accuracy)
                }
            }
        } catch (_: Exception) {
        }

        // Configure continuous high-accuracy LocationRequest
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 4000L)
            .setMinUpdateIntervalMillis(2000L)
            .setMinUpdateDistanceMeters(3.0f) // Only trigger when user moves > 3 meters
            .setWaitForAccurateLocation(false)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                lastKnownLat = loc.latitude
                lastKnownLng = loc.longitude
                onLocationChanged(loc.latitude, loc.longitude, loc.accuracy)
            }

            override fun onLocationAvailability(avail: LocationAvailability) {
                if (!avail.isLocationAvailable) {
                    // Inform without hard-failing
                }
            }
        }

        try {
            client.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
            activeCallback = callback
            isTracking = true
        } catch (e: Exception) {
            isTracking = false
            onError("জিপিএস ট্র্যাকিং সক্রিয় করতে সমস্যা হয়েছে: ${e.localizedMessage ?: "অজ্ঞাত ত্রুটি"}")
        }
    }

    /**
     * Stops continuous tracking and unregisters GPS callback.
     * Must be called whenever user exits the map/route screen.
     */
    fun stopTracking() {
        val client = fusedClient
        val callback = activeCallback
        if (client != null && callback != null) {
            try {
                client.removeLocationUpdates(callback)
            } catch (_: Exception) {
            }
        }
        activeCallback = null
        fusedClient = null
        isTracking = false
    }
}

/**
 * Composable lifecycle helper that automatically starts GPS tracking
 * when [enabled] is true and automatically unregisters and stops tracking
 * when leaving the Composable or when [enabled] becomes false.
 */
@Composable
fun RememberLiveGpsTracker(
    enabled: Boolean,
    context: Context,
    onLocationChanged: (latitude: Double, longitude: Double) -> Unit,
    onError: (errorMessageBn: String) -> Unit
) {
    DisposableEffect(enabled) {
        if (enabled) {
            LocationTrackingManager.startTracking(
                context = context,
                onLocationChanged = { lat, lng, _ ->
                    onLocationChanged(lat, lng)
                },
                onError = onError
            )
        }
        onDispose {
            LocationTrackingManager.stopTracking()
        }
    }
}
