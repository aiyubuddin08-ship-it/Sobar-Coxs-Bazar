package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.datasource.DemoData
import com.example.data.local.converters.Converters
import com.example.data.local.dao.*
import com.example.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TouristSpotEntity::class,
        HotelEntity::class,
        RestaurantEntity::class,
        ShopEntity::class,
        TransportEntity::class,
        EmergencyEntity::class,
        NewsEntity::class,
        EventEntity::class,
        ReviewEntity::class,
        NotificationEntity::class,
        FavoriteEntity::class,
        BeachSafetyEntity::class,
        WeatherEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CoxBazarDatabase : RoomDatabase() {

    abstract fun touristSpotDao(): TouristSpotDao
    abstract fun hotelDao(): HotelDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun shopDao(): ShopDao
    abstract fun transportDao(): TransportDao
    abstract fun emergencyDao(): EmergencyDao
    abstract fun newsDao(): NewsDao
    abstract fun eventDao(): EventDao
    abstract fun reviewDao(): ReviewDao
    abstract fun notificationDao(): NotificationDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun beachSafetyDao(): BeachSafetyDao
    abstract fun weatherDao(): WeatherDao

    /**
     * Seeds the local Room database with rich Cox's Bazar demo tourism data.
     * Can be called safely at any time (uses REPLACE on conflict).
     */
    suspend fun prepopulateDemoData() {
        touristSpotDao().insertSpots(DemoData.spots.map { TouristSpotEntity.fromDomain(it) })
        hotelDao().insertHotels(DemoData.hotels.map { HotelEntity.fromDomain(it) })
        restaurantDao().insertRestaurants(DemoData.restaurants.map { RestaurantEntity.fromDomain(it) })
        shopDao().insertShops(DemoData.shops.map { ShopEntity.fromDomain(it) })
        transportDao().insertTransports(DemoData.transports.map { TransportEntity.fromDomain(it) })
        emergencyDao().insertEmergencies(DemoData.emergencies.map { EmergencyEntity.fromDomain(it) })
        newsDao().insertNewsList(DemoData.news.map { NewsEntity.fromDomain(it) })
        eventDao().insertEvents(DemoData.events.map { EventEntity.fromDomain(it) })
        reviewDao().insertReviews(DemoData.sampleReviews.map { ReviewEntity.fromDomain(it) })
        notificationDao().insertNotifications(DemoData.notifications.map { NotificationEntity.fromDomain(it) })
        
        listOf("spot_1", "spot_4", "hotel_1", "rest_1", "evt_1").forEach { id ->
            favoriteDao().addFavorite(FavoriteEntity(id))
        }

        beachSafetyDao().setBeachSafetyInfo(BeachSafetyEntity.fromDomain(DemoData.beachSafetyInfo))
        weatherDao().setWeatherInfo(WeatherEntity.fromDomain(DemoData.weather))
    }

    companion object {
        @Volatile
        private var INSTANCE: CoxBazarDatabase? = null

        fun getInstance(context: Context): CoxBazarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoxBazarDatabase::class.java,
                    "coxsbazar_tourism.db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.prepopulateDemoData()
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * For unit and integration tests.
         */
        fun createInMemoryDatabase(context: Context): CoxBazarDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                CoxBazarDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()
        }
    }
}
