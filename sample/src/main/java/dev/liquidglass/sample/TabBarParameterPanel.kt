package dev.liquidglass.sample

import dev.liquidglass.*

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.liquidglass.GlassTabBarLayout
import dev.liquidglass.GlassTabBarPreset
import com.zeaze.tianyinwallpaper.backdrop.backdrops.rememberCanvasBackdrop
import dev.liquidglass.ios27.*
import java.io.Serializable
import kotlin.math.roundToInt

/** Page-local settings; defaults preserve the existing advanced tab bar example. */
@Immutable
internal data class TabBarDemoOptions(
    val compact: Boolean = false,
    val scrollMinimize: Boolean = true,
    val expandOnSelect: Boolean = true,
    val frosted: Boolean = false,
    val automaticTone: Boolean = true,
    val media: Boolean = true,
    val showSearchButton: Boolean = true,
    val searchEnabled: Boolean = true,
    val avoidNavigationBar: Boolean = true,
    val showIcons: Boolean = true,
    val showLabels: Boolean = true,
    val originalFont: Boolean = true,
    val customLayout: Boolean = false,
    val presetChoice: Int = 0,
    val standardChoice: Int = 0,
    val themeChoice: Int = 0,
    val accentChoice: Int = 0,
    val tabCount: Int = 4,
    val height: Float = 57f,
    val horizontalMargin: Float = 12f,
    val bottomMargin: Float = 8f,
    val searchSpacing: Float = 7f,
    val labels: List<String> = listOf("首页", "发现", "资料库", "设置"),
) : Serializable {
    fun layout(preset: GlassTabBarPreset) = GlassTabBarLayout(
        compact = compact, showSearchButton = showSearchButton, avoidNavigationBar = avoidNavigationBar,
        height = (if (customLayout) height else if (preset == GlassTabBarPreset.MeloX) 57f else 62f).dp,
        horizontalMargin = (if (customLayout) horizontalMargin else if (avoidNavigationBar) 12f else 0f).dp,
        bottomMargin = (if (customLayout) bottomMargin else if (avoidNavigationBar) 8f else 0f).dp,
        searchSpacing = (if (customLayout) searchSpacing else if (preset == GlassTabBarPreset.MeloX) 7f else 16f).dp,
    )
}

private data class TabBarSwitch(val label: String, val checked: Boolean, val change: (Boolean) -> Unit)

