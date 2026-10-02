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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.DiceRollToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.DiceRollToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlin.random.Random

/** Rolls the Dice Roll tool's die. Closing the sheet shows a one again. */
class DiceRollToolViewModel(
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<DiceRollToolUiState, DiceRollToolEvent>(
    initialState = DiceRollToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "DiceRollToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.DICE_ROLL)

    override fun handleEvent(event: DiceRollToolEvent) {
        when (event) {
            DiceRollToolEvent.Roll -> roll()
            DiceRollToolEvent.Dismiss -> dismiss()
        }
    }

    private fun roll() {
        launchReport(action = Actions.ROLL) {
            val result: Int = Random.nextInt(from = 1, until = 7)
            setState { copy(result = result, request = request + 1) }
            usage.markUsed()
        }
    }

    private fun dismiss() {
        setState { DiceRollToolUiState() }
        usage.endSession()
    }

    private object Actions {
        const val ROLL: String = "rollDice"
    }
}
