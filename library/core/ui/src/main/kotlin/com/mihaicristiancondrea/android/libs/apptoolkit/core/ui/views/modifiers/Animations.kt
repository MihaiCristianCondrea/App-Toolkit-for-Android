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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.min
import kotlin.time.Duration.Companion.milliseconds

/**
 * Animates the visibility of a composable with a fade and vertical offset animation.
 *
 * Staggering by [index] makes an item far down a list wait for every position above it, so a cell
 * scrolled into view later stayed blank for up to [maxStaggeredItems] × [staggerDelay]
 * milliseconds. [animateEntrance] staggers by arrival instead, only for the list's first reveal,
 * and takes its offset in dp.
 *
 * @param index Used to stagger the start time of the animation for items in a list or grid.
 * @param invisibleOffsetY The vertical offset in pixels applied before the animation starts.
 * @param animationDuration Duration of the fade/offset animation in milliseconds.
 * @param staggerDelay Amount of delay in milliseconds per [index] before the animation starts.
 * @param maxStaggeredItems Positions past this one wait no longer than it.
 */
@Deprecated(
    message = "Stagger by arrival with animateEntrance and a list-level " +
            "rememberEntranceStagger(). Staggering by index delays items scrolled into view later.",
    replaceWith = ReplaceWith(
        expression = "animateEntrance()",
        imports = [
            "com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateEntrance",
        ],
    ),
)
@Composable
fun Modifier.animateVisibility(
    index: Int = 0,
    invisibleOffsetY: Int = 50,
    animationDuration: Int = 300,
    staggerDelay: Int = 64,
    maxStaggeredItems: Int = 20,
): Modifier {
    val offsetY = with(LocalDensity.current) { invisibleOffsetY.toDp() }
    return entranceAnimation(
        spec = EntranceSpec(offsetY = offsetY, duration = animationDuration.milliseconds),
        startDelayMillis = { min(index, maxStaggeredItems) * staggerDelay.toLong() },
    )
}
