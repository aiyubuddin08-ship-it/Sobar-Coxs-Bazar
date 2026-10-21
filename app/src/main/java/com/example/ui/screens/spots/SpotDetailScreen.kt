package com.example.ui.screens.spots

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TouristSpot
import com.example.ui.components.*
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel
import com.example.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotDetailScreen(
    spotId: String,
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val spots by viewModel.repository.spots.collectAsState()
    val spot = spots.find { it.id == spotId } ?: spots.firstOrNull()
    val favoriteIds by viewModel.repository.favoriteIds.collectAsState()
    val isFav = spot != null && favoriteIds.contains(spot.id)
    val reviews by viewModel.repository.reviews.collectAsState()
    val spotReviews = reviews.filter { it.targetId == spotId }

    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewAuthor by remember { mutableStateOf("") }
    var reviewRating by remember { mutableIntStateOf(5) }
    var reviewComment by remember { mutableStateOf("") }

    if (spot == null) {
        EmptyStateView(
            titleBn = "স্পট পাওয়া যায়নি",
            descriptionBn = "অনুরোধকৃত স্পটের তথ্য বর্তমানে বিদ্যমান নেই।",
            actionText = "ফিরে যান",
            onActionClick = { viewModel.navigateBack() }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(spot.nameBn, maxLines = 1, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    FavoriteButton(
                        isFavorite = isFav,
                        onToggle = { viewModel.repository.toggleFavorite(spot.id) }
                    )
                    IconButton(onClick = {
                        shareContent(context, spot.nameBn, "${spot.descriptionBn}\nঅবস্থান: ${spot.location}")
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.openPlaceDetailsForId(spot.id, "Spot")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OceanPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Directions, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("দূরত্ব ও রুট দেখুন")
                    }
                    Button(
                        onClick = { showReviewDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OceanPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.RateReview, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("রিভিউ দিন")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Main Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    AppAsyncImage(
                        url = spot.imageUrl,
                        contentDescription = spot.nameBn,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = spot.category,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Image Gallery Row
            if (spot.galleryUrls.size > 1) {
                item {
                    LazyRow(
                        modifier = Modifier.padding(vertical = 12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(spot.galleryUrls) { imgUrl ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .size(width = 120.dp, height = 80.dp)
                            ) {
                                AppAsyncImage(
                                    url = imgUrl,
                                    contentDescription = "Gallery",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }

            // Header Info
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = spot.nameBn,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = spot.nameEn,
                        style = MaterialTheme.typography.titleMedium,
                        color = SunsetAmber,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RatingBadge(rating = spot.rating, reviewCount = spot.reviewCount)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = OceanPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = spot.location,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Key Visit Info Cards (Opening hours & Duration)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        icon = Icons.Default.AccessTime,
                        title = "সময়সূচী",
                        value = spot.openingHours,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        icon = Icons.Default.HourglassBottom,
                        title = "আনুমানিক সময়",
                        value = spot.visitingDuration,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Description
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "স্থান সম্পর্কে (About Place)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = spot.descriptionBn,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Things to Do
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "করণীয় বিষয়সমূহ (Things to Do)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    spot.thingsToDo.forEach { item ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = OceanPrimary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Safety Info Box
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = SunsetAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "নিরাপত্তা তথ্য (Safety Information)",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE65100),
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = spot.safetyInfoBn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5D4037)
                        )
                    }
                }
            }

            // Travel Tips Box
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = Color(0xFF00796B),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ভ্রমণ টিপস (Travel Tips)",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF004D40),
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = spot.travelTipsBn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF004D40)
                        )
                    }
                }
            }

            // Nearby Hotels & Restaurants Section
            item {
                Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                    Text(
                        text = "আশপাশের সুবিধাসমূহ (Nearby)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "🏨 নিকটবর্তী হোটেল: ${spot.nearbyHotels.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🍽️ নিকটবর্তী খাবার দোকান: ${spot.nearbyRestaurants.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // User Reviews Section
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ভ্রমণকারীদের রিভিউ (${spotReviews.size})",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { showReviewDialog = true }) {
                            Text("রিভিউ লিখুন")
                        }
                    }

                    if (spotReviews.isEmpty()) {
                        Text(
                            text = "এখনও কোনো রিভিউ দেওয়া হয়নি। প্রথম রিভিউটি আপনি দিন!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        spotReviews.forEach { review ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(review.authorName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        RatingBadge(rating = review.rating.toFloat())
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(review.comment, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(review.date, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Review Dialog
    if (showReviewDialog) {
        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = { Text("রিভিউ যোগ করুন") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = reviewAuthor,
                        onValueChange = { reviewAuthor = it },
                        label = { Text("আপনার নাম") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("রেটিং:", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(8.dp))
                        (1..5).forEach { star ->
                            IconButton(onClick = { reviewRating = star }) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "$star Stars",
                                    tint = if (star <= reviewRating) SunsetAmber else Color.Gray,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        label = { Text("আপনার অভিজ্ঞতা লিখুন...") },
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reviewComment.isNotBlank()) {
                            viewModel.repository.addReview(
                                targetId = spot.id,
                                authorName = reviewAuthor,
                                rating = reviewRating,
                                comment = reviewComment
                            )
                            showReviewDialog = false
                            reviewComment = ""
                            reviewAuthor = ""
                        }
                    }
                ) {
                    Text("জমা দিন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReviewDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}
