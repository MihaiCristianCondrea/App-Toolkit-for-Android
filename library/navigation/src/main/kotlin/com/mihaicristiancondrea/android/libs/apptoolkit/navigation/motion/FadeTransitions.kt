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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith

// The fades both tabs and screens offer, kept in one place so the two cannot drift apart.
// [duration] scales a duration in milliseconds by the developer options' animation speed.

/** Material's fade through: the old content fades out, then the new one fades and scales in. */
internal fun fadeThrough(duration: (Int) -> Int): ContentTransform {
    val enter = fadeIn(tween(duration(210), delayMillis = duration(90))) +
        scaleIn(tween(duration(210), delayMillis = duration(90)), initialScale = 0.92f)
    return enter togetherWith fadeOut(tween(duration(90)))
}

/** A plain cross-fade. */
internal fun crossFade(duration: (Int) -> Int): ContentTransform =
    fadeIn(tween(duration(300))) togetherWith fadeOut(tween(duration(300)))
