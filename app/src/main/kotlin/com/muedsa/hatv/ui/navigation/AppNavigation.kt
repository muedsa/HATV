package com.muedsa.hatv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.muedsa.compose.tv.widget.ErrorMessageBoxState
import com.muedsa.hatv.ui.features.detail.VideoDetailScreen
import com.muedsa.hatv.ui.features.home.HomeScreen
import com.muedsa.hatv.ui.features.others.NotFoundScreen
import com.muedsa.hatv.viewmodel.SearchViewModel

@Composable
fun AppNavigation(navController: NavHostController, errorMsgBoxState: ErrorMessageBoxState) {

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val searchViewModel: SearchViewModel = hiltViewModel(viewModelStoreOwner)

    NavHost(
        navController = navController,
        startDestination = buildRoute(NavigationItems.Home, listOf("0"))
    ) {

        composable(
            route = NavigationItems.Home.path,
            arguments = listOf(navArgument("tabIndex") {
                type = NavType.IntType
            })
        ) {
            HomeScreen(
                tabIndex = checkNotNull(it.arguments?.getInt("tabIndex")),
                homePageViewModel = hiltViewModel(viewModelStoreOwner),
                searchViewModel = searchViewModel,
                errorMsgBoxState = errorMsgBoxState,
                onNavigate = { navItem, pathParams ->
                    onNavigate(navController, navItem, pathParams)
                }
            )
        }

        composable(
            route = NavigationItems.Detail.path,
            arguments = listOf(navArgument("videoId") {
                type = NavType.StringType
            })
        ) {
            VideoDetailScreen(
                searchViewModel = searchViewModel,
                errorMsgBoxState = errorMsgBoxState,
                onNavigate = { navItem, pathParams ->
                    onNavigate(navController, navItem, pathParams)
                }
            )
        }

        composable(NavigationItems.NotFound.path) {
            NotFoundScreen()
        }
    }
}

fun buildRoute(
    navItem: NavigationItems,
    pathParams: List<String>?
): String {
    var route = navItem.path
    if (!navItem.pathParams.isNullOrEmpty()) {
        checkNotNull(pathParams)
        check(pathParams.size == navItem.pathParams.size)
        for (i in 0 until navItem.pathParams.size) {
            route = route.replace(navItem.pathParams[i], pathParams[i])
        }
    }
    return route
}

fun onNavigate(
    navController: NavHostController,
    navItem: NavigationItems,
    pathParams: List<String>?
) {
    navController.navigate(buildRoute(navItem, pathParams))
}
