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

package com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore
import kotlinx.coroutines.delay
import kotlin.math.min


/**
 * Applies a bouncy click effect to a composable.
 *
 * This modifier scales the composable down to 96% of its size when pressed and
 * animates it back to its original size upon release. The animation can be
 * globally disabled via a `CommonDataStore` setting (`bouncyButtonsEnabled`) or
 * on a per-composable basis using the [animationEnabled] parameter.
 *
 * @param animationEnabled A boolean flag to enable or disable the bounce effect
 * for this specific composable. Defaults to `true`.
 * @return A [Modifier] that applies the bounce click animation.
 */
@Composable
fun Modifier.bounceClick(
    animationEnabled: Boolean = true,
): Modifier = composed {
    LocalContext.current
    val dataStore = rememberCommonDataStore()

    val bouncyButtonsEnabled: Boolean by dataStore.bouncyButtons
        .collectAsStateWithLifecycle(initialValue = true)

    if (!animationEnabled || !bouncyButtonsEnabled) return@composed this

    val pressed = remember { mutableStateOf(false) }

    val scale: Float by animateFloatAsState(
        targetValue = if (pressed.value) 0.96f else 1f,
        label = "Button Press Scale Animation"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                pressed.value = true
                waitForUpOrCancellation()
                pressed.value = false
            }
        }
}

/**
 * Animates the visibility of a composable with a fade and vertical offset animation.
 *
 * The composable will fade and slide into place the first time it enters the
 * composition. The animation for each item can be staggered by providing an
 * [index]. After the initial animation runs, the composable remains visible even
 * if it leaves and re-enters the composition.
 *
 * @param index Used to stagger the start time of the animation for items in a
 * list or grid.
 * @param invisibleOffsetY The vertical offset in pixels applied before the
 * animation starts. Defaults to 50.
 * @param animationDuration Duration of the fade/offset animation in
 * milliseconds. Defaults to 300.
 * @param staggerDelay Amount of delay in milliseconds per [index] before the
 * animation starts. Defaults to 64.
 */
fun Modifier.animateVisibility(
    index: Int = 0,
    invisibleOffsetY: Int = 50,
    animationDuration: Int = 300,
    staggerDelay: Int = 64,
    maxStaggeredItems: Int = 20,
) = composed {
    var visible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!visible) {
            val delayMillis: Int = min(index, maxStaggeredItems) * staggerDelay
            delay(timeMillis = delayMillis.toLong())
            visible = true
        }
    }

    val alpha: State<Float> = animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = animationDuration),
        label = "Alpha"
    )

    val offsetState: State<Float> = animateFloatAsState(
        targetValue = if (visible) 0f else invisibleOffsetY.toFloat(),
        animationSpec = tween(durationMillis = animationDuration),
        label = "OffsetY"
    )

    this
        .offset {
            IntOffset(x = 0, y = offsetState.value.toInt())
        }
        .graphicsLayer {
            this.alpha = alpha.value
        }
}
