package dev.liquidglass

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Flutter popup.dart::_pop: _pressed = step.index, keep _landed. */
class MenuFoldPressHandoffTest {
    private fun startedHeader(depth: Int): FlClashMenuPress =
        FlClashMenuPress().apply {
            start(MenuTouchTarget(TargetKind.Header, depth))
            completeDwell(MenuDwellAction.ToggleHeader)
        }

    @Test fun longHeldHeaderReturnsToOpeningRowWithoutReopening() {
        val handoff = MenuFoldPressHandoff()
        val held = startedHeader(1)
        handoff.completed(childDepth = 1, openingRow = 3)
        assertTrue(held.foldedToParent(handoff.latest!!))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 3), held.pressed)
        assertTrue(held.landed)
        assertFalse(held.canRunRelease())
        // This is the same index, so there must be no new hold/400ms timer.
        repeat(30) {
            assertFalse(held.move(MenuTouchTarget(TargetKind.Row, 0, 3), 0))
            assertTrue(held.landed)
        }
    }

    @Test fun foldingBeforeCompletionIgnoresParentRowsAndEmptyHitTransitions() {
        // Reproduction: around _foldSpring's last frames the Compose hit
        // level for the title vanishes before Flutter would remove it.
        // Old move() replaced _pressed(Header) with root Row or Blank,
        // clearing _landed and invalidating the completion handoff.
        val handoff = MenuFoldPressHandoff()
        val held = startedHeader(1)
        val noise = listOf(
            MenuTouchTarget(TargetKind.Header, 1),
            MenuTouchTarget(TargetKind.Blank, 1),
            MenuTouchTarget(TargetKind.Back),
            MenuTouchTarget(TargetKind.Outside),
            MenuTouchTarget(TargetKind.Row, 0, 3),
            MenuTouchTarget(TargetKind.Blank, 0),
            MenuTouchTarget(TargetKind.Row, 0, 1),
            MenuTouchTarget(TargetKind.Row, 0, 3),
        )
        repeat(20) {
            for (hit in noise) {
                assertFalse("intermediate fold must not create a new hold: $hit",
                    held.move(hit, if (hit.depth >= 0) hit.depth else 0))
                assertEquals(MenuTouchTarget(TargetKind.Header, 1), held.pressed)
                assertTrue(held.landed)
                assertFalse(held.canRunRelease())
            }
        }
        handoff.completed(1, 3)
        assertTrue(held.foldedToParent(handoff.latest!!))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 3), held.pressed)
        assertTrue(held.landed)
        repeat(100) {
            assertFalse(held.move(MenuTouchTarget(TargetKind.Row, 0, 3), 0))
            assertTrue(held.landed)
        }
    }

    @Test fun lateOutgoingHeaderFramesCannotUndoAlreadyDeliveredFoldHandoff() {
        val held = startedHeader(1)
        val handoff = MenuFoldPressHandoff()
        handoff.completed(1, 3)
        assertTrue(held.foldedToParent(handoff.latest!!))
        // Fold completion is published BEFORE the outgoing glass card has
        // necessarily left Compose's hit levels. Reject its stale header.
        repeat(35) {
            assertFalse(held.move(MenuTouchTarget(TargetKind.Header, 1), 1))
            assertFalse(held.move(MenuTouchTarget(TargetKind.Blank, 1), 1))
            assertFalse(held.move(MenuTouchTarget(TargetKind.Back), 1))
            assertEquals(MenuTouchTarget(TargetKind.Row, 0, 3), held.pressed)
            assertTrue(held.landed)
        }
        // Once the parent becomes the actual painted top, this same
        // opening row remains the same held press and cannot rearm.
        assertFalse(held.move(MenuTouchTarget(TargetKind.Row, 0, 3), 0))
        assertTrue(held.landed)
        assertTrue(held.move(MenuTouchTarget(TargetKind.Row, 0, 2), 0))
        assertFalse(held.landed)
    }

    @Test fun afterFoldCompletedOnlyAnActuallyDifferentRowCanRearmDwell() {
        val held = startedHeader(2)
        val handoff = MenuFoldPressHandoff()
        handoff.completed(2, 4)
        assertTrue(held.foldedToParent(handoff.latest!!))
        repeat(40) {
            assertFalse(held.move(MenuTouchTarget(TargetKind.Row, 1, 4), 1))
            assertFalse(held.canRunRelease())
        }
        assertTrue(held.move(MenuTouchTarget(TargetKind.Row, 1, 5), 1))
        assertTrue(held.canRunRelease())
        assertFalse(held.landed)
    }

    @Test fun previousFoldMustNotReapplyAfterNewDown() {
        val held = startedHeader(1)
        val handoff = MenuFoldPressHandoff()
        handoff.completed(1, 1)
        assertTrue(held.foldedToParent(handoff.latest!!))
        held.start(MenuTouchTarget(TargetKind.Row, 0, 1))
        assertFalse(held.foldedToParent(handoff.latest!!))
        assertTrue(held.canRunRelease())
        assertFalse(held.landed)
    }

    @Test fun openingChildDoesNotFreezeDragSelectionDuringUnfold() {
        val held = FlClashMenuPress()
        held.start(MenuTouchTarget(TargetKind.Row, 0, 2))
        held.completeDwell(MenuDwellAction.OpenChild)
        assertEquals(MenuTouchTarget(TargetKind.Header, 1), held.pressed)
        assertFalse(held.move(MenuTouchTarget(TargetKind.Back), 0))
        assertTrue(held.move(MenuTouchTarget(TargetKind.Row, 1, 0), 1))
        assertTrue(held.canRunRelease())
    }

    @Test fun normalAndCarriedPointerOnlyRehitOnActualPhysicalMove() {
        val source = File(
            "src/main/java/dev/liquidglass/GlassMenuGestures.kt",
        ).readText()
        assertTrue(source.contains("if (foldingHeaderDepth != null) return false"))
        assertTrue(source.contains("foldingHeaderDepth = pressed.depth"))
        assertTrue(source.contains("foldingHeaderDepth != event.childDepth"))
        assertTrue(source.contains("val physicallyMoved = event.position != position"))
        assertTrue(source.contains("if (physicallyMoved &&"))
        assertTrue(source.contains("change.position != change.previousPosition &&"))
        assertTrue(source.contains("update(hitAt(last))) armDwell()"))
    }

    @Test fun deliberateMoveToDifferentRowThenBackCanRearm() {
        val handoff = MenuFoldPressHandoff()
        val held = startedHeader(1)
        handoff.completed(1, 3)
        assertTrue(held.foldedToParent(handoff.latest!!))
        assertTrue(held.move(MenuTouchTarget(TargetKind.Row, 0, 2), 0))
        assertFalse(held.landed)
        assertTrue(held.canRunRelease())
        assertTrue(held.move(MenuTouchTarget(TargetKind.Row, 0, 3), 0))
        assertEquals(MenuTouchTarget(TargetKind.Row, 0, 3), held.pressed)
        // Only after a genuine different row may the original opener rearm.
        assertEquals(false, held.landed)
    }

    @Test fun deeperSubmenuReturnsToItsOwnParentEntry() {
        val handoff = MenuFoldPressHandoff()
        val held = startedHeader(3)
        handoff.completed(childDepth = 3, openingRow = 1)
        val event = handoff.latest!!
        assertEquals(3, event.childDepth)
        assertEquals(2, event.parentDepth)
        assertEquals(1, event.openingRow)
        assertTrue(held.foldedToParent(event))
        assertEquals(MenuTouchTarget(TargetKind.Row, 2, 1), held.pressed)
        assertFalse(held.move(MenuTouchTarget(TargetKind.Row, 2, 1), 2))
        assertFalse(held.canRunRelease())
    }

    @Test fun ordinaryHeaderTapWithoutDwellDoesNotTransfer() {
        val handoff = MenuFoldPressHandoff()
        handoff.completed(1, 3)
        val held = FlClashMenuPress().apply {
            start(MenuTouchTarget(TargetKind.Header, 1))
        }
        assertFalse(held.foldedToParent(handoff.latest!!))
        assertEquals(MenuTouchTarget(TargetKind.Header, 1), held.pressed)
        assertTrue(held.canRunRelease())
    }

    @Test fun staleAndDifferentLevelFoldCannotStealCurrentTouch() {
        val handoff = MenuFoldPressHandoff()
        handoff.completed(1, 2)
        val old = handoff.latest!!
        val held = startedHeader(2)
        assertFalse(held.foldedToParent(old))
        assertEquals(MenuTouchTarget(TargetKind.Header, 2), held.pressed)
        handoff.completed(2, 4)
        assertEquals(old.sequence + 1L, handoff.sequence())
        assertTrue(held.foldedToParent(handoff.latest!!))
        assertEquals(MenuTouchTarget(TargetKind.Row, 1, 4), held.pressed)
        assertFalse(held.foldedToParent(old))
        assertEquals(MenuTouchTarget(TargetKind.Row, 1, 4), held.pressed)
    }

    @Test fun invalidFoldRowsDoNotPublishEvents() {
        val handoff = MenuFoldPressHandoff()
        handoff.completed(0, 2)
        handoff.completed(1, -1)
        assertEquals(null, handoff.latest)
        assertEquals(0L, handoff.sequence())
        handoff.completed(1, 0)
        assertEquals(1L, handoff.sequence())
    }

    @Test fun aCompletelyNewDownResetsTheOldLandedGate() {
        val handoff = MenuFoldPressHandoff()
        val held = startedHeader(1)
        handoff.completed(1, 2)
        held.foldedToParent(handoff.latest!!)
        assertFalse(held.canRunRelease())
        // A NEW pointer (after previous Up) legitimately opens the entry.
        held.start(MenuTouchTarget(TargetKind.Row, 0, 2))
        assertFalse(held.landed)
        assertTrue(held.canRunRelease())
        assertFalse(held.move(MenuTouchTarget(TargetKind.Row, 0, 2), 0))
    }

    @Test fun firstAndDeepFoldCompletionPublishBeforeRemovingPaths() {
        val source = File(
            "src/main/java/dev/liquidglass/GlassMenu.kt",
        ).readText()
        val deep = source.substringAfter("fun popNested(")
            .substringBefore("var latestNested")
        assertTrue(deep.contains("foldPressHandoff.completed("))
        assertTrue(deep.indexOf("foldPressHandoff.completed(") <
            deep.indexOf("nestedPath = nestedPath.dropLast(1)"))
        val first = source.substringAfter("LaunchedEffect(submenuOpen, submenuIndex)")
            .substringBefore("BackHandler(enabled")
        assertTrue(first.contains("foldPressHandoff.completed("))
        assertTrue(first.indexOf("foldPressHandoff.completed(") <
            first.indexOf("submenuIndex = -1"))
        assertTrue(source.contains("foldPressHandoff = nestedMotionJobs.foldPressHandoff"))
    }

    @Test fun foldStateUsesExistingCapturedJobOwnerToAvoidAndroidVerifierCrash() {
        // The previous APK compiled and passed 267 JVM tests, yet ART on
        // API 36 rejected GlassMorphMenu$lambda$104 with VerifyError:
        // `type Float unexpected as arg to if-eqz/if-nez`. Introducing
        // an extra captured reference pushed its gigantic Compose closure
        // into the bad D8 register layout. Do not reintroduce that capture.
        val menu = File(
            "src/main/java/dev/liquidglass/GlassMenu.kt",
        ).readText()
        val owner = File(
            "src/main/java/dev/liquidglass/NestedMenuMotionJobs.kt",
        ).readText()
        assertFalse(menu.contains("val foldPressHandoff = remember { MenuFoldPressHandoff() }"))
        assertTrue(owner.contains("val foldPressHandoff = MenuFoldPressHandoff()"))
        assertTrue(menu.contains("foldPressHandoff = nestedMotionJobs.foldPressHandoff"))
        assertTrue(menu.contains("nestedMotionJobs.foldPressHandoff.completed("))
    }

    @Test fun bothNormalAndCarriedTouchConsumeTheSameFoldCompletion() {
        val source = File(
            "src/main/java/dev/liquidglass/GlassMenuGestures.kt",
        ).readText()
        val start = source.substringAfter("LaunchedEffect(carriedPress)")
            .substringBefore("Box(modifier")
        val normal = source.substringAfter("val downTarget = hitAt(down.position)")
        assertTrue(start.contains("applyCompletedFold()"))
        assertTrue(normal.contains("applyCompletedFold()"))
        assertTrue(source.contains("held.foldedToParent(folded)"))
        assertTrue(source.contains("seenFoldSequence = foldPressHandoff?.sequence() ?: 0L"))
        assertTrue(source.contains("hover(folded.parentDepth, folded.openingRow)"))
    }
}
