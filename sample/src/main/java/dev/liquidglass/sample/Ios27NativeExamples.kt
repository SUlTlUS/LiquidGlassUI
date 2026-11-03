package dev.liquidglass.sample

import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import dev.liquidglass.ios27.Ios27ListSection
import dev.liquidglass.ios27.Ios27PageContent
import dev.liquidglass.ios27.Ios27PageHeader
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.*
import dev.liquidglass.GlassStandard
import com.zeaze.tianyinwallpaper.backdrop.backdrops.LayerBackdrop

/** Host-owned examples: Sketch owns pages and navigation; the new library owns mechanisms. */
internal enum class Ios27MechanismExample(val key: String, val title: String, val hint: String) {
    Menu("ios27-menu", "多级菜单", "从按钮展开菜单，选择子菜单或长列表中的项目。"),
    Popup("ios27-popup", "Popup", "从来源按钮展开内容，填写后保存，结果返回本页。"),
    Theme("ios27-theme", "主题与图标", "配色跟随应用；切换色彩风格，点击图标查看填充动画。"),
}

@RequiresApi(33)
@Composable
internal fun Ios27NativeMechanismPage(
    example: Ios27MechanismExample,
    onBack: () -> Unit,
    selectedStandard: GlassStandard,
) {
    val ios26Menu = currentGlassMaterial() == GlassStandard.Ios26
    val standard = selectedStandard.label
    val colors = LocalIos27PageColors.current
    val dark = !MaterialTheme.colors.isLight
    val backdrop = rememberExamplePageBackdrop(colors.page)
    val previewBackdrop = rememberExamplePageBackdrop(colors.page)
    var result by rememberSaveable { mutableStateOf("尚未操作") }
    var count by rememberSaveable { mutableIntStateOf(0) }
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var styleIndex by rememberSaveable { mutableIntStateOf(0) }
    var longMenu by rememberSaveable { mutableStateOf(false) }
    var menuOpen by remember { mutableStateOf(false) }
    var menuMounted by remember { mutableStateOf(false) }
    var menuAnchor by remember { mutableStateOf(Rect.Zero) }
    var draft by rememberSaveable { mutableStateOf("") }
    var saved by rememberSaveable { mutableStateOf("") }
    var modal by rememberSaveable { mutableStateOf(true) }
    val popup = rememberGlassPopupState<String>()
    val popupAnchor = rememberGlassPopupAnchor()
    val modalVisible = menuMounted || (popup.mounted && modal)
    BackHandler(!menuMounted && !popup.mounted, onBack)

    GlassTheme(darkTheme = dark, accentColor = colors.accent, colorStyle = GlassColorStyle.entries[styleIndex]) {
        GlassBackdropProvider(backdrop) {
            // Full-window overlay coordinates match the menu / popup source anchors.
            Box(Modifier.fillMaxSize()) {
                ExampleRecordedResponsivePage(
                    background = colors.page,
                    backdrop = backdrop,
                    modifier = Modifier.semantics { paneTitle = "$standard · ${example.title}" }
                        .then(if (modalVisible) Modifier.clearAndSetSemantics {} else Modifier),
                    content = { topInset, _ ->
                        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                            .padding(top = topInset + 56.dp, bottom = 24.dp)) {
                            Ios27PageContent {
                                Ios27PageHeader(example.title, "$standard · ${example.hint}")
                                Ios27ExamplePreview(previewBackdrop) {
                                    when (example) {
                                        Ios27MechanismExample.Menu -> {
                                            Ios27DemoButton(if (longMenu) "切换为多级菜单" else "切换为长列表", dark,
                                                ios26Backdrop = previewBackdrop.takeIf { ios26Menu },
                                                onClick = { longMenu = !longMenu })
                                            Ios27DemoButton("打开菜单", dark,
                                                ios26Backdrop = previewBackdrop.takeIf { ios26Menu },
                                                modifier = Modifier.onGloballyPositioned { menuAnchor = it.boundsInRoot() }
                                                    .graphicsLayer { alpha = if (menuMounted) 0f else 1f },
                                                onClick = { menuMounted = true; menuOpen = true })
                                        }
                                        Ios27MechanismExample.Popup -> {
                                            Ios27DemoButton(if (modal) "模式：模态" else "模式：非模态", dark,
                                                onClick = { modal = !modal })
                                            GlassButtonGroup(Modifier.fillMaxWidth()) {
                                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                            GlassPopupBox(popup, popupAnchor, Modifier.weight(1f)) { open ->
                                                GlassButton(backdrop = previewBackdrop,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    height = 52.dp, contentPadding = PaddingValues(0.dp),
                                                    onClick = {
                                                        draft = saved
                                                        open()
                                                    }) {
                                                    GlassPopupSourceContent(popup, Modifier.fillMaxWidth().padding(16.dp)) {
                                                        Text("编辑备注", color = colors.label, style = Ios27Tokens.Type.Body)
                                                    }
                                                }
                                            }
                                            GlassButton(backdrop = previewBackdrop, height = 52.dp,
                                                contentPadding = PaddingValues(0.dp),
                                                modifier = Modifier.size(52.dp).semantics { contentDescription = "增加计数" },
                                                onClick = { count++; result = "页面计数：$count" }) {
                                                GlassGlyphIcon(GlassGlyphs.add, colors.label, Modifier.size(24.dp))
                                            }
                                            }
                                            }
                                            Ios27DemoButton("页面计数 · $count", dark, onClick = {
                                                count++; result = "页面计数：$count"
                                            })
                                        }
                                        Ios27MechanismExample.Theme -> {
                                            Ios27DemoButton("色彩风格：${GlassColorStyle.entries[styleIndex].name}", dark,
                                                tinted = true, onClick = {
                                                    styleIndex = (styleIndex + 1) % GlassColorStyle.entries.size
                                                    result = "色彩风格：${GlassColorStyle.entries[styleIndex].name}"
                                                })
                                        }
                                    }
                                }
                                Ios27ListSection(title = "操作结果") {
                                    Text(result, Modifier.fillMaxWidth().padding(16.dp)
                                        .semantics { liveRegion = LiveRegionMode.Polite },
                                        color = colors.label, style = Ios27Tokens.Type.Body)
                                }
                                Ios27ListSection(title = "试一试") {
                                    ExampleBody(when (example) {
                                        Ios27MechanismExample.Menu -> "进入排序子菜单，再进入高级排序；切换到长列表并选择靠后的项目；按返回键关闭。"
                                        Ios27MechanismExample.Popup -> "保存与取消应有不同结果；切换非模态后可操作页面计数。"
                                        Ios27MechanismExample.Theme -> "浅深色沿用应用设置；图标选中后同时显示文字状态。"
                                    })
                                }
                                if (example == Ios27MechanismExample.Theme) Ios27ExampleGlyphs { result = it }
                            }
                        }
                    },
                    overlay = { topInset, pageBackdrop ->
                        ExampleTopChrome(pageBackdrop, isLightTheme = !dark, appBarHeight = topInset + 48.dp)
                        Ios27BackButton(onBack, pageBackdrop,
                            Modifier.align(Alignment.TopStart).padding(start = 16.dp, top = topInset + 4.dp))
                    },
                )
                // One-way sampling: overlays -> page -> controls -> independent preview background.
                if (example == Ios27MechanismExample.Menu) GlassMorphMenu(
                    highlightStyle = currentGlassHighlight(),
                    expanded = menuOpen, anchor = menuAnchor, dark = dark,
                    onDismiss = { menuOpen = false }, onReturnComplete = { menuMounted = false },
                    maxVisibleItems = if (longMenu) 7 else null,
                    sourceSurface = if (ios26Menu) ({
                        // Same independent preview backdrop and original button material as the source.
                        Ios27DemoButton("打开菜单", dark, ios26Backdrop = previewBackdrop, onClick = {})
                    }) else null,
                    sourceContent = { Text("打开菜单", color = colors.label, style = Ios27Tokens.Type.Body) },
                    actions = if (longMenu) List(40) { index ->
                        GlassMenuAction("示例 ${index + 1}", selected = selected == index,
                            onClick = { selected = index; result = "已选择示例 ${index + 1}" })
                    } else listOf(
                        GlassMenuAction("收藏示例", vectorGlyph = GlassGlyphs.add, onClick = { result = "已收藏示例" }),
                        GlassMenuAction("排序", vectorGlyph = GlassGlyphs.swap, subActions = listOf(
                            GlassMenuAction("按名称", selected = selected == 0, onClick = { selected = 0; result = "排序：按名称" }),
                            GlassMenuAction("按最近使用", selected = selected == 1, onClick = { selected = 1; result = "排序：按最近使用" }),
                            GlassMenuAction("高级排序", subActions = listOf(
                                GlassMenuAction("升序", selected = selected == 2, onClick = { selected = 2; result = "高级排序：升序" }),
                                GlassMenuAction("降序", selected = selected == 3, onClick = { selected = 3; result = "高级排序：降序" }),
                            )),
                        )),
                        GlassMenuAction("暂不可用", subtitle = "没有动作时保持禁用"),
                    ),
                )
                if (example == Ios27MechanismExample.Popup) GlassMenuMaterialScope {
                GlassPopup(
                    popup, popupAnchor, dark, properties = GlassPopupProperties(modal = modal),
                    onResult = { value ->
                        if (value != null) { saved = value; result = "备注已保存：$value" }
                        else result = "已取消，备注未更改"
                    },
                    sourceSurface = if (ios26Menu) ({
                        GlassButton(onClick = {}, backdrop = previewBackdrop,
                            modifier = Modifier.fillMaxWidth(), height = 52.dp,
                            contentPadding = PaddingValues(0.dp)) {
                            Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                Text("编辑备注", color = colors.label, style = Ios27Tokens.Type.Body)
                            }
                        }
                    }) else null,
                    sourceContent = { Text("编辑备注", color = colors.label) },
                ) { dismiss ->
                    Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("示例备注", color = colors.label, style = Ios27Tokens.Type.Headline)
                        Text("备注内容", color = colors.label)
                        GlassDialogTextField(draft, { draft = it }, dark, placeholder = "输入备注")
                        Ios27DemoButton("保存备注", dark, onClick = { dismiss(draft) })
                        Ios27DemoButton("取消", dark, onClick = { dismiss(null) })
                    }
                }
                }
            }
        }
    }
}

