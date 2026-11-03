# Handoff — Codex → Qoder 交接记录

## 当前玻璃接入规则（2026-09-16 修复后）

- 修改 Inline、Popup 或图片背景前先读 `docs/glass-backdrop-integration.md`，并遵守根目录 `AGENTS.md`。以下历史记录中的“最新”“优先级最高”和设备地址只描述对应轮次，不覆盖后续用户要求或本节修正。
- Inline 读取独立背景兄弟层的 `pickerBackgroundBackdrop`（`LayerBackdrop`）；背景图只在该背景层做一次中心裁切。禁止把整张图片缩放到日历大小伪装成透视，禁止采样包含自身的父层。纯色列表控件仍可使用独立 `CanvasBackdrop`。
- 玻璃先 `drawBackdrop`，再裁切内部内容；Inline 圆角 13dp，Compact 实色分组圆角 26dp。材质参数区分 Figma 原稿、上游 Dialog 实现与本地适配，不将上游 lens 24/48 宣称为 Figma 原始值。
- 最近一次修复已在模拟器 `emulator-5556` 查看浅色图片采样与日期 20→21 的点击变化，证据为 `docs/iphone-examples-compare/inline-fixed.png` 和 `inline-interaction.png`。构建与静态检查通过；滑动未实际产生位移，滚动采样未验收，深色和 Compact 尚未补拍，不宣称像素级完成。
- Stepper 原稿页已改为 `sample/src/main/java/dev/liquidglass/sample/Ios27StepperExample.kt`：`Stepper` 左对齐，行 370×52dp，右侧连体 accessory 92×32dp，圆角 16dp，左右各 46dp，中间 1×24dp；不显示额外数值文本。模拟器对比报告为 `docs/iphone-examples-compare/stepper-pixel-20260916/`，相似值 99.68%；减号/加号边界状态已实际验证。

## 最新接续：2026-09-16（分割线 hairline 对齐）

- 用户确认字体差异来自设备系统字体，后续视觉对比忽略字体，不调整字体实现。
- 用户反馈项目分割线比参考稿细；已统一页面列表、公共 `Ios27ListDivider`、行内 `ios27RowTopSeparator` 和 Picker 手动画线的渲染厚度。
- 原稿 token 保持 `separatorHeight = 1pt`，但按用户提供的 Slider 参考实测，参考线约 6px、项目原线约 3px；Android 页面统一使用 `Ios27Tokens.SeparatorRenderHeight = 2dp`。此前 `0.5dp` 与 `1dp` 方案均已撤回，玻璃组件未修改。
- 修改文件：`tools/generate-ios27-tokens.mjs`、`liquidglass/src/main/java/dev/liquidglass/ios27/Ios27Lists.kt`、生成的 `Ios27Tokens.kt`、`sample/src/main/java/dev/liquidglass/sample/Ios27PickerExample.kt`、`docs/ios27-page-adaptation.md`。
- 真机 `192.168.1.247:40085` 已安装新 APK 并复测 Slider 与两个 Picker；Slider 最新截图在 `docs/iphone-examples-slider-real-40085-separator2dp/`，相似度 97.51%；Picker 最新截图在 `docs/iphone-examples-picker-real-40085-divider-final/`。原始像素指标仍受系统字体影响，不作为字体结论。
- Node 原生测试 8/8、`verify-extraction.mjs`、tokens `--check`、`git diff --check` 已通过；crash buffer 未发现 `dev.liquidglass.sample` 的新异常，已有异常属于其他应用。
- Picker 日期/时间胶囊已按原稿层级固化：外层 accessory frame 为 34dp 高、radius=6，内层填充为 34dp 高、radius=100（Compose 50%）；Compact 的日期与时间组合间距为 6dp，不使用旧的 10dp 间距或固定 18dp 圆角。后续复用 `PickerPillText`，不要把两层圆角合并。
- Picker Compact 外层浅色分组框：日历上框和下方日期/时间列表框使用 `Ios27Tokens.GroupRadius = 26dp`；此规则不适用于 Inline。
- Picker Inline 与 Compact 的外框规范已分开：Inline 原稿为 370x377、radius=13 并带 Material/Glass Effect；Compact 原稿为 radius=26 的实色 Grouped List。Inline 当前读取独立背景兄弟层的 `LayerBackdrop`，不再仅采样页面纯色，也不能读取包含自身的父层。
- Picker Inline 直接调用已有 `drawBackdrop`，当前采用上游 Dialog 的 colorControls/blur/lens 配置及本地明确设置的高光、阴影和主题表面色，并非旧版 vibrancy 加默认阴影。准确接入方式及参数来源见 `docs/glass-backdrop-integration.md`；Inline=13dp、Compact=26dp，不能用单一 `panelRadius` 覆盖。
- Picker 页面已完成深色 token 适配：页面/分组/文字/次要文字/分割线/蓝色强调/选中日期/日期时间胶囊均由 `LocalIos27PageColors` 和对应 dark tokens 决定，禁止在 Picker 中重新写 `Color.Black` 或固定浅色胶囊。

