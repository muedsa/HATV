package com.muedsa.hatv.ui.features.home.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.tv.material3.Card
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

    val searchOptionsData by viewModel.searchOptionsLD.observeAsState(initial = LazyData.init())

    val searchText by viewModel.searchTextLD.observeAsState(initial = "")
    val searchGenre by viewModel.searchGenreLD.observeAsState(initial = "")
    val selectedTags by viewModel.selectedTagsLD.observeAsState(initial = mutableSetOf())

    val searchLoad by viewModel.searchLoadLD.observeAsState(initial = LazyData.success(Unit))
    val searchVideos by viewModel.searchVideosLD.observeAsState(initial = listOf())
    val searchPage by viewModel.pageLD.observeAsState(initial = 1)
    val searchMaxPage by viewModel.maxPageLD.observeAsState(initial = 1)

    var searchOptionsExpand by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = searchOptionsData.type, key2 = searchOptionsData.error) {
        if (searchOptionsData.type == LazyType.FAILURE) {
            errorMsgBoxState.error(searchOptionsData.error)
        }
    }

    LaunchedEffect(key1 = searchLoad.type, key2 = searchLoad.error) {
        if (searchLoad.type == LazyType.FAILURE) {
            errorMsgBoxState.error(searchLoad.error)
        }
    }

    LaunchedEffect(key1 = searchText, key2 = searchGenre, key3 = selectedTags.size) {
        viewModel.pageLD.value = 1
        viewModel.maxPageLD.value = 1
    }

    when (searchOptionsData.type) {
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
                            viewModel.searchTextLD.value = it
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedIconButton(onClick = {
                        searchOptionsExpand = false
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
                        searchOptionsExpand = !searchOptionsExpand
                    }) {
                        Text(text = "搜索项")
                        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                        Icon(
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            imageVector = if (searchOptionsExpand) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.ArrowDropDown,
                            contentDescription = "展开搜索项"
                        )
                    }
                }

                if (searchOptionsExpand) {
                    if (searchOptionsData.data != null
                        && (searchOptionsData.data!!.genres.isNotEmpty() || searchOptionsData.data!!.tagsRows.isNotEmpty())
                    ) {
                        val searchOptions = searchOptionsData.data!!
                        TvLazyColumn(contentPadding = PaddingValues(top = ImageCardRowCardPadding)) {
                            item {
                                Text(
                                    text = "影片类型",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                FlowRow {
                                    for (genre in searchOptions.genres) {
                                        FilterChip(
                                            modifier = Modifier.padding(8.dp),
                                            selected = genre == searchGenre,
                                            leadingIcon = if (genre == searchGenre) {
                                                {
                                                    Icon(
                                                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                                                        imageVector = Icons.Outlined.Check,
                                                        contentDescription = "选择${genre}"
                                                    )
                                                }
                                            } else null,
                                            onClick = {
                                                Timber.d("click Genre: $genre")
                                                viewModel.searchGenreLD.value =
                                                    if (genre == searchGenre) "" else genre
                                            }
                                        ) {
                                            Text(text = genre)
                                        }
                                    }
                                }
                                Divider(modifier = Modifier.padding(bottom = 10.dp))
                            }
                            items(
                                items = searchOptions.tagsRows,
                                key = { it.title }
                            ) {
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
                } else {
                    if (searchVideos.isNotEmpty()) {
                        TvLazyVerticalGrid(
                            columns = TvGridCells.Adaptive(HorizontalPosterSize.width + ImageCardRowCardPadding),
                            contentPadding = PaddingValues(top = ImageCardRowCardPadding)
                        ) {
                            items(
                                items = searchVideos,
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

                            if (searchLoad.type != LazyType.LOADING && searchPage < searchMaxPage) {
                                item {
                                    Card(
                                        modifier = Modifier
                                            .size(HorizontalPosterSize)
                                            .padding(end = ImageCardRowCardPadding),
                                        onClick = {
                                            viewModel.fetchSearchVideosNextPage()
                                        }
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(text = "继续加载")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (searchLoad.type == LazyType.LOADING) {
                        LoadingScreen(model = true)
                    }
                }
            }
        }

        LazyType.FAILURE -> {
            ErrorScreen(onRefresh = {
                viewModel.initSearchOptions()
            })
        }
    }
}