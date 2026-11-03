# iOS 27 示例架构

本目录的原生示例统一遵守一个原则：

> **内容层负责被采样，玻璃层必须作为 sibling overlay 存在，不能把玻璃本身录进它要采样的 backdrop。**

## 1. 页面层级

### 1.1 402pt 原稿页

用于绝大多数从 iPhone 原稿导出的示例。

```
ExampleDesignPage
├─ recorded content layer
│  ├─ background
│  ├─ page content
│  └─ LayerBackdrop recorder
└─ overlays
   ├─ ProgressiveBlurContent
   ├─ ExampleNavBar / custom toolbar
   ├─ menu
   ├─ tab bar / bottom toolbar
   └─ dialogs / sheets
```

优先使用：

- `ExampleRecordedDesignPage`：只有内容层 + 自定义 overlay。
- `ExampleToolbarDesignPage`：标准顶部渐进模糊 + `ExampleNavBar`。
- `ExampleDesignOverlay`：只需要 402pt 坐标系、不需要重新建立背景或录制层的 modal overlay。

不要再在单个示例里复制：

- `BoxWithConstraints + Density(width / 402f)`
- `rememberLayerBackdrop { drawRect(...); drawContent() }`
- `ExampleBackdropLayer + ProgressiveBlurContent + ExampleNavBar`

### 1.2 响应式应用页

用于设置页、根列表等非 Figma 402pt 页面。

使用：

- `ExampleRecordedResponsivePage`

它统一处理：

- 页面背景
- safeDrawing 的左右/底部 inset
- page backdrop
- content / overlay sibling 顺序

### 1.3 Full-bleed 系统场景

用于：

- Control Center
- Home Screen
- Home Screen Widgets
- Home Screen Quick Actions

使用：

- `ExampleFullBleedDesignPage`

这些页面本身就是完整设备可见区域，不应套标准 list/page scaffold。

## 2. 顶部工具栏

标准顶部工具栏统一由：

- `ExampleTopChrome`
- `ExampleNavBar`

组成。

`ExampleTopChrome` 固定保证：

1. Progressive blur 先画；
2. toolbar 后画；
3. 两者都在 recorded layer 外。

原稿按钮 glyph 统一由：

- `SceneToolbarButtonContent`

渲染。

不要再在其他页面自行遍历 `SceneToolbarButton.texts`。

ListDetail 等特殊页面可以保留自定义按钮布局，但 glyph 内容仍应走 `SceneToolbarButtonContent`。

## 3. Backdrop 录制

统一入口：

- `rememberExamplePageBackdrop(surface)`
- `ExampleBackdropLayer`

规则：

- background 必须先画进 backdrop；
- glass overlay 永远不能放进 `layerBackdrop()` 子树；
- Popup/Picker 如果需要独立采样源，使用单独的 `LayerBackdrop`，不要复用主 recorder 造成递归。

## 4. 列表

列表类示例统一使用：

- `Ios27PageContent`
- `Ios27ListSection`
- `Ios27ListRows`
- `Ios27ListRow`

只有原稿明确是 full-width、sectioned 或特殊 swipe geometry 时，才由专用组件实现：

- `FullWidthListExample`
- `SectionedListExample`
- `ListDetailPage`

不要再手写另一套普通 inset list 容器。

## 5. 文本输入与选择菜单

普通输入：

- `Ios27EditableTextField`

整页可编辑：

- `Ios27MenuEditablePage`

文本选择菜单 provider：

- `Ios27TextContextMenuHost`

选择菜单玻璃：

- `Ios27TextSelectionMenu`
- `Ios27TextSelectionExpandedMenu`

Menus 是一个合理特例：`TextContextMenuProvider` 必须包住编辑内容和 selection overlay，所以它可以继续使用底层 `ExampleDesignPage + ExampleBackdropLayer + ExampleTopChrome` 组合，而不强行套高层 toolbar scaffold。

## 6. Modal / Sheet

通用 dialog 层级：

```
LiquidDialogLayer
├─ Scrim
└─ Panel glass
   └─ Content
```

标准弹窗：

- `LiquidDialog`

底部 sheet：

- `Ios27BottomSheetDialog`

当前复用 `Ios27BottomSheetDialog` 的页面：

- Color Picker
- Sheets / Fullscreen
- Sheets / Inspector

Fullscreen Stack 是原稿定义的独立 opaque stack，不应为了“统一”强行套 glass sheet。

## 7. Menu

菜单玻璃材质统一由：

- `menuGlass()`

使用者：

- 原稿 Edit Medium
- 原稿 Menu
- Context Menu
- Text selection menu

菜单内容和几何可以不同，但不要复制另一套 blur/lens/highlight/shadow 配方。

## 8. Bottom Chrome

底部导航：

- `Ios27TabBarExample`

底部工具栏：

- `Ios27ToolbarBottomExample`

它们都必须作为 recorded content 的 sibling overlay，不能嵌进主 `LayerBackdrop`。

## 9. 当前页面映射

| 示例类型 | 页面壳 |
| --- | --- |
| Root / Settings / Notes | `ExampleRecordedResponsivePage` 或响应式根壳 |
| Category list | `ExampleToolbarDesignPage` |
| Standard native examples | `ExampleDesignPage` + shared backdrop/chrome primitives |
| Menus | `ExampleDesignPage` + `Ios27TextContextMenuHost` 特例 |
| List detail | `ExampleRecordedDesignPage` + custom toolbar overlay |
| Control Center / Home Screen 系列 | `ExampleFullBleedDesignPage` |
| Alert / Activity View | `LiquidDialog` |
| Color Picker / Fullscreen / Inspector | `Ios27BottomSheetDialog` |
| Action Sheet / Activity overlay | `ExampleDesignOverlay` |
| Tab Bar / Bottom Toolbar | standard page + sibling bottom chrome |

## 10. 新增示例时的顺序

1. 先判断它属于 responsive、402pt、full-bleed、modal 还是 overlay。
2. 选已有 scaffold，不要先写新的 `BoxWithConstraints`。
3. 内容层只画内容并负责 backdrop recording。
4. blur / toolbar / menu / sheet / tab bar 放 sibling overlay。
5. 原稿 toolbar glyph 用 `SceneToolbarButtonContent`。
6. 普通列表用共享 list primitives。
7. 只有原稿几何确实不同，才新增专用组件。
