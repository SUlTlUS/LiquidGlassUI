package dev.liquidglass27.glass

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Source coverage for popup.dart:PopupValueText and _buildRow Semantics. */
class PopupValueTextAndCheckedSemanticsTest {
    @Test
    fun valueSwitcherKeepsOriginalTimingOverlapBlurAndSizeEasing() {
        val content = File(
            "src/main/java/dev/liquidglass27/glass/GlassPopupValueText.kt",
        ).readText()
        assertTrue(content.contains("popupValueDurationMs = 300"))
        assertTrue(content.contains("popupValueBlurDp = 4f"))
        assertTrue(content.contains("tween(210, delayMillis = 90"))
        assertTrue(content.contains("tween(120, delayMillis = 180"))
        assertTrue(content.contains("SizeTransform(clip = false)"))
        assertTrue(content.contains("easeOutCubic"))
        assertTrue(content.contains("BlurredEdgeTreatment.Unbounded"))
        assertTrue(content.contains("maxLines = 1"))
        assertTrue(content.contains("TextOverflow.Ellipsis"))
    }

    @Test
    fun selectedRowsExposeCheckedSemanticsWithoutMarkingEveryItemAsCheckable() {
        val shared = File(
            "src/main/java/dev/liquidglass27/glass/MenuItemContent.kt",
        ).readText()
        val root = File("src/main/java/dev/liquidglass27/glass/GlassMenu.kt").readText()
        val child = File("src/main/java/dev/liquidglass27/glass/GlassSubmenu.kt").readText()
        assertTrue(shared.contains("if (action.selected) this.semantics"))
        assertTrue(shared.contains("toggleableState = ToggleableState.On"))
        assertFalse(shared.contains("toggleableState = ToggleableState.Off"))
        assertTrue(root.contains(".menuCheckedSemantics(action)"))
        assertTrue(child.contains(".menuCheckedSemantics(action)"))
        assertTrue(root.contains("role = Role.Button"))
        assertTrue(child.contains("role = Role.Button"))
    }
}
