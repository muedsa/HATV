package com.muedsa.hatv.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.muedsa.hatv.model.LazyData
import com.muedsa.hatv.model.VideosRowModel
import com.muedsa.hatv.repository.IHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val repo: IHARepository
) : ViewModel() {

    val videosRowsDataState = mutableStateOf<LazyData<List<VideosRowModel>>>(LazyData.init())

    fun fetchHomeVideosRows() {
        videosRowsDataState.value = LazyData.init()
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                repo.fetchHomeVideosRows().let {
                    videosRowsDataState.value = LazyData.success(it)
                }
            } catch (t: Throwable) {
                Timber.d(t)
                withContext(Dispatchers.Main) {
                    videosRowsDataState.value = LazyData.fail(t)
                }
                FirebaseCrashlytics.getInstance().recordException(t)
            }
        }
    }

    init {
        Timber.d("HomePageViewModel init")
        viewModelScope.launch {
            Timber.d("HomePageViewModel init viewModelScope launch")
            fetchHomeVideosRows()
        }
    }
}