@Composable
internal fun TabBarParameterPanel(
    options: TabBarDemoOptions,
    onChange: (TabBarDemoOptions) -> Unit,
    minimized: Boolean,
    onMinimizedChange: (Boolean) -> Unit,
    searchSelected: Boolean,
    onSearchSelectedChange: (Boolean) -> Unit,
    onReset: () -> Unit,
) {
    val colors = LocalIos27PageColors.current
    val controlBackdrop = rememberCanvasBackdrop { drawRect(colors.group) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Ios27ListSection(title = "功能", footer = "修改只影响本页预览。开关使用液态玻璃组件。") {
            val switches = listOf(
                TabBarSwitch("紧凑模式", options.compact) { onChange(options.copy(compact = it)) },
                TabBarSwitch("收缩标签栏", minimized, onMinimizedChange),
                TabBarSwitch("滚动时自动收缩", options.scrollMinimize) { onChange(options.copy(scrollMinimize = it)) },
                TabBarSwitch("选择后自动展开", options.expandOnSelect) { onChange(options.copy(expandOnSelect = it)) },
                TabBarSwitch("毛玻璃模式", options.frosted) { onChange(options.copy(frosted = it)) },
                TabBarSwitch("自动明暗采样", options.automaticTone) { onChange(options.copy(automaticTone = it)) },
                TabBarSwitch("显示播放器", options.media) { onChange(options.copy(media = it)) },
                TabBarSwitch("显示搜索按钮", options.showSearchButton) { onChange(options.copy(showSearchButton = it)) },
                TabBarSwitch("允许点击搜索", options.searchEnabled) { onChange(options.copy(searchEnabled = it)) },
                TabBarSwitch("避让系统导航区", options.avoidNavigationBar) { onChange(options.copy(avoidNavigationBar = it)) },
                TabBarSwitch("显示图标", options.showIcons) { onChange(options.copy(showIcons = it)) },
                TabBarSwitch("显示标签文字", options.showLabels) { onChange(options.copy(showLabels = it)) },
                TabBarSwitch("MeloX 字体", options.originalFont) { onChange(options.copy(originalFont = it)) },
            ) + if (options.showSearchButton) listOf(TabBarSwitch("选中搜索", searchSelected, onSearchSelectedChange)) else emptyList()
            Ios27ListRows(switches) { item ->
                Ios27ListRow(item.label, trailing = {
                    GlassSwitch(item.checked, item.change, backdrop = controlBackdrop,
                        modifier = Modifier.semantics { contentDescription = item.label })
                })
            }
        }
        Ios27ListSection(title = "样式", footer = "点击选项切换。玻璃标准与材质预设默认跟随设置。") {
            Ios27ListRows(listOf(0, 1, 2, 3)) { index ->
                when (index) {
                    0 -> Ios27ListRow("玻璃标准", detail = listOf("跟随设置", "iOS 26", "iOS 27")[options.standardChoice],
                        onClick = { onChange(options.copy(standardChoice = (options.standardChoice + 1) % 3)) })
                    1 -> Ios27ListRow("材质预设", detail = listOf("跟随设置", "MeloX", "通用")[options.presetChoice],
                        onClick = { onChange(options.copy(presetChoice = (options.presetChoice + 1) % 3)) })
                    2 -> Ios27ListRow("标签栏主题", detail = listOf("跟随页面", "浅色", "深色")[options.themeChoice],
                        onClick = { onChange(options.copy(themeChoice = (options.themeChoice + 1) % 3)) })
                    else -> Ios27ListRow("选中颜色", detail = listOf("默认", "红色", "绿色", "橙色")[options.accentChoice],
                        onClick = { onChange(options.copy(accentChoice = (options.accentChoice + 1) % 4)) })
                }
            }
        }
        Ios27ListSection(title = "布局", footer = "关闭自定义尺寸后恢复当前材质预设的布局。") {
            Ios27ListRow("自定义尺寸", trailing = {
                GlassSwitch(options.customLayout, { onChange(options.copy(customLayout = it)) },
                    backdrop = controlBackdrop, modifier = Modifier.semantics { contentDescription = "自定义尺寸" })
            })
            if (options.customLayout) {
                TabBarDimension("展开高度", options.height, 48f..80f) { onChange(options.copy(height = it)) }
                TabBarDimension("水平外边距", options.horizontalMargin, 0f..32f) { onChange(options.copy(horizontalMargin = it)) }
                TabBarDimension("底部外边距", options.bottomMargin, 0f..32f) { onChange(options.copy(bottomMargin = it)) }
                TabBarDimension("搜索按钮间距", options.searchSpacing, 0f..32f) { onChange(options.copy(searchSpacing = it)) }
            }
        }
        Ios27ListSection(title = "标签内容") {
            Ios27StepperRow("标签数量", options.tabCount, { onChange(options.copy(tabCount = it)) }, 2..4)
            options.labels.take(options.tabCount).forEachIndexed { index, label ->
                Ios27TextFieldRow("标签 ${index + 1}", label, { text ->
                    onChange(options.copy(labels = options.labels.mapIndexed { i, old -> if (i == index) text else old }))
                })
            }
        }
        GlassButton(onReset, controlBackdrop, Modifier.fillMaxWidth()) { Text("恢复默认参数", color = colors.label) }
        Text("向下滚动查看标签栏在不同背景上的效果。", color = colors.secondaryLabel)
    }
}

@Composable
private fun TabBarDimension(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onChange: (Float) -> Unit) {
    val colors = LocalIos27PageColors.current
    val backdrop = rememberCanvasBackdrop { drawRect(colors.group) }
    Column {
        Text("$label · ${value.roundToInt()} dp", Modifier.padding(start = 16.dp, top = 10.dp),
            color = colors.label, style = Ios27Tokens.Type.Footnote)
        Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).heightIn(min = Ios27Tokens.RowHeight.dp),
            contentAlignment = Alignment.Center) {
            GlassSlider((value - range.start) / (range.endInclusive - range.start),
                { onChange((range.start + it * (range.endInclusive - range.start)).roundToInt().toFloat()) },
                backdrop = backdrop, modifier = Modifier.semantics { contentDescription = label })
        }
    }
}
