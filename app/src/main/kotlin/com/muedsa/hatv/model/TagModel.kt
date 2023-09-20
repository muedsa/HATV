package com.muedsa.hatv.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class TagModel(val tag: String, var selected: MutableState<Boolean> = mutableStateOf(false))
