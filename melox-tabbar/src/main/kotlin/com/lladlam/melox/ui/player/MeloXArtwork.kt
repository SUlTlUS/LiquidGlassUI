package com.lladlam.melox.ui.player
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import coil3.compose.AsyncImage
import com.lladlam.melox.R
import com.lladlam.melox.ui.glass.*

@Composable
internal fun Artwork(
    url: String?,
    modifier: Modifier,
    model: Any? = url,
) {
    Box(
        modifier = modifier.background(Color.White.copy(alpha = 0.07f)),
        contentAlignment = Alignment.Center,
    ) {
        if (model != null && (model !is String || model.isNotBlank())) {
            AsyncImage(
                model = model,
                contentDescription = stringResource(R.string.player_artwork),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            MeloXSymbolIcon(
                symbol = MeloXSymbol.MusicNote,
                modifier = Modifier.size(44.dp),
                color = Color.White.copy(alpha = 0.24f),
                iconSize = 42.sp,
                contentDescription = stringResource(R.string.player_artwork_default),
            )
        }
    }
}
