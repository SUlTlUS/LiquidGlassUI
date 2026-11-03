# iOS 26 / iOS 27 标准

2026-11-03 按用户要求：本次合并前的实现归为 **iOS 26**；`liquidglass27` 的全部非演示机制归为 **iOS 27**。这是本项目的版本划分，不表示 Apple 官方实现或像素级认证。

| 标准 | Gradle 依赖 | 接口与用途 |
| --- | --- | --- |
| iOS 26 | `implementation(project(":liquidglass"))` | 原有玻璃、Slider、Toggle、列表、Dialog、MeloX / ColorOS 接入保持原状 |
| iOS 27 | `implementation(project(":liquidglass-ios27"))` | 新几何、高光、弹性动作、Goo、Dock、多级菜单、Popup、Alert / Dialog、主题、124 图标与性能机制；传递依赖原 Sketch 光学核心 |

旧 `dev.liquidglass.ios27` 包、`Ios27*` 符号、tokens 生成器和 Figma 文件名是兼容性名称，仍代表旧 iOS 26 实现/参考数据。没有批量改名或重新生成原稿，以免破坏已有宿主。新增标准选择使用 `dev.liquidglass.standard.LiquidGlassStandard`；新的机制 API 在 `dev.liquidglass27.glass`。

机制库不包含 demo UI。按用户最新要求，示例在现有 sample 内重新编排，入口是“手机示例 → iOS 27”，七项分别为弹性按钮、Goo 操作组、导航底栏、多级菜单、Popup、对话框、主题与图标。统一复用 `ExampleRecordedResponsivePage`、`Ios27PageContent`、`Ios27PageHeader`、`Ios27ListSection` 和现有返回按钮；配色与字号来自当前页面 tokens。源项目的 `GlassShowcase` / `GlassVerificationPage` 已移除，不在 APK 中。

示例实现位于 `sample/src/main/java/dev/liquidglass/sample/Ios27NativeExamples.kt`，调用新机制库并显示真实回调结果。行内控件采样独立预览背景的色带；顶部模糊、菜单、Popup、对话框位于页面录制层之外，采样完整底层页面。依赖单向为“外层浮层 → 页面 → 行内控件 → 独立预览背景”，不会读取包含自身的录制层；`Ios27BackdropScope` 固定复用 Sketch 光学。菜单包含多级选择和长列表，Popup 演示保存/取消及模态切换，对话框包含输入/确认/长内容，图标页使用完整 124 图标。浅深色跟随应用设置，没有另起一套演示导航或主题设置。

现有底部导航和底栏示例使用 `Ios27NavigationDock`：26 的 `LiquidBottomTabsRegular` 布局、拖动、选中、采样和光学主体，加上 27 高光。旧库仅扩展可选 `surfaceLayer`，默认 null 保持 26 调用行为；依赖仍从新库指向旧库。原 `GlassNavigationDock` / `FlClashDockMotion` 机制继续保留，标准入口不使用它们的另一套主体。设置 → 玻璃标准可切换；API 33+ 默认 27，API 24–32 自动 26。原有组件示例按 iOS 26 保留。

Popup 已与菜单共用 `SketchMenuRootSurface` 及 `menuRootCornerPower`，包括源按钮高光到面板边缘高光的连续交接。前景快照和内容都保持宽高比例；内容转换对齐 FlClash `popup.dart` 的 `_contentTransform`，居中定位并按实时菜单轮廓裁切，避免把按钮字形随面板宽高分别拉伸。修改依据与验收见 [底栏与 Popup 修正](dock-popup-correction.md)。

## 接入 iOS 27

启动时可调用 `prepareIos27Glass(applicationContext)`，低版本直接返回 false。渲染入口要求 API 33；将整段新实现放在 SDK 分支中。库和宿主安装下限仍为 API 24，iOS 27 不使用 `overrideLibrary`；宿主原有的 MeloX 专用例外保持原样。

```kotlin
if (Build.VERSION.SDK_INT >= 33) {
    GlassTheme(dark = dark) {
        Ios27GlassScene(
            modifier = Modifier.fillMaxSize(),
            background = { PageBackground() },
        ) {
            ElasticGlassButton(
                modifier = Modifier.size(56.dp),
                style = GlassStyle.regular(dark),
                onClick = onAction,
            ) { Text("Action") }
        }
    }
} else {
    ExistingIos26Content()
}
```

`Ios27GlassScene` 固定启用 Sketch 光学。原始 `GlassScene` API 仍保留完整实验分支，默认 `sketchMaterial=false`；直接调用它时必须明确选择。实验 capture planner / GPU atlas 完整迁入但不强制启用。

已有独立 `Backdrop` 时，用 `Ios27BackdropScope(backdrop) { ... }` 提供场景级材质缓存与上下文。该 scope 不重新录制背景、不缩放整图；传入 source 必须不含它自己的玻璃。菜单和 Popup 所在场景需覆盖根窗口，锚点使用 `positionInRoot()` 的像素值。具体层级见 [背景接入约束](../glass-backdrop-integration.md)。

