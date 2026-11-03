package dev.liquidglass27.glass

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.node.*
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize

/** Compose pointer input with a hit margin, without changing layout or anchors. */
internal fun Modifier.expandedTouchInput(expansion: Dp, vararg keys: Any?,
    shareWithSiblings: Boolean = false,
    handler: PointerInputEventHandler): Modifier = then(ExpandedInput(expansion, keys.toList(), shareWithSiblings, handler))

private data class ExpandedInput(val expansion: Dp, val keys: List<Any?>, val share: Boolean,
    val handler: PointerInputEventHandler) : ModifierNodeElement<ExpandedInputNode>() {
    override fun create() = ExpandedInputNode(expansion, keys, share, handler)
    override fun update(node: ExpandedInputNode) {
        node.expansion = expansion
        node.share = share
        if (node.keys != keys) {
            node.keys = keys
            node.input.pointerInputEventHandler = handler
            node.input.resetPointerInputHandler()
        }
    }
    override fun InspectorInfo.inspectableProperties() { name = "expandedTouchInput" }
}

private class ExpandedInputNode(var expansion: Dp, var keys: List<Any?>, var share: Boolean, handler: PointerInputEventHandler) :
    DelegatingNode(), PointerInputModifierNode {
    val input = delegate(SuspendingPointerInputModifierNode(handler))
    override fun sharePointerInputWithSiblings() = share
    override val touchBoundsExpansion: TouchBoundsExpansion get() {
        val margin = with(requireDensity()) { expansion.roundToPx() }
        return TouchBoundsExpansion.Absolute(margin, margin, margin, margin)
    }
    override fun onPointerEvent(pointerEvent: PointerEvent, pass: PointerEventPass, bounds: IntSize) =
        input.onPointerEvent(pointerEvent, pass, bounds)
    override fun onCancelPointerInput() = input.onCancelPointerInput()
}
