package com.muedsa.hatv.ui.features.playback

import androidx.compose.runtime.Composable
import androidx.media3.common.MediaItem
import com.muedsa.compose.tv.widget.player.SimpleVideoPlayer

@Composable
fun PlaybackScreen(
    mediaUrl: String
) {
    SimpleVideoPlayer(init = {
        playWhenReady = true
        setMediaItem(MediaItem.fromUri(mediaUrl))
        prepare()
    })
}