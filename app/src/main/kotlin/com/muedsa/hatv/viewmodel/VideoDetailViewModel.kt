package com.muedsa.hatv.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muedsa.hatv.model.LazyData
import com.muedsa.hatv.repository.IHARepository
import com.muedsa.uitl.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: IHARepository
) : ViewModel() {

    private val _navVideoIdFlow = savedStateHandle.getStateFlow(VIDEO_ID_SAVED_STATE_KEY, "0")
    val videoIdSF = MutableStateFlow(_navVideoIdFlow.value)

    val videoDetailLDSF = videoIdSF.map {
        LazyData.success(withContext(Dispatchers.IO) {
            repo.fetchVideoDetail(it)
        })
    }.catch {
        LogUtil.d(it)
        emit(LazyData.fail(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LazyData.init()
    )

    init {
        viewModelScope.launch {
            _navVideoIdFlow.collectLatest {
                videoIdSF.value = it
            }
        }

    }

    companion object {
        private const val VIDEO_ID_SAVED_STATE_KEY = "videoId"
    }
}