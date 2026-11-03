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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.isSystemAnimationDisabled
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellBackHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellPlayer
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.R
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/** Height of the collapsed player pill. */
val MiniPlayerHeight: Dp = 64.dp

/** Space the content keeps free at its bottom while the mini player is docked over it. */
internal val MiniPlayerReserve: Dp = MiniPlayerHeight + 16.dp

/**
 * How far the player is open, from 0 (the docked pill) to 1 (the full window).
 *
 * A stable holder around an [Animatable], whose stability the Compose compiler can only decide at
 * runtime because it comes from another module: passed around as is, it kept the overlay and the
 * chrome from skipping. Reading [value] or [targetValue] is a snapshot read, as on the animatable.
 */
@Stable
internal class PlayerExpansion {
    private val animatable: Animatable<Float, AnimationVector1D> = Animatable(0f)

    val value: Float get() = animatable.value

    val targetValue: Float get() = animatable.targetValue

    suspend fun snapTo(target: Float) {
        animatable.snapTo(target)
    }

    suspend fun animateTo(target: Float, animationSpec: AnimationSpec<Float>) {
        animatable.animateTo(target, animationSpec)
    }
}

/**
 * Draws [player] over the shell.
 *
 * Collapsed, the player is a pill docked [dockBottom] above the window's bottom edge (the top of
 * the navigation bar, or of the system bar when there is none) and [dockStart] in from the start
 * edge (past a rail or drawer). Dragging it up, or tapping it, grows it into a full-window surface;
 * [expansion] runs from 0 to 1 along the way so the shell can move its own bars in step. When
 * [ShellPlayer.onDismiss] is provided, dragging the collapsed pill down past a deliberate distance
 * or velocity threshold dismisses it without disturbing [expansion]. Back collapses an expanded
 * player before it does anything else.
 */
