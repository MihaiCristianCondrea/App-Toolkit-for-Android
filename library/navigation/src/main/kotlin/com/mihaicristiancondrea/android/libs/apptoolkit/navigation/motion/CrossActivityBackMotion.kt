/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion

import android.os.SystemClock
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.util.VelocityTracker1D
import androidx.compose.ui.util.lerp
import androidx.navigationevent.NavigationEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * Android's default cross-activity back animation, driven by a back gesture and drawn on two
 * pages instead of two windows.
 *
 * This is a port of AOSP's `DefaultCrossActivityBackAnimation` and its base class
 * `CrossActivityBackAnimation` (`frameworks/base/libs/WindowManager/Shell/src/com/android/wm/
 * shell/back/`, Apache License 2.0). Like them it works in rectangles: every frame places the
 * closing and the entering page in a rectangle of the display, and the phases only differ in
 * where those rectangles come from.
 *
 * - **Pre-commit**, while the finger is down: both rectangles move from their start to their target
 *   by the gesture's progress, eased with `BACK_GESTURE`. The closing page shrinks to
 *   [MaxScale] and rests against the far edge (or stays centred for a swipe from the right
 *   edge); the entering page, [ActivityMotion.EnteringStartOffset] further out, shrinks with it.
 *   Both shift vertically with the finger, by as much as the closing page's *current* size leaves
 *   room for.
 * - **Post-commit**, once back is invoked: both rectangles start from exactly where the gesture
 *   left them. The entering page grows to fill the display and the closing page carries on,
 *   fading within the first fifth, over [PostCommitMillis] with the emphasized curve. A spring
 *   started with the gesture's velocity shrinks both a little further and lets them bounce back.
 * - **Cancel**: the system animates the progress back to zero, and the rectangles follow it.
 *
 * The progress this receives has already been smoothed by the platform's `BackProgressAnimator`,
 * as the system's own animation receives it; it is not smoothed again here.
 */
