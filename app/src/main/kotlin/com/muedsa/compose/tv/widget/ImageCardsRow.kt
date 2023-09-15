package com.muedsa.compose.tv.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyListState
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.muedsa.compose.tv.model.ContentModel
import com.muedsa.compose.tv.model.KeyModel
import com.muedsa.compose.tv.theme.CardContentPadding
import com.muedsa.compose.tv.theme.HorizontalPosterSize
import com.muedsa.compose.tv.theme.ImageCardRowCardPadding
import com.muedsa.compose.tv.theme.TvTheme
import com.muedsa.compose.tv.theme.VerticalPosterSize


@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun <T> ImageCardsRow(
    modifier: Modifier = Modifier,
    state: TvLazyListState = rememberTvLazyListState(),
    title: String,
    modelList: List<T> = listOf(),
    imageFn: (model: T) -> String,
    imageSize: DpSize = HorizontalPosterSize,
    contentFn: (model: T) -> ContentModel? = { _ -> null },
    onItemFocus: (index: Int, item: T) -> Unit = { _, _ -> },
    onItemClick: (index: Int, item: T) -> Unit = { _, _ -> }
) {

    val focusRequester = remember { FocusRequester() }

    val firstItemFocusRequester = remember { FocusRequester() }

    Column(modifier) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(ImageCardRowCardPadding))
        TvLazyRow(
            modifier = Modifier
                .focusRequester(focusRequester)
                .focusProperties {
                    exit = { focusRequester.saveFocusedChild(); FocusRequester.Default }
                    enter = {
                        if (focusRequester.restoreFocusedChild()) {
                            FocusRequester.Cancel
                        } else {
                            firstItemFocusRequester
                        }
                    }
                },
            state = state,
            contentPadding = PaddingValues(
                start = 4.dp,
                bottom = ImageCardRowCardPadding,
                end = 100.dp
            )
        ) {
            modelList.forEachIndexed { index, it ->
                var itemModifier = Modifier.padding(end = ImageCardRowCardPadding)
                if (index == 0) {
                    itemModifier = itemModifier.focusRequester(firstItemFocusRequester)
                }
                item(key = if (it is KeyModel ) it.key else null) {
                    ImageContentCard(
                        modifier = itemModifier,
                        url = imageFn(it),
                        imageSize = imageSize,
                        type = CardType.COMPACT,
                        model = contentFn(it),
                        onItemFocus = { onItemFocus(index, it) },
                        onItemClick = { onItemClick(index, it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun <T> StandardImageCardsRow(
    modifier: Modifier = Modifier,
    state: TvLazyListState = rememberTvLazyListState(),
    title: String,
    modelList: List<T> = listOf(),
    imageFn: (model: T) -> String,
    imageSize: DpSize = HorizontalPosterSize,
    contentFn: (model: T) -> ContentModel? = { _ -> null },
    onItemFocus: (index: Int, item: T) -> Unit = { _, _ -> },
    onItemClick: (index: Int, item: T) -> Unit = { _, _ -> }
) {
    val focusRequester = remember { FocusRequester() }

    val rowBottomPadding =
        if (modelList.isNotEmpty() && modelList.stream().anyMatch {
                contentFn(it) != null
            }) ImageCardRowCardPadding - CardContentPadding
        else ImageCardRowCardPadding

    Column(modifier) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(10.dp))
        TvLazyRow(
            modifier = Modifier
                .focusRequester(focusRequester)
                .focusProperties {
                    exit = { focusRequester.saveFocusedChild(); FocusRequester.Default }
                    enter =
                        { if (focusRequester.restoreFocusedChild()) FocusRequester.Cancel else FocusRequester.Default }
                },
            state = state,
            contentPadding = PaddingValues(
                start = 4.dp,
                bottom = rowBottomPadding,
                end = 100.dp
            )
        ) {
            modelList.forEachIndexed { index, it ->
                item(key = if (it is KeyModel ) it.key else null) {
                    ImageContentCard(
                        modifier = Modifier.padding(end = 12.dp),
                        url = imageFn(it),
                        imageSize = imageSize,
                        type = CardType.STANDARD,
                        model = contentFn(it),
                        onItemFocus = { onItemFocus(index, it) },
                        onItemClick = { onItemClick(index, it) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ImageCardsRowPreview() {
    val modelList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 5")
    TvTheme {
        ImageCardsRow(
            modifier = Modifier.fillMaxWidth(),
            title = "Row Title",
            modelList = modelList,
            imageFn = { _ -> "" },
            contentFn = { ContentModel(it) }
        )
    }
}

@Preview
@Composable
fun VerticalImageCardsRowPreview() {
    val modelList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 5")
    TvTheme {
        ImageCardsRow(
            modifier = Modifier.fillMaxWidth(),
            title = "Row Title",
            modelList = modelList,
            imageFn = { _ -> "" },
            imageSize = VerticalPosterSize,
            contentFn = { ContentModel(it) }
        )
    }
}

@Preview
@Composable
fun StandardImageCardsRowPreview() {
    val modelList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 5")
    TvTheme {
        StandardImageCardsRow(
            modifier = Modifier.fillMaxWidth(),
            title = "Standard Row Title",
            modelList = modelList,
            imageFn = { _ -> "" },
            contentFn = { ContentModel(it) }
        )
    }
}

@Preview
@Composable
fun StandardVerticalImageCardsRowPreview() {
    val modelList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 5")
    TvTheme {
        StandardImageCardsRow(
            modifier = Modifier.fillMaxWidth(),
            title = "Standard Row Title",
            modelList = modelList,
            imageFn = { _ -> "" },
            imageSize = VerticalPosterSize,
            contentFn = { ContentModel(it) }
        )
    }
}