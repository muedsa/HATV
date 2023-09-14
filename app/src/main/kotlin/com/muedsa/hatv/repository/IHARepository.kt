package com.muedsa.hatv.repository

import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.model.VideosRowModel
import io.reactivex.rxjava3.core.Single

interface IHARepository {

    fun fetchHomeVideosRows(): Single<List<VideosRowModel>>

    fun fetchVideoDetail(videoId: String): Single<VideoDetailModel>

    fun fetchSearchVideos(): Single<List<VideoInfoModel>>
}