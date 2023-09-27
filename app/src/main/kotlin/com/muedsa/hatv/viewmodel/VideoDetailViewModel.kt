package com.muedsa.hatv.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.muedsa.hatv.model.LazyData
import com.muedsa.hatv.model.LazyType
import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.repository.IHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: IHARepository
) : ViewModel() {

    val videoIdLD = savedStateHandle.getLiveData<String?>(VIDEO_ID_SAVED_STATE_KEY, null)
    val videoDetailDataState = mutableStateOf<LazyData<VideoDetailModel>>(LazyData.init())

    private fun fetchVideoDetail(videoId: String) {
        clearVideoDetail()
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                repo.fetchVideoDetail(videoId).let {
                    videoDetailDataState.value = LazyData.success(it)
                }
            } catch (t: Throwable) {
                Timber.d(t)
                withContext(Dispatchers.Main) {
                    videoDetailDataState.value = LazyData.fail(t)
                }
                FirebaseCrashlytics.getInstance().recordException(t)
            }
        }
    }

    private fun clearVideoDetail() {
        if (videoDetailDataState.value.type != LazyType.LOADING) {
            videoDetailDataState.value = LazyData.init()
        }
    }

    init {
        Timber.d("VideoDetailViewModel init")
        viewModelScope.launch {
            Timber.d("VideoDetailViewModel init viewModelScope launch")
            videoIdLD.observeForever {
                it?.let {
                    fetchVideoDetail(it)
                }
            }
        }
    }

    companion object {
        private const val VIDEO_ID_SAVED_STATE_KEY = "videoId"
    }
}