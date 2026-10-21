package com.example.ui.screens.favorites

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppAsyncImage
import com.example.ui.components.EmptyStateView
import com.example.ui.components.FavoriteButton
import com.example.ui.theme.OceanPrimary
import com.example.viewmodel.CoxViewModel
import com.example.viewmodel.Screen

data class FavoriteDisplayItem(
    val id: String,
    val titleBn: String,
    val subtitle: String,
    val typeBn: String,
    val typeKey: String, // Spot, Hotel, Restaurant, Shop, Event
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val favoriteIds by viewModel.repository.favoriteIds.collectAsState()
    val spots by viewModel.repository.spots.collectAsState()
    val hotels by viewModel.repository.hotels.collectAsState()
    val restaurants by viewModel.repository.restaurants.collectAsState()
    val shops by viewModel.repository.shops.collectAsState()
    val events by viewModel.repository.events.collectAsState()

    var activeTab by remember { mutableStateOf("All") }

    val allFavoriteItems = remember(favoriteIds, spots, hotels, restaurants, shops, events) {
        val list = mutableListOf<FavoriteDisplayItem>()
        spots.filter { favoriteIds.contains(it.id) }.forEach {
            list.add(FavoriteDisplayItem(it.id, it.nameBn, it.location, "স্পট", "Spot", it.imageUrl))
        }
        hotels.filter { favoriteIds.contains(it.id) }.forEach {
            list.add(FavoriteDisplayItem(it.id, it.nameBn, "৳${it.priceFrom} হতে • ${it.location}", "হোটেল", "Hotel", it.imageUrl))
        }
        restaurants.filter { favoriteIds.contains(it.id) }.forEach {
            list.add(FavoriteDisplayItem(it.id, it.nameBn, "${it.category} • ${it.location}", "রেস্তোরাঁ", "Restaurant", it.imageUrl))
        }
        shops.filter { favoriteIds.contains(it.id) }.forEach {
            list.add(FavoriteDisplayItem(it.id, it.nameBn, it.location, "শপ", "Shop", it.imageUrl))
        }
        events.filter { favoriteIds.contains(it.id) }.forEach {
            list.add(FavoriteDisplayItem(it.id, it.nameBn, it.date, "ইভেন্ট", "Event", it.imageUrl))
        }
        list
    }

    val tabs = listOf(
        "All" to "সকল সংরক্ষিত",
        "Spot" to "স্পট",
        "Hotel" to "হোটেল",
        "Restaurant" to "রেস্তোরাঁ",
        "Shop" to "দোকান",
        "Event" to "ইভেন্ট"
    )

    val displayedItems = if (activeTab == "All") allFavoriteItems else allFavoriteItems.filter { it.typeKey == activeTab }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("সংরক্ষিত তালিকা (${allFavoriteItems.size})", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Your Saved Places & Hotels", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEach { (key, label) ->
                    val isSelected = activeTab == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { activeTab = key },
                        label = { Text(label, fontSize = 12.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanPrimary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            if (displayedItems.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Default.FavoriteBorder,
                    titleBn = "কোনো পছন্দের আইটেম সংরক্ষিত নেই",
                    descriptionBn = "যেকোনো স্পট, হোটেল বা রেস্তোরাঁর হার্ট আইকনে ট্যাপ করে সহজেই সংরক্ষণ করুন।",
                    actionText = "স্পট ঘুরে দেখুন",
                    onActionClick = { viewModel.navigateTo(Screen.Spots) }
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(displayedItems, key = { it.id }) { item ->
                        Card(
                            onClick = {
                                when (item.typeKey) {
                                    "Spot" -> viewModel.selectSpot(item.id)
                                    "Hotel" -> viewModel.selectHotel(item.id)
                                    "Restaurant" -> viewModel.selectRestaurant(item.id)
                                    "Shop" -> viewModel.navigateTo(Screen.Shopping)
                                    "Event" -> viewModel.navigateTo(Screen.Events)
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("favorite_item_${item.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AppAsyncImage(
                                    url = item.imageUrl,
                                    contentDescription = item.titleBn,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Surface(
                                        color = OceanPrimary.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = item.typeBn,
                                            color = OceanPrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.titleBn,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = item.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                FavoriteButton(
                                    isFavorite = true,
                                    onToggle = { viewModel.repository.toggleFavorite(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
