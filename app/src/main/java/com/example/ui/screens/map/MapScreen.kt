package com.example.ui.screens.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ui.screens.route.RouteSystemScreen
import com.example.viewmodel.CoxViewModel

/**
 * Location Distance & Route System Screen using Leaflet and OpenStreetMap.
 * Displays service locations, destinations, distance calculations, and real-time navigation.
 */
@Composable
fun MapScreen(
    viewModel: CoxViewModel,
    modifier: Modifier = Modifier
) {
    RouteSystemScreen(
        viewModel = viewModel,
        modifier = modifier
    )
}
