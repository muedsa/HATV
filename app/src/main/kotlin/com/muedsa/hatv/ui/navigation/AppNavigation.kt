package com.muedsa.hatv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.muedsa.hatv.ui.features.others.NotFoundScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationItems.Home.path) {

        composable(NavigationItems.Home.path) {
            NotFoundScreen()
        }

        composable(NavigationItems.Detail.path) {
            NotFoundScreen()
        }

        composable(NavigationItems.NotFound.path) {
            NotFoundScreen()
        }
    }
}

fun onNavigate(
    navController: NavHostController,
    navItem: NavigationItems,
    pathParams: List<String>?
) {
    var route = navItem.path
    if (!navItem.pathParams.isNullOrEmpty()) {
        checkNotNull(pathParams)
        check(pathParams.size == navItem.pathParams.size)
        for (i in 0 until navItem.pathParams.size) {
            route = route.replace(navItem.pathParams[i], pathParams[i])
        }
    }
    navController.navigate(route)
}