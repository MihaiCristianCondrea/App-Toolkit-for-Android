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

import android.graphics.Path
import android.view.animation.PathInterpolator
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * The transitions Android plays between activities, rebuilt for destinations that are not
 * activities.
 *
 * Opening slides the new page in from the end edge over the old one. Closing without a gesture
 * plays the post-commit phase of the system's cross-activity back animation on its own. Closing
 * with a gesture is [CrossActivityBackMotion], which `ShellNavDisplay` drives.
 */
@Immutable
class ActivityTransitions internal constructor(
    private val enteringOffsetPx: Int,
    private val displayMarginPx: Int,
    private val durationScale: Float,
    /** 1 in left-to-right layouts, -1 in right-to-left ones, where every slide is mirrored. */
    private val direction: Int = 1,
) {

    /** Navigating forward. */
    fun open(): ContentTransform {
        val enter = slideInHorizontally(motion()) { width -> direction * width / 4 } + fadeIn(fade())
        val exit = slideOutHorizontally(motion()) { width -> -direction * width / 12 } + fadeOut(fade())
        return enter togetherWith exit
    }

    /**
     * Back that did not come from a gesture: an app bar button, a key, or three-button navigation.
     * This is the post-commit half of the system animation on its own.
     */
    fun close(): ContentTransform {
        val enter = slideInHorizontally(motion()) { -direction * enteringOffsetPx } +
            scaleIn(motion(), initialScale = ActivityMotion.MINIMUM_SCALE)
        val exit = slideOutHorizontally(motion()) { width -> direction * (drift(width) + enteringOffsetPx) } +
            scaleOut(motion(), targetScale = ActivityMotion.MINIMUM_SCALE) +
            fadeOut(tween(duration(ActivityMotion.CLOSING_FADE_MILLIS), easing = ActivityMotion.Emphasized))
        return enter togetherWith exit
    }

    /** How far a page shrunk to the minimum scale moves to rest against an edge, less the margin. */
    private fun drift(width: Int): Int =
        ((width * (1f - ActivityMotion.MINIMUM_SCALE) / 2f).roundToInt() - displayMarginPx).coerceAtLeast(0)

    private fun <T> motion(): FiniteAnimationSpec<T> =
        tween(duration(ActivityMotion.DURATION_MILLIS), easing = ActivityMotion.Emphasized)

    private fun <T> fade(): FiniteAnimationSpec<T> =
        tween(duration(ActivityMotion.FADE_MILLIS), easing = ActivityMotion.Emphasized)

    private fun duration(millis: Int): Int = (millis * durationScale).roundToInt()
}

/** The system's activity motion values. See `CrossActivityBackAnimation` in AOSP. */
object ActivityMotion {
    const val MINIMUM_SCALE: Float = 0.90f
    val EnteringStartOffset = 96.dp
    val DisplayBoundsMargin = 8.dp
    const val DURATION_MILLIS: Int = 450
    const val FADE_MILLIS: Int = 120
    const val CLOSING_FADE_MILLIS: Int = DURATION_MILLIS / 5

    /** Android's `fast_out_extra_slow_in`, the emphasized curve activity transitions use. */
    val Emphasized: Easing = run {
        val interpolator = PathInterpolator(
            Path().apply {
                moveTo(0f, 0f)
                cubicTo(0.05f, 0f, 0.133333f, 0.06f, 0.166666f, 0.4f)
                cubicTo(0.208333f, 0.82f, 0.25f, 1f, 1f, 1f)
            },
        )
        Easing { fraction -> interpolator.getInterpolation(fraction) }
    }
}
