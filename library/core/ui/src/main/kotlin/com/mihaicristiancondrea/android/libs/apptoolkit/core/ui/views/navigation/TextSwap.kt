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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MotionScheme

/**
 * How an app bar's text gives way to the next, a title to another or to the search field: the old
 * one fades out quickly and the new one fades and grows into place, on the app's motion tempo.
 */
internal fun MotionScheme.textSwap(initialScale: Float = 0.92f, targetScale: Float = 1f): ContentTransform =
    (fadeIn(defaultEffectsSpec()) + scaleIn(fastSpatialSpec(), initialScale = initialScale))
        .togetherWith(fadeOut(fastEffectsSpec()) + scaleOut(fastSpatialSpec(), targetScale = targetScale))
