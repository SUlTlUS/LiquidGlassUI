# 统一底栏与 MeloX 来源记录

2026-11-03：按用户要求合并 MeloX 与通用底栏，保留可切换参数、默认 MeloX。当前公开入口为 `dev.liquidglass.GlassTabBar` / `GlassTabBarScaffold`，旧别名与重复 Scaffold 已删除；`MeloXTabBar` 承担底层实际渲染。主界面和“标签栏”示例使用统一入口，26 / 27 独立控制高光。新 API 见 [组件库指南](component-library.md)。下文来源与播放器说明保留；“原样”仅描述冻结来源或未修改的交互机制，旧 API 代码片段只作历史参考。

| 参数 | MeloX（默认） | 通用 |
| --- | --- | --- |
| 主体模糊 | 2 dp | 8 dp |
| 主体涂层 | 原 MeloX 主题色，alpha 0.52 | FAFAFA / 121212，alpha 0.4 |
| 展开高度 | 57 dp | 62 dp |
| 水珠折射比例 | 49 / 56 | 1 |
| 水珠静止外/内阴影系数 | 0.50 / 0.60 | 0 / 0 |

两组参数共用收缩、拖拽、捕获层和水珠机制。独立搜索、迷你播放器、滚动收缩均为选项。背景由独立兄弟层录制，27 高光通过 `surfaceLayerBlock` 绘制在外阴影之后，隐藏捕获层不注入高光。原始提取 manifest 不刷新；授权修改在 `docs/melox-tabbar-merged-files.json`，提取工具会核对基线及已审核输出，也不会覆盖这些本地修改。

