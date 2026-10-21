package com.example.ui.screens.hotels

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.Hotel
import com.example.data.network.CloudflareHotelProxyService
import com.example.ui.components.*
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelsScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val hotels by viewModel.repository.hotels.collectAsState()
    val favoriteIds by viewModel.repository.favoriteIds.collectAsState()
    val selectedSort by viewModel.selectedHotelSort.collectAsState()
    val selectedType by viewModel.selectedHotelType.collectAsState()
    val comparisonHotels by viewModel.comparisonHotels.collectAsState()
    val isComparisonSheetVisible by viewModel.isComparisonSheetVisible.collectAsState()
    val comparisonMessage by viewModel.hotelComparisonMessage.collectAsState()
    val proxyStatus by viewModel.hotelProxyStatus.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val hotelCategories = listOf(
        "All" to "সকল হোটেল",
        "Budget" to "বাজেট (৳২,৫০০)",
        "SeaView" to "সমুদ্র দৃশ্য",
        "Family" to "পরিবারবান্ধব",
        "Premium" to "প্রিমিয়াম রিসোর্ট",
        "Boutique" to "বুটিক ও ইকো"
    )

    val sortOptions = listOf(
        "Default" to "জনপ্রিয়",
        "PriceLow" to "কম ভাড়া",
        "PriceHigh" to "বেশি ভাড়া",
        "Rating" to "রেটিং"
    )

    val filteredHotels = hotels
        .filter { hotel ->
            val matchesCategory = when (selectedType) {
                "Budget" -> hotel.isBudget || hotel.priceFrom <= 2500
                "SeaView" -> hotel.isSeaView || hotel.amenities.any { it.contains("সি-ভিউ") || it.contains("সমুদ্র") }
                "Family" -> hotel.isFamilyFriendly || hotel.hotelType.contains("Family", ignoreCase = true)
                "Premium" -> hotel.hotelType.contains("Luxury", ignoreCase = true) || hotel.priceFrom >= 7000
                "Boutique" -> hotel.hotelType.contains("Boutique", ignoreCase = true) || hotel.hotelType.contains("Eco", ignoreCase = true)
                else -> true
            }

            val matchesSearch = searchQuery.isBlank() ||
                    hotel.nameBn.contains(searchQuery, ignoreCase = true) ||
                    hotel.nameEn.contains(searchQuery, ignoreCase = true) ||
                    hotel.location.contains(searchQuery, ignoreCase = true)

            matchesCategory && matchesSearch
        }
        .let { list ->
            when (selectedSort) {
                "PriceLow" -> list.sortedBy { it.priceFrom }
                "PriceHigh" -> list.sortedByDescending { it.priceFrom }
                "Rating" -> list.sortedByDescending { it.rating }
                else -> list
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "কক্সবাজারের হোটেল ও রিসোর্ট",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Tourist Information & Verification System",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (comparisonHotels.isNotEmpty()) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = SunsetAmber) {
                                    Text(comparisonHotels.size.toString())
                                }
                            }
                        ) {
                            IconButton(onClick = { viewModel.setComparisonSheetVisible(true) }) {
                                Icon(imageVector = Icons.Default.CompareArrows, contentDescription = "View Comparison", tint = OceanPrimary)
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("হোটেল নাম, এলাকা বা সুগন্ধা, কলাতলী...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("hotel_search_input")
                )

                // Category Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    hotelCategories.forEach { (catKey, label) ->
                        val isSelected = selectedType == catKey
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setSelectedHotelType(catKey) },
                            label = { Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            shape = RoundedCornerShape(18.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = OceanPrimary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                // Sorting Strip & Data Source Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${filteredHotels.size}টি হোটেল পাওয়া গেছে",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        sortOptions.forEach { (key, label) ->
                            val isSelected = selectedSort == key
                            Surface(
                                onClick = { viewModel.setSelectedHotelSort(key) },
                                color = if (isSelected) OceanPrimary.copy(alpha = 0.12f) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(2.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) OceanPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                // Comparison limit or info toast banner
                comparisonMessage?.let { msg ->
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = msg, fontSize = 11.sp, color = Color(0xFF1B5E20), fontWeight = FontWeight.Medium)
                            }
                            IconButton(onClick = { viewModel.clearComparisonMessage() }, modifier = Modifier.size(20.dp)) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }

                // Hotel List
                if (filteredHotels.isEmpty()) {
                    EmptyStateView(
                        titleBn = "কোনো হোটেল পাওয়া যায়নি",
                        descriptionBn = "অনুগ্রহ করে ফিল্টার পরিবর্তন করুন অথবা অন্য এলাকা অনুসন্ধান করুন।"
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = if (comparisonHotels.isNotEmpty()) 72.dp else 0.dp)
                    ) {
                        items(filteredHotels) { hotel ->
                            val isCompared = comparisonHotels.any { it.id == hotel.id }
                            EnhancedHotelListingCard(
                                hotel = hotel,
                                isFavorite = favoriteIds.contains(hotel.id),
                                isCompared = isCompared,
                                onHotelClick = { viewModel.selectHotel(hotel.id) },
                                onFavoriteToggle = { viewModel.repository.toggleFavorite(hotel.id) },
                                onToggleCompare = { viewModel.toggleHotelComparison(hotel) }
                            )
                        }
                    }
                }
            }

            // Floating Comparison Action Bar at Bottom
            AnimatedVisibility(
                visible = comparisonHotels.isNotEmpty(),
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OceanPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            comparisonHotels.forEach { h ->
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .border(1.5.dp, OceanPrimary, CircleShape)
                                ) {
                                    AppAsyncImage(url = h.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize())
                                }
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${comparisonHotels.size}/৩টি নির্বাচিত",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = OceanPrimary
                            )
                        }

                        Button(
                            onClick = { viewModel.setComparisonSheetVisible(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = OceanPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("open_hotel_comparison_button")
                        ) {
                            Icon(imageVector = Icons.Default.CompareArrows, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "তুলনা দেখুন", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (isComparisonSheetVisible) {
        HotelComparisonSheet(
            viewModel = viewModel,
            onDismiss = { viewModel.setComparisonSheetVisible(false) }
        )
    }
}

@Composable
fun EnhancedHotelListingCard(
    hotel: Hotel,
    isFavorite: Boolean,
    isCompared: Boolean,
    onHotelClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onToggleCompare: () -> Unit
) {
    Card(
        onClick = onHotelClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hotel_item_${hotel.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
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
                        .padding(10.dp)
                )

                // Category & Type Badge
                Surface(
                    color = OceanPrimary,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {
                    Text(
                        text = hotel.hotelType,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Price Tag Banner
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (hotel.priceVerified) "যাচাইকৃত শুরু" else "আনুমানিক শুরু",
                            fontSize = 9.sp,
                            color = if (hotel.priceVerified) Color(0xFF2E7D32) else SunsetAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "৳${hotel.priceFrom}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanPrimary
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                // Name & Location
                Text(
                    text = hotel.nameBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "📍 ${hotel.location}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Verified Price Metadata Box
                Surface(
                    color = if (hotel.priceVerified) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (hotel.priceVerified) Icons.Default.CheckCircle else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (hotel.priceVerified) Color(0xFF2E7D32) else SunsetAmber,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (hotel.priceVerified) {
                                "যাচাইকৃত ভাড়া • উৎস: ${hotel.priceSourceBn} (${hotel.lastVerifiedDate})"
                            } else {
                                "আনুমানিক রেট • ${hotel.priceSourceBn}"
                            },
                            fontSize = 10.sp,
                            color = if (hotel.priceVerified) Color(0xFF1B5E20) else Color(0xFFE65100),
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom row: Rating and Compare Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RatingBadge(rating = hotel.rating, reviewCount = hotel.reviewCount)

                    FilterChip(
                        selected = isCompared,
                        onClick = onToggleCompare,
                        label = {
                            Text(
                                text = if (isCompared) "✓ নির্বাচিত" else "+ তুলনা করুন",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
    }
}
