package com.muedsa.hatv.ui.features.home.search

import androidx.compose.runtime.Composable
import com.muedsa.compose.tv.widget.ScreenBackgroundState
import com.muedsa.hatv.ui.features.others.FillTextScreen
import com.muedsa.hatv.ui.navigation.NavigationItems

@Composable
fun SearchScreen(
    backgroundState: ScreenBackgroundState,
    onNavigate: (NavigationItems, List<String>?) -> Unit = { _, _ -> }
) {
    FillTextScreen(context = "Not Implement")
}