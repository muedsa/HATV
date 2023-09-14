package com.muedsa.hatv.ui.features.home

sealed class HomeNavTabs (val title: String) {
    data object Browser : HomeNavTabs("首页")
    data object Search : HomeNavTabs("搜索")
}