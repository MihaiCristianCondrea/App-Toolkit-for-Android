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

@file:Suppress("FunctionName")

package com.d4rk.android.libs.apptoolkit.navigation.animations

import android.graphics.Path
import android.view.animation.PathInterpolator
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Remembers and provides native-like activity transitions.
 */
@Composable
fun rememberNativeActivityTransitions(): NativeActivityTransitions {
    val density = LocalDensity.current

    val enteringStartOffsetPx = with(density) {
        NativeActivityMotion.EnteringStartOffset.roundToPx()
    }

    val displayBoundsMarginPx = with(density) {
        NativeActivityMotion.DisplayBoundsMargin.roundToPx()
    }

    return remember(
        enteringStartOffsetPx,
        displayBoundsMarginPx,
    ) {
        NativeActivityTransitions(
            enteringStartOffsetPx = enteringStartOffsetPx,
            displayBoundsMarginPx = displayBoundsMarginPx,
        )
    }
}

/**
 * Class providing Enter and Exit transitions for activity-like screens.
 */
class NativeActivityTransitions internal constructor(
    private val enteringStartOffsetPx: Int,
    private val displayBoundsMarginPx: Int,
) {

    val nativeSlideEnter: EnterTransition
        get() = slideInHorizontally(
            animationSpec = NativeActivityMotion.motionSpec(),
            initialOffsetX = { fullWidth -> fullWidth / 4 },
        ) + fadeIn(
            animationSpec = NativeActivityMotion.fadeSpec(),
        )

    val nativeSlideExit: ExitTransition
        get() = slideOutHorizontally(
            animationSpec = NativeActivityMotion.motionSpec(),
            targetOffsetX = { fullWidth -> -(fullWidth / 12) },
        ) + fadeOut(
            animationSpec = NativeActivityMotion.fadeSpec(),
        )

    val nativeSlidePopEnter: EnterTransition
        get() = previousScreenEnter()

    val nativeSlidePopExit: ExitTransition
        get() = currentScreenExit(
            includePostCommitFade = true,
            includePostCommitOffset = true,
        )

    val nativePredictiveSlidePopExit: ExitTransition
        get() = currentScreenExit(
            includePostCommitFade = false,
            includePostCommitOffset = false,
        )

    /**
     * Standard forward navigation transition.
     */
    fun forward(): ContentTransform {
        return nativeSlideEnter togetherWith nativeSlideExit
    }

    /**
     * Standard pop (back) navigation transition.
     */
    fun pop(): ContentTransform {
        return nativeSlidePopEnter togetherWith nativeSlidePopExit
    }

    /**
     * Predictive back navigation transition.
     */
    fun predictivePop(): ContentTransform {
        return nativeSlidePopEnter togetherWith nativePredictiveSlidePopExit
    }

    private fun previousScreenEnter(): EnterTransition {
        return slideInHorizontally(
            animationSpec = NativeActivityMotion.motionSpec(),
            initialOffsetX = { -enteringStartOffsetPx },
        ) + scaleIn(
            animationSpec = NativeActivityMotion.motionSpec(),
            initialScale = NativeActivityMotion.MINIMUM_WINDOW_SCALE,
        )
    }

    private fun currentScreenExit(
        includePostCommitFade: Boolean,
        includePostCommitOffset: Boolean,
    ): ExitTransition {
        val movement = slideOutHorizontally(
            animationSpec = NativeActivityMotion.motionSpec(),
            targetOffsetX = { fullWidth ->
                val preCommitDrift = (
                        (fullWidth * NativeActivityMotion.CLOSING_WINDOW_DRIFT_FRACTION).roundToInt() -
                                displayBoundsMarginPx
                        ).coerceAtLeast(0)

                if (includePostCommitOffset) {
                    preCommitDrift + enteringStartOffsetPx
                } else {
                    preCommitDrift
                }
            },
        )

        val scale = scaleOut(
            animationSpec = NativeActivityMotion.motionSpec(),
            targetScale = NativeActivityMotion.MINIMUM_WINDOW_SCALE,
        )

        val fade = if (includePostCommitFade) {
            fadeOut(
                animationSpec = NativeActivityMotion.closingAlphaSpec(),
            )
        } else {
            ExitTransition.None
        }

        return movement + scale + fade
    }
}

/**
 * Constants and specs for native activity motion.
 */
object NativeActivityMotion {

    const val MINIMUM_WINDOW_SCALE: Float = 0.90f

    val EnteringStartOffset = 96.dp

    val DisplayBoundsMargin = 8.dp

    const val POST_COMMIT_DURATION_MILLIS: Int = 450

    const val CLOSING_FADE_DURATION_MILLIS: Int = POST_COMMIT_DURATION_MILLIS / 5

    const val CLOSING_WINDOW_DRIFT_FRACTION: Float = 0.05f

    private val emphasizedEasing = Easing { fraction ->
        emphasizedPathInterpolator.getInterpolation(fraction)
    }

    private val emphasizedPathInterpolator: PathInterpolator by lazy {
        PathInterpolator(
            Path().apply {
                moveTo(0f, 0f)
                cubicTo(
                    0.05f, 0f,
                    0.133333f, 0.06f,
                    0.166666f, 0.4f,
                )
                cubicTo(
                    0.208333f, 0.82f,
                    0.25f, 1f,
                    1f, 1f,
                )
            },
        )
    }

    fun <T> motionSpec() = tween<T>(
        durationMillis = POST_COMMIT_DURATION_MILLIS,
        easing = emphasizedEasing,
    )

    fun <T> fadeSpec() = tween<T>(
        durationMillis = 120,
        easing = emphasizedEasing,
    )

    fun <T> closingAlphaSpec() = tween<T>(
        durationMillis = CLOSING_FADE_DURATION_MILLIS,
        easing = emphasizedEasing,
    )
}
