package com.example.data.network

import com.example.data.model.RouteStepItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit

data class OsrmResult(
    val success: Boolean,
    val distanceMeters: Double = 0.0,
    val durationSeconds: Double = 0.0,
    val coordinates: List<List<Double>> = emptyList(), // [[lng, lat], ...]
    val steps: List<RouteStepItem> = emptyList(),
    val errorMessageBn: String? = null
)

object OsrmRoutingService {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val baseUrl: String
        get() = CloudflareRoutingConfig.activeBaseUrl

    /**
     * Calculates the driving route using OSRM public routing API.
     * Note: Coordinates must be in longitude,latitude order!
     */
    suspend fun getRoute(
        startLat: Double,
        startLng: Double,
        destLat: Double,
        destLng: Double
    ): OsrmResult = withContext(Dispatchers.IO) {
        // Validate coordinates
        if (startLat !in -90.0..90.0 || startLng !in -180.0..180.0 ||
            destLat !in -90.0..90.0 || destLng !in -180.0..180.0
        ) {
            return@withContext OsrmResult(
                success = false,
                errorMessageBn = "অবস্থানের স্থানাঙ্ক (Latitude/Longitude) সঠিক নয়।"
            )
        }

        // Check if start and dest are identical (within ~10 meters)
        if (Math.abs(startLat - destLat) < 0.0001 && Math.abs(startLng - destLng) < 0.0001) {
            return@withContext OsrmResult(
                success = true,
                distanceMeters = 0.0,
                durationSeconds = 0.0,
                coordinates = listOf(listOf(startLng, startLat), listOf(destLng, destLat)),
                steps = listOf(
                    RouteStepItem(
                        instructionBn = "আপনি ইতিমধ্যেই গন্তব্যে অবস্থান করছেন।",
                        streetName = "বর্তমান স্থান",
                        distanceTextBn = "০ মিটার",
                        durationTextBn = "০ মিনিট",
                        maneuverType = "arrive"
                    )
                )
            )
        }

        val coordString = String.format(
            Locale.US,
            "%.6f,%.6f;%.6f,%.6f",
            startLng, startLat, destLng, destLat
        )
        val url = "$baseUrl$coordString?overview=full&geometries=geojson&steps=true"

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "CoxBazarTourismApp/1.0 (Android; OpenStreetMap & OSRM Routing)")
            .get()
            .build()

        try {
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val code = response.code
                    return@withContext OsrmResult(
                        success = false,
                        errorMessageBn = "সড়ক রুট সংযোগে সমস্যা হয়েছে। অনুগ্রহ করে পুনরায় চেষ্টা করুন।"
                    )
                }

                val responseBody = response.body?.string()
                if (responseBody.isNullOrEmpty()) {
                    return@withContext OsrmResult(
                        success = false,
                        errorMessageBn = "সড়ক রুট সংক্রান্ত তথ্য পাওয়া যায়নি।"
                    )
                }

                val json = JSONObject(responseBody)
                val code = json.optString("code", "")
                if (code != "Ok") {
                    val message = when (code) {
                        "NoRoute" -> "প্রদত্ত অবস্থানের মধ্যে সরাসরি কোনো সড়কপথ পাওয়া যায়নি।"
                        "InvalidInput" -> "স্থানাঙ্কের তথ্য সঠিক নয়।"
                        else -> "রুট হিসাব করা সম্ভব হয়নি। অনুগ্রহ করে পুনরায় চেষ্টা করুন।"
                    }
                    return@withContext OsrmResult(
                        success = false,
                        errorMessageBn = message
                    )
                }

                val routesArray = json.optJSONArray("routes")
                if (routesArray == null || routesArray.length() == 0) {
                    return@withContext OsrmResult(
                        success = false,
                        errorMessageBn = "কোনো রুট পাওয়া যায়নি।"
                    )
                }

                val primaryRoute = routesArray.getJSONObject(0)
                val distanceMeters = primaryRoute.optDouble("distance", 0.0)
                val durationSeconds = primaryRoute.optDouble("duration", 0.0)

                // Parse GeoJSON geometry
                val coordinatesList = mutableListOf<List<Double>>()
                val geometry = primaryRoute.optJSONObject("geometry")
                if (geometry != null) {
                    val coordsArray = geometry.optJSONArray("coordinates")
                    if (coordsArray != null) {
                        for (i in 0 until coordsArray.length()) {
                            val coord = coordsArray.getJSONArray(i)
                            val lng = coord.getDouble(0)
                            val lat = coord.getDouble(1)
                            coordinatesList.add(listOf(lng, lat))
                        }
                    }
                }