/** Inline controls never read the parent page recording that contains them. */
@RequiresApi(33)
@Composable
private fun Ios27ExamplePreview(backdrop: LayerBackdrop, content: @Composable ColumnScope.() -> Unit) {
    val colors = LocalIos27PageColors.current
    Box(Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        ExampleBackdropLayer(backdrop, colors.page, Modifier.matchParentSize()) { Ios27ExampleBackdrop() }
        GlassBackdropProvider(backdrop) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp), content = content)
        }
    }
}

/** Non-glass sibling source. Token colors provide actual edges for refraction. */
@Composable
private fun Ios27ExampleBackdrop() {
    val colors = LocalIos27PageColors.current
    Canvas(Modifier.fillMaxSize()) {
        val band = size.width / 6f
        for (index in 1..4) drawRect(
            color = if (index % 2 == 0) colors.group else colors.accent.copy(alpha = .06f),
            topLeft = Offset(index * band, 0f), size = Size(band / 2f, size.height),
        )
    }
}

@RequiresApi(33)
@Composable
private fun Ios27DemoButton(label: String, dark: Boolean, modifier: Modifier = Modifier,
    tinted: Boolean = false, ios26Backdrop: com.zeaze.tianyinwallpaper.backdrop.Backdrop? = null,
    onClick: () -> Unit) {
    val scheme = androidx.compose.material3.MaterialTheme.colorScheme
    GlassButton(
        backdrop = ios26Backdrop ?: requireNotNull(currentGlassBackdrop()),
        modifier = modifier.fillMaxWidth(), height = 52.dp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        tint = if (tinted) scheme.primary else androidx.compose.ui.graphics.Color.Unspecified,
        onClick = onClick,
    ) {
        Text(label,
            color = if (tinted) scheme.onPrimary else LocalIos27PageColors.current.label,
            style = Ios27Tokens.Type.Body)
    }
}

@Composable
private fun ExampleBody(text: String) {
    Text(text, Modifier.fillMaxWidth().padding(16.dp), color = LocalIos27PageColors.current.label,
        style = Ios27Tokens.Type.Body)
}

@RequiresApi(33)
@Composable
private fun Ios27ExampleGlyphs(onSelected: (String) -> Unit) {
    val colors = LocalIos27PageColors.current
    var selectedName by rememberSaveable { mutableStateOf("") }
    Ios27ListSection(title = "${GlassGlyphs.all.size} 个矢量图标") {
        GlassGlyphs.all.entries.toList().chunked(3).forEach { row ->
            Row(Modifier.fillMaxWidth()) {
                row.forEach { (name, glyph) ->
                    val filled = selectedName == name
                    Column(Modifier.weight(1f).heightIn(min = 80.dp)
                        .clickable(role = Role.Button) {
                            selectedName = if (filled) "" else name
                            onSelected(if (filled) "已取消选择 $name" else "已选择图标 $name")
                        }.semantics { selected = filled; contentDescription = name }
                        .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        AnimatedGlassGlyph(glyph, filled, colors.accent, Modifier.size(28.dp))
                        Text(name, color = colors.label, style = Ios27Tokens.Type.Caption2)
                    }
                }
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}
