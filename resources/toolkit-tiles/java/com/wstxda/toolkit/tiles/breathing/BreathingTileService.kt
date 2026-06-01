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

package com.wstxda.toolkit.tiles.breathing

import android.service.quicksettings.Tile
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.breathing.BreathingModule
import com.wstxda.toolkit.manager.breathing.BreathingPhase
import com.wstxda.toolkit.ui.icon.BreathingIconProvider
import com.wstxda.toolkit.ui.label.BreathingLabelProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BreathingTileService : BaseTileService() {

    private val breathingManager by lazy { BreathingModule.getInstance(applicationContext) }
    private val labelProvider by lazy { BreathingLabelProvider(applicationContext) }
    private val iconProvider by lazy { BreathingIconProvider(applicationContext) }
    private var visibilityJob: Job? = null

    override fun onClick() {
        breathingManager.toggle()
        updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        visibilityJob?.cancel()
        visibilityJob = null
    }

    override fun onStopListening() {
        super.onStopListening()
        visibilityJob = serviceScope.launch {
            delay(3000L)
            val currentState = breathingManager.breathingState.value.phase
            if (currentState != BreathingPhase.IDLE) {
                breathingManager.stop()
                updateTile()
            }
        }
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        breathingManager.breathingState,
    )

    override fun updateTile() {
        val breathingState = breathingManager.breathingState.value
        val isIdle = breathingState.phase == BreathingPhase.IDLE

        setTileState(
            state = if (isIdle) Tile.STATE_INACTIVE else Tile.STATE_ACTIVE,
            label = labelProvider.getLabel(breathingState.phase),
            subtitle = labelProvider.getSubtitle(breathingState.phase),
            icon = iconProvider.getIcon(breathingState.phase, breathingState.progress),
        )
    }
}