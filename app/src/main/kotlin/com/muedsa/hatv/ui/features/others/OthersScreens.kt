package com.muedsa.hatv.ui.features.others

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.OutlinedIconButton
import androidx.tv.material3.Text

@Composable
fun NotFoundScreen() {
    FillTextScreen("404 Not Found (っ °Д °;)っ")
}

@Composable
fun NotImplementScreen() {
    FillTextScreen("Not Implement (っ °Д °;)っ")
}

@Composable
fun LoadingScreen() {
    FillTextScreen("Loading... {{{(>_<)}}}")
}

@Composable
fun EmptyDataScreen() {
    FillTextScreen("Empty(っ °Д °;)っ")
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ErrorScreen(onRefresh: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Error (っ °Д °;)っ", color = Color.White)
        if (onRefresh != null) {
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedIconButton(onClick = { onRefresh() }) {
                Icon(imageVector = Icons.Outlined.Refresh, contentDescription = "Refresh")
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FillTextScreen(context: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = context, color = Color.White)
    }
}