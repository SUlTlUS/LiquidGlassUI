# Inline 与 Popup 液态玻璃接入方法

适用于本项目日期与时间 Inline、Color Picker Popup、Menus / Context Menu、底部工具栏和需要透出背景图片的新页面。先复用现有玻璃实现，再配置宿主采样层。不要重写玻璃着色器。

背景图片选择属于宿主设置页操作，不属于 Toolbar 玻璃组件本身。设置页统一复用 `rememberPopupBackground`，图片绘制在各示例所读取的页面 `LayerBackdrop` 内；示例页面不再分散放置选择/移除按钮，工具栏仍保持独立的 `LiquidButton` 玻璃按钮和原稿几何。

## 先找正确入口

| 文件（相对项目根目录） | 用途 |
| --- | --- |
| `sample/src/main/java/dev/liquidglass/sample/NativeExamplePage.kt` | 背景录制层、页面布局和玻璃参数传递 |
| `sample/src/main/java/dev/liquidglass/sample/PopupBackground.kt` | 系统选图、URI 保存、异步限尺寸解码、居中裁切绘制 |
| `sample/src/main/java/dev/liquidglass/sample/Ios27PickerExample.kt` | Inline 日历，接收 `background: Backdrop`；Compact 为实色分组 |
| `sample/src/main/java/dev/liquidglass/sample/Ios27ColorPickerDialog.kt` | Color Picker，接收宿主 `backdrop`，作为页面兄弟覆盖层 |
| `liquidglass/src/main/java/com/zeaze/tianyinwallpaper/backdrop/backdrops/LayerBackdrop.kt` | 已有窗口坐标映射实现，默认只读 |
| `docs/color-picker-material.md` | 当前材质参数及其来源 |

这些 sample 接口是 internal；用于新 app 时连同宿主适配代码移植，不能仅添加玻璃库依赖就假设接口公开可用。

## 背景与采样必须共享坐标

Inline 正确层级：

```text
页面 Box
├─ 全页背景 Box → 录制到 pickerBackgroundBackdrop
│  └─ 页面底色 + 居中裁切的图片
└─ 页面内容（可以滚动）
   ├─ 选图按钮
   └─ Inline 日历 → 读取 pickerBackgroundBackdrop
```

背景层不含日历。玻璃库通过源层和读取层的 LayoutCoordinates 计算偏移，日历读取其所在位置下方的内容。图片固定在页面背景，日历滚动时采样位置跟随日历变化。

本项目的最小接法如下，放在现有主题和设计密度作用域中。省略的日历业务参数按现有调用补齐：

```kotlin
val source = rememberLayerBackdrop()
Box(Modifier.fillMaxSize()) {
    Box(
        Modifier.fillMaxSize()
            .layerBackdrop(source)
            .background(pageSurface)
            .popupBackground(selectedImage)
    )
    Column(Modifier.fillMaxSize().verticalScroll(scroll)) {
        // 将 source 传给 Ios27PickerExample 的 background 参数。
        // 日历里的 drawBackdrop(backdrop = background, ...) 负责坐标采样。
    }
}
```

不要在第二层再画一张不透明的全页底色遮住背景。若另有页面内容录制层供顶部或 Popup 使用，可以保留它，但 Inline 必须读取背景专用 source。采样依赖可以单向嵌套，不可形成环。

Color Picker 则读取已录制的底层页面（图片与页面内容），弹窗和遮罩放在该录制层之外。打开弹窗时不能移除底层内容，否则只剩单色源。

## 不允许的替代实现

- 不要在 `CanvasBackdrop` 内把整张图片 `drawImage(... dstSize = 日历尺寸)`：它会拉伸整图，位置与页面居中裁切不一致。
- 不要只画页面纯色后宣称图片透视正常。纯色 CanvasBackdrop 只适合本来就位于纯色列表中的控件。
- 不要将包含日历的父页面 source 传回日历本身；这会形成自采样，可能导致 RenderThread/Skia 递归崩溃。
- 不要在页面和玻璃中分别计算两套图片裁切、缩放或滚动偏移。
- 不要把静态截图、FigmaSceneView 或半透明背景框当成实时玻璃实现。

## 阴影、圆角与材质

2026-10-17 共用圆角绘制修复见 [修复记录与回归方法](glass-panel-corners.md)：高光参数必须保留原始 Shape 类型，按四角半径采样的着色器必须使用居中坐标。不要在各页面通过关闭高光或改圆角数值补偿底层绘制错误。

