package com.muedsa.hatv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muedsa.hatv.model.LazyData
import com.muedsa.hatv.model.LazyPagedList
import com.muedsa.hatv.model.SelectedSearchOptionsModel
import com.muedsa.hatv.model.ha.SearchOptionsModel
import com.muedsa.hatv.model.ha.VideoInfoModel
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
class SearchViewModel @Inject constructor(
    private val repo: IHARepository
) : ViewModel() {
    private val _searchOptionsLDSF = MutableStateFlow<LazyData<SearchOptionsModel>>(LazyData.init())
    val searchOptionsLDSF: StateFlow<LazyData<SearchOptionsModel>> = _searchOptionsLDSF

    val selectedSearchOptionsSF = MutableStateFlow(SelectedSearchOptionsModel())

    private val _searchVideosLPSF =
        MutableStateFlow<LazyPagedList<SelectedSearchOptionsModel, VideoInfoModel>>(
            LazyPagedList.new(
                selectedSearchOptionsSF.value
            )
        )
    val searchVideosLPSF: StateFlow<LazyPagedList<SelectedSearchOptionsModel, VideoInfoModel>> =
        _searchVideosLPSF

    private val _searchVideosIsHorizontalSF = MutableStateFlow(false)
    val searchVideosIsHorizontalSF: StateFlow<Boolean> = _searchVideosIsHorizontalSF

    fun searchVideos(lp: LazyPagedList<SelectedSearchOptionsModel, VideoInfoModel>) {
        viewModelScope.launch {
            val loadingLP = lp.loadingNext()
            _searchVideosLPSF.value = loadingLP
            _searchVideosLPSF.value = withContext(Dispatchers.IO) {
                fetchSearchVideos(loadingLP)
            }
        }
    }


    private fun fetchSearchVideos(lp: LazyPagedList<SelectedSearchOptionsModel, VideoInfoModel>): LazyPagedList<SelectedSearchOptionsModel, VideoInfoModel> {
        return try {
            repo.fetchSearchVideos(
                query = lp.query.query,
                genre = lp.query.genre,
                tags = lp.query.tags,
                page = lp.nextPage
            ).let {
                _searchVideosIsHorizontalSF.value = it.horizontalVideoImage
                lp.successNext(it.videos, it.maxPage)
            }
        } catch (t: Throwable) {
            LogUtil.fd(t)
            lp.failNext(t)
        }
    }

    fun initSearchOptions() {
        viewModelScope.launch {
            _searchOptionsLDSF.value = LazyData.init()
            _searchOptionsLDSF.value = withContext(Dispatchers.IO) {
                fetchSearchOptions()
            }
        }
    }

    private fun fetchSearchOptions(): LazyData<SearchOptionsModel> {
        return try {
            repo.fetchSearchOptions().let {
                LazyData.success(it)
            }
        } catch (t: Throwable) {
            LogUtil.fd(t)
            LazyData.fail(t)
        }
    }

    fun resetSearch() {
        selectedSearchOptionsSF.value = SelectedSearchOptionsModel()
    }

    init {
        initSearchOptions()
    }
}