## 当前接续：2026-09-15（真实设备 192.168.1.247:40085，优先级最高）

- 用户本轮明确要求恢复使用真实设备 `192.168.1.247:40085`，覆盖旧记录中“只用模拟器”的临时验证限制。设备为 Android 16，1272×2800，已成功安装最新 `sample-debug.apk`。
- 针对真实设备全量 56 页排名最低的 `Control Center / Default`（原相似度 11.2%，截图仍是普通设置列表）已完成首轮原生页面重建：新增 `sample/src/main/java/dev/liquidglass/sample/Ios27ControlCenterScene.kt`，并在 `NativeExamplePage.kt` 增加全屏原生路由。
- 页面使用 Figma `12740-35188.json` 的 402×874 坐标、壁纸资源 `9bddf87eae143962a7ecc5a5e617b20f45bacb69`、50% 原稿遮罩；玻璃卡片作为独立 `drawBackdrop` 兄弟层，背景先经 `layerBackdrop` 采样，未修改受保护玻璃运行源码。
- 控制中心卡片已包含实际点击状态回调：网络/音乐/快捷卡片可切换，亮度/音量卡片可点击改变填充比例，底部控制按钮可点击；长按页面进入原稿对照，避免回到不可操作的矢量查看器。
- 真机单页复测输出：`docs/iphone-examples-control-center-real-40085-final/`；相似度从 11.67% 提升到 81.97%，changed 从 88.33% 降到 18.03%。最终 APK SHA-256 为 `DAA8E8E04D859EA757B2DE70E2A4AE25D91E4D8095A1A92BD2F89EE1324BA3C3`，报告与最终产物一致。截图确认壁纸、暗遮罩、卡片几何、主要 glyph 和底部控制图标已出现；剩余差异主要是彩色 SF 图标/矢量细节、真实玻璃折射与原稿合成差异。
- 本轮最终构建通过：`:verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug`；Node 原生框架测试 7/7；APK 共 15377 个 DEX 类，必要类守卫通过。`verify-extraction.mjs`、tokens `--check`、`git diff --check` 通过。
- crash buffer 检查未发现 `dev.liquidglass.sample` 的 `FATAL EXCEPTION`；buffer 中已有的是 `com.heytap.cloud`、微信、淘宝等其他应用历史异常，不能据此宣称整机无崩溃。
- 顺手恢复并固化 `12740-24239` 分节列表原生路由：`sectionedList` 进入 `SectionedListExample`，静态测试覆盖 8 个分节/13 行和无首行顶线规则。
- 下一步按真实设备排名继续处理第二低的 Notifications，或重跑全量 56 页真实设备 sweep；不要把当前单页 81.97% 当作全量完成，也不要覆盖正式全量报告。

## 最新接续：2026-09-15（本节优先）

