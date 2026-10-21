package com.example.ui.screens.search

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppAsyncImage
import com.example.ui.components.EmptyStateView
import com.example.ui.components.RatingBadge
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel
import com.example.ui.components.PlaceDetailsSheet
import com.example.viewmodel.Screen
import com.example.viewmodel.SearchResultItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchCategory by viewModel.searchCategory.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()
    val isNominatimSearching by viewModel.isNominatimSearching.collectAsState()
    val selectedUnifiedPlace by viewModel.selectedUnifiedPlace.collectAsState()

    val categories = listOf(
        "All" to "সবকিছু",
        "Spots" to "স্পট",
        "Hotels" to "হোটেল",
        "Restaurants" to "খাবার",
        "Shopping" to "শপিং",
        "Transport" to "যাতায়াত",
        "Emergency" to "জরুরি",
        "News" to "খবর"
    )

    val popularSearches = listOf(
        "ইনানী বিচ", "রূপচাঁদা ফ্রাই", "সায়মন রিসোর্ট", "ট্যুরিস্ট পুলিশ", "হিমছড়ি ঝর্ণা", "বার্মিজ মার্কেট", "মেরিন ড্রাইভ"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            viewModel.setSearchQuery(it)
                            if (it.length > 2) {
                                viewModel.addRecentSearch(it)
                            }
                        },
                        placeholder = { Text("কক্সবাজারের যেকোনো কিছু খুঁজুন...", fontSize = 14.sp) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("global_search_input")
                    )
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
            // Category Filter Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { (catKey, label) ->
                    val isSelected = searchCategory == catKey
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setSearchCategory(catKey) },
                        label = { Text(label, fontSize = 12.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanPrimary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            if (isNominatimSearching) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(13.dp), strokeWidth = 1.8.dp, color = OceanPrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "OpenStreetMap ও Nominatim থেকে লাইভ স্থান খোঁজা হচ্ছে...",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (searchQuery.isBlank()) {
                // Recent & Popular Searches UI
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (recentSearches.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "সাম্প্রতিক অনুসন্ধান (Recent Searches)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                TextButton(onClick = { viewModel.clearRecentSearches() }) {
                                    Text("মুছে ফেলুন", fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                recentSearches.forEach { query ->
                                    Surface(
                                        onClick = { viewModel.setSearchQuery(query) },
                                        shape = RoundedCornerShape(10.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(imageVector = Icons.Default.History, contentDescription = null, tint = OceanPrimary, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(query, style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "জনপ্রিয় অনুসন্ধান (Popular Searches)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            popularSearches.forEach { tag ->
                                AssistChip(
                                    onClick = { viewModel.setSearchQuery(tag) },
                                    label = { Text(tag, fontSize = 12.sp) },
                                    leadingIcon = { Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                )
                            }
                        }
                    }
                }
            } else {
                // Search Results
                if (searchResults.isEmpty()) {
                    EmptyStateView(
                        titleBn = "'$searchQuery' এর কোনো ফলাফল নেই",
                        descriptionBn = "বানান ঠিক আছে কি না যাচাই করুন অথবা অন্য শব্দ দিয়ে খুঁজুন।"
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "${searchResults.size} টি ফলাফল পাওয়া গেছে",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        items(searchResults) { item ->
                            SearchResultCard(
                                item = item,
                                onClick = {
                                    if (item.unifiedPlace != null) {
                                        viewModel.selectUnifiedPlace(item.unifiedPlace)
                                    } else {
                                        viewModel.openPlaceDetailsForId(item.id, item.itemType)
                                    }
                                },
                                onRouteClick = {
                                    if (item.unifiedPlace != null) {
                                        viewModel.openRouteForPlace(item.unifiedPlace)
                                    } else {
                                        viewModel.openRouteForDestinationId(item.id)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(
    item: SearchResultItem,
    onClick: () -> Unit,
    onRouteClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.imageUrl.isNotBlank()) {
                AppAsyncImage(
                    url = item.imageUrl,
                    contentDescription = item.titleBn,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.titleBn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = if (item.itemType == "OsmPlace") Color(0xFFE0F2F1) else OceanPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (item.itemType == "OsmPlace") "OpenStreetMap" else item.itemType,
                            color = if (item.itemType == "OsmPlace") Color(0xFF00695C) else OceanPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitleBn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (item.rating != null) {
                RatingBadge(rating = item.rating)
                Spacer(modifier = Modifier.width(4.dp))
            }
            IconButton(
                onClick = onRouteClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Directions,
                    contentDescription = "রুট দেখুন",
                    tint = OceanPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
