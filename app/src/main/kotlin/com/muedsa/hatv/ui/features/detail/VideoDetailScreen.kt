package com.muedsa.hatv.ui.features.detail

import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.muedsa.compose.tv.model.ContentModel
import com.muedsa.compose.tv.widget.ContentBlock
import com.muedsa.compose.tv.widget.ContentBlockType
import com.muedsa.compose.tv.widget.ScreenBackground
import com.muedsa.compose.tv.widget.ScreenBackgroundType
import com.muedsa.compose.tv.widget.StandardImageCardsRow
import com.muedsa.compose.tv.widget.rememberScreenBackgroundState
import com.muedsa.hatv.PlaybackActivity
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.ui.navigation.NavigationItems
import com.muedsa.hatv.viewmodel.VideoDetailViewModel
import timber.log.Timber

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VideoDetailScreen(
    viewModel: VideoDetailViewModel = hiltViewModel()
) {
    val videoDetail = viewModel.videoDetail.observeAsState(initial = null).value

    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
   

    if (videoDetail != null) {
        val backgroundState = rememberScreenBackgroundState(
            initUrl = videoDetail.image,
            initType = ScreenBackgroundType.SCRIM
        )
        val playButtonFocusRequester = remember { FocusRequester() }
        
        ScreenBackground(backgroundState)
        TvLazyColumn(Modifier.offset(x = 50.dp)) {
            item {
                ContentBlock(
                    modifier = Modifier
                        .offset(x = 8.dp)
                        .width(screenWidth / 2)
                        .height(screenHeight - 150.dp - 75.dp - 48.dp),
                    model = ContentModel(
                        title = videoDetail.title,
                        subtitle = videoDetail.author,
                        description = videoDetail.desc
                    ),
                    type = ContentBlockType.CAROUSEL
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        modifier = Modifier.focusRequester(playButtonFocusRequester),
                        onClick = {
                            context.startActivity(Intent(context, PlaybackActivity::class.java))
                        },
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                    ) {
                        Icon(
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            imageVector = Icons.Outlined.PlayArrow,
                            contentDescription = "播放"
                        )
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "播放")
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))

                LaunchedEffect(key1 = Unit) {
                    playButtonFocusRequester.requestFocus()
                }
            }
            
            if (videoDetail.videoList.isNotEmpty()) {
                item {
                    StandardImageCardsRow(
                        title = "视频列表",
                        modelList = videoDetail.videoList,
                        imageFn = VideoInfoModel::image,
                        contentFn = { ContentModel(it.title, subtitle = it.author) },
                        onItemFocus = { _, video ->
                            backgroundState.url = video.image
                            backgroundState.type = ScreenBackgroundType.BLUR
                        },
                        onItemClick = { _, video ->
                            Timber.d("Click $video")
                            viewModel.fetchVideoDetail(video.id)
                        }
                    )
                }
            }
            
        }
    }
    
    
}