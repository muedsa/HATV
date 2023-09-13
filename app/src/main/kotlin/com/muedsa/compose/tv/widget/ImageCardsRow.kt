package com.muedsa.compose.tv.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyListState
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.muedsa.compose.tv.model.ContentModel
import com.muedsa.compose.tv.theme.TvTheme

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun <T> ImageCardsRow(
    state: TvLazyListState = rememberTvLazyListState(),
    title: String,
    modelList: List<T> = listOf(),
    imageFn: (model: T) -> String,
    contentFn: (model: T) -> ContentModel? = { _ -> null },
    onItemFocus: (index: Int, item: T) -> Unit = { _, _ -> },
    onItemClick: (index: Int, item: T) -> Unit = { _, _ -> }
) {

    val focusRequester = remember { FocusRequester() }

    val firstItemFocusRequester = remember { FocusRequester() }

    Column(Modifier.height(150.dp)) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1
        )
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
            contentPadding = PaddingValues(end = 100.dp)
        ) {
            modelList.forEachIndexed { index, it ->
                var modifier = Modifier.padding(end = 12.dp)
                if (index == 0) {
                    modifier = modifier.focusRequester(firstItemFocusRequester)
                }
                item {
                    ImageContentCard(
                        modifier = modifier,
                        url = imageFn(it),
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
    state: TvLazyListState = rememberTvLazyListState(),
    title: String,
    modelList: List<T> = listOf(),
    imageFn: (model: T) -> String,
    contentFn: (model: T) -> ContentModel? = { _ -> null },
    onItemFocus: (index: Int, item: T) -> Unit = { _, _ -> },
    onItemClick: (index: Int, item: T) -> Unit = { _, _ -> }
) {
    val focusRequester = remember { FocusRequester() }

    Column {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1
        )
        TvLazyRow(
            modifier = Modifier
                .focusRequester(focusRequester)
                .focusProperties {
                    exit = { focusRequester.saveFocusedChild(); FocusRequester.Default }
                    enter =
                        { if (focusRequester.restoreFocusedChild()) FocusRequester.Cancel else FocusRequester.Default }
                },
            state = state,
            contentPadding = PaddingValues(end = 100.dp)
        ) {
            modelList.forEachIndexed { index, it ->
                item {
                    ImageContentCard(
                        modifier = Modifier
                            .width(215.dp)
                            .padding(end = 12.dp),
                        url = imageFn(it),
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
            title = "Row Title",
            modelList = modelList,
            imageFn = { _ -> "" },
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
            title = "Standard Row Title",
            modelList = modelList,
            imageFn = { _ -> "" },
            contentFn = { ContentModel(it) }
        )
    }
}