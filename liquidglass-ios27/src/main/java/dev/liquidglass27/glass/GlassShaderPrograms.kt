package dev.liquidglass27.glass

import android.content.Context
import android.graphics.RuntimeShader
import android.util.Log
import java.util.concurrent.ConcurrentHashMap
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
            }
        }
    }

    fun obtain(context: Context, asset: String): RuntimeShader =
        prepared.remove(asset) ?: create(context, asset)

    /** Compile-time specialization keeps the ordinary 64-ray control program unchanged. */
    fun obtainControl(context: Context, profileSamples: Int): RuntimeShader {
        if (profileSamples == 64) return obtain(context, Control)
        require(profileSamples == 256)
        val key = "$Control:$profileSamples"
        return RuntimeShader(sources.computeIfAbsent(key) {
            context.assets.open(Control).bufferedReader().use { it.readText() }
                .replace("const int PROFILE_PACKS = 16;", "const int PROFILE_PACKS = 64;")
                .replace("const float PROFILE_SAMPLES = 64.0;", "const float PROFILE_SAMPLES = 256.0;")
                .replace("uniform float4 uProfile[16];", "uniform float4 uProfile[64];")
        })
    }

    private fun create(context: Context, asset: String): RuntimeShader = RuntimeShader(
        sources.computeIfAbsent(asset) {
            context.assets.open(it).bufferedReader().use { reader -> reader.readText() }
        },
    )
}
