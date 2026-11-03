# 当前修正：底栏外圈采样精度

底栏高光改为 256 射线，原轮廓路径采样加密至 1024–4096 段，仍读取 26 主体的同一个连续胶囊形状。其他控件维持 64 射线；原有 27 明暗、高光宽度、材质和主体几何不变。采样仅在轮廓变化时重建。

构建成功，按用户要求自动安装；未运行任何测试、Lint、shader 渲染验证或设备启动操作。运行时表现与边缘视觉效果待用户验收，不能以编译成功替代。安装结果见 [机器记录](verification-results.json)。

[当前 APK](../../dist/ios27-dock-outline-20261103/LiquidGlassSketch-ios27-dock-outline-debug.apk)，SHA-256 `d534959300788916e5b014e33521bdbf1cbe70951ceced24837d11c43ec89d18`。

---

下文为先前版本记录，不代表当前版本的测试结果。

# 当前修正：底栏重复边线

移除选中文字隐藏参考层上的 27 高光，防止选中胶囊采样整条参考层的上下边线；选中胶囊的 hairlineSide 与按压进度同步归零。仅修改 27 适配器，保留 26 主体。

用户要求自动安装到 `192.168.1.148:45149`，不要测试。本轮仅执行 `:sample:assembleDebug`（排除 QQ 发送），构建成功；未运行单测、Lint、静态测试、启动或交互检查。安装状态见 [机器记录](verification-results.json)。

[当前 APK](../../dist/ios27-dock-rim-20261103/LiquidGlassSketch-ios27-dock-rim-debug.apk)；SHA-256 `daee3beb2e534dc42d1ca24c27899d9f2b724f0e97a539fbc79764d529fea481`。当前修正的真机效果待用户验收。

---

以下均为上一版 Goo APK 的历史验证，不是当前修正的测试结果；其中 AAR 与 APK 链接保留历史版本。

# 2026-11-03 合并、原生示例与底栏 / Popup 修正验证

此文件只记录 `liquidglass_sketch` 本次合并结果。`baseline/` 中源项目的 324 单测、真机帧时间与截图不计为本次验收。

## 通过的检查

| 检查 | 本次结果 |
| --- | --- |
| 完整迁入与来源哈希 | 69 个机制文件、5 个 shader、56 个原测试文件；86 个旧基线保留，其中 84 个文件逐字节不变，`LiquidBottomTabs.kt` / `DrawBackdropModifier.kt` 的可选高光钩子按本轮授权单独审核 |
| 原提取边界、页面 tokens、项目规范 | 通过；保留原提取基线，未重生成 Figma 资产 |
| 新库 JVM 单测 | 341 / 341 通过，无跳过；含 17 项新增 Popup 文字比例、共用圆角/高光、底栏主体及通用 Goo 分组回归 |
| 旧库与版本分流 JVM 单测 | 68 / 68 通过，无跳过 |
| 新库 Lint | 0 错误，18 警告 |
| 宿主 Lint | 0 错误，102 警告；新宿主页复用 Material 2 页面框架、机制库使用 Material 3，存在混用提示，未压制；此前旧 ControlCenter 未使用 scope 的 BoxWithConstraints 已改为同尺寸 Box |
| 新示例结构回归 | 4 / 4 通过；检查七个路由、宿主框架复用、单向采样边界、源演示源码缺席 |
| 图标生成器 | 124 个图标全部匹配固定输入 |
| 实际 AGSL 渲染检查 | dark regular / tinted、light regular、无 hairline 四组全部通过；[明细](shader-validation.json) |
| Debug 构建与打包 | 通过；AAR 类定义、APK DEX 定义、5 个 shader、原生示例入口均存在；APK 无 `dev.liquidglass27.demo` 类定义 |
| APK 签名与对齐 | APK Signature Scheme v2 通过；`zipalign -c -P 16 -v 4` 通过 |
| 宿主依赖解析 | Compose UI / Foundation 1.12.0、Material3 1.4.0；沿用依赖解析结果，未增加 force / enforcedPlatform |

