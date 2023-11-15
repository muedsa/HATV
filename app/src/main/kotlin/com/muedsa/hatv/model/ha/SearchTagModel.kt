package com.muedsa.hatv.model.ha

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class SearchTagModel(
    val tag: String,
    var selected: MutableState<Boolean> = mutableStateOf(false)
)
