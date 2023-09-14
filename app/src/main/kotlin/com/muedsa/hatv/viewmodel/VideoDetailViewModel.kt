package com.muedsa.hatv.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.repository.IHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: IHARepository
) : ViewModel() {

    private val _disposable = CompositeDisposable()

    private val _savedVideoId = savedStateHandle.getStateFlow<String?>(VIDEO_ID_SAVED_STATE_KEY, null)
    val videoDetail = MutableLiveData<VideoDetailModel?>()

    fun fetchVideoDetail(videoId: String) {
        clearVideoDetail()
        repo.fetchVideoDetail(videoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                videoDetail.value = it
            }, {
                videoDetail.value = null
            }, _disposable)
    }

    private fun clearVideoDetail() {
        videoDetail.value = null
    }

    init {
        viewModelScope.launch {
            addCloseable {
                _disposable.clear()
            }

            _savedVideoId.collectLatest {
                if(it != null) {
                    fetchVideoDetail(it)
                }
            }
        }
    }

    companion object {
        private const val VIDEO_ID_SAVED_STATE_KEY = "videoId"
    }
}