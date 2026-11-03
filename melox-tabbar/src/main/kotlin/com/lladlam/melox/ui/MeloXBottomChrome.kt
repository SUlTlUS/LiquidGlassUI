package com.lladlam.melox.ui

import dev.liquidglass.*

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import com.zeaze.tianyinwallpaper.backdrop.backdrops.*
import com.zeaze.tianyinwallpaper.catalog.utils.InteractiveHighlight
import com.kyant.shapes.Capsule
import com.lladlam.melox.R
import com.lladlam.melox.ui.animation.*
import com.lladlam.melox.ui.glass.*
import com.lladlam.melox.ui.glass.publicdemo.*
import com.lladlam.melox.ui.settings.MeloXSettingsRuntime
import kotlinx.coroutines.launch
import kotlin.math.*

data class AppTab(val name: String, @StringRes val titleRes: Int = 0) : java.io.Serializable {
    companion object {
        val Home = AppTab("Home", R.string.tab_home)
        val Explore = AppTab("Explore", R.string.tab_explore)
        val Library = AppTab("Library", R.string.tab_library)
        val Podcasts = AppTab("Podcasts", R.string.tab_podcasts)
        val Downloads = AppTab("Downloads", R.string.tab_downloads)
        val Cloud = AppTab("Cloud", R.string.tab_cloud)
        val Settings = AppTab("Settings", R.string.tab_settings)
        val Search = AppTab("Search", R.string.tab_search)
        val entries = listOf(Home, Explore, Library, Podcasts, Downloads, Cloud, Settings, Search)
        fun valueOf(name: String) = entries.first { it.name == name }
        fun custom(key: String) = AppTab("custom:$key")
    }
}

/**
 * 展开态下底栏 nav 胶囊的高度与搜索键边长 —— 同一个设计基准（360dp 屏）。
 * 收缩态对应 [ChromeCompactSize]。两处必须同源，否则展开态会出现
 * 「胶囊比邻居高一截」的错位。
 */
private val ChromeExpandedSize = 57.dp

/** 收缩态下底栏 nav 胶囊的高度与搜索键边长。 */
private val ChromeCompactSize = 46.dp

/** 收缩态胶囊宽度。和 [MeloXBottomChrome] 里的 compact 宽度必须同源。 */
private val ChromeCompactWidth = 48.dp

private val ChromeHorizontalMargin = 12.dp

private val ChromeSearchGap = 7.dp

/**
 * ⚠ **2026-09-25 一笔勾销**：本常量（及其自研「实心着色胶囊」Layer 2 方案）已随
 *   一比一复刻 BiliNext `LiquidGlassTabsBar.kt` 全部删除 —— Layer 2 现在是一块
 *   **真玻璃**（`meloXLiquidCaptureLayer`），不再是本项目自创的着色胶囊。
 *   保留此墓碑仅为阻止后来者重新发明它。
 */

