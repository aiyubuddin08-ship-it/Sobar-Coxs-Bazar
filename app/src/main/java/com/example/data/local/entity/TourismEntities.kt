package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.*

@Entity(tableName = "tourist_spots")
data class TouristSpotEntity(
    @PrimaryKey val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: String,
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
) {
    fun toDomain() = TouristSpot(
        id = id,
        nameBn = nameBn,
        nameEn = nameEn,
        category = category,
        rating = rating,
        reviewCount = reviewCount,
        location = location,
        distance = distance,
        descriptionBn = descriptionBn,
        descriptionEn = descriptionEn,
        openingHours = openingHours,
        visitingDuration = visitingDuration,
        thingsToDo = thingsToDo,
        safetyInfoBn = safetyInfoBn,
        travelTipsBn = travelTipsBn,
        nearbyHotels = nearbyHotels,
        nearbyRestaurants = nearbyRestaurants,
        imageUrl = imageUrl,
        galleryUrls = galleryUrls,
        latitude = latitude,
        longitude = longitude,
        isFavorite = isFavorite,
        status = status
    )

    companion object {
        fun fromDomain(spot: TouristSpot) = TouristSpotEntity(
            id = spot.id,
            nameBn = spot.nameBn,
            nameEn = spot.nameEn,
            category = spot.category,
            rating = spot.rating,
            reviewCount = spot.reviewCount,
            location = spot.location,
            distance = spot.distance,
            descriptionBn = spot.descriptionBn,
            descriptionEn = spot.descriptionEn,
            openingHours = spot.openingHours,
            visitingDuration = spot.visitingDuration,
            thingsToDo = spot.thingsToDo,
            safetyInfoBn = spot.safetyInfoBn,
            travelTipsBn = spot.travelTipsBn,
            nearbyHotels = spot.nearbyHotels,
            nearbyRestaurants = spot.nearbyRestaurants,
            imageUrl = spot.imageUrl,
            galleryUrls = spot.galleryUrls,
            latitude = spot.latitude,
            longitude = spot.longitude,
            isFavorite = spot.isFavorite,
            status = spot.status
        )
    }
}

@Entity(tableName = "hotels")
data class HotelEntity(
    @PrimaryKey val id: String,
    val nameBn: String,
    val nameEn: String,
    val rating: Float,
    val reviewCount: Int,
    val location: String,
    val address: String,
    val priceFrom: Int,
    val hotelType: String,
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
    val status: ContentStatus = ContentStatus.PUBLISHED
) {
    fun toDomain() = Hotel(
        id = id,
        nameBn = nameBn,
        nameEn = nameEn,
        rating = rating,
        reviewCount = reviewCount,
        location = location,
        address = address,
        priceFrom = priceFrom,
        hotelType = hotelType,
        amenities = amenities,
        descriptionBn = descriptionBn,
        roomTypes = roomTypes,
        checkInTime = checkInTime,
        checkOutTime = checkOutTime,
        phone = phone,
        latitude = latitude,
        longitude = longitude,
        imageUrl = imageUrl,
        galleryUrls = galleryUrls,
        isFavorite = isFavorite,
        status = status
    )

    companion object {
        fun fromDomain(hotel: Hotel) = HotelEntity(
            id = hotel.id,
            nameBn = hotel.nameBn,
            nameEn = hotel.nameEn,
            rating = hotel.rating,
            reviewCount = hotel.reviewCount,
            location = hotel.location,
            address = hotel.address,
            priceFrom = hotel.priceFrom,
            hotelType = hotel.hotelType,
            amenities = hotel.amenities,
            descriptionBn = hotel.descriptionBn,
            roomTypes = hotel.roomTypes,
            checkInTime = hotel.checkInTime,
            checkOutTime = hotel.checkOutTime,
            phone = hotel.phone,
            latitude = hotel.latitude,
            longitude = hotel.longitude,
            imageUrl = hotel.imageUrl,
            galleryUrls = hotel.galleryUrls,
            isFavorite = hotel.isFavorite,
            status = hotel.status
        )
    }
}

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: String,
    val rating: Float,
    val reviewCount: Int,
    val priceRange: String,
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
) {
    fun toDomain() = Restaurant(
        id = id,
        nameBn = nameBn,
        nameEn = nameEn,
        category = category,
        rating = rating,
        reviewCount = reviewCount,
        priceRange = priceRange,
        location = location,
        address = address,
        openingHours = openingHours,
        phone = phone,
        descriptionBn = descriptionBn,
        popularDishes = popularDishes,
        imageUrl = imageUrl,
        galleryUrls = galleryUrls,
        isFavorite = isFavorite,
        status = status
    )

    companion object {
        fun fromDomain(restaurant: Restaurant) = RestaurantEntity(
            id = restaurant.id,
            nameBn = restaurant.nameBn,
            nameEn = restaurant.nameEn,
            category = restaurant.category,
            rating = restaurant.rating,
            reviewCount = restaurant.reviewCount,
            priceRange = restaurant.priceRange,
            location = restaurant.location,
            address = restaurant.address,
            openingHours = restaurant.openingHours,
            phone = restaurant.phone,
            descriptionBn = restaurant.descriptionBn,
            popularDishes = restaurant.popularDishes,
            imageUrl = restaurant.imageUrl,
            galleryUrls = restaurant.galleryUrls,
            isFavorite = restaurant.isFavorite,
            status = restaurant.status
        )
    }
}

