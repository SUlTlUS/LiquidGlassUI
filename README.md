# LiquidGlass UI

面向 Android 13 及以上版本的 Jetpack Compose 液态玻璃组件库，提供 iOS 26 / 27 风格的材质、高光、按钮运动，以及按钮融合、标签栏、菜单、Popup 和模态组件。使用原生 Android 渲染，无需 Flutter 运行时。

公开 API 统一位于 `dev.liquidglass`，当前库版本为 **2.0.1**。

## 组件能力

| 类别 | 主要入口 |
| --- | --- |
| 主题与效果 | `GlassTheme`、`GlassStandard`、`GlassEffects`、`GlassEffectScope` |
| 背景采样 | `GlassScene`、`GlassBackdropProvider` |
| 按钮与融合 | `GlassButton`、`GlassButtonGroup` |
| 开关与滑块 | `GlassSwitch`、`GlassSlider` |
| 标签栏 | `GlassTabBar`、`GlassTabBarScaffold`、`GlassTab` |
| 标签栏布局与预设 | `GlassTabBarLayout`、`GlassTabBarDefaults`、`GlassTabBarPreset` |
| 多级菜单 | `GlassMorphMenu`、`GlassMenuAction` |
| Popup | `GlassPopup`、`rememberGlassPopupState`、`GlassPopupSourceContent` |
| 模态、对话框与输入 | `GlassModal`、`GlassDialog`、`GlassDialogTextField` |

标签栏支持紧凑布局、收缩、独立操作按钮和播放器插槽，默认使用 MeloX 材质预设。开关、滑块及底栏交互共用实现，材质与高光可独立选择。

## 快速接入

### 1. 生成本地 Maven 仓库

在本仓库根目录运行：

```powershell
.\gradlew.bat packageLibrary --no-daemon --console=plain
```

产物位于 `dist/maven/`，包含 AAR、源码及传递依赖元数据。请将整个 Maven 目录交付给消费者项目；顶层 AAR 不内嵌其余两个库，不能只复制一个 AAR。

### 2. 添加依赖

在消费者项目的 `settings.gradle.kts` 中添加生成的仓库路径：

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("path/to/dist/maven") }
    }
}
```

在应用模块启用 Compose，设置 `minSdk = 33`，并添加依赖：

```kotlin
android {
    defaultConfig { minSdk = 33 }
    buildFeatures { compose = true }
}

dependencies {
    implementation("dev.liquidglass:liquidglass-ui:2.0.1")
}
```

消费者项目还需配置 Compose 编译器插件及兼容的 Kotlin、Compose 和 Java 工具链。本仓库使用的版本见下方构建环境；仓库内宿主直接依赖 `project(":liquidglass-ios27")`。

### 3. 提供背景与控件

以下示例放在 Composable 函数中，`BackgroundContent()` 替换为应用自身的图片或页面背景：

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.liquidglass.*

var checked by rememberSaveable { mutableStateOf(false) }
var value by rememberSaveable { mutableFloatStateOf(0.5f) }

GlassTheme(standard = GlassStandard.Ios27) {
    GlassScene(
        modifier = Modifier.fillMaxSize(),
        background = { BackgroundContent() },
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            GlassButton(onClick = { checked = !checked }) { Text("切换") }
            GlassSwitch(checked = checked, onCheckedChange = { checked = it })
            GlassSlider(value = value, onValueChange = { value = it })
        }
    }
}
```

开关和滑块的业务状态由调用者持有。已有独立背景采样层时，可使用 `GlassBackdropProvider(backdrop) { ... }` 接入。

**采样层级必须独立。** `GlassScene` 只录制背景，玻璃控件位于兄弟层；不要让控件采样包含自身的录制层，否则可能触发递归渲染和崩溃。图片背景、行内控件及 Popup 的接入方式见[背景采样规范](docs/glass-backdrop-integration.md)。

## 选择标准与效果

`GlassTheme(standard = ...)` 切换整体标准。`GlassEffects` 可分别指定材质、高光与按钮拖拽效果，三项为 `null` 时跟随整体标准：

