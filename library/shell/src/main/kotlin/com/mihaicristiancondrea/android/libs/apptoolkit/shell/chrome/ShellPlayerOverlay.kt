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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
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
    expansion: Animatable<Float, AnimationVector1D>,
    dockBottom: Dp,
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

    val travel = { _: Int -> with(density) { (dockBottom + MiniPlayerHeight + PlayerEntranceRise).roundToPx() } }
    AnimatedVisibility(
        visible = active,
        enter = slideInVertically(spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow), travel) +
            fadeIn(tween(240, delayMillis = 40)),
        exit = slideOutVertically(tween(280), travel) + fadeOut(tween(220, delayMillis = 40)),
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val progress = expansion.value
            val travelPx = with(density) { (maxHeight - MiniPlayerHeight - dockBottom).toPx().coerceAtLeast(1f) }
            val dragState = rememberDraggableState { delta ->
                scope.launch { expansion.snapTo((expansion.value - delta / travelPx).coerceIn(0f, 1f)) }
            }
            Surface(
                onClick = { if (expansion.value == 0f) settle(1f) },
                enabled = progress == 0f,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        start = lerp(dockStart + 12.dp, 0.dp, progress),
                        end = lerp(12.dp, 0.dp, progress),
                        bottom = lerp(dockBottom + 8.dp, 0.dp, progress),
                    )
                    .fillMaxWidth()
                    .height(lerp(MiniPlayerHeight, maxHeight, progress))
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
                shape = RoundedCornerShape(lerp(20.dp, 0.dp, progress)),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 3.dp,
                shadowElevation = lerp(6.dp, 0.dp, progress),
            ) {
                Box {
                    if (progress < 1f) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(MiniPlayerHeight)
                                .graphicsLayer { alpha = (1f - progress * 4f).coerceIn(0f, 1f) },
                        ) {
                            player.mini()
                        }
                    }
                    if (progress > 0f) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = ((progress - 0.25f) / 0.75f).coerceIn(0f, 1f) }
                                .windowInsetsPadding(WindowInsets.safeDrawing),
                        ) {
                            player.expanded { settle(0f) }
                        }
                    }
                }
            }
        }
    }

    // Composed only while needed, so it registers after every other back handler and wins.
    if (active && (expansion.value > 0f || expansion.targetValue > 0f)) {
        ShellBackHandler { settle(0f) }
    }
}

private val PlayerEntranceRise = 96.dp
private const val FlingVelocity = 1200f