@Composable
internal fun MeloXBottomChrome(
    selectedTab: AppTab,
    onSelect: (AppTab) -> Unit,
    hasMedia: Boolean,
    minimized: Boolean,
    visibleRootTabs: List<AppTab>,
    searchEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    layout: GlassTabBarLayout = GlassTabBarLayout(),
    animateSearchVisibility: Boolean = false,
    miniPlayer: @Composable (compactProgress: Float) -> Unit,
) {
    val tabsBackdrop = rememberLayerBackdrop()
    val navigationSurface = bottomGlassSurfaceColor()
    val dockScope = rememberCoroutineScope()
    val searchReveal = remember {
        Animatable(if (animateSearchVisibility) 0f else if (layout.showSearchButton) 1f else 0f)
    }
    LaunchedEffect(layout.showSearchButton, animateSearchVisibility) {
        val target = if (layout.showSearchButton) 1f else 0f
        if (animateSearchVisibility) searchReveal.animateTo(target,
            androidx.compose.animation.core.tween(260, easing = androidx.compose.animation.core.FastOutSlowInEasing))
        else searchReveal.snapTo(target)
    }
    val searchPresence = if (animateSearchVisibility) searchReveal.value.coerceIn(0f, 1f)
        else if (layout.showSearchButton) 1f else 0f
    val rawProgress by animateFloatAsState(
        targetValue = if (minimized) 1f else 0f,
        // 展开 / 收缩各用**一条完整弹簧**（方向不同、手感不同）：
        //   · 展开 bounce 0.245 → ζ=0.755，过冲 2.68% → 外扩 6.02dp（左 2.01 / 右 4.01dp，
        //     与搜索键的 7dp 缝留 3.00dp 视觉余量）—— 空间受限，所以收着弹、且**偏右**落；
        //   · 收缩 bounce 0.300 → ζ=0.700，过冲 4.60% → 10.30dp —— 四周空出来了，放开弹。
        // ⚠ 别让两个方向共用 spec：共用时保护邻居只剩「对展开方向事后压缩」一条路，
        //   而压缩后的曲线不再是弹簧（阻尼包络仍是 0.30 的），手感与收缩方向不对等。
        //   target 与 spec 在同一帧一起算 —— 方向就是 targetValue 本身。
        animationSpec = if (minimized) BottomBarCollapseSpec else BottomBarExpandSpec,
        label = "melox-tab-minimize-progress",
    )
    // 语义值（alpha / 图层门控 / 选中态判定）必须夹在 [0,1]，否则负 alpha 会炸。
    val progress = rawProgress.coerceIn(0f, 1f)

    // 几何值 = 弹簧本身（带符号限幅）。两侧对称、不过任何窗口 —— 详见 sprungFrac 注释：
    // 旧的 [0.20, 1.00] 窗口在展开方向把几何钉死在弹簧峰值速度处，随后空转 246ms。
    val bounceFrac = sprungFrac(rawProgress)

    val labelStage = smoothStep(progress, 0.00f, 0.32f)   // alpha 用，保持夹住
    val dropStage = smoothStep(progress, 0.78f, 1.00f)    // 容器高度，不参与过冲

    val navHeight = lerpDpBouncy(layout.height, ChromeCompactSize, bounceFrac)
    // ⚠ f 的**负方向**才是「展开外扩」（f=0 是展开稳态、f=1 是收缩稳态）。
    // 这里只夹下界：搜索键右对齐，若跟着外扩会吃掉与 nav 之间的固有间隙，
    // 而它自身的外扩量只有 0.9dp、肉眼不可见 —— 整段间隙留给 nav 的过冲更划算。
    val searchSize = lerpDpBouncy(layout.height, ChromeCompactSize, bounceFrac.coerceAtLeast(0f))
    val mediaReveal by animateFloatAsState(
        targetValue = if (hasMedia) 1f else 0f,
        // 下方是 miniLift × mediaReveal 的纯乘子，不经过 lerpDp 的夹取，
        // 所以过冲天然可见 —— 播放栏出现时会先"弹过"再落位。
        animationSpec = spring(
            dampingRatio = MeloXSprings.dampingRatio(MeloXSprings.MiniPlayerRevealBounce),
            stiffness = MeloXSprings.MiniPlayerRevealStiffness,
        ),
        label = "melox-mini-player-reveal",
    )
    val expandedChromeHeight by animateDpAsState(
        targetValue = if (hasMedia) 124.dp else 64.dp,
        animationSpec = spring(dampingRatio = 0.9f, stiffness = 360f),
        label = "melox-chrome-height",
    )
    val chromeHeight = lerpDp(expandedChromeHeight, 56.dp, dropStage)
    val labelAlpha = 1f - labelStage
    val expandedLayerAlpha = 1f - smoothStep(progress, 0.43f, 0.72f)
    val compactLayerAlpha = smoothStep(progress, 0.52f, 0.82f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (layout.avoidNavigationBar) Modifier.navigationBarsPadding() else Modifier)
            // Mei keeps the 64dp navigation capsule 8dp above the gesture
            // inset, giving the dock the same breathing room as iOS.
            .padding(bottom = layout.bottomMargin),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(chromeHeight),
        ) {
            val horizontalMargin = layout.horizontalMargin
            val compactSize = ChromeCompactWidth
            // nav 胶囊右缘 ↔ 搜索键左缘的固有间隙（展开态）。
            // 官方截图实测 = **10px = 7.12dp**（rim 峰法：nav 右缘 472 → 搜索键左缘 483，
            // 阈值 140~205 全区间稳定），取 7dp。
            // ⚠ 这个缝同时是展开方向弹簧的约束：它定出右缘可用外扩 = 7 − 3（视觉余量）= 4dp，
            //   再按 NavExpandLeftShare(左1:右2) 反推出总外扩 6dp 与
            //   MeloXSprings.BottomBarExpandBounce 0.245。改这个值必须回头重算那个常数。
            val searchSpacing = layout.searchSpacing * searchPresence
            val compactGap = 8.dp
            val expandedNavWidth =
                (maxWidth - horizontalMargin * 2 - searchSpacing -
                    layout.height * searchPresence).coerceAtLeast(48.dp)
            // 宽度就是弹簧本身：lerpDpBouncy 不夹取，展开方向 f<0 会让胶囊长到
            // 272 + 6.02 = 278.02dp —— **不需要按预算压缩、也不需要限幅**，因为展开那条
            // 弹簧（MeloXSprings.BottomBarExpandBounce）本来就是照着这点余地定出来的。
            val navWidth = lerpDpBouncy(expandedNavWidth, compactSize, bounceFrac)
            // 外扩 = 胶囊变大，多出来的宽度往哪边落由 NavExpandLeftShare 决定：
            // 现取 **左 1 : 右 2（偏右）** —— 右侧固有缝只有 7dp，那点余量必须花在
            // 「朝搜索键弹过去」这个方向上；左侧是 12dp 屏幕边距、没有邻居，让出 2dp 很安全。
            // 代价：nav 中心在峰值轻微右漂 1dp（148 → 149），这是「右边弹得更多」的必然结果，
            // 不是 bug —— 想消除就只能回到 1/2 等分，而那正是被否掉的「对称」。
            // 收缩方向 f>0 宽度不超稳态，coerceAtLeast(0.dp) 让越界量归零 → 退回纯边距，
            // 也就是收缩天然是**左锚定**（左缘恒 12dp、右缘 284 → 60dp）。
            // 旧的「先喂右边、余量给左边」+「等比压缩」两层都被这一行替掉了。
            val navOffsetX = horizontalMargin -
                (navWidth - expandedNavWidth).coerceAtLeast(0.dp) * NavExpandLeftShare
            val navShape = LocalNavigationShape.current ?: Capsule()
            val primaryTabs = visibleRootTabs.map { it to it.rootGlyph() }

            val desiredCompactMiniVisibleWidth =
                (maxWidth - horizontalMargin * 2 - compactSize - compactGap -
                    (compactSize + compactGap) * searchPresence)
                    .coerceAtLeast(80.dp)
            val compactMiniWrapperWidth = desiredCompactMiniVisibleWidth
            val compactMiniWrapperX = horizontalMargin + compactSize + compactGap
            val miniWrapperWidth = lerpDpBouncy(maxWidth - horizontalMargin * 2, compactMiniWrapperWidth, bounceFrac)
            val miniWrapperX = lerpDpBouncy(horizontalMargin, compactMiniWrapperX, bounceFrac)
            val miniLift = lerpDpBouncy(66.dp, 0.dp, bounceFrac) * mediaReveal
            // 收缩态下播放栏需与两侧底栏按钮/搜索键共中心线。
            // 实测播放栏整体比按钮高 1dp（top/bottom 各差 4px），
            // 这里随收缩进度补一个 1dp 下移，把两者垂直中心压平。
            val miniCenterAlignNudge = lerpDpBouncy(0.dp, 1.dp, bounceFrac) * mediaReveal

            if (hasMedia) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(
                            x = miniWrapperX,
                            y = -miniLift + miniCenterAlignNudge,
                        )
                        .width(miniWrapperWidth),
                ) {
                    miniPlayer(progress)
                }
            }

            // ════════════════════════════════════════════════════════════════════════
            //  一比一复刻 BiliNext `LiquidGlassTabsBar.kt`（2026-09-25 用户指令）
            //
            //  权威源：D:/bilinext/app/src/main/java/com/android/bilinext/ui/
            //          components/LiquidGlassTabsBar.kt   （Apache-2.0 → GPL-3.0 允许）
            //
            //  BiliNext 三层结构（原值，**不再自创**）：
            //    L1 可见面板   : height = maxHeight(= navHeight)
            //                    + padding(horizontal = 10dp)
            //                    + vibrancy + blur(8dp) + lens(24dp·aspectScale, 24dp)
            //                    + layerBlock { scaleX = 1; scaleY = 1 }   ← 不随按压缩放
            //                    + Highlight(0.55·2·rim) / InnerShadow / Shadow
            //                    + onDrawSurface = drawRect(containerColor)
            //    L2 隐形捕获层 : height = DropletHeight = **navHeight − 8dp**
            //                    + padding(horizontal = 10dp)
            //                    + 同一套 vibrancy/blur/lens 但 **H、A 都 ×p**
            //                    + highlight ×p；**无 shadow / innerShadow**
            //                    + onDrawSurface = drawRect(containerColor)
            //                    + 内含**同一份 tab 内容**（图标用 accent 色染）
            //    L3 水珠       : height = DropletHeight（**与 L2 等高**）
            //                    width  = tabWidth × 1.15 + 2dp
            //                    + lens(8.4dp·p, 11.8dp·p, chromatic)   // 10/14 × 47/56
            //                    + Highlight/Shadow/InnerShadow(8dp·p) 全部 ×p
            //                    + layerBlock { scaleX = damped.scaleX; … 速度修正 }
            //                      ← **挂 dampedDragAnimation.modifier ⇒ 缩放真的生效**
            //                    + onDrawSurface = 0.22 黑/白 ×(1−p) + 0.03 黑 ×p
            //    ⇒ 源码 L101-102 的恒等式：
            //       「水珠 = Layer 2 高度，且都小于 Layer 1，让水珠边缘恰好触及 Layer 2 边界」
            //
            //  ⚠ 本项目的两处**保留差异**（用户口径优先，不跟 BiliNext 改）：
            //    ① 指示器颜色 = 黑/白（MeloX 用户拍板「指示器是黑白，不是蓝色」），
            //       BiliNext 的 accent 蓝只染 L2 的 tab 图标。
            //    ② 选中 tab 图标用红色（MeloX 既有主题口径），非 BiliNext 的 accent 蓝。
            // ════════════════════════════════════════════════════════════════════════
            // ⚠ 这里**不能再读 `isMeloXDarkTheme()`** —— 必须接同一个 `LocalBottomBarTone`，
            //   否则在 d 处于中间态时，容器色 / 指示器色会与别层的进度脱节。
            val tone = bottomBarTone()
            val frostedGlass = MeloXSettingsRuntime.frostedGlassEnabled
            // BiliNext L131-133：containerColor = 0xFAFAFA/0x121212 @ 0.35
            val containerColor = lerp(
                Color(0xFFFAFAFA).copy(0.35f),
                Color(0xFF121212).copy(0.35f),
                tone,
            )
            // 指示器本体涂层（官方口径，黑白）
            val selectionTint = lerp(
                Color.Black.copy(0.1f),
                Color.White.copy(0.1f),
                tone,
            )

            BoxWithConstraints(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = navOffsetX, y = 0.dp)
                    .width(navWidth)
                    .height(navHeight),
                contentAlignment = Alignment.CenterStart,
            ) {
                val density = LocalDensity.current
                val panelDecoration = LocalMeloXPanelDecoration.current
                val currentActionTabs by rememberUpdatedState(LocalMeloXActionTabs.current)
                val tabCount = primaryTabs.size.coerceAtLeast(1)

                // BiliNext L152-156：tabWidth = (maxWidth − 20dp) / n（= 两侧 10dp padding）
                val meloxParameters = LocalNavigationPreset.current == GlassTabBarPreset.MeloX
                val edgePadding = if (meloxParameters) 10.dp else 4.dp
                val tabWidth = ((maxWidth - edgePadding * 2) / tabCount).coerceAtLeast(1.dp)
                val tabWidthPx = with(density) { tabWidth.toPx() }

                // ★ 核心恒等式：水珠与 Layer 2 **等高**，两者都比 Layer 1 矮 8dp
                //   （BiliNext：L1=55dp / L2=DropletHeight=47dp / L3=47dp）
                val dropletHeight = (navHeight - 8.dp).coerceAtLeast(1.dp)
                // ⚠ `dropletWidth` 必须夹取，`dropletHeight` 同理（上面已夹）。
                //   `navWidth` 是**不夹取的弹簧**（见 `lerpDpBouncy` 的注释），收缩/回弹过冲的
                //   某一帧会跌破 `compactSize`(48dp)；而 `tabWidth = (navWidth − 20dp)/tabCount`
                //   在 navWidth < 20dp 时变负 ⇒ `dropletWidth` 跟着变 0 / 负。
                //   水珠是 `drawBackdrop` 节点，0 或负尺寸会让 `GraphicsLayer.record()` 拿到
                //   空/非法尺寸并在 draw 阶段直接抛（不在任何 try/catch 内）。
                //   360dp 宽 / 固定 4 tab 过冲后仍为正所以不触发；
                //   **窄屏（320~340dp）、折叠屏外屏、开「显示大小」放大**的机型会。
                //   夹到 1.dp 只在退化帧生效，正常区间数值完全不变。
                // ★ 边缘 inset 约束：水珠左右必须离胶囊外缘 ≥ `capsuleEdgeInset`，
                //   否则 2-tab 服务 / 横屏（tabWidth 变大）时 `tabWidth*1.15+2` 会超出胶囊。
                //   几何（value=0 时 left = edgePadding + 0.5·tabWidth − width/2）：
                //     left ≥ capsuleEdgeInset ⇒ width ≤ tabWidth + 2·(edgePadding − capsuleEdgeInset)
                //   `coerceAtMost` 只在会溢出时收缩；正常 4-tab 竖屏不触发，观感零变化。
                //   capsuleEdgeInset=4dp 与上下 inset（(navHeight−dropletHeight)/2=4dp）严格一致。
                val capsuleEdgeInset = 4.dp
                val dropletWidth = (if (meloxParameters) tabWidth * 1.15f + 2.dp else tabWidth)
                    .coerceAtMost(tabWidth + (edgePadding - capsuleEdgeInset) * 2f)
                    .coerceAtLeast(1.dp)

                val selectedIndex = primaryTabs.indexOfFirst { it.first == selectedTab }
                val dockExpanded = progress < 0.56f
                val currentProgress by rememberUpdatedState(progress)
                val currentSelectedTab by rememberUpdatedState(selectedTab)
                val currentPrimaryTabs by rememberUpdatedState(primaryTabs)
                val currentTabWidthPx by rememberUpdatedState(tabWidthPx)
                val currentOnSelect by rememberUpdatedState(onSelect)
                // ⚠ **必须走 rememberUpdatedState**（2026-09-25 修 bug）。
                //   `dampedDock` 是 `remember(tabCount)` —— 只有 tab 数变化才会重建，
                //   所以它内部所有 lambda 会在**首次组合**时把当时的局部值**捕获死**。
                //   `selectedIndex` 若不是 State 包装，`onDragStopped` 里那句
                //   `if (target != selectedIndex)` 就永远在跟「App 启动时的选中下标」比：
                //   启动在首页(0) ⇒ 拖动指示器回首页时 `0 != 0` 为假 ⇒ **不触发切换**
                //   （真机症状：「拖到首页 tab 没反应，只有点击才行」）。
                val currentSelectedIndex by rememberUpdatedState(selectedIndex)
                // onTap 里要把「水珠局部坐标」换算回 tab 下标，需要水珠宽度（见 onTap 注释）。
                val currentDropletWidthPx by rememberUpdatedState(with(density) { dropletWidth.toPx() })

                // BiliNext L158-166 面板 recoil：拖动时整条面板轻微反向偏移
                val panelOffsetAnim = remember { Animatable(0f) }
                val panelOffset by remember(density) {
                    derivedStateOf {
                        val fraction = (panelOffsetAnim.value / constraints.maxWidth)
                            .coerceIn(-1f, 1f)
                        val sign = if (fraction >= 0f) 1f else -1f
                        with(density) {
                            4.dp.toPx() * sign * EaseOut.transform(abs(fraction))
                        }
                    }
                }

                val dampedDock = remember(tabCount) {
                    PublicDampedDragAnimation(
                        animationScope = dockScope,
                        initialValue = selectedIndex.coerceAtLeast(0).toFloat(),
                        valueRange = 0f..(tabCount - 1).toFloat(),
                        visibilityThreshold = 0.001f,
                        initialScale = 1f,
                        // BiliNext L185：pressedScale = 78/56
                        pressedScale = 78f / 56f,
                        onTap = { position ->
                            // ⚠ position.x 是**水珠局部**坐标（手势挂在 L3 水珠 Box 上，
                            //   宽仅 ~1.15 个 tab）。旧算法把它当面板坐标直接除 tabWidthPx
                            //   ⇒ 恒落在 0/1 ⇒「点水珠盖着的 tab 必跳首页」。
                            //   换算：水珠中心当前停在 value 格，落点 = value + 局部位移/tabWidth。
                            val index = (value +
                                (position.x - currentDropletWidthPx / 2f) /
                                currentTabWidthPx.coerceAtLeast(1f))
                                .roundToInt()
                                .coerceIn(0, tabCount - 1)
                            if (currentProgress < 0.56f) {
                                currentOnSelect(currentPrimaryTabs[index].first)
                            } else if (currentProgress >= 0.68f) {
                                currentOnSelect(currentSelectedTab)
                            }
                        },
                        onDragStopped = {
                            val target = targetValue.roundToInt().coerceIn(0, tabCount - 1)
                            val actionOnly = currentPrimaryTabs[target].first in currentActionTabs
                            animateToValue(if (actionOnly) currentSelectedIndex.coerceAtLeast(0).toFloat() else target.toFloat())
                            // ⚠ 用 `currentSelectedIndex`（rememberUpdatedState）而不是捕获死的
                            //   `selectedIndex` —— 见上面 currentSelectedIndex 的长注释。
                            if (actionOnly || target != currentSelectedIndex) {
                                currentOnSelect(currentPrimaryTabs[target].first)
                            }
                            dockScope.launch {
                                panelOffsetAnim.animateTo(
                                    0f,
                                    spring(dampingRatio = 1f, stiffness = 300f, visibilityThreshold = 0.5f),
                                )
                            }
                        },
                        onDrag = { _, dragAmount ->
                            updateValue(
                                (targetValue + dragAmount.x / currentTabWidthPx.coerceAtLeast(1f))
                                    .coerceIn(0f, (tabCount - 1).toFloat()),
                            )
                            dockScope.launch {
                                panelOffsetAnim.snapTo(panelOffsetAnim.value + dragAmount.x)
                            }
                        },
                    )
                }
                // 「呼出」判定：上一个选中是搜索 ⇒ 这次是从搜索返回，水珠要 Q 弹落格
                // （animateToValueBouncy 带过冲）；普通 tab 切换 / 拖拽回位仍走临界阻尼。
                var prevSelectedTab by remember { mutableStateOf(selectedTab) }
                LaunchedEffect(selectedIndex, dockExpanded, selectedTab) {
                    if (dockExpanded && selectedIndex >= 0) {
                        if (prevSelectedTab == AppTab.Search && selectedTab != AppTab.Search) {
                            dampedDock.animateToValueBouncy(selectedIndex.toFloat())
                        } else {
                            dampedDock.animateToValue(selectedIndex.toFloat())
                        }
                    }
                    prevSelectedTab = selectedTab
                }
                val dockHighlight = remember(dockScope, dampedDock) {
                    InteractiveHighlight(
                        animationScope = dockScope,
                        cancelOnConsumed = false,
                        position = { size, _ ->
                            // BiliNext：(value + 0.5) × tabWidthPx + panelOffset
                            Offset(
                                (dampedDock.value + 0.5f) * currentTabWidthPx + panelOffset,
                                size.height / 2f,
                            )
                        },
                    )
                }

                val tabsBackdrop = rememberLayerBackdrop()
                // BiliNext L245：L2 的 tab 缩放（按压时图标放大 1.2×，只作用于捕获层内容）
                val tabScale: () -> Float = { lerp(1f, 1.2f, dampedDock.pressProgress) }
                // BiliNext L130：`accentColor = if (isDarkTheme) TabAccentDark else TabAccentLight`。
                // MeloX 保留自己的口径 —— 强调色 = 主题红（与选中 tab / 收缩态圆心标一致）。
                val accentColor = LocalNavigationAccent.current ?: MeloXSystemColors.Red

                // ══ Layer 1：可见面板（BiliNext 原样）════════════════════════════
                // ⚠ **收缩态必须把 tab 行淡出**（2026-09-25 修 bug）：
                //   `navWidth` 会从展开 272dp 弹簧到 `compactSize` 48dp，而本 Row 里是
                //   `primaryTabs.forEach { RootTabButton(weight(1f)) }` —— 4 个 tab 在
                //   48dp − 20dp(padding) = 28dp 里各分 7dp，24dp 图标互相重叠、挤出容器，
                //   真机表现为**左侧一个糊成一团的畸形图标**（截图 tmp/mx_col2_bar.png）。
                //   ⇒ 用既有的 `expandedLayerAlpha`（progress 0.43→0.72 之间淡出）把
                //     **图标那层**隐去，收缩态改由下面的 compact 层渲染单个圆心标。
                //   ⚠ 淡出必须只作用在**内容**上，不能连 `meloXLiquidBottomBar` 一起淡 ——
                //     面板玻璃在收缩全程都得在（否则收缩中胶囊会闪一下消失）。
                Row(
                    modifier = Modifier
                        .graphicsLayer { translationX = panelOffset }
                        .meloXLiquidBottomBar(
                            shape = navShape,
                            tint = bottomLiquidGlassTint(),
                            surfaceColor = navigationSurface,
                            // 官方 LiquidBottomTabs L174-179：面板随水珠按压整体膨胀
                            // （scale = 1 + 16dp/width · p）。原先写死 0f ⇒ 长按完全没有反馈：
                            // 指示器有高光、底栏却纹丝不动，读起来像「按压没生效」。
                            pressProgress = dampedDock.pressProgress,
                            panelDecoration = panelDecoration,
                        )
                        .then(if (LocalNavigationLight.current == null) dockHighlight.modifier else Modifier)
                        .height(navHeight)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // 内容层：随收缩进度淡出（不含面板材质）
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { alpha = expandedLayerAlpha }
                            .padding(horizontal = edgePadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        primaryTabs.forEach { (tab, glyph) ->
                            RootTabButton(
                                tab = tab,
                                title = tab.titleFor(),
                                glyph = glyph,
                                // BiliNext L364：可见面板的图标**永远用未激活色**，
                                // 强调色只来自 L2 捕获层透出（`colorFilterTint = null` ⇒
                                // 这里的 `selected` 已无着色作用，仅用于关掉点击后的 shape 语义）。
                                selected = false,
                                semanticSelected = tab == selectedTab,
                                // ⚠ **毛玻璃模式的红色唯一来源**：毛玻璃下 L3 水珠整段不挂
                                //   （见下面 `if (indicatorAlpha > 0.001f && !frostedGlass)`），
                                //   L2 那张「全染强调色」的捕获层也就无从被采样 ⇒ 强调色彻底失去出口，
                                //   选中 tab 会退化成和未选中完全一样。⇒ 让 L1 自己上色。
                                //   与 `selected` 解耦的原因见 `RootTabButton` 的参数注释。
                                selectedTint = frostedGlass && tab == selectedTab,
                                labelAlpha = labelAlpha,
                                interactive = dockExpanded,
                                onClick = { onSelect(tab) },
                            )
                        }
                    }
                }

                // ══ 收缩态圆心标（MeloX 既有惯例，BiliNext 的「末位 tab 居中」等价物）════
                //   收缩后胶囊只剩 48dp 宽，4 个 tab 放不下 ⇒ 只留**当前选中 tab**的图标，
                //   居中、红色、25dp。`expandedLayerAlpha`/`compactLayerAlpha` 是一条交叉淡入
                //   淡出（0.43~0.72 淡出旧层 / 0.52~0.82 淡入新层），中间有重叠区，避免闪空。
                if (compactLayerAlpha > 0.001f) {
                    val compactTitle = selectedTab.titleFor()
                    // ⚠ 收缩态「当前选中」图标：选中搜索时搜索键是独立的右侧按钮，
                    //   左侧收缩胶囊没有搜索格可显示 ⇒ 默认回落首页房子图标（与下面
                    //   「选搜索时左侧指示器消失」同一个语义：搜索不算左侧 nav 的一员）。
                    //   选搜索时图标**不着红色** —— 红色是「左侧 nav 当前选中」的语义，
                    //   回落的房子只是占位，用未激活色（与右侧搜索键未选中态同口径）。
                    val compactGlyph =
                        if (selectedTab == AppTab.Search) RootGlyph.Home else selectedTab.rootGlyph()
                    // ⚠ 收缩态点击展开（2026-09-25 修 bug）：
                    //   展开的手势只挂在 L3 水珠上（dampedDock.modifier @ L1404），而水珠在收缩态
                    //   被定位到胶囊最左缘（(value+0.5)*tabWidth，tabWidth 仅 7dp），并不在用户看到的
                    //   居中红图标下方；L2 捕获层又 alpha(0f) 铺满整条并 onClick={} 拦截 ⇒
                    //   点中间的「左边按钮」命中不到展开手势，无法展开。
                    //   ⇒ 让收缩态这块居中红图标本身可点：zIndex 抬到最上层（盖过 L2/L3），
                    //      **仅在收缩态**（compactLayerAlpha>0.5f）挂 clickable 调 onSelect(selectedTab)
                    //     （onSelect 在调用方会把 tabBarMinimized=false ⇒ 展开）。
                    //   ⚠ 展开态不挂 clickable，否则会抢掉 L1 tab 的点击 / 水珠拖拽。
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { alpha = compactLayerAlpha }
                            .zIndex(2f)
                            .then(
                                if (compactLayerAlpha > 0.5f) {
                                    Modifier.clickable(role = Role.Tab) { onSelect(selectedTab) }
                                        .semantics {
                                            contentDescription = compactTitle
                                            selected = selectedTab != AppTab.Search
                                        }
                                } else {
                                    Modifier
                                },
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        RootGlyphIcon(
                            tab = if (selectedTab == AppTab.Search) visibleRootTabs.first() else selectedTab,
                            glyph = compactGlyph,
                            modifier = Modifier.size(25.dp),
                            color = if (selectedTab == AppTab.Search) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                LocalNavigationAccent.current ?: MeloXSystemColors.Red
                            },
                            selected = selectedTab != AppTab.Search,
                        )
                    }
                }

                // ══ Layer 2：隐形捕获层（BiliNext 原样，**一块真玻璃**）══════════
                CompositionLocalProvider(LocalGlassTabScale provides tabScale) {
                    Row(
                        modifier = Modifier
                            .clearAndSetSemantics { }
                            .alpha(0f)
                            .layerBackdrop(tabsBackdrop)
                            .graphicsLayer { translationX = panelOffset }
                            .meloXLiquidCaptureLayer(
                                shape = navShape,
                                surfaceColor = navigationSurface,
                                pressProgress = dampedDock.pressProgress,
                            )
                            .meloXPanelDecoration(navShape, panelDecoration, navHeight)
                            .then(if (LocalNavigationLight.current == null) dockHighlight.modifier else Modifier)
                            .height(dropletHeight)
                            .fillMaxWidth()
                            .padding(horizontal = edgePadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        primaryTabs.forEach { (tab, glyph) ->
                            RootTabButton(
                                tab = tab,
                                title = tab.titleFor(),
                                glyph = glyph,
                                // ⚠ 捕获层里 `selected` 必须传 false、改用 colorFilterTint ——
                                //   见 RootTabButton 里 colorFilterTint 的长注释。
                                //   传 true 会让「只有选中格有色」，水珠划过别的格就没有变色效果。
                                selected = false,
                                labelAlpha = labelAlpha,
                                interactive = false,
                                // kyant0 / BiliNext 的「划过才变色」核心：
                                //   整层 tab 全染强调色（层 alpha=0 看不见），
                                //   水珠一采样 ⇒ 只把它盖住那一格"照"出来。
                                colorFilterTint = accentColor,
                                onClick = {},
                            )
                        }
                    }
                }

                // ══ Layer 3：水珠指示器（BiliNext 原样，**缩放真的生效**）════════
                // BiliNext L444-448：alpha 走弹簧（不是二值开关），退出搜索时同步淡出
                // ⚠ 选中搜索键时左侧指示器消失：搜索不在 primaryTabs 里、没有对应水珠格，
                //   仍挂着会停在上一格导致「选中态错位」。所以选搜索时 target 直接归 0。
                val indicatorAlpha by animateFloatAsState(
                    targetValue = if (dockExpanded && selectedTab != AppTab.Search) 1f else 0f,
                    animationSpec = spring(stiffness = 180f, dampingRatio = 0.7f),
                    label = "melox-indicator-alpha",
                )
                if (indicatorAlpha > 0.001f && !frostedGlass) {
                    // ⚠ 原先这里建了一个 `rememberCombinedBackdrop(...)` 赋给 `indicatorBackdrop`
                    //   但**从未被使用**（真正生效的是下面 `meloXLiquidTabSelection` 内部按
                    //   `(LocalMeloXBackdrop, panelBackdrop = tabsBackdrop)` 自己建的那一枚）。
                    //   它白白多占一整张 GPU 图层，且因为挂在这个条件分支里，水珠每次显隐都会
                    //   重新分配/释放一次 —— 低端机上等于反复抖动一大块显存。
                    //   已删除；组合关系由 `meloXLiquidTabSelection` 内部负责。
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                // BiliNext L489-494：
                                //   tx = 10dp + (value + 0.5)·tabWidthPx − size.width/2 + panelOffset
                                translationX = with(density) { edgePadding.toPx() } +
                                    (dampedDock.value + 0.5f) * tabWidthPx -
                                    size.width / 2f +
                                    panelOffset
                                alpha = indicatorAlpha
                            }
                            .padding(horizontal = edgePadding)
                            .then(dockHighlight.gestureModifier)
                            .then(dampedDock.modifier)
                            .meloXLiquidTabSelection(
                                shape = navShape,
                                selected = true,
                                tint = selectionTint,
                                panelBackdrop = tabsBackdrop,
                                pressProgress = dampedDock.pressProgress,
                                // ★ BiliNext L534-541：缩放**照抄**，因为水珠挂了
                                //   dampedDock.modifier ⇒ press() 对整个子树生效，
                                //   scaleX/scaleY 会真的走到 pressedScale = 78/56。
                                layerBlock = {
                                    scaleX = dampedDock.scaleX
                                    scaleY = dampedDock.scaleY
                                    val velocity = dampedDock.velocity / 10f
                                    scaleX /= 1f - (velocity * 0.75f).coerceIn(-0.2f, 0.2f)
                                    scaleY *= 1f - (velocity * 0.25f).coerceIn(-0.2f, 0.2f)
                                },
                            )
                            .height(dropletHeight)
                            .width(dropletWidth),
                        contentAlignment = Alignment.Center,
                    ) {
                    }
                }
            }

            if (layout.showSearchButton || searchPresence > 0f) {
            val searchContentDescription = AppTab.Search.titleFor()
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = -horizontalMargin, y = 0.dp)
                    .size(searchSize)
                    .graphicsLayer {
                        alpha = searchPresence
                        scaleX = 0.72f + 0.28f * searchPresence
                        scaleY = scaleX
                        translationX = 20.dp.toPx() * (1f - searchPresence)
                    }
                    // 搜索键材质与底栏 nav 胶囊完全同源：同样走
                    // meloXLiquidBottomBar（vibrancy + blur + lens，且只叠
                    // surfaceColor、不额外加 Screen 白纱与 Hue tint），
                    // 避免观感与左侧底栏对不上。
                    .meloXLiquidBottomBar(
                        shape = navShape,
                        tint = bottomLiquidGlassTint(),
                        surfaceColor = navigationSurface,
                        refractionHeight = lerpDp(10.dp, 8.dp, bounceFrac),
                    )
                    .clickable(enabled = searchEnabled && layout.showSearchButton && searchPresence > 0.5f,
                        role = Role.Button) { onSelect(AppTab.Search) }
                    .semantics {
                        contentDescription = searchContentDescription
                    },
                contentAlignment = Alignment.Center,
            ) {
                RootGlyphIcon(
                    glyph = RootGlyph.Search,
                    modifier = Modifier.size(lerpDp(28.dp, 27.dp, bounceFrac)),
                    // ⚠ 搜索键的字形同样不能写死 —— 它读的是同一个被插值的 scheme。
                    color = if (selectedTab == AppTab.Search) {
                        LocalNavigationAccent.current ?: MeloXSystemColors.Red
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    selected = selectedTab == AppTab.Search,
                )
            }
            }
        }
    }
}

