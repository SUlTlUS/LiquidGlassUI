package dev.liquidglass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Flutter _buildContent's divider and _buildSectionTitle, not a row. */
@Composable
internal fun MenuSectionDecoration(
    index: Int,
    section: GlassMenuSection?,
    dark: Boolean,
    fontScale: Float,
) {
    if (section == null) return
    if (index > 0) {
        Box(
            modifier = Modifier.fillMaxWidth().height(17.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(Modifier.fillMaxWidth().height(1.dp).background(
                if (dark) Color.White.copy(alpha = .13f)
                else Color.Black.copy(alpha = .08f),
            ))
        }
    }
    section.title?.let { title ->
        Text(
            title,
            modifier = Modifier.fillMaxWidth()
                .height((10f + 18f * fontScale).dp)
                .padding(start = 12.dp, top = 8.dp, bottom = 2.dp)
                .semantics { heading() },
            color = if (dark) Color.White.copy(alpha = .70f)
                else Color(0xFF343843).copy(alpha = .78f),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
