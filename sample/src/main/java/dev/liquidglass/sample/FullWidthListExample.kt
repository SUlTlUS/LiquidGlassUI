package dev.liquidglass.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.liquidglass.ios27.Ios27DisclosureIndicator
import dev.liquidglass.ios27.Ios27ListRows
import dev.liquidglass.ios27.Ios27Tokens
import dev.liquidglass.ios27.LocalIos27PageColors
import dev.liquidglass.ios27.ios27ListPressMask

/**
 * Native implementation of 12740:24224, List / Type=Full Width.
 *
 * The list intentionally has no rounded group container: the six 68pt rows
 * reach the page edges and only the row contents keep the 16pt inset. Every
 * row supports the source left-swipe reveal state; the three actions are real
 * buttons and never appear as a permanently inserted extra row.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun FullWidthListExample(
    modifier: Modifier = Modifier,
    selectedAction: Int,
    onRowClick: (String) -> Unit,
    onActionSelected: (Int) -> Unit,
) {
    val colors = LocalIos27PageColors.current
    val listSurface = if (colors.page == Ios27Tokens.PageDark) colors.page else colors.group
    val detailGlyph = rememberSceneText("12740-24224.json", "Detail")
    val actionSymbolGlyph = rememberSceneText("12740-24224.json", "Symbol")
    val actionLabelGlyph = rememberSceneText("12740-24224.json", "Label")
    var openRow by rememberSaveable { mutableIntStateOf(-1) }
    var revealingRow by rememberSaveable { mutableIntStateOf(-1) }
    Column(
        modifier.fillMaxWidth().background(listSurface)
            .semantics { contentDescription = "List Full Width" },
    ) {
        Ios27ListRows(
            items = (0..5).toList(),
            showSeparator = { index -> index != revealingRow && index != revealingRow + 1 },
            roundedPressMask = false,
        ) { index ->
            FullWidthSwipeRow(
                rowIndex = index,
                isOpen = openRow == index,
                activeRevealRow = revealingRow,
                selectedAction = selectedAction,
                detailGlyph = detailGlyph,
                actionSymbolGlyph = actionSymbolGlyph,
                actionLabelGlyph = actionLabelGlyph,
                listSurface = listSurface,
                onOpenChanged = { open ->
                    if (open) openRow = index
                    else if (openRow == index) openRow = -1
                },
                onRevealChanged = { revealing ->
                    if (revealing) {
                        // Starting a new swipe immediately owns the single reveal slot.
                        // Any previously open/revealing row observes this change and closes.
                        revealingRow = index
                        if (openRow != index) openRow = -1
                    } else if (revealingRow == index) {
                        revealingRow = -1
                    }
                },
                onRowClick = { onRowClick("Title ${index + 1}") },
                onActionSelected = {
                    onActionSelected(it)
                    openRow = -1
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FullWidthSwipeRow(
    rowIndex: Int,
    isOpen: Boolean,
    activeRevealRow: Int,
    selectedAction: Int,
    detailGlyph: SceneText?,
    actionSymbolGlyph: SceneText?,
    actionLabelGlyph: SceneText?,
    listSurface: Color,
    onOpenChanged: (Boolean) -> Unit,
    onRevealChanged: (Boolean) -> Unit,
    onRowClick: () -> Unit,
    onActionSelected: (Int) -> Unit,
) {
    val colors = LocalIos27PageColors.current
    val density = LocalDensity.current
    val revealPx = with(density) { 124.dp.toPx() }
    val swipeState = rememberSwipeableState(if (isOpen) RevealState.Open else RevealState.Closed)
    val offset = swipeState.offset.value
    val progress = if (offset.isNaN()) 0f else (-offset / revealPx).coerceIn(0f, 1f)
    val contentWidth = 402.dp - 102.dp * progress
    val isRevealing = isOpen || progress > 0.001f
    val rowInteractions = remember { MutableInteractionSource() }
    val rowPressed by rowInteractions.collectIsPressedAsState()
    val rowShape = if (isRevealing) {
        RoundedCornerShape(topEnd = 34.dp, bottomEnd = 34.dp)
    } else {
        RoundedCornerShape(0.dp)
    }
    val dark = colors.page == Ios27Tokens.PageDark
    val draggedSurface = if (dark) Color(0xFF2C2C2E) else Color(0xFFE5E5EA)

    LaunchedEffect(isOpen) {
        swipeState.animateTo(if (isOpen) RevealState.Open else RevealState.Closed)
    }
    LaunchedEffect(activeRevealRow) {
        if (activeRevealRow != -1 && activeRevealRow != rowIndex && isRevealing) {
            swipeState.animateTo(RevealState.Closed)
        }
    }
    LaunchedEffect(isRevealing) {
        onRevealChanged(isRevealing)
    }
    LaunchedEffect(swipeState) {
        snapshotFlow { swipeState.targetValue }.collect { target ->
            onOpenChanged(target == RevealState.Open)
        }
    }

    val actionColors = listOf(
        if (dark) Color(0xFF8E8E93) else Color(0xFFAEAEB2),
        colors.accent.copy(alpha = 1f),
        (if (dark) Ios27Tokens.GreenDark else Ios27Tokens.GreenLight).copy(alpha = 1f),
    )
    Box(Modifier.fillMaxWidth().height(Ios27Tokens.TallRowHeight.dp)) {
        if (progress > 0f || isOpen) {
            Row(
                // Keep the actions behind the moving row surface. The row itself
                // is the reveal mask, so the three actions appear progressively
                // instead of popping in as a complete group.
                Modifier.align(Alignment.CenterEnd).padding(end = 16.dp).zIndex(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                actionColors.forEachIndexed { index, color ->
                    val actionInteractions = remember { MutableInteractionSource() }
                    val actionPressed by actionInteractions.collectIsPressedAsState()
                    Box(
                        Modifier.width(60.dp).height(68.dp)
                            .clickable(
                                interactionSource = actionInteractions,
                                indication = null,
                            ) { onActionSelected(index) }
                            .semantics {
                                contentDescription = "Action ${index + 1}"
                                role = Role.Button
                                selected = selectedAction == index
                            },
                    ) {
                        Box(
                            Modifier.offset(y = 6.dp).width(60.dp).height(38.dp)
                                .clip(RoundedCornerShape(19.dp))
                                .background(color)
                                .background(if (actionPressed) Color.White.copy(alpha = 0.18f) else Color.Transparent),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (actionSymbolGlyph != null) {
                                SceneGlyphText(actionSymbolGlyph, Color.White)
                            } else {
                                Text("◌", color = Color.White, style = Ios27Tokens.Type.Title3)
                            }
                        }
                        Box(Modifier.offset(y = 49.dp).width(60.dp).height(15.dp), contentAlignment = Alignment.TopCenter) {
                            if (actionLabelGlyph != null) {
                                SceneGlyphText(actionLabelGlyph, colors.secondaryLabel)
                            } else {
                                Text("Action", color = colors.secondaryLabel, style = Ios27Tokens.Type.Caption1)
                            }
                        }
                    }
                }
            }
        }
        Box(
            Modifier.width(contentWidth).fillMaxHeight()
                .offset { androidx.compose.ui.unit.IntOffset(if (offset.isNaN()) 0 else offset.toInt(), 0) }
                // Pressed carries alpha; composite it first so actions behind the
                // row cannot bleed through the gray reveal mask.
                // The list host already paints listSurface. Transparent resting
                // rows must not cover the previous row's half-separator mask.
                .background(if (isRevealing) draggedSurface else if (progress > 0f) listSurface else Color.Transparent, rowShape)
                .zIndex(3f)
                .swipeable(
                    state = swipeState,
                    anchors = mapOf(0f to RevealState.Closed, -revealPx to RevealState.Open),
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Horizontal,
                )
                .clickable(
                    interactionSource = rowInteractions,
                    indication = null,
                ) { if (isOpen) onOpenChanged(false) else onRowClick() }
                .semantics {
                    contentDescription = "Title ${rowIndex + 1}"
                    role = Role.Button
                    stateDescription = if (isOpen) "操作已展开" else "操作已收起"
                    customActions = (0..2).map { index ->
                        CustomAccessibilityAction("Action ${index + 1}") { onActionSelected(index); true }
                    } + CustomAccessibilityAction(if (isOpen) "收起操作" else "展开操作") {
                        onOpenChanged(!isOpen); true
                    }
                },
        ) {
            Row(
                Modifier.fillMaxSize()
                    .ios27ListPressMask(rowPressed, rowShape)
                    .clip(rowShape)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Title", color = colors.label, style = Ios27Tokens.Type.Body)
                    Text("Subtitle", color = colors.secondaryLabel, style = Ios27Tokens.Type.Subheadline)
                }
                Spacer(Modifier.width(8.dp))
                if (detailGlyph != null) {
                    SceneGlyphText(detailGlyph, colors.secondaryLabel)
                } else {
                    Text("Detail", color = colors.secondaryLabel, style = Ios27Tokens.Type.Body)
                }
                Spacer(Modifier.width(16.dp))
                Ios27DisclosureIndicator(color = colors.secondaryLabel)
            }
        }
    }
}

private enum class RevealState { Closed, Open }
