package com.muedsa.hatv.model

data class VideoDetailModel (
    val id: String,
    val image: String,
    val title: String,
    val author: String?,
    val desc: String?,
    val tags: List<String>,
    val playUrl: String,
    val videoList: List<VideoInfoModel>
)
