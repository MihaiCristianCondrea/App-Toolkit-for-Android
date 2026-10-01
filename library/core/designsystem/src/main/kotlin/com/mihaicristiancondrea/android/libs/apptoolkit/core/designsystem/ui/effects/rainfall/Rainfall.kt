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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.rainfall

import androidx.compose.ui.Modifier
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.effects.ParticleEffectNode

/**
 * Draws falling rain over this element's content, the way `snowfall` draws snow: streaks at
 * different depths that lean with gusting wind, thicken and thin in showers, and splash where they
 * land.
 *
 * The rain is drawn after the content, so it sits on top of it, and it takes no input: taps go
 * through to whatever is underneath. Apply it last in a chain to cover everything the element draws.
 *
 * Only the draw phase runs per frame. Nothing recomposes or re-measures while rain falls, and the
 * frame loop follows the composition's frame clock, so it stops while the window is in the
 * background. Callers that honour the system's animation setting should pass `enabled = false`
 * when it is off (see `Context.isSystemAnimationDisabled`).
 *
 * ```
 * Box(Modifier.fillMaxSize().rainfall(RainfallStyle(density = 0.5f, wind = 0.2f)))
 * ```
 *
 * @param style Appearance and motion of the drops.
 * @param enabled When false nothing is drawn and no frames are requested.
 */
fun Modifier.rainfall(
    style: RainfallStyle = RainfallStyle(),
    enabled: Boolean = true,
): Modifier = this then RainfallElement(style = style, enabled = enabled)

private data class RainfallElement(
    val style: RainfallStyle,
    val enabled: Boolean,
) : ModifierNodeElement<ParticleEffectNode<RainfallStyle>>() {

    override fun create(): ParticleEffectNode<RainfallStyle> =
        ParticleEffectNode(style = style, enabled = enabled) { RainfallSimulation(style = it) }

    override fun update(node: ParticleEffectNode<RainfallStyle>) {
        node.update(style = style, enabled = enabled)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "rainfall"
        properties["style"] = style
        properties["enabled"] = enabled
    }
}