新示例入口为 **手机示例 → iOS 27**，分别为弹性按钮、Goo 操作组、导航底栏、多级菜单、Popup、对话框、主题与图标。使用现有分类路由、页面框架、tokens、分组与返回按钮；原项目的两个整页演示已经删除。行内控件读取独立预览背景，顶部与浮层读取完整页面，保持单向采样。原有示例按 iOS 26 保留。

初次合并的 69 个机制与源项目去除 API 注解后完全一致。本轮按用户反馈修改 `GlassPopup.kt` 和 `GlassControls.kt`，其余 67 个原机制保持一致；新增的标准底栏适配器和内容转换单独纳入清单。临时为原演示开放的 `canUseSourceContentFrame` 保持源项目 `internal`，该文件哈希与源基线相同。[本轮改动与来源依据](dock-popup-correction.md)。

## 已有失败与验收边界

`node --test tools/figma-reader/native-framework.test.mjs` 为 15 通过、17 失败。合并前后失败名称一致：使用初始 SHA-256 匹配的 MainActivity/IphoneExamples 原文件重跑基线，其他输入未改变；不是把 HEAD 当成原工作区。没有删除测试或修改旧组件去迎合过时源码断言。失败名称保存在 [机器报告](verification-results.json)。因此不声明全仓所有静态测试通过。

早期复制式演示 APK 曾安装到 PLQ110（API 37），其中间版本的自动导航被系统浮层/前台切换干扰，结果断言未通过。这些历史尝试不计为当前版本验收。用户明确要求“先完成代码和构建，真机由我验收”，因此本轮底栏 / Popup 修正版 APK **未安装、未启动、未操作手机**。重点待验收：26/27 切换后的底栏拖动和选中、Popup 展开 / 中途反向时文字比例、源按钮与面板高光连续性、菜单与 Popup 圆角一致性。

API 24–32 真机启动、完整多级/快速反向菜单、Popup 键盘/旋转、Goo 动态融合、两套主题视觉、背景对应位置透视、滚动和 Slider 两端回归均仍待真机验收。这里的合并与测试通过不替代用户视觉结论，源项目的历史帧时间不转记为目标性能结果。

## 产物

- [底栏 / Popup 修正版 APK](../../dist/ios27-goo-buttons-20261103/LiquidGlassSketch-ios27-goo-buttons-debug.apk)，47,536,098 字节；SHA-256 `bcae9a3fb7a27ef9d83e3605825cc96b6bb5bf59e6bb51ffc8279590d2bf6f35`。
- [iOS 27 AAR](../../dist/ios27-goo-buttons-20261103/liquidglass-ios27-debug.aar) 与 [iOS 26 AAR](../../dist/ios27-goo-buttons-20261103/liquidglass-ios26-debug.aar)；裸 AAR 不包含传递依赖，优先使用 Gradle 项目依赖。本轮增加旧库可选钩子，因此两份库应一同更新。此前 `ios27-merge-20261103` / `ios27-native-examples-20261103` 是历史快照。
- [源代码清单](merge-manifest.json)、[测试/产物机器报告](verification-results.json)。

未执行 Git 提交、清理、同步或 QQ 发送，原有暂存和未暂存内容保留。

本次追加：修正来源快照遗漏 padding 和按前景尺寸放大造成的末帧字号变化，新增三项回归。未进行设备安装和交互，文字视觉结果待用户验收。

底栏截图反馈修正：27 高光移至阴影内侧的独立材质层，背景逆变换不再调用高光回调；新增图层顺序回归通过。未安装操作手机，修正后的边缘效果待用户验收。

通用按钮 Goo：新增六项物理顺序、行隔离、材质边界、容量与空组回归；普通文字/图标/着色和 Popup 来源示例接入 `GooGlassGroup`。同排同材质分组，最多六个一批；新公开入口通过 AAR/APK 类定义检查。未安装操作手机，融合、增删后点击与 Popup 回源效果待用户验收。
