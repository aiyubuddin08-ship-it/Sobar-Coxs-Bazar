package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.CustomBottomBar
import com.example.ui.components.PlaceDetailsSheet
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminLoginScreen
import com.example.ui.screens.emergency.EmergencyScreen
import com.example.ui.screens.events.EventsScreen
import com.example.ui.screens.explore.ExploreScreen
import com.example.ui.screens.favorites.FavoritesScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.hotels.HotelDetailScreen
import com.example.ui.screens.hotels.HotelsScreen
import com.example.ui.screens.map.MapScreen
import com.example.ui.screens.news.NewsDetailScreen
import com.example.ui.screens.news.NewsScreen
import com.example.ui.screens.notifications.NotificationsScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.restaurants.RestaurantDetailScreen
import com.example.ui.screens.restaurants.RestaurantsScreen
import com.example.ui.screens.route.RouteSystemScreen
import com.example.ui.screens.safety.BeachSafetyScreen
import com.example.ui.screens.search.GlobalSearchScreen
import com.example.ui.screens.shopping.ShoppingScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.spots.SpotDetailScreen
import com.example.ui.screens.spots.SpotsScreen
import com.example.ui.screens.transport.TransportScreen
import com.example.ui.screens.weather.WeatherScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.CoxViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: CoxViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by viewModel.isDarkMode.collectAsState()
            MyApplicationTheme(darkTheme = isDarkTheme) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: CoxViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    // Handle Android system Back button
    val isRootScreen = currentScreen is Screen.Home || currentScreen is Screen.Splash || currentScreen is Screen.Onboarding
    BackHandler(enabled = !isRootScreen) {
        val navigated = viewModel.navigateBack()
        if (!navigated && currentScreen !is Screen.Home) {
            viewModel.navigateTo(Screen.Home)
        }
    }

    val showBottomBar = currentScreen is Screen.Home ||
            currentScreen is Screen.Explore ||
            currentScreen is Screen.Map ||
            currentScreen is Screen.Favorites ||
            currentScreen is Screen.Profile

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                CustomBottomBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier.padding(innerPadding),
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                is Screen.Splash -> SplashScreen(onContinue = { viewModel.navigateTo(Screen.Home) })
                is Screen.Onboarding -> OnboardingScreen(onFinish = { viewModel.navigateTo(Screen.Home) })
                is Screen.Home -> HomeScreen(viewModel = viewModel)
                is Screen.Spots -> SpotsScreen(viewModel = viewModel)
                is Screen.SpotDetail -> SpotDetailScreen(spotId = screen.spotId, viewModel = viewModel)
                is Screen.Hotels -> HotelsScreen(viewModel = viewModel)
                is Screen.HotelDetail -> HotelDetailScreen(hotelId = screen.hotelId, viewModel = viewModel)
                is Screen.Restaurants -> RestaurantsScreen(viewModel = viewModel)
                is Screen.RestaurantDetail -> RestaurantDetailScreen(restaurantId = screen.restaurantId, viewModel = viewModel)
                is Screen.Shopping -> ShoppingScreen(viewModel = viewModel)
                is Screen.Transport -> TransportScreen(viewModel = viewModel)
                is Screen.Map -> MapScreen(viewModel = viewModel)
                is Screen.Emergency -> EmergencyScreen(viewModel = viewModel)
                is Screen.BeachSafety -> BeachSafetyScreen(viewModel = viewModel)
                is Screen.Weather -> WeatherScreen(viewModel = viewModel)
                is Screen.News -> NewsScreen(viewModel = viewModel)
                is Screen.NewsDetail -> NewsDetailScreen(newsId = screen.newsId, viewModel = viewModel)
                is Screen.Events -> EventsScreen(viewModel = viewModel)
                is Screen.Explore -> ExploreScreen(viewModel = viewModel)
                is Screen.Search -> GlobalSearchScreen(viewModel = viewModel)
                is Screen.Favorites -> FavoritesScreen(viewModel = viewModel)
                is Screen.Profile -> ProfileScreen(viewModel = viewModel)
                is Screen.Notifications -> NotificationsScreen(viewModel = viewModel)
                is Screen.Settings -> ProfileScreen(viewModel = viewModel)
                is Screen.AdminLogin -> AdminLoginScreen(viewModel = viewModel)
                is Screen.AdminDashboard -> AdminDashboardScreen(viewModel = viewModel)
                is Screen.RouteDetails -> RouteSystemScreen(viewModel = viewModel)
            }
        }
    }

    val selectedUnifiedPlace by viewModel.selectedUnifiedPlace.collectAsState()

    selectedUnifiedPlace?.let { place ->
        PlaceDetailsSheet(
            place = place,
            viewModel = viewModel,
            onDismiss = { viewModel.closeUnifiedPlace() }
        )
    }
}