| 机制 | 公开 API |
| --- | --- |
| 容器与材质 | `Ios27GlassScene`, `Ios27BackdropScope`, `LiquidGlass`, `GlassPane`, `GlassStyle` |
| 弹性与融合 | `ElasticGlassButton`, `rememberElasticPressState`, `elasticPress`, `GooGlassRow`, `GooGlassGroup` |
| 标准导航 | `Ios27NavigationDock`, `GlassDestination`；原完整机制 `GlassNavigationDock` 继续保留 |
| 多级菜单 | `GlassMorphMenu`, `GlassMenuAction`；保留锚点、按压接力、回源、长列表与滚动回弹 |
| Popup 生命周期 | `GlassPopupState<T>`, `GlassPopupBox`, `GlassPopupSourceContent`, `GlassPopup` |
| Alert / Dialog | `GlassAlert`, `GlassDialog`, `GlassDialogAction`, `GlassDialogTextField` |
| 主题与图标 | `GlassTheme`, `glassColorScheme`, `GlassColorStyle`, `GlassGlyphs` |

源码中的场景级 32 项材质缓存、单 renderer shader 所有权、异步预热、九层 Picture 高光缓存、绘制阶段动画、曲线/轮廓/文本宽度缓存、惰性长菜单及滚动合并均成组保留。新桥接关闭旧 Sketch 默认高光，旧控件默认参数不变。没有复制第二份 Sketch 核心。

## 来源与验证

### 普通按钮加入 Goo

`GooGlassGroup` 内的 `ElasticGlassButton` 默认参与融合，支持文字、图标、着色内容和 `GlassPopupBox` 来源按钮，无需转换成 `GooAction`。继续使用按钮自身的点击、按压、拖动和来源前景录制。

```kotlin
GooGlassGroup(Modifier.fillMaxWidth()) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ElasticGlassButton(Modifier.weight(1f).height(52.dp), GlassStyle.regular(dark),
            onClick = onSave) { Text("保存") }
        ElasticGlassButton(Modifier.weight(1f).height(52.dp), GlassStyle.regular(dark),
            onClick = onCancel) { Text("取消") }
    }
}
```

沿用源 Goo 的胶囊轮廓、同排判断和 8+8dp 按压融合量；相同材质的相邻按钮合成一个材质面，不跨行或跨不同颜色/光学材质连桥。按照实际布局位置排序，支持不同宽度、RTL 和动态增删；超过六个按钮按 shader 容量分批，每批最多六个，批次之间不连桥。分组内部预留 24dp 以容纳按压和拖动外扩。

`gooEnabled=false` 可让按钮独立绘制，适用于自定义非胶囊轮廓或宿主用 alpha 隐藏按钮时。`GlassPopupBox` 自动在回源完成前退出融合，避免分组绘制隐藏来源。组外按钮保持原有行为。分组必须放在已有独立背景 scope 中，不得将分组自身录入采样源。

宿主“弹性按钮”和“Goo 操作组”提供文字/图标混排、着色以及动态移除示例；“Popup”提供可与图标按钮融合的来源按钮。

- [逐文件清单](merge-manifest.json)：源/目标路径、baseline 与 merged SHA-256、本次修改前的旧运行文件哈希。
- [原合并指南](source-merge-guide.md)：保留原文；机制完整合并，演示由当前 sample 自行组织，未复制原演示 UI。
- [原验收清单](source-verification-2026-11-03.md)、[原性能报告](baseline/README.md)：均为源项目历史记录；其中相对链接以源工作区为基准，不能当成目标项目结果。
- [来源与许可](provenance.md)、[本次验证](validation.md)。

```powershell
node tools/verify-ios27-merge.mjs
node --test tools/ios27/native-examples.test.mjs
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
python tools/ios27/generate_glass_glyphs.py --check
python tools/ios27/check_control_highlight.py
.\gradlew.bat :verifyIos27Merge :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :liquidglass-ios27:testDebugUnitTest :liquidglass-ios27:lintDebug :sample:assembleDebug :verifyDebugArtifacts -x :sample:sendDebugApkToQQ --no-daemon --console=plain
```

新 AAR 依赖旧 `:liquidglass`、Compose、Material3 等，不能只复制一个裸 AAR 就假定依赖齐全。推荐 Gradle 项目依赖。当前迁入可完全脱离原 `C:\IDE\Android\liquidglass27` 目录构建和运行测试。此轮只完成代码及构建，真机由用户验收；设备脚本保留为可选入口，没有自动运行。

底栏截图反馈后的材质层修正同时涉及 `LiquidBottomTabs.kt` 和 `DrawBackdropModifier.kt` 两个核心文件；新参数默认 null，基线与授权改动分别记录。见 [修正记录](dock-popup-correction.md)。