@Entity(tableName = "shops")
data class ShopEntity(
    @PrimaryKey val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: String,
    val location: String,
    val descriptionBn: String,
    val openingHours: String,
    val phone: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val status: ContentStatus = ContentStatus.PUBLISHED
) {
    fun toDomain() = Shop(
        id = id,
        nameBn = nameBn,
        nameEn = nameEn,
        category = category,
        location = location,
        descriptionBn = descriptionBn,
        openingHours = openingHours,
        phone = phone,
        imageUrl = imageUrl,
        isFavorite = isFavorite,
        status = status
    )

    companion object {
        fun fromDomain(shop: Shop) = ShopEntity(
            id = shop.id,
            nameBn = shop.nameBn,
            nameEn = shop.nameEn,
            category = shop.category,
            location = shop.location,
            descriptionBn = shop.descriptionBn,
            openingHours = shop.openingHours,
            phone = shop.phone,
            imageUrl = shop.imageUrl,
            isFavorite = shop.isFavorite,
            status = shop.status
        )
    }
}

@Entity(tableName = "transports")
data class TransportEntity(
    @PrimaryKey val id: String,
    val nameBn: String,
    val serviceType: String,
    val phone: String,
    val location: String,
    val descriptionBn: String,
    val estimatedFare: String,
    val imageUrl: String,
    val status: ContentStatus = ContentStatus.PUBLISHED
) {
    fun toDomain() = TransportService(
        id = id,
        nameBn = nameBn,
        serviceType = serviceType,
        phone = phone,
        location = location,
        descriptionBn = descriptionBn,
        estimatedFare = estimatedFare,
        imageUrl = imageUrl,
        status = status
    )

    companion object {
        fun fromDomain(item: TransportService) = TransportEntity(
            id = item.id,
            nameBn = item.nameBn,
            serviceType = item.serviceType,
            phone = item.phone,
            location = item.location,
            descriptionBn = item.descriptionBn,
            estimatedFare = item.estimatedFare,
            imageUrl = item.imageUrl,
            status = item.status
        )
    }
}

