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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

/** Applies the design system's optional press-scale feedback. */
@Composable
fun Modifier.bounceClick(animationEnabled: Boolean = true): Modifier {
    val globallyEnabled = LocalBouncyAnimationsEnabled.current
    val pressed = remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed.value) 0.96f else 1f,
        label = "Button Press Scale Animation",
    )

    if (!animationEnabled || !globallyEnabled) return this

    return graphicsLayer {
        scaleX = scale
        scaleY = scale
    }.pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false)
            pressed.value = true
            waitForUpOrCancellation()
            pressed.value = false
        }
    }
}
