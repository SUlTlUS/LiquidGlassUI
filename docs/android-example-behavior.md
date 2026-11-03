# 全示例 Android 行为适配（视觉冻结）

适用日期：2026-10-08。此文档是追加的行为规范，不替代任何已有 iOS/Figma、玻璃提取、MeloX 或听音模式设计说明。

## 不能改变的部分

原稿和现有设计说明仍决定颜色、系统字体、字号、圆角、尺寸、间距、分隔线、图标、玻璃背景采样、阴影、高光及动画。不得为了“Android 化”换成 Material 外观、重画玻璃、放大可见按钮、增加间距或删掉原规范。

浅深色是同一实例跟随宿主主题，不是默认并排展示。已从菜单删除的系统场景不重新加回。不得把公开 API 保留的历史示例误当成需要恢复的入口。

本轮没有改受保护的 `com/zeaze/tianyinwallpaper` 玻璃运行目录、原始提取基线、生成 tokens 或手机示例资产。原有八份规范文件与清单的 SHA-256，以及运行目录、原始基线、资产目录的聚合 SHA-256 已固定在 `android-example-visual-contract.json`。

## 自动复用入口

行为层源码：`liquidglass/src/main/java/dev/liquidglass/ios27/Ios27AndroidBehavior.kt`。

| 入口 | 用途 | 不做的事 |
| --- | --- | --- |
| `Ios27AndroidDesignBehavior(designDensity)` | 在 402pt 设计坐标下换算系统最小触摸目标；提供原来的设计 Density | 不改测量、padding、字体或绘制 |
| `Modifier.ios27AndroidPane(title, onDismiss)` | 页面/浮层名称、遍历分组，以及可选关闭动作 | 不添加可见标题，不改变采样层级 |
| `Modifier.ios27AndroidProgress(...)` | 自定义滑块/滚轮的范围、朗读值、TalkBack 设置进度、方向键/Home/End | 不替换原触摸手势、轨道、把手、阴影或动画 |
| `Modifier.ios27AndroidBackgroundBlocked(blocked)` | 将被模态面板覆盖的内容移出无障碍树 | 不隐藏画面，不改背景或触摸动画 |

三个公共设计壳 `ExampleDesignPage`、`ExampleDesignOverlay`、`ExampleFullBleedDesignPage` 已自动应用触摸换算；调用者不要再用 48dp 的可见 padding/size “补规范”。响应式页面仍使用实际 Android Density，无须做第二次设计坐标换算。

标准 `ios27PressedClickable` 继续复用 Foundation clickable 的焦点、禁用和按压行为，现有圆角遮罩与短按动画保持原样。页面公共入口 `Ios27ExampleContent` 统一提供页面名称；原稿对照仍是显式路由。

## 56 个原稿变体与额外示例的覆盖清单

“覆盖”指接入公共 Android 行为层并按类别补充/保留操作语义，不代表逐页真机像素验收，也不表示模拟系统场景成为真正的系统功能。

| 类别 | 变体数 | 行为适配或保留的接口 |
| --- | ---: | --- |
| Toolbars - Top | 5 | 公共页面名称、现有返回/按钮点击与预测返回；视觉不变 |
| Toolbars - Bottom | 3 | 公共页面名称、设计坐标触摸目标、现有输入框与操作回调 |
| Tab Bar | 2 | 公共页面名称；既有标签选择、split search、原动画不改 |
| Text Fields | 1 | 四态示例保留；朗读区分未输入/聚焦/输入/已输入，保留 BasicTextField 的真实编辑语义 |
| Toggles (Switches) | 1 | 保留 Switch role、状态与切换动作、既有玻璃渲染 |
| Slider | 1 | 增加键盘和无障碍进度调整；连续调整默认 5%，拒绝 NaN/Infinity，端点钳制 |
| Steppers | 1 | 增减按钮、边界禁用与当前数值朗读；92×32 原尺寸不改 |
| Sheets (Modals) | 3 | 面板名称、关闭动作、既有返回处理；保存操作按钮状态 |
| Picker | 2 | 前后月名称、日期选择状态及完整日期朗读；年/月/时/分可无障碍与键盘逐项调整；弹层关闭动作 |
| Menus | 2 | 面板名称、关闭动作，保留编辑菜单真实动作与现有菜单布局 |
| List | 3 | 公共列表的集合/位置元数据；Full Width 每行可直接访问三项操作与展开/收起动作 |
| Empty States | 3 | 公共页面名称，保留现有按钮语义、固定位置及非滚动布局 |
| Context Menu | 1 | 增加朗读长按动作与 Enter 打开入口；触摸长按和缩放动画不变 |
| Color Picker | 1 | 网格位置/选中状态，光谱四项调整动作，RGB/透明度进度接口，页签选中状态；保存模式和自定义 RGBA |
| Action Sheet | 1 | 面板名称、关闭动作；原右上角触发及展开动画保留 |
| Alert | 2 | 继续用 LiquidDialog，添加关闭语义；输入框复用四态组件 |
| Activity View | 1 | 分享面板名称、关闭动作、保存协作选项；保持纯色表面 |
| Keyboard | 11 | 仅保留公开兼容入口、公共行为壳及现有输入类型；不恢复菜单入口 |
| Face ID | 2 | 仅保留公开演示回调与公共行为壳；不冒充系统生物识别 |
| Notifications | 2 | 仅保留公开演示回调与公共行为壳 |
| Lock Screen | 1 | 仅保留公开兼容入口与公共行为壳 |
| Home Screen Widgets | 4 | 仅保留公开兼容入口与公共行为壳 |
| Home Screen Quick Actions | 1 | 仅保留公开兼容入口与公共行为壳 |
| Home Screen | 1 | 仅保留公开兼容入口与公共行为壳 |
| Control Center | 1 | 仅保留公开演示回调与公共行为壳 |
| 听音模式（额外） | 1 | 保留已实现的单实例宿主主题、单选语义、48dp 隐形目标、键盘焦点和 SegmentTick；加页面名称 |
| MeloX TabBar（额外） | 1 | 添加宿主页面名称；不改源提取组件、播放器、标签动画和三层采样结构 |

