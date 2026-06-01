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

package com.wstxda.toolkit.ui.label

import android.content.Context
import com.wstxda.toolkit.R
import com.wstxda.toolkit.manager.breathing.BreathingPhase

class BreathingLabelProvider(private val context: Context) {

    fun getLabel(phase: BreathingPhase): CharSequence {
        return when (phase) {
            BreathingPhase.PREPARING, BreathingPhase.IDLE -> context.getString(R.string.breathing_tile)
            BreathingPhase.INHALE -> context.getString(R.string.breathing_tile_inhale)
            BreathingPhase.HOLD_FULL -> context.getString(R.string.breathing_tile_pause)
            BreathingPhase.EXHALE -> context.getString(R.string.breathing_tile_exhale)
            BreathingPhase.HOLD_EMPTY -> context.getString(R.string.breathing_tile_relax)
        }
    }

    fun getSubtitle(phase: BreathingPhase): CharSequence {
        return if (phase == BreathingPhase.IDLE) {
            context.getString(R.string.tile_start)
        } else {
            context.getString(R.string.breathing_tile_prepare)
        }
    }
}