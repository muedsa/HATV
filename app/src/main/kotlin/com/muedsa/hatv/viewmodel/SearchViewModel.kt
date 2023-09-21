package com.muedsa.hatv.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.muedsa.hatv.model.LazyData
import com.muedsa.hatv.model.TagsRowModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.repository.IHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: IHARepository
) : ViewModel() {

    private val _disposable = CompositeDisposable()

    val tagsRowsData = MutableLiveData<LazyData<List<TagsRowModel>>>(LazyData.init())

    val searchText = mutableStateOf("")
    val selectedTags = mutableSetOf<String>()

    val searchVideosData = MutableLiveData<LazyData<List<VideoInfoModel>>>(LazyData.init())

    fun fetchSearchVideos() {
        repo.fetchSearchVideos(query = searchText.value, tags = selectedTags.toList())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                searchVideosData.value = LazyData.success(it)
            }, {
                searchVideosData.value = LazyData.fail(it)
                FirebaseCrashlytics.getInstance().recordException(it)
            }, _disposable)
    }

    fun initTags() {
        tagsRowsData.value = LazyData.init()
        repo.fetchSearchTags()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tagsRowsData.value = LazyData.success(it)
            }, {
                tagsRowsData.value = LazyData.fail(it)
                FirebaseCrashlytics.getInstance().recordException(it)
            }, _disposable)
    }

    init {
        addCloseable {
            _disposable.clear()
        }
        initTags()
    }
}