@Stable
class CrossActivityBackMotion internal constructor(
    private val scope: CoroutineScope,
    private val enteringStartOffsetPx: Float,
    private val displayMarginPx: Float,
) {
    /** Where the animation is. [Phase.Idle] draws both pages untouched. */
    var phase by mutableStateOf(Phase.Idle)
        private set

    /** The scene key of the page going away, while [phase] is not [Phase.Idle]. */
    var closingKey: Any? by mutableStateOf(null)
        private set

    var durationScale: Float = 1f
    var darkTheme: Boolean = false

    /**
     * Whether a swipe from the right edge mirrors a swipe from the left, so the page follows the
     * finger to the left edge and the page underneath waits on the right; the default. False plays
     * Android's own animation, which shrinks the page in place for a swipe from the right.
     */
    var mirrorRightEdge: Boolean = true

    /** The gesture under way came from the right edge and is drawn as a mirrored left swipe. */
    private var mirrored = false

    private var size = Size.Zero
    private var swipeEdge = NavigationEvent.EDGE_LEFT
    private var startTouchY = 0f
    private var touchY by mutableFloatStateOf(0f)
    private var gestureProgress by mutableFloatStateOf(0f)
    private val velocityTracker = VelocityTracker1D(isDataDifferential = false)

    private val postCommit = Animatable(0f)
    private val flingScale = Animatable(1f)
    private var commitClosing = Rect.Zero
    private var commitEntering = Rect.Zero
    private var commitJob: Job? = null

    /** A gesture began on the scene [closingKey], over a display of [displaySize]. */
    fun start(event: NavigationEvent, closingKey: Any, displaySize: Size) {
        finishNow()
        this.closingKey = closingKey
        closingGone = false
        size = displaySize
        mirrored = mirrorRightEdge && event.swipeEdge == NavigationEvent.EDGE_RIGHT
        swipeEdge = if (mirrored) NavigationEvent.EDGE_LEFT else event.swipeEdge
        startTouchY = event.touchY
        touchY = event.touchY
        gestureProgress = 0f
        velocityTracker.resetTracking()
        phase = Phase.Gesture
    }

    /** The gesture moved, or the system moved its progress, while cancelling included. */
    fun progress(event: NavigationEvent) {
        if (phase != Phase.Gesture) return
        gestureProgress = BackGesture.transform(event.progress.coerceIn(0f, 1f))
        touchY = event.touchY
        // Platforms before the timestamp API report no frame time; the time of arrival stands in.
        val time = event.frameTimeMillis.takeIf { it > 0L } ?: SystemClock.uptimeMillis()
        velocityTracker.addDataPoint(time, gestureProgress)
    }

    /**
     * Back was invoked: plays the post-commit phase from where the gesture left the pages, and
     * calls [onFinished] once both have arrived.
     */
    fun commit(onFinished: () -> Unit) {
        if (phase != Phase.Gesture) return
        commitClosing = rawClosingRect()
        commitEntering = rawEnteringRect()
        val velocity = runCatching { velocityTracker.calculateVelocity() }.getOrDefault(0f)
        // AOSP: velocity (progress per second) × SPRING_SCALE × (1 − MAX_SCALE), doubled for
        // swipes from a side edge, at least DEFAULT_FLING_VELOCITY for a barely started gesture.
        val edgeFactor = if (swipeEdge == NavigationEvent.EDGE_LEFT || swipeEdge == NavigationEvent.EDGE_RIGHT) 2f else 1f
        var startVelocity = velocity * SpringScale * (1f - MaxScale) * edgeFactor
        if (gestureProgress < 0.1f) startVelocity = max(startVelocity, DefaultFlingVelocity)
        startVelocity = startVelocity.coerceIn(0f, MaxFlingVelocity)
        phase = Phase.PostCommit
        commitJob = scope.launch {
            postCommit.snapTo(0f)
            flingScale.snapTo(1f)
            launch {
                flingScale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
                    initialVelocity = -startVelocity / SpringScale,
                )
            }
            postCommit.animateTo(1f, tween((PostCommitMillis * durationScale).roundToInt(), easing = { it }))
            // The pages stay where the motion left them, the closing one invisible, until the
            // display lets the closing scene go. Going idle before that drew it, for a frame or
            // two, at full size over the page underneath: a flicker.
            withTimeoutOrNull(ClosingSceneGraceMillis) { snapshotFlow { closingGone }.first { it } }
            reset()
            onFinished()
        }
    }

    private var closingGone by mutableStateOf(false)

    /** The layer of the scene [key] left composition. */
    internal fun onSceneGone(key: Any) {
        if (key == closingKey) closingGone = true
    }

    /** The gesture was cancelled and the system has brought its progress back to zero. */
    fun cancel() {
        if (phase == Phase.Gesture) reset()
    }

    /** Jumps to the end of whatever is playing, so a new gesture can start. */
    private fun finishNow() {
        commitJob?.cancel()
        commitJob = null
        reset()
    }

    private fun reset() {
        phase = Phase.Idle
        closingKey = null
        gestureProgress = 0f
    }

    /** The closing page's rectangle, in pixels of the display. Read while drawing. */
    fun closingRect(): Rect = rawClosingRect().mirroredIf(mirrored)

    /** The entering page's rectangle, in pixels of the display. Read while drawing. */
    fun enteringRect(): Rect = rawEnteringRect().mirroredIf(mirrored)

    // The rectangles as AOSP computes them; a mirrored gesture flips them about the centre.
    private fun rawClosingRect(): Rect = when (phase) {
        Phase.Idle -> full()
        Phase.Gesture -> {
            val rect = lerp(full(), closingTarget(), gestureProgress)
            rect.translate(0f, yOffset(rect))
        }
        Phase.PostCommit -> {
            val target = full().translate(commitClosing.left + enteringStartOffsetPx, 0f)
            lerp(commitClosing, target, emphasized()).scaleCentered(fling())
        }
    }

    private fun rawEnteringRect(): Rect = when (phase) {
        Phase.Idle -> full()
        Phase.Gesture -> {
            val start = full().translate(-enteringStartOffsetPx, 0f)
            val rect = lerp(start, start.scaleCentered(MaxScale), gestureProgress)
            rect.translate(0f, yOffset(lerp(full(), closingTarget(), gestureProgress)))
        }
        Phase.PostCommit -> lerp(commitEntering, full(), emphasized()).scaleCentered(fling())
    }

    /** The closing page fades out within the first fifth of the post-commit phase. */
    fun closingAlpha(): Float = if (phase == Phase.PostCommit) max(1f - postCommit.value * 5f, 0f) else 1f

    /**
     * The black scrim between the closing page and everything under it: at its full strength
     * from the start of the gesture, fading out over the post-commit phase.
     */
    fun scrimAlpha(): Float {
        val max = if (darkTheme) MaxScrimAlphaDark else MaxScrimAlphaLight
        return when (phase) {
            Phase.Idle -> 0f
            Phase.Gesture -> max
            Phase.PostCommit -> max * (1f - postCommit.value)
        }
    }

    private fun full() = Rect(Offset.Zero, size)

    /** Flipped horizontally about the display's centre, when [mirror]. */
    private fun Rect.mirroredIf(mirror: Boolean): Rect {
        if (!mirror) return this
        val displayWidth = this@CrossActivityBackMotion.size.width
        return Rect(displayWidth - right, top, displayWidth - left, bottom)
    }

    private fun closingTarget(): Rect {
        val target = full().scaleCentered(MaxScale)
        // Scaled into the middle for a swipe from the right edge, and against the right edge,
        // less the margin, for any other.
        return if (swipeEdge != NavigationEvent.EDGE_RIGHT) {
            target.translate(size.width - target.right - displayMarginPx, 0f)
        } else {
            target
        }
    }

    /**
     * How far the pages follow the finger up or down: decelerating over half the display's
     * height, and never so far that the closing page, at its current size, passes the margin.
     */
    private fun yOffset(closing: Rect): Float {
        val height = size.height
        if (height <= 0f) return 0f
        val rawDelta = touchY - startTouchY
        val ratio = min(height / 2f, abs(rawDelta)) / (height / 2f)
        val room = max(0f, (height - closing.height) / 2f - displayMarginPx)
        return room * decelerate(ratio) * sign(rawDelta)
    }

    private fun emphasized() = ActivityMotion.Emphasized.transform(postCommit.value)

    private fun fling() = min(flingScale.value, 1f)

    enum class Phase { Idle, Gesture, PostCommit }

    companion object {
        /** `MAX_SCALE`: how small the closing page gets. */
        const val MaxScale = 0.9f

        /** `POST_COMMIT_DURATION` of the default cross-activity animation. */
        const val PostCommitMillis = 450

        private const val MaxScrimAlphaDark = 0.8f
        private const val MaxScrimAlphaLight = 0.2f
        private const val SpringScale = 100f
        private const val MaxFlingVelocity = 1000f
        private const val DefaultFlingVelocity = 120f

        /** How long the finished motion waits for the display to remove the closing scene. */
        private const val ClosingSceneGraceMillis = 1_000L

        /** `Interpolators.BACK_GESTURE`, a `PathInterpolator(0.1, 0.1, 0, 1)`. */
        private val BackGesture = CubicBezierEasing(0.1f, 0.1f, 0f, 1f)

        /** `DecelerateInterpolator()` with its default factor of 1. */
        private fun decelerate(x: Float) = 1f - (1f - x) * (1f - x)
    }
}

private fun lerp(start: Rect, stop: Rect, fraction: Float) = Rect(
    left = lerp(start.left, stop.left, fraction),
    top = lerp(start.top, stop.top, fraction),
    right = lerp(start.right, stop.right, fraction),
    bottom = lerp(start.bottom, stop.bottom, fraction),
)

private fun Rect.scaleCentered(scale: Float): Rect {
    val halfWidth = width * scale / 2f
    val halfHeight = height * scale / 2f
    return Rect(center.x - halfWidth, center.y - halfHeight, center.x + halfWidth, center.y + halfHeight)
}
