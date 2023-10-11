package com.muedsa.hatv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.muedsa.compose.tv.theme.TvTheme
import com.muedsa.compose.tv.widget.AppCloseHandler
import com.muedsa.compose.tv.widget.ErrorMessageBox
import com.muedsa.compose.tv.widget.ErrorMessageBoxState
import com.muedsa.hatv.model.LazyType
import com.muedsa.hatv.ui.navigation.AppNavigation
import com.muedsa.hatv.viewmodel.HomePageViewModel
import com.muedsa.hatv.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homePageViewModel: HomePageViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            homePageViewModel.videosRowsDataState.value.type == LazyType.LOADING
                    || searchViewModel.searchOptionsState.value.type == LazyType.LOADING
        }
        setContent {

            TvTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val errorMsgBoxState = remember { ErrorMessageBoxState() }
                    AppCloseHandler {
                        errorMsgBoxState.error("再次点击返回键退出")
                    }
                    ErrorMessageBox(state = errorMsgBoxState) {
                        AppNavigation(
                            navController = rememberNavController(),
                            errorMsgBoxState = errorMsgBoxState
                        )
                    }

                }
            }
        }
    }
}