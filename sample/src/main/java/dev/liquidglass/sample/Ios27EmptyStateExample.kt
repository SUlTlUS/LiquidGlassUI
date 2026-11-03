package dev.liquidglass.sample

import dev.liquidglass.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberCanvasBackdrop
import dev.liquidglass.GlassButton
import dev.liquidglass.ios27.Ios27PageColors
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.ios27PressedClickable

private const val TitleAndSubtitle = "Config=Title and Subtitle"
private const val SymbolInCircle = "Config=Symbol in Circle"
private const val LargeSymbol = "Config=Large Symbol"
private val EmptyStateButtonTop = 635.dp
private val EmptyStateLargeSymbolBodyOffset = 67.dp

// U+1002C3 is the source node name used by both symbol variants.
private val EmptySymbolNode = String(Character.toChars(0x1002C3))

/**
 * Native Empty States host. The body keeps the source 402pt coordinate model:
 * the page scaffold has already consumed the 54pt title-toolbar block, so these
 * offsets are the source coordinates normalized by the toolbar's 62pt origin.
 * The button uses the existing LiquidButton; only its host geometry and tint vary.
 */
@Composable
internal fun Ios27EmptyStateExample(
    entry: IphoneExample,
    colors: Ios27PageColors,
    onCreate: () -> Unit,
) {
    val context = LocalContext.current
    val buttonBackdrop = rememberCanvasBackdrop { drawRect(colors.page) }
    val message = remember(entry.file) { loadSceneText(context, entry.file, "Message") }
    val description = remember(entry.file) { loadSceneText(context, entry.file, "Description") }
    val variant = entry.variant
    val messageY = when (variant) {
        TitleAndSubtitle -> 184.dp
        SymbolInCircle -> 217.dp
        LargeSymbol -> 190.5.dp
        else -> 184.dp
    }
    val descriptionY = when (variant) {
        TitleAndSubtitle -> 210.dp
        SymbolInCircle -> 243.dp
        LargeSymbol -> 216.5.dp
        else -> 210.dp
    }
    val tint = when (variant) {
        SymbolInCircle -> Color(0xFFFF2D55)
        LargeSymbol -> Color(0xFFFFCC00)
        else -> Color(0xFF0088FF)
    }
    val actionLabel = when (variant) {
        SymbolInCircle -> "New Reminder"
        LargeSymbol -> "Primary Action"
        else -> "Create Note"
    }
    val actionDescription = when (variant) {
        SymbolInCircle -> "新建提醒"
        LargeSymbol -> "主要操作"
        else -> "新建内容"
    }
    val bodyOffset = if (variant == LargeSymbol) EmptyStateLargeSymbolBodyOffset else 0.dp

    Box(
        Modifier.offset(y = bodyOffset).fillMaxWidth().padding(horizontal = 20.dp).height(786.dp),
    ) {
        if (variant == SymbolInCircle) {
            Box(
                Modifier.offset(x = 140.dp, y = 151.dp).size(50.dp)
                    .clip(CircleShape).background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                SceneGlyphIcon(entry.file, EmptySymbolNode, tint, Modifier.fillMaxSize())
            }
        } else if (variant == LargeSymbol) {
            SceneGlyphIcon(
                entry.file,
                EmptySymbolNode,
                tint,
                Modifier.offset(x = 16.dp, y = 125.5.dp).size(298.dp, 49.dp),
            )
        }
        message?.let {
            SceneGlyphText(
                it,
                if (variant == TitleAndSubtitle) colors.secondaryLabel else colors.label,
                Modifier.offset(x = 16.dp, y = messageY),
            )
        }
        description?.let {
            SceneGlyphText(it, colors.secondaryLabel, Modifier.offset(x = 16.dp, y = descriptionY))
        }
        EmptyStateButton(
            onClick = onCreate,
            backdrop = buttonBackdrop,
            tint = tint,
            description = actionDescription,
            modifier = Modifier.offset(y = EmptyStateButtonTop).size(330.dp, 52.dp),
        ) {
            Text(actionLabel, style = Ios27Tokens.Type.Headline, color = Color.White)
        }
    }
}

@Composable
private fun EmptyStateButton(
    onClick: () -> Unit,
    backdrop: com.zeaze.tianyinwallpaper.backdrop.Backdrop,
    tint: Color,
    description: String,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    val accessible = modifier.semantics {
        contentDescription = description
        role = Role.Button
    }
        GlassButton(
            onClick = onClick,
            backdrop = backdrop,
            modifier = accessible,
            tint = tint,
            height = 52.dp,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            content = { content() },
        )
}
