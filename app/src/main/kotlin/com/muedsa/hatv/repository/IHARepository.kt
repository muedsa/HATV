package com.muedsa.hatv.repository

import com.muedsa.hatv.model.ha.PagedVideoInfoModel
import com.muedsa.hatv.model.ha.SearchOptionsModel
import com.muedsa.hatv.model.ha.VideoDetailModel
import com.muedsa.hatv.model.ha.VideosRowModel

interface IHARepository {

    fun fetchHomeVideosRows(): List<VideosRowModel>

    fun fetchVideoDetail(videoId: String): VideoDetailModel

    fun fetchSearchOptions(): SearchOptionsModel

    fun fetchSearchVideos(
        query: String,
        genre: String = "",
        tags: Set<String> = emptySet(),
        page: Int = 1
    ): PagedVideoInfoModel
}