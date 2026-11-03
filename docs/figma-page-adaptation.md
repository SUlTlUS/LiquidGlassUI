# Figma 页面适配（1.0.8）

## 1.0.8：区分普通分组页与滑块参考页

根据用户深色实机截图，撤销将滑块示例的纯黑分组推广到全部页面的做法。`Ios27PageTheme` 新增 `surface`，默认 `Ios27PageSurface.Grouped`，设置页及说明页使用黑色页面 + #1C1C1E 分组，恢复可见的圆角分组层次。该深灰来自 vendor 的 grouped secondary，是普通分组页适配选择，不声称来自本地滑块示例。

组件 / 滑块页显式选择 `Ios27PageSurface.SliderReference`，继续保留本地原稿的黑色填充。两种用途的浅色模式仍为 #F2F2F7 页面 + 白色分组。

普通深色分组上的分隔线为 #363638（#1A1A1A 以 Linear Dodge 叠在 #1C1C1E 上的解析结果），黑色参考分组上仍为 #1A1A1A。分隔线位置不变：仅行间显示、两侧各缩进 16dp。

后续新 app 默认用 `Ios27PageTheme(darkTheme)`，只有需要忠实呈现该滑块例图时才传 `surface = Ios27PageSurface.SliderReference`。没有覆盖 MaterialTheme、玻璃 style 或原始源码；背景采样连接仍保留原有兄弟层关系。

以下为历史版本记录，以本节用途划分为准。

## 1.0.7：去除每组首行顶线

修正 1.0.6 给每条滑块行都画顶线的问题：现在只有第二、第三行画顶线，每组三行恰好两条内部隔线，没有额外顶部或底部边框。保持 52dp 行高和左右 16dp 缩进。

用户要求所有页面颜色对齐后，页面 palette 的七个角色全部改为读取本地原稿变量，不再混用 vendor 的页面颜色。演示页额外的橙色 Aa 图标底色改为原稿蓝色。玻璃自身的轨道、材质、光照及动画不在此 palette 修改范围。

| 角色 | 浅色 | 深色 |
| --- | --- | --- |
| 页面 | #F2F2F7 | #000000 |
| 分组填充 | #FFFFFF | #000000 |
| 主文字 | #000000 | #FFFFFF |
| 次文字 | #3C3C43 / 60% | #EBEBF5 / 70% |
| 分隔线 | #E6E6E6 | #1A1A1A |
| 按压填充 | #767680 / 12% | #767680 / 24% |
| 强调色 | #0088FF | #0091FF |

浅色页面与白色框内底色在之前版本已是这些值，数值没有变化；本次不声称这些底色产生了可见变化。颜色参数对齐不等于完成真机视觉验收，仍需安装最新 APK 对比。原始玻璃颜色按用户边界保留。

## 1.0.6：颜色与分隔线修正

继续解析本地原稿的颜色变量与实例内分隔线，修正此前只提取外层尺寸的问题：

- `1:3 Backgrounds (Grouped)/Primary`：浅色页面 #F2F2F7、深色页面 #000000。
- 分组实际绑定 `1:15 Backgrounds/Primary`：浅色 #FFFFFF、深色 #000000。不能替换成 grouped secondary 的深灰色。
- `1:19 Separators/Vibrant`：浅色 #E6E6E6、深色 #1A1A1A。在当前白色/黑色实色分组上使用对应解析色，等效于该底色下原稿的 Linear Burn / Linear Dodge；不作为任意彩色背景上的通用混合实现。
- 滑块实例 `1:775` 等内部分隔线实际为 338×1，所在行宽 370，即两侧各 16。原实现只缩进左侧，已修正为两侧缩进。
- 六条演示滑块补齐行顶部分隔线；直接绘制在行内，不额外增加高度，三行仍为 156dp。普通文字列表的分隔线也改为两侧缩进。

颜色变量和内部线条尺寸保存在 `figma-local-measurements.json`，由解析器重新生成。玻璃滑块着色、折射、阴影及顶部模糊源码均未修改。

## 1.0.5：以用户提供的本地原稿为准

已解析 `docs/Untitled.fig`（来自用户的 `D:/下载/Untitled.fig`），共 510 个节点。可见页面只有 `Examples/Slider`，并非整套 UI Kit。原始文件与缩略图保留在项目中，精简节点数据在 `docs/figma-local-measurements.json`。

直接读取结果：

| 节点 | 属性 | 数值 |
| --- | --- | --- |
| 1:770 Examples/Slider | 画布 | 402×874 |
| 1:771 Screen | 页面填充 / 水平内距 | #F2F2F7 / 16 |
| 1:773 Content Area | stackSpacing / 顶部内距 | 10 / 10 |
| 1:774、1:778 Grouped List | cornerRadius / size / 填充 | 26 / 370×156 / 白色 |
| 1:775–781 中的六条滑块行 | 高度 / 水平内距 | 52 / 16 |
| 1:782 Toolbar - Top | 高度 | 54 |

因此，1.0.5 将页面分组圆角由 12 改为 26dp、间距由 24 改为 10dp；这些是此张示例的实测参数，不推广为所有 iOS 页面的固定规范。组件页增加两组、每组三条滑块，起始值为 0%、50%、100%，使用 54dp 居中标题。原有开关、按钮等演示保留在下方；底部玻璃导航也保留。

