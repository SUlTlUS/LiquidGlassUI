package dev.liquidglass.sample

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import dev.liquidglass.coloros17.*
import dev.liquidglass.ios27.LocalIos27PageColors
import com.zeaze.tianyinwallpaper.backdrop.backdrops.layerBackdrop

/** This page hosts the original Settings objects. It does not emulate COUI in Compose. */
@Composable
internal fun ColorOs17ExamplePage(spec: ColorOs17ComponentSpec, onBack: () -> Unit) {
    val host = LocalContext.current
    val configuration = LocalConfiguration.current
    val dark = !MaterialTheme.colors.isLight
    val colors = LocalIos27PageColors.current
    var state by remember(host, spec.key, dark, configuration) {
        mutableStateOf<Result<ColorOs17SettingsComponents>?>(null)
    }
    var status by rememberSaveable(spec.key) { mutableStateOf("等待原组件调用") }
    LaunchedEffect(host, spec.key, dark, configuration) {
        val result = ColorOs17SettingsComponents.open(host, dark)
        // Publish on main only; disposal below closes the session and its windows.
        state = result
    }
    val session = state?.getOrNull()
    DisposableEffect(session) { onDispose { session?.close() } }
    ExampleDesignPage(background = colors.page) { topInset ->
        val backdrop = rememberExamplePageBackdrop(colors.page)
        // Only record the plain background; COUI may perform its own window background blur.
        Box(Modifier.fillMaxSize().layerBackdrop(backdrop).background(colors.page))
        val systemDensity = Density(host.resources.displayMetrics.density, configuration.fontScale)
        CompositionLocalProvider(LocalDensity provides systemDensity) {
            val nativeTopInset = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(top = nativeTopInset + 72.dp, bottom = 32.dp)
                .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(spec.category, color = colors.secondaryLabel)
                Text(spec.title, color = colors.label)
                Text(spec.className, color = colors.secondaryLabel)
                when {
                    state == null -> CircularProgressIndicator()
                    session == null -> Text("无法调用原组件：\n" +
                        ColorOs17SettingsComponents.failure(state!!.exceptionOrNull()!!), color = colors.label)
                    else -> key(session, spec.key) {
                        AndroidView(
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            factory = { context ->
                                // Empty host is not a component fallback; errors are shown in Compose.
                                FrameLayout(context).also { container ->
                                    val popup = spec.kind in setOf(ColorOs17ComponentKind.Dialog,
                                        ColorOs17ComponentKind.Sheet, ColorOs17ComponentKind.Menu)
                                    val result = session.createView(if (popup)
                                        ColorOs17ComponentCatalog.find("coloros17-button")!! else spec) { status = it }
                                    result.onSuccess { view ->
                                        if (popup) {
                                            (view as TextView).text = "打开${spec.title}"
                                            view.setOnClickListener { anchor ->
                                                session.showPopup(spec, anchor) { status = it }
                                                    .onSuccess { status = "原组件接口已执行，视觉效果请现场检查" }
                                                    .onFailure { status = "调用失败：" + ColorOs17SettingsComponents.failure(it) }
                                            }
                                        }
                                        container.addView(view, FrameLayout.LayoutParams(
                                            if (spec.key in setOf("coloros17-switch", "coloros17-checkbox", "coloros17-stepper"))
                                                FrameLayout.LayoutParams.WRAP_CONTENT else FrameLayout.LayoutParams.MATCH_PARENT,
                                            FrameLayout.LayoutParams.WRAP_CONTENT))
                                        // Avoid state writes during factory's composition/layout phase.
                                        container.post { status = "已创建：${view.javaClass.name}" }
                                    }.onFailure { error ->
                                        container.post { status = "调用失败：" + ColorOs17SettingsComponents.failure(error) }
                                    }
                                }
                            },
                            onRelease = { it.removeAllViews() },
                        )
                        Text("设置版本：${session.settingsVersion}", color = colors.secondaryLabel)
                        Text(status, color = colors.label)
                    }
                }
                Text("仅 ColorOS 17；直接使用已安装设置包的代码与资源。" +
                    "不重画控件、不套液态玻璃材质、不改变系统设置。", color = colors.secondaryLabel)
            }
        }
        ExampleTopChrome(backdrop, isLightTheme = !dark, appBarHeight = topInset + 48.dp)
        Ios27BackButton(onBack, backdrop,
            Modifier.align(Alignment.TopStart).padding(start = 16.dp, top = topInset + 4.dp))
    }
}
