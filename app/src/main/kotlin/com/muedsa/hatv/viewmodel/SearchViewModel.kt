package com.muedsa.hatv.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.repository.IHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: IHARepository
) : ViewModel() {

    val searchVideos = mutableStateListOf<VideoInfoModel>()

}