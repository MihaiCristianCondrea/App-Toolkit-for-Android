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

import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.requireDensity
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * The moving state of a falling-particle effect, such as snow or rain, kept apart from Compose so
 * its motion can be tested directly. [ParticleEffectNode] drives it.
 */
internal interface ParticleSimulation {

    /** Number of particles currently simulated; none means there is nothing to draw or move. */
    val particleCount: Int

    /** Rebuilds the particles for a [widthPx] by [heightPx] surface at [pxPerDp]. */
    fun resize(widthPx: Int, heightPx: Int, pxPerDp: Float)

    /** Moves everything on by [elapsedMillis]. */
    fun advance(elapsedMillis: Float)

    /** Draws the particles over what is already drawn. */
    fun draw(scope: DrawScope)
}

/**
 * Draws a [ParticleSimulation] over its element's content, shared by `snowfall` and `rainfall`.
 *
 * Only the draw phase runs per frame: the frame loop follows the composition's frame clock, so it
 * stops while the window is in the background, and it only runs while the effect is enabled and
 * has particles to move. Nothing recomposes or re-measures while particles fall, and the effect
 * takes no input.
 *
 * @param createSimulation Builds the simulation for a style, again whenever the style changes.
 */
internal class ParticleEffectNode<S : Any>(
    private var style: S,
    private var enabled: Boolean,
    private val createSimulation: (S) -> ParticleSimulation,
) : Modifier.Node(), DrawModifierNode, LayoutAwareModifierNode {

    private var simulation: ParticleSimulation = createSimulation(style)
    private var size: IntSize = IntSize.Zero
    private var frameLoop: Job? = null

    override val shouldAutoInvalidate: Boolean = false

    override fun onAttach() {
        startIfNeeded()
    }

    override fun onDetach() {
        stop()
    }

    fun update(style: S, enabled: Boolean) {
        if (style != this.style) {
            this.style = style
            simulation = createSimulation(style)
            resizeSimulation()
        }
        if (enabled != this.enabled) {
            this.enabled = enabled
            if (enabled) startIfNeeded() else stop()
        }
        invalidateDraw()
    }

    override fun onRemeasured(size: IntSize) {
        if (size == this.size) return
        this.size = size
        resizeSimulation()
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        if (enabled) simulation.draw(this)
    }

    private fun resizeSimulation() {
        if (!isAttached) return
        simulation.resize(
            widthPx = size.width,
            heightPx = size.height,
            pxPerDp = requireDensity().density,
        )
        // An empty surface, or a style with no particles, needs no frames.
        if (simulation.particleCount > 0) startIfNeeded() else stop()
    }

    private fun startIfNeeded() {
        if (!enabled || !isAttached || simulation.particleCount == 0 || frameLoop?.isActive == true) return
        frameLoop = coroutineScope.launch {
            var lastFrameNanos = -1L
            while (isActive) {
                withFrameNanos { frameNanos ->
                    if (lastFrameNanos >= 0) {
                        simulation.advance((frameNanos - lastFrameNanos) / NANOS_PER_MILLI)
                    }
                    lastFrameNanos = frameNanos
                }
                invalidateDraw()
            }
        }
    }

    private fun stop() {
        frameLoop?.cancel()
        frameLoop = null
    }

    private companion object {
        const val NANOS_PER_MILLI: Float = 1_000_000f
    }
}
