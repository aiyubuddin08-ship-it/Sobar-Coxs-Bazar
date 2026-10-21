package com.example.data.repository

import com.example.data.datasource.DemoData
import com.example.data.local.CoxBazarDatabase
import com.example.data.local.entity.*
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoxRepository(
    val database: CoxBazarDatabase? = null,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    private val _spots = MutableStateFlow<List<TouristSpot>>(DemoData.spots)
    val spots: StateFlow<List<TouristSpot>> = _spots.asStateFlow()

    private val _hotels = MutableStateFlow<List<Hotel>>(DemoData.hotels)
    val hotels: StateFlow<List<Hotel>> = _hotels.asStateFlow()

    private val _restaurants = MutableStateFlow<List<Restaurant>>(DemoData.restaurants)
    val restaurants: StateFlow<List<Restaurant>> = _restaurants.asStateFlow()

    private val _shops = MutableStateFlow<List<Shop>>(DemoData.shops)
    val shops: StateFlow<List<Shop>> = _shops.asStateFlow()

    private val _transports = MutableStateFlow<List<TransportService>>(DemoData.transports)
    val transports: StateFlow<List<TransportService>> = _transports.asStateFlow()

    private val _emergencies = MutableStateFlow<List<EmergencyService>>(DemoData.emergencies)
    val emergencies: StateFlow<List<EmergencyService>> = _emergencies.asStateFlow()

    private val _news = MutableStateFlow<List<NewsArticle>>(DemoData.news)
    val news: StateFlow<List<NewsArticle>> = _news.asStateFlow()

    private val _events = MutableStateFlow<List<TourismEvent>>(DemoData.events)
    val events: StateFlow<List<TourismEvent>> = _events.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(DemoData.notifications)
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _reviews = MutableStateFlow<List<UserReview>>(DemoData.sampleReviews)
    val reviews: StateFlow<List<UserReview>> = _reviews.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(setOf("spot_1", "spot_4", "hotel_1", "rest_1", "evt_1"))
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _safetyInfo = MutableStateFlow(DemoData.beachSafetyInfo)
    val safetyInfo: StateFlow<BeachSafetyInfo> = _safetyInfo.asStateFlow()

    private val _weather = MutableStateFlow(DemoData.weather)
    val weather: StateFlow<WeatherInfo> = _weather.asStateFlow()

    init {
        if (database != null) {
            observeDatabase(database)
        }
    }

    private fun observeDatabase(db: CoxBazarDatabase) {
        scope.launch {
            // Seed initial data if database is empty
            if (db.touristSpotDao().getSpotCount() == 0) {
                db.prepopulateDemoData()
            }

            // Observe Spots
            launch {
                db.touristSpotDao().getAllSpots().collect { list ->
                    if (list.isNotEmpty()) {
                        _spots.value = list.map { it.toDomain() }
                    }
                }
            }

            // Observe Hotels
            launch {
                db.hotelDao().getAllHotels().collect { list ->
                    if (list.isNotEmpty()) {
                        _hotels.value = list.map { it.toDomain() }
                    }
                }
            }

            // Observe Restaurants
            launch {
                db.restaurantDao().getAllRestaurants().collect { list ->
                    if (list.isNotEmpty()) {
                        _restaurants.value = list.map { it.toDomain() }
                    }
                }
            }

            // Observe Shops
            launch {
                db.shopDao().getAllShops().collect { list ->
                    if (list.isNotEmpty()) {
                        _shops.value = list.map { it.toDomain() }
                    }
                }
            }

            // Observe Transports
            launch {
                db.transportDao().getAllTransports().collect { list ->
                    if (list.isNotEmpty()) {
                        _transports.value = list.map { it.toDomain() }
                    }
                }
            }

            // Observe Emergencies
            launch {
                db.emergencyDao().getAllEmergencies().collect { list ->
                    if (list.isNotEmpty()) {
                        _emergencies.value = list.map { it.toDomain() }
                    }
                }
            }

            // Observe News
            launch {
                db.newsDao().getAllNews().collect { list ->
                    if (list.isNotEmpty()) {
                        _news.value = list.map { it.toDomain() }
                    }
                }
            }

            // Observe Events
            launch {
                db.eventDao().getAllEvents().collect { list ->
                    if (list.isNotEmpty()) {
                        _events.value = list.map { it.toDomain() }
                    }
                }
            }

            // Observe Reviews
            launch {
                db.reviewDao().getAllReviews().collect { list ->
                    if (list.isNotEmpty()) {
                        _reviews.value = list.map { it.toDomain() }
                    }
                }
            }

            // Observe Notifications
            launch {
                db.notificationDao().getAllNotifications().collect { list ->
                    if (list.isNotEmpty()) {
                        _notifications.value = list.map { it.toDomain() }
                    }
                }
            }

            // Observe Favorites
            launch {
                db.favoriteDao().getAllFavoriteIds().collect { ids ->
                    if (ids.isNotEmpty()) {
                        _favoriteIds.value = ids.toSet()
                    }
                }
            }

            // Observe Beach Safety
            launch {
                db.beachSafetyDao().getBeachSafetyInfo().collect { entity ->
                    if (entity != null) {
                        _safetyInfo.value = entity.toDomain()
                    }
                }
            }

            // Observe Weather
            launch {
                db.weatherDao().getWeatherInfo().collect { entity ->
                    if (entity != null) {
                        _weather.value = entity.toDomain()
                    }
                }
            }
        }
    }

    // Favorites
    fun toggleFavorite(id: String) {
        val willBeFav = !_favoriteIds.value.contains(id)
        _favoriteIds.update { current ->
            if (current.contains(id)) current - id else current + id
        }
        database?.let { db ->
            scope.launch {
                if (willBeFav) {
                    db.favoriteDao().addFavorite(FavoriteEntity(id))
                } else {
                    db.favoriteDao().removeFavorite(id)
                }
            }
        }
    }

    fun isFavorite(id: String): Boolean = _favoriteIds.value.contains(id)

    // Reviews
    fun addReview(targetId: String, authorName: String, rating: Int, comment: String) {
        val newReview = UserReview(
            id = "rev_${System.currentTimeMillis()}",
            targetId = targetId,
            authorName = if (authorName.isBlank()) "ভ্রমণকারী" else authorName,
            rating = rating,
            comment = comment,
            date = "এইমাত্র"
        )
        _reviews.update { listOf(newReview) + it }
        database?.let { db ->
            scope.launch {
                db.reviewDao().insertReview(ReviewEntity.fromDomain(newReview))
            }
        }
    }

    fun getReviewsForTarget(targetId: String): List<UserReview> {
        return _reviews.value.filter { it.targetId == targetId }
    }

    // Notifications
    fun markNotificationAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
        database?.let { db ->
            scope.launch {
                db.notificationDao().markAsRead(id)
            }
        }
    }

    fun addNotification(titleBn: String, messageBn: String, category: String) {
        val notif = AppNotification(
            id = "notif_${System.currentTimeMillis()}",
            titleBn = titleBn,
            messageBn = messageBn,
            category = category,
            timeAgo = "এইমাত্র",
            isRead = false
        )
        _notifications.update { listOf(notif) + it }
        database?.let { db ->
            scope.launch {
                db.notificationDao().insertNotification(NotificationEntity.fromDomain(notif))
            }
        }
    }

    // Admin CRUD Operations
    // Hotels
    fun addHotel(hotel: Hotel) {
        _hotels.update { listOf(hotel) + it }
        database?.let { db ->
            scope.launch {
                db.hotelDao().insertHotel(HotelEntity.fromDomain(hotel))
            }
        }
    }

    fun updateHotel(hotel: Hotel) {
        _hotels.update { list -> list.map { if (it.id == hotel.id) hotel else it } }
        database?.let { db ->
            scope.launch {
                db.hotelDao().updateHotel(HotelEntity.fromDomain(hotel))
            }
        }
    }

    fun deleteHotel(hotelId: String) {
        _hotels.update { list -> list.filterNot { it.id == hotelId } }
        database?.let { db ->
            scope.launch {
                db.hotelDao().deleteHotelById(hotelId)
            }
        }
    }

    fun changeHotelStatus(hotelId: String, status: ContentStatus) {
        _hotels.update { list -> list.map { if (it.id == hotelId) it.copy(status = status) else it } }
        database?.let { db ->
            scope.launch {
                db.hotelDao().updateHotelStatus(hotelId, status)
            }
        }
    }

    // Spots
    fun addSpot(spot: TouristSpot) {
        _spots.update { listOf(spot) + it }
        database?.let { db ->
            scope.launch {
                db.touristSpotDao().insertSpot(TouristSpotEntity.fromDomain(spot))
            }
        }
    }

    fun updateSpot(spot: TouristSpot) {
        _spots.update { list -> list.map { if (it.id == spot.id) spot else it } }
        database?.let { db ->
            scope.launch {
                db.touristSpotDao().updateSpot(TouristSpotEntity.fromDomain(spot))
            }
        }
    }

    fun deleteSpot(spotId: String) {
        _spots.update { list -> list.filterNot { it.id == spotId } }
        database?.let { db ->
            scope.launch {
                db.touristSpotDao().deleteSpotById(spotId)
            }
        }
    }

    fun changeSpotStatus(spotId: String, status: ContentStatus) {
        _spots.update { list -> list.map { if (it.id == spotId) it.copy(status = status) else it } }
        database?.let { db ->
            scope.launch {
                db.touristSpotDao().updateSpotStatus(spotId, status)
            }
        }
    }

    // Restaurants
    fun addRestaurant(restaurant: Restaurant) {
        _restaurants.update { listOf(restaurant) + it }
        database?.let { db ->
            scope.launch {
                db.restaurantDao().insertRestaurant(RestaurantEntity.fromDomain(restaurant))
            }
        }
    }

    fun updateRestaurant(restaurant: Restaurant) {
        _restaurants.update { list -> list.map { if (it.id == restaurant.id) restaurant else it } }
        database?.let { db ->
            scope.launch {
                db.restaurantDao().updateRestaurant(RestaurantEntity.fromDomain(restaurant))
            }
        }
    }

    fun deleteRestaurant(restaurantId: String) {
        _restaurants.update { list -> list.filterNot { it.id == restaurantId } }
        database?.let { db ->
            scope.launch {
                db.restaurantDao().deleteRestaurantById(restaurantId)
            }
        }
    }

    fun changeRestaurantStatus(restaurantId: String, status: ContentStatus) {
        _restaurants.update { list -> list.map { if (it.id == restaurantId) it.copy(status = status) else it } }
        database?.let { db ->
            scope.launch {
                db.restaurantDao().updateRestaurantStatus(restaurantId, status)
            }
        }
    }

    // News
    fun addNews(article: NewsArticle) {
        _news.update { listOf(article) + it }
        database?.let { db ->
            scope.launch {
                db.newsDao().insertNews(NewsEntity.fromDomain(article))
            }
        }
    }

    fun updateNews(article: NewsArticle) {
        _news.update { list -> list.map { if (it.id == article.id) article else it } }
        database?.let { db ->
            scope.launch {
                db.newsDao().updateNews(NewsEntity.fromDomain(article))
            }
        }
    }

    fun deleteNews(articleId: String) {
        _news.update { list -> list.filterNot { it.id == articleId } }
        database?.let { db ->
            scope.launch {
                db.newsDao().deleteNewsById(articleId)
            }
        }
    }

    fun changeNewsStatus(articleId: String, status: ContentStatus) {
        _news.update { list -> list.map { if (it.id == articleId) it.copy(status = status) else it } }
        database?.let { db ->
            scope.launch {
                db.newsDao().updateNewsStatus(articleId, status)
            }
        }
    }
}
