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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences.FakeToolkitTilesPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.CounterRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CoinFlipToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CounterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.DiceRollToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.CoinSide
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CoinFlipToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.DiceRollToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DecisionToolViewModelsTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `coin flip publishes a new request and resets on dismiss`() {
        val viewModel = CoinFlipToolViewModel(telemetryRepository = FakeTelemetryRepository())

        viewModel.onEvent(CoinFlipToolEvent.Flip)
        assertEquals(1, viewModel.state.value.request)

        viewModel.onEvent(CoinFlipToolEvent.Dismiss)
        assertEquals(CoinFlipToolUiState(), viewModel.state.value)
        assertEquals(CoinSide.Heads, viewModel.state.value.side)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `dice roll stays in range and resets on dismiss`() {
        val viewModel = DiceRollToolViewModel(telemetryRepository = FakeTelemetryRepository())

        viewModel.onEvent(DiceRollToolEvent.Roll)
        assertTrue(viewModel.state.value.result in 1..6)
        assertEquals(1, viewModel.state.value.request)

        viewModel.onEvent(DiceRollToolEvent.Dismiss)
        assertEquals(DiceRollToolUiState(), viewModel.state.value)
        assertTrue(viewModel.messages.value.isEmpty())
    }

    @Test
    fun `counter shares one count across sheets and resets it`() {
        val repository = CounterRepository(
            preferencesDataSource = FakeToolkitTilesPreferencesDataSource(),
            dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        )
        val firstSheet = CounterToolViewModel(repository = repository, telemetryRepository = FakeTelemetryRepository())

        firstSheet.onEvent(CounterToolEvent.Increment)
        firstSheet.onEvent(CounterToolEvent.Increment)
        assertEquals(2, firstSheet.state.value.count)

        val reopenedSheet = CounterToolViewModel(
            repository = repository,
            telemetryRepository = FakeTelemetryRepository(),
        )
        assertEquals(2, reopenedSheet.state.value.count)

        reopenedSheet.onEvent(CounterToolEvent.Reset)
        assertEquals(0, firstSheet.state.value.count)
        assertEquals(0, reopenedSheet.state.value.count)
    }

    @Test
    fun `closing the counter keeps the count`() {
        val repository = CounterRepository(
            preferencesDataSource = FakeToolkitTilesPreferencesDataSource(),
            dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        )
        val viewModel = CounterToolViewModel(repository = repository, telemetryRepository = FakeTelemetryRepository())

        viewModel.onEvent(CounterToolEvent.Increment)
        viewModel.onEvent(CounterToolEvent.Dismiss)

        assertEquals(1, viewModel.state.value.count)
    }
}
