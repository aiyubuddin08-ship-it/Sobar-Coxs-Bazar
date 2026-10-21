package com.example.data.model

enum class ContentStatus {
    PUBLISHED,
    DRAFT,
    PENDING,
    HIDDEN
}

enum class BeachFlag {
    GREEN,   // নিরাপদ / Safe
    YELLOW,  // সতর্কতা / Caution
    RED      // বিপদ / Prohibited / Danger
}

data class TouristSpot(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: String, // Beach, Nature, Island, Historical, Family, Adventure, Cultural
    val rating: Float,
    val reviewCount: Int,
    val location: String,
    val distance: String,
    val descriptionBn: String,
    val descriptionEn: String,
    val openingHours: String,
    val visitingDuration: String,
    val thingsToDo: List<String>,
    val safetyInfoBn: String,
    val travelTipsBn: String,
    val nearbyHotels: List<String>,
    val nearbyRestaurants: List<String>,
    val imageUrl: String,
    val galleryUrls: List<String>,
    val latitude: Double,
    val longitude: Double,
    val isFavorite: Boolean = false,
    val status: ContentStatus = ContentStatus.PUBLISHED
)

data class Hotel(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val rating: Float,
    val reviewCount: Int,
    val location: String,
    val address: String,
    val priceFrom: Int, // in BDT ৳
    val hotelType: String, // Luxury Resort, Beachfront, Boutique, Budget, Eco Resort
    val amenities: List<String>,
    val descriptionBn: String,
    val roomTypes: List<RoomType>,
    val checkInTime: String,
    val checkOutTime: String,
    val phone: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val galleryUrls: List<String>,
    val isFavorite: Boolean = false,
    val status: ContentStatus = ContentStatus.PUBLISHED,
    val priceVerified: Boolean = true,
    val priceSourceBn: String = "হোটেল কাউন্টার ও ভেরিফাইড রিসেপশন",
    val lastVerifiedDate: String = "অক্টোবর ২০২৪",
    val isSeaView: Boolean = false,
    val isFamilyFriendly: Boolean = true,
    val isBudget: Boolean = false,
    val googlePlaceId: String? = null,
    val googleMapsUrl: String? = null
)

data class TravelAdvisoryItem(
    val id: String,
    val titleBn: String,
    val tipBn: String,
    val categoryBn: String,
    val sourceName: String,
    val sourceUrl: String,
    val lastUpdatedBn: String = "অক্টোবর ২০২৪"
)

data class RoomType(
    val name: String,
    val pricePerNight: Int,
    val capacity: String,
    val bedType: String,
    val hasOceanView: Boolean = false
)

data class Restaurant(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: String, // Bangla, Seafood, Fast Food, Chinese, Indian, Cafe, Local Food
    val rating: Float,
    val reviewCount: Int,
    val priceRange: String, // ৳, ৳৳, ৳৳৳
    val location: String,
    val address: String,
    val openingHours: String,
    val phone: String,
    val descriptionBn: String,
    val popularDishes: List<DishItem>,
    val imageUrl: String,
    val galleryUrls: List<String>,
    val isFavorite: Boolean = false,
    val status: ContentStatus = ContentStatus.PUBLISHED
)

data class DishItem(
    val nameBn: String,
    val nameEn: String,
    val price: Int, // ৳
    val isSpecial: Boolean = false
)

data class Shop(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: String, // Burmese Market, Dry Fish, Local Products, Clothing, Souvenirs, Gifts
    val location: String,
    val descriptionBn: String,
    val openingHours: String,
    val phone: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val status: ContentStatus = ContentStatus.PUBLISHED
)

data class TransportService(
    val id: String,
    val nameBn: String,
    val serviceType: String, // Bus, CNG, Auto-rickshaw, Taxi, Rent-a-car, Tourist vehicle
    val phone: String,
    val location: String,
    val descriptionBn: String,
    val estimatedFare: String, // e.g. "৳১০০ - ৳৩০০"
    val imageUrl: String,
    val status: ContentStatus = ContentStatus.PUBLISHED
)

data class EmergencyService(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: String, // Police, Hospital, Fire Service, Ambulance, Beach Safety, Tourist Police
    val phone: String,
    val location: String,
    val address: String,
    val is24x7: Boolean = true,
    val isHotline: Boolean = false,
    val status: ContentStatus = ContentStatus.PUBLISHED
)

