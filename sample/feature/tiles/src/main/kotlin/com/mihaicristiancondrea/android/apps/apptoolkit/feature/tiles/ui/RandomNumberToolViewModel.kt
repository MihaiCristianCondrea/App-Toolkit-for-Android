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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.extensions.toToolLongOrNull
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RandomNumberToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RandomNumberToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlin.random.Random
import kotlin.random.nextLong

class RandomNumberToolViewModel(
    telemetryRepository: TelemetryRepository,
    private val random: Random = Random.Default,
) : LoggedScreenViewModel<RandomNumberToolUiState, RandomNumberToolEvent>(
    initialState = RandomNumberToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.NUMBERS.name,
    viewModelName = "RandomNumberToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.RANDOM_NUMBER)

    override fun handleEvent(event: RandomNumberToolEvent) {
        when (event) {
            is RandomNumberToolEvent.MinimumChanged -> {
                if (event.input.length > 20) return
                setState { copy(minimum = event.input, result = null) }
                validateRange()
            }
            is RandomNumberToolEvent.MaximumChanged -> {
                if (event.input.length > 20) return
                setState { copy(maximum = event.input, result = null) }
                validateRange()
            }
            is RandomNumberToolEvent.PresetSelected -> {
                if (event.maximum !in listOf(2L, 6L, 10L, 100L)) return
                setState { copy(minimum = "1", maximum = event.maximum.toString(), result = null, invalidInput = false) }
            }
            RandomNumberToolEvent.Generate -> {
                val range = validateRange() ?: return
                setState { copy(result = random.nextLong(range)) }
                usage.markUsed()
            }
            RandomNumberToolEvent.Dismiss -> {
                setState { RandomNumberToolUiState() }
                usage.endSession()
            }
        }
    }

    private fun validateRange(): LongRange? {
        val minimum = currentState.minimum.toToolLongOrNull()
        val maximum = currentState.maximum.toToolLongOrNull()
        if (minimum == null || maximum == null || minimum > maximum) {
            setState { copy(invalidInput = true) }
            return null
        }
        setState { copy(invalidInput = false) }
        return minimum..maximum
    }
}
