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
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.VectorizedFiniteAnimationSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MotionScheme

/**
 * How an app bar's text gives way to the next, a title to another or to the search field: the old
 * one fades out quickly, then the new one fades and grows into place, on the app's motion tempo.
 * Waiting for the old text lets the bar's buttons, which move at once, settle first, so a search
 * field arrives after them rather than ahead of them.
 */
internal fun MotionScheme.textSwap(initialScale: Float = 0.92f, targetScale: Float = 1f): ContentTransform {
    val outgoing = fastEffectsSpec<Float>()
    val wait = outgoing.durationNanos()
    return (fadeIn(defaultEffectsSpec<Float>().after(wait)) + scaleIn(fastSpatialSpec<Float>().after(wait), initialScale = initialScale))
        .togetherWith(fadeOut(outgoing) + scaleOut(fastSpatialSpec(), targetScale = targetScale))
}

/** How long this spec takes to run from 0 to 1, springs included. */
private fun FiniteAnimationSpec<Float>.durationNanos(): Long =
    vectorize(Float.VectorConverter).getDurationNanos(AnimationVector1D(0f), AnimationVector1D(1f), AnimationVector1D(0f))

/** This spec, held at its start for [delayNanos] first. Works for springs, which take no delay. */
private fun <T> FiniteAnimationSpec<T>.after(delayNanos: Long): FiniteAnimationSpec<T> = DelayedSpec(this, delayNanos)

private class DelayedSpec<T>(private val spec: FiniteAnimationSpec<T>, private val delayNanos: Long) : FiniteAnimationSpec<T> {
    override fun <V : AnimationVector> vectorize(converter: TwoWayConverter<T, V>): VectorizedFiniteAnimationSpec<V> =
        DelayedVectorizedSpec(spec.vectorize(converter), delayNanos)
}

private class DelayedVectorizedSpec<V : AnimationVector>(
    private val spec: VectorizedFiniteAnimationSpec<V>,
    private val delayNanos: Long,
) : VectorizedFiniteAnimationSpec<V> {
    override fun getValueFromNanos(playTimeNanos: Long, initialValue: V, targetValue: V, initialVelocity: V): V =
        if (playTimeNanos < delayNanos) {
            initialValue
        } else {
            spec.getValueFromNanos(playTimeNanos - delayNanos, initialValue, targetValue, initialVelocity)
        }

    override fun getVelocityFromNanos(playTimeNanos: Long, initialValue: V, targetValue: V, initialVelocity: V): V =
        if (playTimeNanos < delayNanos) {
            initialVelocity
        } else {
            spec.getVelocityFromNanos(playTimeNanos - delayNanos, initialValue, targetValue, initialVelocity)
        }

    override fun getDurationNanos(initialValue: V, targetValue: V, initialVelocity: V): Long =
        delayNanos + spec.getDurationNanos(initialValue, targetValue, initialVelocity)
}
