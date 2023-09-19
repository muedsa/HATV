package com.muedsa.hatv.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.muedsa.hatv.model.VideosRowModel
import com.muedsa.hatv.repository.IHARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject internal constructor(
    private val repo: IHARepository
) : ViewModel() {

    private val _disposable = CompositeDisposable()
    val videosRows = MutableLiveData<List<VideosRowModel>>()

    private fun fetchHomeVideosRows() {
        repo.fetchHomeVideosRows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                videosRows.value = it
            }, {
                videosRows.value = emptyList()
                FirebaseCrashlytics.getInstance().recordException(it)
            }, _disposable)
    }

    init {
        viewModelScope.launch {
            addCloseable {
                _disposable.clear()
            }
            fetchHomeVideosRows()
        }
    }
}