package dev.liquidglass.rendering

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import androidx.compose.ui.graphics.Color
import com.kyant.backdrop.RuntimeShader
import kotlin.math.sqrt

/** Ported from LiquidTab's shared BloomTiltTracker and dual-light edge shader.
 * Clients register only for visible, attached glass; a process shares one sensor.
 */
internal object GyroscopeHighlight {
    private val clients = mutableSetOf<() -> Unit>()
    private val matrix = FloatArray(9)
    private var manager: SensorManager? = null
    private var listener: SensorEventListener? = null
    private val tilt = GyroscopeHighlightMath()

    fun register(context: Context, onChange: () -> Unit) {
        clients.add(onChange)
        if (listener != null) return
        val sensors = context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as? SensorManager ?: return
        val sensor = sensors.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
            ?: sensors.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) ?: return
        val callback = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                SensorManager.getRotationMatrixFromVector(matrix, event.values)
                if (tilt.sample(-matrix[6], -matrix[7])) clients.toList().forEach { it() }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        if (runCatching {
            sensors.registerListener(callback, sensor, SensorManager.SENSOR_DELAY_GAME, Handler(Looper.getMainLooper()))
        }.getOrDefault(false)) {
            manager = sensors
            listener = callback
        }
    }

    fun unregister(onChange: () -> Unit) {
        clients.remove(onChange)
        if (clients.isNotEmpty()) return
        listener?.let { manager?.unregisterListener(it) }
        listener = null
        manager = null
        tilt.reset()
    }

    fun configure(shader: RuntimeShader, width: Float, height: Float, rotation: Int, angle: Float, color: Color) {
        shader.setFloatUniform("halfView", width / 2f, height / 2f)
        // Alpha stays on the existing graphics layer/paint, so it is applied once.
        shader.setFloatUniform("highlightAlpha", 1f)
        shader.setColorUniform("strokeColor", color.copy(alpha = 1f))
        shader.setFloatUniform("strokeAlphaMul", .12f)
        val light = tilt.light(rotation, angle)
        shader.setFloatUniform("lightDir1", light[0], light[1], light[2])
        shader.setColorUniform("lightColor1", Color.White)
        shader.setFloatUniform("lightIntensity1", 1f)
        val secondaryLength = sqrt(.1f * .1f + .5f * .5f)
        shader.setFloatUniform("lightDir2", 0f, .1f / secondaryLength, -.5f / secondaryLength)
        shader.setColorUniform("lightColor2", Color.White)
        shader.setFloatUniform("lightIntensity2", .4f)
    }
}

// LiquidTab BLOOM_STROKE_DUAL_SHADER. The existing Canvas outline owns geometry.
internal const val GyroscopeHighlightShader = """
uniform float2 halfView;
uniform float highlightAlpha;
layout(color) uniform half4 strokeColor;
uniform float strokeAlphaMul;
uniform float3 lightDir1;
layout(color) uniform half4 lightColor1;
uniform float lightIntensity1;
uniform float3 lightDir2;
layout(color) uniform half4 lightColor2;
uniform float lightIntensity2;
half4 main(float2 fragCoord) {
    float2 delta = fragCoord - halfView;
    float2 n = delta / max(length(delta), 0.001);
    half3 rgb = strokeColor.rgb * half(strokeAlphaMul);
    float l1 = dot(n, normalize(lightDir1.xy));
    rgb += half(l1 * l1 * lightIntensity1) * lightColor1.rgb;
    float l2 = dot(n, normalize(lightDir2.xy));
    rgb += half(l2 * l2 * lightIntensity2) * lightColor2.rgb;
    return half4(rgb * half(highlightAlpha), half(highlightAlpha));
}
"""