## Android 官方依据与约束

- [Compose 默认无障碍行为](https://developer.android.com/develop/ui/compose/accessibility/api-defaults)：触摸目标、Foundation 默认语义和装饰图标处理。
- [Compose semantics](https://developer.android.com/develop/ui/compose/accessibility/semantics)：paneTitle、dismiss、集合元数据、自定义动作、选择和进度状态。
- [保存 UI 状态](https://developer.android.com/develop/ui/compose/state-saving)：只保存恢复所需的简单 UI 数据。颜色保存 RGBA 数值；动画、焦点请求器、录制层及临时会话不写入 SavedState。

用户要求视觉不变，因此本轮不是 Material 重设计，也不宣称完全符合所有 Android 可访问性建议：部分原稿页明确固定 `fontScale=1`，密集相邻控件的隐形触摸区可能重叠，新增键盘入口没有新增可见焦点环。需要大字号重排、更大实体目标、可见焦点样式或完整模态键盘焦点圈定时，必须先获得改变视觉/行为范围的明确要求，不能自行改稿。

## 防误改与验证

```powershell
node --test tools/android-example-behavior.test.mjs tools/listening-mode.test.mjs tools/melox-tabbar-extraction.test.mjs
node --test tools/figma-reader/native-framework.test.mjs
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
$env:QQ_APK_AUTO_SEND='0'
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
```

视觉契约测试保留现有绘制、尺寸、布局、字体与动画调用的归一化摘要；这是源码防误改，不是像素截图证明。不得重新生成摘要来隐藏未授权的视觉修改。已有提取检查中 `ProgressiveBlurContent.kt` 的未审阅差异仍需独立处理，不刷新原基线。

人工验收：正常触摸应保持原画面；TalkBack 检查标签/选中/禁用/进度、无手势执行左滑三项操作与打开上下文菜单；键盘检查滑块端点和日期逐项变化；旋转/恢复检查颜色模式、自定义颜色、输入值和已选状态；模态关闭后再检查页面访问。用户此前选择自己验证，不能将构建成功或静态检查冒充真机验收。

### 本轮验证结果

- `:verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug`：成功；27 项 JVM 单测（含新增行为数学测试 5 项）零失败。
- Android 行为/视觉契约、听音模式、MeloX 提取三组专项测试：21/21 通过。
- tokens 三个原稿投影哈希一致；MeloX 提取的 66 个文件校验通过。
- 旧 `native-framework.test.mjs`：14/31 通过，原有 17 项失败仍在，未删断言或掩盖失败；滑块进度断言仅迁移到实际公共行为层入口。
- `verify-extraction.mjs`：仍被已有 `ui/commom/ProgressiveBlurContent.kt` 未审阅差异阻塞；未修改该源码或原基线来使它假通过。
- `adb devices -l` 当前无在线设备。未安装本轮 APK，未进行真机截图、TalkBack、键盘或恢复操作验收。
- APK：`sample/build/outputs/apk/debug/sample-debug.apk`。不要将这里的构建验证理解为所有页面已完成像素验收。
