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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CoinFlipToolState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.DiceRollToolState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DecisionToolViewModelsTest {

    @JvmField
    @RegisterExtension
    val dispatcherExtension = UnconfinedDispatcherExtension()

    @Test
    fun `coin flip publishes a new request and resets on dismiss`() {
        val viewModel = CoinFlipToolViewModel(FakeFirebaseController())

        viewModel.flip()
        assertEquals(1, viewModel.state.value.request)

        viewModel.dismiss()
        assertEquals(CoinFlipToolState(), viewModel.state.value)
    }

    @Test
    fun `dice roll stays in range and resets on dismiss`() {
        val viewModel = DiceRollToolViewModel(FakeFirebaseController())

        viewModel.roll()
        assertTrue(viewModel.state.value.result in 1..6)
        assertEquals(1, viewModel.state.value.request)

        viewModel.dismiss()
        assertEquals(DiceRollToolState(), viewModel.state.value)
    }

    @Test
    fun `counter shares one count across sheets and resets it`() {
        val repository = CounterRepository(
            preferencesDataSource = FakeToolkitTilesPreferencesDataSource(),
            dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        )
        val firstSheet = CounterToolViewModel(repository, FakeFirebaseController())

        firstSheet.increment()
        firstSheet.increment()
        assertEquals(2, firstSheet.count.value)

        val reopenedSheet = CounterToolViewModel(repository, FakeFirebaseController())
        assertEquals(2, reopenedSheet.count.value)

        reopenedSheet.reset()
        assertEquals(0, firstSheet.count.value)
        assertEquals(0, reopenedSheet.count.value)
    }
}
