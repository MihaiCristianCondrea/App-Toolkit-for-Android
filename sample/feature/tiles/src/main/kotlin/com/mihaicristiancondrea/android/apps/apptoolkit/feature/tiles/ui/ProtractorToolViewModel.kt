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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ProtractorToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ProtractorToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel

class ProtractorToolViewModel(
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<ProtractorToolUiState, ProtractorToolEvent>(
    initialState = ProtractorToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "ProtractorToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.PROTRACTOR)

    override fun handleEvent(event: ProtractorToolEvent) {
        when (event) {
            is ProtractorToolEvent.AngleChanged -> {
                if (!event.angle.isFinite()) return
                setState { copy(angle = event.angle.coerceIn(0f, 180f)) }
                usage.markUsed()
            }
            ProtractorToolEvent.Dismiss -> {
                setState { ProtractorToolUiState() }
                usage.endSession()
            }
        }
    }
}