@Composable
private fun RowScope.RootTabButton(
    tab: AppTab,
    title: String,
    glyph: RootGlyph,
    selected: Boolean,
    semanticSelected: Boolean = selected,
    // ⚠ **只上强调色，不动字形/语义** —— 专给「毛玻璃模式下没有水珠指示器」用。
    //   为什么不直接复用 `selected`：`selected` 是**三件事捆在一起**的开关 ——
    //     ① `desiredForeground` 的强调红；② `RootGlyphIcon` 的 Fill/Regular 字形；
    //     ③ 语义 `selected`。
    //   而液态模式透过水珠看到的那张图来自 **L2 捕获层**，那里是恒 `selected = false`
    //   ⇒ 折射出来的是 **Regular 轮廓 + 强调红 + SemiBold 标签**。
    //   若毛玻璃模式直接传 `selected = true`，字形会跳成 Fill ⇒ 同一个 tab 在两种
    //   模式下的**图标形状**都对不上（「毛玻璃模式的房子是实心的，玻璃模式是空心的」）。
    //   ⇒ 拆出这个参数，让毛玻璃模式与液态模式的观感逐项相等。
    selectedTint: Boolean = false,
    labelAlpha: Float,
    // 原先这里有个 `dark: Boolean` 且完全没人用。改成「整棵子树插值配色」后，
    // 前景直接读 `colorScheme.onSurface` 就会跟着动 ⇒ 该参数连调用点一并删掉。
    interactive: Boolean = true,
    // ⚠ **「指示器划过才变色」的全部机关就在这一个参数**（BiliNext `GlassTabItem.colorFilterTint`）：
    //   非 null ⇒ 该 tab 无条件用这个颜色（`selected` 被无视），用于 **L2 隐形捕获层**，
    //   把整层 tab 都染成强调色；层本身 `alpha(0f)` 看不见，**只有水珠采样到的那一格会被"照"出来**
    //   ⇒ 水珠移到哪，哪格就亮成强调色 —— 不需要任何逐 tab 动画。
    //   L1 可见面板传 null ⇒ `selected` 生效（MeloX 保留红色口径）。
    colorFilterTint: Color? = null,
    onClick: () -> Unit,
) {
    val desiredForeground = when {
        colorFilterTint != null -> colorFilterTint
        // `selectedTint`（毛玻璃模式的强调色）与 `selected` 同色、同动画路径。
        selected || selectedTint -> LocalNavigationAccent.current ?: MeloXSystemColors.Red
        // 回到 `colorScheme.onSurface` —— 外层 `BottomBarToneTheme` 已经把整套配色
        // 插值过，这个读值自己就会跟着动，**不需要**再在这里写端点。
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
    }
    val foreground by animateColorAsState(
        targetValue = desiredForeground,
        animationSpec = spring(dampingRatio = 0.84f, stiffness = 480f),
        label = "melox-tab-foreground",
    )
    // BiliNext `GlassTabItem(scaleOverride = tabScale)` 的等价物：
    // Layer 2 捕获层里的 tab 内容随按压放大（1 → 1.2×），只影响**被水珠折射的那张图**。
    // 可见面板（Layer 1）不提供 LocalGlassTabScale ⇒ 默认恒 1f，不受影响。
    val tabScale = LocalGlassTabScale.current()
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .graphicsLayer {
                scaleX = tabScale
                scaleY = tabScale
            }
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .then(
                if (interactive) Modifier
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        role = Role.Tab,
                        onClick = onClick,
                    )
                    .semantics {
                        contentDescription = title
                        role = Role.Tab
                        this.selected = semanticSelected
                    }
                else Modifier.clearAndSetSemantics { },
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val customContent = LocalMeloXTabContents.current[tab]
        if (customContent != null) {
            customContent(foreground, semanticSelected)
            return@Column
        }
        RootGlyphIcon(
            tab = tab,
            glyph = glyph,
            modifier = Modifier.size(24.dp),
            color = foreground,
            selected = selected,
        )
        if (LocalMeloXShowLabels.current) Text(
            text = title,
            modifier = Modifier.graphicsLayer { alpha = labelAlpha },
            fontSize = 11.sp,
            lineHeight = 13.sp,
            // BiliNext L704：`colorFilterTint != null` 也算「强调」→ SemiBold
            // ⚠ `selectedTint` 也算：液态模式下透过水珠看到的标签就是 SemiBold
            //   （L2 恒 `colorFilterTint != null`）⇒ 毛玻璃模式必须同字重，否则
            //   「切到毛玻璃标签变细」。
            fontWeight = if (selected || selectedTint || colorFilterTint != null) {
                FontWeight.SemiBold
            } else {
                FontWeight.Medium
            },
            color = foreground,
        )
    }
}