@Composable
internal fun ShellPlayerOverlay(
    player: ShellPlayer,
    active: Boolean,
    expansion: PlayerExpansion,
    dockBottom: () -> Dp,
    dockStart: Dp,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val density = LocalDensity.current
    val durationScale = LocalShellSettings.current.animationSpeed.durationScale.coerceAtLeast(0.1f)
    val dismissOffset = remember { Animatable(0f) }
    val currentOnDismiss by rememberUpdatedState(player.onDismiss)
    val currentActive by rememberUpdatedState(active)
    var dismissInFlight by remember { mutableStateOf(value = false) }

    val settleSpec: () -> AnimationSpec<Float> = {
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow / (durationScale * durationScale),
        )
    }
    val settle: (Float) -> Unit = { target ->
        scope.launch {
            if (context.isSystemAnimationDisabled()) {
                expansion.snapTo(target)
            } else {
                expansion.animateTo(target, settleSpec())
            }
        }
    }
    val settleDismiss: (Float) -> Unit = { target ->
        scope.launch {
            if (context.isSystemAnimationDisabled()) {
                dismissOffset.snapTo(target)
            } else {
                dismissOffset.animateTo(target, settleSpec())
            }
        }
    }
    val completeDismiss: () -> Unit = {
        val callback = currentOnDismiss
        if (callback != null && (!dismissInFlight)) {
            dismissInFlight = true
            scope.launch {
                try {
                    callback()
                    withFrameNanos { }
                    if (currentActive) {
                        if (context.isSystemAnimationDisabled()) {
                            dismissOffset.snapTo(0f)
                        } else {
                            dismissOffset.animateTo(0f, settleSpec())
                        }
                    }
                } finally {
                    dismissInFlight = false
                }
            }
        }
    }

    LaunchedEffect(active) {
        if (active) {
            dismissOffset.snapTo(0f)
        } else {
            expansion.snapTo(0f)
            if (context.isSystemAnimationDisabled()) {
                dismissOffset.snapTo(0f)
            }
        }
    }

    val travel = { _: Int -> with(density) { (dockBottom() + MiniPlayerHeight + PlayerEntranceRise).roundToPx() } }
    val reducedMotion = context.isSystemAnimationDisabled()
    val enterTransition = if (reducedMotion) {
        EnterTransition.None
    } else {
        slideInVertically(
            spring(
                dampingRatio = 0.85f,
                stiffness = Spring.StiffnessMediumLow / (durationScale * durationScale),
            ),
            travel,
        ) + fadeIn(tween((240 * durationScale).roundToInt(), delayMillis = (40 * durationScale).roundToInt()))
    }
    val exitTransition = if (reducedMotion) {
        ExitTransition.None
    } else {
        slideOutVertically(tween((280 * durationScale).roundToInt()), travel) +
            fadeOut(tween((220 * durationScale).roundToInt(), delayMillis = (40 * durationScale).roundToInt()))
    }
    AnimatedVisibility(
        visible = active,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        DisposableEffect(Unit) {
            onDispose {
                scope.launch { dismissOffset.snapTo(0f) }
            }
        }
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val fullHeight: Dp = maxHeight
            val travelPx = { with(density) { (fullHeight - MiniPlayerHeight - dockBottom()).toPx().coerceAtLeast(1f) } }
            val dismissTravelPx = { with(density) { DismissTravel.toPx().coerceAtLeast(1f) } }
            val dragInteraction = remember { DragCancellationTracker() }
            var gesture by remember { mutableStateOf(PlayerDragGesture.Undecided) }
            val dragState = rememberDraggableState { delta ->
                if (gesture == PlayerDragGesture.Undecided && delta != 0f) {
                    gesture = if (delta > 0f && currentOnDismiss != null) {
                        PlayerDragGesture.Dismiss
                    } else {
                        PlayerDragGesture.ExpandOrCollapse
                    }
                }
                when (gesture) {
                    PlayerDragGesture.Dismiss -> {
                        scope.launch {
                            dismissOffset.snapTo((dismissOffset.value + delta).coerceIn(0f, dismissTravelPx()))
                        }
                    }
                    PlayerDragGesture.ExpandOrCollapse -> {
                        scope.launch {
                            expansion.snapTo((expansion.value - delta / travelPx()).coerceIn(0f, 1f))
                        }
                    }
                    PlayerDragGesture.Undecided -> Unit
                }
            }
            // Observe thresholds in composition; read continuous expansion during layout and drawing to avoid recomposing player content on every frame.
            val collapsed by remember(expansion) { derivedStateOf { expansion.value == 0f } }
            val showMini by remember(expansion) { derivedStateOf { expansion.value < 1f } }
            val showExpanded by remember(expansion) { derivedStateOf { expansion.value > 0f } }
            val dismissLabel = stringResource(R.string.shell_dismiss_player)
            Surface(
                onClick = { if (expansion.value == 0f && dismissOffset.value == 0f) settle(1f) },
                enabled = collapsed,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .playerFrame(
                        progress = { expansion.value },
                        dockStart = dockStart,
                        dockBottom = dockBottom,
                        fullHeight = fullHeight,
                    )
                    .semantics {
                        if (collapsed && currentOnDismiss != null) {
                            dismiss(label = dismissLabel) {
                                completeDismiss()
                                true
                            }
                        }
                    }
                    .graphicsLayer {
                        val progress = expansion.value
                        val dismissPx = dismissOffset.value
                        val dismissFraction = (dismissPx / dismissTravelPx()).coerceIn(0f, 1f)
                        shape = RoundedCornerShape(lerp(20.dp, 0.dp, progress))
                        clip = true
                        shadowElevation = lerp(6.dp, 0.dp, progress).toPx() * (1f - dismissFraction)
                        translationY = dismissPx * DismissDragTranslationFactor
                        alpha = 1f - dismissFraction * DismissMaxFade
                    }
                    .draggable(
                        state = dragState,
                        orientation = Orientation.Vertical,
                        interactionSource = dragInteraction,
                        onDragStarted = {
                            gesture = when {
                                expansion.value > 0f || expansion.targetValue > 0f || currentOnDismiss == null ->
                                    PlayerDragGesture.ExpandOrCollapse
                                dismissOffset.value > 0f ->
                                    PlayerDragGesture.Dismiss
                                else ->
                                    PlayerDragGesture.Undecided
                            }
                        },
                        onDragStopped = { velocity ->
                            val flingVelocityPx = with(density) { FlingVelocity.toPx() }
                            val currentGesture = gesture
                            val wasCancelled = dragInteraction.cancelled
                            scope.launch {
                                when (currentGesture) {
                                    PlayerDragGesture.Dismiss -> {
                                        val dismissThresholdPx = with(density) { DismissDragThreshold.toPx() }
                                        val minDismissFlingPx = with(density) { MinDismissFlingDistance.toPx() }
                                        val shouldDismiss = !wasCancelled &&
                                            velocity > -flingVelocityPx && (
                                                dismissOffset.value >= dismissThresholdPx ||
                                                    (velocity >= flingVelocityPx && dismissOffset.value >= minDismissFlingPx)
                                            )
                                        if (shouldDismiss) {
                                            completeDismiss()
                                        } else {
                                            settleDismiss(0f)
                                        }
                                    }
                                    PlayerDragGesture.ExpandOrCollapse,
                                    PlayerDragGesture.Undecided,
                                    -> {
                                        settle(
                                            when {
                                                wasCancelled -> if (expansion.value > 0.5f) 1f else 0f
                                                velocity < -flingVelocityPx -> 1f
                                                velocity > flingVelocityPx -> 0f
                                                expansion.value > 0.5f -> 1f
                                                else -> 0f
                                            },
                                        )
                                    }
                                }
                            }
                        },
                    ),
                // The graphics layer above draws the rounded corners and the shadow.
                shape = RectangleShape,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 3.dp,
            ) {
                Box {
                    if (showMini) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(MiniPlayerHeight)
                                .graphicsLayer { alpha = (1f - expansion.value * 4f).coerceIn(0f, 1f) },
                        ) {
                            player.mini()
                        }
                    }
                    if (showExpanded) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = ((expansion.value - 0.25f) / 0.75f).coerceIn(0f, 1f) }
                                .windowInsetsPadding(WindowInsets.safeDrawing),
                        ) {
                            player.expanded { settle(0f) }
                        }
                    }
                }
            }
        }
    }

    // Composed only while needed, so it registers after every other back handler and wins. Derived,
    // so this scope follows the player opening, not each frame of it.
    val opened by remember(expansion) { derivedStateOf { expansion.value > 0f || expansion.targetValue > 0f } }
    if (active && opened) {
        ShellBackHandler { settle(0f) }
    }
}

