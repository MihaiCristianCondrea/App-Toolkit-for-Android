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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Immutable

/**
 * How quickly the app moves: the springs every Material component, entrance and transition takes
 * from `MaterialTheme.motionScheme`. Spatial springs move, scale and reshape; effects springs fade
 * and change colour. Each tempo keeps Material's three speeds, default, fast and slow, in step.
 */
enum class MotionTempo {
    /**
     * Unhurried: about half a second for a default movement, with fades that run alongside it
     * rather than finishing first. The pace of a calm, deliberate app.
     */
    Calm,

    /** Material 3 Expressive's own springs. */
    Standard,

    /** Quick: about half the time of [Standard]. */
    Brisk,
}

/** The Material [MotionScheme] this tempo moves with. */
fun MotionTempo.motionScheme(): MotionScheme = when (this) {
    MotionTempo.Calm -> CalmMotionScheme
    MotionTempo.Standard -> MotionScheme.expressive()
    MotionTempo.Brisk -> BriskMotionScheme
}

private val CalmMotionScheme: MotionScheme = SpringMotionScheme(
    spatialDamping = 0.8f,
    defaultSpatialStiffness = 200f,
    fastSpatialStiffness = 380f,
    slowSpatialStiffness = 120f,
    defaultEffectsStiffness = 800f,
    fastEffectsStiffness = 1600f,
    slowEffectsStiffness = 300f,
)

private val BriskMotionScheme: MotionScheme = SpringMotionScheme(
    spatialDamping = 0.8f,
    defaultSpatialStiffness = 800f,
    fastSpatialStiffness = 1400f,
    slowSpatialStiffness = 380f,
    defaultEffectsStiffness = 2400f,
    fastEffectsStiffness = 3800f,
    slowEffectsStiffness = 1600f,
)

/** A [MotionScheme] of springs: spatial ones at [spatialDamping], effects ones without bounce. */
@Immutable
private class SpringMotionScheme(
    private val spatialDamping: Float,
    private val defaultSpatialStiffness: Float,
    private val fastSpatialStiffness: Float,
    private val slowSpatialStiffness: Float,
    private val defaultEffectsStiffness: Float,
    private val fastEffectsStiffness: Float,
    private val slowEffectsStiffness: Float,
) : MotionScheme {
    override fun <T> defaultSpatialSpec(): FiniteAnimationSpec<T> = spring(spatialDamping, defaultSpatialStiffness)
    override fun <T> fastSpatialSpec(): FiniteAnimationSpec<T> = spring(spatialDamping, fastSpatialStiffness)
    override fun <T> slowSpatialSpec(): FiniteAnimationSpec<T> = spring(spatialDamping, slowSpatialStiffness)
    override fun <T> defaultEffectsSpec(): FiniteAnimationSpec<T> = spring(1f, defaultEffectsStiffness)
    override fun <T> fastEffectsSpec(): FiniteAnimationSpec<T> = spring(1f, fastEffectsStiffness)
    override fun <T> slowEffectsSpec(): FiniteAnimationSpec<T> = spring(1f, slowEffectsStiffness)
}
