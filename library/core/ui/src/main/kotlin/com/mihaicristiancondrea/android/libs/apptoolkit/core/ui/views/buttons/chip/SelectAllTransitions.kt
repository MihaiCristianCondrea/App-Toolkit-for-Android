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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.chip

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith

/**
 * A collection of predefined transitions for animating content changes,
 * specifically designed for selection states like in chips or checkboxes.
 */
object SelectAllTransitions {
    private const val DURATION = 300
    private val fadeScaleSpec = tween<Float>(DURATION)

    val fadeScale: ContentTransform by lazy {
        (fadeIn(animationSpec = fadeScaleSpec) + scaleIn(animationSpec = fadeScaleSpec))
            .togetherWith(fadeOut(animationSpec = fadeScaleSpec) + scaleOut(animationSpec = fadeScaleSpec))
    }
}