只移植外围布局：不复制 iPhone 机框、灵动岛或固定 62pt 状态栏，Android 使用实际 WindowInsets。原稿两端图标以现有太阳图标演示，不宣称图标逐像素一致。SF Pro 不打包分发，厂商主题仍可能替换 SansSerif（1.0.4 真机截图已观察到这一现象）。顶部原始渐进模糊及 48 个玻璃相关 Kotlin 文件不变，因此滑块和模糊效果不宣称与 Figma 原稿一致。

### 重现测量

先在临时目录解压 `docs/Untitled.fig`，然后执行：

```powershell
npm ci --prefix tools/figma-reader --ignore-scripts --no-audit --no-fund
node tools/figma-reader/read.mjs <临时目录>/canvas.fig docs/figma-local-measurements.json
node tools/generate-ios27-tokens.mjs
node tools/generate-ios27-tokens.mjs --check
node tools/verify-extraction.mjs
```

解析器使用 Node 24 的解压能力及锁定版本的 kiwi-schema。输入文件只读，提取结果不含用户编辑信息。数据生成器优先采用本地 1:774 圆角、1:773 间距、1:782 标题高度；其余页面 token 继续使用 vendor 数据。对应单元测试已更新。

1.0.5 验证：2026-09-13 构建成功，6 项单元测试通过，AAR / APK 必需类检查与 48 文件原文校验通过。原稿副本 SHA256 为 `C316361336ECFF0F1F266E3AAADA6FEB5CD2866094AF3DD3FBCB89DDEBFA8FD6`，与输入一致。设备 `192.168.1.247:36377` 重连超时，新版尚未安装和完成真机视觉验收。

以下为 1.0.4 历史记录，12/24dp 参数已被上述本地测量取代。

参考：[iOS and iPadOS 27 UI Kit](https://www.figma.com/community/file/1651309003795292092/ios-and-ipados-27)。

## 数据来源和限制

本次直接打开 Figma 社区页面失败，浏览器访问也超时，未取得设计节点或预览图，因此不宣称逐像素复刻。当前依据是已保存的 `vendor/ios27-design-system/packages/tokens/src/` 数据；其中 spacing.json 声明来自同一社区文件的 27.0.2 版本。这是上游记录，不是本次重新测量的结果。

## 已实现的页面参数

| 项目 | Android 实现 | 来源边界 |
| --- | --- | --- |
| 浅色底色 / 分组 | #F2F2F7 / #FFFFFF | colors.json |
| 深色底色 / 分组 | #000000 / #1C1C1E | colors.json |
| 标准 / 双行列表 | 最小 52 / 68dp，水平内距 16dp | spacing.json 的列表测量记录 |
| 分组标题 | Nested 42 / Prominent 45 / ExtraProminent 48dp 最小高度 | 同上；字体分别映射 Footnote / Headline / Title3 是 Android 适配选择 |
| 分组页脚 | 最小 32dp，上 8 / 下 6 / 左右 16dp | 同上 |
| 普通 / 双行图标 | 30dp、圆角 7dp / 44dp、圆角 11dp | spacing.json 的 radius 观察记录 |
| 页面文字 | 11 档字号、行高与字距，显式 SansSerif | typography.json；Android 字体替代，不内置 SF Pro，厂商仍可能替换系统字体 |
| 整组圆角 / 分组间距 | 12dp / 24dp | 沿用项目选择；不是该 Kit 已证实的分组圆角与间距 |

1.0.4 新增三档标题、页脚最小高度、双行图标和显式字体；底色与 52/68dp 列表原本已对应记录，继续保留。“说明”页的“内容布局”“阅读体验”分别展示两档强调标题，首个双行条目使用 44dp 图标。宿主状态栏及导航栏图标跟随应用选中的深浅色更新。

## 后续 agent 如何使用

```kotlin
Ios27ListSection(
    title = "显示",
    emphasis = Ios27SectionEmphasis.Prominent,
    footer = "跟随系统字体大小自动增长。",
) {
    Ios27ListRow(
        title = "设置",
        subtitle = "浅色与深色",
        leading = { Ios27IconTile(color = colors.accent, tall = true) { /* 图标 */ } },
    )
}
```

页面层使用 `dev.liquidglass.ios27`；只改此包、sample 宿主及 token 生成器。生成文件不要手改：执行 `node tools/generate-ios27-tokens.mjs`，再用 `--check` 校验。

`com.zeaze.tianyinwallpaper` 下 48 个 Kotlin 文件全部保持提取原文。不要给玻璃按钮、底栏、滑块、开关套用这里的尺寸或字体；不要覆盖其 style、着色器、动画。顶部 ProgressiveBlurContent 与底栏仍在被采样页面的外部，避免自我录制递归。

## 验证

2026-09-13：1.0.4（versionCode 5）构建成功，6 项单元测试通过；AAR 的 8 个必需类及 APK 的必需 DEX 类定义校验通过。

- `node tools/verify-extraction.mjs`：校验 48 个原始文件逐字节一致。
- `:liquidglass:testDebugUnitTest`：检查明暗色、尺寸、字体映射。
- `:sample:assembleDebug`：自动运行 AAR / APK 类定义打包校验。
- 本次设备地址 `192.168.1.247:35761` 拒绝连接，1.0.4 的安装、明暗模式截图、大字体及横屏效果尚未完成真机验收。之前版本的截图不作为本版本验收证据。
