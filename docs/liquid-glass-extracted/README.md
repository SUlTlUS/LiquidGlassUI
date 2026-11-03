# LiquidGlass 原样代码提取包

这里保存的是从 `app/src/main/java/com/zeaze/tianyinwallpaper/` 逐文件复制的 LiquidGlass 相关 Kotlin 源码，保留原包名、imports、注释、API guard 和实现顺序。提取包不是重写版本；源项目中的对应文件仍是运行时的唯一权威版本。

## 内容

- `source/backdrop/`：完整底层实现，共 27 个 Kotlin 文件。
- `source/catalog/components/`：LiquidButton、LiquidButtonGroup、LiquidBottomTabs、LiquidSlider、LiquidToggle、LiquidSegmentedSelector、StripedGlass 及 fallback，共 11 个文件。
- `source/catalog/utils/`：DampedDragAnimation、InteractiveHighlight、AdaptiveLuminanceGlass、LiquidMorph、SdfShader 等辅助，共 8 个文件。
- `source/utils/LiquidGlassPrefs.kt`：API 33 能力判断和持久化开关。

- `source/ui/commom/ProgressiveBlurContent.kt`：顶部 128dp 渐进模糊，4dp blur + AlphaMask；在 1.0.3 中从原项目补齐。

总计 48 个文件。复制时没有改写内容，已用 SHA-256 与源文件逐一核对。

## 复用方式

1. 先复制 `source/backdrop/`，再复制需要的 components/utils；保留目录关系。
2. 将包名从 `com.zeaze.tianyinwallpaper` 改成新 App 包名时，只对新项目副本做包名迁移；不要回写本提取包。
3. 保留 `io.github.kyant0:backdrop:2.0.1`、`io.github.kyant0:shapes:1.2.1` 和 Kotlin `-Xcontext-parameters` 配置。
4. `LiquidGlassPrefs.kt` 依赖应用自己的 `App.TIANYIN` 偏好名称；新 App 应接入自己的 SharedPreferences 名称，但不要改变 `isSupported` 的 API 33 判断。
5. 从页面根部创建 `rememberLayerBackdrop` 并用 `layerBackdrop` 录制内容，再把 `Backdrop` 向下传给控件；不能只复制控件文件。

完整的架构、绘制顺序、API 兼容矩阵和接入注意事项见 [`../liquid-glass-implementation-guide.md`](../liquid-glass-implementation-guide.md)。

## 原始文件映射

提取包 `source/` 后的相对路径与应用源目录 `app/src/main/java/com/zeaze/tianyinwallpaper/` 后的相对路径完全一致，例如：

```text
source/backdrop/DrawBackdropModifier.kt
    -> app/src/main/java/com/zeaze/tianyinwallpaper/backdrop/DrawBackdropModifier.kt

source/catalog/components/LiquidButtonCore.kt
    -> app/src/main/java/com/zeaze/tianyinwallpaper/catalog/components/LiquidButtonCore.kt
```

`source/` 仍是不可变原样快照；当前可构建 module 的副本位于 `liquidglass/src/main/java/com/zeaze/tianyinwallpaper/`。修改外围页面时，禁止直接改这些 47 个文件；使用 `dev.liquidglass.ios27` 下的页面、列表和 token API。运行 `node tools/verify-extraction.mjs` 可以确认原样代码没有漂移。
