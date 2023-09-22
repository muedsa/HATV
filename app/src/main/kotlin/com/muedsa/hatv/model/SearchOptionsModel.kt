package com.muedsa.hatv.model

data class SearchOptionsModel(
    val genres: List<String>,
    val tagsRows: List<SearchTagsRowModel>
)
