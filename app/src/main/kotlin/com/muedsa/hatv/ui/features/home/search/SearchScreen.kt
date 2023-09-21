package com.muedsa.hatv.ui.features.home.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.FilterChip
import androidx.tv.material3.FilterChipDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.OutlinedIconButton
import androidx.tv.material3.Text
import com.muedsa.compose.tv.model.ContentModel
import com.muedsa.compose.tv.theme.CustomerColor
import com.muedsa.compose.tv.theme.HorizontalPosterSize
import com.muedsa.compose.tv.theme.ImageCardRowCardPadding
import com.muedsa.compose.tv.theme.ScreenPaddingLeft
import com.muedsa.compose.tv.widget.CardType
import com.muedsa.compose.tv.widget.ErrorMessageBoxState
import com.muedsa.compose.tv.widget.ImageContentCard
import com.muedsa.compose.tv.widget.ScreenBackgroundState
import com.muedsa.compose.tv.widget.ScreenBackgroundType
import com.muedsa.hatv.model.LazyData
import com.muedsa.hatv.model.LazyType
import com.muedsa.hatv.ui.features.others.ErrorScreen
import com.muedsa.hatv.ui.features.others.LoadingScreen
import com.muedsa.hatv.ui.navigation.NavigationItems
import com.muedsa.hatv.viewmodel.SearchViewModel
import timber.log.Timber

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    backgroundState: ScreenBackgroundState,
    errorMsgBoxState: ErrorMessageBoxState,
    onNavigate: (NavigationItems, List<String>?) -> Unit = { _, _ -> }
) {

    val tagsRowsData by viewModel.tagsRowsData.observeAsState(initial = LazyData.init())

    var searchText by viewModel.searchText
    val selectedTags = viewModel.selectedTags

    val searchVideosData by viewModel.searchVideosData.observeAsState(initial = LazyData.init())

    var tagsExpand by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = tagsRowsData.type, key2 = tagsRowsData.error) {
        if (tagsRowsData.type == LazyType.FAILURE) {
            errorMsgBoxState.error(tagsRowsData.error)
        }
    }

    LaunchedEffect(key1 = searchVideosData.type, key2 = searchVideosData.error) {
        if (searchVideosData.type == LazyType.FAILURE) {
            errorMsgBoxState.error(searchVideosData.error)
        }
    }

    when (tagsRowsData.type) {
        LazyType.LOADING -> {
            LoadingScreen()
        }

        LazyType.SUCCESS -> {

            Column(modifier = Modifier.padding(start = ScreenPaddingLeft)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(x = -ScreenPaddingLeft)
                        .padding(vertical = 30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.55f)
                            .background(
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                shape = OutlinedTextFieldDefaults.shape
                            ),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CustomerColor.outline,
                            cursorColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        value = searchText,
                        onValueChange = {
                            searchText = it
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedIconButton(onClick = {
                        tagsExpand = false
                        viewModel.fetchSearchVideos()
                    }) {
                        Icon(
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "搜索"
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedButton(onClick = {
                        tagsExpand = !tagsExpand
                    }) {
                        Text(text = "标签")
                        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                        Icon(
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            imageVector = if (tagsExpand) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.ArrowDropDown,
                            contentDescription = "展开标签"
                        )
                    }
                }

                if (tagsExpand && !tagsRowsData.data.isNullOrEmpty()) {
                    TvLazyColumn(contentPadding = PaddingValues(top = ImageCardRowCardPadding)) {
                        items(tagsRowsData.data!!) {
                            Text(
                                text = it.title,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow {
                                for (tag in it.tags) {
                                    FilterChip(
                                        modifier = Modifier.padding(8.dp),
                                        selected = tag.selected.value,
                                        leadingIcon = if (tag.selected.value) {
                                            {
                                                Icon(
                                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                                    imageVector = Icons.Outlined.Check,
                                                    contentDescription = "选择${tag.tag}"
                                                )
                                            }
                                        } else null,
                                        onClick = {
                                            Timber.d("click $tag")
                                            if (tag.selected.value) {
                                                selectedTags.remove(tag.tag)
                                                tag.selected.value = false
                                            } else {
                                                selectedTags.add(tag.tag)
                                                tag.selected.value = true
                                            }
                                        }
                                    ) {
                                        Text(text = tag.tag)
                                    }
                                }
                            }
                            Divider(modifier = Modifier.padding(bottom = 10.dp))
                        }
                    }
                }

                if (!tagsExpand && searchVideosData.type == LazyType.SUCCESS && !searchVideosData.data.isNullOrEmpty()) {
                    TvLazyVerticalGrid(
                        columns = TvGridCells.Adaptive(HorizontalPosterSize.width + ImageCardRowCardPadding),
                        contentPadding = PaddingValues(top = ImageCardRowCardPadding)
                    ) {
                        items(
                            items = searchVideosData.data!!,
                            key = { it.id }
                        ) {
                            ImageContentCard(
                                modifier = Modifier.padding(end = ImageCardRowCardPadding),
                                url = it.image,
                                imageSize = HorizontalPosterSize,
                                type = CardType.STANDARD,
                                model = ContentModel(it.title, subtitle = it.author),
                                onItemFocus = {
                                    backgroundState.url = it.image
                                    backgroundState.type = ScreenBackgroundType.BLUR
                                },
                                onItemClick = {
                                    Timber.d("Click $it")
                                    onNavigate(NavigationItems.Detail, listOf(it.id))
                                }
                            )
                        }
                    }
                }
            }
        }

        LazyType.FAILURE -> {
            ErrorScreen(onRefresh = {
                viewModel.initTags()
            })
        }
    }
}