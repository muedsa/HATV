package com.muedsa.hatv.ui.features.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.muedsa.compose.tv.widget.ErrorMessageBoxState
import com.muedsa.compose.tv.widget.ScreenBackground
import com.muedsa.compose.tv.widget.rememberScreenBackgroundState
import com.muedsa.hatv.ui.navigation.NavigationItems
import com.muedsa.hatv.viewmodel.HomePageViewModel

@Composable
fun HomeScreen(
    homePageViewModel: HomePageViewModel = hiltViewModel(),
    errorMsgBoxState: ErrorMessageBoxState,
    onNavigate: (NavigationItems, List<String>?) -> Unit = { _, _ -> },
) {
    val backgroundState = rememberScreenBackgroundState()
    ScreenBackground(state = backgroundState)
    HomeNavTab(
        homePageViewModel = homePageViewModel,
        backgroundState = backgroundState,
        errorMsgBoxState = errorMsgBoxState,
        onNavigate = onNavigate
    )
}