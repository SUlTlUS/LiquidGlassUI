# 将 LiquidGlass27 合并进 LiquidGlass Sketch

源目录：`C:\IDE\Android\liquidglass27`  
目标目录：`C:\IDE\Android\liquidglass_sketch`  
核对日期：2026-11-03

推荐在目标仓库新增 `:flclash-glass` 机制库和 `:flclash-demo` 验证应用，然后逐页接入现有 `:sample`。保留 Sketch 的背景采样、模糊、折射和材质，迁入 FlClash 的几何、高光、动画、交互、菜单、弹窗及对应性能优化。

本指南描述待执行的合并步骤；编写指南时没有修改目标项目。第一阶段完成模块迁入，第二阶段完成现有应用接入。

## 1. 当前关系与合并边界

源项目已经直接使用目标的玻璃库：

```kotlin
// liquidglass27/settings.gradle.kts 当前配置
include(":app", ":sketch-glass")
project(":sketch-glass").projectDir = file("../liquidglass_sketch/liquidglass")
```

需要迁入的是 `dev.liquidglass27.glass` 机制层，Sketch 核心不需要复制第二份。当前机制层没有导入源应用的 `R`、`BuildConfig` 或 `demo` 包，可以独立提取。

```text
第一阶段
:flclash-demo  →  :flclash-glass  →  :liquidglass
:sample       →  :liquidglass、:melox-tabbar（保持现有关系）

第二阶段，完成 API 24 兼容入口后
:sample       →  :flclash-glass  →  :liquidglass
```

不要让 `:liquidglass` 反向依赖 `:flclash-glass`。现有 `dev.liquidglass.ios27` 位于 `:liquidglass` 内，需要调用新机制的适配器先放在 sample 或新机制库中，避免循环依赖。

| 配置 | 源项目 | 目标项目 | 合并处理 |
| --- | --- | --- | --- |
| AGP / Compose 编译器插件 | 9.3.1 / 2.4.10 | 相同 | 复用目标根配置 |
| Gradle Wrapper | 9.7.1 | 9.7.1 | 不复制 Wrapper |
| compileSdk / targetSdk | 37 / 36 | 37 / 36 | 保持 |
| minSdk | 33 | 库与 sample 均为 24 | 第一阶段隔离；第二阶段增加 API 33 入口 |
| Java 字节码配置 | 源 app 为 17 | 目标为 21 | 新模块统一 Java / Kotlin JVM 21，使用 JDK 21 构建 |
| Compose 依赖 | BOM 2026.06.00、Material3 | Foundation / Material 1.11.2 | 保留 BOM，并检查最终依赖解析 |
| Sketch 依赖名 | `:sketch-glass` | `:liquidglass` | 新库引用 `project(":liquidglass")` |
| 页面主题 | `GlassTheme` / Material3 | `Ios27PageTheme` / Material | 局部适配，不替换全局主题 |

目标仓库当前有大量已暂存、未暂存和未跟踪内容。开始前检查 `git status --short`，单独记录本次文件清单；不要清理工作区、整目录覆盖、自动提交原有改动或使用 `git add .`。

目标 `AGENTS.md` 的约束继续适用：

- `liquidglass/src/main/java/com/zeaze/tianyinwallpaper/` 默认只读；`docs/liquid-glass-extracted/source/` 保留原始基线。
- 如确需修改原玻璃源码，按授权文件范围修改，并更新 `docs/liquid-glass-merged-files.json` 中的原因和前后 SHA-256；不刷新基线掩盖差异。
- 不手工修改生成的 `Ios27Tokens.kt`、Figma JSON 来适配新控件。
- “手机示例库”继续默认进入 `NativeExamplePage`；新验证应用不替换这一入口。

## 2. 文件迁移清单

先保留包名 `dev.liquidglass27.glass`。包名整理放在行为验证之后，避免同时改源码、测试路径和生成器。下表左侧相对源根目录，右侧相对目标根目录。

