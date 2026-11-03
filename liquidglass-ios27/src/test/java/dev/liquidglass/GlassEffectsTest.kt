package dev.liquidglass

import org.junit.Assert.assertEquals
import org.junit.Test

class GlassEffectsTest {
    @Test fun dragLimitDefaultsOnAndIsIndependentOfStandardOverrides() {
        assertEquals(true, GlassEffects().limitButtonDrag)
        val effects = GlassEffects(limitButtonDrag = false)
        for (standard in GlassStandard.entries) {
            val changed = effects.copy(material = standard, highlight = standard, buttonDragEffect = standard)
            assertEquals(false, changed.limitButtonDrag)
            assertEquals(standard, changed.buttonAnimation(standard))
        }
    }

    @Test fun buttonMotionFollowsOverallStandardAndExplicitOverridesRemainIndependent() {
        for (standard in GlassStandard.entries) {
            assertEquals(standard, GlassEffects().buttonAnimation(standard))
            for (material in GlassStandard.entries) {
                assertEquals(standard, GlassEffects(material = material).buttonAnimation(standard))
            }
        }
        for (animation in GlassStandard.entries) for (standard in GlassStandard.entries) {
            val effects = GlassEffects(buttonDragEffect = animation)
            assertEquals(animation, effects.buttonAnimation(standard))
            assertEquals(standard, effects.materialFor(standard))
            assertEquals(standard, effects.highlightFor(standard))
        }
    }

    @Test fun unsetEffectsFollowEitherOverallStandard() {
        for (standard in GlassStandard.entries) {
            assertEquals(standard, GlassEffects().materialFor(standard))
            assertEquals(standard, GlassEffects().highlightFor(standard))
        }
    }

    @Test fun highlightOverrideDoesNotChangeMaterial() {
        for (standard in GlassStandard.entries) for (highlight in GlassStandard.entries) {
            val effects = GlassEffects(highlight = highlight)
            assertEquals(standard, effects.materialFor(standard))
            assertEquals(highlight, effects.highlightFor(standard))
        }
    }

    @Test fun explicitOverridesSurviveAnOverallChangeUntilCleared() {
        val effects = GlassEffects(material = GlassStandard.Ios26, highlight = GlassStandard.Ios27)
        for (standard in GlassStandard.entries) {
            assertEquals(GlassStandard.Ios26, effects.materialFor(standard))
            assertEquals(GlassStandard.Ios27, effects.highlightFor(standard))
        }
        val cleared = effects.copy(highlight = null)
        assertEquals(GlassStandard.Ios26, cleared.highlightFor(GlassStandard.Ios26))
    }
}
