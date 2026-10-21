package com.example.viewmodel

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.RouteData
import com.example.data.model.*
import com.example.data.network.CloudflareHotelProxyConfig
import com.example.data.network.CloudflareHotelProxyService
import com.example.data.network.CloudflareRoutingConfig
import com.example.data.network.NominatimGeocodingService
import com.example.data.network.OsmPlaceResult
import com.example.data.network.OsrmRoutingService
import com.example.data.repository.CoxRepository
import com.example.ui.components.LocationTrackingManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Spots : Screen("spots")
    data class SpotDetail(val spotId: String) : Screen("spot_detail/$spotId")
    object Hotels : Screen("hotels")
    data class HotelDetail(val hotelId: String) : Screen("hotel_detail/$hotelId")
    object Restaurants : Screen("restaurants")
    data class RestaurantDetail(val restaurantId: String) : Screen("restaurant_detail/$restaurantId")
    object Shopping : Screen("shopping")
    object Transport : Screen("transport")
    object Map : Screen("map")
    object Emergency : Screen("emergency")
    object BeachSafety : Screen("beach_safety")
    object Weather : Screen("weather")
    object News : Screen("news")
    data class NewsDetail(val newsId: String) : Screen("news_detail/$newsId")
    object Events : Screen("events")
    object Explore : Screen("explore")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
    object AdminLogin : Screen("admin_login")
    object AdminDashboard : Screen("admin_dashboard")
    data class RouteDetails(val destinationId: String? = null) : Screen("route_details")
}

data class SearchResultItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val subtitleBn: String,
    val category: String,
    val itemType: String, // Spot, Hotel, Restaurant, Shop, Transport, Emergency, News, OsmPlace, RouteDest
    val imageUrl: String,
    val rating: Float? = null,
    val unifiedPlace: UnifiedPlaceItem? = null
)

