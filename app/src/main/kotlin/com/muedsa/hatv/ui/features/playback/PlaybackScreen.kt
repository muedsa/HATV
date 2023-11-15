package com.muedsa.hatv.ui.features.playback

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.muedsa.compose.tv.widget.ErrorMessageBox
import com.muedsa.compose.tv.widget.ErrorMessageBoxState
import com.muedsa.compose.tv.widget.player.SimpleVideoPlayer
import com.muedsa.hatv.BuildConfig
import com.muedsa.uitl.LogUtil

@Composable
fun PlaybackScreen(
    mediaUrl: String
) {
    val errorMessageBoxState = remember { ErrorMessageBoxState() }
    ErrorMessageBox(state = errorMessageBoxState) {
        SimpleVideoPlayer(debug = BuildConfig.DEBUG) {
            LogUtil.fd("exoplayer mediaUrl: $mediaUrl")
            addListener(object : Player.Listener {
                override fun onPlayerErrorChanged(error: PlaybackException?) {
                    errorMessageBoxState.error(error, SnackbarDuration.Long)
                    error?.let {
                        LogUtil.fd(it)
                    }
                }
            })
            playWhenReady = true
            setMediaItem(MediaItem.fromUri(mediaUrl))
            prepare()
        }
    }
}