- 用户最新要求：只使用模拟器，不再操作真机；功能回归仍暂缓，优先原稿布局。
- 已完成 List 的 `Type=Full Width + Section Headers` 原生布局接入：`sample/src/main/java/dev/liquidglass/sample/SectionedListExample.kt`。从场景提取 8 个分节、13 行文本与字形，39pt 节标题、52pt 行、16pt 内距；使用 `Ios27ListRows` 自动分隔线，点击仍走宿主弹窗，非整页矢量查看器。
- 修正 `IphoneExamples.kt` 导航标题误取大标题/行标题、`SceneGlyphs.kt` 按钮字形重复平移。没有修改受保护玻璃源码。
- 既有 Slider 刻度的硬编码颜色改为完全同色的 `Ios27Tokens.SecondaryLabelLight.copy(alpha = 0.18f)`，未修改刻度尺寸、组件效果。
- 校验：完整 Gradle 构建/AAR/APK 定义守卫、源码基线和 tokens 检查通过；Node 静态/解析测试 10 项通过。最新模拟器截图结果另见后续补充。
- 先前真机的分节列表对比 3.17% 属于导航修正前结果，不可当作最新 APK 验收。对照渲染器会画节首顶线，原生页遵守项目禁止首行顶线规则；不能为压低差异率破坏规则。
- 下一步：验证本次分节列表和导航修正；其余 List 两种变体仍待对齐，随后处理底栏和系统场景。不要把一页进展宣称为 56 页像素完成。

### 本次模拟器结果

- `Medium_Phone_API_36.1` 已成功冷启动，序列号 `emulator-5556`，无窗口运行；已安装最新 APK。
- 已人工查看 `docs/iphone-examples-compare/native/12740-24239.png`：全宽白底、分节文本/行距已接入；重复导航标题及字形偏移已消除。仍有玻璃阴影、分隔线和系统栏差异，不宣称像素完全一致。
- 最新单页报告 `docs/iphone-examples-compare/report.json` 为模拟器结果，阈值24的差异像素占比2.96%，只比较本地矢量参考，不是 Figma 官方截图金标准。
- 冷启动时 `NATIVE_READY` 先于 GPU 绘制：原600ms等待曾截到目录页，相关 Slider 截图/指标无效。脚本现等待4秒/2秒；仍须人工检查截图，不得只信差异率。
- 用户切换要求后未再操作真机。后续所有验证仅指定模拟器。

### 建议技能

- `android-kotlin-encoding-safe-edits`：UTF-8 小范围 Kotlin 编辑。
- `ui-ux-pro-max`：布局稳定性与可访问性检查；原稿参数和项目规范优先。
- `handoff`：接续后更新本文件，不重复其他文档。

