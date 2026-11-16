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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects

import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * The moving state of a falling-particle effect, such as snow or rain, kept apart from Compose so
 * its motion can be tested directly. [ParticleEffectNode] drives it.
 */
internal interface ParticleSimulation {

    /** Number of particles currently simulated; none means there is nothing to draw or move. */
    val particleCount: Int

    /** Rebuilds the particles for a [widthPx] by [heightPx] surface at [pxPerDp]. */
    fun resize(widthPx: Int, heightPx: Int, pxPerDp: Float)

    fun advance(elapsedMillis: Float)

    fun draw(scope: DrawScope)
}
