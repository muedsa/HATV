package com.muedsa.compose.tv.widget.player

import EnvConfig
import android.annotation.SuppressLint
import android.view.KeyEvent
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedIconButton
import androidx.tv.material3.Text
import com.kuaishou.akdanmaku.DanmakuConfig
import com.kuaishou.akdanmaku.render.SimpleRenderer
import com.kuaishou.akdanmaku.ui.DanmakuPlayer
import com.kuaishou.akdanmaku.ui.DanmakuView
import com.muedsa.compose.tv.widget.OutlinedIconBox
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@SuppressLint("OpaqueUnitKey")
@OptIn(UnstableApi::class)
@Composable
fun DanmakuVideoPlayer(
    videoPlayerInit: ExoPlayer.() -> Unit,
    danmakuPlayerInit: DanmakuPlayer.() -> Unit
) {

    val context = LocalContext.current

    val playerControlTicker = remember { mutableIntStateOf(0) }

    val danmakuConfig = remember {
        DanmakuConfig().apply {
            textSizeScale = 1.4f
        }
    }

    val danmakuPlayer = remember {
        DanmakuPlayer(SimpleRenderer())
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
            .also {
                it.videoPlayerInit()
                it.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        if (isPlaying) {
                            danmakuPlayer.seekTo(it.currentPosition)
                            danmakuPlayer.start(danmakuConfig)
                        } else {
                            danmakuPlayer.pause()
                        }
                    }
                })
            }
    }

    BackHandler(enabled = playerControlTicker.intValue > 0) {
        playerControlTicker.intValue = 0
    }

    DisposableEffect(
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = {
                PlayerView(context).apply {
                    hideController()
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                }
            })
            AndroidView(factory = {
                DanmakuView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                }.also {
                    danmakuPlayer.bindView(it)
                    danmakuPlayer.danmakuPlayerInit()
                }
            })
        }

    ) {
        onDispose {
            exoPlayer.release()
            danmakuPlayer.release()
        }
    }

    PlayerControl(player = exoPlayer, state = playerControlTicker)
}

@SuppressLint("OpaqueUnitKey")
@OptIn(UnstableApi::class)
@Composable
fun SimpleVideoPlayer(
    init: ExoPlayer.() -> Unit
) {
    val context = LocalContext.current

    val playerControlTicker = remember { mutableIntStateOf(0) }


    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
            .also {
                it.init()
            }
    }

    BackHandler(enabled = playerControlTicker.intValue > 0) {
        playerControlTicker.intValue = 0
    }

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                hideController()
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        })
    ) {
        onDispose {
            exoPlayer.release()
        }
    }

    PlayerControl(player = exoPlayer, state = playerControlTicker)
}

@kotlin.OptIn(ExperimentalTvMaterial3Api::class)
@OptIn(UnstableApi::class)
@Composable
fun PlayerControl(
    player: Player,
    modifier: Modifier = Modifier,
    state: MutableState<Int> = remember { mutableIntStateOf(0) },
) {
    val playButtonFocusRequester = remember { FocusRequester() }
    var leftArrowBtnPressed by remember { mutableStateOf(false) }
    var rightArrowBtnPressed by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1.seconds)
            if (state.value > 0) {
                state.value--
            }
        }
    }

    Box(modifier = modifier
        .focusable()
        .onPreviewKeyEvent {
            if (it.nativeKeyEvent.action == KeyEvent.ACTION_UP) {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_UP
                    || it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                    || it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                    || it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                    || it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                    || it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER
                    || it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_MENU
                    || it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                    || it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
                    || it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
                ) {
                    state.value = 5
                }
            }
            if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    leftArrowBtnPressed = true
                } else if (it.nativeKeyEvent.action == KeyEvent.ACTION_UP) {
                    leftArrowBtnPressed = false
                    player.seekBack()
                }
            } else if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    rightArrowBtnPressed = true
                } else if (it.nativeKeyEvent.action == KeyEvent.ACTION_UP) {
                    rightArrowBtnPressed = false
                    player.seekForward()
                }
            }
            return@onPreviewKeyEvent false
        }
        .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = state.value > 0,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.75f))
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                PlayerProgressIndicator(player)
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    OutlinedIconBox(modifier = Modifier.scale(if (leftArrowBtnPressed) 1.1f else 1f)) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "后退")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    OutlinedIconButton(
                        modifier = Modifier.focusRequester(playButtonFocusRequester),
                        onClick = {
                            if (player.isPlaying) {
                                player.pause()
                            } else {
                                player.play()
                            }
                        }
                    ) {
                        if (player.isPlaying) {
                            Icon(
                                Icons.Outlined.Pause,
                                contentDescription = "暂停"
                            )
                        } else {
                            Icon(Icons.Outlined.PlayArrow, contentDescription = "播放")
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    OutlinedIconBox(modifier = Modifier.scale(if (rightArrowBtnPressed) 1.1f else 1f)) {
                        Icon(Icons.Outlined.ArrowForward, contentDescription = "前进")
                    }
                }

                if (EnvConfig.DEBUG) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "show: $state", color = Color.Red)
                }
            }

            LaunchedEffect(key1 = Unit) {
                playButtonFocusRequester.requestFocus()
            }
        }
    }
}

@Composable
fun PlayerProgressIndicator(player: Player) {
    if (player.duration != 0L) {
        LinearProgressIndicator(
            progress = player.currentPosition.toFloat() / player.duration,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}