> 生成时间：2026-09-15。来源：Codex CLI 会话记录（`C:\Users\29373\.codex\sessions\2026\09\`）。
> 中断原因：Codex 用量超限（usage_limit_exceeded），非任务完成，接续时优先补跑验证。
> 二次更新：2026-09-15，Qoder 会话，方向已转为「像素级复刻原稿」（用户明确：别测试功能，首要是使用现有组件像素级复刻原稿）。
> 三次更新：2026-09-15，Qoder 会话，主屏 6 页（Home Screen / Quick Actions / Widgets Small~XL）已收敛到均值 1.49%，见「主屏 6 页收敛」一节；全量 56 页 report 仍是旧 APK 的，必须重跑落盘。

## 最新接续：2026-09-15 下午（Qoder，git 基线 + 主屏场景复刻）

- 真机 `192.168.1.247:45563` 已离线（连接超时），全部验证改用模拟器 `emulator-5556`（Medium_Phone_API_36.1，1080×2274 内容区）；`compare-native-pixels.mjs` 现强制 emulator-only。
- 已 `git init` 并建立基线：`e32089e`（回退后工作区快照）→ `1d2c52d`（主屏场景 + 导航按钮漂浮修复）。后续每收敛一页就提交，防止再次丢改动。
- 全量 56 页相似度扫描已完成：`docs/iphone-examples-scan-2026-09-15/ranking.md`（低分优先，口径=RGB 最大通道差≤24 像素占比，参考为本地矢量渲染，空白背景会抬分，不能单凭分数认定一致）。
- **漂浮修复**（用户播报确认根因：字形画在 LiquidButton 兄弟层，按钮缩放不跟随）：`IphoneExamples.kt` ExampleNavBar 已把 `button.texts` 字形移入 LiquidButton `content`，fallback 分支同样内嵌。
- **主屏场景复刻**（ranking 1/9/13/14/15 名，原差异 80%+）：新文件 `sample/.../Ios27HomeScreenScene.kt`，JSON 驱动解析壁纸 hash、App 图标网格（含 App Name 字形）、Widget（solid+linear-dodge 渐变预算端色、Padding Guide 虚线框、外置标签阴影）、Search/Dock 玻璃（`drawBackdrop` + RoundedRectangle(38) + vibrancy/blur2/lens12×24 + #999@33% 表面）。`NativeExamplePage` 对 Home Screen / Home Screen Widgets 类别提前分流到全屏场景（pt 对齐 density、safeDrawing 与 reference 同裁切）；场景坐标 y 减 62（状态栏带），壁纸 `requiredHeight(874.dp)` 防父约束压缩。
- `SceneGlyphText` 新增 `shadowDp`（三层偏移近似原稿 drop shadow y2/r25），用于 Widget Name 与 App Name。
- 模拟器结果（阈值 24 差异占比，越低越好）：XL **10.94%**、Large 15.21%、Medium 23.67%、Small 26.32%、Home Screen 29.10%（修复前 80%+）。扫描时的 Medium 82% 为截到目录页的时序噪声，重跑正常。
- 剩余差异主项：Dock/Search 真玻璃折射 vs 矢量模拟（已知规格缺口，不得宣称像素验收）、文字阴影强度、虚线相位、My App 徽标位置。未修改受保护玻璃源码。
- 下一步（按 ranking 从低到高）：Quick Actions 11.5%、Control Center 11.84%、Notifications 12.42%、Color Picker 12.64%、Lock Screen 13.2%、Sheets Fullscreen 14.76%、Action Sheet 15.48%、Alert 两变体——均为系统场景/弹层结构缺口，需要页面结构重建而非微调。（Quick Actions 已在下面收敛到 1.99%，其余仍待重建）

### 主屏 6 页收敛（同日延续，Qoder）

口径：`compare-native-pixels.mjs` 的 changed%（RGB 最大通道差 >24 的像素占比，参考为本地矢量渲染），越低越好。

| 版本 | Small 24471 | Medium 24474 | Large 24477 | XL 24480 | QA 35187 | Home 35189 | 均值 |
|---|---|---|---|---|---|---|---|
| 上轮末（TEXT 投影 + fitted） | 5.84 | 5.69 | 5.38 | 5.02 | 6.44 | 5.59 | 5.66 |
| `8214c8c` 壁纸/Overlay 按 authored 874pt 裁剪 | 2.03 | 1.89 | 1.58 | 1.24 | 3.11 | 1.78 | 1.94 |
| `bd5409b` 红色 App 角标 | 1.91 | 1.89 | 1.58 | 1.23 | 3.12 | 1.66 | 1.90 |
| `411a0be` 去掉玻璃默认高光边 | 1.63 | 1.61 | 1.31 | 0.95 | 2.78 | 1.38 | 1.61 |
| `8eec722` Quick Actions dim 底边修正 | 1.63 | 1.63 | 1.31 | 0.96 | 2.26 | 1.39 | 1.53 |
| `91c666b` 菜单 Widgets 行矢量图标 | 1.63 | 1.62 | 1.32 | 0.97 | **1.99** | 1.39 | **1.49** |

本轮五个根因（全部在 sample 宿主，未碰受保护玻璃源码）：

1. **fitted vs authored 高度**：`fitSceneToViewport` 会把 STRETCH/MAX/CENTER 节点撑到真实 viewport，但 `FigmaSceneView` 仍用节点 authored `paths` 矩形（0..874pt）裁剪，所以壁纸/Overlay 的“生长尾部”在参考里是被切掉的。解决：同时记 `wallpaperH`（fitted）与 `wallpaperClipH`/`overlayH`（从 `source` 未 fit 树读 874）。
2. **App 角标**：Badge 是 App Icon 的直接子节点（matrix 1,0,53/0,1,-12，24×24，r=100，rgb(1,0.2196,0.2353)），内部 TEXT 是白色数字。身份判定必须用包围盒 `HomeBadge.contains(tx,ty)`，不能比 `===` 实例（两次 `collectTexts` 会 parse 出不同对象），否则数字会被当成 App Name。
3. **玻璃高光边**：`drawBackdrop(...)` 默认 `highlight = DefaultHighlight` = 0.5dp 白描边，在 Dock/面板上下沿各产 ~3 行亮边（native 219/198/156 vs 参考 134）。Figma 原稿这些面板只有两个 DROP_SHADOW，无高光边 → 传 `highlight = { null }`。单项收益最大（均值 1.90→1.61）。
4. **`requiredSize` 静默居中**：`scenePos().requiredSize(w,h)` 的 h 超过父级约束（846.4dp）时会被 coerce 回父约束并**居中**，溢出 (2348-2274)/2 = 37px 使整张表上移。解决：dim 改用 `drawBehind { drawRect(color, Offset(0f, -StatusBarPt*density), Size(width, overlayH*density)) }` 浮点矩形，完全绕开布局。
5. **菜单 Widgets 行矢量图标**（`91c666b`）：5 个 SF Symbol 轮廓 + 5 个 237 槽圆角矩形只存在于 `paths` 几何（无 TEXT、无 imageHash），之前根本没画。新增 `figmaPath()` + `collectVectors()`，从 `findNode(menuNode, "Actions")` 子树按父先于子顺收集 SOLID fill（起点必须是 Actions：Actions 自己的 matrix 携带“距面板顶 10pt”内缩，从 Widgets 起会丢这 10pt，表现为图标整行偏高 27px）。只取 Actions 子树：面板自身的 `Fill + Shadow` / `Glass Effect` 也是矢量路径，但它们是玻璃表面，由 `HomeGlass` 负责。

Compose 1.11.2 API 坑（下次直接避）：`androidx.compose.ui.graphics.Path` 没有 `quadTo`，只有 `quadraticTo`；`DrawTransform.translate/scale` 已无 block 重载（会永久改变变换），所以矢量在解析期就烘焙成“面板相对像素”坐标，绘制只需 `drawPath`。

主屏 6 页剩余差异构成（已定性，下次不要重做取证）：

- 图标行 ~0.15–0.22%/页：native 图标顶行 100% 覆盖 vs 参考 50% 混合，属 0.3–0.5px 亚像素相位差，判为不可约 AA。
- Dock 内图标边缘 ~0.10%/页。
- QA 菜单：Widgets 行条带已从 14.83% 降到 5.17% red（剩下的也是描边 AA）；面板左沿 x75..94 有 ~0.11% 的亮边带，来源是参考渲染器**忽略**了 `Glass Effect` 的 GLASS/INNER_SHADOW 效果（它只处理 DROP_SHADOW，且 white MULTIPLY fill 对参考是恒等操作），而 native 走真 `drawBackdrop` 折射 —— 这是已知规格缺口，不得为了分数去模参考的平坦合成而宣称玻璃一致。
- 菜单文字列（Symbol/Label 字形）~0.2–0.4%/页：光栅化器 AA 差异。

## 项目

Tianyin LiquidGlass：Android 液态玻璃 UI 组件库（`liquidglass` 为 AAR 库，`sample` 为演示宿主），按 iOS 27 / Figma 社区稿复刻页面与交互。

## 已完成工作（Codex 阶段，截至会话中断）

1. **代码提取**：48 个 LiquidGlass Kotlin 文件存于 `docs/liquid-glass-extracted/source/`，`tools/verify-extraction.mjs` 做 SHA-256 逐字节校验。
2. **上游融合**：合入上游 2 处布局坐标修复，保留本地主题/拖动/触觉反馈。见 `docs/upstream-merge-2026-09-13.md`。
3. **闪退修复**：根因是 APK 打包缺 `Ios27PageThemeKt`（非渲染递归）。见 `docs/startup-crash-2026-09-12.md`；新增 `tools/verify-android-artifacts.ps1`。
4. **视觉修复（1.0.1–1.0.12）**：底栏透视、顶部渐进模糊、深色主题 `#000000`/`#1C1C1E`、分组圆角 26dp、分隔线 16dp 缩进、Slider 阴影与把手两端停靠。
5. **规范固化**：`Ios27ListRows(...)` 自动规范 API；颜色/行高/间距统一走 `Ios27Tokens`（只能由 `node tools/generate-ios27-tokens.mjs` 生成）。
6. **iPhone 示例（1.0.18）**：`tools/figma-reader/` 完整工具链，56 个变体全部绘制并截图通过；修复数字键盘颜色、表情黑块、标签栏遮罩。
7. **框架融入**：默认入口为可操作 Compose 页面 `NativeExamplePage`；滑块/开关/步进器/输入框/弹层已接状态，交互实测有状态变化。

