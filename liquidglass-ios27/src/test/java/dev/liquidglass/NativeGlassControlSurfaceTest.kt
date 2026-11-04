package dev.liquidglass

import android.content.Context
import android.graphics.Path
import android.graphics.RenderEffect
import java.lang.reflect.Modifier
import org.junit.Assert.*
import org.junit.Test

/** Native consumers must be able to link without a Composition or accessing internal symbols. */
class NativeGlassControlSurfaceTest {
    @Test fun nativeBridgeHasPublicAndroidOnlySignatures() {
        val type = NativeGlassControlSurface::class.java
        assertTrue(Modifier.isPublic(type.getConstructor(Context::class.java).modifiers))
        val primitiveFloat = Float::class.javaPrimitiveType!!
        val primitiveInt = Int::class.javaPrimitiveType!!
        assertEquals(Path::class.java, type.getMethod("contour", primitiveFloat, primitiveFloat, primitiveFloat).returnType)
        assertEquals(RenderEffect::class.java, type.getMethod("lightingEffect", primitiveInt, primitiveInt,
            primitiveFloat, primitiveFloat, Boolean::class.javaPrimitiveType!!, Path::class.java).returnType)
    }
}