| 源 | 目标 | 处理 |
| --- | --- | --- |
| `app/src/main/java/dev/liquidglass27/glass/` | `flclash-glass/src/main/java/dev/liquidglass27/glass/` | 完整迁入当前 69 个 Kotlin 文件 |
| `app/src/main/assets/shaders/` | `flclash-glass/src/main/assets/shaders/` | 迁入全部 5 个 AGSL，保留路径 |
| `app/src/test/java/dev/liquidglass27/glass/` | `flclash-glass/src/test/java/dev/liquidglass27/glass/` | 完整迁入测试，调整必要的路径定位 |
| `app/src/main/java/dev/liquidglass27/demo/` | `flclash-demo/src/main/java/dev/liquidglass27/demo/` | 两个演示文件一起迁入 |
| `app/src/main/java/dev/liquidglass27/MainActivity.kt` | `flclash-demo/src/main/java/dev/liquidglass27/MainActivity.kt` | 修改预热入口，见第 4 节 |
| `app/src/main/AndroidManifest.xml`、`app/src/main/res/` | `flclash-demo/src/main/AndroidManifest.xml`、`flclash-demo/src/main/res/` | 只用于验证应用，Activity 改为完整类名 |
| `tools/check_control_highlight.py` | `tools/flclash/check_control_highlight.py` | 用 `--shader` 指定新路径 |
| `tools/generate_glass_glyphs.py` | `tools/flclash/generate_glass_glyphs.py` | 修正 ROOT、SOURCE、TARGET |
| `docs/verification-2026-11-03.md` | `docs/flclash/verification-2026-11-03.md` | 保留人工验收清单 |
| `plan.md`、`docs/flclash_popup_port_coverage_2026-11-02.md` | `docs/flclash/` | 迁入基线记录，不替换目标主计划 |
| `artifacts/20261103-retained-rendering/validation.json`、同目录 `README.md` | `docs/flclash/baseline/` | 原项目验证记录，修正引用路径 |
| `FlClash/LICENSE`、来源说明 | `flclash-glass/LICENSE`、`docs/flclash/provenance.md` | 保留许可证、作者及来源 |
| `FlClash/lib/ui/icons/app_glyphs.dart` | `tools/flclash/reference/app_glyphs.dart` | 固定图标生成器输入，记录来源 |

五个资产为 `flclash_control_highlight.agsl`、`flclash_glass.agsl`、`flclash_glass_compat.agsl`、`sketch_contrast_tint.agsl`、`sketch_goo_lens.agsl`。首次迁移全部保留；不代表五者都在当前 Sketch 路径执行。暂不顺手删除实验分支和对应测试。

不要复制源根 settings、根构建配置、`local.properties`、`.gradle/`、`.codegraph/`、`build/`、Python venv 或整个 `artifacts/`。`FlClash/` 不是运行时模块，不需要作为嵌套 Git 仓库整体迁入。源 app 的 QQ 自动发送任务不迁入新模块。

源项目根目录目前不是 Git 仓库，不能直接对它执行 `git merge`。按清单迁入文件，保存来源路径、日期和 SHA-256。FlClash 参考 HEAD 为 `7fd417fb9a9a277c6a9339a8ac87f907df7c25f7`；另外记录 Compose 移植文件哈希，单记上游 commit 不能标识这份本地移植。

## 3. 第一阶段 Gradle 配置

在目标 `settings.gradle.kts` 末尾新增，保留现有模块：

```kotlin
include(":flclash-glass", ":flclash-demo")
```

