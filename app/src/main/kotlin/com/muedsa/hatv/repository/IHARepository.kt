package com.muedsa.hatv.repository

import com.muedsa.hatv.model.PagedVideoInfoModel
import com.muedsa.hatv.model.SearchOptionsModel
import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.model.VideosRowModel
import io.reactivex.rxjava3.core.Single

interface IHARepository {

    fun fetchHomeVideosRows(): Single<List<VideosRowModel>>

    fun fetchVideoDetail(videoId: String): Single<VideoDetailModel>

    fun fetchSearchOptions(): Single<SearchOptionsModel>

    fun fetchSearchVideos(
        query: String,
        genre: String = "",
        tags: List<String> = emptyList(),
        page: Int = 1
    ): Single<PagedVideoInfoModel>
}