package com.muedsa.compose.tv.widget

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun AppCloseHandler(
    onAllowBack: () -> Unit = {}
) {

    var allowBack by remember { mutableStateOf(false) }
    var ticker by remember { mutableIntStateOf(8) }

    LaunchedEffect(key1 = ticker) {
        delay(250)
        if (ticker > 0) {
            ticker--
        } else {
            allowBack = false
        }
    }

    BackHandler(enabled = !allowBack) {
        allowBack = true
        ticker = 8
        try {
            onAllowBack()
        } catch (_: Throwable) {
        }
    }
}