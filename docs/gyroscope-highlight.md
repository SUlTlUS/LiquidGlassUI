# LiquidTab 陀螺仪高光移植

目标是本项目 `liquidglass_sketch`，来源是本机 LiquidTab 的
`common/src/main/java/io/github/offlineglass/hook/GlassHostLayout.kt` 中的
`BloomTiltTracker`、`setBloomLight`、`BLOOM_STROKE_DUAL_SHADER`。

旋转矢量优先使用 `TYPE_GAME_ROTATION_VECTOR`，回退 `TYPE_ROTATION_VECTOR`，
以 `SENSOR_DELAY_GAME` 采样；取旋转矩阵第三行的负值作为重力投影，保留
0.15 低通滤波、0.01 平放阈值平方和 0.0007 重绘方向阈值平方。
按显示方向重映射后旋转主光源，叠加固定次光源。所有数值来自 LiquidTab，
不属于 Figma 参数。

共用 `HighlightNode` 将默认 `HighlightStyle.Default` 接到双光源高光；
`followDeviceTilt=false` 可恢复原方向性高光。`sensorRotation` 默认 -45°，
可设为 90° 用于选中胶囊。Ambient/Plain 维持原有模式。
高光仍沿原控件 Outline 绘制，保留原来的宽度、模糊、颜色透明度、PLUS 合成
与圆角裁切，不改变页面布局、背景采样、按压交互和外部阴影。

同进程共享一份传感器监听，窗口不可见、高光禁用或节点移除时解除相应订阅，
最后一个订阅离开时停止采样。无传感器时采用平放参考方向；Android 13 以下保留
原有兼容路径，着色器创建失败回退原方向性高光。

测试覆盖低通滤波、静止重绘阈值、四方向旋转、平放回退、光源旋转、重启与非法样本。
构建与单元测试不能替代设备倾斜时高光移动、横屏映射和退后台停采样验收。

本轮验证：提取基线、tokens、`:verifyLiquidGlassSpec`、66 项 JVM 测试
（含新增 7 项传感器数学测试）、7 项圆角/裁切检查，以及 sample Debug 构建通过。
Debug APK：`sample/build/outputs/apk/debug/sample-debug.apk`。没有连接 ADB 设备，
未进行安装、真机倾斜、浅深色、滚动透景或后台停采样验收。
