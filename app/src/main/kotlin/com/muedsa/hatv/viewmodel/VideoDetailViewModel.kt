package com.muedsa.hatv.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.muedsa.hatv.model.LazyData
import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.repository.IHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: IHARepository
) : ViewModel() {

    private val _disposable = CompositeDisposable()

    private val _savedVideoId = savedStateHandle.getStateFlow<String?>(VIDEO_ID_SAVED_STATE_KEY, null)
    val videoDetailData = MutableLiveData<LazyData<VideoDetailModel>>(LazyData.init())

    fun fetchVideoDetail(videoId: String? = null) {
        clearVideoDetail()
        (videoId ?: _savedVideoId.value)?.let {
            repo.fetchVideoDetail(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    videoDetailData.value = LazyData.success(it)
                }, {
                    videoDetailData.value = LazyData.fail(it)
                    FirebaseCrashlytics.getInstance().recordException(it)
                }, _disposable)
        }
    }

    private fun clearVideoDetail() {
        videoDetailData.value = LazyData.init()
    }

    init {
        viewModelScope.launch {
            addCloseable {
                _disposable.clear()
            }

            _savedVideoId.collect {
                fetchVideoDetail(it)
            }
        }
    }

    companion object {
        private const val VIDEO_ID_SAVED_STATE_KEY = "videoId"
    }
}