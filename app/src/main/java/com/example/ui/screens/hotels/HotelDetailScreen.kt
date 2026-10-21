package com.example.ui.screens.hotels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Hotel
import com.example.data.model.RoomType
import com.example.data.model.TravelAdvisoryItem
import com.example.data.model.UserReview
import com.example.ui.components.*
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetailScreen(
    hotelId: String,
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hotels by viewModel.repository.hotels.collectAsState()
    val hotel = hotels.find { it.id == hotelId } ?: hotels.firstOrNull()
    val favoriteIds by viewModel.repository.favoriteIds.collectAsState()
    val isFav = hotel != null && favoriteIds.contains(hotel.id)
    val reviews by viewModel.repository.reviews.collectAsState()
    val hotelReviews = reviews.filter { it.targetId == hotelId }
    val comparisonHotels by viewModel.comparisonHotels.collectAsState()
    val isCompared = hotel != null && comparisonHotels.any { it.id == hotel.id }
    val travelAdvisories by viewModel.travelAdvisories.collectAsState()

    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewAuthor by remember { mutableStateOf("") }
    var reviewRating by remember { mutableIntStateOf(5) }
    var reviewComment by remember { mutableStateOf("") }

    if (hotel == null) {
        EmptyStateView(
            titleBn = "হোটেল পাওয়া যায়নি",
            descriptionBn = "অনুরোধকৃত হোটেলের তথ্য বর্তমানে নেই।",
            actionText = "ফিরে যান",
            onActionClick = { viewModel.navigateBack() }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(hotel.nameBn, maxLines = 1, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    FavoriteButton(
                        isFavorite = isFav,
                        onToggle = { viewModel.repository.toggleFavorite(hotel.id) }
                    )
                    IconButton(onClick = {
                        shareContent(
                            context,
                            hotel.nameBn,
                            "${hotel.nameBn}\n${hotel.descriptionBn}\nঠিকানা: ${hotel.address}\nযোগাযোগ হটলাইন: ${hotel.phone}"
                        )
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
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (hotel.priceVerified) "যাচাইকৃত শুরু" else "আনুমানিক শুরু",
                            fontSize = 10.sp,
                            color = if (hotel.priceVerified) Color(0xFF2E7D32) else SunsetAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "৳${hotel.priceFrom} / রাত",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanPrimary
                        )
                    }

                    if (hotel.phone.isNotBlank()) {
                        OutlinedButton(
                            onClick = { dialNumber(context, hotel.phone) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("কল দিন")
                        }
                    }

                    Button(
                        onClick = { viewModel.toggleHotelComparison(hotel) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompared) SunsetAmber else OceanPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(44.dp)
                    ) {
                        Icon(
                            imageVector = if (isCompared) Icons.Default.Check else Icons.Default.CompareArrows,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isCompared) "তুলনা হচ্ছে" else "তুলনা করুন")
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
                        .height(240.dp)
                ) {
                    AppAsyncImage(
                        url = hotel.imageUrl,
                        contentDescription = hotel.nameBn,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = hotel.hotelType,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Gallery Row
            if (hotel.galleryUrls.size > 1) {
                item {
                    LazyRow(
                        modifier = Modifier.padding(vertical = 12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(hotel.galleryUrls) { imgUrl ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.size(width = 120.dp, height = 80.dp)
                            ) {
                                AppAsyncImage(url = imgUrl, contentDescription = "Gallery", modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }

            // Header Info & Verification Metadata
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = hotel.nameBn,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = hotel.nameEn,
                        style = MaterialTheme.typography.titleMedium,
                        color = SunsetAmber,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Verified vs Estimated Price Box
                    Surface(
                        color = if (hotel.priceVerified) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (hotel.priceVerified) Color(0xFFA5D6A7) else Color(0xFFFFCC80)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (hotel.priceVerified) Icons.Default.Verified else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (hotel.priceVerified) Color(0xFF2E7D32) else Color(0xFFE65100),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (hotel.priceVerified) "যাচাইকৃত ট্যারিফ তথ্য" else "আনুমানিক মূল্য সতর্কতা",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (hotel.priceVerified) Color(0xFF1B5E20) else Color(0xFFBF360C)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• উৎস: ${hotel.priceSourceBn}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "• সর্বশেষ যাচাই: ${hotel.lastVerifiedDate}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (!hotel.priceVerified) {
                                Text(
                                    text = "পরামর্শ: পিক-সিজন ও সরকারি ছুটির দিনে ভাড়া পরিবর্তনশীল। বুকিংয়ের আগে সরাসরি হটলাইনে কল করে নিশ্চিত হোন।",
                                    fontSize = 11.sp,
                                    color = Color(0xFFD84315),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Location & Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RatingBadge(rating = hotel.rating, reviewCount = hotel.reviewCount)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = OceanPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = hotel.location, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "📍 ${hotel.address}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Map Navigation Buttons (Google Maps Platform Deep-link & Internal Leaflet Navigation)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val mapUri = if (!hotel.googleMapsUrl.isNullOrBlank()) {
                                    Uri.parse(hotel.googleMapsUrl)
                                } else {
                                    Uri.parse("https://www.google.com/maps/search/?api=1&query=${hotel.latitude},${hotel.longitude}+(${Uri.encode(hotel.nameBn)})")
                                }
                                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                                context.startActivity(mapIntent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "গুগল ম্যাপে দেখুন", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.openPlaceDetailsForId(hotel.id, "Hotel") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Directions, contentDescription = null, tint = OceanPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "অ্যাপে রুট ও দূরত্ব", color = OceanPrimary, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Check-in & Check-out timing info
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(icon = Icons.Default.Login, title = "চেক-ইন সময়", value = hotel.checkInTime, modifier = Modifier.weight(1f))
                    InfoCard(icon = Icons.Default.Logout, title = "চেক-আউট সময়", value = hotel.checkOutTime, modifier = Modifier.weight(1f))
                }
            }

            // Description
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "হোটেল পরিচিতি (About Hotel)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = hotel.descriptionBn, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Amenities
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(text = "সুবিধাসমূহ (Amenities)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    hotel.amenities.forEach { amenity ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = OceanPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = amenity, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            // Room Types
            if (hotel.roomTypes.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "রুমের ধরন ও ট্যারিফ (Room Types)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        hotel.roomTypes.forEach { room ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = room.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                            if (room.hasOceanView) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    color = Color(0xFFE0F7FA),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(text = "🌊 সি-ভিউ", fontSize = 9.sp, color = Color(0xFF006064), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "ধারণক্ষমতা: ${room.capacity} • বেড: ${room.bedType}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text(text = "৳${room.pricePerNight}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OceanPrimary)
                                }
                            }
                        }
                    }
                }
            }

            // Google Search Travel Advisory & Booking Tips Card
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ভ্রমণ-পরামর্শ ও নির্দেশিকা (Travel Advice)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    travelAdvisories.forEach { adv ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCFD8DC)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = SunsetAmber, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = adv.titleBn, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = adv.tipBn, fontSize = 12.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(adv.sourceUrl))
                                        context.startActivity(intent)
                                    },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, tint = OceanPrimary, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "উৎস: ${adv.sourceName}",
                                        fontSize = 11.sp,
                                        color = OceanPrimary,
                                        textDecoration = TextDecoration.Underline,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
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
                        Text(text = "পর্যটকদের রিভিউ (${hotelReviews.size})", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        TextButton(onClick = { showReviewDialog = true }) {
                            Icon(imageVector = Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("রিভিউ দিন")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (hotelReviews.isEmpty()) {
                        Text(text = "এখনো কোনো রিভিউ দেওয়া হয়নি। প্রথম রিভিউটি আপনি দিন!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        hotelReviews.forEach { review ->
                            ReviewItemCard(review = review)
                            Spacer(modifier = Modifier.height(8.dp))
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
            title = { Text("হোটেল রিভিউ দিন") },
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
                                targetId = hotel.id,
                                authorName = reviewAuthor.ifBlank { "পর্যটক" },
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

@Composable
fun ReviewItemCard(
    review: UserReview,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(OceanPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.authorName.take(1),
                            fontWeight = FontWeight.Bold,
                            color = OceanPrimary,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = review.authorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                RatingBadge(rating = review.rating.toFloat())
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = review.comment, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = review.date,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
