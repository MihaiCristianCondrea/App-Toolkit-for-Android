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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.NavigationBackHandler
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.navigation3.scene.rememberNavigationEventState
import androidx.navigation3.scene.rememberSceneState
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.DirectNavigationEventInput
import androidx.navigationevent.NavigationEvent
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.NavigationEventTransitionState
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.NavigationBackHandler as SystemBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState as rememberSystemBackState
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ActivityMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.CrossActivityBackMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.LocalShellMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.rememberDeviceCornerShape
import kotlin.math.roundToInt

/**
 * A [NavDisplay] whose predictive back is Android's cross-activity back animation.
 *
 * `NavDisplay` alone seeks one `ContentTransform` with the gesture's progress and plays whatever is
 * left of it once back is invoked. Android's animation is not one timeline: while the finger is
 * down the pages move toward one set of rectangles, and once back is invoked they move from
 * wherever they are toward another, with a spring carrying the gesture's speed. No single
 * transition can seek both.
 *
 * So the display is only asked to show the two scenes. Its back handler listens on a private
 * dispatcher, and a handler on the system dispatcher forwards the gesture to it at progress zero,
 * which makes it compose the scene underneath and hold it, with a transition that changes
 * nothing. Every scene is wrapped in a layer that [CrossActivityBackMotion] places in its
 * rectangle each frame. When back is invoked the display completes the pop and holds both scenes
 * for the post-commit phase, which the motion plays from exactly where the gesture left them.
 *
 * When [activityBack] is false at the start of a gesture, the gesture is passed through untouched
 * and seeks [predictivePopTransitionSpec] instead, which suits a move between tabs. A back that is
 * not a gesture and reports no progress, such as a key, plays [popTransitionSpec].
 *
 * [backEnabled] false keeps the display from handling back at all. A display inside a scene that
 * is being covered, such as the tabs under a page that is still opening, is composed for the
 * length of the transition; without it, its handler, registered after the outer display's, would
 * take a back gesture meant for the page.
 *
 * Handlers composed inside the display, such as an inner display or a drawer, still use the
 * system dispatcher, and win over this one while they are enabled.
 *
 * Feeding a dispatcher through `DirectNavigationEventInput` is the technique of *3 unique
 * predictive back animations you can create with the Navigation Events library* (tunjid.com).
 */
@Composable
fun <T : Any> ShellNavDisplay(
    entries: List<NavEntry<T>>,
    sceneStrategies: List<SceneStrategy<T>>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    transitionSpec: AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform,
    popTransitionSpec: AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform,
    activityBack: () -> Boolean = { true },
    backEnabled: Boolean = true,
    predictivePopTransitionSpec: AnimatedContentTransitionScope<Scene<T>>.(swipeEdge: Int) -> ContentTransform =
        { popTransitionSpec() },
) {
    val scope = rememberCoroutineScope()
    val shellMotion = LocalShellMotion.current
    val density = LocalDensity.current
    val backMotion = remember(density) {
        with(density) {
            CrossActivityBackMotion(
                scope = scope,
                enteringStartOffsetPx = ActivityMotion.EnteringStartOffset.toPx(),
                displayMarginPx = ActivityMotion.DisplayBoundsMargin.toPx(),
            )
        }
    }
    backMotion.durationScale = shellMotion.durationScale
    backMotion.darkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    backMotion.mirrorRightEdge = shellMotion.followFingerFromRight

    val strategies = remember(sceneStrategies, backMotion) {
        sceneStrategies.map { MotionSceneStrategy(it, backMotion) }
    }
    val sceneState = rememberSceneState(entries = entries, sceneStrategies = strategies, onBack = onBack)
    val displayBackState = rememberNavigationEventState(sceneState)
    var size by remember { mutableStateOf(IntSize.Zero) }
    val currentActivityBack by rememberUpdatedState(activityBack)
    // The scene state is a new object whenever the entries change; the forwarder outlives it.
    val currentSceneState by rememberUpdatedState(sceneState)
    val forwarder = remember(backMotion) {
        GestureForwarder(
            motion = backMotion,
            activityBack = { currentActivityBack() },
            currentSceneKey = { currentSceneState.currentScene.key },
            displaySize = { size.toSize() },
        )
    }

    CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides forwarder) {
        NavigationBackHandler(sceneState, displayBackState, onBackCompleted = onBack)
    }

    val systemBackState = rememberSystemBackState(NavigationEventInfo.None)
    SystemBackHandler(
        state = systemBackState,
        isBackEnabled = backEnabled && sceneState.currentScene.previousEntries.isNotEmpty(),
        onBackCancelled = { forwarder.cancel() },
        onBackCompleted = { forwarder.complete() },
    )
    LaunchedEffect(systemBackState, forwarder) {
        snapshotFlow { systemBackState.transitionState }.collect { state ->
            if (state is NavigationEventTransitionState.InProgress) forwarder.progress(state.latestEvent)
        }
    }

    val currentPredictive by rememberUpdatedState(predictivePopTransitionSpec)
    NavDisplay(
        sceneState = sceneState,
        navigationEventState = displayBackState,
        modifier = modifier.onSizeChanged { size = it },
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = { swipeEdge ->
            if (forwarder.drivesMotion) {
                hold((CrossActivityBackMotion.PostCommitMillis * shellMotion.durationScale).roundToInt() + HoldMarginMillis)
            } else {
                currentPredictive(this, swipeEdge)
            }
        },
    )
}

