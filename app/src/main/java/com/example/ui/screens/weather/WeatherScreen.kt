package com.example.ui.screens.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WeatherForecastDay
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import com.example.viewmodel.CoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    val weather by viewModel.repository.weather.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("কক্সবাজারের আবহাওয়া", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Cox's Bazar Coastal Weather", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            // Main Weather Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF007791), Color(0xFF0F3057))
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "বঙ্গোপসাগর উপকূল • কক্সবাজার",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = weather.currentTemp,
                                color = Color.White,
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = weather.conditionBn,
                                color = Color(0xFFFFE082),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = weather.conditionEn,
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                WeatherMetricItem(title = "আর্দ্রতা", value = weather.humidity)
                                WeatherMetricItem(title = "বাতাস", value = weather.wind)
                                WeatherMetricItem(title = "বৃষ্টির সম্ভাবনা", value = weather.rainChance)
                            }
                        }
                    }
                }
            }

            // Sunrise & Sunset Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.WbTwilight, contentDescription = null, tint = SunsetAmber)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("সূর্যোদয়", fontSize = 12.sp, color = Color(0xFFE65100))
                                Text(weather.sunrise, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFE65100))
                            }
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBE9E7)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.WbSunny, contentDescription = null, tint = Color(0xFFD84315))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("সূর্যাস্ত", fontSize = 12.sp, color = Color(0xFFD84315))
                                Text(weather.sunset, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFD84315))
                            }
                        }
                    }
                }
            }

            // 5-Day Forecast Section Header
            item {
                Text(
                    text = "৫ দিনের উপকূলীয় আবহাওয়ার পূর্বাভাস",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Forecast Days List
            items(weather.forecasts) { forecast ->
                ForecastRowCard(forecast = forecast)
            }
        }
    }
}

@Composable
fun WeatherMetricItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, color = Color.White.copy(alpha = 0.75f), fontSize = 11.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
fun ForecastRowCard(forecast: WeatherForecastDay) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(forecast.dayBn, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(forecast.dayEn, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = null,
                    tint = OceanPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(forecast.conditionBn, style = MaterialTheme.typography.bodySmall)
            }
            Row {
                Text(
                    text = forecast.tempMax,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "/ ${forecast.tempMin}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
