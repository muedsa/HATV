package com.muedsa.hatv.model

data class SelectedSearchOptionsModel(
    val query: String = "",
    val genre: String = "",
    val tags: Set<String> = emptySet()
)