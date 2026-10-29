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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ConversionUnit
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.RulerRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.UnitConversionRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.RulerToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ScreenLightToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.UnitConverterToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ScreenLightToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.UnitConverterToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExpandedToolsViewModelsTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `unit conversion repository converts exact SI, Fahrenheit, and rejects below absolute zero`() {
        val repository = UnitConversionRepository()

        assertEquals("100", repository.convert("1", ConversionUnit.Metre, ConversionUnit.Centimetre))
        assertEquals("32", repository.convert("0", ConversionUnit.Celsius, ConversionUnit.Fahrenheit))
        assertEquals("100", repository.convert("212", ConversionUnit.Fahrenheit, ConversionUnit.Celsius))
        assertEquals("1000", repository.convert("1,0", ConversionUnit.Kilobyte, ConversionUnit.Byte))
        assertNull(repository.convert("-300", ConversionUnit.Celsius, ConversionUnit.Kelvin))
        assertNull(repository.convert("abc", ConversionUnit.Metre, ConversionUnit.Foot))
    }

    @Test
    fun `unit converter view model updates bidirectionally and preserves inputs on dismiss`() {
        val viewModel = UnitConverterToolViewModel(
            repository = UnitConversionRepository(),
            telemetryRepository = FakeTelemetryRepository(),
        )

        viewModel.onEvent(UnitConverterToolEvent.CategorySelected(ConversionCategory.Temperature))
        assertEquals(ConversionCategory.Temperature, viewModel.state.value.category)
        assertEquals(ConversionUnit.Celsius, viewModel.state.value.from)
        assertEquals(ConversionUnit.Fahrenheit, viewModel.state.value.to)

        viewModel.onEvent(UnitConverterToolEvent.InputChanged(input = "100", left = true))
        assertEquals("212", viewModel.state.value.right)
        assertFalse(viewModel.state.value.invalidInput)

        viewModel.onEvent(UnitConverterToolEvent.InputChanged(input = "32", left = false))
        assertEquals("0", viewModel.state.value.left)

        viewModel.onEvent(UnitConverterToolEvent.InputChanged(input = "-500", left = true))
        assertTrue(viewModel.state.value.invalidInput)

        viewModel.onEvent(UnitConverterToolEvent.Dismiss)
        assertEquals("-500", viewModel.state.value.left)
        assertTrue(viewModel.state.value.invalidInput)
    }

    @Test
    fun `ruler view model clamps range and scale, persists calibration, and keeps its draft and units on dismiss`() {
        val preferences = FakeToolkitTilesPreferencesDataSource()
        val repository = RulerRepository(preferences = preferences)
        val viewModel = RulerToolViewModel(
            repository = repository,
            telemetryRepository = FakeTelemetryRepository(),
        )

        viewModel.onEvent(RulerToolEvent.RangeChanged(start = -0.2f, end = 1.5f))
        assertEquals(0f, viewModel.state.value.start)
        assertEquals(1f, viewModel.state.value.end)

        viewModel.onEvent(RulerToolEvent.ScaleChanged(scale = 1.25f))
        viewModel.onEvent(RulerToolEvent.SaveScale)
        assertEquals(1.25f, preferences.rulerScale.value)

        viewModel.onEvent(RulerToolEvent.ScaleChanged(scale = 1.8f))
        viewModel.onEvent(RulerToolEvent.ToggleUnits)
        assertTrue(viewModel.state.value.inches)

        viewModel.onEvent(RulerToolEvent.Dismiss)
        assertEquals(1.8f, viewModel.state.value.scale)
        assertTrue(viewModel.state.value.inches)
    }

    @Test
    fun `screen light view model clamps brightness and warmth and resets on dismiss`() {
        val viewModel = ScreenLightToolViewModel(telemetryRepository = FakeTelemetryRepository())

        viewModel.onEvent(ScreenLightToolEvent.Open)
        viewModel.onEvent(ScreenLightToolEvent.BrightnessChanged(value = 0f))
        viewModel.onEvent(ScreenLightToolEvent.WarmthChanged(value = 1.5f))
        assertEquals(0.01f, viewModel.state.value.brightness)
        assertEquals(1f, viewModel.state.value.warmth)

        viewModel.onEvent(ScreenLightToolEvent.Dismiss)
        assertEquals(ScreenLightToolUiState(), viewModel.state.value)
    }
}
