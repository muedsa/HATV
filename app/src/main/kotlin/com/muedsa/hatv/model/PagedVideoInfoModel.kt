package com.muedsa.hatv.model

data class PagedVideoInfoModel(
    val page: Int,
    val maxPage: Int,
    val videos: List<VideoInfoModel>,
    val horizontalVideoImage: Boolean
)