                // Parse Legs and Steps
                val stepItems = mutableListOf<RouteStepItem>()
                val legs = primaryRoute.optJSONArray("legs")
                if (legs != null && legs.length() > 0) {
                    val firstLeg = legs.getJSONObject(0)
                    val stepsArray = firstLeg.optJSONArray("steps")
                    if (stepsArray != null) {
                        for (i in 0 until stepsArray.length()) {
                            val step = stepsArray.getJSONObject(i)
                            val stepDist = step.optDouble("distance", 0.0)
                            val stepDur = step.optDouble("duration", 0.0)
                            val name = step.optString("name", "").trim()
                            val maneuver = step.optJSONObject("maneuver")
                            val manType = maneuver?.optString("type", "turn") ?: "turn"
                            val manModifier = maneuver?.optString("modifier", "") ?: ""

                            val instructionBn = translateInstructionToBn(manType, manModifier, name)
                            val distBn = formatDistanceBn(stepDist)
                            val durBn = formatDurationBn(stepDur)

                            stepItems.add(
                                RouteStepItem(
                                    instructionBn = instructionBn,
                                    streetName = if (name.isNotEmpty()) name else "অজ্ঞাত রাস্তা",
                                    distanceTextBn = distBn,
                                    durationTextBn = durBn,
                                    maneuverType = manType,
                                    modifier = manModifier,
                                    distanceMeters = stepDist,
                                    durationSeconds = stepDur
                                )
                            )
                        }
                    }
                }

                val finalSteps = simplifyRouteSteps(stepItems)