## Qoder 阶段：像素级复刻（2026-09-15）

### 设备与环境

- 验证机：**只用模拟器 emulator-5556**（1080×2400，420dpi，density 2.625，Medium_Phone_API_36.1）。用户明确要求不再操作真机；`compare-native-pixels.mjs` 也已硬性 `if (!device.startsWith('emulator-')) throw`。
- 真机 `192.168.1.247:45563` 已弃用（早期 MX Player 悬浮窗污染截图、IME 切换等问题都来自真机）。
- 坐标映射（emulator-5556 实测，1 设计 pt = 屏宽/402 px = 2.686567 px）：
  `screen_x = canvas_x × 2.686567`，`screen_y = 63 + (canvas_y - 62) × 2.686567`；viewport = 1080×2274 @ top=63；`deltaPt = ((62 + viewportPt - 874) * 4).toInt() / 4f ≈ 34.25`。
  常用锚点：canvas 90→138.2、479.3→1184、874→2244。
- 构建：`$env:ANDROID_HOME="C:\Users\29373\AppData\Local\Android\Sdk"`，然后 `.\gradlew.bat :sample:installDebug --no-daemon --console=plain -q`（-q 成功时无输出，别以为没跑）。
- PowerShell 注意：`&&` 用 `;`；adb 用绝对路径 `C:/Users/29373/AppData/Local/Android/Sdk/platform-tools/adb.exe`；`node -e` 在仓库根 require 不到 pngjs（node_modules 在 `tools/figma-reader/`），一次性分析脚本统一写成 `tools/figma-reader/tmp-*.mjs`；`2>&1` 不能写在 `foreach{}` 块后面。

