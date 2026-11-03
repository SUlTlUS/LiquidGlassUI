# 绘制阶段更新与资源复用 · 2026-11-03

本轮列出的四项性能工作已处理，最终 APK 已安装 PLQ110。菜单短测改善可复现，仍有长帧；外观及触感由用户按 [验证清单](../../docs/verification-2026-11-03.md) 验收。

## 四项落实情况

1. **根菜单开场/回收移出整棵菜单的逐帧重组**：同一份 `MenuRootMotion` 在绘制、测量和 placement/graphicsLayer 中驱动玻璃、阴影、源图标和内容变换。组合层只跟踪显示/隐藏/命中阈值；布局和交互结构变化仍由 Compose 处理。共用控件高光也在 graphicsLayer 阶段更新，并复用相同输入的 RenderEffect。
2. **完整光环缓存**：`GlassMenuRimRenderer` 分别缓存几何和光照；重绘参数相同则回放完整 Picture，避免重建渐变、画笔及九个浮点颜色滤镜。原有九层光环、外侧暗线和混合方式保留。
3. **统一 shader 准备**：应用启动时后台加载源码、准备 RuntimeShader；源码进程内共享，预备实例独占移交。每个 pane 的可变 uniform 独立，Activity 重建不重复预热。
4. **按 Sketch 管线实现共享复用**：核对发现 Sketch 已有共享 `LayerBackdrop` 和每个 pane 的效果缓存，所以原先“图集尚未接入”需要区分后端。当前补充场景级 `SketchMaterialCache`，按尺寸、圆角、密度、字体缩放、方向和材质类型复用最多 32 组不可变效果。保留 Sketch 的背景层与原 blur/lens/vibrancy/colorControls；FlClash 位图 Atlas 仍只属于非 Sketch 后端。这项是共享效果描述与现有录制层的复用，不宣称共享了各控件最终的模糊像素。

Sketch 采样与材质参数、动画时长、36dp 采样边距、射线精度和轮廓计算公式保持原值。`apk-validation.json` 证明五个 shader 资产与基线逐字节一致，`uniform-block-validation.json` 证明高光 uniform 上传主体未修改。本地 FlClash 参考源码和导入的 Sketch 源码未修改。

## 无采样器的真机数据

设备 PLQ110，ADB `192.168.1.148:36935`。每轮重启应用，用相同脚本检查布局并连续开关菜单三次；操作前确认测试应用仍在前台。卡顿使用 gfxinfo 非 legacy 指标，耗时单位 ms。

| 数据文件前缀 | 版本 | 帧数 | 卡顿帧 | P50 | P95 | P99 |
|---|---|---:|---:|---:|---:|---:|
| `before` | 本轮基线 | 328 | 36（10.98%） | 17 | 44 | 77 |
| `phase` | 初次运行检查 | 359 | 13（3.62%） | 13 | 30 | 53 |
| `final` | 最终 APK，第 1 轮 | 362 | 12（3.31%） | 13 | 30 | 57 |
| `final-repeat` | 最终 APK，第 2 轮 | 362 | 12（3.31%） | 13 | 29 | 48 |

最终版还完成各三次 Goo 和底栏拖动：

| 场景 | 帧数 | 卡顿帧 | P95 | P99 |
|---|---:|---:|---:|---:|
| Goo | 450 | 3（0.67%） | 17 | 22 |
| 底栏 | 413 | 9（2.18%） | 24 | 31 |

Goo/底栏本轮没有相邻的基线采样，仅记录最终状态。上述均为 Debug 短测，未固定设备负载、刷新策略或编译器预热状态；不代表其他设备和全部场景，也不代表卡顿已消除。

## 方法采样和运行检查

`final-sampled.trace` 使用 ART 1000μs 方法采样，仅用于确认 CPU 工作位置，采样器下的帧数据不用于评价提速。最终根菜单 `Content` 累计采样 CPU 231.076ms；同一基线 APK 的历史记录为 1206.335ms（`../20261103-performance/final-menu-sampled.summary.json`）。两次采样帧数不同，不将这一差值换算为固定总体加速比例。采样栈进入次数也不等于真实调用次数。

`routes-complete.log` 和对应 XML 记录了一级→二级→三级、从三级点击外部整体回收、快速打开后返回三次、主题切换及弹窗打开/关闭。最后菜单/Goo/底栏及路由进程日志未发现 FATAL、VerifyError 或 shader 准备/高光设置失败。XML 与日志只证明运行状态，不证明视觉一致。

## 构建与交付

- `build-final.log`：Debug 构建、324 项 JVM 测试及 Lint 通过；0 测试失败/错误，Lint 0 errors / 22 warnings / 1 hint。
- 新增采样边界及中途回收圆角回归；材质所有权检查随代码抽取更新，继续约束调用 Sketch 原生效果。
- 源码 UTF-8 检查通过；模拟器未启动；构建排除 `:app:sendNamedDebugApkToQQ`。
- 最终 APK：[`liquidglass27-retained-rendering-debug.apk`](liquidglass27-retained-rendering-debug.apk)
- SHA-256：`6C785851D1F2347247C95A20797B681216E1767B35AC0DE8119EA10BA4B7C925`
- 基线 APK：`baseline.apk`，来自前轮 SHA-256 `53EFEEC3087EDE47FB894B953C8E2ABFD9568D90C358B209766294CAB9F58CEF`。

帧数据复现：`python artifacts/20261103-retained-rendering/profile_device.py NAME --serial CURRENT_ADB_ADDRESS --scenario menu`。方法采样额外加 `--trace`。操作期间保持测试应用前台。
