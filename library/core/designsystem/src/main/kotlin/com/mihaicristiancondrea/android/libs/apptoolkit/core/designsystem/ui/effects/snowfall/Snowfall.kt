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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.snowfall

import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.requireDensity
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Draws falling snow over this element's content.
 *
 * The snow is drawn after the content, so it sits on top of it, and it takes no input: taps go
 * through to whatever is underneath. Apply it last in a chain to cover everything the element draws.
 *
 * Only the draw phase runs per frame. Nothing recomposes or re-measures while snow falls, and the
 * frame loop follows the composition's frame clock, so it stops while the window is in the
 * background. Callers that honour the system's animation setting should pass `enabled = false`
 * when it is off (see `Context.isSystemAnimationDisabled`).
 *
 * ```
 * Box(Modifier.fillMaxSize().snowfall(SnowfallStyle(density = 0.5f, wind = 0.2f)))
 * ```
 *
 * @param style Appearance and motion of the flakes.
 * @param enabled When false nothing is drawn and no frames are requested.
 */
fun Modifier.snowfall(
    style: SnowfallStyle = SnowfallStyle(),
    enabled: Boolean = true,
): Modifier = this then SnowfallElement(style = style, enabled = enabled)

private data class SnowfallElement(
    val style: SnowfallStyle,
    val enabled: Boolean,
) : ModifierNodeElement<SnowfallNode>() {

    override fun create(): SnowfallNode = SnowfallNode(style = style, enabled = enabled)

    override fun update(node: SnowfallNode) {
        node.update(style = style, enabled = enabled)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "snowfall"
        properties["style"] = style
        properties["enabled"] = enabled
    }
}

private class SnowfallNode(
    private var style: SnowfallStyle,
    private var enabled: Boolean,
) : Modifier.Node(), DrawModifierNode, LayoutAwareModifierNode {

    private var simulation: SnowfallSimulation = SnowfallSimulation(style = style)
    private var size: IntSize = IntSize.Zero
    private var frameLoop: Job? = null

    override val shouldAutoInvalidate: Boolean = false

    override fun onAttach() {
        startIfNeeded()
    }

    override fun onDetach() {
        frameLoop = null
    }

    fun update(style: SnowfallStyle, enabled: Boolean) {
        if (style != this.style) {
            this.style = style
            simulation = SnowfallSimulation(style = style)
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
    }

    private fun startIfNeeded() {
        if (!enabled || !isAttached || frameLoop?.isActive == true) return
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