```kotlin
GlassTheme(
    standard = GlassStandard.Ios26,
    effects = GlassEffects(
        highlight = GlassStandard.Ios27,
        buttonDragEffect = GlassStandard.Ios27,
        limitButtonDrag = false,
    ),
) {
    GlassButton(onClick = {}) { Text("打开") }
}
```

上例使用 26 材质、27 高光与按钮运动，并关闭附加拖拽限制。

- **按钮轮廓**：26 拉伸原始轮廓；27 按实时尺寸重画轮廓。轮廓随按钮拖拽效果切换，独立于材质。
- **拖拽限制**：`limitButtonDrag` 默认 `true`，同时作用于两种按钮效果；关闭后取消附加距离衰减和绘制余量限制。
- **局部覆盖**：使用 `GlassEffectScope`，通过 `LocalGlassEffects.current.copy(...)` 保留当前其他设置。
- **浮层动画**：菜单、Popup、子菜单与模态共用 27 动画，不读取按钮拖拽效果。

完整参数、标签栏示例和旧 API 迁移表见[组件库指南](docs/component-library.md)。2.0.1 的输入框参数见[输入组件接入说明](docs/toscreen-input-2.0.1.md)。

## 构建示例应用

当前仓库构建环境：

| 项目 | 配置 |
| --- | --- |
| Android 最低版本 | Android 13 / API 33 |
| Android SDK | compileSdk 37 |
| JDK / JVM 目标 | 21 |
| Gradle Wrapper | 9.7.1 |
| Android Gradle Plugin | 9.3.1 |
| Kotlin Compose 插件 | 2.4.10 |
| Compose BOM（UI / 导航模块） | 2026.06.00 |

配置本机 Android SDK 路径后，在仓库根目录运行：

```powershell
# 示例 APK
.\gradlew.bat :sample:assembleDebug --no-daemon --console=plain

# 本地 Maven 分发仓库
.\gradlew.bat packageLibrary --no-daemon --console=plain
```

示例 APK 位于 `sample/build/outputs/apk/debug/sample-debug.apk`。普通构建和打包不会自动发送文件或操作手机。

示例应用提供按钮、标签栏、Popup、多级菜单及主题等演示，设置页可切换整体标准和单项效果。构建成功仅说明产物生成完成，交互手感、背景采样和视觉效果仍需在设备上验收。

## 目录结构

```text
liquidglass/         共享光学核心与基础控件
melox-tabbar/        复用核心的标签栏渲染与交互
liquidglass-ios27/   公开组件 API、主题及 iOS 27 机制
sample/             示例应用
docs/               接入说明、来源快照、审计与截图
tools/              可复用的提取、生成和检查工具
vendor/             上游来源
dist/maven/         生成的本地 Maven 仓库
.tmp/               临时输出（不提交）
```

高层 UI 模块依赖导航模块与核心，导航模块依赖核心。三个发布坐标分别为 `liquidglass-ui`、`liquidglass-navigation`、`liquidglass-core`，由 Maven 元数据传递依赖，共享同一套光学核心。

模块名与历史文件名保留部分来源标记；新代码以 `dev.liquidglass` 的公开入口为准。临时产物和目录归属约定见[临时输出说明](tools/TEMP_OUTPUTS.md)。

## 文档与许可

- [组件库接入、参数与迁移](docs/component-library.md)
- [背景采样集成](docs/glass-backdrop-integration.md)
- [iOS 26 / 27 合并来源与审计](docs/ios27/README.md)
- [MeloX 来源与集成](docs/melox-tabbar.md)
- [项目修改约束](AGENTS.md)

UI 与导航模块使用 GPL-3.0，核心发布声明使用 Apache-2.0；各模块及其资源目录保留对应许可证和来源信息。分发时请一并保留：[UI 许可](liquidglass-ios27/LICENSE)、[导航许可](melox-tabbar/LICENSE)、[核心许可](liquidglass/src/main/resources/META-INF/liquidglass-core/LICENSE)。

`docs/*-extracted/` 和 `vendor/` 中的原始来源用于溯源。历史文档可能含旧入口或旧行为描述，当前接入以公开 API、构建配置及组件库指南为准。
