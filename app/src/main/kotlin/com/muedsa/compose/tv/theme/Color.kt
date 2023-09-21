package com.muedsa.compose.tv.theme

import androidx.compose.foundation.background
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
@Preview
@Composable
fun ThemeColorPreview() {
    TvTheme {
        TvLazyColumn {
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                    text = "primary",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                    text = "primaryContainer",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.inversePrimary),
                    text = "inversePrimary"
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
                    text = "secondary",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer),
                    text = "secondaryContainer",
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.tertiary),
                    text = "tertiary",
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }

            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    text = "background",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                    text = "surface",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                    text = "surfaceVariant",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceTint),
                    text = "surfaceTint"
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.inverseSurface),
                    text = "inverseSurface",
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.error),
                    text = "error",
                    color = MaterialTheme.colorScheme.onError
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer),
                    text = "errorContainer",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.border),
                    text = "border",
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.borderVariant),
                    text = "borderVariant"
                )
            }
            item {
                Text(
                    modifier = Modifier.background(MaterialTheme.colorScheme.scrim),
                    text = "scrim"
                )
            }
        }
    }
}