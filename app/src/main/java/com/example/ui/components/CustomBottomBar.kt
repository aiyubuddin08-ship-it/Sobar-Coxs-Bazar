package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.viewmodel.Screen

sealed class BottomNavItem(
    val titleBn: String,
    val titleEn: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val screen: Screen,
    val tag: String
) {
    object Home : BottomNavItem(
        titleBn = "হোম",
        titleEn = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        screen = Screen.Home,
        tag = "nav_home"
    )

    object Explore : BottomNavItem(
        titleBn = "ওভারভিউ",
        titleEn = "Overview",
        selectedIcon = Icons.Filled.Dashboard,
        unselectedIcon = Icons.Outlined.Dashboard,
        screen = Screen.Explore,
        tag = "nav_overview"
    )

    object Map : BottomNavItem(
        titleBn = "রুট ও ম্যাপ",
        titleEn = "Route & Map",
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map,
        screen = Screen.Map,
        tag = "nav_map"
    )

    object Favorites : BottomNavItem(
        titleBn = "সংরক্ষিত",
        titleEn = "Favorites",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        screen = Screen.Favorites,
        tag = "nav_favorites"
    )

    object Profile : BottomNavItem(
        titleBn = "প্রোফাইল",
        titleEn = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        screen = Screen.Profile,
        tag = "nav_profile"
    )
}

@Composable
fun CustomBottomBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.Map,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    NavigationBar(
        modifier = modifier.navigationBarsPadding(),
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            val isSelected = when (item) {
                BottomNavItem.Home -> currentScreen is Screen.Home
                BottomNavItem.Explore -> currentScreen is Screen.Explore
                BottomNavItem.Map -> currentScreen is Screen.Map
                BottomNavItem.Favorites -> currentScreen is Screen.Favorites
                BottomNavItem.Profile -> currentScreen is Screen.Profile
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.titleBn
                    )
                },
                label = {
                    Text(
                        text = item.titleBn,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.tag)
            )
        }
    }
}
