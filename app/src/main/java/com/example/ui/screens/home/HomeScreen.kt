package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.CoxViewModel
import com.example.viewmodel.Screen

data class QuickAccessItem(
    val titleBn: String,
    val icon: ImageVector,
    val iconTint: Color,
    val bgColor: Color,
    val screen: Screen
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val spots by viewModel.repository.spots.collectAsState()
    val hotels by viewModel.repository.hotels.collectAsState()
    val restaurants by viewModel.repository.restaurants.collectAsState()
    val safetyInfo by viewModel.repository.safetyInfo.collectAsState()
    val emergencies by viewModel.repository.emergencies.collectAsState()
    val favoriteIds by viewModel.repository.favoriteIds.collectAsState()
    val notifications by viewModel.repository.notifications.collectAsState()
    val unreadNotifCount = notifications.count { !it.isRead }

    val quickAccessList = listOf(
        QuickAccessItem("স্পট", Icons.Default.BeachAccess, Color(0xFF007791), OceanPrimaryContainer, Screen.Spots),
        QuickAccessItem("হোটেল", Icons.Default.Hotel, Color(0xFFE65100), SunsetContainer, Screen.Hotels),
        QuickAccessItem("রেস্তোরাঁ", Icons.Default.Restaurant, Color(0xFFC2185B), Color(0xFFFCE4EC), Screen.Restaurants),
        QuickAccessItem("ম্যাপ", Icons.Default.Map, Color(0xFF2E7D32), Color(0xFFE8F5E9), Screen.Map),
        QuickAccessItem("জরুরি", Icons.Default.Emergency, Color(0xFFC62828), Color(0xFFFFEBEE), Screen.Emergency),
        QuickAccessItem("যাতায়াত", Icons.Default.DirectionsBus, Color(0xFF0277BD), Color(0xFFE1F5FE), Screen.Transport),
        QuickAccessItem("সুরক্ষা", Icons.Default.Security, Color(0xFF00695C), Color(0xFFE0F2F1), Screen.BeachSafety),
        QuickAccessItem("আবহাওয়া", Icons.Default.WbSunny, Color(0xFFF57F17), Color(0xFFFFFDE7), Screen.Weather),
        QuickAccessItem("কেনাকাটা", Icons.Default.ShoppingBag, Color(0xFF6A1B9A), Color(0xFFF3E5F5), Screen.Shopping),
        QuickAccessItem("নোটিশ", Icons.Default.Article, Color(0xFF455A64), Color(0xFFECEFF1), Screen.News)
    )

    val showAppOverviewSheet by viewModel.showAppOverviewSheet.collectAsState()

    Scaffold(
        topBar = {
            TopGreetingBar(
                unreadNotifCount = unreadNotifCount,
                onOverviewClick = { viewModel.setAppOverviewVisible(true) },
                onSearchClick = { viewModel.navigateTo(Screen.Search) },
                onNotificationClick = { viewModel.navigateTo(Screen.Notifications) },
                onProfileClick = { viewModel.navigateTo(Screen.Profile) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Search Input Trigger
            item {
                Surface(
                    onClick = { viewModel.navigateTo(Screen.Search) },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("home_search_bar")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "হোটেল, বিচ, রেস্তোরাঁ ও স্পট খুঁজুন...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Hero Banner
            item {
                HeroBanner(
                    onExploreClick = { viewModel.navigateTo(Screen.Spots) }
                )
            }

            // App Overview & Guide Featured Card
            item {
                AppOverviewFeaturedCard(
                    onOpenOverview = { viewModel.setAppOverviewVisible(true) },
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }

            // Beach Safety Live Alert Banner
            item {
                BeachSafetyAlertCard(
                    safetyInfo = safetyInfo,
                    onClick = { viewModel.navigateTo(Screen.BeachSafety) }
                )
            }

            // Location Distance & Route System Banner Card
            item {
                HomeRouteQuickCard(
                    onOpenRouteSystem = { viewModel.navigateTo(Screen.Map) },
                    onSelectQuickDestination = { destId -> viewModel.openPlaceDetailsForId(destId) }
                )
            }

            // Quick Access Section
            item {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text(
                        text = "দ্রুত সেবা (Quick Services)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        quickAccessList.forEach { item ->
                            QuickServicePill(item = item, onClick = { viewModel.navigateTo(item.screen) })
                        }
                    }
                }
            }

            // Popular Places Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    titleBn = "জনপ্রিয় পর্যটন স্পট",
                    subtitleEn = "Top Attractions in Cox's Bazar",
                    onSeeAllClick = { viewModel.navigateTo(Screen.Spots) }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(spots.take(6)) { spot ->
                        PopularSpotCard(
                            spot = spot,
                            isFavorite = favoriteIds.contains(spot.id),
                            onSpotClick = { viewModel.selectSpot(spot.id) },
                            onFavoriteToggle = { viewModel.repository.toggleFavorite(spot.id) }
                        )
                    }
                }
            }

            // Popular Hotels Section
            item {
                Spacer(modifier = Modifier.height(20.dp))
                SectionHeader(
                    titleBn = "শীর্ষস্থানীয় হোটেল ও রিসোর্ট",
                    subtitleEn = "Premium Resorts & Stays",
                    onSeeAllClick = { viewModel.navigateTo(Screen.Hotels) }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(hotels.take(5)) { hotel ->
                        PopularHotelCard(
                            hotel = hotel,
                            isFavorite = favoriteIds.contains(hotel.id),
                            onHotelClick = { viewModel.selectHotel(hotel.id) },
                            onFavoriteToggle = { viewModel.repository.toggleFavorite(hotel.id) }
                        )
                    }
                }
            }

            // Popular Restaurants Section
            item {
                Spacer(modifier = Modifier.height(20.dp))
                SectionHeader(
                    titleBn = "সেরা সামুদ্রিক খাবারের রেস্তোরাঁ",
                    subtitleEn = "Famous Seafood & Dining",
                    onSeeAllClick = { viewModel.navigateTo(Screen.Restaurants) }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(restaurants.take(5)) { restaurant ->
                        PopularRestaurantCard(
                            restaurant = restaurant,
                            onRestaurantClick = { viewModel.selectRestaurant(restaurant.id) }
                        )
                    }
                }
            }

            // Emergency Quick Dial Section
            item {
                Spacer(modifier = Modifier.height(20.dp))
                SectionHeader(
                    titleBn = "জরুরি সহায়তা সেবা",
                    subtitleEn = "Emergency & Help Desk",
                    onSeeAllClick = { viewModel.navigateTo(Screen.Emergency) }
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    emergencies.take(3).forEach { emergency ->
                        EmergencyQuickRow(
                            service = emergency,
                            onCallClick = { dialNumber(context, emergency.phone) }
                        )
                    }
                }
            }
        }

        if (showAppOverviewSheet) {
            AppOverviewBottomSheet(
                onDismissRequest = { viewModel.setAppOverviewVisible(false) },
                onNavigate = { screen -> viewModel.navigateTo(screen) }
            )
        }
    }
}

@Composable
fun AppOverviewFeaturedCard(
    onOpenOverview: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = Brush.horizontalGradient(listOf(OceanPrimary.copy(alpha = 0.4f), SunsetAmber.copy(alpha = 0.4f)))
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("home_overview_featured_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = OceanPrimary,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Dashboard,
                                contentDescription = "Overview",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "অ্যাপ ওভারভিউ ও পূর্ণাঙ্গ গাইড",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "১০টি গুরুত্বপূর্ণ পর্যটন ফিচার এক নজরে",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                FilledTonalButton(
                    onClick = onOpenOverview,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = OceanPrimary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("open_overview_sheet_btn")
                ) {
                    Text("সব দেখুন", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick chips for top 4 sections
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Triple("বিচ ও স্পট", Icons.Default.BeachAccess, Screen.Spots),
                    Triple("হোটেল", Icons.Default.Hotel, Screen.Hotels),
                    Triple("সি-ফুড", Icons.Default.Restaurant, Screen.Restaurants),
                    Triple("নিরাপত্তা", Icons.Default.Security, Screen.BeachSafety)
                ).forEach { (label, icon, target) ->
                    Surface(
                        onClick = { onNavigate(target) },
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                        ) {
                            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = OceanPrimary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopGreetingBar(
    unreadNotifCount: Int,
    onOverviewClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = OceanPrimary,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "স্বাগতম কক্সবাজার!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "বিশ্বের দীর্ঘতম সমুদ্র সৈকত",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // App Overview quick button
            FilledTonalButton(
                onClick = onOverviewClick,
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = OceanPrimary.copy(alpha = 0.12f),
                    contentColor = OceanPrimary
                ),
                modifier = Modifier.testTag("home_overview_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Dashboard,
                    contentDescription = "ওভারভিউ",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("ওভারভিউ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            // Notification with badge
            BadgedBox(
                badge = {
                    if (unreadNotifCount > 0) {
                        Badge(containerColor = CoralRed) {
                            Text(text = "$unreadNotifCount")
                        }
                    }
                }
            ) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.testTag("home_notif_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Profile Avatar Button
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier.testTag("home_profile_button")
            ) {
                Surface(
                    shape = CircleShape,
                    color = SunsetAmber.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = SunsetAmber,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeroBanner(
    onExploreClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("home_hero_banner")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AppAsyncImage(
                url = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
                contentDescription = "Cox's Bazar Beach",
                modifier = Modifier.fillMaxSize()
            )
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Surface(
                    color = SunsetAmber,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "এক্সক্লুসিভ ভ্রমণ গাইড",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "বিশ্বের দীর্ঘতম সমুদ্র সৈকত এক্সপ্লোর করুন",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onExploreClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanPrimary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("ঘুরে আসুন (Explore)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
fun BeachSafetyAlertCard(
    safetyInfo: BeachSafetyInfo,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (safetyInfo.flag == BeachFlag.RED) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("home_beach_safety_alert")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (safetyInfo.flag == BeachFlag.RED) CoralRed else Color(0xFF2E7D32),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Beach Flag",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "সৈকত আবহ ও নিরাপত্তা সতর্কতা",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (safetyInfo.flag == BeachFlag.RED) CoralRed else Color(0xFF2E7D32)
                )
                Text(
                    text = "${safetyInfo.flagTitleBn} • জোয়ার: ${safetyInfo.highTideTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QuickServicePill(
    item: QuickAccessItem,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = item.bgColor,
        modifier = Modifier
            .width(72.dp)
            .height(84.dp)
            .testTag("quick_${item.titleBn}")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(6.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.titleBn,
                tint = item.iconTint,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.titleBn,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PopularSpotCard(
    spot: TouristSpot,
    isFavorite: Boolean,
    onSpotClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Card(
        onClick = onSpotClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(220.dp)
            .testTag("spot_card_${spot.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                AppAsyncImage(
                    url = spot.imageUrl,
                    contentDescription = spot.nameBn,
                    modifier = Modifier.fillMaxSize()
                )
                FavoriteButton(
                    isFavorite = isFavorite,
                    onToggle = onFavoriteToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
                RatingBadge(
                    rating = spot.rating,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = spot.nameBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = OceanPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = spot.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = spot.descriptionBn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PopularHotelCard(
    hotel: Hotel,
    isFavorite: Boolean,
    onHotelClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Card(
        onClick = onHotelClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(230.dp)
            .testTag("hotel_card_${hotel.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
            ) {
                AppAsyncImage(
                    url = hotel.imageUrl,
                    contentDescription = hotel.nameBn,
                    modifier = Modifier.fillMaxSize()
                )
                FavoriteButton(
                    isFavorite = isFavorite,
                    onToggle = onFavoriteToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
                Surface(
                    color = Color.Black.copy(alpha = 0.65f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "৳${hotel.priceFrom} / রাত",
                        color = Color(0xFFFFD54F),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = hotel.nameBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = hotel.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                RatingBadge(rating = hotel.rating, reviewCount = hotel.reviewCount)
            }
        }
    }
}

@Composable
fun PopularRestaurantCard(
    restaurant: Restaurant,
    onRestaurantClick: () -> Unit
) {
    Card(
        onClick = onRestaurantClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(200.dp)
            .testTag("restaurant_card_${restaurant.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
            ) {
                AppAsyncImage(
                    url = restaurant.imageUrl,
                    contentDescription = restaurant.nameBn,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    color = SunsetAmber,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                ) {
                    Text(
                        text = restaurant.category,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = restaurant.nameBn,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = restaurant.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RatingBadge(rating = restaurant.rating)
                    Text(
                        text = restaurant.priceRange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun EmergencyQuickRow(
    service: EmergencyService,
    onCallClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.nameBn,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = service.phone,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            FilledTonalButton(
                onClick = onCallClick,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = CoralRed.copy(alpha = 0.15f),
                    contentColor = CoralRed
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("কল", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun HomeRouteQuickCard(
    onOpenRouteSystem: () -> Unit,
    onSelectQuickDestination: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("home_route_quick_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = OceanPrimary.copy(alpha = 0.12f),
                    shape = CircleShape,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Route,
                            contentDescription = null,
                            tint = OceanPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "লোকেশন ও রুট ডিরেকশন",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "লাইভ সড়ক দূরত্ব ও সঠিক ভ্রমণ রুট",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "আপনার বর্তমান অবস্থান থেকে সরাসরি দূরত্ব ও সময় হিসাব করুন:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick destination chips
            val quickSpots = listOf(
                "dest_maheshkhali" to "🏝️ মহেশখালী",
                "dest_naniarchara" to "⚓ নানিয়ারছড়া",
                "dest_laboni_beach" to "🏖️ লাবণী বিচ",
                "dest_himchari" to "🌲 হিমছড়ি",
                "dest_inani" to "🪨 ইনানী বিচ",
                "dest_hosp_sadar" to "🏥 সদর হাসপাতাল"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickSpots.forEach { (destId, label) ->
                    SuggestionChip(
                        onClick = { onSelectQuickDestination(destId) },
                        label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onOpenRouteSystem,
                colors = ButtonDefaults.buttonColors(containerColor = OceanPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("সম্পূর্ণ রুট ও দূরত্ব ক্যালকুলেটর খুলুন", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

