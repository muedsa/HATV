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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.itemsIndexed
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
import com.muedsa.hatv.model.LazyPagedList
import com.muedsa.hatv.model.LazyType
import com.muedsa.hatv.ui.navigation.NavigationItems
import com.muedsa.hatv.viewmodel.SearchViewModel
import com.muedsa.uitl.LogUtil
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    backgroundState: ScreenBackgroundState,
    errorMsgBoxState: ErrorMessageBoxState,
    onNavigate: (NavigationItems, List<String>?) -> Unit = { _, _ -> }
) {
    val searchOptionsLD by viewModel.searchOptionsLDSF.collectAsState()

    val selectedSearchOptions by viewModel.selectedSearchOptionsSF.collectAsState()

    val searchVideosLP by viewModel.searchVideosLPSF.collectAsState()
    val searchVideosIsHorizontal by viewModel.searchVideosIsHorizontalSF.collectAsState()

    var searchOptionsExpand by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = searchOptionsLD.type, key2 = searchOptionsLD.error) {
        if (searchOptionsLD.type == LazyType.FAILURE) {
            errorMsgBoxState.error(searchOptionsLD.error)
        }
    }

    LaunchedEffect(key1 = searchVideosLP.type, key2 = searchVideosLP.error) {
        if (searchVideosLP.type == LazyType.FAILURE) {
            errorMsgBoxState.error(searchVideosLP.error)
        }
    }

    BackHandler(enabled = searchOptionsExpand) {
        searchOptionsExpand = false
    }

    when (searchOptionsLD.type) {
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
                        value = selectedSearchOptions.query,
                        onValueChange = {
                            viewModel.selectedSearchOptionsSF.value =
                                selectedSearchOptions.copy(query = it)
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedIconButton(onClick = {
                        searchOptionsExpand = false
                        viewModel.searchVideos(LazyPagedList.new(selectedSearchOptions))
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
                    if (searchOptionsLD.data != null
                        && (searchOptionsLD.data!!.genres.isNotEmpty() || searchOptionsLD.data!!.tagsRows.isNotEmpty())
                    ) {
                        val searchOptions = searchOptionsLD.data!!
                        TvLazyColumn(contentPadding = PaddingValues(top = ImageCardRowCardPadding)) {
                            if (selectedSearchOptions.tags.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "选择的标签",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    FlowRow {
                                        for (tag in selectedSearchOptions.tags) {
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
                                                    LogUtil.d("click selected tag: $tag")
                                                    viewModel.selectedSearchOptionsSF.update {
                                                        it.copy(
                                                            tags = buildSet {
                                                                it.tags.forEach { oldTag ->
                                                                    if (oldTag != tag) {
                                                                        add(oldTag)
                                                                    }
                                                                }
                                                            }
                                                        )
                                                    }
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
                                            selected = genre == selectedSearchOptions.genre,
                                            leadingIcon = if (genre == selectedSearchOptions.genre) {
                                                {
                                                    Icon(
                                                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                                                        imageVector = Icons.Outlined.Check,
                                                        contentDescription = "选择${genre}"
                                                    )
                                                }
                                            } else null,
                                            onClick = {
                                                LogUtil.d("click Genre: $genre")
                                                viewModel.selectedSearchOptionsSF.update {
                                                    it.copy(genre = if (genre == it.genre) "" else genre)
                                                }
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
                                        val selected = selectedSearchOptions.tags.contains(tag)
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
                                                LogUtil.d("click $tag")
                                                viewModel.selectedSearchOptionsSF.update { old ->
                                                    old.copy(tags = buildSet {
                                                        if (selected) {
                                                            old.tags.forEach { oldTag ->
                                                                if (oldTag != tag) {
                                                                    add(oldTag)
                                                                }
                                                            }
                                                        } else {
                                                            addAll(old.tags)
                                                            add(tag)
                                                        }
                                                    })
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
                    if (searchVideosLP.list.isNotEmpty()) {
                        TvLazyVerticalGrid(
                            columns = TvGridCells.Adaptive(if (searchVideosIsHorizontal) HorizontalPosterSize.width else VerticalPosterSize.width + ImageCardRowCardPadding),
                            contentPadding = PaddingValues(top = ImageCardRowCardPadding)
                        ) {
                            itemsIndexed(
                                items = searchVideosLP.list,
                                key = { _, item -> item.id }
                            ) { index, item ->
                                val itemFocusRequester = remember { FocusRequester() }
                                ImageContentCard(
                                    modifier = Modifier
                                        .focusRequester(itemFocusRequester)
                                        .padding(end = ImageCardRowCardPadding),
                                    url = item.image,
                                    imageSize = if (searchVideosIsHorizontal) HorizontalPosterSize else VerticalPosterSize,
                                    type = CardType.STANDARD,
                                    model = ContentModel(item.title, subtitle = item.author),
                                    onItemFocus = {
                                        backgroundState.url = item.image
                                        backgroundState.type = ScreenBackgroundType.BLUR
                                    },
                                    onItemClick = {
                                        LogUtil.d("Click $item")
                                        onNavigate(NavigationItems.Detail, listOf(item.id))
                                    }
                                )

                                LaunchedEffect(key1 = Unit) {
                                    if (searchVideosLP.offset == index) {
                                        itemFocusRequester.requestFocus()
                                    }
                                }
                            }

                            if (searchVideosLP.type != LazyType.LOADING && searchVideosLP.hasNext) {
                                item {
                                    Card(
                                        modifier = Modifier
                                            .size(if (searchVideosIsHorizontal) HorizontalPosterSize else VerticalPosterSize)
                                            .padding(end = ImageCardRowCardPadding),
                                        onClick = {
                                            viewModel.searchVideos(searchVideosLP)
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

                            item {
                                // 最后一行占位高度
                                Spacer(modifier = Modifier.height(200.dp))
                            }
                        }
                    }
                    if (searchVideosLP.type == LazyType.LOADING) {
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