/** Native artwork participates in the same clipped refraction capture as tab content. */
private fun Modifier.meloXPanelDecoration(
    shape: Shape, decoration: MeloXPanelDecoration?, panelHeight: Dp,
): Modifier = if (decoration == null) this else drawWithCache {
    val path = Path().apply { addOutline(shape.createOutline(size, layoutDirection, this@drawWithCache)) }
    val overflow = (panelHeight.toPx() - size.height) / 2f
    onDrawWithContent {
        clipPath(path) { inset(vertical = -overflow) { decoration() } }
        drawContent()
    }
}

private enum class RootGlyph { Home, Explore, Library, Podcasts, Downloads, Cloud, Settings, Search }

@Composable
@Suppress("UNUSED_PARAMETER")
private fun AppTab.titleFor(): String = LocalMeloXTabLabels.current[this]
    ?: if (titleRes != 0) stringResource(titleRes) else name.removePrefix("custom:")

private fun AppTab.rootGlyph(): RootGlyph = when (this) {
    AppTab.Home -> RootGlyph.Home
    AppTab.Explore -> RootGlyph.Explore
    AppTab.Library -> RootGlyph.Library
    AppTab.Podcasts -> RootGlyph.Podcasts
    AppTab.Downloads -> RootGlyph.Downloads
    AppTab.Cloud -> RootGlyph.Cloud
    AppTab.Settings -> RootGlyph.Settings
    AppTab.Search -> RootGlyph.Search
    else -> RootGlyph.Home
}

