package com.muedsa.hatv.ui.features.detail

import android.content.Intent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import androidx.tv.material3.AssistChip
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.muedsa.compose.tv.model.ContentModel
import com.muedsa.compose.tv.theme.ScreenPaddingLeft
import com.muedsa.compose.tv.widget.ContentBlock
import com.muedsa.compose.tv.widget.ContentBlockType
import com.muedsa.compose.tv.widget.EmptyDataScreen
import com.muedsa.compose.tv.widget.ErrorMessageBoxState
import com.muedsa.compose.tv.widget.ErrorScreen
import com.muedsa.compose.tv.widget.LoadingScreen
import com.muedsa.compose.tv.widget.ScreenBackground
import com.muedsa.compose.tv.widget.ScreenBackgroundType
import com.muedsa.compose.tv.widget.StandardImageCardsRow
import com.muedsa.compose.tv.widget.rememberScreenBackgroundState
import com.muedsa.hatv.PlaybackActivity
import com.muedsa.hatv.model.LazyType
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.ui.navigation.NavigationItems
import com.muedsa.hatv.viewmodel.SearchViewModel
import com.muedsa.hatv.viewmodel.VideoDetailViewModel
import timber.log.Timber
import java.lang.Integer.max

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun VideoDetailScreen(
    viewModel: VideoDetailViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    errorMsgBoxState: ErrorMessageBoxState,
    onNavigate: (NavigationItems, List<String>?) -> Unit = { _, _ -> }
) {
    val videoDetailData by remember { viewModel.videoDetailDataState }

    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    LaunchedEffect(key1 = videoDetailData.type, key2 = videoDetailData.error) {
        if (videoDetailData.type == LazyType.FAILURE) {
            errorMsgBoxState.error(videoDetailData.error)
        }
    }
    if (videoDetailData.type == LazyType.SUCCESS) {
        if (videoDetailData.data != null) {
            val videoDetail = videoDetailData.data!!
            val backgroundState = rememberScreenBackgroundState(
                initUrl = videoDetail.image,
                initType = ScreenBackgroundType.SCRIM
            )
            val playButtonFocusRequester = remember { FocusRequester() }

            ScreenBackground(backgroundState)
            TvLazyColumn(
                modifier = Modifier.offset(x = ScreenPaddingLeft),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    // 占位锚点 使之可以通过Dpad返回页面的顶部
                    Spacer(modifier = Modifier.focusable())
                }
                item {
                    ContentBlock(
                        modifier = Modifier
                            .width(screenWidth / 2)
                            .height(screenHeight * 0.45f),
                        model = ContentModel(
                            title = videoDetail.title,
                            subtitle = videoDetail.author,
                            description = videoDetail.desc
                        ),
                        type = ContentBlockType.CAROUSEL,
                        descriptionMaxLines = 3
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            modifier = Modifier
                                .focusRequester(playButtonFocusRequester)
                                .onFocusChanged {
                                    backgroundState.url = videoDetail.image
                                    backgroundState.type = ScreenBackgroundType.SCRIM
                                },
                            onClick = {
                                FirebaseCrashlytics.getInstance().log(
                                    "try play => \n" +
                                            "id: ${videoDetail.id} \n" +
                                            "title: ${videoDetail.title} \n" +
                                            "url: ${videoDetail.playUrl}"
                                )
                                val intent = Intent(context, PlaybackActivity::class.java)
                                intent.putExtra(PlaybackActivity.MEDIA_URL_KEY, videoDetail.playUrl)
                                context.startActivity(intent)
                            },
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                        ) {
                            Icon(
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                imageVector = Icons.Outlined.PlayArrow,
                                contentDescription = "播放"
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                text = "播放"
                            )
                        }
                    }

                    if (videoDetail.tags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(40.dp))
                        FlowRow(
                            verticalArrangement = Arrangement.Center,
                        ) {
                            videoDetail.tags.forEach {
                                AssistChip(
                                    modifier = Modifier.padding(8.dp),
                                    onClick = {
                                        Timber.d("click tag: $it")
                                        // to search screen
                                        searchViewModel.resetSearch()
                                        searchViewModel.addSearchTag(it)
                                        searchViewModel.fetchSearchVideos()
                                        onNavigate(NavigationItems.Home, listOf("1"))
                                    }
                                ) {
                                    Text(text = it)
                                }
                            }

                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    LaunchedEffect(key1 = Unit) {
                        playButtonFocusRequester.requestFocus()
                    }
                }

                if (videoDetail.videoList.isNotEmpty()) {
                    val selectedIndex =
                        max(videoDetail.videoList.indexOfFirst { it.id == videoDetail.id }, 0)
                    item {
                        StandardImageCardsRow(
                            state = rememberTvLazyListState(
                                initialFirstVisibleItemIndex = selectedIndex
                            ),
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
                                viewModel.videoIdLD.value = video.id
                            }
                        )
                    }
                }

            }
        } else {
            EmptyDataScreen()
        }
    } else if (videoDetailData.type == LazyType.FAILURE) {
        ErrorScreen {
            viewModel.videoIdLD.value = viewModel.videoIdLD.value
        }
    } else {
        LoadingScreen()
    }
}