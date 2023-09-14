package com.muedsa.hatv.repository

import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.model.VideosRowModel
import io.reactivex.rxjava3.core.Single

class HARepositoryImpl : IHARepository {
    override fun fetchHomeVideosRows(): Single<List<VideosRowModel>> {
        TODO("Not yet implemented")
    }

    override fun fetchVideoDetail(id: String): Single<VideoDetailModel> {
        TODO("Not yet implemented")
    }

    override fun fetchSearchVideos(): Single<List<VideoInfoModel>> {
        TODO("Not yet implemented")
    }
}