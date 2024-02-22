package com.muedsa.hatv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NonInteractiveSurfaceDefaults
import androidx.tv.material3.Surface
import com.muedsa.compose.tv.theme.TvTheme
import com.muedsa.compose.tv.widget.AppBackHandler
import com.muedsa.compose.tv.widget.ErrorMessageBox
import com.muedsa.compose.tv.widget.ErrorMessageBoxState
import com.muedsa.hatv.ui.features.playback.PlaybackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaybackActivity : ComponentActivity() {

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mediaUrl = intent.getStringExtra(MEDIA_URL_KEY)
        setContent {
            TvTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape,
                    colors = NonInteractiveSurfaceDefaults.colors(
                        containerColor = Color.Black,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {

                    val errorMsgBoxState = remember { ErrorMessageBoxState() }
                    AppBackHandler {
                        errorMsgBoxState.error("再次点击返回键退出")
                    }
                    ErrorMessageBox(state = errorMsgBoxState) {
                        if (!mediaUrl.isNullOrEmpty()) {
                            PlaybackScreen(mediaUrl = mediaUrl)
                        }
                    }
                    LaunchedEffect(key1 = mediaUrl) {
                        if (mediaUrl.isNullOrEmpty()) {
                            errorMsgBoxState.error("视频地址错误")
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val MEDIA_URL_KEY = "MEDIA_URL"
    }
}