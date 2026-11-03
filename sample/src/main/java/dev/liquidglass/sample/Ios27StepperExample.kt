package dev.liquidglass.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import dev.liquidglass.ios27.Ios27ListRow
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.ios27.ios27PressedClickable

/** Native Stepper host matching the source's 370x52 row and 92x32 accessory. */
@Composable
internal fun Ios27StepperExample(file: String, value: Int, onValueChange: (Int) -> Unit) {
    val colors = LocalIos27PageColors.current
    val decrement = rememberSceneText(file, "Decrement")
    val increment = rememberSceneText(file, "Increment")
    val surface = colors.pressed.copy(alpha = if (MaterialTheme.colors.isLight) 0.08f else 0.24f)
    val separator = colors.secondaryLabel.copy(alpha = 0.3f)

    Ios27ListRow(title = "Stepper", trailing = {
        Box(
            Modifier.size(92.dp, 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(surface, RoundedCornerShape(16.dp))
                .semantics { contentDescription = "Stepper"; stateDescription = value.toString() },
        ) {
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                StepperAction(
                    modifier = Modifier.width(46.dp).fillMaxHeight(),
                    description = "Stepper 减少",
                    enabled = value > 0,
                    onClick = { onValueChange((value - 1).coerceAtLeast(0)) },
                    glyph = decrement,
                    tint = colors.label,
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                )
                Box(Modifier.width(1.dp).height(24.dp).background(separator))
                StepperAction(
                    modifier = Modifier.width(46.dp).fillMaxHeight(),
                    description = "Stepper 增加",
                    enabled = value < 99,
                    onClick = { onValueChange((value + 1).coerceAtMost(99)) },
                    glyph = increment,
                    tint = colors.label,
                    shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                )
            }
        }
    })
}

@Composable
private fun StepperAction(
    modifier: Modifier,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit,
    glyph: SceneText?,
    tint: androidx.compose.ui.graphics.Color,
    shape: RoundedCornerShape,
) {
    Box(
        modifier
            .ios27PressedClickable(
                shape = shape,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        glyph?.let { SceneGlyphText(it, tint, Modifier.size(it.width.dp, it.height.dp)) }
    }
}
