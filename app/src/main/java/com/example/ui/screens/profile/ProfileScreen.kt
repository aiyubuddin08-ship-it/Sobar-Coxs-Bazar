package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel
import com.example.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val favoriteIds by viewModel.repository.favoriteIds.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val isEnglish by viewModel.isEnglish.collectAsState()
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()

    var showAboutDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("প্রোফাইল ও সেটিংস", fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Header Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = OceanPrimary,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Avatar",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "ভ্রমণপিপাসু পর্যটক",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "tourist@coxsbazar.com",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SunsetAmber.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "কক্সবাজার ট্রাভেলার সদস্য",
                                    color = SunsetAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Quick Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        onClick = { viewModel.navigateTo(Screen.Favorites) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${favoriteIds.size}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = OceanPrimary
                            )
                            Text("সংরক্ষিত আইটেম", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Card(
                        onClick = { viewModel.navigateTo(Screen.Notifications) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "৪",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = SunsetAmber
                            )
                            Text("বিজ্ঞপ্তি ও নোটিশ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // Settings Group: App Preferences
            item {
                Text("অ্যাপ সেটিংস ও সুবিধা", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Dark Mode Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.DarkMode, contentDescription = null, tint = OceanPrimary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("ডার্ক মোড (Dark Mode)", fontWeight = FontWeight.Medium)
                            }
                            Switch(
                                checked = isDark,
                                onCheckedChange = { viewModel.toggleDarkMode() },
                                modifier = Modifier.testTag("dark_mode_switch")
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                        // Language Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = OceanPrimary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("ভাষা (বাংলা / English)", fontWeight = FontWeight.Medium)
                            }
                            TextButton(onClick = { viewModel.toggleLanguage() }) {
                                Text(if (isEnglish) "English" else "বাংলা", fontWeight = FontWeight.Bold)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                        // Notifications
                        ProfileMenuRow(
                            icon = Icons.Default.Notifications,
                            title = "নোটিফিকেশন সেন্টার",
                            onClick = { viewModel.navigateTo(Screen.Notifications) }
                        )
                    }
                }
            }

            // Admin Panel Section
            item {
                Text("অ্যাডমিন ও ব্যবস্থাপনা", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isAdminLoggedIn) Color(0xFFE8F5E9) else Color(0xFFFFF8E1)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isAdminLoggedIn) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isAdminLoggedIn) Color(0xFF2E7D32) else Color(0xFFF57F17),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAdminLoggedIn) "অ্যাডমিন ড্যাশবোর্ড (সক্রিয়)" else "অ্যাডমিন প্যানেল লগইন",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isAdminLoggedIn) "হোটেল, স্পট ও নোটিশ পরিচালনা করুন" else "কন্টেন্ট ও সেবা পরিচালনার অ্যাডমিন পোর্টাল",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Button(
                            onClick = {
                                if (isAdminLoggedIn) viewModel.navigateTo(Screen.AdminDashboard)
                                else viewModel.navigateTo(Screen.AdminLogin)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanPrimary)
                        ) {
                            Text(if (isAdminLoggedIn) "ড্যাশবোর্ড" else "লগইন")
                        }
                    }
                }
            }

            // Information & Support Group
            item {
                Text("তথ্য ও সহায়তা", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        ProfileMenuRow(icon = Icons.Default.Info, title = "কক্সবাজার অ্যাপ সম্পর্কে", onClick = { showAboutDialog = true })
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileMenuRow(icon = Icons.Default.Help, title = "সহায়তা ও প্রশ্নোত্তর (FAQ)", onClick = { showHelpDialog = true })
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileMenuRow(icon = Icons.Default.PrivacyTip, title = "প্রাইভেসি পলিসি ও নীতিমালা", onClick = { showPrivacyDialog = true })
                    }
                }
            }
        }
    }

    // Dialogs
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("কক্সবাজার - Discover Cox's Bazar") },
            text = {
                Text("কক্সবাজারের পর্যটন শিল্পকে ডিজিটাল ও আধুনিক করতে তৈরি একটি পূর্ণাঙ্গ স্মার্ট পর্যটন প্ল্যাটফর্ম। বিশ্বের দীর্ঘতম ১২০ কিমি বালুকাময় সমুদ্র সৈকত, বিলাসবহুল রিসোর্ট, সুস্বাদু সামুদ্রিক খাবার ও পর্যটকদের সম্পূর্ণ সুরক্ষায় এই অ্যাপ সদা প্রস্তুত।\n\nভার্সন: ১.০.০ (রেডি ফর এপিআই ও ক্লাউড ইন্টিগ্রেশন)")
            },
            confirmButton = { Button(onClick = { showAboutDialog = false }) { Text("ঠিক আছে") } }
        )
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("সহায়তা ও জরুরি হেল্পডেস্ক") },
            text = {
                Text("ভ্রমণ সংক্রান্ত যেকোনো জিজ্ঞাসায় বা জরুরি প্রয়োজনে:\n\n• জাতীয় জরুরি হেল্পলাইন: ৯৯৯\n• ট্যুরিস্ট পুলিশ হেডকোয়ার্টার: +8801320-169999\n• লাইফগার্ড কন্ট্রোল: +8801815123456\n• জেলা প্রশাসন তথ্য সেবা: ৩৩৩")
            },
            confirmButton = { Button(onClick = { showHelpDialog = false }) { Text("বুঝেছি") } }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("প্রাইভেসি ও শর্তাবলী") },
            text = {
                Text("কক্সবাজার পর্যটন অ্যাপ ব্যবহারকারীর কোনো ব্যক্তিগত সংবেদনশীল তথ্য তৃতীয় পক্ষের সাথে বিনিময় করে না। আপনার পছন্দের তালিকা ও অনুসন্ধানের তথ্য আপনার ডিভাইসেই সুরক্ষিত থাকে।")
            },
            confirmButton = { Button(onClick = { showPrivacyDialog = false }) { Text("ঠিক আছে") } }
        )
    }
}

@Composable
fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = OceanPrimary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
