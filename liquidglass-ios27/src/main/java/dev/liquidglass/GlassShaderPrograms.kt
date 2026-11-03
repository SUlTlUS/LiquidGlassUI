package dev.liquidglass

import android.content.Context
import android.graphics.RuntimeShader
import android.util.Log
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Immutable source is shared; each pane owns its mutable shader uniforms. */
@androidx.annotation.RequiresApi(33)
internal object GlassShaderPrograms {
    const val Control = "shaders/flclash_control_highlight.agsl"
    const val Material = "shaders/flclash_glass.agsl"
    private val sources = ConcurrentHashMap<String, String>()
    private val prepared = ConcurrentHashMap<String, RuntimeShader>()
    // The dock has two visible surfaces. Never share their mutable uniforms.
    private val preparedDockControls = ConcurrentLinkedQueue<RuntimeShader>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var started = false

    @Synchronized
    fun prepare(context: Context) {
        if (started) return
        started = true
        val app = context.applicationContext
        scope.launch {
            for (asset in listOf(Control, Material)) {
                runCatching { create(app, asset) }.onSuccess {
                    prepared.putIfAbsent(asset, it)
                }.onFailure { Log.e("LiquidGlass27", "Shader preparation failed: $asset", it) }
                if (asset == Control) {
                    repeat(2) {
                        runCatching { create(app, Control) }
                            .onSuccess { preparedDockControls.add(it) }
                            .onFailure { Log.e("LiquidGlass27", "Dock shader preparation failed", it) }
                    }
                }
            }
        }
    }

    fun obtain(context: Context, asset: String): RuntimeShader =
        prepared.remove(asset) ?: create(context, asset)

    /** Dedicated owners for the dock's two original corner-geometry surfaces. */
    fun obtainDockControl(context: Context): RuntimeShader =
        preparedDockControls.poll() ?: create(context, Control)

    private fun create(context: Context, asset: String): RuntimeShader = RuntimeShader(
        sources.computeIfAbsent(asset) {
            context.assets.open(it).bufferedReader().use { reader -> reader.readText() }
        },
    )
}