### 对比工具链（tools/figma-reader/）

- `compare-native-pixels.mjs <设备> [key...]`：同机先后截 native 页与 reference 原稿视图，pngjs 逐像素差异（阈值 24）+ 红色热力图，输出 `docs/iphone-examples-compare/{native,reference,diff}/<key>.png` 与 report.json。不传 key 跑全量 56 页。脚本会强制浅色模式并在退出时恢复 uimode。
- `measure-example.mjs <key>`：原稿绝对几何测量（pt，y 已减 62pt 假状态栏）。
- `find-bands.mjs <png> <#hex> [tol]`：截图色带 y 定位，验证垂直对齐。
- reference 直达：intent extra `example_reference=true`（仅 debuggable）。
- 迭代对比不覆盖正式产物：`$env:EXAMPLE_COMPARE_OUT="docs/tmp-home-compareN"` 切换输出目录。report.json 每次全量重写，只跑子集会丢掉另外 50 页结果，所以正式 56 页 report 必须单独跑一次全量再提交。
- 已知坑：全量 sweep 的**第一页**偶尔因列表点击/首帧未完成拍到空白页（曾出现 24471 = 80.65%，单跑即 1.63%）。看到离群值先单页复跑确认。
- 逐行/逐列取证脚本（一次性，用完可删）：`tmp-rows.mjs <key>`（按红色条带汇总）、`tmp-band.mjs <x0> <y0> <x1> <y1> <png...>`（矩形内红色占比）、`tmp-cols.mjs <png> <y0> <y1>`、`tmp-rowcount.mjs`、`tmp-scan.mjs [r]<y> <x0> <x1>`（单行/单列取色）、`tmp-dark.mjs` / `tmp-dy.mjs`（暗像素 bbox / 逐行）、`tmp-red.mjs`（红色连通域）、`tmp-crop.mjs` / `tmp-stack.mjs`（裁剪 / native+reference 叠图目视）、`tmp-tree.mjs` / `tmp-badge.mjs` / `tmp-tree2.mjs`（场景 JSON 几何与原始节点 dump）。

