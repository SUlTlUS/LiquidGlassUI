# 列表二级页面

入口：组件 → 手机示例库 → List → Type=Inset。顶部返回按钮及 Android 系统返回先回到手机示例库；二级页不显示主页面底栏。原组件页的独立“列表二级页面 / Examples/List”入口已删除。

来源：用户提供的 `D:/下载/Untitled (1).fig`。离线提取保存在 `figma-secondary-source/`，原稿预览为 `thumbnail.png`，完整节点证据为 `decoded.json`。

目标节点 `38:2882`（Examples/List）：画布 402×874，分组 `38:2885` 宽 362、高 656、圆角 26。12 行按原顺序展示；第 2、3 行高 68，其余 52。分组相对画布左右边距 20，行内距 16。首行不画顶线。原稿英文 Title、Subtitle、Detail 等占位文案保留。

实现：`sample/src/main/java/dev/liquidglass/sample/IphoneExamples.kt` 将目录项 `12740-24203` 路由到 `ListDetailPage.kt`；二级页列表复用 `ExamplesListInset` 的 656pt 高度、26dp 圆角和内部配件规格，但横向框架统一到二级页的 16pt 内容边距，因此运行时宽度为 370pt。日期和时间可点击选择，收藏可切换，Pop-up 可选择，Stepper 行直接复用 `Ios27StepperExample`，开关维护状态，Button/Action/工具按钮提供演示反馈。旧的独立 Inset 入口和其旧页面路由已移除。工具栏图形按原稿占位符绘制，不赋予未定义的业务含义。

1.0.14 几何修正：二级页单独以 402 设计单位映射可用宽度，避免手机约 363dp 宽度挤压原稿。保留系统字体缩放。相对 Android 顶部安全区域：工具栏 y=0、高44；标题 y=54；分组 y=121。1.0.16 起忽略 Inset 原稿 toolbar 实例自身额外的 x=9 外层平移，使三种 List 顶部按钮统一为 leading x=16、trailing x=172/226。1.0.17 起 Inset 与外层分类二级页统一宽度计算顺序：先消费 Horizontal/Bottom safe inset，再把剩余宽度映射为 402pt。1.0.18 起进一步统一横向框架：Title、顶部返回与列表卡片都从 x=16 开始，Inset 卡片随页面 16pt 内容边距扩为 370pt；内部行高和配件规格保持不变。右侧工具组总宽214（44+10+160）。行内配件间距16，日期胶囊106×34、时间86×34、步进器92×32。普通行移除通用列表行的纵向 padding，防止开关撑高行并累积偏差。

以上是原稿逻辑几何对齐，不代表 SF Pro 字形、Android 系统栏及实际设备截图已经达到像素一致；仍需解锁设备后截图验收。

主题采用默认 Grouped，与设置页一致；深色分组为 #1C1C1E，不使用 SliderReference。状态栏及底部安全距离由 Android 提供，未复制 iPhone 状态栏、灵动岛或机身边框。字体沿用现有 Android 字体体系，未嵌入 SF Pro。

顶部渐进模糊和统一的 `Ios27BackButton` 位于页面录制层之外；返回按钮不参与 `LayerBackdrop` 录制。行内开关只采样独立的分组色 CanvasBackdrop。玻璃组件源码未修改。

构建及静态校验不能代替真机视觉验收；需在设备上检查浅/深色、二级页进入与返回、滚动模糊、日期时间选择和开关操作。
