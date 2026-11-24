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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.audio.BellAudioDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.sensors.BellShakeDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.BellRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolGa4Contract
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.BellToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BellToolViewModelTest {
    companion object {
        @JvmField
        @RegisterExtension
        val dispatcher = UnconfinedDispatcherExtension()
    }

    @Test
    fun `open prepares audio, rings on tap and shake, and logs tool_used once per session`() {
        val telemetry = FakeTelemetryRepository()
        val shakes = MutableSharedFlow<Float?>(extraBufferCapacity = 4)
        val played = mutableListOf<Pair<Float, Float>>()
        var shakeCollectors = 0
        val viewModel = BellToolViewModel(
            repository = BellRepository(
                audio = FakeBellAudioDataSource(
                    ready = true,
                    onPlay = { volume, rate ->
                        played += volume to rate
                        true
                    },
                ),
                sensors = FakeBellShakeDataSource(
                    flow = shakes
                        .onStart { shakeCollectors++ }
                        .onCompletion { shakeCollectors-- },
                ),
            ),
            telemetryRepository = telemetry,
        )

        viewModel.onEvent(event = BellToolEvent.Open)
        assertTrue(viewModel.state.value.audioReady)
        assertFalse(viewModel.state.value.audioFailed)
        assertEquals(1, shakeCollectors)

        viewModel.onEvent(event = BellToolEvent.Ring)
        shakes.tryEmit(0.75f)

        assertEquals(2, viewModel.state.value.strike)
        assertEquals(2, played.size)
        assertEquals(0.9948f, played[0].second)
        assertEquals(1.0052f, played[1].second)

        val usedEvents = telemetry.loggedEvents.filter { it.name == ToolGa4Contract.EventName.TOOL_USED }
        assertEquals(1, usedEvents.size)
        assertEquals(
            AnalyticsValue.Str(ToolkitTileIds.BELL),
            usedEvents.single().params[ToolGa4Contract.Param.TOOL_ID],
        )

        viewModel.onEvent(event = BellToolEvent.Close)
        assertEquals(0, shakeCollectors)
        assertFalse(viewModel.state.value.audioReady)
        assertEquals(0, viewModel.state.value.strike)

        viewModel.onEvent(event = BellToolEvent.Open)
        viewModel.onEvent(event = BellToolEvent.Ring)
        assertEquals(
            2,
            telemetry.loggedEvents.count { it.name == ToolGa4Contract.EventName.TOOL_USED },
        )
        viewModel.onEvent(event = BellToolEvent.Close)
    }

    @Test
    fun `null shake emission marks motion unavailable while keeping manual ring working`() {
        val telemetry = FakeTelemetryRepository()
        val shakes = MutableSharedFlow<Float?>(extraBufferCapacity = 2)
        val viewModel = BellToolViewModel(
            repository = BellRepository(
                audio = FakeBellAudioDataSource(ready = true),
                sensors = FakeBellShakeDataSource(flow = shakes),
            ),
            telemetryRepository = telemetry,
        )

        viewModel.onEvent(event = BellToolEvent.Open)
        shakes.tryEmit(null)

        assertFalse(viewModel.state.value.motionAvailable)
        viewModel.onEvent(event = BellToolEvent.Ring)
        assertEquals(1, viewModel.state.value.strike)
        viewModel.onEvent(event = BellToolEvent.Close)
    }

    @Test
    fun `audio load failure sets audioFailed and stops the shake sensor`() {
        val telemetry = FakeTelemetryRepository()
        val shakes = MutableSharedFlow<Float?>()
        var shakeCollectors = 0
        val viewModel = BellToolViewModel(
            repository = BellRepository(
                audio = FakeBellAudioDataSource(ready = false),
                sensors = FakeBellShakeDataSource(
                    flow = shakes
                        .onStart { shakeCollectors++ }
                        .onCompletion { shakeCollectors-- },
                ),
            ),
            telemetryRepository = telemetry,
        )

        viewModel.onEvent(event = BellToolEvent.Open)

        assertFalse(viewModel.state.value.audioReady)
        assertTrue(viewModel.state.value.audioFailed)
        assertEquals(0, shakeCollectors)
        assertTrue(telemetry.loggedEvents.none { it.name == ToolGa4Contract.EventName.TOOL_USED })
    }

    @Test
    fun `audio runtime failure sets audioFailed and stops the shake sensor`() {
        val telemetry = FakeTelemetryRepository()
        val shakes = MutableSharedFlow<Float?>()
        var shakeCollectors = 0
        val viewModel = BellToolViewModel(
            repository = BellRepository(
                audio = object : BellAudioDataSource {
                    override suspend fun runSession(
                        onAudioReady: (Boolean) -> Unit,
                        session: suspend (play: suspend (volume: Float, rate: Float) -> Boolean) -> Unit,
                    ) {
                        error("SoundPool initialization failed")
                    }
                },
                sensors = FakeBellShakeDataSource(
                    flow = shakes
                        .onStart { shakeCollectors++ }
                        .onCompletion { shakeCollectors-- },
                ),
            ),
            telemetryRepository = telemetry,
        )

        viewModel.onEvent(event = BellToolEvent.Open)

        assertFalse(viewModel.state.value.audioReady)
        assertTrue(viewModel.state.value.audioFailed)
        assertEquals(0, shakeCollectors)
        assertTrue(telemetry.loggedEvents.none { it.name == ToolGa4Contract.EventName.TOOL_USED })
    }

    private class FakeBellAudioDataSource(
        private val ready: Boolean,
        private val onPlay: (Float, Float) -> Boolean = { _, _ -> true },
    ) : BellAudioDataSource {
        override suspend fun runSession(
            onAudioReady: (Boolean) -> Unit,
            session: suspend (play: suspend (volume: Float, rate: Float) -> Boolean) -> Unit,
        ) {
            onAudioReady(ready)
            if (!ready) return
            session { volume, rate -> onPlay(volume, rate) }
        }
    }

    private class FakeBellShakeDataSource(
        private val flow: Flow<Float?>,
    ) : BellShakeDataSource {
        override fun observe(): Flow<Float?> = flow
    }
}
