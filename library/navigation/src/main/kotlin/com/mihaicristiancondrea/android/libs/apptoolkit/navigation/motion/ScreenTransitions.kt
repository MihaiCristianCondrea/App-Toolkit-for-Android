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

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/** Plays a [ScreenTransition] forward or back, scaled by the developer options' animation speed. */
@Immutable
class ScreenTransitions internal constructor(
    private val activity: ActivityTransitions,
    private val durationScale: Float,
    /** 1 in left-to-right layouts, -1 in right-to-left ones. */
    private val direction: Int = 1,
) {
    fun forward(transition: ScreenTransition): ContentTransform = play(transition, back = false)

    /**
     * @param mirrored Plays a sliding back the other way, for a back swipe from the right edge that
     * should follow the finger.
     */
    fun back(transition: ScreenTransition, mirrored: Boolean = false): ContentTransform =
        play(transition, back = true, mirrored = mirrored)

    private fun play(transition: ScreenTransition, back: Boolean, mirrored: Boolean = false): ContentTransform = when (transition) {
        ScreenTransition.Activity -> if (back) activity.close() else activity.open()

        ScreenTransition.Slide -> {
            val sign = (if (back) -direction else direction) * (if (mirrored) -1 else 1)
            val spec = tween<IntOffset>(duration(350), easing = FastOutSlowInEasing)
            if (back) {
                // The old screen returns from a quarter out; the leaving one slides away whole,
                // and stays on top until it has.
                val enter = slideInHorizontally(spec) { width -> sign * width / 4 }
                val exit = slideOutHorizontally(spec) { width -> -sign * width }
                (enter togetherWith exit).apply { targetContentZIndex = -1f }
            } else {
                val enter = slideInHorizontally(spec) { width -> sign * width }
                val exit = slideOutHorizontally(spec) { width -> -sign * width / 4 }
                enter togetherWith exit
            }
        }

        ScreenTransition.FadeThrough -> fadeThrough(::duration)

        ScreenTransition.Fade -> crossFade(::duration)

        ScreenTransition.None -> EnterTransition.None togetherWith ExitTransition.None
    }

    private fun duration(millis: Int): Int = (millis * durationScale).roundToInt()
}
