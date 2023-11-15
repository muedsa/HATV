package com.muedsa.hatv.ui.features.home.browser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.tv.material3.MaterialTheme
import com.muedsa.compose.tv.model.ContentModel
import com.muedsa.compose.tv.theme.HorizontalPosterSize
import com.muedsa.compose.tv.theme.ImageCardRowCardPadding
import com.muedsa.compose.tv.theme.ScreenPaddingLeft
import com.muedsa.compose.tv.theme.VerticalPosterSize
import com.muedsa.compose.tv.widget.ContentBlock
import com.muedsa.compose.tv.widget.EmptyDataScreen
import com.muedsa.compose.tv.widget.ErrorMessageBoxState
import com.muedsa.compose.tv.widget.ErrorScreen
import com.muedsa.compose.tv.widget.ImageCardsRow
import com.muedsa.compose.tv.widget.LoadingScreen
import com.muedsa.compose.tv.widget.ScreenBackgroundState
import com.muedsa.compose.tv.widget.ScreenBackgroundType
import com.muedsa.compose.tv.widget.StandardImageCardsRow
import com.muedsa.hatv.model.LazyType
import com.muedsa.hatv.ui.navigation.NavigationItems
import com.muedsa.hatv.viewmodel.HomePageViewModel
import com.muedsa.uitl.LogUtil

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun BrowserScreen(
    viewModel: HomePageViewModel,
    backgroundState: ScreenBackgroundState,
    errorMsgBoxState: ErrorMessageBoxState,
    onNavigate: (NavigationItems, List<String>?) -> Unit = { _, _ -> }
) {
    val videosRowsLD by viewModel.videosRowsLDSF.collectAsState()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var title by remember { mutableStateOf("") }
    var subTitle by remember { mutableStateOf<String?>(null) }
    var description by remember { mutableStateOf<String?>(null) }

    val contentModel = ContentModel(title = title, subtitle = subTitle, description = description)

    LaunchedEffect(key1 = videosRowsLD.type, key2 = videosRowsLD.error) {
        if (videosRowsLD.type == LazyType.FAILURE) {
            errorMsgBoxState.error(videosRowsLD.error)
        }
    }
    if (videosRowsLD.type == LazyType.SUCCESS) {
        if (!videosRowsLD.data.isNullOrEmpty()) {
            val videosRows = videosRowsLD.data!!
            val firstRowHeight =
                (MaterialTheme.typography.titleLarge.fontSize.value * configuration.fontScale + 0.5f).dp +
                        ImageCardRowCardPadding * 3 +
                        if (videosRowsLD.data!![0].horizontalVideoImage)
                            HorizontalPosterSize.height
                        else VerticalPosterSize.height
            val tabHeight =
                (MaterialTheme.typography.labelLarge.fontSize.value * configuration.fontScale + 0.5f).dp +
                        24.dp * 2 +
                        6.dp * 2
            TvLazyColumn(
                modifier = Modifier
                    .offset(x = ScreenPaddingLeft - ImageCardRowCardPadding),
                contentPadding = PaddingValues(bottom = 100.dp)
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
                                        .width(screenWidth / 2)
                                        .height(screenHeight - firstRowHeight - tabHeight - 20.dp),
                                    model = contentModel,
                                    descriptionMaxLines = 3
                                )
                            },
                        ) {
                            Column {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(screenHeight - firstRowHeight - tabHeight)
                                )
                                ImageCardsRow(
                                    title = item.title,
                                    modelList = item.videos,
                                    imageFn = { _, m -> m.image },
                                    imageSize = if (item.horizontalVideoImage) HorizontalPosterSize else VerticalPosterSize,
                                    onItemFocus = { _, video ->
                                        title = video.title
                                        subTitle = video.author
                                        description = video.desc

                                        backgroundState.url = video.image
                                        backgroundState.type = ScreenBackgroundType.SCRIM
                                    },
                                    onItemClick = { _, video ->
                                        LogUtil.d("Click $video")
                                        onNavigate(NavigationItems.Detail, listOf(video.id))
                                    }
                                )
                            }
                        }
                    } else {
                        StandardImageCardsRow(
                            title = item.title,
                            modelList = item.videos,
                            imageFn = { _, m -> m.image },
                            imageSize = if (item.horizontalVideoImage) HorizontalPosterSize else VerticalPosterSize,
                            contentFn = { _, m -> ContentModel(m.title, subtitle = m.author) },
                            onItemFocus = { _, video ->
                                title = video.title
                                subTitle = video.author
                                description = video.desc
                                backgroundState.url = video.image
                                backgroundState.type = ScreenBackgroundType.BLUR
                            },
                            onItemClick = { _, video ->
                                LogUtil.d("Click $video")
                                onNavigate(NavigationItems.Detail, listOf(video.id))
                            }
                        )
                    }

                }
            }


            LaunchedEffect(key1 = videosRowsLD) {
                if (videosRows[0].videos.isNotEmpty()) {
                    val video = videosRows[0].videos[0]
                    title = video.title
                    subTitle = video.author
                    description = video.desc
                    backgroundState.url = video.image
                    backgroundState.type = ScreenBackgroundType.SCRIM
                }
            }

        } else {
            EmptyDataScreen()
        }
    } else if (videosRowsLD.type == LazyType.FAILURE) {
        ErrorScreen {
            viewModel.refreshHomeVideosRows()
        }
    } else {
        LoadingScreen()
    }
}