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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.contracts.AppGa4Contract
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.sensors.FakeSensorLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.TorchPreset
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.FakeTorchRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.MorseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SensorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CompassToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.FlashDimmerToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.LevelToolEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlatformToolViewModelsTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()
    private val sensors = FakeSensorLocalDataSource()

    private fun operationErrors(): List<AnalyticsEvent> =
        telemetryRepository.loggedEvents.filter { it.name == "vm_op_error" }

    @Test
    fun `the compass shows readings only while its sensor runs`() {
        val viewModel = CompassToolViewModel(
            repository = SensorRepository(localDataSource = sensors),
            telemetryRepository = telemetryRepository,
        )

        viewModel.onEvent(CompassToolEvent.StartSensor)
        sensors.azimuth.tryEmit(90f)
        assertEquals(90f, viewModel.state.value.azimuth)
        assertEquals(1, sensors.subscriptionCount)

        viewModel.onEvent(CompassToolEvent.StopSensor)
        sensors.azimuth.tryEmit(180f)

        assertEquals(0, sensors.subscriptionCount)
        assertEquals(90f, viewModel.state.value.azimuth)
    }

    @Test
    fun `closing the level stops its sensor`() {
        val viewModel = LevelToolViewModel(
            repository = SensorRepository(localDataSource = sensors),
            telemetryRepository = telemetryRepository,
        )

        viewModel.onEvent(LevelToolEvent.Open)
        viewModel.onEvent(LevelToolEvent.StartSensor)
        sensors.orientation.tryEmit(12f to -4f)
        assertEquals(12f, viewModel.state.value.pitch)
        assertEquals(-4f, viewModel.state.value.roll)

        viewModel.onEvent(LevelToolEvent.Dismiss)

        assertEquals(0, sensors.subscriptionCount)
    }

    @Test
    fun `a failing sensor is reported, and starting it again listens again`() {
        val viewModel = CompassToolViewModel(
            repository = SensorRepository(localDataSource = sensors),
            telemetryRepository = telemetryRepository,
        )
        sensors.failure = IllegalStateException("sensor")

        viewModel.onEvent(CompassToolEvent.StartSensor)

        val error: AnalyticsEvent = operationErrors().single()
        assertEquals(AnalyticsValue.Str("observeAzimuth"), error.params["action"])
        assertEquals(AnalyticsValue.Str("CompassToolViewModel"), error.params["view_model"])
        assertTrue(viewModel.messages.value.isEmpty())

        sensors.failure = null
        viewModel.onEvent(CompassToolEvent.StartSensor)
        sensors.azimuth.tryEmit(45f)

        assertEquals(45f, viewModel.state.value.azimuth)
    }

    @Test
    fun `the dimmer stops Morse playback before it changes the torch, and turns it off on close`() {
        val torch = FakeTorchRepository()
        val morse = MorseRepository(
            torchRepository = torch,
            dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        )
        val viewModel = FlashDimmerToolViewModel(
            torchRepository = torch,
            morseRepository = morse,
            telemetryRepository = telemetryRepository,
        )
        morse.start("SOS")

        viewModel.onEvent(FlashDimmerToolEvent.PresetSelected(TorchPreset.Half))

        assertFalse(morse.state.value.isActive)
        assertEquals(2, viewModel.state.value.torch.currentLevel)
        assertTrue(
            telemetryRepository.loggedEvents.any { event ->
                event.name == AppGa4Contract.EventName.TOOL_USED &&
                    event.params[AppGa4Contract.Param.TOOL_ID] == AnalyticsValue.Str(ToolkitTileIds.FLASH_DIMMER)
            },
        )

        viewModel.onEvent(FlashDimmerToolEvent.LevelChanged(level = 4))
        assertEquals(4, viewModel.state.value.torch.currentLevel)

        viewModel.onEvent(FlashDimmerToolEvent.Dismiss)

        assertEquals(0, viewModel.state.value.torch.currentLevel)
        assertTrue(operationErrors().isEmpty())
    }
}
