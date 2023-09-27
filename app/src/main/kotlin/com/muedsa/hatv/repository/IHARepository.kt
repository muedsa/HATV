package com.muedsa.hatv.repository

import com.muedsa.hatv.model.PagedVideoInfoModel
import com.muedsa.hatv.model.SearchOptionsModel
import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.model.VideosRowModel

interface IHARepository {

    fun fetchHomeVideosRows(): List<VideosRowModel>

    fun fetchVideoDetail(videoId: String): VideoDetailModel

    fun fetchSearchOptions(): SearchOptionsModel

    fun fetchSearchVideos(
        query: String,
        genre: String = "",
        tags: List<String> = emptyList(),
        page: Int = 1
    ): PagedVideoInfoModel
}