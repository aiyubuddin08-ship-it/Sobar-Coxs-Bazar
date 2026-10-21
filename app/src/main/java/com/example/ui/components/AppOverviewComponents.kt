package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.Screen

data class OverviewModuleItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val descriptionBn: String,
    val icon: ImageVector,
    val iconTint: Color,
    val bgColor: Color,
    val targetScreen: Screen,
    val badgeBn: String
)

val overviewModulesList = listOf(
    OverviewModuleItem(
        id = "mod_spots",
        titleBn = "পর্যটন স্পট",
        titleEn = "Tourist Attractions",
        descriptionBn = "লাবণী, সুগন্ধা, কলাতলী, ইনানী বিচ, হিমছড়ি ঝর্ণা, সেন্টমার্টিন ও ডায়নামিক স্পট গাইড।",
        icon = Icons.Default.BeachAccess,
        iconTint = Color(0xFF007791),
        bgColor = OceanPrimaryContainer,
        targetScreen = Screen.Spots,
        badgeBn = "১০+ স্পট"
    ),
    OverviewModuleItem(
        id = "mod_hotels",
        titleBn = "হোটেল ও রিসোর্ট",
        titleEn = "Hotels & Stays",
        descriptionBn = "বিচ ভিউ ফাইভ স্টার রিসোর্ট, সাশ্রয়ী হোটেল ও কটেজের ভাড়া, সুবিধা ও ফোন বুকিং।",
        icon = Icons.Default.Hotel,
        iconTint = Color(0xFFE65100),
        bgColor = SunsetContainer,
        targetScreen = Screen.Hotels,
        badgeBn = "বুকিং তথ্য"
    ),
    OverviewModuleItem(
        id = "mod_restaurants",
        titleBn = "খাবার ও রেস্তোরাঁ",
        titleEn = "Food & Dining",
        descriptionBn = "তাজা সামুদ্রিক রূপচাঁদা, লইট্টা ফ্রাই, কাঁকড়া, ঐতিহ্যবাহী মেজবানি ও সেরা রেস্তোরাঁ।",
        icon = Icons.Default.Restaurant,
        iconTint = Color(0xFFC2185B),
        bgColor = Color(0xFFFCE4EC),
        targetScreen = Screen.Restaurants,
        badgeBn = "সি-ফুড"
    ),
    OverviewModuleItem(
        id = "mod_map",
        titleBn = "ইন্টারঅ্যাক্টিভ ম্যাপ",
        titleEn = "Interactive Map",
        descriptionBn = "সমস্ত পর্যটন স্পট, হোটেল ও জরুরি সেন্টারের অবস্থান এবং গুগল ম্যাপ ডিরেকশন।",
        icon = Icons.Default.Map,
        iconTint = Color(0xFF2E7D32),
        bgColor = Color(0xFFE8F5E9),
        targetScreen = Screen.Map,
        badgeBn = "জিপিএস রুট"
    ),
    OverviewModuleItem(
        id = "mod_safety",
        titleBn = "সৈকত সুরক্ষা ও লাইভ ফ্ল্যাগ",
        titleEn = "Beach Safety & Live Flag",
        descriptionBn = "লাল/সবুজ পতাকার সতর্কতা, লাইভ জোয়ার-ভাটা সময়সূচী ও নিরাপদ গোসলের নিয়মাবলী।",
        icon = Icons.Default.Security,
        iconTint = Color(0xFF00695C),
        bgColor = Color(0xFFE0F2F1),
        targetScreen = Screen.BeachSafety,
        badgeBn = "লাইভ অ্যালার্ট"
    ),
    OverviewModuleItem(
        id = "mod_emergency",
        titleBn = "জরুরি সহায়তা ও পুলিশ",
        titleEn = "Emergency Helpline",
        descriptionBn = "ট্যুরিস্ট পুলিশ, সদর হাসপাতাল, অ্যাম্বুলেন্স, ফায়ার সার্ভিস ও ওয়ান-ট্যাপ কল।",
        icon = Icons.Default.Emergency,
        iconTint = Color(0xFFC62828),
        bgColor = Color(0xFFFFEBEE),
        targetScreen = Screen.Emergency,
        badgeBn = "২৪/৭ হটলাইন"
    ),
    OverviewModuleItem(
        id = "mod_transport",
        titleBn = "যাতায়াত ও ভাড়া তালিকা",
        titleEn = "Transport & Fare Guide",
        descriptionBn = "মেরিন ড্রাইভ খোলা জিপ, অটো-রিকশা, সিএনজি ও সরকারি নির্ধারিত ভাড়ার চার্ট।",
        icon = Icons.Default.DirectionsBus,
        iconTint = Color(0xFF0277BD),
        bgColor = Color(0xFFE1F5FE),
        targetScreen = Screen.Transport,
        badgeBn = "নির্ধারিত ভাড়া"
    ),
    OverviewModuleItem(
        id = "mod_shopping",
        titleBn = "কেনাকাটা ও বার্মিজ মার্কেট",
        titleEn = "Shopping & Burmese Market",
        descriptionBn = "বিখ্যাত বার্মিজ আচার, রাখাইন তাঁত বস্ত্র, সামুদ্রিক শুঁটকি ও আসল ঝিনুকের অলঙ্কার।",
        icon = Icons.Default.ShoppingBag,
        iconTint = Color(0xFF6A1B9A),
        bgColor = Color(0xFFF3E5F5),
        targetScreen = Screen.Shopping,
        badgeBn = "স্থানীয় পণ্য"
    ),
    OverviewModuleItem(
        id = "mod_weather",
        titleBn = "আবহাওয়া ও জোয়ার-ভাটা",
        titleEn = "Weather & Tides",
        descriptionBn = "সরাসরি বর্তমান তাপমাত্রা, বাতাসের গতি, আর্দ্রতা ও সাগরের ঢেউয়ের পূর্বাভাস।",
        icon = Icons.Default.WbSunny,
        iconTint = Color(0xFFF57F17),
        bgColor = Color(0xFFFFFDE7),
        targetScreen = Screen.Weather,
        badgeBn = "পূর্বাভাস"
    ),
    OverviewModuleItem(
        id = "mod_news",
        titleBn = "সংবাদ ও পর্যটন নোটিশ",
        titleEn = "News & Notices",
        descriptionBn = "জেলা প্রশাসন ও পর্যটন বোর্ডের জরুরি নির্দেশনা, উৎসব এবং গুরুত্বপূর্ণ খবর।",
        icon = Icons.Default.Article,
        iconTint = Color(0xFF455A64),
        bgColor = Color(0xFFECEFF1),
        targetScreen = Screen.News,
        badgeBn = "আপডেট"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppOverviewBottomSheet(
    onDismissRequest: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.fillMaxHeight(0.92f)
    ) {
        AppOverviewContent(
            onNavigate = { screen ->
                onDismissRequest()
                onNavigate(screen)
            },
            onClose = onDismissRequest
        )
    }
}

@Composable
fun AppOverviewContent(
    onNavigate: (Screen) -> Unit,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "কক্সবাজার অ্যাপ ওভারভিউ",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "অ্যাপের সকল সেবা ও ফিচার এক নজরে (Complete Guide)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (onClose != null) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("overview_close_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }
        }

        // Summary Statistics Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OceanPrimary.copy(alpha = 0.08f),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(OceanPrimary.copy(alpha = 0.4f), SunsetAmber.copy(alpha = 0.4f)))
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OverviewStatItem(number = "১০+", label = "প্রধান সেবা")
                    Divider(modifier = Modifier.height(36.dp).width(1.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    OverviewStatItem(number = "১০০%", label = "অফলাইন রেডি")
                    Divider(modifier = Modifier.height(36.dp).width(1.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    OverviewStatItem(number = "২৪/৭", label = "জরুরি সহায়তা")
                }
            }
        }

        item {
            Text(
                text = "অ্যাপ্লিকেশন মডিউলসমূহ (১০টি সেবা)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        items(overviewModulesList, key = { it.id }) { module ->
            OverviewModuleCard(
                item = module,
                onClick = { onNavigate(module.targetScreen) }
            )
        }
    }
}

@Composable
fun OverviewStatItem(
    number: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = OceanPrimary
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun OverviewModuleCard(
    item: OverviewModuleItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("overview_card_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = item.bgColor,
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.titleBn,
                        tint = item.iconTint,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.titleBn,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = item.bgColor
                    ) {
                        Text(
                            text = item.badgeBn,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = item.iconTint,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.descriptionBn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