/**
 * Receives the system's back events and replays them to the display's private dispatcher, handing
 * the gesture itself to the back motion.
 */
private class GestureForwarder(
    private val motion: CrossActivityBackMotion,
    private val activityBack: () -> Boolean,
    private val currentSceneKey: () -> Any,
    private val displaySize: () -> Size,
) : NavigationEventDispatcherOwner {
    private val input = DirectNavigationEventInput()
    override val navigationEventDispatcher = NavigationEventDispatcher().apply { addInput(input) }

    /** Whether the gesture under way plays the activity back motion rather than a seeked transition. */
    var drivesMotion = false
        private set
    private var started = false

    fun progress(event: NavigationEvent) {
        if (!started) {
            started = true
            drivesMotion = activityBack()
            if (drivesMotion) {
                motion.start(event, currentSceneKey(), displaySize())
                // The display composes the scene underneath and holds it where it is.
                input.backStarted(event.withProgress(0f))
            } else {
                input.backStarted(event)
            }
        }
        if (drivesMotion) motion.progress(event) else input.backProgressed(event)
    }

    fun complete() {
        if (started && drivesMotion) motion.commit(onFinished = {})
        // Without a gesture, this is a key or a button that reported no progress: the display
        // pops with its plain pop transition.
        input.backCompleted()
        started = false
    }

    fun cancel() {
        if (started) {
            if (drivesMotion) motion.cancel()
            input.backCancelled()
        }
        started = false
    }

    private fun NavigationEvent.withProgress(progress: Float) = NavigationEvent(
        touchX = touchX,
        touchY = touchY,
        progress = progress,
        swipeEdge = swipeEdge,
        frameTimeMillis = frameTimeMillis,
    )
}

/** Wraps every scene [inner] calculates in a layer the back motion can move. */
private class MotionSceneStrategy<T : Any>(
    private val inner: SceneStrategy<T>,
    private val motion: CrossActivityBackMotion,
) : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? =
        with(inner) { calculateScene(entries) }?.let { MotionScene(it, motion) }
}

/** [scene], drawn inside a [BackMotionLayer]. Equal to another wrapper of an equal scene. */
private class MotionScene<T : Any>(
    val scene: Scene<T>,
    private val motion: CrossActivityBackMotion,
) : Scene<T> {
    override val key: Any get() = scene.key
    override val entries: List<NavEntry<T>> get() = scene.entries
    override val previousEntries: List<NavEntry<T>> get() = scene.previousEntries
    override val metadata: Map<String, Any> get() = scene.metadata
    override val content: @Composable () -> Unit = { BackMotionLayer(motion, scene.key, scene.content) }

    override fun equals(other: Any?): Boolean = other is MotionScene<*> && other.scene == scene
    override fun hashCode(): Int = scene.hashCode()
}

/**
 * Places a scene in the rectangle the back motion gives it, clipped to the display's corners as a
 * window is. A scene that is not the closing one also draws the scrim over itself, which is where
 * the system puts it: under the closing window, over everything else.
 */
@Composable
private fun BackMotionLayer(motion: CrossActivityBackMotion, key: Any, content: @Composable () -> Unit) {
    val corners = rememberDeviceCornerShape()
    DisposableEffect(motion, key) { onDispose { motion.onSceneGone(key) } }
    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    if (motion.phase == CrossActivityBackMotion.Phase.Idle) return@graphicsLayer
                    val closing = key == motion.closingKey
                    val rect = if (closing) motion.closingRect() else motion.enteringRect()
                    if (size.width <= 0f) return@graphicsLayer
                    val scale = rect.width / size.width
                    transformOrigin = TransformOrigin(0f, 0f)
                    scaleX = scale
                    scaleY = scale
                    translationX = rect.left
                    translationY = rect.top
                    alpha = if (closing) motion.closingAlpha() else 1f
                    shape = corners
                    clip = true
                },
        ) {
            content()
        }
        Box(
            Modifier
                .fillMaxSize()
                .drawBehind {
                    if (key == motion.closingKey) return@drawBehind
                    val alpha = motion.scrimAlpha()
                    if (alpha > 0f) drawRect(Color.Black.copy(alpha = alpha))
                },
        )
    }
}

/**
 * A transition that moves nothing and lasts [durationMillis], so the display keeps both scenes
 * composed while the back motion moves them.
 */
private fun hold(durationMillis: Int): ContentTransform =
    fadeIn(tween(durationMillis), initialAlpha = HeldAlpha) togetherWith fadeOut(tween(durationMillis), targetAlpha = HeldAlpha)

/** Close enough to opaque to be invisible, far enough from it to make a transition of it. */
private const val HeldAlpha = 0.999f

/** The display outlasts the motion by a frame or two, so no page leaves before it has arrived. */
private const val HoldMarginMillis = 50