class CoxViewModel(
    val repository: CoxRepository = try {
        com.example.CoxBazarApp.instance.repository
    } catch (e: Throwable) {
        CoxRepository()
    }
) : ViewModel() {

    // Screen navigation stack - launch directly to Home for instant zero-wait startup
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val backStack = mutableListOf<Screen>()

    // App Overview sheet visibility
    private val _showAppOverviewSheet = MutableStateFlow(false)
    val showAppOverviewSheet: StateFlow<Boolean> = _showAppOverviewSheet.asStateFlow()

    fun setAppOverviewVisible(visible: Boolean) {
        _showAppOverviewSheet.value = visible
    }

    // Selected item IDs for detail screens
    private val _selectedSpotId = MutableStateFlow<String?>(null)
    val selectedSpotId: StateFlow<String?> = _selectedSpotId.asStateFlow()

    private val _selectedHotelId = MutableStateFlow<String?>(null)
    val selectedHotelId: StateFlow<String?> = _selectedHotelId.asStateFlow()

    private val _selectedRestaurantId = MutableStateFlow<String?>(null)
    val selectedRestaurantId: StateFlow<String?> = _selectedRestaurantId.asStateFlow()

    private val _selectedNewsId = MutableStateFlow<String?>(null)
    val selectedNewsId: StateFlow<String?> = _selectedNewsId.asStateFlow()

    // Preferences
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isEnglish = MutableStateFlow(false)
    val isEnglish: StateFlow<Boolean> = _isEnglish.asStateFlow()

    // Global Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchCategory = MutableStateFlow("All")
    val searchCategory: StateFlow<String> = _searchCategory.asStateFlow()

    private val _recentSearches = MutableStateFlow<List<String>>(
        listOf("ইনানী বিচ", "রূপচাঁদা ফ্রাই", "সায়মন বিচ রিসোর্ট", "ট্যুরিস্ট পুলিশ")
    )
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    // Category filters
    private val _selectedSpotCategory = MutableStateFlow("All")
    val selectedSpotCategory: StateFlow<String> = _selectedSpotCategory.asStateFlow()

    private val _selectedHotelSort = MutableStateFlow("Default") // Default, PriceLow, PriceHigh, Rating
    val selectedHotelSort: StateFlow<String> = _selectedHotelSort.asStateFlow()

    private val _selectedHotelType = MutableStateFlow("All")
    val selectedHotelType: StateFlow<String> = _selectedHotelType.asStateFlow()

    // --- Tourist Information & Hotel Comparison States ---
    private val _comparisonHotels = MutableStateFlow<List<Hotel>>(emptyList())
    val comparisonHotels: StateFlow<List<Hotel>> = _comparisonHotels.asStateFlow()

    private val _isComparisonSheetVisible = MutableStateFlow(false)
    val isComparisonSheetVisible: StateFlow<Boolean> = _isComparisonSheetVisible.asStateFlow()

    private val _hotelComparisonMessage = MutableStateFlow<String?>(null)
    val hotelComparisonMessage: StateFlow<String?> = _hotelComparisonMessage.asStateFlow()

    private val _hotelProxyStatus = MutableStateFlow<CloudflareHotelProxyService.ProxyFetchStatus>(
        CloudflareHotelProxyService.ProxyFetchStatus.NotConfigured
    )
    val hotelProxyStatus: StateFlow<CloudflareHotelProxyService.ProxyFetchStatus> = _hotelProxyStatus.asStateFlow()

    val travelAdvisories: StateFlow<List<TravelAdvisoryItem>> = MutableStateFlow(CloudflareHotelProxyService.getTravelAdvisories()).asStateFlow()

    fun setSelectedHotelType(type: String) {
        _selectedHotelType.value = type
    }

    fun setSelectedHotelSort(sort: String) {
        _selectedHotelSort.value = sort
    }

    fun toggleHotelComparison(hotel: Hotel) {
        val current = _comparisonHotels.value
        if (current.any { it.id == hotel.id }) {
            _comparisonHotels.value = current.filter { it.id != hotel.id }
        } else {
            if (current.size >= 3) {
                _hotelComparisonMessage.value = "একসাথে সর্বোচ্চ ৩টি হোটেল তুলনা করা সম্ভব।"
            } else {
                _comparisonHotels.value = current + hotel
                _hotelComparisonMessage.value = "${hotel.nameBn} তুলনা তালিকায় যুক্ত হয়েছে।"
            }
        }
    }

    fun removeHotelFromComparison(hotelId: String) {
        _comparisonHotels.value = _comparisonHotels.value.filter { it.id != hotelId }
    }

    fun clearHotelComparison() {
        _comparisonHotels.value = emptyList()
        _isComparisonSheetVisible.value = false
    }

    fun setComparisonSheetVisible(visible: Boolean) {
        _isComparisonSheetVisible.value = visible
    }

    fun clearComparisonMessage() {
        _hotelComparisonMessage.value = null
    }

    fun setCustomHotelProxyUrl(url: String?) {
        CloudflareHotelProxyConfig.setProxyUrl(url)
        refreshHotelsFromProxy()
    }

    fun refreshHotelsFromProxy() {
        viewModelScope.launch {
            _hotelProxyStatus.value = CloudflareHotelProxyService.ProxyFetchStatus.Loading
            val (_, status) = CloudflareHotelProxyService.searchHotels(category = _selectedHotelType.value)
            _hotelProxyStatus.value = status
        }
    }

    private val _selectedRestaurantCategory = MutableStateFlow("All")
    val selectedRestaurantCategory: StateFlow<String> = _selectedRestaurantCategory.asStateFlow()

    // Map screen filter & selected location
    private val _mapCategory = MutableStateFlow("All")
    val mapCategory: StateFlow<String> = _mapCategory.asStateFlow()

    private val _selectedMapMarkerId = MutableStateFlow<String?>("spot_1")
    val selectedMapMarkerId: StateFlow<String?> = _selectedMapMarkerId.asStateFlow()

    // --- Location Distance & Route System States ---
    private val _destinations = MutableStateFlow<List<RouteDestination>>(RouteData.initialDestinations)
    val destinations: StateFlow<List<RouteDestination>> = _destinations.asStateFlow()

    val startingPoints: StateFlow<List<StartingPoint>> = MutableStateFlow(RouteData.verifiedStartingPoints).asStateFlow()

    private val _currentOrigin = MutableStateFlow<StartingPoint>(RouteData.verifiedStartingPoints[0])
    val currentOrigin: StateFlow<StartingPoint> = _currentOrigin.asStateFlow()

    private val _selectedRouteDestination = MutableStateFlow<RouteDestination?>(RouteData.initialDestinations[0])
    val selectedRouteDestination: StateFlow<RouteDestination?> = _selectedRouteDestination.asStateFlow()

    private val _routeCalculationState = MutableStateFlow<RouteCalculationState>(RouteCalculationState.Idle)
    val routeCalculationState: StateFlow<RouteCalculationState> = _routeCalculationState.asStateFlow()

    private val _isLiveNavigationActive = MutableStateFlow(false)
    val isLiveNavigationActive: StateFlow<Boolean> = _isLiveNavigationActive.asStateFlow()

    private val _currentStepIndex = MutableStateFlow(0)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

    private val _gpsStatusMessage = MutableStateFlow<String?>(null)
    val gpsStatusMessage: StateFlow<String?> = _gpsStatusMessage.asStateFlow()

    // --- Automated Place Search & Details State ---
    private val _selectedUnifiedPlace = MutableStateFlow<UnifiedPlaceItem?>(null)
    val selectedUnifiedPlace: StateFlow<UnifiedPlaceItem?> = _selectedUnifiedPlace.asStateFlow()

    private val _placeRouteCalculationState = MutableStateFlow<RouteCalculationState>(RouteCalculationState.Idle)
    val placeRouteCalculationState: StateFlow<RouteCalculationState> = _placeRouteCalculationState.asStateFlow()

    private val _isNominatimSearching = MutableStateFlow(false)
    val isNominatimSearching: StateFlow<Boolean> = _isNominatimSearching.asStateFlow()

    private val _osmSearchResults = MutableStateFlow<List<OsmPlaceResult>>(emptyList())
    val osmSearchResults: StateFlow<List<OsmPlaceResult>> = _osmSearchResults.asStateFlow()

    private var nominatimJob: Job? = null

    fun selectUnifiedPlace(place: UnifiedPlaceItem) {
        _selectedUnifiedPlace.value = place
        calculatePlaceRoute(place, _currentOrigin.value)
    }

    fun closeUnifiedPlace() {
        _selectedUnifiedPlace.value = null
        _placeRouteCalculationState.value = RouteCalculationState.Idle
    }

    fun recalculatePlaceRoute() {
        val place = _selectedUnifiedPlace.value ?: return
        calculatePlaceRoute(place, _currentOrigin.value)
    }

    private fun calculatePlaceRoute(place: UnifiedPlaceItem, origin: StartingPoint) {
        viewModelScope.launch {
            _placeRouteCalculationState.value = RouteCalculationState.Loading

            val targetLat = if (place.requiresFerry && place.ferryGhatLat != null) place.ferryGhatLat else place.latitude
            val targetLng = if (place.requiresFerry && place.ferryGhatLng != null) place.ferryGhatLng else place.longitude

            val result = OsrmRoutingService.getRoute(
                startLat = origin.latitude,
                startLng = origin.longitude,
                destLat = targetLat,
                destLng = targetLng
            )

            if (result.success) {
                val synthesizedDest = convertPlaceToRouteDestination(place)
                _placeRouteCalculationState.value = RouteCalculationState.Success(
                    destination = synthesizedDest,
                    origin = origin,
                    distanceMeters = result.distanceMeters,
                    durationSeconds = result.durationSeconds,
                    distanceFormattedBn = OsrmRoutingService.formatDistanceBn(result.distanceMeters),
                    durationFormattedBn = OsrmRoutingService.formatDurationBn(result.durationSeconds),
                    coordinates = result.coordinates,
                    steps = result.steps,
                    requiresFerry = place.requiresFerry,
                    ferryGhatName = place.ferryGhatNameBn,
                    ferryDurationBn = place.ferryDurationBn,
                    ferryTipsBn = place.ferryTipsBn
                )
            } else {
                _placeRouteCalculationState.value = RouteCalculationState.Error(
                    errorMessageBn = result.errorMessageBn ?: "দূরত্ব ও রুট হিসাব করা যায়নি।"
                )
            }
        }
    }

    // --- Continuous Live GPS Tracking with Auto-pause ---
    private val _isLiveGpsTracking = MutableStateFlow(false)
    val isLiveGpsTracking: StateFlow<Boolean> = _isLiveGpsTracking.asStateFlow()

    private val _liveGpsAccuracy = MutableStateFlow<Float?>(null)
    val liveGpsAccuracy: StateFlow<Float?> = _liveGpsAccuracy.asStateFlow()

    fun startLiveTracking(context: Context) {
        LocationTrackingManager.startTracking(
            context = context,
            onLocationChanged = { lat, lng, acc ->
                _liveGpsAccuracy.value = acc
                _isLiveGpsTracking.value = true
                val current = _currentOrigin.value
                val distDelta = FloatArray(1)
                Location.distanceBetween(current.latitude, current.longitude, lat, lng, distDelta)

                // Only trigger route recalculation if shifted > 10m or if previous origin was not GPS
                if (!current.isGps || distDelta[0] > 10f) {
                    val gpsPoint = StartingPoint(
                        nameBn = "আমার বর্তমান অবস্থান (Live GPS)",
                        latitude = lat,
                        longitude = lng,
                        isGps = true
                    )
                    _currentOrigin.value = gpsPoint
                    _gpsStatusMessage.value = "লাইভ জিপিএস সক্রিয় (নির্ভুলতা: ±${acc.toInt()} মি.)"

                    _selectedUnifiedPlace.value?.let { place ->
                        calculatePlaceRoute(place, gpsPoint)
                    }

                    if (_isLiveNavigationActive.value) {
                        _selectedRouteDestination.value?.let { dest ->
                            calculateRouteFor(gpsPoint, dest)
                        }
                    }
                }
            },
            onError = { err ->
                _isLiveGpsTracking.value = false
                _gpsStatusMessage.value = err
            }
        )
    }

    fun stopLiveTracking() {
        LocationTrackingManager.stopTracking()
        _isLiveGpsTracking.value = false
    }

    // Cloudflare Worker Proxy Support
    fun setCustomCloudflareProxyUrl(url: String?) {
        CloudflareRoutingConfig.setProxyUrl(url)
        calculateRoute()
    }

    val isUsingCloudflareProxy: Boolean
        get() = CloudflareRoutingConfig.isUsingCloudflareProxy

    val activeRoutingUrl: String
        get() = CloudflareRoutingConfig.activeBaseUrl

    fun startNavigationForPlace(place: UnifiedPlaceItem) {
        val dest = convertPlaceToRouteDestination(place)
        openRouteForDestination(dest)
        startLiveNavigation()
    }

    fun openRouteForPlace(place: UnifiedPlaceItem) {
        val dest = convertPlaceToRouteDestination(place)
        openRouteForDestination(dest)
    }

    fun convertPlaceToRouteDestination(place: UnifiedPlaceItem): RouteDestination {
        return RouteDestination(
            id = place.id,
            nameBn = place.nameBn,
            nameEn = place.nameEn,
            category = place.categoryBn,
            address = place.address,
            latitude = place.latitude,
            longitude = place.longitude,
            requiresFerry = place.requiresFerry,
            ferryGhatNameBn = place.ferryGhatNameBn,
            ferryGhatLat = place.ferryGhatLat,
            ferryGhatLng = place.ferryGhatLng,
            ferryDurationBn = place.ferryDurationBn,
            ferryTipsBn = place.ferryTipsBn,
            source = place.source,
            verificationStatus = place.verificationStatus,
            imageUrl = place.imageUrl ?: ""
        )
    }

    fun openPlaceDetailsForId(id: String, itemType: String? = null) {
        val spot = repository.spots.value.find { it.id == id }
        if (spot != null) {
            val requiresFerry = spot.id == "spot_7" || spot.nameBn.contains("মহেশখালী")
            val place = UnifiedPlaceItem(
                id = spot.id,
                nameBn = spot.nameBn,
                nameEn = spot.nameEn,
                categoryBn = spot.category,
                itemType = "Spot",
                address = spot.location,
                latitude = spot.latitude,
                longitude = spot.longitude,
                imageUrl = spot.imageUrl,
                rating = spot.rating,
                priceInfo = if (spot.visitingDuration.isNotBlank()) "সময়: ${spot.visitingDuration}" else null,
                openingHours = spot.openingHours,
                descriptionBn = spot.descriptionBn,
                requiresFerry = requiresFerry,
                ferryGhatNameBn = if (requiresFerry) "কস্তুরী ঘাট / ৬ নং জেটি ঘাট" else null,
                ferryGhatLat = if (requiresFerry) 21.4485 else null,
                ferryGhatLng = if (requiresFerry) 91.9720 else null,
                ferryDurationBn = if (requiresFerry) "স্পিডবোটে ১৫ - ২০ মিনিট (৳১০০ - ৳১২০), কাঠের ট্রলারে ৫০ - ৬০ মিনিট (৳৪০ - ৳৫০)" else null,
                ferryTipsBn = if (requiresFerry) "মহেশখালী একটি দ্বীপ। সড়কপথে প্রথমে কস্তুরী ঘাট বা নানিয়ারছড়া ঘাটে পৌঁছে সেখান থেকে স্পিডবোট বা ট্রলারে মহেশখালী পৌঁছাতে হবে।" else null
            )
            selectUnifiedPlace(place)
            return
        }

        val hotel = repository.hotels.value.find { it.id == id }
        if (hotel != null) {
            val place = UnifiedPlaceItem(
                id = hotel.id,
                nameBn = hotel.nameBn,
                nameEn = hotel.nameEn,
                categoryBn = "হোটেল ও রিসোর্ট",
                itemType = "Hotel",
                address = hotel.location,
                latitude = hotel.latitude,
                longitude = hotel.longitude,
                imageUrl = hotel.imageUrl,
                phone = hotel.phone,
                rating = hotel.rating,
                priceInfo = "৳${hotel.priceFrom} হতে",
                descriptionBn = "চেক-ইন: ${hotel.checkInTime}, চেক-আউট: ${hotel.checkOutTime}। সুবিধাসমূহ: ${hotel.amenities.joinToString(", ")}"
            )
            selectUnifiedPlace(place)
            return
        }

        val rest = repository.restaurants.value.find { it.id == id }
        if (rest != null) {
            val place = UnifiedPlaceItem(
                id = rest.id,
                nameBn = rest.nameBn,
                nameEn = rest.nameEn,
                categoryBn = rest.category,
                itemType = "Restaurant",
                address = rest.location,
                latitude = 21.4310,
                longitude = 91.9790,
                imageUrl = rest.imageUrl,
                phone = rest.phone,
                rating = rest.rating,
                priceInfo = rest.priceRange,
                openingHours = rest.openingHours,
                descriptionBn = if (rest.popularDishes.isNotEmpty()) "জনপ্রিয় খাবার: " + rest.popularDishes.joinToString(", ") { it.nameBn } else rest.descriptionBn
            )
            selectUnifiedPlace(place)
            return
        }

        val dest = _destinations.value.find { it.id == id } ?: RouteData.initialDestinations.find { it.id == id }
        if (dest != null) {
            val place = UnifiedPlaceItem(
                id = dest.id,
                nameBn = dest.nameBn,
                nameEn = dest.nameEn,
                categoryBn = dest.category,
                itemType = "RouteDest",
                address = dest.address,
                latitude = dest.latitude,
                longitude = dest.longitude,
                imageUrl = dest.imageUrl,
                requiresFerry = dest.requiresFerry,
                ferryGhatNameBn = dest.ferryGhatNameBn,
                ferryGhatLat = dest.ferryGhatLat,
                ferryGhatLng = dest.ferryGhatLng,
                ferryDurationBn = dest.ferryDurationBn,
                ferryTipsBn = dest.ferryTipsBn,
                source = dest.source,
                verificationStatus = dest.verificationStatus
            )
            selectUnifiedPlace(place)
            return
        }

        val osm = _osmSearchResults.value.find { it.id == id }
        if (osm != null) {
            val place = UnifiedPlaceItem(
                id = osm.id,
                nameBn = osm.nameBn,
                nameEn = osm.nameEn,
                categoryBn = osm.categoryBn,
                itemType = osm.itemType,
                address = osm.address,
                latitude = osm.latitude,
                longitude = osm.longitude,
                phone = osm.phone,
                openingHours = osm.openingHours,
                requiresFerry = osm.requiresFerry,
                ferryGhatNameBn = osm.ferryGhatNameBn,
                ferryGhatLat = osm.ferryGhatLat,
                ferryGhatLng = osm.ferryGhatLng,
                ferryDurationBn = osm.ferryDurationBn,
                ferryTipsBn = osm.ferryTipsBn,
                source = osm.source,
                verificationStatus = osm.verificationStatus
            )
            selectUnifiedPlace(place)
            return
        }

        // Fallback to route
        openRouteForDestinationId(id)
    }

    fun openRouteForDestination(destination: RouteDestination) {
        _selectedRouteDestination.value = destination
        _isLiveNavigationActive.value = false
        _currentStepIndex.value = 0
        calculateRouteFor(origin = _currentOrigin.value, destination = destination)
        navigateTo(Screen.RouteDetails(destination.id))
    }

    fun openRouteForDestinationId(id: String) {
        val dest = _destinations.value.find { it.id == id } ?: RouteData.initialDestinations.find { it.id == id }
        if (dest != null) {
            openRouteForDestination(dest)
        } else {
            // Check if it's a spot or hotel id
            val spot = repository.spots.value.find { it.id == id }
            if (spot != null) {
                val requiresFerry = spot.id == "spot_7" || spot.nameBn.contains("মহেশখালী")
                val synthesized = RouteDestination(
                    id = spot.id,
                    nameBn = spot.nameBn,
                    nameEn = spot.nameEn,
                    category = "পর্যটন স্পট",
                    address = spot.location,
                    latitude = spot.latitude,
                    longitude = spot.longitude,
                    requiresFerry = requiresFerry,
                    ferryGhatNameBn = if (requiresFerry) "কস্তুরী ঘাট / ৬ নং জেটি ঘাট" else null,
                    ferryGhatLat = if (requiresFerry) 21.4485 else null,
                    ferryGhatLng = if (requiresFerry) 91.9720 else null,
                    ferryDurationBn = if (requiresFerry) "স্পিডবোটে ১৫ - ২০ মিনিট (৳১০০ - ৳১২০), কাঠের ট্রলারে ৫০ - ৬০ মিনিট (৳৪০ - ৳৫০)" else null,
                    ferryTipsBn = if (requiresFerry) "মহেশখালী একটি দ্বীপ। সড়কপথে প্রথমে কস্তুরী ঘাট বা নানিয়ারছড়া ঘাটে পৌঁছে সেখান থেকে স্পিডবোট বা ট্রলারে মহেশখালী পৌঁছাতে হবে।" else null,
                    imageUrl = spot.imageUrl
                )
                openRouteForDestination(synthesized)
            } else {
                val hotel = repository.hotels.value.find { it.id == id }
                if (hotel != null) {
                    val synthesized = RouteDestination(
                        id = hotel.id,
                        nameBn = hotel.nameBn,
                        nameEn = hotel.nameEn,
                        category = "হোটেল ও রিসোর্ট",
                        address = hotel.location,
                        latitude = hotel.latitude,
                        longitude = hotel.longitude,
                        imageUrl = hotel.imageUrl
                    )
                    openRouteForDestination(synthesized)
                }
            }
        }
    }

    fun setStartingPoint(point: StartingPoint) {
        _currentOrigin.value = point
        _selectedRouteDestination.value?.let { dest ->
            calculateRouteFor(origin = point, destination = dest)
        }
        _selectedUnifiedPlace.value?.let { place ->
            calculatePlaceRoute(place, point)
        }
    }

    fun setUserGpsLocation(lat: Double, lng: Double) {
        val gpsPoint = StartingPoint(
            nameBn = "আমার বর্তমান অবস্থান (GPS)",
            latitude = lat,
            longitude = lng,
            isGps = true
        )
        _currentOrigin.value = gpsPoint
        _gpsStatusMessage.value = "জিপিএস অবস্থান সফলভাবে সক্রিয় হয়েছে।"
        _selectedRouteDestination.value?.let { dest ->
            calculateRouteFor(origin = gpsPoint, destination = dest)
        }
        _selectedUnifiedPlace.value?.let { place ->
            calculatePlaceRoute(place, gpsPoint)
        }
    }

    fun calculateRoute() {
        val origin = _currentOrigin.value
        val dest = _selectedRouteDestination.value ?: return
        calculateRouteFor(origin, dest)
    }

    private fun calculateRouteFor(origin: StartingPoint, destination: RouteDestination) {
        viewModelScope.launch {
            _routeCalculationState.value = RouteCalculationState.Loading

            // If destination requires ferry (e.g., Maheshkhali), we route road leg to the verified Ghat
            val targetLat = if (destination.requiresFerry && destination.ferryGhatLat != null) {
                destination.ferryGhatLat
            } else {
                destination.latitude
            }
            val targetLng = if (destination.requiresFerry && destination.ferryGhatLng != null) {
                destination.ferryGhatLng
            } else {
                destination.longitude
            }

            val osrmResult = OsrmRoutingService.getRoute(
                startLat = origin.latitude,
                startLng = origin.longitude,
                destLat = targetLat,
                destLng = targetLng
            )

            if (osrmResult.success) {
                val distFormatted = OsrmRoutingService.formatDistanceBn(osrmResult.distanceMeters)
                val durFormatted = OsrmRoutingService.formatDurationBn(osrmResult.durationSeconds)

                _routeCalculationState.value = RouteCalculationState.Success(
                    destination = destination,
                    origin = origin,
                    distanceMeters = osrmResult.distanceMeters,
                    durationSeconds = osrmResult.durationSeconds,
                    distanceFormattedBn = distFormatted,
                    durationFormattedBn = durFormatted,
                    coordinates = osrmResult.coordinates,
                    steps = osrmResult.steps,
                    requiresFerry = destination.requiresFerry,
                    ferryGhatName = destination.ferryGhatNameBn,
                    ferryDurationBn = destination.ferryDurationBn,
                    ferryTipsBn = destination.ferryTipsBn
                )
            } else {
                _routeCalculationState.value = RouteCalculationState.Error(
                    errorMessageBn = osrmResult.errorMessageBn ?: "রুট হিসাব করতে সমস্যা হয়েছে। অনুগ্রহ করে পুনরায় চেষ্টা করুন।"
                )
            }
        }
    }

    fun startLiveNavigation() {
        _isLiveNavigationActive.value = true
        _currentStepIndex.value = 0
    }

    fun stopLiveNavigation() {
        _isLiveNavigationActive.value = false
        _currentStepIndex.value = 0
    }

    fun nextNavigationStep() {
        val state = _routeCalculationState.value
        if (state is RouteCalculationState.Success) {
            if (_currentStepIndex.value < state.steps.size - 1) {
                _currentStepIndex.value += 1
            }
        }
    }

    fun prevNavigationStep() {
        if (_currentStepIndex.value > 0) {
            _currentStepIndex.value -= 1
        }
    }

    fun addOrUpdateDestination(destination: RouteDestination) {
        _destinations.update { list ->
            val existing = list.indexOfFirst { it.id == destination.id }
            if (existing >= 0) {
                list.toMutableList().apply { set(existing, destination) }
            } else {
                listOf(destination) + list
            }
        }
    }

    fun deleteDestination(id: String) {
        _destinations.update { list -> list.filterNot { it.id == id } }
    }

    // Admin Auth State
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    // Navigation Methods
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            backStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (backStack.isNotEmpty()) {
            _currentScreen.value = backStack.removeAt(backStack.size - 1)
            return true
        }
        return false
    }

    fun selectSpot(id: String) {
        _selectedSpotId.value = id
        navigateTo(Screen.SpotDetail(id))
    }

    fun selectHotel(id: String) {
        _selectedHotelId.value = id
        navigateTo(Screen.HotelDetail(id))
    }

    fun selectRestaurant(id: String) {
        _selectedRestaurantId.value = id
        navigateTo(Screen.RestaurantDetail(id))
    }

    fun selectNews(id: String) {
        _selectedNewsId.value = id
        navigateTo(Screen.NewsDetail(id))
    }

    // Toggle Preferences
    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setDarkMode(dark: Boolean) {
        _isDarkMode.value = dark
    }

    fun toggleLanguage() {
        _isEnglish.value = !_isEnglish.value
    }

    // Search Functions with Debounced Nominatim Geocoding
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        nominatimJob?.cancel()
        if (query.trim().length >= 2) {
            nominatimJob = viewModelScope.launch {
                delay(600)
                _isNominatimSearching.value = true
                val osmResults = NominatimGeocodingService.searchPlaces(query)
                _osmSearchResults.value = osmResults
                _isNominatimSearching.value = false
            }
        } else {
            _osmSearchResults.value = emptyList()
            _isNominatimSearching.value = false
        }
    }

    fun setSearchCategory(category: String) {
        _searchCategory.value = category
    }

    fun addRecentSearch(query: String) {
        if (query.isNotBlank()) {
            _recentSearches.update { list ->
                (listOf(query) + (list - query)).take(8)
            }
        }
    }

    fun clearRecentSearches() {
        _recentSearches.value = emptyList()
    }

    // Spot filter
    fun setSpotCategory(category: String) {
        _selectedSpotCategory.value = category
    }

    // Hotel filter
    fun setHotelSort(sort: String) {
        _selectedHotelSort.value = sort
    }

    fun setHotelType(type: String) {
        _selectedHotelType.value = type
    }

    // Restaurant filter
    fun setRestaurantCategory(category: String) {
        _selectedRestaurantCategory.value = category
    }

    // Map selection
    fun setMapCategory(category: String) {
        _mapCategory.value = category
    }

    fun selectMapMarker(id: String?) {
        _selectedMapMarkerId.value = id
    }

    // Admin Auth
    fun adminLogin(email: String, pass: String): Boolean {
        if ((email.trim().equals("admin@coxsbazar.gov.bd", ignoreCase = true) || email.trim() == "admin") &&
            (pass == "admin123" || pass == "123456")) {
            _isAdminLoggedIn.value = true
            navigateTo(Screen.AdminDashboard)
            return true
        }
        return false
    }

    fun adminLogout() {
        _isAdminLoggedIn.value = false
        navigateTo(Screen.Home)
    }

    // Combined Search Results Flow (Local database + OpenStreetMap Nominatim geocoding)
    val searchResults: StateFlow<List<SearchResultItem>> = combine(
        _searchQuery,
        _searchCategory,
        _osmSearchResults
    ) { query: String, cat: String, osmPlaces: List<OsmPlaceResult> ->
        if (query.isBlank()) return@combine emptyList<SearchResultItem>()

        val q = query.trim().lowercase()
        val results = mutableListOf<SearchResultItem>()

        val spots = repository.spots.value
        val hotels = repository.hotels.value
        val rests = repository.restaurants.value
        val shops = repository.shops.value
        val transports = repository.transports.value
        val emergencies = repository.emergencies.value
        val news = repository.news.value

        if (cat == "All" || cat == "Spots") {
            spots.filter { it.nameBn.lowercase().contains(q) || it.nameEn.lowercase().contains(q) || it.descriptionBn.contains(q) }
                .forEach {
                    val requiresFerry = it.id == "spot_7" || it.nameBn.contains("মহেশখালী")
                    val up = UnifiedPlaceItem(
                        id = it.id,
                        nameBn = it.nameBn,
                        nameEn = it.nameEn,
                        categoryBn = it.category,
                        itemType = "Spot",
                        address = it.location,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        imageUrl = it.imageUrl,
                        rating = it.rating,
                        priceInfo = if (it.visitingDuration.isNotBlank()) "সময়: ${it.visitingDuration}" else null,
                        openingHours = it.openingHours,
                        descriptionBn = it.descriptionBn,
                        requiresFerry = requiresFerry,
                        ferryGhatNameBn = if (requiresFerry) "কস্তুরী ঘাট / ৬ নং জেটি ঘাট" else null,
                        ferryGhatLat = if (requiresFerry) 21.4485 else null,
                        ferryGhatLng = if (requiresFerry) 91.9720 else null,
                        ferryDurationBn = if (requiresFerry) "স্পিডবোটে ১৫ - ২০ মিনিট (৳১০০ - ৳১২০), কাঠের ট্রলারে ৫০ - ৬০ মিনিট (৳৪০ - ৳৫০)" else null,
                        ferryTipsBn = if (requiresFerry) "মহেশখালী একটি দ্বীপ। সড়কপথে প্রথমে কস্তুরী ঘাট বা নানিয়ারছড়া ঘাটে পৌঁছে সেখান থেকে স্পিডবোট বা ট্রলারে মহেশখালী পৌঁছাতে হবে।" else null
                    )
                    results.add(SearchResultItem(it.id, it.nameBn, it.nameEn, it.location, it.category, "Spot", it.imageUrl, it.rating, up))
                }
        }
        if (cat == "All" || cat == "Hotels") {
            hotels.filter { it.nameBn.lowercase().contains(q) || it.nameEn.lowercase().contains(q) || it.location.lowercase().contains(q) }
                .forEach {
                    val up = UnifiedPlaceItem(
                        id = it.id,
                        nameBn = it.nameBn,
                        nameEn = it.nameEn,
                        categoryBn = "হোটেল ও রিসোর্ট",
                        itemType = "Hotel",
                        address = it.location,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        imageUrl = it.imageUrl,
                        phone = it.phone,
                        rating = it.rating,
                        priceInfo = "৳${it.priceFrom} হতে",
                        descriptionBn = "চেক-ইন: ${it.checkInTime}, চেক-আউট: ${it.checkOutTime}। সুবিধাসমূহ: ${it.amenities.joinToString(", ")}"
                    )
                    results.add(SearchResultItem(it.id, it.nameBn, it.nameEn, "৳${it.priceFrom} হতে • ${it.location}", it.hotelType, "Hotel", it.imageUrl, it.rating, up))
                }
        }
        if (cat == "All" || cat == "Restaurants") {
            rests.filter { it.nameBn.lowercase().contains(q) || it.nameEn.lowercase().contains(q) || it.category.lowercase().contains(q) }
                .forEach {
                    val up = UnifiedPlaceItem(
                        id = it.id,
                        nameBn = it.nameBn,
                        nameEn = it.nameEn,
                        categoryBn = it.category,
                        itemType = "Restaurant",
                        address = it.location,
                        latitude = 21.4310,
                        longitude = 91.9790,
                        imageUrl = it.imageUrl,
                        phone = it.phone,
                        rating = it.rating,
                        priceInfo = it.priceRange,
                        openingHours = it.openingHours,
                        descriptionBn = if (it.popularDishes.isNotEmpty()) "জনপ্রিয় খাবার: " + it.popularDishes.joinToString(", ") { d -> d.nameBn } else it.descriptionBn
                    )
                    results.add(SearchResultItem(it.id, it.nameBn, it.nameEn, "${it.category} • ${it.location}", it.priceRange, "Restaurant", it.imageUrl, it.rating, up))
                }
        }
        if (cat == "All" || cat == "Shopping") {
            shops.filter { it.nameBn.lowercase().contains(q) || it.nameEn.lowercase().contains(q) || it.category.lowercase().contains(q) }
                .forEach {
                    val up = UnifiedPlaceItem(
                        id = it.id,
                        nameBn = it.nameBn,
                        nameEn = it.nameEn,
                        categoryBn = it.category,
                        itemType = "Shop",
                        address = it.location,
                        latitude = 21.4330, // Market area coordinates
                        longitude = 91.9780,
                        imageUrl = it.imageUrl,
                        descriptionBn = "কক্সবাজারের সুপরিচিত শপিং স্থান"
                    )
                    results.add(SearchResultItem(it.id, it.nameBn, it.nameEn, it.location, it.category, "Shop", it.imageUrl, null, up))
                }
        }
        if (cat == "All" || cat == "Transport") {
            transports.filter { it.nameBn.lowercase().contains(q) || it.serviceType.lowercase().contains(q) }
                .forEach {
                    val up = UnifiedPlaceItem(
                        id = it.id,
                        nameBn = it.nameBn,
                        nameEn = it.serviceType,
                        categoryBn = it.serviceType,
                        itemType = "Transport",
                        address = "সেন্ট্রাল বাস টার্মিনাল / কক্সবাজার শহর",
                        latitude = 21.4380,
                        longitude = 91.9860,
                        imageUrl = it.imageUrl,
                        priceInfo = it.estimatedFare,
                        descriptionBn = "ভাড়া ও যাতায়াত তথ্য: ${it.estimatedFare}"
                    )
                    results.add(SearchResultItem(it.id, it.nameBn, it.serviceType, it.estimatedFare, it.serviceType, "Transport", it.imageUrl, null, up))
                }
        }
        if (cat == "All" || cat == "Emergency") {
            emergencies.filter { it.nameBn.lowercase().contains(q) || it.nameEn.lowercase().contains(q) || it.category.lowercase().contains(q) }
                .forEach {
                    val up = UnifiedPlaceItem(
                        id = it.id,
                        nameBn = it.nameBn,
                        nameEn = it.nameEn,
                        categoryBn = it.category,
                        itemType = "Emergency",
                        address = "কক্সবাজার সদর",
                        latitude = 21.4350,
                        longitude = 91.9750,
                        phone = it.phone,
                        descriptionBn = "জরুরি সেবা হেল্পলাইন: ${it.phone}"
                    )
                    results.add(SearchResultItem(it.id, it.nameBn, it.nameEn, it.phone, it.category, "Emergency", "", null, up))
                }
        }
        if (cat == "All" || cat == "News") {
            news.filter { it.titleBn.lowercase().contains(q) || it.titleEn.lowercase().contains(q) }
                .forEach {
                    results.add(SearchResultItem(it.id, it.titleBn, it.titleEn, it.date, it.category, "News", it.imageUrl))
                }
        }

        // Search & match verified route destinations (e.g. মহেশখালী, নানিয়ারছড়া, ইত্যাদি)
        _destinations.value.filter {
            it.nameBn.lowercase().contains(q) || it.nameEn.lowercase().contains(q) || it.address.lowercase().contains(q)
        }.forEach {
            if (results.none { res -> res.id == it.id }) {
                val up = UnifiedPlaceItem(
                    id = it.id,
                    nameBn = it.nameBn,
                    nameEn = it.nameEn,
                    categoryBn = it.category,
                    itemType = "RouteDest",
                    address = it.address,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    imageUrl = it.imageUrl,
                    requiresFerry = it.requiresFerry,
                    ferryGhatNameBn = it.ferryGhatNameBn,
                    ferryGhatLat = it.ferryGhatLat,
                    ferryGhatLng = it.ferryGhatLng,
                    ferryDurationBn = it.ferryDurationBn,
                    ferryTipsBn = it.ferryTipsBn,
                    source = it.source,
                    verificationStatus = it.verificationStatus
                )
                results.add(SearchResultItem(it.id, it.nameBn, it.nameEn, "${it.category} • ${it.address}", it.category, "RouteDest", it.imageUrl, null, up))
            }
        }

        // Add OpenStreetMap Nominatim Live Geocoding Results
        osmPlaces.forEach { osm ->
            val isDuplicate = results.any { res ->
                if (res.id == osm.id) return@any true
                val pLat = res.unifiedPlace?.latitude
                val pLng = res.unifiedPlace?.longitude
                if (pLat != null && pLng != null) {
                    Math.abs(pLat - osm.latitude) < 0.001 && Math.abs(pLng - osm.longitude) < 0.001
                } else false
            }
            if (!isDuplicate) {
                val up = UnifiedPlaceItem(
                    id = osm.id,
                    nameBn = osm.nameBn,
                    nameEn = osm.nameEn,
                    categoryBn = osm.categoryBn,
                    itemType = osm.itemType,
                    address = osm.address,
                    latitude = osm.latitude,
                    longitude = osm.longitude,
                    phone = osm.phone,
                    openingHours = osm.openingHours,
                    requiresFerry = osm.requiresFerry,
                    ferryGhatNameBn = osm.ferryGhatNameBn,
                    ferryGhatLat = osm.ferryGhatLat,
                    ferryGhatLng = osm.ferryGhatLng,
                    ferryDurationBn = osm.ferryDurationBn,
                    ferryTipsBn = osm.ferryTipsBn,
                    source = osm.source,
                    verificationStatus = osm.verificationStatus
                )
                results.add(
                    SearchResultItem(
                        id = osm.id,
                        titleBn = osm.nameBn,
                        titleEn = osm.nameEn,
                        subtitleBn = "${osm.categoryBn} • ${osm.address}",
                        category = osm.categoryBn,
                        itemType = "OsmPlace",
                        imageUrl = "",
                        unifiedPlace = up
                    )
                )
            }
        }

        results
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
