package dev.liquidglass27.glass

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * FlClash popup.dart::_buildRow puts a Glyph BEFORE the label and only uses
 * the trailing slot for the chevron or check. The prototype's "symbol" is a
 * legacy text glyph; the new explicit glyph takes precedence.
 */
internal fun menuLeadingGlyph(action: GlassMenuAction): String? =
    action.glyph ?: action.symbol.takeIf { it.isNotBlank() && it != "›" }

internal fun menuHasLeadingGlyph(action: GlassMenuAction): Boolean =
    action.vectorGlyph != null || menuLeadingGlyph(action) != null

internal fun menuHasEndMark(action: GlassMenuAction, header: Boolean = false): Boolean =
    header || action.subActions.isNotEmpty() || action.selected

/** Nullable Flutter checked maps to checked semantics only when present. */
internal fun Modifier.menuCheckedSemantics(action: GlassMenuAction): Modifier =
    if (action.selected) this.semantics { toggleableState = ToggleableState.On }
    else this

/** 24dp vertical insets + 20dp headline + up to two 18dp footnote lines. */
internal fun menuItemHeightDp(fontScale: Float, subtitleLines: Int): Float =
    menuRowHeightDp(fontScale) +
        subtitleLines.coerceIn(0, 2) * 18f * fontScale.coerceAtLeast(0f)

private data class MenuRowTextSignature(
    val subtitle: String?,
    val hasGlyph: Boolean,
    val hasEndMark: Boolean,
    val section: GlassMenuSection?,
)

/**
 * TextMeasurer uses the same 13sp/18sp, maxLines=2 constraints as painted
 * subtitle Text. The resulting heights are shared by card geometry, scroll,
 * opening row anchor, section offsets and pointer hit-testing.
 *
 * No subtitle means a strict 44dp row, preserving the established spring
 * and layout geometry of every existing ordinary menu item.
 */
@Composable
internal fun rememberMenuContentRows(
    items: List<GlassMenuAction>,
    fontScale: Float,
    panelWidthPx: Float,
    density: Float,
): MenuContentRows {
    val measurer = rememberTextMeasurer()
    val signature = items.map {
        MenuRowTextSignature(
            it.subtitle, menuHasLeadingGlyph(it), menuHasEndMark(it), it.section,
        )
    }
    return remember(signature, panelWidthPx, density, fontScale, measurer) {
        val heights = items.mapIndexed { index, action ->
            val subtitle = signature[index].subtitle
            if (subtitle == null) menuRowHeightDp(fontScale)
            else {
                // Full card minus 8dp inset on each side, _itemPadding,
                // optional 20dp glyph + 12dp gap and 16dp mark + 8dp gap.
                val hasMark = signature[index].hasEndMark
                val rowPadding = if (hasMark) 20f else 24f
                val glyphWidth = if (signature[index].hasGlyph) 32f else 0f
                val markWidth = if (hasMark) 24f else 0f
                val availablePx = (panelWidthPx -
                    (16f + rowPadding + glyphWidth + markWidth) * density)
                    .roundToInt().coerceAtLeast(1)
                val measured = measurer.measure(
                    text = AnnotatedString(subtitle),
                    style = TextStyle(
                        fontSize = 13.sp, lineHeight = 18.sp,
                        fontWeight = FontWeight.Normal, letterSpacing = 0.sp,
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    constraints = Constraints(maxWidth = availablePx),
                )
                menuItemHeightDp(fontScale, measured.lineCount.coerceIn(1, 2))
            }
        }
        menuContentRows(items, fontScale, heights)
    }
}

/**
 * Source _buildRow content: leading 20dp glyph, label 15sp/20sp, optional
 * subtitle 13sp/18sp (two lines), and trailing 16dp check/chevron.
 *
 * Width is controlled by the enclosing row. The row's action, pressed fill,
 * disabled alpha and pointer handling remain in GlassMenu/GlassSubmenu.
 */
@Composable
internal fun MenuItemContent(
    action: GlassMenuAction,
    dark: Boolean,
    modifier: Modifier = Modifier,
    headerProgress: Float? = null,
) {
    val mark = menuHasEndMark(action, header = headerProgress != null)
    val leading = menuLeadingGlyph(action)
    val vector = action.vectorGlyph ?: leading?.let(GlassGlyphs::forSymbol)
    val colors = glassColors(dark)
    val foreground = if (action.destructive) {
        if (dark) Color(0xFFFF4245) else Color(0xFFFF383C)
    } else colors.onSurface
    val secondary = colors.onSurfaceVariant
    Row(
        modifier.fillMaxWidth().padding(
            start = 12.dp,
            end = if (mark) 8.dp else 12.dp,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (vector != null || leading != null) {
            Box(Modifier.size(20.dp), contentAlignment = Alignment.Center) {
                if (vector != null) GlassGlyphIcon(vector, foreground, Modifier.size(20.dp))
                else Text(
                    leading.orEmpty(), fontSize = 19.sp, lineHeight = 20.sp,
                    color = foreground, maxLines = 1,
                )
            }
            Spacer(Modifier.width(12.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(
                action.text, fontSize = 15.sp, lineHeight = 20.sp,
                fontWeight = FontWeight.Normal, color = foreground,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
            action.subtitle?.let { subtitle ->
                Text(
                    subtitle, fontSize = 13.sp, lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal, color = secondary,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (mark) {
            Spacer(Modifier.width(8.dp))
            Box(Modifier.size(16.dp), contentAlignment = Alignment.Center) {
                GlassGlyphIcon(
                    glyph = if (headerProgress != null || action.subActions.isNotEmpty())
                        GlassGlyphs.chevronForward else GlassGlyphs.check,
                    modifier = if (headerProgress != null) Modifier.graphicsLayer {
                        rotationZ = 90f * headerProgress.coerceIn(0f, 1f)
                    } else Modifier,
                    color = foreground,
                )
            }
        }
    }
}
