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
import androidx.compose.animation.core.Easing
import androidx.compose.ui.unit.dp

/**
 * The system's activity motion values, the one source for [ActivityTransitions] and
 * [CrossActivityBackMotion]. See `CrossActivityBackAnimation` in AOSP.
 */
internal object ActivityMotion {
    /** `MAX_SCALE`: how small the closing page gets. */
    const val MinimumScale: Float = 0.90f
    val EnteringStartOffset = 96.dp
    val DisplayBoundsMargin = 8.dp

    /** `POST_COMMIT_DURATION` of the default cross-activity animation. */
    const val DurationMillis: Int = 450
    const val FadeMillis: Int = 120
    const val ClosingFadeMillis: Int = DurationMillis / 5

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
