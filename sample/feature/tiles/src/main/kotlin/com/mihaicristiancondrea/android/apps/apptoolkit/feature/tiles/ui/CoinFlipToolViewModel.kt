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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CoinFlipToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.CoinSide
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CoinFlipToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import kotlin.random.Random

/** Flips the Coin Flip tool's coin. Closing the sheet shows heads again. */
class CoinFlipToolViewModel(
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<CoinFlipToolUiState, CoinFlipToolEvent>(
    initialState = CoinFlipToolUiState(),
    telemetryRepository = telemetryRepository,
    screenName = AppScreenTracking.Screens.TOOLKIT_TILES.name,
    viewModelName = "CoinFlipToolViewModel",
) {
    private val usage = ToolUsageTracker(telemetryRepository, ToolkitTileIds.COIN_FLIP)

    override fun handleEvent(event: CoinFlipToolEvent) {
        when (event) {
            CoinFlipToolEvent.Flip -> flip()
            CoinFlipToolEvent.Dismiss -> dismiss()
        }
    }

    private fun flip() {
        launchReport(action = Actions.FLIP) {
            val side: CoinSide = if (Random.nextBoolean()) CoinSide.Heads else CoinSide.Tails
            setState { copy(side = side, request = request + 1) }
            usage.markUsed()
        }
    }

    private fun dismiss() {
        setState { CoinFlipToolUiState() }
        usage.endSession()
    }

    private object Actions {
        const val FLIP: String = "flipCoin"
    }
}
