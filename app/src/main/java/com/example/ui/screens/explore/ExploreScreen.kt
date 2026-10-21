package com.example.ui.screens.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.window.Dialog
import com.example.ui.components.AppAsyncImage
import com.example.ui.components.AppOverviewContent
import com.example.ui.components.shareContent
import com.example.ui.theme.OceanPrimary
import com.example.viewmodel.CoxViewModel

data class ExplorePhoto(
    val id: String,
    val titleBn: String,
    val category: String, // Beach, Sunset, Food, Nature
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0 = Overview, 1 = Photo Gallery
    var selectedCategory by remember { mutableStateOf("All") }
    var viewingPhoto by remember { mutableStateOf<ExplorePhoto?>(null) }

    val photos = remember {
        listOf(
            ExplorePhoto("p1", "লাবণী বিচে গোধূলি সূর্যাস্ত", "Sunset", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80"),
            ExplorePhoto("p2", "ইনানী বিচের প্রবাল ও নীল জল", "Beach", "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=800&q=80"),
            ExplorePhoto("p3", "মেরিন ড্রাইভ পাহাড়ি রোড", "Nature", "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=800&q=80"),
            ExplorePhoto("p4", "তাজা ফ্রাইড রূপচাঁদা ও কাঁকড়া", "Food", "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80"),
            ExplorePhoto("p5", "হিমছড়ি পাহাড়ি সবুজ অরণ্য", "Nature", "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80"),
            ExplorePhoto("p6", "সুগন্ধা পয়েন্টের রাতের আলো", "Beach", "https://images.unsplash.com/photo-1471922694855-fa59acdb3608?auto=format&fit=crop&w=800&q=80"),
            ExplorePhoto("p7", "মারমেইড ক্যাফে বিচ ভিউ", "Food", "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=800&q=80"),
            ExplorePhoto("p8", "সোনাদিয়া দ্বীপের সোনালি বালুচর", "Beach", "https://images.unsplash.com/photo-1473186578172-c141e6798cf4?auto=format&fit=crop&w=800&q=80")
        )
    }

    val categories = listOf(
        "All" to "সব ছবি",
        "Sunset" to "🌅 সূর্যাস্ত",
        "Beach" to "🏖️ সৈকত",
        "Nature" to "🌲 প্রকৃতি ও পাহাড়",
        "Food" to "🦀 সি-ফুড"
    )

    val filteredPhotos = if (selectedCategory == "All") photos else photos.filter { it.category == selectedCategory }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text("কক্সবাজার গাইড ও ওভারভিউ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Explore Services & Photo Gallery", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                )
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("অ্যাপ ওভারভিউ", fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.testTag("tab_app_overview")
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("ফটো গ্যালারি", fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.testTag("tab_photo_gallery")
                    )
                }
            }
        }
    ) { innerPadding ->
        if (selectedTab == 0) {
            AppOverviewContent(
                onNavigate = { screen -> viewModel.navigateTo(screen) },
                modifier = modifier.padding(innerPadding)
            )
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Category Filter Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { (catKey, label) ->
                    val isSelected = selectedCategory == catKey
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = catKey },
                        label = { Text(label, fontSize = 12.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanPrimary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredPhotos) { photo ->
                    Card(
                        onClick = { viewingPhoto = photo },
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.height(170.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            AppAsyncImage(
                                url = photo.imageUrl,
                                contentDescription = photo.titleBn,
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                color = Color.Black.copy(alpha = 0.55f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                            ) {
                                Text(
                                    text = photo.titleBn,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

    // Full Screen Photo Viewer Dialog
    if (viewingPhoto != null) {
        val photo = viewingPhoto!!
        Dialog(onDismissRequest = { viewingPhoto = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) {
                        AppAsyncImage(
                            url = photo.imageUrl,
                            contentDescription = photo.titleBn,
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            onClick = { viewingPhoto = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Surface(shape = RoundedCornerShape(50), color = Color.Black.copy(alpha = 0.6f)) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.padding(6.dp))
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(photo.titleBn, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = {
                                shareContent(context, photo.titleBn, "কক্সবাজারের মনোরম দৃশ্য")
                            }) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ছবি শেয়ার")
                            }
                        }
                    }
                }
            }
        }
    }
}
