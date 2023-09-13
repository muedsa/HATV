package com.muedsa.hatv.ui.features.home

import androidx.compose.runtime.Composable
import com.muedsa.compose.tv.widget.ScreenBackground
import com.muedsa.compose.tv.widget.rememberScreenBackgroundState
import com.muedsa.hatv.ui.navigation.NavigationItems

@Composable
fun HomeScreen(
    onNavigate: (NavigationItems, List<String>?) -> Unit = { _, _ -> },
) {
    val backgroundState = rememberScreenBackgroundState()
    ScreenBackground(state = backgroundState)
}