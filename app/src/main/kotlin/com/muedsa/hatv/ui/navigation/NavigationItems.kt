package com.muedsa.hatv.ui.navigation

sealed class NavigationItems(
    val path: String,
    val pathParams: List<String>? = null,
) {
    data object Home : NavigationItems("home")

    data object Detail : NavigationItems("detail/{videoId}", listOf("{videoId}"))

    data object NotFound : NavigationItems("not_found")
}
