package com.muedsa.hatv.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.muedsa.hatv.model.LazyData
import com.muedsa.hatv.model.SearchOptionsModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.repository.IHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: IHARepository
) : ViewModel() {

    private val _disposable = CompositeDisposable()

    val searchOptionsLD = MutableLiveData<LazyData<SearchOptionsModel>>(LazyData.init())

    val searchTextLD = MutableLiveData("")
    val searchGenreLD = MutableLiveData("")

    val searchVideosLD = MutableLiveData<MutableList<VideoInfoModel>>(mutableListOf())
    val pageLD = MutableLiveData(1)
    val maxPageLD = MutableLiveData(1)
    val searchLoadLD = MutableLiveData<LazyData<Unit>>()
    val horizontalCardLD = MutableLiveData(true)

    fun fetchSearchVideos() {
        searchLoadLD.value = LazyData.init()
        repo.fetchSearchVideos(
            query = searchTextLD.value ?: "",
            genre = searchGenreLD.value ?: "",
            tags = getSelectedTags(),
            page = 1
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                horizontalCardLD.value = it.horizontalVideoImage
                searchVideosLD.value = it.videos.toMutableList()
                pageLD.value = it.page
                maxPageLD.value = it.maxPage
                searchLoadLD.value = LazyData.success(Unit)
            }, {
                searchVideosLD.value = mutableListOf()
                FirebaseCrashlytics.getInstance().recordException(it)
            }, _disposable)
    }

    fun fetchSearchVideosNextPage() {
        searchLoadLD.value = LazyData.init()
        repo.fetchSearchVideos(
            query = searchTextLD.value ?: "",
            genre = searchGenreLD.value ?: "",
            tags = getSelectedTags(),
            page = pageLD.value!! + 1
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                searchVideosLD.value!!.addAll(it.videos)
                pageLD.value = it.page
                maxPageLD.value = it.maxPage
                searchLoadLD.value = LazyData.success(Unit)
            }, {
                searchLoadLD.value = LazyData.success(Unit)
                FirebaseCrashlytics.getInstance().recordException(it)
            }, _disposable)
    }

    fun initSearchOptions() {
        searchOptionsLD.value = LazyData.init()
        repo.fetchSearchOptions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                searchOptionsLD.value = LazyData.success(it)
            }, {
                searchOptionsLD.value = LazyData.fail(it)
                FirebaseCrashlytics.getInstance().recordException(it)
            }, _disposable)
    }

    fun resetSearch() {
        searchTextLD.value = ""
        searchGenreLD.value = ""
        searchOptionsLD.value?.data?.tagsRows?.forEach {
            it.tags.forEach { tag ->
                if (tag.selected.value) {
                    tag.selected.value = false
                }
            }
        }
        searchVideosLD.value?.clear()
    }

    private fun getSelectedTags(): List<String> {
        return searchOptionsLD.value?.data?.tagsRows?.flatMap {
            it.tags
        }?.filter {
            it.selected.value
        }?.map {
            it.tag
        } ?: emptyList()
    }

    init {
        addCloseable {
            _disposable.clear()
        }
        searchTextLD.observeForever {
            pageLD.value = 1
            maxPageLD.value = 1
            Timber.d("search text change, next page disabled")
        }
        searchGenreLD.observeForever {
            pageLD.value = 1
            maxPageLD.value = 1
            Timber.d("genre change, next page disabled")
        }
        searchOptionsLD.observeForever {
            pageLD.value = 1
            maxPageLD.value = 1
            Timber.d("tag change, next page disabled")
        }
        initSearchOptions()
    }
}