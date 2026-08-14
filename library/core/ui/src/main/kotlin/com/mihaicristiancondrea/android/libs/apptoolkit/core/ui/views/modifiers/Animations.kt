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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlin.math.min


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
@Composable
fun Modifier.animateVisibility(
    index: Int = 0,
    invisibleOffsetY: Int = 50,
    animationDuration: Int = 300,
    staggerDelay: Int = 64,
    maxStaggeredItems: Int = 20,
): Modifier {
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

    return this
        .offset {
            IntOffset(x = 0, y = offsetState.value.toInt())
        }
        .graphicsLayer {
            this.alpha = alpha.value
        }
}