@Composable
fun MeloXNavigationIcon(tab: AppTab, modifier: Modifier, color: Color, selected: Boolean = false) {
    RootGlyphIcon(tab = tab, glyph = tab.rootGlyph(), modifier = modifier, color = color, selected = selected)
}

@Composable
private fun RootGlyphIcon(
    tab: AppTab? = null,
    glyph: RootGlyph,
    modifier: Modifier,
    color: Color,
    selected: Boolean = false,
) {
    LocalMeloXTabIcons.current[tab ?: AppTab.valueOf(glyph.name)]?.let { customIcon ->
        customIcon(modifier, color, selected)
        return
    }
    if (tab != null && tab.titleRes == 0) {
        Text(tab.titleFor().take(1), modifier = modifier, color = color)
        return
    }
    // The home tab uses the custom house vector (traced from the user's image)
    // instead of the tinted symbol glyph, so tint applies to it like the others.
    if (glyph == RootGlyph.Home) {
        Icon(
            painter = painterResource(R.drawable.melox_home_tab),
            contentDescription = null,
            modifier = modifier,
            tint = color,
        )
        return
    }
    MeloXSymbolIcon(
        symbol = when (glyph) {
            RootGlyph.Home -> MeloXSymbol.Home
            RootGlyph.Explore -> MeloXSymbol.Explore
            RootGlyph.Library -> MeloXSymbol.Library
            RootGlyph.Podcasts -> MeloXSymbol.RadioWaves
            RootGlyph.Downloads -> MeloXSymbol.Download
            RootGlyph.Cloud -> MeloXSymbol.Storage
            RootGlyph.Settings -> MeloXSymbol.Settings
            RootGlyph.Search -> MeloXSymbol.Search
        },
        modifier = modifier,
        color = color,
        variant = if (selected) MeloXSymbolVariant.Fill else MeloXSymbolVariant.Regular,
    )
}

