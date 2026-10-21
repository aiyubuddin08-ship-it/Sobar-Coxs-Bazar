package com.example.ui.screens.admin

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CoralRed
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val spots by viewModel.repository.spots.collectAsState()
    val hotels by viewModel.repository.hotels.collectAsState()
    val restaurants by viewModel.repository.restaurants.collectAsState()
    val newsList by viewModel.repository.news.collectAsState()
    val events by viewModel.repository.events.collectAsState()

    var activeTab by remember { mutableStateOf("Hotels") }

    // Dialog States
    var showAddHotelDialog by remember { mutableStateOf(false) }
    var showAddSpotDialog by remember { mutableStateOf(false) }
    var showAddNewsDialog by remember { mutableStateOf(false) }
    var showSendNotifDialog by remember { mutableStateOf(false) }
    var deleteConfirmItem by remember { mutableStateOf<Pair<String, String>?>(null) } // type to id

    // Add Hotel Form States
    var newHotelNameBn by remember { mutableStateOf("") }
    var newHotelLocation by remember { mutableStateOf("") }
    var newHotelPrice by remember { mutableStateOf("5000") }
    var newHotelType by remember { mutableStateOf("Luxury Resort") }

    // Add Spot Form States
    var newSpotNameBn by remember { mutableStateOf("") }
    var newSpotLocation by remember { mutableStateOf("") }
    var newSpotCategory by remember { mutableStateOf("Beach") }
    var newSpotDesc by remember { mutableStateOf("") }

    // Add News Form States
    var newNewsTitle by remember { mutableStateOf("") }
    var newNewsDesc by remember { mutableStateOf("") }
    var newNewsCat by remember { mutableStateOf("Tourism") }

    // Notification Form States
    var notifTitle by remember { mutableStateOf("") }
    var notifMessage by remember { mutableStateOf("") }
    var notifCat by remember { mutableStateOf("Safety") }

    // Destination & Coordinates Form States
    val destinations by viewModel.destinations.collectAsState()
    var showAddDestDialog by remember { mutableStateOf(false) }
    var editingDestination by remember { mutableStateOf<RouteDestination?>(null) }
    var newDestNameBn by remember { mutableStateOf("") }
    var newDestCategory by remember { mutableStateOf("পর্যটন স্পট") }
    var newDestAddress by remember { mutableStateOf("") }
    var newDestLat by remember { mutableStateOf("21.4339") }
    var newDestLng by remember { mutableStateOf("91.9682") }
    var newDestRequiresFerry by remember { mutableStateOf(false) }
    var newDestGhatName by remember { mutableStateOf("") }
    var newDestFerryDuration by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("অ্যাডমিন কন্ট্রোল সেন্টার", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.adminLogout() }) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (activeTab) {
                        "Hotels" -> showAddHotelDialog = true
                        "Spots" -> showAddSpotDialog = true
                        "News" -> showAddNewsDialog = true
                        "Notifications" -> showSendNotifDialog = true
                    }
                },
                containerColor = OceanPrimary,
                modifier = Modifier.testTag("admin_fab_add")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Metrics Overview Grid
            item {
                Text("সিস্টেম পরিসংখ্যান (System Metrics)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(title = "মোট পর্যটক", count = "১২,৪৫০+", icon = Icons.Default.People, color = OceanPrimary, modifier = Modifier.weight(1f))
                    MetricCard(title = "হোটেল তালিকা", count = "${hotels.size}", icon = Icons.Default.Hotel, color = SunsetAmber, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(title = "স্পট সংখ্যা", count = "${spots.size}", icon = Icons.Default.BeachAccess, color = Color(0xFF2E7D32), modifier = Modifier.weight(1f))
                    MetricCard(title = "খবর ও নোটিশ", count = "${newsList.size}", icon = Icons.Default.Article, color = Color(0xFF6A1B9A), modifier = Modifier.weight(1f))
                }
            }

            // Section Tabs
            item {
                val tabs = listOf(
                    "Hotels" to "🏨 হোটেল ব্যবস্থাপনা",
                    "Spots" to "🏖️ স্পট ব্যবস্থাপনা",
                    "Destinations" to "📍 রুট ও নৌঘাট স্থানাঙ্ক",
                    "News" to "📰 নোটিশ প্রকাশ",
                    "Notifications" to "🔔 পুশ বিজ্ঞপ্তি"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabs.forEach { (tabKey, label) ->
                        val isSelected = activeTab == tabKey
                        FilterChip(
                            selected = isSelected,
                            onClick = { activeTab = tabKey },
                            label = { Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = OceanPrimary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            // Dynamic Content Section
            when (activeTab) {
                "Hotels" -> {
                    items(hotels) { hotel ->
                        AdminItemCard(
                            title = hotel.nameBn,
                            subtitle = "${hotel.location} • ৳${hotel.priceFrom}",
                            status = hotel.status,
                            onStatusChange = { newStatus ->
                                viewModel.repository.changeHotelStatus(hotel.id, newStatus)
                            },
                            onDelete = {
                                deleteConfirmItem = "Hotel" to hotel.id
                            }
                        )
                    }
                }
                "Spots" -> {
                    items(spots) { spot ->
                        AdminItemCard(
                            title = spot.nameBn,
                            subtitle = "${spot.category} • ${spot.location}",
                            status = spot.status,
                            onStatusChange = { newStatus ->
                                viewModel.repository.changeSpotStatus(spot.id, newStatus)
                            },
                            onDelete = {
                                deleteConfirmItem = "Spot" to spot.id
                            }
                        )
                    }
                }
                "News" -> {
                    items(newsList) { article ->
                        AdminItemCard(
                            title = article.titleBn,
                            subtitle = "${article.category} • ${article.date}",
                            status = article.status,
                            onStatusChange = { newStatus ->
                                viewModel.repository.changeNewsStatus(article.id, newStatus)
                            },
                            onDelete = {
                                deleteConfirmItem = "News" to article.id
                            }
                        )
                    }
                }
                "Destinations" -> {
                    item {
                        Button(
                            onClick = {
                                editingDestination = null
                                newDestNameBn = ""
                                newDestAddress = ""
                                newDestLat = "21.4339"
                                newDestLng = "91.9682"
                                newDestRequiresFerry = false
                                newDestGhatName = ""
                                newDestFerryDuration = ""
                                showAddDestDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OceanPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.AddLocationAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("নতুন গন্তব্য / নৌঘাট স্থানাঙ্ক যুক্ত করুন")
                        }
                    }
                    items(destinations) { dest ->
                        AdminDestinationCard(
                            destination = dest,
                            onEdit = {
                                editingDestination = dest
                                newDestNameBn = dest.nameBn
                                newDestCategory = dest.category
                                newDestAddress = dest.address
                                newDestLat = dest.latitude.toString()
                                newDestLng = dest.longitude.toString()
                                newDestRequiresFerry = dest.requiresFerry
                                newDestGhatName = dest.ferryGhatNameBn ?: ""
                                newDestFerryDuration = dest.ferryDurationBn ?: ""
                                showAddDestDialog = true
                            },
                            onDelete = {
                                viewModel.deleteDestination(dest.id)
                            }
                        )
                    }
                }
                "Notifications" -> {
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("নতুন বিজ্ঞপ্তি প্রচার করুন", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = notifTitle,
                                    onValueChange = { notifTitle = it },
                                    label = { Text("বিজ্ঞপ্তির শিরোনাম") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = notifMessage,
                                    onValueChange = { notifMessage = it },
                                    label = { Text("বার্তা / বিস্তারিত") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        if (notifTitle.isNotBlank() && notifMessage.isNotBlank()) {
                                            viewModel.repository.addNotification(notifTitle, notifMessage, notifCat)
                                            notifTitle = ""
                                            notifMessage = ""
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = OceanPrimary)
                                ) {
                                    Icon(imageVector = Icons.Default.Send, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("বিজ্ঞপ্তি সম্প্রচার করুন")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Hotel Dialog
    if (showAddHotelDialog) {
        AlertDialog(
            onDismissRequest = { showAddHotelDialog = false },
            title = { Text("নতুন হোটেল যুক্ত করুন") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newHotelNameBn,
                        onValueChange = { newHotelNameBn = it },
                        label = { Text("হোটেলের নাম (বাংলা)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newHotelLocation,
                        onValueChange = { newHotelLocation = it },
                        label = { Text("অবস্থান (উদাঃ কলাতলী)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newHotelPrice,
                        onValueChange = { newHotelPrice = it },
                        label = { Text("শুরুর মূল্য (৳)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newHotelNameBn.isNotBlank()) {
                            val hotel = Hotel(
                                id = "hotel_${System.currentTimeMillis()}",
                                nameBn = newHotelNameBn,
                                nameEn = "New Hotel",
                                rating = 4.5f,
                                reviewCount = 1,
                                location = newHotelLocation.ifBlank { "কক্সবাজার" },
                                address = newHotelLocation,
                                priceFrom = newHotelPrice.toIntOrNull() ?: 5000,
                                hotelType = newHotelType,
                                amenities = listOf("ওয়াইফাই", "এয়ার কন্ডিশন", "রুফটপ ভিউ"),
                                descriptionBn = "কক্সবাজারের মনোরম পরিবেশে অবস্থিত আধুনিক সুবিধা সম্পন্ন রিসোর্ট।",
                                roomTypes = listOf(RoomType("Standard Deluxe", newHotelPrice.toIntOrNull() ?: 5000, "২ জন", "১ কিং বেড")),
                                checkInTime = "১৪:০০",
                                checkOutTime = "১২:০০",
                                phone = "+8801700000000",
                                latitude = 21.42,
                                longitude = 91.98,
                                imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80",
                                galleryUrls = listOf("https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80")
                            )
                            viewModel.repository.addHotel(hotel)
                            showAddHotelDialog = false
                            newHotelNameBn = ""
                            newHotelLocation = ""
                        }
                    }
                ) {
                    Text("সংরক্ষণ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddHotelDialog = false }) { Text("বাতিল") }
            }
        )
    }

    // Add Spot Dialog
    if (showAddSpotDialog) {
        AlertDialog(
            onDismissRequest = { showAddSpotDialog = false },
            title = { Text("নতুন পর্যটন স্পট যুক্ত করুন") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newSpotNameBn,
                        onValueChange = { newSpotNameBn = it },
                        label = { Text("স্পটের নাম (বাংলা)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newSpotLocation,
                        onValueChange = { newSpotLocation = it },
                        label = { Text("অবস্থান ও দূরত্ব") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newSpotDesc,
                        onValueChange = { newSpotDesc = it },
                        label = { Text("সংক্ষিপ্ত বিবরণ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newSpotNameBn.isNotBlank()) {
                            val spot = TouristSpot(
                                id = "spot_${System.currentTimeMillis()}",
                                nameBn = newSpotNameBn,
                                nameEn = "New Spot",
                                category = newSpotCategory,
                                rating = 4.8f,
                                reviewCount = 1,
                                location = newSpotLocation.ifBlank { "কক্সবাজার" },
                                distance = "১০ কিমি",
                                descriptionBn = newSpotDesc.ifBlank { "কক্সবাজারের মনোরম প্রাকৃতিক স্পট।" },
                                descriptionEn = "Beautiful tourist destination in Cox's Bazar.",
                                openingHours = "সার্বক্ষণিক",
                                visitingDuration = "২ ঘণ্টা",
                                thingsToDo = listOf("ঘোরাঘুরি", "ফটোগ্রাফি"),
                                safetyInfoBn = "লাইফগার্ড ও স্থানীয় নির্দেশনা অনুসরণ করুন।",
                                travelTipsBn = "বিকালে ভ্রমণ সুবিধাজনক।",
                                nearbyHotels = listOf("কলাতলী হোটেল জোন"),
                                nearbyRestaurants = listOf("ঝাউবন রেস্তোরাঁ"),
                                imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
                                galleryUrls = listOf("https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80"),
                                latitude = 21.43,
                                longitude = 91.97
                            )
                            viewModel.repository.addSpot(spot)
                            showAddSpotDialog = false
                            newSpotNameBn = ""
                            newSpotLocation = ""
                            newSpotDesc = ""
                        }
                    }
                ) {
                    Text("সংরক্ষণ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSpotDialog = false }) { Text("বাতিল") }
            }
        )
    }

    // Add News Dialog
    if (showAddNewsDialog) {
        AlertDialog(
            onDismissRequest = { showAddNewsDialog = false },
            title = { Text("নতুন সংবাদ ও নোটিশ প্রকাশ") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newNewsTitle,
                        onValueChange = { newNewsTitle = it },
                        label = { Text("সংবাদের শিরোনাম") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newNewsDesc,
                        onValueChange = { newNewsDesc = it },
                        label = { Text("বিস্তারিত সংবাদ") },
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newNewsTitle.isNotBlank()) {
                            val article = NewsArticle(
                                id = "news_${System.currentTimeMillis()}",
                                titleBn = newNewsTitle,
                                titleEn = "Announcement",
                                category = newNewsCat,
                                date = "আজ",
                                shortDescriptionBn = newNewsDesc.take(60),
                                fullContentBn = newNewsDesc,
                                imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
                                author = "ট্যুরিজম প্রশাসন"
                            )
                            viewModel.repository.addNews(article)
                            showAddNewsDialog = false
                            newNewsTitle = ""
                            newNewsDesc = ""
                        }
                    }
                ) {
                    Text("প্রকাশ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddNewsDialog = false }) { Text("বাতিল") }
            }
        )
    }

    // Add / Edit Route Destination & Ghat Dialog
    if (showAddDestDialog) {
        AlertDialog(
            onDismissRequest = { showAddDestDialog = false },
            title = { Text(if (editingDestination != null) "গন্তব্য / নৌঘাট স্থানাঙ্ক সংশোধন" else "নতুন গন্তব্য / নৌঘাট যোগ করুন") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newDestNameBn,
                        onValueChange = { newDestNameBn = it },
                        label = { Text("গন্তব্যের নাম (বাংলা)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDestCategory,
                        onValueChange = { newDestCategory = it },
                        label = { Text("ক্যাটাগরি (উদাঃ পর্যটন স্পট / দ্বীপ ও নৌপথ / হাসপাতাল)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDestAddress,
                        onValueChange = { newDestAddress = it },
                        label = { Text("ঠিকানা / অবস্থান") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newDestLat,
                            onValueChange = { newDestLat = it },
                            label = { Text("Latitude (অক্ষাংশ)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = newDestLng,
                            onValueChange = { newDestLng = it },
                            label = { Text("Longitude (দ্রাঘিমা)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = newDestRequiresFerry,
                            onCheckedChange = { newDestRequiresFerry = it }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("নৌপথ / স্পিডবোট সংযোগ প্রয়োজন (যেমন মহেশখালী দ্বীপ)", fontSize = 12.sp)
                    }

                    if (newDestRequiresFerry) {
                        OutlinedTextField(
                            value = newDestGhatName,
                            onValueChange = { newDestGhatName = it },
                            label = { Text("সংযোগ নৌঘাটের নাম (উদাঃ কস্তুরী ঘাট / ৬ নং ঘাট)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newDestFerryDuration,
                            onValueChange = { newDestFerryDuration = it },
                            label = { Text("নৌযান সময়সূচী ও ভাড়া (উদাঃ স্পিডবোট ১৫-২০ মি, ট্রলার ৫০ মি)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val lat = newDestLat.toDoubleOrNull() ?: 21.4339
                        val lng = newDestLng.toDoubleOrNull() ?: 91.9682
                        if (newDestNameBn.isNotBlank()) {
                            val dest = RouteDestination(
                                id = editingDestination?.id ?: "dest_${System.currentTimeMillis()}",
                                nameBn = newDestNameBn,
                                nameEn = editingDestination?.nameEn ?: newDestNameBn,
                                category = newDestCategory.ifBlank { "পর্যটন স্পট" },
                                address = newDestAddress.ifBlank { "কক্সবাজার" },
                                latitude = lat,
                                longitude = lng,
                                requiresFerry = newDestRequiresFerry,
                                ferryGhatNameBn = if (newDestRequiresFerry) newDestGhatName.ifBlank { "কস্তুরী ঘাট" } else null,
                                ferryGhatLat = if (newDestRequiresFerry) 21.4485 else null,
                                ferryGhatLng = if (newDestRequiresFerry) 91.9720 else null,
                                ferryDurationBn = if (newDestRequiresFerry) newDestFerryDuration.ifBlank { "স্পিডবোটে ১৫-২০ মিনিট" } else null,
                                ferryTipsBn = if (newDestRequiresFerry) "যাচাইকৃত ঘাট থেকে লাইফজ্যাকেট পরিধান করে নৌযাত্রা করুন।" else null,
                                source = "অ্যাডমিন প্যানেল / স্থানীয় গেজেট",
                                verificationStatus = "যাচাইকৃত (Admin Verified)"
                            )
                            viewModel.addOrUpdateDestination(dest)
                            showAddDestDialog = false
                            editingDestination = null
                        }
                    }
                ) {
                    Text("সংরক্ষণ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDestDialog = false
                    editingDestination = null
                }) {
                    Text("বাতিল")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (deleteConfirmItem != null) {
        val (type, id) = deleteConfirmItem!!
        AlertDialog(
            onDismissRequest = { deleteConfirmItem = null },
            title = { Text("আইটেম মুছে ফেলার নিশ্চিতকরণ") },
            text = { Text("আপনি কি নিশ্চিতভাবে এই $type টি মুছে ফেলতে চান? এটি স্থায়ীভাবে অপসারিত হবে।") },
            confirmButton = {
                Button(
                    onClick = {
                        when (type) {
                            "Hotel" -> viewModel.repository.deleteHotel(id)
                            "Spot" -> viewModel.repository.deleteSpot(id)
                            "News" -> viewModel.repository.deleteNews(id)
                        }
                        deleteConfirmItem = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
                ) {
                    Text("মুছে ফেলুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteConfirmItem = null }) { Text("বাতিল") }
            }
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    count: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = count, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun AdminItemCard(
    title: String,
    subtitle: String,
    status: ContentStatus,
    onStatusChange: (ContentStatus) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StatusBadge(status = status)
                    TextButton(
                        onClick = {
                            val next = when (status) {
                                ContentStatus.PUBLISHED -> ContentStatus.DRAFT
                                ContentStatus.DRAFT -> ContentStatus.HIDDEN
                                ContentStatus.HIDDEN -> ContentStatus.PUBLISHED
                                ContentStatus.PENDING -> ContentStatus.PUBLISHED
                            }
                            onStatusChange(next)
                        },
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                    ) {
                        Text("স্ট্যাটাস বদলান", fontSize = 11.sp)
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = CoralRed)
            }
        }
    }
}

@Composable
fun AdminDestinationCard(
    destination: RouteDestination,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = destination.nameBn, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = OceanPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = destination.category,
                            color = OceanPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "${destination.address} • [${destination.latitude}, ${destination.longitude}]",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (destination.requiresFerry) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "⚓ ঘাট: ${destination.ferryGhatNameBn ?: "কস্তুরী ঘাট"} | ${destination.ferryDurationBn ?: "স্পিডবোট ১৫-২০ মি"}",
                        fontSize = 11.sp,
                        color = SunsetAmber,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            IconButton(onClick = onEdit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = OceanPrimary)
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = CoralRed)
            }
        }
    }
}

