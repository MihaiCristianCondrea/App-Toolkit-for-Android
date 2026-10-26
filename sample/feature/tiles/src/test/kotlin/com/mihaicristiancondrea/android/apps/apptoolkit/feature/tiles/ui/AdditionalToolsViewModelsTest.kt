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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.BillSplitterRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BillSplitterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ProtractorToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RandomNumberToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.BillSplitterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ProtractorToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RandomNumberToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.math.BigDecimal
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdditionalToolsViewModelsTest {
    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `protractor validates angles and resets on dismiss`() {
        val viewModel = ProtractorToolViewModel(FakeTelemetryRepository())
        viewModel.onEvent(ProtractorToolEvent.AngleChanged(45f))
        viewModel.onEvent(ProtractorToolEvent.AngleChanged(Float.NaN))
        assertEquals(45f, viewModel.state.value.angle)
        viewModel.onEvent(ProtractorToolEvent.AngleChanged(200f))
        assertEquals(180f, viewModel.state.value.angle)
        viewModel.onEvent(ProtractorToolEvent.AngleChanged(-10f))
        assertEquals(0f, viewModel.state.value.angle)
        viewModel.onEvent(ProtractorToolEvent.Dismiss)
        assertEquals(ProtractorToolUiState(), viewModel.state.value)
    }

    @Test
    fun `bill splitter recalculates clears invalid results and recovers`() {
        val viewModel = BillSplitterToolViewModel(BillSplitterRepository(), FakeTelemetryRepository())
        viewModel.onEvent(BillSplitterToolEvent.BillChanged("100"))
        viewModel.onEvent(BillSplitterToolEvent.PeopleChanged("2"))
        assertEquals(BigDecimal("57.50"), viewModel.state.value.result?.perPerson)
        viewModel.onEvent(BillSplitterToolEvent.TipSelected(20))
        assertEquals(BigDecimal("60.00"), viewModel.state.value.result?.perPerson)
        viewModel.onEvent(BillSplitterToolEvent.PeopleChanged("0"))
        assertTrue(viewModel.state.value.invalidInput)
        assertNull(viewModel.state.value.result)
        viewModel.onEvent(BillSplitterToolEvent.PeopleChanged("3"))
        assertFalse(viewModel.state.value.invalidInput)
        assertEquals(BigDecimal("40.00"), viewModel.state.value.result?.perPerson)
        viewModel.onEvent(BillSplitterToolEvent.TipSelected(99))
        assertEquals(20, viewModel.state.value.tipPercent)
        viewModel.onEvent(BillSplitterToolEvent.Dismiss)
        assertEquals(BillSplitterToolUiState(), viewModel.state.value)
    }

    @Test
    fun `people controls recalculate support localized input and stop at bounds`() {
        val viewModel = BillSplitterToolViewModel(BillSplitterRepository(), FakeTelemetryRepository())
        viewModel.onEvent(BillSplitterToolEvent.BillChanged("100"))
        viewModel.onEvent(BillSplitterToolEvent.RemovePerson)
        assertEquals("1", viewModel.state.value.people)
        viewModel.onEvent(BillSplitterToolEvent.AddPerson)
        assertEquals("2", viewModel.state.value.people)
        assertEquals(BigDecimal("57.50"), viewModel.state.value.result?.perPerson)
        viewModel.onEvent(BillSplitterToolEvent.PeopleChanged("٣"))
        viewModel.onEvent(BillSplitterToolEvent.RemovePerson)
        assertEquals("2", viewModel.state.value.people)
        viewModel.onEvent(BillSplitterToolEvent.PeopleChanged(Long.MAX_VALUE.toString()))
        viewModel.onEvent(BillSplitterToolEvent.AddPerson)
        assertEquals(Long.MAX_VALUE.toString(), viewModel.state.value.people)
        for (invalid in listOf("", "0", "-1", "9223372036854775808")) {
            viewModel.onEvent(BillSplitterToolEvent.PeopleChanged(invalid))
            viewModel.onEvent(BillSplitterToolEvent.AddPerson)
            viewModel.onEvent(BillSplitterToolEvent.RemovePerson)
            assertEquals(invalid, viewModel.state.value.people)
        }
    }

    @Test
    fun `presets replace invalid ranges and clear generated results`() {
        val viewModel = RandomNumberToolViewModel(FakeTelemetryRepository(), Random(42))
        for (maximum in listOf(2L, 6L, 10L, 100L)) {
            viewModel.onEvent(RandomNumberToolEvent.MinimumChanged("invalid"))
            viewModel.onEvent(RandomNumberToolEvent.PresetSelected(maximum))
            assertEquals("1", viewModel.state.value.minimum)
            assertEquals(maximum.toString(), viewModel.state.value.maximum)
            assertFalse(viewModel.state.value.invalidInput)
            assertNull(viewModel.state.value.result)
            viewModel.onEvent(RandomNumberToolEvent.Generate)
            assertTrue(assertNotNull(viewModel.state.value.result) in 1..maximum)
            viewModel.onEvent(RandomNumberToolEvent.PresetSelected(maximum))
            assertNull(viewModel.state.value.result)
        }
        val before = viewModel.state.value
        viewModel.onEvent(RandomNumberToolEvent.PresetSelected(Long.MAX_VALUE))
        assertEquals(before, viewModel.state.value)
    }

    @Test
    fun `random picker includes both bounds and clears results when edited`() {
        val viewModel = RandomNumberToolViewModel(FakeTelemetryRepository(), Random(42))
        viewModel.onEvent(RandomNumberToolEvent.MinimumChanged("0"))
        viewModel.onEvent(RandomNumberToolEvent.MaximumChanged("1"))
        val results = mutableSetOf<Long>()
        repeat(100) {
            viewModel.onEvent(RandomNumberToolEvent.Generate)
            results.add(assertNotNull(viewModel.state.value.result))
        }
        assertEquals(setOf(0L, 1L), results)
        viewModel.onEvent(RandomNumberToolEvent.MinimumChanged("2"))
        assertTrue(viewModel.state.value.invalidInput)
        assertNull(viewModel.state.value.result)
        viewModel.onEvent(RandomNumberToolEvent.Generate)
        assertNull(viewModel.state.value.result)
        viewModel.onEvent(RandomNumberToolEvent.MaximumChanged("2"))
        viewModel.onEvent(RandomNumberToolEvent.Generate)
        assertEquals(2L, viewModel.state.value.result)
        viewModel.onEvent(RandomNumberToolEvent.Dismiss)
        assertEquals(RandomNumberToolUiState(), viewModel.state.value)
    }

    @Test
    fun `random picker supports signed Long bounds without overflowing`() {
        val viewModel = RandomNumberToolViewModel(FakeTelemetryRepository(), Random(1))
        viewModel.onEvent(RandomNumberToolEvent.MinimumChanged(Long.MAX_VALUE.toString()))
        viewModel.onEvent(RandomNumberToolEvent.MaximumChanged(Long.MAX_VALUE.toString()))
        viewModel.onEvent(RandomNumberToolEvent.Generate)
        assertEquals(Long.MAX_VALUE, viewModel.state.value.result)
        viewModel.onEvent(RandomNumberToolEvent.MinimumChanged(Long.MIN_VALUE.toString()))
        viewModel.onEvent(RandomNumberToolEvent.Generate)
        assertNotNull(viewModel.state.value.result)
        assertFalse(viewModel.state.value.invalidInput)
        viewModel.onEvent(RandomNumberToolEvent.MaximumChanged("9223372036854775808"))
        assertTrue(viewModel.state.value.invalidInput)
        assertNull(viewModel.state.value.result)
        viewModel.onEvent(RandomNumberToolEvent.MaximumChanged("1.5"))
        assertTrue(viewModel.state.value.invalidInput)
        viewModel.onEvent(RandomNumberToolEvent.MinimumChanged("١"))
        viewModel.onEvent(RandomNumberToolEvent.MaximumChanged("١"))
        viewModel.onEvent(RandomNumberToolEvent.Generate)
        assertEquals(1L, viewModel.state.value.result)
    }

    @Test
    fun `protractor reports use once per opening`() {
        val telemetry = FakeTelemetryRepository()
        val viewModel = ProtractorToolViewModel(telemetry)
        viewModel.onEvent(ProtractorToolEvent.AngleChanged(30f))
        viewModel.onEvent(ProtractorToolEvent.AngleChanged(60f))
        assertEquals(1, telemetry.loggedEvents.count { it.name == "tool_used" })
        viewModel.onEvent(ProtractorToolEvent.Dismiss)
        viewModel.onEvent(ProtractorToolEvent.AngleChanged(90f))
        assertEquals(2, telemetry.loggedEvents.count { it.name == "tool_used" })
    }
}
