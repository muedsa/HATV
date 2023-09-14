package com.muedsa.hatv.ui.features.home.browser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ImmersiveList
import com.muedsa.compose.tv.model.ContentModel
import com.muedsa.compose.tv.widget.ContentBlock
import com.muedsa.compose.tv.widget.ImageCardsRow
import com.muedsa.compose.tv.widget.ScreenBackgroundState
import com.muedsa.compose.tv.widget.ScreenBackgroundType
import com.muedsa.compose.tv.widget.StandardImageCardsRow
import com.muedsa.hatv.viewmodel.HomePageViewModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.ui.features.others.FillTextScreen
import com.muedsa.hatv.ui.navigation.NavigationItems
import timber.log.Timber

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun BrowserScreen(
    viewModel: HomePageViewModel,
    backgroundState: ScreenBackgroundState,
    onNavigate: (NavigationItems, List<String>?) -> Unit = { _, _ -> }
) {
    val videosRows by viewModel.videosRows.observeAsState(initial = emptyList())

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var title by remember { mutableStateOf("") }
    var subTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val contentModel = ContentModel(title = title, subtitle = subTitle, description = description)

    if (videosRows.isNotEmpty()) {

        LaunchedEffect(key1 = Unit) {
            if (videosRows[0].videos.isNotEmpty()) {
                backgroundState.url = videosRows[0].videos[0].image
                backgroundState.type = ScreenBackgroundType.SCRIM
            }
        }

        TvLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = 50.dp)
        ) {
            itemsIndexed(
                items = videosRows,
                key = { _, item ->
                    item.title
                }
            ) { index, item ->
                if (index == 0) {
                    ImmersiveList(
                        background = { _, _ ->
                            ContentBlock(
                                modifier = Modifier
                                    .offset(x = 8.dp)
                                    .width(screenWidth / 2)
                                    .height(screenHeight - 150.dp - 75.dp - 48.dp),
                                model = contentModel,
                                descriptionMaxLines = 3
                            )
                        },
                    ) {
                        Column {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(screenHeight - 150.dp - 75.dp)
                            )
                            ImageCardsRow(
                                title = item.title,
                                modelList = item.videos,
                                imageFn = VideoInfoModel::image,
                                onItemFocus = { _, video ->
                                    title = video.title
                                    subTitle = video.author
                                    description = video.desc

                                    backgroundState.url = video.image
                                    backgroundState.type = ScreenBackgroundType.SCRIM
                                },
                                onItemClick = { _, video ->
                                    Timber.d("Click $video")
                                    onNavigate(NavigationItems.Detail, listOf(video.id))
                                }
                            )
                        }
                    }
                } else {
                    StandardImageCardsRow(
                        title = item.title,
                        modelList = item.videos,
                        imageFn = VideoInfoModel::image,
                        contentFn = { ContentModel(it.title, subtitle = it.author) },
                        onItemFocus = { _, video ->
                            title = video.title
                            subTitle = video.author
                            description = video.desc
                            backgroundState.url = video.image
                            backgroundState.type = ScreenBackgroundType.BLUR
                        },
                        onItemClick = { _, video ->
                            Timber.d("Click $video")
                            onNavigate(NavigationItems.Detail, listOf(video.id))
                        }
                    )
                }

            }
        }
    } else {
        FillTextScreen("Loading...")
    }
}