来源：[lladlam/MeloX-Android](https://github.com/lladlam/MeloX-Android/tree/723aa764462386440b76aed5df362dadf63a096f)，固定 commit `723aa764462386440b76aed5df362dadf63a096f`。入口是 `ui/MeloXApp.kt` 的 `MeloXBottomChrome`，不是本项目原有 `LiquidBottomTabs` 的换皮。

## 文件与调用入口

旧底栏重复实现已清理：运行代码中只保留 MeloX 这一套导航绘制/手势。旧通用底栏的整套函数、无调用的样式类与分离搜索封装已删除；两个 27 名称的入口只转接 `UnifiedGlassTabBar`。分段选择器使用的单项标签控件保留，当前导航参数、玻璃和动画未修改。来源目录仍作为冻结证据保留，不参与构建。

统一底栏、Scaffold 和兼容入口均提供 `compact: Boolean = false`。开启后以每项 64dp 加主体内距、独立搜索及外边距计算宽度上限，并在宿主内居中；窄屏仍受可用宽度约束。64dp 是本地紧凑布局参数，不是上游或 Figma 原值。即使宿主传入 `fillMaxWidth()`，可见底栏仍按此上限收窄。它与滚动收缩的 `minimized` 独立，默认关闭。“标签栏 → 收缩、搜索与播放器”示例提供开关。

- `melox-tabbar/`：Android Compose Library，依赖 `:liquidglass` 共用光学核心，不依赖音乐服务或账户业务；封面支持本地 URI 与网络图片。
- `melox-tabbar/src/main/kotlin/com/lladlam/melox/ui/MeloXBottomChrome.kt`：机械截取原版布局、手势、水珠、收缩和搜索实现。
- `MeloXTabBar.kt`：公开接口及安全采样宿主。
- `ui/player/MeloXIOSMiniPlayer.kt`：原版玻璃迷你播放器，包含封面、两行文字、音频指示条、播放/暂停/下一首、滑动切歌和收缩几何。
- `ui/MeloXTabBarScrollState.kt`：从原应用宿主搬出的滚动收缩连接。
- `ui/player/MeloXPlayerScaffold.kt`：实际接通 MiniPlayer 与 Artwork 目的层的共享转场宿主，不是点击后显示提示文字。
- `ui/player/MeloXIOSNowPlayingSharedHost.kt`：原版共享壳、封面飞行、可逆拖拽与释放回弹；完整播放器业务不在本次组件范围内。
- `ui/glass/`：原版面板材质、捕获层、水珠材质、动态明暗采样、按压高光和阻尼拖动。
- `melox-tabbar-demo/`：独立演示应用，包名 `dev.liquidglass.meloxtabdemo`，不替换现有应用底栏。
- 现有应用入口：组件 → **标签栏 → 收缩、搜索与播放器**。旧 `melox-tabbar` 深链仍然有效。进入页面时主应用标签栏按已有二级页面规则隐藏。
- `docs/melox-tabbar-extracted/source/`：原始完整 Kotlin 文件，供逐行比较，不要覆盖它们掩盖修改。
- `docs/melox-tabbar-extracted/manifest.json`：来源 commit、原文件和提取后文件 SHA-256、适配清单。

## 推荐接法：组件自动安排采样层

当前集成应包含 `:liquidglass`、`:melox-tabbar`、`:liquidglass-ios27`，应用依赖后者并调用 `UnifiedGlassTabBar`。仅需 MeloX 基础 API 时可依赖 `:melox-tabbar`，但仍需它的 `:liquidglass` 依赖；AAR 不包含传递依赖。

```kotlin
dependencies {
    implementation(files("libs/melox-tabbar-debug.aar")) // 使用源码 module 时不用这行
    implementation(platform("androidx.compose:compose-bom:2026.06.00"))
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation(project(":liquidglass"))
    implementation("io.github.kyant0:shapes:1.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("io.coil-kt.coil3:coil-compose:3.5.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.5.0")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("com.squareup.okhttp3:okhttp:5.3.0")
}
```

应用需启用 Compose 和 Kotlin Compose 插件；只加 AAR 不会自动启用这些构建配置。

```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.lladlam.melox.ui.*

var selected by rememberSaveable { mutableStateOf(AppTab.Home) }
val scrollState = rememberMeloXTabBarScrollState()
val scrollConnection = scrollState.nestedScrollConnection()

MeloXTabBarScaffold(
    modifier = Modifier.fillMaxSize(),
    selectedTab = selected,
    onTabSelected = { tab ->
        selected = tab
        scrollState.expand() // 原版：点击收缩的标签重新展开
    },
    minimized = scrollState.minimized,
) {
    // 真实页面内容和图片画在这里，滚动内容也放这里。
    // 这个 content 不得包含采样同一个背板的玻璃控件！
    MyPage(selected, Modifier.fillMaxSize().nestedScroll(scrollConnection)
        .background(MaterialTheme.colorScheme.background))
}
```

`MyPage` 是宿主自己的页面，不是组件 API。页面要留足底部滚动余量：原版无媒体时布局高度为 64dp，加组件内置底距 8dp，再加导航栏 inset；有媒体时原版布局高度为 124dp。不要在组件外再次套 `navigationBarsPadding()`，也不要通过 Material Scaffold 的 bottomBar 让背景停在玻璃上沿。

原版默认四个主标签为 Home、Explore、Library、Settings；Search 始终是右侧独立按钮。可以改变 `tabs` 顺序或选用 Podcasts、Downloads、Cloud。主标签不可为空、重复或包含 Search，选中项必须是当前主标签之一或 Search。公开接口会直接校验，防止错误调用进入拖动几何。

## 已有背景采样层时直接调用

```kotlin
val source = rememberLayerBackdrop()
Box(Modifier.fillMaxSize()) {
    Box(Modifier.fillMaxSize().layerBackdrop(source)) { MyOpaquePage() }
    MeloXTabBar(
        modifier = Modifier.align(Alignment.BottomCenter),
        backdrop = source,
        selectedTab = selected,
        onTabSelected = { selected = it; scrollState.expand() },
        minimized = scrollState.minimized,
    )
}
```

以上代码需要导入 `com.kyant.backdrop.backdrops.*` 与 Compose layout、Alignment。背景录制层和底栏必须是兄弟，不要把底栏放入录制自身的父层。图片必须画在全页源层，不能缩放到玻璃尺寸冒充采样。

三层单向依赖原样保留：页面背板 → L1 面板；页面背板 → L2 隐形强调色标签捕获层；页面背板 + L2 → L3 水珠。L2 内只能放标签内容，不能再放 L3 或其它读取 L2 的玻璃。

## 可配置项

- `minimized`：使用上述 scrollState 自动接原版滚动收缩，或由宿主显式控制。不要只传固定 false 后误认为组件缺少动画；原应用滚动调度位于 `MeloXApp.kt`，不在底栏 renderer 内。
- `frostedGlass=false`：默认原版液态材质；true 是上游已有的毛玻璃模式，不是本项目替代实现。
- `automaticTone=true`：使用原版 PixelCopy 明暗采样与迟滞。取的是 chrome 上沿往上 8dp 的背景带，不直接取玻璃自身。false 则跟随当前 MaterialTheme 的深浅色。采样只判明暗；实际折射始终来自 Backdrop，而非 PixelCopy 截图。
- `labels: Map<AppTab,String>`：默认沿用上游四种语言的原标签。覆盖标签时 L1/L2 同时应用，搜索辅助说明也一起覆盖。
- `searchEnabled=true`：宿主可禁用右侧独立按钮，点击与无障碍禁用状态同步。选中语义与玻璃捕获层的着色状态分离；收缩按钮也保留当前标签和选中语义。此适配不改变几何、弹簧和着色器。
- `icons: Map<AppTab,MeloXTabIcon>`：可选图标插槽，接收 Modifier、Color、selected。默认使用原版矢量/图标字体；自定义内容必须使用传入 Modifier 和 color，避免图标脱离按钮及 L2 强调色丢失。
- `fontFamily=MeloXOriginalTabFont`：默认复制上游兰亭 Pro 字体。想跟随系统可传 `FontFamily.Default`，但此时字体不再与上游默认相同。标签仍是原版 11sp / 13sp 行高与字重。
- `hasMedia` 和 `miniPlayer(compactProgress)`：保留原版插槽定位、升降和收缩同步几何；使用下面 PlayerScaffold 提供的原 MiniPlayer，不要用 TextButton 占位。播放器服务业务不带入组件。传 true 时必须实际提供内容，不要空留播放器高度。miniPlayer 读取 `LocalMeloXBackdrop`，不得读取包含自己的录制层。

底栏只输出选择回调，不创建页面导航、不保存用户偏好。播放器宿主管理展开/收起，封面加载器可以发起图片网络请求，但不接入音乐服务。

## 没有改动的原版视觉数值

展开导航胶囊/搜索按钮高 57dp，收缩高 46dp、左胶囊宽 48dp。左右屏幕边距 12dp、独立搜索间距 7dp、导航内容水平内距 10dp、水珠高 `navHeight - 8dp`。这是 MeloX 原值，不要套用本项目 iOS 示例的 16dp 搜索间距或旧底栏参数。

面板及捕获层 blur 都是 2dp；面板 lens 为 24/24dp；水珠按压折射为 `10/14 × 49/56 × pressProgress`，带色差。面板白纱/深色表面 alpha 0.52；水珠静息阴影下限 0.50、内阴影下限 0.60。高光、阴影、涂层、标签颜色使用同一个暗度标量。所有这些值、修饰符顺序、弹簧和 AGSL 代码原样保留。

## 提取时的必要适配（不是重写）

1. 从巨大的应用入口截取 chrome 和其所用 helper；迷你播放器视觉源码与转场、封面 helper 一并提取，播放服务、导航、账户代码不带进库。
2. 改导入，并把 chrome 从 private 改成 internal，供公开接口调用。
3. 移除原来无实际用途的 `MusicSource` 参数；标签和图标增加可选 CompositionLocal 插槽，默认仍走原逻辑。
4. `MeloXSettingsRuntime.frostedGlassEnabled` 保留调用写法，但实现为实例内的 CompositionLocal，避免不同底栏互相改全局设置。
5. 只移除诊断用 `MeloXCrashStore.markGlassRendering()`；硬件加速判断、NaN 清洗、零值 lens 门控完全保留。
6. 主题只提取底栏用到的原始 LightColors、DarkColors 与深浅判断；字体在公开适配层设置。不接管宿主的 Window、全局主题或业务偏好。
7. Kotlin 文本机械统一为 LF；字体、矢量二进制/资源原字节复制。
8. MiniPlayer 仅将 `MeloXAudioReactiveRuntime.sample(mediaId)` 替换为注入的 `audioSample(mediaId)`，其余整份视觉文件不改。`MeloXPlaybackUiState` 是独立 UI/回调适配器，不是原音乐服务；动画偏好使用原默认值，360ms 转场、reduceMotion=false、artworkMotionEnabled=true。
9. 共享目的层固定为 Artwork，排除 Lyrics、Queue、音质面板与音乐服务自动混音；原版 shell/drag/sharedElement 和 Artwork/播放按钮动画片段直接提取。默认内容拼接原版 Artwork 辅助控件，没有伪造歌词或队列入口。
10. 前后台状态改由宿主 Lifecycle 注入；音频分析由回调注入。无分析时默认采样仅报告 UI 的 isPlaying，hasAnalysis=false，不伪造节拍。图片调色使用独立 OkHttpClient，不继承原应用的账户/DNS；不存在的后台音频分析负载按空闲处理。

## 原版上方播放器插槽

```kotlin
import com.lladlam.melox.ui.player.*

val playback = MeloXPlaybackUiState(
    queue = queue, // List<MeloXQueueEntry>，封面为 URL、content:// 或 android.resource://
    currentIndex = currentIndex,
    isPlaying = isPlaying,
    repeatMode = 2,
    onTogglePlayPause = { /* 调用你的播放器并更新 isPlaying */ },
    onNext = { /* 切下一首并更新 currentIndex */ },
    onPrevious = { /* 切上一首并更新 currentIndex */ },
)
MeloXPlayerScaffold(state = playback) { miniPlayer ->
    MeloXTabBarScaffold(
        selectedTab = selected,
        onTabSelected = { selected = it; scrollState.expand() },
        minimized = scrollState.minimized,
        hasMedia = playback.hasMedia,
        miniPlayer = miniPlayer,
    ) { MyPage(Modifier.nestedScroll(scrollConnection)) }
}
```

`queue/currentIndex/isPlaying` 变化时重新创建适配 state；回调需要真正修改宿主状态，不能只提供空函数。普通下一首按钮在无下一首且不循环时禁用；原版横滑 `nextFromMiniPlayer/previousFromMiniPlayer` 走宿主切歌回调，宿主自行实现原版循环队列行为。

玻璃壳固定 48dp，原版展开封面 40dp / 收缩 30dp，封面圆角 6dp；字形按原版 `Dp.toSp()` 保持 14dp 标题 / 12dp 作者 / 15dp 行高，不修改设备字体缩放。单独 MiniPlayer 默认音频采样 Idle；PlayerScaffold 默认补 UI 的播放状态。实际音频可传 `audioSample`，没有音源时不要把指示条称为真实音频分析。

固定上游版本的四条音频指示条替代了用户参考截图中的省略号，源码保留该差异，不伪称完全像素相同。示例默认显示播放器，封面使用上游应用图标作为演示图片（不是参考歌曲封面），播放/暂停与切歌仅演示 UI 状态，没有加载真实音频；点标题打开真实共享转场目的层。网络封面需宿主声明 INTERNET 权限；本地演示封面不需要联网。

## 动画不能漏接的宿主链

直接调用 MiniPlayer 且不传共享 scope，只会得到迷你播放器自身动画，不会凭空出现目的页面。新 app 应使用 `MeloXPlayerScaffold`，把它提供的 miniPlayer 原封不动传给底栏插槽；不要另起 SharedTransitionLayout、不要改成导航跳转或文字回调。源与目的 AnimatedVisibility 必须使用同一个 SeekableTransitionState 和 SharedTransitionScope。

- 自动展开/系统返回：原版 master timeline 为 360ms LinearEasing，实际壳和封面使用原版 boundsTransform、分段 alpha/scale 曲线。
- 拖拽：原版 seek 进度上限 0.999，范围为窗口高度的 0.86；释放超过 0.42 或速度达到 1200px/s 收起，否则弹回。settle 为 dampingRatio=1、stiffness=420 的原版 spring。
- 滚动：原版方向累加阈值为 18 **px**，不是 18dp；连接必须放在实际可滚动内容上。
- 播放/暂停：原版 AnimatedContent 字形替换、封面缩放及按压弹簧均保留；不能用静态图标与即时切换替代。
- 初始暂停且无音频采样时，原版 FlowingLight 可能尚未产生首帧；播放后默认无节拍恒速流光才运行。真实 app 应传实际音频运行状态，不要把 Idle 永久传入后声称流光已经接通。

源码片段一致性与同设备原 APK 动画像素一致性是两项验收。本项目目前提供前者与模拟器中间帧/手势证据，未完成后者，也未搬入完整音乐服务、Lyrics/Queue 和业务弹层。

默认显示路径之外新增了入参验证、安全 Scaffold 和可选配置；不能把整个独立库宣称为上游应用文件的逐字节副本。原版 renderer 的差异可通过脚本、快照与 manifest 复核。

## 构建、检查与输出

最低 Android API 26（同上游），compileSdk 37，当前宿主工具链 JVM 21 / AGP 9.3.1 / Kotlin Compose 2.4.10。Compose BOM 2026.06.00、Backdrop 2.0.0、Shapes 1.2.0、Lifecycle Compose 2.10.0 与上游版本一致。不要在宿主中升级 Backdrop 后仍宣称最终 shader 运行库与此基线相同。

本项目 sample 原有依赖会将 Backdrop / Shapes 分别解析到 2.0.1 / 1.2.1；不强制降级既有框架。独立演示才使用组件声明的原版 2.0.0 / 1.2.0。sample 保留 API 24，manifest 只针对该库覆盖 minSdk 合并检查，新页面在 API 24/25 显示版本要求且不调用 MeloX；这不是给原版组件虚构低版本玻璃支持。

```powershell
node tools/extract-melox-tabbar.mjs --check
node --test tools/melox-tabbar-extraction.test.mjs
.\gradlew.bat :melox-tabbar:testDebugUnitTest :melox-tabbar:assembleDebug :melox-tabbar-demo:assembleDebug --no-daemon --console=plain
```

输出：

- `melox-tabbar/build/outputs/aar/melox-tabbar-debug.aar`
- `melox-tabbar-demo/build/outputs/apk/debug/melox-tabbar-demo-debug.apk`

检查脚本需要 `vendor/MeloX-Android` 是上述固定 commit。没有 vendor 时先 `git clone https://github.com/lladlam/MeloX-Android vendor/MeloX-Android`，再 `git -C vendor/MeloX-Android checkout --detach 723aa764462386440b76aed5df362dadf63a096f`。更新上游前先比较，不要直接重新运行生成脚本覆盖本地改动。

演示可检查标签点击、拖动水珠、搜索往返、收缩展开、明暗采样、毛玻璃/液态切换及滚动折射。编译或交互通过不等于已经和原 APK 在同设备上逐像素比较；动态视觉差异还受背景、主题、窗口 inset、屏幕密度和宿主依赖影响。

## 许可证与资源边界

EPD 宿主在 2026-10-28 改为显式传入 `FontFamily.SansSerif`。`MeloXOriginalTabFont` 改为按需 getter，其他宿主仍默认使用原字体；不再因顶层属性初始化阻止不使用原字体的 Release 宿主裁剪大型 Mi 字体资源。对应文件与前后哈希已记录在 `docs/liquid-glass-merged-files.json`。

上游仓库 LICENSE 为 GPL-3.0，原文附在 `melox-tabbar/LICENSE`。其代码注释说明部分玻璃/拖动 helper 来自 Apache-2.0 的 AndroidLiquidGlass / BiliNext，原归属注释保留，Apache-2.0 许可证文本附在 `melox-tabbar/LICENSE-AndroidLiquidGlass`。

这两份许可证及来源信息同时嵌入 AAR 的 `classes.jar` 内 `META-INF/melox-tabbar/`，方便随组件包携带归属说明。

原版 `sf_pro_subset.ttf`、`mi_lan_pro_vf.ttf` 及品牌图标为还原默认外观而原样复制。未在上游找到能独立确认这些字体再分发权限的授权文件；仓库许可证不应被当成第三方字体/商标资源的授权证明。正式分发前请自行确认资源授权，或用已获授权的字体及 `icons` 插槽替换；替换后默认字形不再是原版。不把该模块标成 MIT/Apache 独占许可。