private enum class PlayerDragGesture {
    Undecided,
    ExpandOrCollapse,
    Dismiss,
}

private class DragCancellationTracker(
    private val delegate: MutableInteractionSource = MutableInteractionSource(),
) : MutableInteractionSource by delegate {
    var cancelled: Boolean = false
        private set

    override suspend fun emit(interaction: Interaction) {
        update(interaction)
        delegate.emit(interaction)
    }

    override fun tryEmit(interaction: Interaction): Boolean {
        update(interaction)
        return delegate.tryEmit(interaction)
    }

    private fun update(interaction: Interaction) {
        when (interaction) {
            is DragInteraction.Start -> cancelled = false
            is DragInteraction.Stop -> cancelled = false
            is DragInteraction.Cancel -> cancelled = true
        }
    }
}

private val PlayerEntranceRise = 96.dp
private val FlingVelocity = 600.dp
private val DismissDragThreshold = 48.dp
private val MinDismissFlingDistance = 12.dp
private val DismissTravel = MiniPlayerHeight + 24.dp
private const val DismissDragTranslationFactor = 0.6f
private const val DismissMaxFade = 0.75f

/**
 * Sizes and places the player between its docked pill and the full window, reading [progress] and
 * [dockBottom] while measuring. A dock that moves on every frame, as the bottom bar does while it
 * hides, and every frame of a drag or settle then lay the player out again without recomposing it.
 *
 * Docked, the pill is inset by [dockStart] plus a margin at the start, a margin at the end, and
 * sits above [dockBottom]; expanded, it fills the width and [fullHeight].
 */
private fun Modifier.playerFrame(
    progress: () -> Float,
    dockStart: Dp,
    dockBottom: () -> Dp,
    fullHeight: Dp,
): Modifier = layout { measurable, constraints ->
    val fraction = progress()
    val startPx = lerp(dockStart + 12.dp, 0.dp, fraction).roundToPx().coerceAtLeast(0)
    val endPx = lerp(12.dp, 0.dp, fraction).roundToPx().coerceAtLeast(0)
    val bottomPx = lerp(dockBottom() + 8.dp, 0.dp, fraction).roundToPx().coerceAtLeast(0)
    val width = (constraints.maxWidth - startPx - endPx).coerceAtLeast(0)
    val height = lerp(MiniPlayerHeight, fullHeight, fraction).roundToPx()
        .coerceIn(0, (constraints.maxHeight - bottomPx).coerceAtLeast(0))
    val placeable = measurable.measure(Constraints.fixed(width, height))
    layout(constraints.maxWidth, constraints.constrainHeight(height + bottomPx)) {
        placeable.place(startPx, 0)
    }
}
