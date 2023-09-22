package com.muedsa.compose.tv.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

internal object CustomerColor {
    val outline = Color(red = 147, green = 143, blue = 153) // PaletteTokens.NeutralVariant60
    val outlineVariant = Color(red = 73, green = 69, blue = 79) // PaletteTokens.NeutralVariant30
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvColorPreview(modifier: Modifier = Modifier) {
    TvLazyColumn(modifier) {
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                text = "[On]Primary",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                text = "[On]PrimaryContainer",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.inversePrimary),
                text = "InversePrimary"
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary),
                text = "[On]Secondary",
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                text = "[On]SecondaryContainer",
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary),
                text = "[On]Tertiary",
                color = MaterialTheme.colorScheme.onTertiary
            )
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                text = "[On]Background",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                text = "[On]Surface",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                text = "[On]SurfaceVariant",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceTint),
                text = "SurfaceTint"
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.inverseSurface),
                text = "[On]InverseSurface",
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.error),
                text = "[On]Error",
                color = MaterialTheme.colorScheme.onError
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer),
                text = "[On]ErrorContainer",
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.border),
                text = "Border",
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.borderVariant),
                text = "BorderVariant"
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.scrim),
                text = "Scrim"
            )
        }
    }
}


@Preview
@Composable
fun TvThemeColorPreview() {
    TvTheme {
        TvColorPreview()
    }
}