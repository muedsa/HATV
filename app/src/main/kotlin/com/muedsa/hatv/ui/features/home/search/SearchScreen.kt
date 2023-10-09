package com.muedsa.hatv.ui.features.home.search

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.muedsa.compose.tv.theme.VerticalPosterSize
import com.muedsa.compose.tv.widget.CardType
import com.muedsa.compose.tv.widget.ErrorMessageBoxState
import com.muedsa.compose.tv.widget.ErrorScreen
import com.muedsa.compose.tv.widget.ImageContentCard
import com.muedsa.compose.tv.widget.LoadingScreen
import com.muedsa.compose.tv.widget.ScreenBackgroundState
import com.muedsa.compose.tv.widget.ScreenBackgroundType
import com.muedsa.hatv.model.LazyType
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

    val searchOptionsData by remember { viewModel.searchOptionsState }

    var searchText by remember { viewModel.searchTextState }
    var searchGenre by remember { viewModel.searchGenreState }
    val searchTags = remember { viewModel.searchTagsState }

    val searchLoad by remember { viewModel.searchLoadState }

    val isHorizontal by remember { viewModel.horizontalCardState }
    val searchVideos = remember { viewModel.searchVideosState }
    val searchPage by remember { viewModel.pageState }
    val searchMaxPage by remember { viewModel.maxPageState }

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

    BackHandler(enabled = searchOptionsExpand) {
        searchOptionsExpand = false
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
                            searchText = it
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
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedIconButton(onClick = {
                        viewModel.resetSearch()
                    }) {
                        Icon(
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "重置搜索项"
                        )
                    }
                }

                if (searchOptionsExpand) {
                    if (searchOptionsData.data != null
                        && (searchOptionsData.data!!.genres.isNotEmpty() || searchOptionsData.data!!.tagsRows.isNotEmpty())
                    ) {
                        val searchOptions = searchOptionsData.data!!
                        TvLazyColumn(contentPadding = PaddingValues(top = ImageCardRowCardPadding)) {
                            if (searchTags.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "选择的标签",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    FlowRow {
                                        for (tag in searchTags) {
                                            FilterChip(
                                                modifier = Modifier.padding(8.dp),
                                                selected = true,
                                                trailingIcon = {
                                                    Icon(
                                                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                                                        imageVector = Icons.Outlined.Clear,
                                                        contentDescription = "选择${tag}"
                                                    )
                                                },
                                                onClick = {
                                                    Timber.d("click selected tag: $tag")
                                                    viewModel.removeSearchTag(tag)
                                                }
                                            ) {
                                                Text(text = tag)
                                            }

                                        }
                                    }
                                }
                            }

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
                                                searchGenre =
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
                                        val selected = searchTags.contains(tag)
                                        FilterChip(
                                            modifier = Modifier.padding(8.dp),
                                            selected = selected,
                                            leadingIcon = if (selected) {
                                                {
                                                    Icon(
                                                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                                                        imageVector = Icons.Outlined.Check,
                                                        contentDescription = "选择${tag}"
                                                    )
                                                }
                                            } else null,
                                            onClick = {
                                                Timber.d("click $tag")
                                                if (selected) {
                                                    viewModel.removeSearchTag(tag)
                                                } else {
                                                    viewModel.addSearchTag(tag)
                                                }
                                            }
                                        ) {
                                            Text(text = tag)
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
                            columns = TvGridCells.Adaptive(if (isHorizontal) HorizontalPosterSize.width else VerticalPosterSize.width + ImageCardRowCardPadding),
                            contentPadding = PaddingValues(top = ImageCardRowCardPadding)
                        ) {
                            items(
                                items = searchVideos,
                                key = { it.id }
                            ) {
                                ImageContentCard(
                                    modifier = Modifier.padding(end = ImageCardRowCardPadding),
                                    url = it.image,
                                    imageSize = if (isHorizontal) HorizontalPosterSize else VerticalPosterSize,
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
                                            .size(if (isHorizontal) HorizontalPosterSize else VerticalPosterSize)
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