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

package com.d4rk.android.libs.apptoolkit.core.ui.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith

/**
 * Default navigation animations shared across the toolkit.
 */
object NavigationAnimations {
    private const val FADE_SCALE_DURATION_MILLIS = 200
    private val fadeScaleEnterSpec = tween<Float>(durationMillis = FADE_SCALE_DURATION_MILLIS)
    private val fadeScaleExitSpec = tween<Float>(durationMillis = FADE_SCALE_DURATION_MILLIS)

    fun default(): ContentTransform {
        val enter: EnterTransition = fadeIn(animationSpec = fadeScaleEnterSpec) + scaleIn(
            initialScale = 0.92f,
            animationSpec = fadeScaleEnterSpec
        )
        val exit: ExitTransition = fadeOut(animationSpec = fadeScaleExitSpec) + scaleOut(
            targetScale = 0.95f,
            animationSpec = fadeScaleExitSpec
        )
        return enter togetherWith exit
    }
}
