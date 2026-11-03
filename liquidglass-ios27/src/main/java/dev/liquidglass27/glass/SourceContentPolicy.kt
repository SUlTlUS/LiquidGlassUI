package dev.liquidglass27.glass

import kotlin.math.abs

/**
 * Transparent source CONTENT only: never re-use a stale or differently sized
 * foreground when a popup animates its opening button into the menu.
 *
 * No background or glass material is captured by this policy.
 */
internal fun canUseSourceContentFrame(
    frameWidth: Int,
    frameHeight: Int,
    frameStyleKey: Int,
    frameTimestampNanos: Long,
    expectedWidth: Float,
    expectedHeight: Float,
    expectedStyleKey: Int,
    nowNanos: Long,
    maxAgeNanos: Long = 350_000_000L,
): Boolean {
    if (frameWidth <= 0 || frameHeight <= 0 ||
        !expectedWidth.isFinite() || !expectedHeight.isFinite() ||
        expectedWidth <= 0f || expectedHeight <= 0f ||
        frameStyleKey != expectedStyleKey || maxAgeNanos < 0L
    ) return false

    // Monotonic timestamps can roll over. A future or negative age should
    // fail closed, not accidentally resurrect an ancient source bitmap.
    val age = nowNanos - frameTimestampNanos
    return age >= 0L && age <= maxAgeNanos &&
        abs(frameWidth - expectedWidth) <= 2f &&
        abs(frameHeight - expectedHeight) <= 2f
}