### 已实施的复刻修复

1. **全局主题**：`MainActivity` 的 MaterialTheme primary 改为 `Ios27Tokens.BlueLight/BlueDark`（LiquidSlider 轨道填充读 `MaterialTheme.colors.primary`，受保护组件不可改）。
2. **密度对齐**（核心）：`NativeExamplePage` 用 `BoxWithConstraints` + `CompositionLocalProvider(LocalDensity provides Density(constraints.maxWidth / 402f, 1f))`，1 设计 pt = 屏宽/402 px，消除真机 dpi 导致的 10.6% 整体偏大。topInset 先用真实 density 取 px 再换算。
3. **字形渲染**（`sample/.../SceneGlyphs.kt`）：从场景 JSON 的 TEXT 节点 glyphs 直接画轮廓，不依赖文字内容。组件：`SceneGlyphIcon`（单字形）、`SceneGlyphText`/`rememberSceneText`（整 TEXT 节点）、`rememberSceneToolbar`（导航栏）、`rememberSceneContentEmpty`（内容区空检测）。节点查找：导航栏 INSTANCE 名 "Toolbar" 或前缀 "Toolbar - Top"；导航标题取 toolbar 直接子 "Title"；大标题在 "Title and Subtitle" 块。
4. **导航栏复刻**（`ExampleNavBar`，IphoneExamples.kt）：原稿 Toolbar 的所有 "Button Group" 按绝对位置渲染为 LiquidButton 玻璃钮（44×44 圆钮 / 160×44 胶囊，含 Tint→accent 检测）+ 字形叠加；左侧钮点击=onBack，右侧=演示动作；标题居中，长按标题进原稿对照。reference 侧不再画宿主按钮（场景自带导航栏，BackHandler 退出）。
5. **Slider 页**：乌龟/兔子图标接入（leading/trailing 插槽，`Ios27SliderRow` 新增参数）；`Ios27SliderRow` 新增 `ticks` 参数（5 个 4pt 刻度点，中心距轨道起点 18+53.5i pt、轨道中心下 9pt、画在滑块下层被把手遮挡）；把手宽 40dp vs 原稿 38pt 为受保护组件差异，不改。
6. **FigmaSceneView alpha 修复**：.fig 导出中 `color.a` 与 `fill.opacity` 冗余同步，原代码相乘导致轨道等元素淡出（250 vs 正确 228）。已修：color 不透明时才乘 fill.opacity。
7. **大标题**：非 compact 页原稿有 largeTitle 时用字形渲染（替代中文 LargeTitle）。
8. **空内容页**：Toolbars Default 等原稿 Content Area 为空时，native 不渲染演示列表。

### 当前对比结果（浅色）

- 已接近：Slider 1.53%、Toolbars Default 1.36%、Large Title 1.72%、Steppers 0.95%、Toggles 1.65%、Text Fields 2.45%、Face ID ~2.4%、Sheets Inspector 2.35%。
- 中等：Toolbars Bottom 4.7-6.9%、Tab Bar 5.1-5.4%、List 6.1-8.8%、Menus ~3.5-4%、Keyboard 2.7-4.8%（Emoji 11.6%）、Empty States 7-7.5%、Picker Inline 11.3%、Activity View 11.5%。
- 主屏系（本轮已复刻）：Home Screen 1.39%、Widgets Small/Medium/Large/XL 1.63/1.62/1.32/0.97%、Quick Actions 1.99%。详见上面「主屏 6 页收敛」。
- 仍未复刻的系统场景页（80%+，native 还是演示布局）：Sheets Fullscreen 85.2%、Notifications 82.8%、24428 87.6%、Lock Screen 86.8%、Color Picker 87.4%、Action Sheet 84.7%、Alert 24488 84.0% / 24497 82.9%、Control Center 88.2%。注：这些数字来自 `8214c8c` 那次 sweep，之后的四项修复（角标/高光/dim/矢量）还没反映到全量报告里，必须重跑。