@Entity(tableName = "emergencies")
data class EmergencyEntity(
    @PrimaryKey val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: String,
    val phone: String,
    val location: String,
    val address: String,
    val is24x7: Boolean = true,
    val isHotline: Boolean = false,
    val status: ContentStatus = ContentStatus.PUBLISHED
) {
    fun toDomain() = EmergencyService(
        id = id,
        nameBn = nameBn,
        nameEn = nameEn,
        category = category,
        phone = phone,
        location = location,
        address = address,
        is24x7 = is24x7,
        isHotline = isHotline,
        status = status
    )

    companion object {
        fun fromDomain(item: EmergencyService) = EmergencyEntity(
            id = item.id,
            nameBn = item.nameBn,
            nameEn = item.nameEn,
            category = item.category,
            phone = item.phone,
            location = item.location,
            address = item.address,
            is24x7 = item.is24x7,
            isHotline = item.isHotline,
            status = item.status
        )
    }
}

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey val id: String,
    val titleBn: String,
    val titleEn: String,
    val category: String,
    val date: String,
    val shortDescriptionBn: String,
    val fullContentBn: String,
    val imageUrl: String,
    val author: String,
    val status: ContentStatus = ContentStatus.PUBLISHED
) {
    fun toDomain() = NewsArticle(
        id = id,
        titleBn = titleBn,
        titleEn = titleEn,
        category = category,
        date = date,
        shortDescriptionBn = shortDescriptionBn,
        fullContentBn = fullContentBn,
        imageUrl = imageUrl,
        author = author,
        status = status
    )

    companion object {
        fun fromDomain(item: NewsArticle) = NewsEntity(
            id = item.id,
            titleBn = item.titleBn,
            titleEn = item.titleEn,
            category = item.category,
            date = item.date,
            shortDescriptionBn = item.shortDescriptionBn,
            fullContentBn = item.fullContentBn,
            imageUrl = item.imageUrl,
            author = item.author,
            status = item.status
        )
    }
}

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
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
) {
    fun toDomain() = TourismEvent(
        id = id,
        nameBn = nameBn,
        nameEn = nameEn,
        date = date,
        time = time,
        location = location,
        descriptionBn = descriptionBn,
        category = category,
        imageUrl = imageUrl,
        isFavorite = isFavorite,
        status = status
    )

    companion object {
        fun fromDomain(item: TourismEvent) = EventEntity(
            id = item.id,
            nameBn = item.nameBn,
            nameEn = item.nameEn,
            date = item.date,
            time = item.time,
            location = item.location,
            descriptionBn = item.descriptionBn,
            category = item.category,
            imageUrl = item.imageUrl,
            isFavorite = item.isFavorite,
            status = item.status
        )
    }
}

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val targetId: String,
    val authorName: String,
    val rating: Int,
    val comment: String,
    val date: String
) {
    fun toDomain() = UserReview(
        id = id,
        targetId = targetId,
        authorName = authorName,
        rating = rating,
        comment = comment,
        date = date
    )

    companion object {
        fun fromDomain(item: UserReview) = ReviewEntity(
            id = item.id,
            targetId = item.targetId,
            authorName = item.authorName,
            rating = item.rating,
            comment = item.comment,
            date = item.date
        )
    }
}

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val titleBn: String,
    val messageBn: String,
    val category: String,
    val timeAgo: String,
    val isRead: Boolean = false
) {
    fun toDomain() = AppNotification(
        id = id,
        titleBn = titleBn,
        messageBn = messageBn,
        category = category,
        timeAgo = timeAgo,
        isRead = isRead
    )

    companion object {
        fun fromDomain(item: AppNotification) = NotificationEntity(
            id = item.id,
            titleBn = item.titleBn,
            messageBn = item.messageBn,
            category = item.category,
            timeAgo = item.timeAgo,
            isRead = item.isRead
        )
    }
}

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val targetId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "beach_safety")
data class BeachSafetyEntity(
    @PrimaryKey val id: Int = 1,
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
) {
    fun toDomain() = BeachSafetyInfo(
        flag = flag,
        flagTitleBn = flagTitleBn,
        flagDescriptionBn = flagDescriptionBn,
        highTideTime = highTideTime,
        lowTideTime = lowTideTime,
        tideWarningBn = tideWarningBn,
        nightSwimmingWarningBn = nightSwimmingWarningBn,
        seaConditionBn = seaConditionBn,
        lifeguardHotline = lifeguardHotline,
        safetyTips = safetyTips
    )

    companion object {
        fun fromDomain(item: BeachSafetyInfo) = BeachSafetyEntity(
            id = 1,
            flag = item.flag,
            flagTitleBn = item.flagTitleBn,
            flagDescriptionBn = item.flagDescriptionBn,
            highTideTime = item.highTideTime,
            lowTideTime = item.lowTideTime,
            tideWarningBn = item.tideWarningBn,
            nightSwimmingWarningBn = item.nightSwimmingWarningBn,
            seaConditionBn = item.seaConditionBn,
            lifeguardHotline = item.lifeguardHotline,
            safetyTips = item.safetyTips
        )
    }
}

@Entity(tableName = "weather_info")
data class WeatherEntity(
    @PrimaryKey val id: Int = 1,
    val currentTemp: String,
    val conditionBn: String,
    val conditionEn: String,
    val humidity: String,
    val wind: String,
    val rainChance: String,
    val sunrise: String,
    val sunset: String,
    val forecasts: List<WeatherForecastDay>
) {
    fun toDomain() = WeatherInfo(
        currentTemp = currentTemp,
        conditionBn = conditionBn,
        conditionEn = conditionEn,
        humidity = humidity,
        wind = wind,
        rainChance = rainChance,
        sunrise = sunrise,
        sunset = sunset,
        forecasts = forecasts
    )

    companion object {
        fun fromDomain(item: WeatherInfo) = WeatherEntity(
            id = 1,
            currentTemp = item.currentTemp,
            conditionBn = item.conditionBn,
            conditionEn = item.conditionEn,
            humidity = item.humidity,
            wind = item.wind,
            rainChance = item.rainChance,
            sunrise = item.sunrise,
            sunset = item.sunset,
            forecasts = item.forecasts
        )
    }
}