新建 `flclash-glass/build.gradle.kts`：

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
}
android {
    namespace = "dev.liquidglass27.flclash"
    compileSdk = 37
    defaultConfig { minSdk = 33 }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
}
dependencies {
    api(project(":liquidglass"))
    api(platform("androidx.compose:compose-bom:2026.06.00"))
    api("androidx.compose.ui:ui")
    api("androidx.compose.ui:ui-graphics")
    api("androidx.compose.foundation:foundation")
    api("androidx.compose.animation:animation")
    api("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
}
```

Material Android 依赖供 `GlassTheme` 使用 HCT 算法，不是可以随手删除的旧 View UI 依赖。沿用目标 AGP 9 内置 Kotlin 模式，不额外添加旧 `kotlin-android` 插件。库 `src/main/AndroidManifest.xml` 使用空 `<manifest />`，不放 application 或启动 Activity。

新建 `flclash-demo/build.gradle.kts`：

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}
android {
    namespace = "dev.liquidglass27"
    compileSdk = 37
    defaultConfig {
        applicationId = "dev.liquidglass.sketch.flclashdemo"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
    }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
}
dependencies {
    implementation(project(":flclash-glass"))
    implementation("androidx.activity:activity-compose:1.13.0")
}
```

验证应用使用独立 applicationId，与源 `dev.liquidglass27`、目标 `dev.liquidglass.sample` 并存。Manifest 使用 `android:name="dev.liquidglass27.MainActivity"`，保留 `adjustResize`、透明系统栏、边到边布局和资源主题。

这些是迁移模板，仍需在目标工程构建验证。检查最终 Compose UI / Foundation / Material / Material3 版本，不用 `force`、全局 exclude 或 `enforcedPlatform` 掩盖冲突。

## 4. 拆模块时必须处理的 internal 入口

`GlassShaderPrograms` 当前为 internal，原 MainActivity 与它同属 app，提取为库后不能继续直接 import。在新库 `dev.liquidglass27.glass` 中增加窄入口：

```kotlin
package dev.liquidglass27.glass

import android.content.Context

fun prepareFlClashGlass(context: Context) {
    GlassShaderPrograms.prepare(context.applicationContext)
}
```

验证应用在 `setContent` 前调用 `prepareFlClashGlass(applicationContext)`。不将 shader 缓存整体改为 public，也不为每个按钮建立初始化状态。第二阶段的低系统兼容要覆盖这个入口。

`GlassShowcase.kt` 还调用 `SourceContentPolicy.kt` 中的 `internal fun canUseSourceContentFrame`。首次提取可只把这个函数改为 public，保持签名和实现，记录为迁移适配；其他 internal 符号维持原状。后续演示页采用 `GlassPopupBox` / `GlassPopupSourceContent` 公开接口后再收回暴露面。不要复制一份判断逻辑到 demo，也不要使用 friend paths 绕过生产模块边界。

两个 demo 文件同属新验证应用，`GlassVerificationPage` 继续保留 internal。

## 5. 采样、材质和高光接入

最小 Compose 接法，`MyBackdrop` 表示宿主背景内容：

```kotlin
GlassScene(
    modifier = Modifier.fillMaxSize(),
    sketchMaterial = true,
    background = { MyBackdrop() },
) {
    ElasticGlassButton(
        modifier = Modifier.size(56.dp),
        style = GlassStyle.regular(dark = false),
        onClick = { /* 业务动作 */ },
    ) { Text("Glass") }
    // 菜单、Popup、Dialog 放在普通内容之后。
}
```

`GlassScene.sketchMaterial` 默认是 **false**，必须显式传 true，或增加固定传 true 的薄场景封装。

`GlassScene` 当前自己建立 Sketch 背景与材质缓存，没有公开“直接传入现有 Backdrop”的参数。初期保留整页场景；新控件放进任意旧页面不会自动取得它的采样源。如需复用目标页面已有 Backdrop，在新库增加场景适配入口，接收该 Backdrop，提供 `LocalSketchGlassBackdrop` 和 `LocalSketchMaterialCache`，并处理场景坐标与公共状态；不向业务层暴露全部 internal locals。

正确层级：

```text
场景根
├─ 背景录制层：底色、图片及确实需要被采样的普通内容
├─ 常规玻璃控件与 Dock
└─ 菜单 / Popup / Dialog
   ├─ 根菜单自身录制层
   ├─ 二级菜单：读取背景及根菜单
   └─ 后续层：只读取它之前的层
```

先阅读目标 `docs/glass-backdrop-integration.md`。禁止让包含玻璃的父层录制成该玻璃输入，也不把整张图片缩放到控件尺寸冒充局部采样。普通文本能否被采样取决于是否进入上游录制层；只录制壁纸不等于录制整个页面。

| 行为 | 合并要求 |
| --- | --- |
| Sketch 光学 | `SketchMaterialCache` 调用原 blur / lens / colorControls / vibrancy，保留顺序 |
| 当前参数 | 菜单 blur 12dp、lens 24/48dp、saturation 1.15；普通控件 blur 2dp、lens 12/24dp、vibrancy。这是当前 Sketch 配置，不是 FlClash 原材质 |
| 控件高光 | `FlClashControlHighlight` 与 AGSL 成对迁入，作用于一次 Sketch 材质结果 |
| 菜单 / 弹窗边缘 | 保留 `GlassMenuRim` 原生多层绘制，不改为普通描边 |
| 双高光 | 新桥接内部关闭 Sketch 默认高光；不要修改旧 Sketch 控件全局默认值，也不额外叠加目标陀螺仪高光 |
| 动态轮廓 | 保留 `SketchGlassShape` 的矩形内部 outline，外部实时 FlClash 路径负责裁切 |
| 阴影 | 保留独立阴影层。旧控件 `drawBackdrop(...).clip(shape)` 与新桥接实时裁切各有作用，不机械交换 modifier |
| 菜单锚点 | 根坐标像素，不是 dp；记录静止布局，先于弹性绘制变换。场景未铺满根窗口时检查偏移 |
| 回源快照 | 只捕获源按钮前景内容，不含玻璃、阴影或背景 |
| 弹窗层级 | 保留同一 Compose 场景内覆盖，不随手替换成 Android Dialog / PopupWindow |

## 6. 性能优化必须成组迁入

以下均在当前源实现中。只复制公开菜单函数会遗漏优化，并可能重新引入“刚从按钮展开就固定位置卡顿”。

| 文件 / 组合 | 保留机制 | 避免回退 |
| --- | --- | --- |
| `SketchMenuRootSurface.kt`、`GlassMenu.kt`、`GlassSubmenu.kt`、`MenuRouteState.kt` | 动画状态在 draw / measure / graphicsLayer 读取，共享当帧几何；`lastRenderedFrame` 为普通快照字段 | 每帧重组整棵菜单，将绘制结果写回可观察状态 |
| `FlClashControlHighlight.kt` | renderer 独占可变 shader，相同输入复用 effect 与几何 | 全局共享可变 RuntimeShader、重复上传不变参数 |
| `GlassMenuRim.kt`、`GlassMenuRimPaths.kt` | 缓存完整 Android Picture，保留九层边缘与外暗线 | 只缓存 Path，仍每帧重建所有边缘绘制对象 |
| `GlassShaderPrograms.kt` | 源码缓存、后台预热，预热 shader 交给单一拥有者 | 首开菜单同步读文件、编译所有 shader |
| `SketchMaterialCache.kt`、`GlassScene.kt` | 场景级 32 项滤镜缓存，区分尺寸、半径、密度、字体缩放、方向和类型 | 预模糊截图替代实时采样，丢失缓存键 |
| `GlassCornerCurveCache.kt`、`GooRowPathCache.kt`、`GooPairOutline.kt`、`GooRowOutline.kt`、`GlassRadialProfile.kt` | 曲线、相邻轮廓与距离计算复用 | 降低轮廓精度或更改高光公式换速度 |
| `MenuLabelWidthCache.kt`、`MenuLazyRows.kt`、`MenuScrollConflator.kt` | 文本测量缓存、指定平铺长菜单惰性构建、滚动输入合并 | 每个指针事件排队大量工作，所有菜单强制 LazyColumn |
| `SketchLayerOrder.kt` 与各层 backdrop | 依赖按层向前组合 | 上层采样包含自己的整个 Popup 宿主 |

保留 `GlassMenu.kt` 匿名对象成员 `@Composable fun BoxWithConstraintsScope.Content()` 的组织方式。该处曾因巨大捕获 lambda 在真机出现 ART `VerifyError`；Kotlin 编译通过不能证明改回大 lambda 安全。

`capturePlannerEnabled`、`gpuResidentAtlas` 等实验选项不是 Sketch 性能开关。当前 Sketch 模式旁路这些路径，不要为“移植全部优化”强行启用另一套采样。

## 7. 第二阶段接入 sample 并保留低版本

第一阶段新库 minSdk 为 33，不能直接依赖到 minSdk 24 的 sample。不要用 `tools:overrideLibrary` 绕过 Manifest 检查，也不直接将整个目标工程提高到 33。

保留 API 24 支持的步骤：

1. 为新库 API 33 图形入口和相关调用链添加准确的 `@RequiresApi(33)`，包括预热、场景和渲染类型；检查静态初始化是否提前创建 `RuntimeShader` 等对象。
2. 宿主采用一个明确的 API 分支：33+ 进入新机制，24–32 继续走已有普通控件或旧页面。API 33 专属实现集中到独立文件 / 对象，低版本启动不初始化它。
3. 完成隔离后将新库 minSdk 降至 24，再给 sample 增加 `implementation(project(":flclash-glass"))`。可安装范围扩大不表示液态玻璃在 24–32 上可用。
4. 执行 Lint 并修复 API 调用链，不全局压制 `NewApi`。在 24–32 真机验证原入口与降级，在 33+ 验证新机制；无旧设备则明确留待验证。

只在宿主边界分流，不为每个内部函数增加版本判断、吞异常或多层兜底。

| 目标用途 | 新入口 | 接入重点 |
| --- | --- | --- |
| 玻璃容器 | `LiquidGlass` / `GlassPane` | 背景源、布局尺寸、内容 slot |
| 弹性按钮 / 融合组 | `ElasticGlassButton` / `GooGlassRow` | 回调、禁用状态、语义、触觉 |
| 可拖动底栏 | `GlassNavigationDock` | 原导航状态、安全区及点击结果 |
| 展开菜单 | `GlassMorphMenu` | 静止锚点、按压接力、前景快照、多级与长列表 |
| 通用 Popup | `GlassPopupState<T>` / `GlassPopupBox` / `GlassPopupSourceContent` / `GlassPopup` | 挂载、关闭动画、结果、焦点与模态 |
| 弹窗 | `GlassAlert` / `GlassDialog` | 输入框、危险动作、长内容与业务状态 |
| 主题 / 图标 | `GlassTheme` / `GlassGlyphs` | 局部 Material3 主题，保留页面 tokens |

同名组件按实际 import 区分，必要时使用 import alias，不批量替换。目标 Slider、Toggle、MeloX TabBar、ColorOS 组件没有一一对应的新替代物，继续复用。

先增加“FlClash 机制”独立入口，再按按钮、Goo、Dock、菜单、Dialog / Popup 顺序接入实际页面。`NativeExamplePage` 的 Figma 尺寸与列表规范不自动改成演示页布局；每页明确采用哪套外观。继续使用状态上提、Modifier、slot、回调等 Compose 接口。

## 8. 测试和构建

### 路径与生成器适配

当前基线有 324 个单元测试。部分测试读取 `src/main/java/dev/liquidglass27/glass/...` 或 `src/main/assets/shaders/...`，另有硬编码 `app/...` 回退路径。包路径不变时，模块工作目录的 `src/...` 读取应继续有效；将根目录回退的 `app` 改为 `flclash-glass`，确认实际读取新文件。必要时在新库测试任务显式设置 `workingDir(projectDir)`。不要跳过测试，或继续读取原仓库后宣称迁移通过。

迁入 `tools/flclash/generate_glass_glyphs.py` 后调整：

```python
ROOT = Path(__file__).resolve().parents[2]
SOURCE = ROOT / 'tools/flclash/reference/app_glyphs.dart'
TARGET = ROOT / 'flclash-glass/src/main/java/dev/liquidglass27/glass/GlassGlyphs.kt'
```

固定输入旁记录上游 commit、原始路径及许可证。`--check` 校验 124 个图标，不手工改 `GlassGlyphs.kt` 来消除差异。高光脚本已有 `--shader` 参数，无需改变公式。

### 在目标根目录执行

```powershell
Set-Location 'C:\IDE\Android\liquidglass_sketch'

# 第一阶段：新库与独立验证应用
.\gradlew.bat :flclash-glass:testDebugUnitTest :flclash-glass:lintDebug :flclash-demo:lintDebug :flclash-demo:assembleDebug --no-daemon --console=plain

# 核对最终 Compose 版本
.\gradlew.bat :flclash-glass:dependencyInsight --configuration debugRuntimeClasspath --dependency androidx.compose.ui --console=plain
.\gradlew.bat :flclash-glass:dependencyInsight --configuration debugRuntimeClasspath --dependency androidx.compose.foundation --console=plain

# 目标原有约束与产物检查
node tools/verify-extraction.mjs
node tools/generate-ios27-tokens.mjs --check
node --test tools/figma-reader/native-framework.test.mjs
.\gradlew.bat :verifyLiquidGlassSpec :liquidglass:testDebugUnitTest :sample:lintDebug :sample:assembleDebug :verifyDebugArtifacts -x :sample:sendDebugApkToQQ --no-daemon --console=plain

# 迁入的生成器与实际 AGSL 回归
python tools/flclash/generate_glass_glyphs.py --check
python tools/flclash/check_control_highlight.py --shader flclash-glass/src/main/assets/shaders/flclash_control_highlight.agsl
```

命令逐条检查退出码，失败后处理原因再继续。高光脚本需要 `skia-python` 和 `numpy`；源基线使用 144.1 / 2.4.6，可在独立工具虚拟环境复现，不复制旧 venv。脚本验证特定高光断点，不代替手机视觉验收。

`-x :sample:sendDebugApkToQQ` 关闭目标已有发送 finalizer；命令显式执行它原先关联的规范和产物检查。新模块不增加发送任务，Release 验证也排除对应发送任务。

第二阶段 sample 增加依赖后，重新执行 sample 依赖解析、Lint、构建和产物检查。修改 Figma 解析器才追加 `node --test tools/figma-reader/examples.test.mjs` 与资源重生成，纯机制迁入不需要重写 Figma 资产。

输出与检查：

- `flclash-glass/build/outputs/aar/flclash-glass-debug.aar`：包含机制类与五个 shader。
- `flclash-demo/build/outputs/apk/debug/flclash-demo-debug.apk`：独立验证应用。
- `sample/build/outputs/apk/debug/sample-debug.apk`：第二阶段验证现有应用接入。

现有 `verifyDebugArtifacts` 主要检查原框架和 sample，需按分发方式追加新模块产物检查。裸新 AAR 不自动携带 Sketch、Compose 等依赖；优先项目依赖或包含依赖元数据的发布方式。

## 9. 真机与视觉验收清单

重新用 `adb devices -l` / `adb mdns services` 获取设备地址。历史 `192.168.1.148:43923` 和 `:36935` 不写死在脚本中。不启动模拟器，使用真机；视觉效果由用户验收。

| 项目 | 验证结果 |
| --- | --- |
| 启动 / 首开菜单 | 冷启动、首次和后续展开正常；重点看刚离开按钮时的固定位置停顿及 ART VerifyError |
| Goo 高光 | 四按钮顶部 / 底部直线与曲线连接处没有突出的短亮条；融合、分离、按压连续 |
| 采样 | 明显色块或文字背景下移动控件，覆盖处内容对应正确，无自采样、整图缩放、旧截图 |
| 轮廓 / 阴影 | 快速按压、反向、切层无白矩形、尾巴、阴影切边 |
| 多级菜单 | 根→二级→三级、标题与系统返回、400ms 停留展开、外部关闭、62 项长列表、惯性和回弹 |
| 快速打断 | 打开中关闭、关闭中重开、连续三次、滑动中切层，不误触或留遮罩 |
| Dock | 拖动、快速反向、边缘点击，导航状态、透镜和回调一致 |
| Popup / Dialog | 结果在关闭后交付；模态、键盘、旋转、移动锚点、长内容和危险动作正确 |
| 主题 / 图标 | 浅深色、纯黑、种子色、124 图标、RTL、大字、页面往返 |
| 目标原功能 | 手机示例库入口、列表间距、Slider 端点与结束回调、Toggle、顶部模糊、旧底栏及页面状态 |
| 低版本 | 第二阶段 API 24–32 真机启动和旧路径可用；无设备则保留未验证 |
| 日志 | 操作后无应用崩溃、shader 编译失败、RenderThread 递归、VerifyError |

目标行为回归可运行 `node tools/figma-reader/check-native-interactions.mjs <当前设备序列号>`，同时使用迁入的完整人工清单。

性能比较固定同一手机、刷新率、构建类型、温度区间和操作序列，分别记录冷启动首开、预热后三次开合及长列表。先在未附加方法采样器时用 `dumpsys gfxinfo <包名> reset` / `framestats` 记录帧，再单独采 trace；不混用 profiler 开启和关闭的数据。手机断连、被其他应用覆盖或切后台时重采。

源项目 2026-11-03 参考数据：324 个单测通过；菜单三轮短测从 328 帧中 36 个 janky frame（10.98%，P95 44ms）变为 362 帧中 12 个（3.31%，P95 30ms），重复一轮 P95 29ms。这是设备条件未完全固定的 Debug 短测，只作为明显回退的参照，不是目标工程性能承诺。

源 APK：`C:\IDE\Android\liquidglass27\artifacts\20261103-retained-rendering\liquidglass27-retained-rendering-debug.apk`。SHA-256：`6C785851D1F2347247C95A20797B681216E1767B35AC0DE8119EA10BA4B7C925`。可与独立包名的新验证应用并存比较。原项目视觉验收仍待用户确认，迁入不能改记为“全部视觉一致”。

## 10. 变更记录、回退和完成标准

建议三批审阅：新库 / shader / 测试及最小入口调整；独立验证应用 / 工具 / 产物检查；API 24 兼容入口及 sample 页面接入。

每批记录新增和修改文件、原因、源文件及合并后 SHA-256。在目标 `docs/liquid-glass-merged-files.json` 按现有结构追加本次记录及详细清单引用，保留原条目。中文源码保持 UTF-8，用补丁修改。

回退先移除本次 sample 接入，再移除新增模块引用与本次新增文件；只撤销本次差异。共享文件原有修改不能通过整文件 `git restore` 或硬重置撤销。

- 新工程不再通过 `../liquidglass27` 读取源码、测试、资产或生成器输入，可以脱离源目录构建。
- 新机制依赖目标 `:liquidglass`，不复制核心、不形成模块循环。
- 新库测试、shader / 图标检查、目标原有约束及 AAR / APK 检查通过，保存实际报告。
- 第二阶段完成时，sample 保留 API 24 和原页面行为，API 33+ 可以从现有应用进入新机制。
- 性能优化成组保留，真机结果重新记录；低版本与视觉未验收项单独列出。
- 视觉结论以用户确认的清单为准。

来源与许可说明沿用源 README 和 `FlClash/LICENSE` 的 GPL-3.0 标识及作者信息，同时保留目标 Sketch 原有许可证和第三方说明；不以目标原声明替换新代码来源声明。
