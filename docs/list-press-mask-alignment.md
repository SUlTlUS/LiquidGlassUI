# 列表点按遮罩：以分隔线中心为边界

2026-10-08，按用户“list 单按遮罩的上下边界没有和分割线中间对齐”修正。此条只修正遮罩几何，不替换原 iOS/Figma 或 Android 行为规范。

## 固化规则

分隔线保持原来的画法：下一行局部坐标 `y=0..t`，其中心为 `t/2`。遮罩以前直接使用行的 `0..height`，因此错了半条线宽。

现在每行遮罩的上下边界由真正可见的相邻分隔线决定：

- 上方有线：顶部 `t/2`；否则顶部 `0`。
- 下方有线：底部 `height+t/2`；否则底部 `height`。
- 第一行顶部、最后一行底部、单行分组不外扩。隐藏了分隔线的左滑相邻边界也不应用半线偏移。
- 使用实际 `SeparatorRenderHeight`，不是逻辑 `SeparatorHeight`。当前渲染厚度为 2dp，因此偏移为 1dp；随页面实际 Density 换算为像素，不能硬编码 1px。

相邻遮罩在同一条线的中心相接；中间行的遮罩高度不变。分隔线仍在原位置，行高、内容 padding、颜色、圆角半径和点击命中区域不变。

## 复用入口

- `Ios27ListRows` 自动向整行提供 `LocalIos27RowPressMaskEdges`。`Ios27ListRow` 已自动接入，无需调用者计算偏移。
- 自定义整行操作使用 `Modifier.ios27ListPressedClickable(shape, onClick=...)`，例如 Inset 的底部 Action。
- 行内日期胶囊、星标、步进器等小控件仍使用原 `ios27PressedClickable`，不能套整行的遮罩边界。
- 单独绘制的 Sectioned 行显式传入 `Ios27PressMaskEdges`，因为它的原稿第一行也有分隔线。
- Full Width 保留自己的按压/左滑状态，用 `ios27ListPressMask` 在文字图标后方绘制。外层不能先裁切遮罩；静止行底色由原纯色列表父层提供，避免下一行的重复实色遮住半条线外扩。任何左滑进度大于零时，移动行仍绘制不透明表面，按钮不会从行内透出。

普通控件原接口保留了兼容重载。原 Press/Release/Cancel 处理与 120ms 短按淡出不变。没有改受保护的玻璃运行目录或原始提取基线。

## 变更追踪

下表记录本轮开始前与修改后文件 SHA-256，不代表 Git HEAD 基线，也不包含此前用户已存在的改动。

| 文件 | 修改前 SHA-256 | 修改后 SHA-256 |
| --- | --- | --- |
| `Ios27Lists.kt` | `883b06ce62989c5ef6036733def0fb617e0dc859eb88f3bb2bc777d57f3926b7` | `45cbf7388e2b0a9829555afcba68648735ca780e57a2f5aaeab0b61071755a43` |
| `Ios27PressFeedback.kt` | `a7836ad59357ab3c3656a2352cd1b79f2c13a889edd5f93b245487515d457071` | `d76bcbec4b1adea85442b9764b18501d7882e38edfe8613f3b8b06faaa056b5f` |
| `ListDetailPage.kt` | `bfa9bad25a0bff3eea32e5a7ba2189101b60cdee6cac1ac812c3c44a49ac0a09` | `3b18320a91a658609e658cd13d142d2a7dafb48c726eec1c2b7dad2113dbfd7a` |
| `SectionedListExample.kt` | `9f8a0716453546cdf36fbddfd4617d67164b5213217cea57c3742254ba33923c` | `3acf23dca1845bff5b87bf1a858fc69181ab9694d1b5b05f8fe6d2217cd649a6` |
| `FullWidthListExample.kt` | `afb5324e58c777996ed48bfa064861abba64ceeda27f6f93098f2ed953a78176` | `59a56d085f1ef3959022d6ad056ce4d6899e5d314b75448a07630ae4d5cdc9c3` |

## 验证

```powershell
node --test tools/list-press-mask.test.mjs tools/android-example-behavior.test.mjs tools/listening-mode.test.mjs tools/melox-tabbar-extraction.test.mjs
$env:QQ_APK_AUTO_SEND='0'
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:assembleDebug --no-daemon --console=plain
```

新增 JVM 测试覆盖不同像素密度、52/68dp 行、首尾与单行组、隐藏分隔线，以及渲染厚度和逻辑线宽的区别。静态检查固定分隔线原绘制位置、原短按动画及 Full Width 的裁切顺序。

`android-example-visual-contract.json` 原哈希不刷新。测试只对用户明确授权的 Full Width 遮罩绘制迁移做精确代码投影，其他绘制、规范、资产和玻璃源码仍匹配原契约。

构建/数学/源码检查不等于真机按压截图验收；当前 `adb devices -l` 无在线设备，用户此前选择人工验证。旧提取检查的 `ProgressiveBlurContent.kt` 未审阅差异仍存在，不通过改原基线掩盖。

本轮最终验证：构建成功，32 项 JVM 单测零失败（含边界测试 5 项），26 项专项静态检查通过，tokens 哈希匹配。旧框架回归仍为 14/31 通过、17 项旧失败，未改变原失败集合。APK 位于 `sample/build/outputs/apk/debug/sample-debug.apk`，尚未安装到设备。
