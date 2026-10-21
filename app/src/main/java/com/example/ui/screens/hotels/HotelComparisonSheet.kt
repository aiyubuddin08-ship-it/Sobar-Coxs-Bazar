package com.example.ui.screens.hotels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Hotel
import com.example.ui.components.AppAsyncImage
import com.example.ui.components.RatingBadge
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelComparisonSheet(
    viewModel: CoxViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val comparisonHotels by viewModel.comparisonHotels.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.testTag("hotel_comparison_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "হোটেল তুলনা (Hotel Comparison)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${comparisonHotels.size}টি হোটেল নির্বাচিত",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (comparisonHotels.isNotEmpty()) {
                        TextButton(
                            onClick = { viewModel.clearHotelComparison() },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text("সব মুছুন", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (comparisonHotels.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.CompareArrows, contentDescription = null, tint = OceanPrimary, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "কোনো হোটেল নির্বাচিত নেই",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "হোটেল তালিকা থেকে 'তুলনা করুন' বাটনে চাপ দিয়ে সর্বোচ্চ ৩টি হোটেল একসাথে তুলনা করুন।",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 6.dp)
                        )
                    }
                }
            } else {
                // Horizontal scrollable comparison matrix
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    comparisonHotels.forEach { hotel ->
                        HotelComparisonColumn(
                            hotel = hotel,
                            context = context,
                            onRemove = { viewModel.removeHotelFromComparison(hotel.id) },
                            onOpenRoute = {
                                onDismiss()
                                viewModel.openRouteForDestinationId(hotel.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HotelComparisonColumn(
    hotel: Hotel,
    context: Context,
    onRemove: () -> Unit,
    onOpenRoute: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            // Header Image & Remove Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AppAsyncImage(
                    url = hotel.imageUrl,
                    contentDescription = hotel.nameBn,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(26.dp)
                        .background(Color.Black.copy(alpha = 0.55f), CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hotel Name & Type
            Text(
                text = hotel.nameBn,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = hotel.hotelType,
                fontSize = 11.sp,
                color = OceanPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Price & Verification Status Card
            Surface(
                color = if (hotel.priceVerified) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(
                    0.8.dp,
                    if (hotel.priceVerified) Color(0xFFA5D6A7) else Color(0xFFFFCC80)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (hotel.priceVerified) Icons.Default.CheckCircle else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (hotel.priceVerified) Color(0xFF2E7D32) else SunsetAmber,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (hotel.priceVerified) "যাচাইকৃত ভাড়া" else "আনুমানিক ভাড়া",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hotel.priceVerified) Color(0xFF2E7D32) else SunsetAmber
                        )
                    }
                    Text(
                        text = "৳${hotel.priceFrom} / রাত",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "উৎস: ${hotel.priceSourceBn}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "সর্বশেষ: ${hotel.lastVerifiedDate}",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rating & Reviews
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                RatingBadge(rating = hotel.rating)
                Text(
                    text = "${hotel.reviewCount} রিভিউ",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Key Attributes Comparison
            ComparisonMetricRow(label = "অবস্থান", value = hotel.location, icon = Icons.Default.LocationOn)
            ComparisonMetricRow(
                label = "সমুদ্র দৃশ্য",
                value = if (hotel.isSeaView || hotel.amenities.any { it.contains("সি-ভিউ") }) "হ্যাঁ (সরাসরি)" else "সীমিত / সাধারণ",
                icon = Icons.Default.BeachAccess
            )
            ComparisonMetricRow(
                label = "পরিবার উপযোগী",
                value = if (hotel.isFamilyFriendly) "হ্যাঁ (ফ্যামিলি ফ্রেন্ডলি)" else "সাধারণ",
                icon = Icons.Default.FamilyRestroom
            )
            ComparisonMetricRow(
                label = "চেক-ইন / আউট",
                value = "${hotel.checkInTime} / ${hotel.checkOutTime}",
                icon = Icons.Default.Schedule
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "জনপ্রিয় সুবিধা:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            hotel.amenities.take(4).forEach { am ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 1.dp)) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = am, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            if (hotel.phone.isNotBlank()) {
                OutlinedButton(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${hotel.phone}"))
                        context.startActivity(dialIntent)
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(36.dp)
                ) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "সরাসরি ফোন", fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Google Maps Platform Deep-link (complying with Google Terms of Service)
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
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Map, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "গুগল ম্যাপে দেখুন", fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Internal OSM Route Navigation
            OutlinedButton(
                onClick = onOpenRoute,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Directions, contentDescription = null, tint = OceanPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "অ্যাপে রুট দেখুন", color = OceanPrimary, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun ComparisonMetricRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = OceanPrimary, modifier = Modifier.size(13.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(text = label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
