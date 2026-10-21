package com.example.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

data class OsmPlaceResult(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val categoryBn: String,
    val itemType: String,
    val displayName: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String? = null,
    val openingHours: String? = null,
    val website: String? = null,
    val requiresFerry: Boolean = false,
    val ferryGhatNameBn: String? = null,
    val ferryGhatLat: Double? = null,
    val ferryGhatLng: Double? = null,
    val ferryDurationBn: String? = null,
    val ferryTipsBn: String? = null,
    val source: String = "OpenStreetMap (Nominatim)",
    val verificationStatus: String = "ওপেনস্ট্রিটম্যাপ পাবলিক ডেটা"
)

object NominatimGeocodingService {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .build()

    // Rate limit: Nominatim allows at most 1 request per second
    @Volatile
    private var lastRequestTime = 0L

    /**
     * Searches OpenStreetMap Nominatim for places matching [query] in Bangladesh,
     * biased towards Cox's Bazar and surrounding coastal region.
     */
    suspend fun searchPlaces(query: String): List<OsmPlaceResult> = withContext(Dispatchers.IO) {
        if (query.trim().length < 2) return@withContext emptyList()

        // Respect Nominatim rate limit: ensure at least 1000ms between calls
        synchronized(this) {
            val now = System.currentTimeMillis()
            val diff = now - lastRequestTime
            if (diff < 1050) {
                try {
                    Thread.sleep(1050 - diff)
                } catch (_: InterruptedException) {
                }
            }
            lastRequestTime = System.currentTimeMillis()
        }

        try {
            val encodedQuery = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8.toString())
            // Cox's Bazar viewbox: minLon=91.6, minLat=20.6, maxLon=92.5, maxLat=22.0
            val url = "https://nominatim.openstreetmap.org/search?" +
                    "q=$encodedQuery" +
                    "&format=json" +
                    "&addressdetails=1" +
                    "&extratags=1" +
                    "&namedetails=1" +
                    "&limit=8" +
                    "&countrycodes=bd" +
                    "&viewbox=91.5,22.0,92.5,20.6"

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "CoxBazarTourGuideApp/1.0 (contact: aiyubuddin08@gmail.com)")
                .header("Accept", "application/json")
                .header("Accept-Language", "bn,en;q=0.8")
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext emptyList()
            }

            val bodyString = response.body?.string() ?: return@withContext emptyList()
            val jsonArray = JSONArray(bodyString)
            val results = mutableListOf<OsmPlaceResult>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val latStr = item.optString("lat")
                val lonStr = item.optString("lon")
                val lat = latStr.toDoubleOrNull() ?: continue
                val lon = lonStr.toDoubleOrNull() ?: continue

                val placeId = item.optString("place_id", i.toString())
                val osmType = item.optString("osm_type", "node")
                val osmId = item.optString("osm_id", placeId)

                val namedetails = item.optJSONObject("namedetails")
                val nameBnRaw = namedetails?.optString("name:bn")
                    ?.takeIf { it.isNotBlank() }
                    ?: item.optString("name").takeIf { it.isNotBlank() }
                    ?: extractShortNameFromDisplay(item.optString("display_name"))

                val nameEnRaw = namedetails?.optString("name:en")
                    ?.takeIf { it.isNotBlank() }
                    ?: item.optString("name")

                val displayName = item.optString("display_name", "")
                val itemClass = item.optString("class", "")
                val itemType = item.optString("type", "")

                val extratags = item.optJSONObject("extratags")
                val phone = extratags?.optString("phone")?.takeIf { it.isNotBlank() }
                    ?: extratags?.optString("contact:phone")?.takeIf { it.isNotBlank() }
                val openingHours = extratags?.optString("opening_hours")?.takeIf { it.isNotBlank() }
                val website = extratags?.optString("website")?.takeIf { it.isNotBlank() }
                    ?: extratags?.optString("contact:website")?.takeIf { it.isNotBlank() }

                val (categoryBn, typeCategory) = mapCategory(itemClass, itemType, displayName)

                // Check if place is located in Maheshkhali / island requiring ferry
                val isMaheshkhali = displayName.contains("Maheshkhali", ignoreCase = true) ||
                        displayName.contains("Moheshkhali", ignoreCase = true) ||
                        displayName.contains("মহেশখালী", ignoreCase = true) ||
                        (lat in 21.48..21.75 && lon in 91.85..91.99)

                val requiresFerry = isMaheshkhali
                val ferryGhatName = if (isMaheshkhali) "কস্তুরী ঘাট / ৬ নং জেটি ঘাট (কক্সবাজার শহর)" else null
                val ferryGhatLat = if (isMaheshkhali) 21.4485 else null
                val ferryGhatLng = if (isMaheshkhali) 91.9720 else null
                val ferryDuration = if (isMaheshkhali) "স্পিডবোটে ১৫-২০ মিনিট, কাঠের ট্রলারে ৫০-৬০ মিনিট" else null
                val ferryTips = if (isMaheshkhali) "মহেশখালী একটি দ্বীপ। সড়কপথে প্রথমে কস্তুরী ঘাট বা নানিয়ারছড়া ঘাটে পৌঁছে সেখান থেকে স্পিডবোট বা ট্রলারে মহেশখালী পৌঁছাতে হবে।" else null

                results.add(
                    OsmPlaceResult(
                        id = "osm_${osmType}_$osmId",
                        nameBn = nameBnRaw,
                        nameEn = nameEnRaw,
                        categoryBn = categoryBn,
                        itemType = typeCategory,
                        displayName = displayName,
                        address = cleanAddress(item.optJSONObject("address"), displayName),
                        latitude = lat,
                        longitude = lon,
                        phone = phone,
                        openingHours = openingHours,
                        website = website,
                        requiresFerry = requiresFerry,
                        ferryGhatNameBn = ferryGhatName,
                        ferryGhatLat = ferryGhatLat,
                        ferryGhatLng = ferryGhatLng,
                        ferryDurationBn = ferryDuration,
                        ferryTipsBn = ferryTips,
                        source = "OpenStreetMap (Nominatim)",
                        verificationStatus = "ওপেনস্ট্রিটম্যাপ পাবলিক ডেটা"
                    )
                )
            }

            results
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun extractShortNameFromDisplay(display: String): String {
        return display.split(",").firstOrNull()?.trim() ?: display
    }

    private fun cleanAddress(addressObj: JSONObject?, fullDisplay: String): String {
        if (addressObj != null) {
            val road = addressObj.optString("road").takeIf { it.isNotBlank() }
            val suburb = addressObj.optString("suburb").takeIf { it.isNotBlank() }
            val town = addressObj.optString("town").takeIf { it.isNotBlank() }
                ?: addressObj.optString("city").takeIf { it.isNotBlank() }
                ?: addressObj.optString("village").takeIf { it.isNotBlank() }
            val parts = listOfNotNull(road, suburb, town)
            if (parts.isNotEmpty()) return parts.joinToString(", ")
        }
        val segments = fullDisplay.split(",")
        return segments.take(3).joinToString(", ").trim()
    }

    private fun mapCategory(osmClass: String, osmType: String, displayName: String): Pair<String, String> {
        return when {
            osmClass == "tourism" && (osmType in listOf("hotel", "motel", "guest_house", "hostel", "apartment")) ->
                "হোটেল ও রিসোর্ট" to "Hotel"

            osmClass == "amenity" && (osmType in listOf("restaurant", "cafe", "fast_food", "food_court", "bar")) ->
                "রেস্তোরাঁ ও ক্যাফে" to "Restaurant"

            osmClass == "amenity" && (osmType in listOf("hospital", "clinic", "doctors", "pharmacy")) ->
                "হাসপাতাল ও চিকিৎসা" to "Emergency"

            osmType == "beach" || displayName.contains("Beach", ignoreCase = true) || displayName.contains("সৈকত") ->
                "সমুদ্রসৈকত" to "Spot"

            osmType in listOf("island", "islet") || displayName.contains("Island", ignoreCase = true) || displayName.contains("দ্বীপ") ->
                "দ্বীপ ও নৌপথ" to "Spot"

            osmClass == "tourism" || osmType in listOf("attraction", "viewpoint", "theme_park", "zoo", "museum") ->
                "পর্যটন স্পট" to "Spot"

            osmClass == "shop" ->
                "শপিং ও মার্কেট" to "Shopping"

            osmClass in listOf("highway", "railway", "aeroway") || osmType in listOf("ferry_terminal", "bus_station", "station") ->
                "যাতায়াত ও টার্মিনাল" to "Transport"

            else ->
                "জায়গা ও আকর্ষণ" to "OsmPlace"
        }
    }
}
