package com.example.ui.screens.safety

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BeachFlag
import com.example.ui.components.dialNumber
import com.example.ui.theme.CoralRed
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeachSafetyScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val safetyInfo by viewModel.repository.safetyInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("সৈকত নিরাপত্তা ও নির্দেশনা", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Beach Safety & Life Saving Guidelines", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Beach Flag Indicator
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (safetyInfo.flag == BeachFlag.GREEN) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = if (safetyInfo.flag == BeachFlag.GREEN) Color(0xFF2E7D32) else CoralRed,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Flag,
                                        contentDescription = "Flag",
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "বর্তমান পতাকা স্ট্যাটাস",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = safetyInfo.flagTitleBn,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (safetyInfo.flag == BeachFlag.GREEN) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = safetyInfo.flagDescriptionBn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Flag Meaning Guide
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "পতাকার অর্থ জানুন (Beach Flag Signs)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FlagMeaningRow(
                            color = Color(0xFF2E7D32),
                            title = "সবুজ পতাকা (Green Flag):",
                            desc = "সাগর শান্ত ও অনুকূলে। লাইফগার্ড নজরদারির সীমানায় সাঁতারের অনুমতি রয়েছে।"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlagMeaningRow(
                            color = SunsetAmber,
                            title = "হলুদ পতাকা (Yellow Flag):",
                            desc = "সতর্কতা! ঢেউ মাঝারি বা হালকা আন্ডার-কারেন্ট রয়েছে। কোমর পানির বেশি নামবেন না।"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlagMeaningRow(
                            color = CoralRed,
                            title = "লাল পতাকা (Red Flag):",
                            desc = "বিপদ! সাগরে নামা সম্পূর্ণ নিষিদ্ধ ও শাস্তিযোগ্য অপরাধ। চোরাবালি বা বড় ঢেউয়ের পূর্বাভাস।"
                        )
                    }
                }
            }

            // Tide Schedule Box (High Tide / Low Tide)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.WaterDrop, contentDescription = null, tint = OceanPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "আজকের জোয়ার-ভাটা সময়সূচী",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = OceanPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🌊 পূর্ণ জোয়ার (High Tide)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(safetyInfo.highTideTime, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = OceanPrimary)
                                Text("সাঁতারের সবচেয়ে উপযুক্ত সময়", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("⚠️ পূর্ণ ভাটা (Low Tide)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = CoralRed)
                                Text(safetyInfo.lowTideTime, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = CoralRed)
                                Text("সাগরে নামা বিপজ্জনক", fontSize = 10.sp, color = CoralRed)
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = safetyInfo.tideWarningBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF01579B)
                        )
                    }
                }
            }

            // Night Swimming Warning (Crucial Safety Rule)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF263238)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NightsStay,
                            contentDescription = null,
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "সূর্যাস্তের পর সাগরে নামা নিষিদ্ধ!",
                                color = Color(0xFFFFD54F),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = safetyInfo.nightSwimmingWarningBn,
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // Beach Safety Golden Rules
            item {
                Text(
                    text = "সৈকত সুরক্ষার ৭টি সুবর্ণ নিয়ম",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(safetyInfo.safetyTips) { tip ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = OceanPrimary,
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = tip,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Lifeguard Emergency Call
            item {
                Button(
                    onClick = { dialNumber(context, safetyInfo.lifeguardHotline) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.HealthAndSafety, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("লাইফগার্ড কন্ট্রোল হেল্পলাইন কল দিন", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FlagMeaningRow(
    color: Color,
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Surface(
            shape = CircleShape,
            color = color,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 4.dp)
        ) {}
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = color)
            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
