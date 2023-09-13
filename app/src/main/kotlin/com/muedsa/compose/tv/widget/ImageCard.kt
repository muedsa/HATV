package com.muedsa.compose.tv.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import coil.compose.AsyncImage
import com.muedsa.compose.tv.model.ContentModel
import com.muedsa.compose.tv.theme.TvTheme

@Composable
fun ImageContentCard(
    modifier: Modifier = Modifier,
    url: String,
    type: CardType = CardType.STANDARD,
    model: ContentModel? = null,
    onItemFocus: () -> Unit = {},
    onItemClick: () -> Unit = {},
) {

    if (model == null) {
        ImageCard(
            modifier = modifier,
            url = url,
            onItemFocus = onItemFocus,
            onItemClick = onItemClick
        )
    } else if (type == CardType.STANDARD) {
        StandardImageContentCard(
            modifier = modifier,
            url = url,
            model = model,
            onItemFocus = onItemFocus,
            onItemClick = onItemClick
        )
    } else if (type == CardType.COMPACT) {
        CompactImageContentCard(
            modifier = modifier,
            url = url,
            model = model,
            onItemFocus = onItemFocus,
            onItemClick = onItemClick
        )
    } else if (type == CardType.WIDE_STANDARD) {
        WideStandardImageContentCard(
            modifier = modifier,
            url = url,
            model = model,
            onItemFocus = onItemFocus,
            onItemClick = onItemClick
        )
    }
}

enum class CardType {
    STANDARD,
    COMPACT,
    WIDE_STANDARD
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ImageCard(
    modifier: Modifier = Modifier,
    url: String,
    onItemFocus: () -> Unit = {},
    onItemClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Card(onClick = { onItemClick() },
        modifier = modifier
            .padding(10.dp)
            .aspectRatio(16f / 9f)
            .onFocusChanged {
                if (it.isFocused) {
                    onItemFocus()
                }
            }
    ) {
        Box {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = url,
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            content()
        }
    }
}

@Composable
fun StandardImageContentCard(
    modifier: Modifier = Modifier,
    url: String,
    model: ContentModel,
    onItemFocus: () -> Unit = {},
    onItemClick: () -> Unit = {},
) {
    Column(modifier) {
        ImageCard(Modifier.fillMaxWidth(), url, onItemFocus, onItemClick)
        ContentBlock(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    top = 0.dp,
                    end = 8.dp,
                    bottom = 8.dp
                ),
            model = model,
            type = ContentBlockType.CARD,
            verticalArrangement = Arrangement.Top,
            textAlign = TextAlign.Center,
            descriptionMaxLines = 2
        )
    }
}

@Composable
fun CompactImageContentCard(
    modifier: Modifier = Modifier,
    url: String,
    model: ContentModel,
    onItemFocus: () -> Unit = {},
    onItemClick: () -> Unit = {},
) {
    ImageCard(modifier, url, onItemFocus, onItemClick) {
        ContentBlock(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .fillMaxHeight()
                .padding(8.dp),
            model = model,
            type = ContentBlockType.CARD,
            verticalArrangement = Arrangement.Bottom,
            descriptionMaxLines = 2
        )
    }
}


@Composable
fun WideStandardImageContentCard(
    modifier: Modifier = Modifier,
    url: String,
    model: ContentModel,
    onItemFocus: () -> Unit = {},
    onItemClick: () -> Unit = {},
) {
    Row(modifier) {
        ImageCard(Modifier.weight(1f), url, onItemFocus, onItemClick)
        ContentBlock(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 8.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                ),
            model = model,
            type = ContentBlockType.CARD,
            verticalArrangement = Arrangement.Top
        )
    }
}


@Preview
@Composable
fun NoContentImageContentCardPreview() {
    TvTheme {
        ImageContentCard(url = "", type = CardType.STANDARD)
    }
}

@Preview
@Composable
fun StandardImageContentCardPreview() {
    TvTheme {
        ImageContentCard(
            url = "",
            type = CardType.STANDARD,
            model = ContentModel(
                title = "Power Sisters",
                subtitle = "Superhero/Action • 2022 • 2h 15m",
                description = "A dynamic duo of superhero siblings " +
                        "join forces to save their city from a sini" +
                        "ster villain, redefining sisterhood in action."
            )
        )
    }
}


@Preview
@Composable
fun WideStandardImageContentCardPreview() {
    TvTheme {
        ImageContentCard(
            url = "",
            type = CardType.WIDE_STANDARD,
            model = ContentModel(
                title = "Power Sisters",
                subtitle = "Superhero/Action • 2022 • 2h 15m",
                description = "A dynamic duo of superhero siblings " +
                        "join forces to save their city from a sini" +
                        "ster villain, redefining sisterhood in action."
            )
        )
    }
}


@Preview
@Composable
fun CompactImageContentCardPreview() {
    TvTheme {
        ImageContentCard(
            url = "",
            type = CardType.COMPACT,
            model = ContentModel(
                title = "Power Sisters",
                subtitle = "Superhero/Action • 2022 • 2h 15m",
                description = "A dynamic duo of superhero siblings " +
                        "join forces to save their city from a sini" +
                        "ster villain, redefining sisterhood in action."
            )
        )
    }
}