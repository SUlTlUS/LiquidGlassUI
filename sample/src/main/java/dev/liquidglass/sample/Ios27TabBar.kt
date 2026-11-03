package dev.liquidglass.sample

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.zeaze.tianyinwallpaper.backdrop.Backdrop
import com.zeaze.tianyinwallpaper.catalog.components.LiquidBottomTab
import com.zeaze.tianyinwallpaper.catalog.components.LiquidBottomTabsRegular
import dev.liquidglass.ios27.Ios27BottomTabsSplitSearch as LiquidBottomTabsSplitSearch
import com.zeaze.tianyinwallpaper.catalog.components.LiquidBottomTabsStyle
import dev.liquidglass.ios27.LocalIos27PageColors
import kotlin.math.roundToInt

private const val QweaTabSettleDurationMillis = 380
private val QweaTabSettleInterpolator = OvershootInterpolator(1.1f)
private val QweaTabSettleEasing = Easing { fraction ->
    QweaTabSettleInterpolator.getInterpolation(fraction)
}

/**
 * Page transition synchronized to QWEA0 LiquidGlassTabBar.animateDropletTo().
 *
 * QWEA0 does not expose a page-transition API; its actual tab settle animation uses
 * ValueAnimator(380 ms) + OvershootInterpolator(1.1). Page content follows that exact
 * settle curve while the existing iOS27 glass Tab Bar keeps its authored appearance.
 */
@Composable
internal fun QweaTabPageTransition(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit,
) {
    AnimatedContent(
        targetState = selectedTabIndex,
        modifier = modifier.clipToBounds(),
        transitionSpec = {
            val direction = if (targetState >= initialState) 1 else -1
            val enter = slideInHorizontally(
                animationSpec = tween(
                    durationMillis = QweaTabSettleDurationMillis,
                    easing = QweaTabSettleEasing,
                ),
                initialOffsetX = { width -> (direction * width * 0.12f).roundToInt() },
            ) + scaleIn(
                animationSpec = tween(
                    durationMillis = QweaTabSettleDurationMillis,
                    easing = QweaTabSettleEasing,
                ),
                initialScale = 0.985f,
            ) + fadeIn(animationSpec = tween(durationMillis = 180))

            val exit = slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = QweaTabSettleDurationMillis,
                    easing = QweaTabSettleEasing,
                ),
                targetOffsetX = { width -> (-direction * width * 0.06f).roundToInt() },
            ) + scaleOut(
                animationSpec = tween(
                    durationMillis = QweaTabSettleDurationMillis,
                    easing = QweaTabSettleEasing,
                ),
                targetScale = 0.992f,
            ) + fadeOut(animationSpec = tween(durationMillis = 160))

            enter togetherWith exit
        },
        label = "QweaTabPageTransition",
    ) { page ->
        Box(Modifier.fillMaxSize()) {
            content(page)
        }
    }
}

/**
 * Reusable host adapter for the iOS 27 Tab Bar examples.
 *
 * Width policy and split-search geometry live in the glass component API;
 * this host only supplies labels, state, and the page-specific glyph slot.
 */
@Composable
internal fun Ios27TabBarExample(
    file: String,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    backdrop: Backdrop?,
    isLightTheme: Boolean,
    modifier: Modifier = Modifier,
    splitSearch: Boolean = false,
    onSearchClick: () -> Unit = {},
    labels: List<String> = listOf("全部", "收藏", "搜索"),
    labelContent: @Composable (index: Int, label: String, tint: Color) -> Unit = { _, label, tint ->
        Text(label, color = tint)
    },
) {
    if (labels.isEmpty()) return

    val colors = LocalIos27PageColors.current
    val tabStyle = LiquidBottomTabsStyle.default(isLightTheme).copy(
        trackHeight = 62.dp,
        indicatorHeight = 54.dp,
    )
    if (splitSearch) {
        LiquidBottomTabsSplitSearch(
            selectedTabIndex = { selectedTabIndex },
            onTabSelected = onTabSelected,
            onSearchClick = onSearchClick,
            backdrop = backdrop,
            tabsCount = labels.take(2).size,
            isLightTheme = isLightTheme,
            itemWidth = 118.dp,
            itemSpacing = 0.dp,
            searchWidth = 62.dp,
            searchHeight = 62.dp,
            searchGap = 16.dp,
            searchContentDescription = "搜索",
            modifier = modifier,
            style = tabStyle,
            searchContent = {
                rememberSceneText(file, String(Character.toChars(0x1002AB)))?.let {
                    SceneGlyphText(it, colors.label)
                }
            },
        ) {
            labels.take(2).forEachIndexed { index, label ->
                LiquidBottomTab(
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.semantics { selected = selectedTabIndex == index },
                ) {
                    labelContent(index, label, if (selectedTabIndex == index) colors.accent else colors.secondaryLabel)
                }
            }
        }
    } else {
        LiquidBottomTabsRegular(
            selectedTabIndex = { selectedTabIndex },
            onTabSelected = onTabSelected,
            backdrop = backdrop,
            tabsCount = labels.size,
            isLightTheme = isLightTheme,
            itemWidth = 94.dp,
            itemSpacing = (-8).dp,
            modifier = modifier,
            style = tabStyle,
        ) {
            labels.forEachIndexed { index, label ->
                LiquidBottomTab(
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.semantics { selected = selectedTabIndex == index },
                ) {
                    labelContent(index, label, if (selectedTabIndex == index) colors.accent else colors.secondaryLabel)
                }
            }
        }
    }
}
