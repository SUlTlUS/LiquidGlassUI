# Color Picker 弹窗材质

采样层级、移植方法和验收规则见 [Inline 与 Popup 接入方法](glass-backdrop-integration.md)。

宿主实现：`sample/src/main/java/dev/liquidglass/sample/Ios27ColorPickerDialog.kt`。
材质参数依据本地上游 `vendor/AndroidLiquidGlass/app/src/commonMain/kotlin/com/kyant/backdrop/catalog/destinations/DialogContent.kt`。

- 浅色表面为 `#FAFAFA`、60% alpha；深色为 `#121212`、40% alpha。
- 亮度浅色 0.2、深色 0；饱和度 1.5。
- 模糊浅色 16dp、深色 8dp；折射 24dp/48dp，启用 depthEffect。
- 保留 Color Picker 的面板尺寸、独立四角圆角和边缘高光。
- 底层页面持续录制到独立 LayerBackdrop；弹窗作为兄弟层读取。不要把弹窗或遮罩放入自身采样树。

玻璃区域下方若只有单色页面，输出也会接近单色；仅看见弹窗外的页面内容，不能证明弹窗内的动态折射已通过视觉验收。需要以玻璃覆盖范围内的内容变化作验证。

本次只调整宿主调用参数，未修改受保护的玻璃运行源码。

日期与时间的 Inline 使用上游 Dialog 材质作为实现基线：浅色 16dp、深色 8dp
模糊，折射 24dp/48dp。这里的深色和折射参数不是 Figma 原始值；本地 Figma
Glass Effect 读到 radius=16、refractionRadius=30，两套渲染器没有一一对应的参数。
浅色表面为 60% #FAFAFA 加 10% #BFBFBF Darken；深色为 40% #121212。

页面背景单独绘制到 `pickerBackgroundBackdrop`，日历作为兄弟层按布局坐标采样。
背景沿用页面的居中裁切，不能把整张图片拉伸到日历尺寸。日历自身不得进入这个
背景采样层。裁切放在 drawBackdrop 之后，只裁日历内容，保留外部阴影。
