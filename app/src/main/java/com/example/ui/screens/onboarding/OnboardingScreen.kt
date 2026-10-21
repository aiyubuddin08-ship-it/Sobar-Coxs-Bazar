package com.example.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OceanPrimary
import com.example.ui.theme.SunsetAmber
import kotlinx.coroutines.launch

data class OnboardingPageData(
    val titleBn: String,
    val titleEn: String,
    val descriptionBn: String,
    val icon: ImageVector,
    val iconBgColor: Color
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pages = listOf(
        OnboardingPageData(
            titleBn = "কক্সবাজারের অপূর্ব পর্যটন স্পট আবিষ্কার করুন",
            titleEn = "Discover Beautiful Places in Cox's Bazar",
            descriptionBn = "লাবণী, সুগন্ধা, ইনানী বিচ ও মেরিন ড্রাইভের মতো বিশ্বের দীর্ঘতম সমুদ্র সৈকত ও মনোরম পাহাড় অন্বেষণ করুন।",
            icon = Icons.Default.BeachAccess,
            iconBgColor = Color(0xFF007791)
        ),
        OnboardingPageData(
            titleBn = "সেরা হোটেল ও রেস্তোরাঁর সহজ সন্ধান",
            titleEn = "Find Hotels & Seafood Restaurants",
            descriptionBn = "বিশ্বমানের সী-ভিউ রিসোর্ট এবং তাজা সামুদ্রিক মাছের আসল স্বাদ পেতে সেরা ডাইনিং পয়েন্ট খুঁজে নিন।",
            icon = Icons.Default.Hotel,
            iconBgColor = Color(0xFFF77F00)
        ),
        OnboardingPageData(
            titleBn = "নিরাপদে ভ্রমণ ও সৈকত সতর্কতা জানুন",
            titleEn = "Explore, Travel and Stay Safe",
            descriptionBn = "জোয়ার-ভাটার সময়সূচী, লাল ও সবুজ পতাকার নির্দেশনা এবং ট্যুরিস্ট পুলিশের জরুরি সহায়তা সবসময় আপনার সাথে।",
            icon = Icons.Default.Security,
            iconBgColor = Color(0xFF2A9D8F)
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (pagerState.currentPage < pages.size - 1) {
                    TextButton(
                        onClick = onFinish,
                        modifier = Modifier.testTag("onboarding_skip_button")
                    ) {
                        Text(
                            text = "এড়িয়ে যান (Skip)",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(8.dp)
                                .width(if (isSelected) 28.dp else 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                        )
                    }
                }

                // Next or Get Started Button
                if (pagerState.currentPage == pages.size - 1) {
                    Button(
                        onClick = onFinish,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("onboarding_start_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = OceanPrimary)
                    ) {
                        Text(
                            text = "শুরু করুন (Get Started)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("onboarding_next_button")
                    ) {
                        Text(
                            text = "পরবর্তী (Next)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            val pageData = pages[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Large Rounded Artwork Container
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    color = pageData.iconBgColor.copy(alpha = 0.12f),
                    modifier = Modifier
                        .size(220.dp)
                        .padding(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Surface(
                            shape = CircleShape,
                            color = pageData.iconBgColor,
                            modifier = Modifier.size(110.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = pageData.icon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = pageData.titleBn,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = pageData.titleEn,
                    style = MaterialTheme.typography.titleSmall,
                    color = SunsetAmber,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = pageData.descriptionBn,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
