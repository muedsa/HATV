package com.muedsa.hatv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muedsa.hatv.model.LazyData
import com.muedsa.hatv.model.ha.VideosRowModel
import com.muedsa.hatv.repository.IHARepository
import com.muedsa.uitl.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val repo: IHARepository
) : ViewModel() {

    private val _videosRowsLDSF = MutableStateFlow<LazyData<List<VideosRowModel>>>(LazyData.init())
    val videosRowsLDSF: StateFlow<LazyData<List<VideosRowModel>>> = _videosRowsLDSF

    fun refreshHomeVideosRows() {
        viewModelScope.launch {
            _videosRowsLDSF.value = LazyData.init()
            _videosRowsLDSF.value = withContext(Dispatchers.IO) {
                fetchHomeVideosRows()
            }
        }
    }

    private fun fetchHomeVideosRows(): LazyData<List<VideosRowModel>> {
        return try {
            repo.fetchHomeVideosRows().let {
                LazyData.success(it)
            }
        } catch (t: Throwable) {
            LogUtil.fb(t)
            LazyData.fail(t)
        }
    }

    init {
        refreshHomeVideosRows()
    }
}