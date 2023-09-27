package com.muedsa.hatv.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.muedsa.hatv.model.LazyData
import com.muedsa.hatv.model.SearchOptionsModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.repository.IHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: IHARepository
) : ViewModel() {

    val searchOptionsState = mutableStateOf<LazyData<SearchOptionsModel>>(LazyData.init())

    val searchTextState = mutableStateOf("")
    val searchGenreState = mutableStateOf("")
    val searchTagsState = mutableStateListOf<String>()

    val searchVideosState = mutableStateListOf<VideoInfoModel>()

    val pageState = mutableIntStateOf(1)
    val maxPageState = mutableIntStateOf(1)
    val searchLoadState = mutableStateOf(LazyData.success(Unit))
    val horizontalCardState = mutableStateOf(true)

    fun fetchSearchVideos() {
        searchLoadState.value = LazyData.init()
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                repo.fetchSearchVideos(
                    query = searchTextState.value,
                    genre = searchGenreState.value,
                    tags = searchTagsState,
                    page = 1
                ).let {
                    horizontalCardState.value = it.horizontalVideoImage
                    searchVideosState.clear()
                    searchVideosState.addAll(it.videos)
                    pageState.intValue = it.page
                    maxPageState.intValue = it.maxPage
                    searchLoadState.value = LazyData.success(Unit)
                }
            } catch (t: Throwable) {
                Timber.d(t)
                withContext(Dispatchers.Main) {
                    searchVideosState.clear()
                    searchLoadState.value = LazyData.fail(t)
                }
                FirebaseCrashlytics.getInstance().recordException(t)
            }
        }
    }

    fun fetchSearchVideosNextPage() {
        searchLoadState.value = LazyData.init()
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                repo.fetchSearchVideos(
                    query = searchTextState.value,
                    genre = searchGenreState.value,
                    tags = searchTagsState,
                    page = pageState.intValue + 1
                ).let {
                    searchVideosState.addAll(it.videos)
                    pageState.intValue = it.page
                    maxPageState.intValue = it.maxPage
                    searchLoadState.value = LazyData.success(Unit)
                }
            } catch (t: Throwable) {
                Timber.d(t)
                withContext(Dispatchers.Main) {
                    searchLoadState.value = LazyData.fail(t)
                }
                FirebaseCrashlytics.getInstance().recordException(t)
            }
        }
    }

    fun initSearchOptions() {
        Timber.d("initSearchOptions start")
        searchOptionsState.value = LazyData.init()
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                repo.fetchSearchOptions().let {
                    Timber.d("initSearchOptions success")
                    searchOptionsState.value = LazyData.success(it)
                }
            } catch (t: Throwable) {
                Timber.d(t)
                withContext(Dispatchers.Main) {
                    searchOptionsState.value = LazyData.fail(t)
                }
                FirebaseCrashlytics.getInstance().recordException(t)
            }
        }
    }

    fun addSearchTag(tagName: String) {
        if (!searchTagsState.contains(tagName)) {
            searchTagsState.add(tagName)
        }
    }

    fun removeSearchTag(tagName: String) {
        searchTagsState.remove(tagName)
    }

    fun resetSearch() {
        searchTextState.value = ""
        searchGenreState.value = ""
        searchTagsState.clear()
        searchVideosState.clear()
        pageState.intValue = 1
        maxPageState.intValue = 1
    }

    init {
        Timber.d("SearchViewModel init")
        viewModelScope.launch {
            Timber.d("SearchViewModel init viewModelScope launch")

            snapshotFlow { searchTextState }.collect {
                pageState.intValue = 1
                maxPageState.intValue = 1
                Timber.d("search params [searchText] change, next page disabled")
            }

            snapshotFlow {
                searchGenreState
            }.collect {
                pageState.intValue = 1
                maxPageState.intValue = 1
                Timber.d("search params [searchGenreState] change, next page disabled")
            }

            snapshotFlow {
                searchTagsState
            }.collect {
                pageState.intValue = 1
                maxPageState.intValue = 1
                Timber.d("search params [searchTagsState] change, next page disabled")
            }
        }
        initSearchOptions()
    }
}