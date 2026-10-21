package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.*
import com.example.data.model.ContentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TouristSpotDao {
    @Query("SELECT * FROM tourist_spots ORDER BY rating DESC")
    fun getAllSpots(): Flow<List<TouristSpotEntity>>

    @Query("SELECT * FROM tourist_spots WHERE id = :id")
    fun getSpotById(id: String): Flow<TouristSpotEntity?>

    @Query("SELECT * FROM tourist_spots WHERE id = :id")
    suspend fun getSpotByIdSync(id: String): TouristSpotEntity?

    @Query("SELECT * FROM tourist_spots WHERE category = :category ORDER BY rating DESC")
    fun getSpotsByCategory(category: String): Flow<List<TouristSpotEntity>>

    @Query("SELECT * FROM tourist_spots WHERE nameBn LIKE '%' || :query || '%' OR nameEn LIKE '%' || :query || '%' OR descriptionBn LIKE '%' || :query || '%'")
    fun searchSpots(query: String): Flow<List<TouristSpotEntity>>

    @Query("SELECT COUNT(*) FROM tourist_spots")
    suspend fun getSpotCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpots(spots: List<TouristSpotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpot(spot: TouristSpotEntity)

    @Update
    suspend fun updateSpot(spot: TouristSpotEntity)

    @Query("DELETE FROM tourist_spots WHERE id = :id")
    suspend fun deleteSpotById(id: String)

    @Query("UPDATE tourist_spots SET status = :status WHERE id = :id")
    suspend fun updateSpotStatus(id: String, status: ContentStatus)

    @Query("UPDATE tourist_spots SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateSpotFavorite(id: String, isFavorite: Boolean)

    @Query("DELETE FROM tourist_spots")
    suspend fun clearAllSpots()
}

@Dao
interface HotelDao {
    @Query("SELECT * FROM hotels ORDER BY rating DESC")
    fun getAllHotels(): Flow<List<HotelEntity>>

    @Query("SELECT * FROM hotels WHERE id = :id")
    fun getHotelById(id: String): Flow<HotelEntity?>

    @Query("SELECT * FROM hotels WHERE id = :id")
    suspend fun getHotelByIdSync(id: String): HotelEntity?

    @Query("SELECT * FROM hotels WHERE hotelType = :type ORDER BY rating DESC")
    fun getHotelsByType(type: String): Flow<List<HotelEntity>>

    @Query("SELECT * FROM hotels WHERE nameBn LIKE '%' || :query || '%' OR nameEn LIKE '%' || :query || '%'")
    fun searchHotels(query: String): Flow<List<HotelEntity>>

    @Query("SELECT COUNT(*) FROM hotels")
    suspend fun getHotelCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotels(hotels: List<HotelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotel(hotel: HotelEntity)

    @Update
    suspend fun updateHotel(hotel: HotelEntity)

    @Query("DELETE FROM hotels WHERE id = :id")
    suspend fun deleteHotelById(id: String)

    @Query("UPDATE hotels SET status = :status WHERE id = :id")
    suspend fun updateHotelStatus(id: String, status: ContentStatus)

    @Query("UPDATE hotels SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateHotelFavorite(id: String, isFavorite: Boolean)

    @Query("DELETE FROM hotels")
    suspend fun clearAllHotels()
}

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants ORDER BY rating DESC")
    fun getAllRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    fun getRestaurantById(id: String): Flow<RestaurantEntity?>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantByIdSync(id: String): RestaurantEntity?

    @Query("SELECT * FROM restaurants WHERE category = :category ORDER BY rating DESC")
    fun getRestaurantsByCategory(category: String): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE nameBn LIKE '%' || :query || '%' OR nameEn LIKE '%' || :query || '%'")
    fun searchRestaurants(query: String): Flow<List<RestaurantEntity>>

    @Query("SELECT COUNT(*) FROM restaurants")
    suspend fun getRestaurantCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurants(restaurants: List<RestaurantEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: RestaurantEntity)

    @Update
    suspend fun updateRestaurant(restaurant: RestaurantEntity)

    @Query("DELETE FROM restaurants WHERE id = :id")
    suspend fun deleteRestaurantById(id: String)

    @Query("UPDATE restaurants SET status = :status WHERE id = :id")
    suspend fun updateRestaurantStatus(id: String, status: ContentStatus)

    @Query("UPDATE restaurants SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateRestaurantFavorite(id: String, isFavorite: Boolean)

    @Query("DELETE FROM restaurants")
    suspend fun clearAllRestaurants()
}

@Dao
interface ShopDao {
    @Query("SELECT * FROM shops")
    fun getAllShops(): Flow<List<ShopEntity>>

    @Query("SELECT * FROM shops WHERE id = :id")
    fun getShopById(id: String): Flow<ShopEntity?>

    @Query("SELECT COUNT(*) FROM shops")
    suspend fun getShopCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShops(shops: List<ShopEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: ShopEntity)

    @Query("DELETE FROM shops WHERE id = :id")
    suspend fun deleteShopById(id: String)
}

@Dao
interface TransportDao {
    @Query("SELECT * FROM transports")
    fun getAllTransports(): Flow<List<TransportEntity>>

    @Query("SELECT COUNT(*) FROM transports")
    suspend fun getTransportCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransports(transports: List<TransportEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransport(transport: TransportEntity)

    @Query("DELETE FROM transports WHERE id = :id")
    suspend fun deleteTransportById(id: String)
}

@Dao
interface EmergencyDao {
    @Query("SELECT * FROM emergencies")
    fun getAllEmergencies(): Flow<List<EmergencyEntity>>

    @Query("SELECT * FROM emergencies WHERE category = :category")
    fun getEmergenciesByCategory(category: String): Flow<List<EmergencyEntity>>

    @Query("SELECT COUNT(*) FROM emergencies")
    suspend fun getEmergencyCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencies(emergencies: List<EmergencyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergency(emergency: EmergencyEntity)
}

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE id = :id")
    fun getNewsById(id: String): Flow<NewsEntity?>

    @Query("SELECT COUNT(*) FROM news")
    suspend fun getNewsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsList(newsList: List<NewsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(article: NewsEntity)

    @Update
    suspend fun updateNews(article: NewsEntity)

    @Query("DELETE FROM news WHERE id = :id")
    suspend fun deleteNewsById(id: String)

    @Query("UPDATE news SET status = :status WHERE id = :id")
    suspend fun updateNewsStatus(id: String, status: ContentStatus)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: String): Flow<EventEntity?>

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: String)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE targetId = :targetId ORDER BY rowid DESC")
    fun getReviewsForTarget(targetId: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews ORDER BY rowid DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Query("SELECT COUNT(*) FROM reviews")
    suspend fun getReviewCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Query("DELETE FROM reviews WHERE id = :id")
    suspend fun deleteReviewById(id: String)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY rowid DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getNotificationCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotificationById(id: String)
}

@Dao
interface FavoriteDao {
    @Query("SELECT targetId FROM favorites")
    fun getAllFavoriteIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE targetId = :targetId)")
    fun isFavorite(targetId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE targetId = :targetId")
    suspend fun removeFavorite(targetId: String)
}

@Dao
interface BeachSafetyDao {
    @Query("SELECT * FROM beach_safety WHERE id = 1")
    fun getBeachSafetyInfo(): Flow<BeachSafetyEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setBeachSafetyInfo(info: BeachSafetyEntity)
}

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_info WHERE id = 1")
    fun getWeatherInfo(): Flow<WeatherEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setWeatherInfo(info: WeatherEntity)
}
