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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Stable

/**
 * How far the player is open, from 0 (the docked pill) to 1 (the full window).
 *
 * A stable holder around an [Animatable], whose stability the Compose compiler can only decide at
 * runtime because it comes from another module: passed around as is, it kept the overlay and the
 * chrome from skipping. Reading [value] or [targetValue] is a snapshot read, as on the animatable.
 */
@Stable
internal class PlayerExpansion {
    private val animatable: Animatable<Float, AnimationVector1D> = Animatable(0f)

    val value: Float get() = animatable.value

    val targetValue: Float get() = animatable.targetValue

    suspend fun snapTo(target: Float) {
        animatable.snapTo(target)
    }

    suspend fun animateTo(target: Float, animationSpec: AnimationSpec<Float>) {
        animatable.animateTo(target, animationSpec)
    }
}
