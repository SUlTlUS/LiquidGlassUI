package dev.liquidglass27.glass

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * FlClash popup.dart::_RowPressRecognizer / _endPress: a drag outside the
 * card does NOT dismiss the popup. The background barrier closes on TAP only.
 */
class MenuDragDismissParityTest {
    private fun target(kind: TargetKind): MenuTouchTarget = MenuTouchTarget(kind)

    @Test
    fun draggingActiveRowOutsideDoesNotCloseMenu() {
        for (down in listOf(
            TargetKind.Row, TargetKind.DisabledRow, TargetKind.Header,
            TargetKind.Blank, TargetKind.Back,
        )) {
            for (wasDrag in listOf(false, true)) {
                assertFalse(
                    "A $down pointer released outside must leave the menu mounted",
                    menuReleaseDismisses(
                        target(down), target(TargetKind.Outside), wasDrag,
                    ),
                )
            }
        }
    }

    @Test
    fun onlyCleanOutsideTapDismissesPopup() {
        assertTrue(menuReleaseDismisses(
            target(TargetKind.Outside), target(TargetKind.Outside), false,
        ))
        assertFalse(menuReleaseDismisses(
            target(TargetKind.Outside), target(TargetKind.Outside), true,
        ))
        assertFalse(menuReleaseDismisses(
            target(TargetKind.Outside), target(TargetKind.Blank), false,
        ))
        assertFalse(menuReleaseDismisses(
            target(TargetKind.Outside), target(TargetKind.Row), true,
        ))
    }

    @Test
    fun draggingFromMenuRowToCoveredParentDoesNotFoldSubmenu() {
        assertFalse(menuReleaseFoldsParent(
            target(TargetKind.Row), target(TargetKind.Back), true,
        ))
        assertFalse(menuReleaseFoldsParent(
            target(TargetKind.Header), target(TargetKind.Back), false,
        ))
        assertTrue(menuReleaseFoldsParent(
            target(TargetKind.Back), target(TargetKind.Back), false,
        ))
        assertFalse(menuReleaseFoldsParent(
            target(TargetKind.Back), target(TargetKind.Back), true,
        ))
        assertFalse(menuReleaseFoldsParent(
            target(TargetKind.Back), target(TargetKind.Outside), false,
        ))
    }

    @Test
    fun backdropOrMenuPaddingDragCannotAccidentallySelectRow() {
        for (kind in listOf(TargetKind.Outside, TargetKind.Blank, TargetKind.Back)) {
            assertFalse(menuCanReleaseRowFrom(target(kind)))
        }
        for (kind in listOf(TargetKind.Row, TargetKind.DisabledRow, TargetKind.Header)) {
            assertTrue(menuCanReleaseRowFrom(target(kind)))
        }
    }

    @Test
    fun activeMenuPointerReleasesElasticWithoutCallingDismissOnDrag() {
        val gesture = File(
            "src/main/java/dev/liquidglass27/glass/GlassMenuGestures.kt",
        ).readText()
        assertTrue(gesture.contains("val downTarget = hitAt(down.position)"))
        assertTrue(gesture.contains("val arena = MenuGestureArena("))
        assertTrue(gesture.contains("arena.finishAt(last)"))
        assertTrue(gesture.contains("arena.mayDismissOutside(released)"))
        assertTrue(gesture.contains("val rowCandidate = menuCanReleaseRowFrom(downTarget)"))
        assertTrue(gesture.contains("arena.mayFoldParent(released)"))
        assertTrue(gesture.contains("if (elasticHeld) {\n                            finishPress()"))
        // The old bug was an unconditional dismissal on release, even for
        // gestures that started inside the card and dragged off the edge.
        assertFalse(gesture.contains("TargetKind.Outside -> dismiss()"))
        assertFalse(gesture.contains("TargetKind.Back -> back()"))
    }
}
