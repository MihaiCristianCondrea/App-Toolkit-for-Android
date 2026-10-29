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

import androidx.lifecycle.SavedStateHandle
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.WheelToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.WheelToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random

class WheelToolViewModel(telemetryRepository: TelemetryRepository,
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(), private val choose: (Int) -> Int = { Random.nextInt(it) },
) : LoggedScreenViewModel<WheelToolUiState, WheelToolEvent>(
    initialState = WheelToolUiState(), telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.PLAY.name, viewModelName = "WheelToolViewModel",
) {
    private val usage = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker(telemetryRepository, com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds.DECISION_WHEEL)
    init { updateChoices(savedStateHandle["choices"] ?: "") }
    override fun handleEvent(event: WheelToolEvent) {
        when (event) {
            is WheelToolEvent.ChoicesChanged -> if (!currentState.spinning && event.text.length <= 1000) updateChoices(event.text)
            WheelToolEvent.Spin -> {
                if (!currentState.valid || currentState.spinning) return
                usage.markUsed()
                val picked = choose(currentState.choices.size)
                val target = 360f - (picked + .5f) * 360f / currentState.choices.size
                setState { copy(spinning = true, winner = choices[picked], target = rotation + 1800f + ((target - rotation % 360f + 360f) % 360f)) }
            }
            WheelToolEvent.Settled -> setState { copy(spinning = false, rotation = target) }
            WheelToolEvent.Dismiss -> { setState { copy(spinning = false, rotation = target) }; usage.endSession() }
        }
    }
    private fun updateChoices(text: String) {
        val choices = text.lineSequence().map(String::trim).filter(String::isNotEmpty).toList().toImmutableList()
        savedStateHandle["choices"] = text
        setState { copy(text = text, choices = choices, winner = null, rotation = 0f, target = 0f) }
    }
}
