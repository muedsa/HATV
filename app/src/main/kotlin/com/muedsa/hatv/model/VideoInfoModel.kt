package com.muedsa.hatv.model

import com.muedsa.compose.tv.model.KeyModel

data class VideoInfoModel (
    val id: String,
    val image: String,
    val title: String,
    val author: String,
    val desc: String,
) : KeyModel {
    override val key = id
}