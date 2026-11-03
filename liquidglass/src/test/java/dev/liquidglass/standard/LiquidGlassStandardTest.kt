package dev.liquidglass.standard

import org.junit.Assert.assertEquals
import org.junit.Test

class LiquidGlassStandardTest {
    @Test fun oldAndroidKeepsTheLegacyPath() {
        for (sdk in 24..32) {
            assertEquals(LiquidGlassStandard.Ios26, LiquidGlassStandard.Ios27.supportedOn(sdk))
        }
    }

    @Test fun modernAndroidHonorsEitherExplicitStandard() {
        for (sdk in 33..37) {
            for (standard in LiquidGlassStandard.entries) {
                assertEquals(standard, standard.supportedOn(sdk))
            }
        }
    }
}
