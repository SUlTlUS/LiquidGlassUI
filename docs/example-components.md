# iOS 27 示例组件 API

所有 56 个 iPhone 示例都通过统一包装层调用，入口位于：

`sample/src/main/java/dev/liquidglass/sample/Ios27ExampleComponents.kt`

现有手机示例库本身也已经改为通过这层 API 渲染，所以库内展示和外部调用共用同一套实现。

## 最简单的调用

按稳定 key：

```kotlin
Ios27Example(
    key = Ios27ExampleKeys.TextFieldsDefault,
    onBack = { /* 返回 */ },
)
```

按分类和原稿 variant：

```kotlin
Ios27Example(
    category = "Picker",
    variant = "Inline",
    onBack = { /* 返回 */ },
)
```

`variant` 既可以写 `Inline`，也可以写完整的 `Type=Inline`。

## 推荐：命名组件

不想记 key 时直接使用 `Ios27Examples`：

```kotlin
Ios27Examples.TextFields()

Ios27Examples.Slider()

Ios27Examples.List(
    variant = Ios27ListVariant.Inset,
    onBack = { /* 返回 */ },
)

Ios27Examples.TopToolbar(
    variant = Ios27TopToolbarVariant.LargeTitle,
)

Ios27Examples.Alert(
    variant = Ios27AlertVariant.Stacked,
)
```

目前提供的命名入口覆盖：

- TopToolbar / BottomToolbar / TabBar
- TextFields / Switches / Slider / Stepper
- Sheet / Picker / Menu / List
- Keyboard / FaceId / Notifications / EmptyState
- LockScreen / ContextMenu / Widget
- ColorPicker / HomeScreenQuickActions / ActionSheet / HomeScreen
- Alert / ControlCenter / ActivityView

有多个原稿变体的组件使用强类型 enum 选择，不需要记字符串。

## 通用参数

`Ios27Example` 支持：

```kotlin
Ios27Example(
    key = Ios27ExampleKeys.TopToolbarLargeTitle,
    modifier = Modifier.fillMaxSize(),
    darkTheme = null,        // null = 跟随当前 MaterialTheme
    onBack = { /* 返回 */ },
    onReference = { /* 打开原稿对照 */ },
)
```

包装层会自动提供 iOS 27 页面颜色，并把 Material primary 临时映射到当前浅色/深色 accent。原示例内部状态、玻璃采样、原稿 JSON 和交互实现没有复制。

## Catalog

`Ios27ExampleCatalog.all` 包含全部 56 个 `Ios27ExampleSpec`。

也可以自行查找：

```kotlin
val inset = Ios27ExampleCatalog.byKey(Ios27ExampleKeys.ListInset)

val compactPicker = Ios27ExampleCatalog.find(
    category = "Picker",
    variant = "Compact",
)

val allKeyboards = Ios27ExampleCatalog.variants("Keyboard")
```

`List / Type=Inset` 仍走其独立的 `ListDetailPage` 实现；其余示例通过 `NativeExamplePage`。这一分流已经封装在 `Ios27Example` 内部，调用方不需要处理。
