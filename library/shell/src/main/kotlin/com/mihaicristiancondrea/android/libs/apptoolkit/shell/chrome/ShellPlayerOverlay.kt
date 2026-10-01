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
import androidx.compose.animation.core.Animatable
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellPlayer
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellBackHandler
import kotlinx.coroutines.launch

/** Height of the collapsed player pill. */
val MiniPlayerHeight: Dp = 64.dp

/** Space the content keeps free at its bottom while the mini player is docked over it. */
internal val MiniPlayerReserve: Dp = MiniPlayerHeight + 16.dp

/**
 * Draws [player] over the shell.
 *
 * Collapsed, the player is a pill docked [dockBottom] above the window's bottom edge (the top of
 * the navigation bar, or of the system bar when there is none) and [dockStart] in from the start
 * edge (past a rail or drawer). Dragging it up, or tapping it, grows it into a full-window surface;
 * [expansion] runs from 0 to 1 along the way so the shell can move its own bars in step. Back
 * collapses it before it does anything else.
 */
@Composable
internal fun ShellPlayerOverlay(
    player: ShellPlayer,
    active: Boolean,
    expansion: Animatable<Float, AnimationVector1D>, // FIXME: Parameter 'expansion' has runtime-determined stability
    dockBottom: () -> Dp,
    dockStart: Dp,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val settle: (Float) -> Unit = { target ->
        scope.launch {
            expansion.animateTo(
                target,
                spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
            )
        }
    }

    LaunchedEffect(active) {
        if (!active) expansion.snapTo(0f)
    }

    val travel = { _: Int -> with(density) { (dockBottom() + MiniPlayerHeight + PlayerEntranceRise).roundToPx() } }
    AnimatedVisibility(
        visible = active,
        enter = slideInVertically(spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow), travel) +
            fadeIn(tween(240, delayMillis = 40)),
        exit = slideOutVertically(tween(280), travel) + fadeOut(tween(220, delayMillis = 40)),
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val fullHeight: Dp = maxHeight
            val travelPx = { with(density) { (fullHeight - MiniPlayerHeight - dockBottom()).toPx().coerceAtLeast(1f) } }
            val dragState = rememberDraggableState { delta ->
                scope.launch { expansion.snapTo((expansion.value - delta / travelPx()).coerceIn(0f, 1f)) }
            }
            // Composition reads only these thresholds. The frame, corners and shadow follow
            // [expansion] in layout and drawing, so a drag or a settle re-lays out and redraws the
            // player on each frame instead of recomposing it and the host's player content.
            val collapsed by remember(expansion) { derivedStateOf { expansion.value == 0f } }
            val showMini by remember(expansion) { derivedStateOf { expansion.value < 1f } }
            val showExpanded by remember(expansion) { derivedStateOf { expansion.value > 0f } }
            Surface(
                onClick = { if (expansion.value == 0f) settle(1f) },
                enabled = collapsed,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .playerFrame(
                        progress = { expansion.value },
                        dockStart = dockStart,
                        dockBottom = dockBottom,
                        fullHeight = fullHeight,
                    )
                    .graphicsLayer {
                        val progress = expansion.value
                        shape = RoundedCornerShape(lerp(20.dp, 0.dp, progress))
                        clip = true
                        shadowElevation = lerp(6.dp, 0.dp, progress).toPx()
                    }
                    .draggable(
                        state = dragState,
                        orientation = Orientation.Vertical,
                        onDragStopped = { velocity ->
                            settle(
                                when {
                                    velocity < -FlingVelocity -> 1f
                                    velocity > FlingVelocity -> 0f
                                    expansion.value > 0.5f -> 1f
                                    else -> 0f
                                },
                            )
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

private val PlayerEntranceRise = 96.dp
private const val FlingVelocity = 1200f

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
