package com.example.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

object LocationManagerHelper {

    fun hasLocationPermission(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    fun requestCurrentGpsLocation(
        context: Context,
        onSuccess: (latitude: Double, longitude: Double) -> Unit,
        onError: (errorMessageBn: String) -> Unit
    ) {
        if (!hasLocationPermission(context)) {
            onError("অবস্থান শনাক্তকরণের অনুমতি (Permission) প্রদান করা হয়নি। অনুগ্রহ করে অনুমতি দিন অথবা তালিকা থেকে শুরু করার স্থান নির্বাচন করুন।")
            return
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val isGpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
        val isNetworkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true

        if (!isGpsEnabled && !isNetworkEnabled) {
            onError("ডিভাইসের GPS বা লোকেশন সার্ভিস বন্ধ রয়েছে। অনুগ্রহ করে সেটিংস থেকে GPS চালু করুন অথবা নিচের তালিকা থেকে আপনার নিকটবর্তী স্থান বেছে নিন।")
            return
        }

        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        val cts = CancellationTokenSource()

        fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onSuccess(location.latitude, location.longitude)
                } else {
                    // Try lastLocation as immediate fallback
                    fusedClient.lastLocation.addOnSuccessListener { lastLoc: Location? ->
                        if (lastLoc != null) {
                            onSuccess(lastLoc.latitude, lastLoc.longitude)
                        } else {
                            // Fallback to legacy LocationManager
                            val gpsLoc = try { locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER) } catch (e: Exception) { null }
                            val netLoc = try { locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) } catch (e: Exception) { null }
                            val best = gpsLoc ?: netLoc
                            if (best != null) {
                                onSuccess(best.latitude, best.longitude)
                            } else {
                                onError("স্যাটেলাইট থেকে বর্তমান জিপিএস স্থানাঙ্ক পাওয়া যায়নি। অনুগ্রহ করে নিচে থেকে আপনার নিকটবর্তী স্থান নির্বাচন করুন।")
                            }
                        }
                    }.addOnFailureListener {
                        onError("স্যাটেলাইট সংযোগ সমস্যা: অবস্থান পাওয়া যায়নি। তালিকা থেকে শুরুর স্থান বেছে নিন।")
                    }
                }
            }
            .addOnFailureListener { e ->
                onError("জিপিএস অবস্থান পড়তে ত্রুটি হয়েছে: ${e.localizedMessage ?: "ত্রুটি"}। তালিকা থেকে শুরুর স্থান বেছে নিন।")
            }
    }
}