private fun smoothStep(value: Float, start: Float, end: Float): Float {
    if (end <= start) return if (value >= end) 1f else 0f
    val t = ((value - start) / (end - start)).coerceIn(0f, 1f)
    return t * t * (3f - 2f * t)
}

private fun lerpDp(start: Dp, end: Dp, progress: Float): Dp =
    (start.value + (end.value - start.value) * progress.coerceIn(0f, 1f)).dp

/**
 * 与 [lerpDp] 相同，但**不夹** fraction —— 专供带过冲的几何。
 *
 * ⚠ 必须用这个版本，否则弹簧的过冲会在最后一步被 [lerpDp] 吃掉，Q 弹永远看不见。
 * fraction 由 [sprungFrac] 产出，已限幅，不会把布局撑爆；但**外扩方向**是否安全
 * 取决于该几何周围有没有邻居 —— 现在这件事交给**弹簧参数自己**，不再做事后压缩：
 * nav 展开方向的越界量由 `MeloXSprings.BottomBarExpandBounce` 定死在 6.02dp
 * （右预算 4dp + 左侧让出 2dp），再按 `NavExpandLeftShare` 落到两端；
 * 收缩方向由 0.30 那条管着（10.30dp，四周没有邻居）。
 */
private fun lerpDpBouncy(start: Dp, end: Dp, fraction: Float): Dp =
    (start.value + (end.value - start.value) * fraction).dp

/**
 * BiliNext `LiquidGlassTabsBar.kt` L99 的等价物：让 Layer 2（隐形捕获层）里的
 * tab 内容在按压时整体放大（`lerp(1f, 1.2f, pressProgress)`）。
 *
 * ⚠ 它**只影响捕获层的背板内容**（= 被水珠折射的那张图），不影响可见面板 ——
 * 所以「按压时图标放大」这件事在本项目只表现为折射内容的呼吸感，1:1 复刻 BiliNext。
 */
private val LocalGlassTabScale: ProvidableCompositionLocal<() -> Float> =
    staticCompositionLocalOf { { 1f } }
