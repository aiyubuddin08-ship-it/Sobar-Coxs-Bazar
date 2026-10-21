package com.example.ui.screens.restaurants

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Restaurant
import com.example.ui.components.*
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantsScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val restaurants by viewModel.repository.restaurants.collectAsState()
    val favoriteIds by viewModel.repository.favoriteIds.collectAsState()
    val selectedCategory by viewModel.selectedRestaurantCategory.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf(
        "All" to "সব খাবার",
        "Bangla" to "বাংলা ও ভর্তা",
        "Seafood" to "সি-ফুড (Seafood)",
        "Cafe" to "ক্যাফে ও কফি",
        "Chinese" to "চাইনিজ ও থাই",
        "Indian" to "ইন্ডিয়ান ও বিরিয়ানি",
        "Fast Food" to "ফাস্টফুড",
        "Local Food" to "কাবাব ও স্থানীয়"
    )

    val filteredRestaurants = restaurants.filter { rest ->
        (selectedCategory == "All" || rest.category == selectedCategory) &&
        (searchQuery.isBlank() || rest.nameBn.contains(searchQuery, ignoreCase = true) || rest.location.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("কক্সবাজারের সেরা রেস্তোরাঁ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Famous Seafood & Restaurants", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("রেস্তোরাঁর নাম বা খাবার খুঁজুন...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )

            // Category Filter Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { (catKey, label) ->
                    val isSelected = selectedCategory == catKey
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setRestaurantCategory(catKey) },
                        label = { Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanPrimary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            if (filteredRestaurants.isEmpty()) {
                EmptyStateView(
                    titleBn = "কোনো রেস্তোরাঁ পাওয়া যায়নি",
                    descriptionBn = "অন্য ক্যাটাগরি বা কীওয়ার্ড দিয়ে খুঁজুন।"
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredRestaurants) { rest ->
                        RestaurantListingCard(
                            restaurant = rest,
                            isFavorite = favoriteIds.contains(rest.id),
                            onRestaurantClick = { viewModel.selectRestaurant(rest.id) },
                            onFavoriteToggle = { viewModel.repository.toggleFavorite(rest.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RestaurantListingCard(
    restaurant: Restaurant,
    isFavorite: Boolean,
    onRestaurantClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Card(
        onClick = onRestaurantClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("restaurant_item_${restaurant.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                AppAsyncImage(
                    url = restaurant.imageUrl,
                    contentDescription = restaurant.nameBn,
                    modifier = Modifier.fillMaxSize()
                )
                FavoriteButton(
                    isFavorite = isFavorite,
                    onToggle = onFavoriteToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                )
                Surface(
                    color = SunsetAmber,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {
                    Text(
                        text = restaurant.category,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.nameBn,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    RatingBadge(rating = restaurant.rating, reviewCount = restaurant.reviewCount)
                }
                Text(
                    text = restaurant.nameEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = OceanPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = restaurant.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "খরচ: ${restaurant.priceRange}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OceanPrimary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = restaurant.descriptionBn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