```kotlin
Modifier
    .drawBackdrop(backdrop = background, shape = { shape }, /* 已有材质参数 */)
    .clip(shape)
    .padding(contentPadding)
```

这是 modifier 顺序示意。外层先画玻璃及外部阴影，后面的 clip 限制内容。反过来 `.clip(shape).drawBackdrop(...)` 会裁掉外扩阴影。祖先容器的裁切仍可能影响阴影，需要检查实际截图。

Inline 的 Figma 外框圆角为 13，Compact 分组为 26；不得互相套用。不能靠增大不透明度掩盖采样错误。

本地 Figma Inline 的 Glass Effect 读到 `radius=16`、`refractionRadius=30`；上游 Dialog 使用浅色 16dp/深色 8dp 模糊、24/48dp lens。本项目借用 Dialog 的运行材质并作本地表面适配，两套渲染器没有已经验证的一一映射。不得将这些数值混称为原稿精确复现。参数完整说明见 [材质记录](color-picker-material.md)。

## Menus / Context Menu

菜单不是 Material `DropdownMenu`，入口是 `NativeExamplePage` 中的 `Ios27MenuExample`。页面内容继续留在 `Ios27PageContent` 的独立 `LayerBackdrop` 内；菜单面板作为兄弟覆盖层读取该 source，不能让包含菜单的父层再次录制自身。

| 变体 | 面板位置与尺寸 | 外框圆角 |
| --- | --- | ---: |
| Edit Medium | `(16, 180)`，`238 × 44 dp` | `34 dp` |
| Menu | `(16, 180)`，`250 × 338 dp` | `34 dp` |
| Context Menu | `(16, 247.5)`，`250 × 278 dp` | `34 dp` |

菜单面板必须复用 `drawBackdrop`，材质包含 `vibrancy`、轻微 `blur`、`lens`、`Highlight.Default` 边缘高光，以及面板外部阴影（半径 `48 dp`、向下偏移 `8 dp`）。高光不能用一条假描边代替，阴影也不能放到被 `clip` 的内容层里，否则边缘会被裁掉。

普通 Menu 的分组分隔间距为 `21 dp`，分隔线居中且厚度为 `1 dp`。Context Menu 的第一组是玻璃面板；后续 Section Title、Submenu 和第二组 Action 按 Figma 层级留在页面背景上，不要把它们错误地包进第一块玻璃。菜单外点击关闭，Cut/Copy/Paste/Delete 等动作通过宿主 clipboard 和状态回调落地；Disabled Action 不触发回调，Submenu 需要有独立的打开或动作回调。

## 验收顺序与证据

1. 选择有明显色块、文字或轮廓的图片。先看未覆盖部分的裁切，再检查玻璃覆盖的同一区域；不能只看弹窗外有没有图片。
2. 更换不同背景，确认玻璃内对应颜色和轮廓变化。检查浅色、深色和无图片状态，记录各自截图。
3. 让玻璃面板实际移动或滚动，检查采样位置随之变化、背景不被重新拉伸。页面内容未溢出、手势没产生位移时，应写“滚动未验证”。
4. 点击日期，确认选中标记移动；检查时间按钮结果、选图取消、移除背景、重进页面后的恢复。
5. 检查圆角和外部阴影是否被切掉，检查启动及操作后的崩溃日志。
6. 执行 AGENTS.md 要求的提取、tokens、玻璃规范、单测、APK 检查，以及 `node --test tools/figma-reader/native-framework.test.mjs`。

静态源码检查与编译只证明对应代码存在和构建成功，不证明像素相似度或运行时光学效果。修改测试时应保留防自采样、来源类型等有意义的约束，不能只更新参数匹配后宣称修复成立。

## 2026-09-16 修复证据与边界

- [修复截图](iphone-examples-compare/inline-fixed.png)：模拟器浅色模式，背景图颜色可见于日历覆盖区域。
- [交互截图](iphone-examples-compare/inline-interaction.png)：日期由 20 改为 21，选中标记随之变化。
- 修复时完整构建与现有静态检查通过，读取的 crash 日志为空。
- 该次页面未产生滚动位移，滚动验收未完成；没有新拍深色和 Compact 回归截图。不得把这些项目写为已验收。
- 本文修复的是图片位置采样和阴影裁切，不代表日历全部布局或玻璃光学已经像素级对齐 Figma。
