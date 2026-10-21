package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.CoxBazarDatabase
import com.example.data.local.converters.Converters
import com.example.data.local.entity.FavoriteEntity
import com.example.data.local.entity.TouristSpotEntity
import com.example.data.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class RoomDatabaseTest {

    private lateinit var db: CoxBazarDatabase
    private val converters = Converters()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = CoxBazarDatabase.createInMemoryDatabase(context)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testTypeConverters() {
        // String list
        val strings = listOf("Wave", "Sunset", "Beach")
        val jsonString = converters.fromStringList(strings)
        val deserializedStrings = converters.toStringList(jsonString)
        assertEquals(strings, deserializedStrings)

        // ContentStatus
        val status = ContentStatus.DRAFT
        val statusStr = converters.fromContentStatus(status)
        assertEquals("DRAFT", statusStr)
        assertEquals(ContentStatus.DRAFT, converters.toContentStatus(statusStr))

        // BeachFlag
        val flag = BeachFlag.RED
        val flagStr = converters.fromBeachFlag(flag)
        assertEquals("RED", flagStr)
        assertEquals(BeachFlag.RED, converters.toBeachFlag(flagStr))

        // RoomType list
        val roomTypes = listOf(
            RoomType(name = "Deluxe Sea View", pricePerNight = 8500, capacity = "২ জন", bedType = "কিং বেড", hasOceanView = true)
        )
        val roomsJson = converters.fromRoomTypeList(roomTypes)
        val deserializedRooms = converters.toRoomTypeList(roomsJson)
        assertEquals(1, deserializedRooms.size)
        assertEquals("Deluxe Sea View", deserializedRooms[0].name)
        assertTrue(deserializedRooms[0].hasOceanView)

        // DishItem list
        val dishes = listOf(
            DishItem(nameBn = "রূপচাঁদা ফ্রাই", nameEn = "Fried Pomfret", price = 650, isSpecial = true)
        )
        val dishesJson = converters.fromDishItemList(dishes)
        val deserializedDishes = converters.toDishItemList(dishesJson)
        assertEquals(1, deserializedDishes.size)
        assertEquals("রূপচাঁদা ফ্রাই", deserializedDishes[0].nameBn)
        assertTrue(deserializedDishes[0].isSpecial)

        // WeatherForecastDay list
        val forecasts = listOf(
            WeatherForecastDay(dayBn = "আজ", dayEn = "Today", tempMax = "৩২°", tempMin = "২৫°", conditionBn = "রৌদ্রোজ্জ্বল")
        )
        val forecastsJson = converters.fromWeatherForecastList(forecasts)
        val deserializedForecasts = converters.toWeatherForecastList(forecastsJson)
        assertEquals(1, deserializedForecasts.size)
        assertEquals("আজ", deserializedForecasts[0].dayBn)
    }

    @Test
    fun testPrepopulateAndRetrieveDemoTourismData() = runBlocking {
        // Database is initially empty
        assertEquals(0, db.touristSpotDao().getSpotCount())
        assertEquals(0, db.hotelDao().getHotelCount())
        assertEquals(0, db.restaurantDao().getRestaurantCount())

        // Seed demo data
        db.prepopulateDemoData()

        // Verify counts
        val spotCount = db.touristSpotDao().getSpotCount()
        assertTrue("Spot count should be at least 10", spotCount >= 10)

        val hotelCount = db.hotelDao().getHotelCount()
        assertTrue("Hotel count should be at least 10", hotelCount >= 10)

        val restaurantCount = db.restaurantDao().getRestaurantCount()
        assertTrue("Restaurant count should be at least 10", restaurantCount >= 10)

        val shopCount = db.shopDao().getShopCount()
        assertTrue("Shop count should be at least 8", shopCount >= 8)

        val transportCount = db.transportDao().getTransportCount()
        assertTrue("Transport count should be at least 8", transportCount >= 8)

        val emergencyCount = db.emergencyDao().getEmergencyCount()
        assertTrue("Emergency count should be at least 8", emergencyCount >= 8)

        val newsCount = db.newsDao().getNewsCount()
        assertTrue("News count should be at least 10", newsCount >= 10)

        val eventCount = db.eventDao().getEventCount()
        assertTrue("Event count should be at least 8", eventCount >= 8)

        val reviewCount = db.reviewDao().getReviewCount()
        assertTrue("Review count should be positive", reviewCount > 0)

        val notifCount = db.notificationDao().getNotificationCount()
        assertTrue("Notification count should be positive", notifCount > 0)

        // Retrieve and test Flow queries
        val spots = db.touristSpotDao().getAllSpots().first()
        assertFalse(spots.isEmpty())
        val firstSpot = spots[0].toDomain()
        assertNotNull(firstSpot.id)
        assertNotNull(firstSpot.nameBn)

        // Test Category filtering
        val beachSpots = db.touristSpotDao().getSpotsByCategory("Beach").first()
        assertTrue(beachSpots.isNotEmpty())

        // Test Favorites DAO
        val favoriteIds = db.favoriteDao().getAllFavoriteIds().first()
        assertTrue(favoriteIds.contains("spot_1"))
        val isSpot1Fav = db.favoriteDao().isFavorite("spot_1").first()
        assertTrue(isSpot1Fav)

        // Test Safety info retrieval
        val safety = db.beachSafetyDao().getBeachSafetyInfo().first()
        assertNotNull(safety)
        assertEquals(BeachFlag.GREEN, safety?.flag)

        // Test Weather retrieval
        val weather = db.weatherDao().getWeatherInfo().first()
        assertNotNull(weather)
        assertEquals("২৯° সে.", weather?.currentTemp)
    }

    @Test
    fun testSpotCrudOperations() = runBlocking {
        val testSpot = TouristSpotEntity(
            id = "test_custom_spot",
            nameBn = "হিমছড়ি ঝর্ণা ভিউ",
            nameEn = "Himchari Waterfall View",
            category = "Nature",
            rating = 4.9f,
            reviewCount = 120,
            location = "হিমছড়ি",
            distance = "১২ কিমি",
            descriptionBn = "অপূর্ব ঝর্ণা",
            descriptionEn = "Beautiful waterfall",
            openingHours = "সকাল ৮টা - বিকাল ৫টা",
            visitingDuration = "২ ঘণ্টা",
            thingsToDo = listOf("ঝর্ণা দেখা", "ছবি তোলা"),
            safetyInfoBn = "পিচ্ছিল পাথর",
            travelTipsBn = "সকালে যান",
            nearbyHotels = listOf("মারমেইড ইকো রিসোর্ট"),
            nearbyRestaurants = listOf("হিমছড়ি রেস্তোরাঁ"),
            imageUrl = "https://example.com/img.jpg",
            galleryUrls = listOf("https://example.com/img.jpg"),
            latitude = 21.35,
            longitude = 92.02,
            isFavorite = false,
            status = ContentStatus.PUBLISHED
        )

        // Insert
        db.touristSpotDao().insertSpot(testSpot)
        val retrieved = db.touristSpotDao().getSpotByIdSync("test_custom_spot")
        assertNotNull(retrieved)
        assertEquals("হিমছড়ি ঝর্ণা ভিউ", retrieved?.nameBn)

        // Update status
        db.touristSpotDao().updateSpotStatus("test_custom_spot", ContentStatus.DRAFT)
        val updated = db.touristSpotDao().getSpotByIdSync("test_custom_spot")
        assertEquals(ContentStatus.DRAFT, updated?.status)

        // Update favorite
        db.touristSpotDao().updateSpotFavorite("test_custom_spot", true)
        val favUpdated = db.touristSpotDao().getSpotByIdSync("test_custom_spot")
        assertTrue(favUpdated?.isFavorite == true)

        // Delete
        db.touristSpotDao().deleteSpotById("test_custom_spot")
        val deleted = db.touristSpotDao().getSpotByIdSync("test_custom_spot")
        assertNull(deleted)
    }

    @Test
    fun testFavoriteToggleInDao() = runBlocking {
        assertFalse(db.favoriteDao().isFavorite("spot_test_999").first())
        db.favoriteDao().addFavorite(FavoriteEntity("spot_test_999"))
        assertTrue(db.favoriteDao().isFavorite("spot_test_999").first())
        db.favoriteDao().removeFavorite("spot_test_999")
        assertFalse(db.favoriteDao().isFavorite("spot_test_999").first())
    }
}
