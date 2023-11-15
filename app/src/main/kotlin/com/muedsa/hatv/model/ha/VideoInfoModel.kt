package com.muedsa.hatv.model.ha

import com.muedsa.compose.tv.model.KeyModel

data class VideoInfoModel (
    val id: String,
    val image: String,
    val title: String,
    val author: String? = null,
    val desc: String? = null,
) : KeyModel {
    override val key = id
}