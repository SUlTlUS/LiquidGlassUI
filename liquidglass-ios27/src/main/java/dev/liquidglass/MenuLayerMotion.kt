package dev.liquidglass

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min

/**
 * FlClash popup.dart _buildLevel:
 *   distance = receding ? depth - 1 + _unfold.value : 0
 * where depth is the number of cards ABOVE this card.
 *
 * Root: cardDepth=0, active top: topDepth=N. The same unbounded spring
 * drives all ancestors (rather than giving each level its own oscillator).
 */
internal fun menuRecedingDistance(
    cardDepth: Int,
    topDepth: Int,
    unfold: Float,
): Float {
    require(cardDepth >= 0 && topDepth >= cardDepth)
    return if (cardDepth == topDepth) 0f
    else topDepth - cardDepth - 1f + unfold
}

internal fun menuRecedingScale(distance: Float): Float =
    max(0f, 1f - .05f * distance)

/** Flutter _minShadow=1/6 and _levelShadowStep=1/3. */
internal fun menuRecedingShadow(distance: Float): Float =
    max(1f / 6f, 1f - distance / 3f)

/**
 * Flutter popup.dart _buildLevel: the current top card has
 * `shadow = settled`, but cards receding behind it have
 * `shadow = max(1/6, 1-distance/3)`.
 * On folding, the top card MUST gradually lose its shadow instead of
 * retaining full strength until the composable disappears at progress=0.
 */
internal fun menuCardShadowStrength(
    progress: Float,
    receding: Boolean,
    distance: Float,
): Float = if (receding) menuRecedingShadow(distance)
else progress.coerceIn(0f, 1f)

/**
 * FlClash's root card retains its full rim while receding under a submenu.
 * The popup morph instead paints no rim at all when returning to the source:
 * popup.dart::_paintReturn passes no rim to the platter and draws no rim line.
 */
internal fun menuRootRimStrength(
    settled: Float,
    merged: Float,
    returningToSource: Boolean,
): Float = if (returningToSource) 0f
else settled.coerceIn(0f, 1f) * merged.coerceIn(0f, 1f)

/**
 * FlClash popup.dart::_buildLevel paints its parent-cover layer using
 * colorScheme.surface, NOT the floating card surface. In the reference
 * seeded scheme this is a near-neutral tone 98 in light and tone 6 in dark
 * (.agents/surfaces.md). The previous #24252B was closer to dark card tone
 * 15 and visibly brightened the parent instead of dimming it.
 *
 * The reference lets a user tint/replace surface via its theme; these are
 * the neutral-mode defaults for the standalone Compose showcase.
 */
internal fun menuRecedingSurfaceColor(dark: Boolean, surface: Color? = null): Color =
    surface ?: if (dark) Color(0xFF121316) else Color(0xFFF9F9FB)

/** Flutter _levelFadeStep=.2 and max visible dim is .4. */
internal fun menuRecedingDim(distance: Float): Float =
    min(.4f, max(0f, .2f * distance))

/**
 * FlClash _push resets _unfold.value to 0 before setState publishes a
 * new _path. Compose's Animatable.snapTo is suspend and therefore cannot
 * do that synchronously in a click handler. Guard this one composition gap:
 * while a new top level is pending its reset, NEVER use the previous
 * level's settled spring value (usually 1f).
 */
internal fun menuVisibleTopUnfold(
    springValue: Float,
    topDepth: Int,
    firstIndex: Int,
    animatedFirstIndex: Int,
    firstOpening: Boolean,
    pendingNestedDepth: Int,
    restoringParent: Boolean,
): Float = when {
    restoringParent -> 1f
    topDepth > 1 && pendingNestedDepth == topDepth -> 0f
    topDepth == 1 && firstOpening && firstIndex != animatedFirstIndex -> 0f
    else -> springValue
}

internal fun menuLevelProgress(
    cardDepth: Int,
    topDepth: Int,
    currentUnfold: Float,
): Float = if (cardDepth == topDepth) currentUnfold else 1f

/** No source image: Flutter's 200ms Curves.easeOut dismissal. */
internal fun menuNoSourceDismissFraction(seconds: Float): Float {
    val t = (seconds / .2f).coerceIn(0f, 1f)
    return 1f - (1f - t) * (1f - t)
}

/** A fully receding level has no pressable content even while still drawn. */
internal fun menuLevelInteractive(cardDepth: Int, topDepth: Int): Boolean =
    cardDepth == topDepth
