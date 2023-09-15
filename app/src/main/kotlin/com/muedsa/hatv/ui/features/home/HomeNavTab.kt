package com.muedsa.hatv.ui.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabDefaults
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.muedsa.compose.tv.widget.ScreenBackgroundState
import com.muedsa.compose.tv.widget.ScreenBackgroundType
import com.muedsa.hatv.ui.features.home.browser.BrowserScreen
import com.muedsa.hatv.ui.features.home.search.SearchScreen
import com.muedsa.hatv.ui.features.others.NotFoundScreen
import com.muedsa.hatv.ui.navigation.NavigationItems
import com.muedsa.hatv.viewmodel.HomePageViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

val tabs = listOf(
    HomeNavTabs.Browser,
    HomeNavTabs.Search
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeNavTab(
    homePageViewModel: HomePageViewModel,
    backgroundState: ScreenBackgroundState = ScreenBackgroundState(),
    onNavigate: (NavigationItems, List<String>?) -> Unit = { _, _ -> },
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    var tabPanelIndex by remember { mutableIntStateOf(selectedTabIndex) }

    LaunchedEffect(selectedTabIndex) {
        delay(150.milliseconds)
        tabPanelIndex = selectedTabIndex
    }

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 24.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onFocus = {
                        if (selectedTabIndex != index) {
                            backgroundState.url = null
                            backgroundState.type = ScreenBackgroundType.BLUR
                            selectedTabIndex = index
                        }
                    },
                    colors = TabDefaults.pillIndicatorTabColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        selectedContentColor = MaterialTheme.colorScheme.onBackground,
                        focusedContentColor = MaterialTheme.colorScheme.onBackground,
                        focusedSelectedContentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        tab.title,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
        HomeContent(
            tabPanelIndex,
            homePageViewModel,
            backgroundState,
            onNavigate
        )
    }
}

@Composable
fun HomeContent(
    tabIndex: Int,
    homePageViewModel: HomePageViewModel,
    backgroundState: ScreenBackgroundState,
    onNavigate: (NavigationItems, List<String>?) -> Unit = { _, _ -> },
) {
    when (tabIndex) {
        0 -> BrowserScreen(
            viewModel = homePageViewModel,
            backgroundState = backgroundState,
            onNavigate = onNavigate
        )
        1 -> SearchScreen(
            backgroundState = backgroundState,
            onNavigate = onNavigate
        )
        else -> NotFoundScreen()
    }
}