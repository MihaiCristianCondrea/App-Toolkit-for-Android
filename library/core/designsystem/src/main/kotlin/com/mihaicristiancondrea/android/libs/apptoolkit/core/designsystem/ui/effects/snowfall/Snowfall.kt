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

import androidx.compose.ui.Modifier
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.ParticleEffectNode

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
) : ModifierNodeElement<ParticleEffectNode<SnowfallStyle>>() {

    override fun create(): ParticleEffectNode<SnowfallStyle> =
        ParticleEffectNode(style = style, enabled = enabled) { SnowfallSimulation(style = it) }

    override fun update(node: ParticleEffectNode<SnowfallStyle>) {
        node.update(style = style, enabled = enabled)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "snowfall"
        properties["style"] = style
        properties["enabled"] = enabled
    }
}