### 进行中 / 下一步

本会话遗留（按优先级，下次接着做）：

- [ ] **重跑全量 56 页 sweep 并提交 `docs/iphone-examples-compare/`**：当前 report.json / ranking.md / 168 张 png 仍是 `8214c8c` APK 的，不包含角标/高光/dim/矢量四项修复。工作区已有一批未提交的旧 sweep 图片，直接覆盖后一次提交。
- [ ] **清理临时产物**：`tools/figma-reader/tmp-*.mjs`（bottom/rows/scan/tree/tree2/badge/red/band/crop/cols/rowcount/dark/dy/stack）、`docs/tmp-home-compare*/`、`docs/tmp-*.png`、`docs/tmp-tree-35187.txt`、`docs/compare-stderr.txt`、`docs/iphone-examples-compare/crop/`。注意 tmp-crop/tmp-rows 已被 git 跟踪，删除要 `git rm`。
- [ ] **执行 AGENTS.md 三条必做校验**（见文末命令链）；若改过解析器还要 `node --test tools/figma-reader/examples.test.mjs`。
- [ ] 同步更新 `docs/iphone-examples.md` 的缺口清单（目前仍写着主屏未复刻）。

其他页面（旧清单，未动）：

- [ ] List 页行内容复刻：原稿行为 Title/Subtitle/Detail/Drill-in 字形行，第 2 行是 swipe actions 演示行（x=-124 滑开态 + 3 个 60×68 操作钮 #aeaeb2/#0088ff/#34c759）。
- [x] Tab Bar 宿主已抽取为 `Ios27TabBarExample`，统一使用 `LiquidBottomTabs` + `LiquidBottomTab`，点击与选中状态已在模拟器验证；Tab Bar 像素几何仍单独跟踪。
- [ ] Toolbars-Bottom / Tab Bar 底部栏与原稿几何对齐。
- [ ] 系统场景页（80%+）逐页布局重写：可复用 `Ios27HomeScreenScene.kt` 的套路（findNode 解析 + scenePos/sceneBox + HomeGlass + collectTexts/collectVectors）。

## 遗留待办（Codex 阶段，功能向，暂缓）

- [ ] 交互回归：`node tools/figma-reader/check-native-interactions.mjs <设备序列号>`（check-native-edges-theme 仅模拟器可用，真机被断言拒绝）。
- [ ] 已知缺口：表情用 Android 彩色字形渲染，未达 iOS 像素一致。
- [ ] （可选）用户未回复是否运行 `codegraph init -i` 建索引。

## 关键约束（AGENTS.md 摘要）

- `liquidglass/src/main/java/com/zeaze/tianyinwallpaper/` 默认只读；授权修改需记录到 `docs/liquid-glass-merged-files.json`。
- 页面布局问题在 `dev/liquidglass/ios27` 或 sample 宿主解决，不改玻璃组件。
- 背景采样：页面背景用独立 `LayerBackdrop` 录制；禁止父层通过 `layerBackdrop` 录制包含玻璃控件的自身（防 RenderThread/Skia 递归闪退）。
- 修改示例解析器后必须 `node --test tools/figma-reader/examples.test.mjs`，重建安装并重拍受影响页面。
- 原稿矢量渲染不等于 LiquidGlass 组件真实折射；`docs/iphone-examples-previews/` 是简化渲染，不是金标准。

## 校验命令链

```powershell
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
node --test tools/figma-reader/native-framework.test.mjs
node tools/figma-reader/check-native-interactions.mjs <设备序列号>
node tools/figma-reader/compare-native-pixels.mjs <设备序列号> [key...]
# 迭代对比不覆盖正式产物：先 $env:EXAMPLE_COMPARE_OUT="docs/tmp-home-compareN"
```