data class NewsArticle(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val category: String, // Tourism, Beach, Local News, Travel Notice, Safety, Events
    val date: String,
    val shortDescriptionBn: String,
    val fullContentBn: String,
    val imageUrl: String,
    val author: String,
    val status: ContentStatus = ContentStatus.PUBLISHED
)

data class TourismEvent(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val date: String,
    val time: String,
    val location: String,
    val descriptionBn: String,
    val category: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val status: ContentStatus = ContentStatus.PUBLISHED
)

data class UserReview(
    val id: String,
    val targetId: String,
    val authorName: String,
    val rating: Int,
    val comment: String,
    val date: String
)

data class AppNotification(
    val id: String,
    val titleBn: String,
    val messageBn: String,
    val category: String, // Safety, Event, Tourism, Alert, General
    val timeAgo: String,
    val isRead: Boolean = false
)

data class BeachSafetyInfo(
    val flag: BeachFlag,
    val flagTitleBn: String,
    val flagDescriptionBn: String,
    val highTideTime: String,
    val lowTideTime: String,
    val tideWarningBn: String,
    val nightSwimmingWarningBn: String,
    val seaConditionBn: String,
    val lifeguardHotline: String,
    val safetyTips: List<String>
)

data class WeatherForecastDay(
    val dayBn: String,
    val dayEn: String,
    val tempMax: String,
    val tempMin: String,
    val conditionBn: String
)

data class WeatherInfo(
    val currentTemp: String,
    val conditionBn: String,
    val conditionEn: String,
    val humidity: String,
    val wind: String,
    val rainChance: String,
    val sunrise: String,
    val sunset: String,
    val forecasts: List<WeatherForecastDay>
)

data class RouteDestination(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: String, // পর্যটন স্পট, দ্বীপ ও নৌপথ, হোটেল, রেস্টুরেন্ট, হাসপাতাল, শপিং, যাতায়াত
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val requiresFerry: Boolean = false,
    val ferryGhatNameBn: String? = null,
    val ferryGhatLat: Double? = null,
    val ferryGhatLng: Double? = null,
    val ferryDurationBn: String? = null,
    val ferryTipsBn: String? = null,
    val source: String = "OpenStreetMap / কক্সবাজার প্রশাসন",
    val verificationStatus: String = "যাচাইকৃত (Verified)",
    val imageUrl: String = "",
    val status: ContentStatus = ContentStatus.PUBLISHED
)

data class StartingPoint(
    val nameBn: String,
    val latitude: Double,
    val longitude: Double,
    val isGps: Boolean = false
)

data class RouteStepItem(
    val instructionBn: String,
    val streetName: String,
    val distanceTextBn: String,
    val durationTextBn: String,
    val maneuverType: String,
    val modifier: String? = null,
    val distanceMeters: Double = 0.0,
    val durationSeconds: Double = 0.0
)

sealed class RouteCalculationState {
    object Idle : RouteCalculationState()
    object Loading : RouteCalculationState()
    data class Success(
        val destination: RouteDestination,
        val origin: StartingPoint,
        val distanceMeters: Double,
        val durationSeconds: Double,
        val distanceFormattedBn: String,
        val durationFormattedBn: String,
        val coordinates: List<List<Double>>, // [[lng, lat], ...]
        val steps: List<RouteStepItem>,
        val requiresFerry: Boolean,
        val ferryGhatName: String?,
        val ferryDurationBn: String?,
        val ferryTipsBn: String?
    ) : RouteCalculationState()
    data class Error(
        val errorMessageBn: String,
        val canRetry: Boolean = true
    ) : RouteCalculationState()
}

data class UnifiedPlaceItem(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val categoryBn: String,
    val itemType: String, // Spot, Hotel, Restaurant, Shop, Transport, Emergency, OsmPlace, RouteDest
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val phone: String? = null,
    val rating: Float? = null,
    val reviewsCount: Int? = null,
    val openingHours: String? = null,
    val priceInfo: String? = null,
    val descriptionBn: String? = null,
    val requiresFerry: Boolean = false,
    val ferryGhatNameBn: String? = null,
    val ferryGhatLat: Double? = null,
    val ferryGhatLng: Double? = null,
    val ferryDurationBn: String? = null,
    val ferryTipsBn: String? = null,
    val source: String = "কক্সবাজার পর্যটন ডেটাবেস",
    val verificationStatus: String = "যাচাইকৃত (Verified)"
)
