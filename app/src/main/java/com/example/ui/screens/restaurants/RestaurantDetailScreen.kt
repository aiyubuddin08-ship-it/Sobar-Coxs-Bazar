package com.example.ui.screens.restaurants

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Restaurant
import com.example.ui.components.*
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel
import com.example.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurantId: String,
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val restaurants by viewModel.repository.restaurants.collectAsState()
    val restaurant = restaurants.find { it.id == restaurantId } ?: restaurants.firstOrNull()
    val favoriteIds by viewModel.repository.favoriteIds.collectAsState()
    val isFav = restaurant != null && favoriteIds.contains(restaurant.id)
    val reviews by viewModel.repository.reviews.collectAsState()
    val restaurantReviews = reviews.filter { it.targetId == restaurantId }

    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewAuthor by remember { mutableStateOf("") }
    var reviewRating by remember { mutableIntStateOf(5) }
    var reviewComment by remember { mutableStateOf("") }

    if (restaurant == null) {
        EmptyStateView(
            titleBn = "রেস্তোরাঁ পাওয়া যায়নি",
            descriptionBn = "অনুরোধকৃত তথ্য বর্তমানে বিদ্যমান নেই।",
            actionText = "ফিরে যান",
            onActionClick = { viewModel.navigateBack() }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurant.nameBn, maxLines = 1, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    FavoriteButton(
                        isFavorite = isFav,
                        onToggle = { viewModel.repository.toggleFavorite(restaurant.id) }
                    )
                    IconButton(onClick = {
                        shareContent(context, restaurant.nameBn, "${restaurant.descriptionBn}\nঠিকানা: ${restaurant.address}\nযোগাযোগ: ${restaurant.phone}")
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
                    OutlinedButton(
                        onClick = { dialNumber(context, restaurant.phone) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("কল করুন")
                    }
                    Button(
                        onClick = {
                            viewModel.selectMapMarker(restaurant.id)
                            viewModel.navigateTo(Screen.Map)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OceanPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("লোকেশন ম্যাপ")
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
            // Main Hero Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    AppAsyncImage(
                        url = restaurant.imageUrl,
                        contentDescription = restaurant.nameBn,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        color = SunsetAmber,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = restaurant.category,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Header Info
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = restaurant.nameBn,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = restaurant.nameEn,
                        style = MaterialTheme.typography.titleMedium,
                        color = SunsetAmber,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RatingBadge(rating = restaurant.rating, reviewCount = restaurant.reviewCount)
                        Text(
                            text = "বাজেট: ${restaurant.priceRange}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = OceanPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = restaurant.address,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Opening Hours & Contact
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        icon = Icons.Default.AccessTime,
                        title = "খোলার সময়সূচী",
                        value = restaurant.openingHours,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        icon = Icons.Default.Phone,
                        title = "যোগাযোগ নম্বর",
                        value = restaurant.phone,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Description
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "রেস্তোরাঁ পরিচিতি (About)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = restaurant.descriptionBn,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Popular Dishes Menu Preview
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "জনপ্রিয় পদসমূহ (Popular Dishes)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    restaurant.popularDishes.forEach { dish ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(dish.nameBn, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        if (dish.isSpecial) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = SunsetAmber.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "স্পেশাল",
                                                    color = SunsetAmber,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(dish.nameEn, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(
                                    text = "৳${dish.price}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Reviews Section
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "খাবারপ্রেমীদের রিভিউ (${restaurantReviews.size})",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { showReviewDialog = true }) {
                            Text("রিভিউ লিখুন")
                        }
                    }

                    if (restaurantReviews.isEmpty()) {
                        Text(
                            text = "প্রথম রিভিউটি লিখে খাবারের মান জানান!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        restaurantReviews.forEach { review ->
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
            title = { Text("খাবারের রিভিউ দিন") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = reviewAuthor,
                        onValueChange = { reviewAuthor = it },
                        label = { Text("আপনার নাম") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("খাবারের স্বাদ রেটিং:", fontWeight = FontWeight.SemiBold)
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
                        label = { Text("খাবারের মান ও অভিজ্ঞতা লিখুন...") },
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
                                targetId = restaurant.id,
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