                OsrmResult(
                    success = true,
                    distanceMeters = distanceMeters,
                    durationSeconds = durationSeconds,
                    coordinates = coordinatesList,
                    steps = finalSteps
                )
            }
        } catch (e: IOException) {
            OsrmResult(
                success = false,
                errorMessageBn = "ইন্টারনেট সংযোগ চেক করে পুনরায় চেষ্টা করুন।"
            )
        } catch (e: Exception) {
            OsrmResult(
                success = false,
                errorMessageBn = "রুট লোড করা সম্ভব হয়নি। অনুগ্রহ করে পুনরায় চেষ্টা করুন।"
            )
        }
    }

    fun formatDistanceBn(meters: Double): String {
        return if (meters < 1000) {
            val m = meters.toInt()
            "${toBanglaDigits(m.toString())} মিটার"
        } else {
            val km = meters / 1000.0
            val formatted = String.format(Locale.US, "%.1f", km)
            "${toBanglaDigits(formatted)} কিমি"
        }
    }

    fun formatDurationBn(seconds: Double): String {
        val totalMinutes = (seconds / 60.0).toInt()
        return when {
            totalMinutes < 1 -> "১ মিনিটের কম"
            totalMinutes < 60 -> "${toBanglaDigits(totalMinutes.toString())} মিনিট"
            else -> {
                val hours = totalMinutes / 60
                val mins = totalMinutes % 60
                if (mins == 0) {
                    "${toBanglaDigits(hours.toString())} ঘণ্টা"
                } else {
                    "${toBanglaDigits(hours.toString())} ঘণ্টা ${toBanglaDigits(mins.toString())} মিনিট"
                }
            }
        }
    }

    fun toBanglaDigits(input: String): String {
        val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        val sb = java.lang.StringBuilder()
        for (ch in input) {
            if (ch in '0'..'9') {
                sb.append(banglaDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    private fun translateInstructionToBn(type: String, modifier: String, streetName: String): String {
        val streetSuffix = if (streetName.isNotEmpty()) " ধরে ($streetName)" else ""
        return when (type) {
            "depart" -> "যাত্রা শুরু করুন$streetSuffix"
            "arrive" -> "আপনার গন্তব্যে পৌঁছে গেছেন"
            "turn" -> when (modifier) {
                "right" -> "ডানে মোড় নিন$streetSuffix"
                "left" -> "বামে মোড় নিন$streetSuffix"
                "slight right" -> "হালকা ডানে মোড় নিন$streetSuffix"
                "slight left" -> "হালকা বামে মোড় নিন$streetSuffix"
                "sharp right" -> "তীব্র ডানে ঘুরুন$streetSuffix"
                "sharp left" -> "তীব্র বামে ঘুরুন$streetSuffix"
                "straight" -> "সোজা এগিয়ে চলুন$streetSuffix"
                else -> "মোড় নিন$streetSuffix"
            }
            "new name", "continue" -> "সোজা রাস্তা ধরে এগিয়ে যান$streetSuffix"
            "roundabout", "rotary" -> "গোলচত্বরে প্রবেশ করে এগিয়ে যান$streetSuffix"
            "fork" -> when (modifier) {
                "right" -> "রাস্তার ডানদিকের শাখায় প্রবেশ করুন"
                "left" -> "রাস্তার বামদিকের শাখায় প্রবেশ করুন"
                else -> "রাস্তা নির্বাচন করুন"
            }
            "end of road" -> when (modifier) {
                "right" -> "রাস্তার শেষে ডানে মোড় নিন"
                "left" -> "রাস্তার শেষে বামে মোড় নিন"
                else -> "রাস্তার শেষে সাবধানে চলুন"
            }
            else -> "সামনে এগিয়ে চলুন$streetSuffix"
        }
    }

    /**
     * Simplifies OSRM route steps by consolidating repetitive straight segments
     * on the same road while strictly preserving all essential turns (left, right, fork, roundabout, arrival, departure).
     */
    fun simplifyRouteSteps(rawSteps: List<RouteStepItem>): List<RouteStepItem> {
        if (rawSteps.size <= 3) return rawSteps

        val simplified = mutableListOf<RouteStepItem>()
        var accumulatorDist = 0.0
        var accumulatorDur = 0.0
        var pendingStreet = ""
        var isAccumulatingStraight = false

        fun flushPendingStraight() {
            if (isAccumulatingStraight && accumulatorDist > 0.0) {
                val distBn = formatDistanceBn(accumulatorDist)
                val durBn = formatDurationBn(accumulatorDur)
                val streetLabel = if (pendingStreet.isNotEmpty()) pendingStreet else "সড়ক"
                simplified.add(
                    RouteStepItem(
                        instructionBn = "সোজা রাস্তা ধরে এগিয়ে যান ($streetLabel)",
                        streetName = streetLabel,
                        distanceTextBn = distBn,
                        durationTextBn = durBn,
                        maneuverType = "continue",
                        modifier = "straight",
                        distanceMeters = accumulatorDist,
                        durationSeconds = accumulatorDur
                    )
                )
                accumulatorDist = 0.0
                accumulatorDur = 0.0
                pendingStreet = ""
                isAccumulatingStraight = false
            }
        }

        for (i in rawSteps.indices) {
            val step = rawSteps[i]
            val isFirst = (i == 0)
            val isLast = (i == rawSteps.lastIndex)
            val isCrucialTurn = step.maneuverType in listOf("turn", "fork", "roundabout", "rotary", "u-turn", "end of road") ||
                    step.modifier in listOf("right", "left", "sharp right", "sharp left", "slight right", "slight left", "u-turn") ||
                    step.maneuverType in listOf("depart", "arrive", "ferry")

            if (isFirst || isLast || isCrucialTurn) {
                flushPendingStraight()
                simplified.add(step)
            } else {
                // Non-turn maneuver: continue straight / new name / slight transition
                if (!isAccumulatingStraight) {
                    isAccumulatingStraight = true
                    pendingStreet = if (step.streetName != "অজ্ঞাত রাস্তা") step.streetName else ""
                    accumulatorDist = step.distanceMeters
                    accumulatorDur = step.durationSeconds
                } else {
                    val currentStreet = if (step.streetName != "অজ্ঞাত রাস্তা") step.streetName else ""
                    if (pendingStreet.isEmpty() || currentStreet.isEmpty() || currentStreet == pendingStreet) {
                        accumulatorDist += step.distanceMeters
                        accumulatorDur += step.durationSeconds
                        if (pendingStreet.isEmpty() && currentStreet.isNotEmpty()) {
                            pendingStreet = currentStreet
                        }
                    } else {
                        flushPendingStraight()
                        isAccumulatingStraight = true
                        pendingStreet = currentStreet
                        accumulatorDist = step.distanceMeters
                        accumulatorDur = step.durationSeconds
                    }
                }
            }
        }

        flushPendingStraight()
        return simplified
    }
}
