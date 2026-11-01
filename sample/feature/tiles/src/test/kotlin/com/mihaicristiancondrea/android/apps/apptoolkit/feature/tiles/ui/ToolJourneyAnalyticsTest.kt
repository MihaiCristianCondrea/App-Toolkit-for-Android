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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.contracts.AppGa4Contract
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.contracts.AppGa4ContractValidator
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.audio.SpeakerAudioDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.WaterEjectRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.toolOpenedEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.toolCycleFinishedEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.WaterEjectToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.delay
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ToolJourneyAnalyticsTest {
    companion object {
        @JvmField @RegisterExtension val dispatcher = UnconfinedDispatcherExtension()
    }

    @Test
    fun catalogueOpeningsHaveBoundedWorkspacesAndNoInputs() {
        val ids = ToolkitTileIds::class.java.fields.filter { it.type == String::class.java }
            .map { it.get(null) as String }
        assertTrue(ids.isNotEmpty())
        ids.forEach { id ->
            val event = toolOpenedEvent(id)
            assertEquals(AppGa4Contract.EventName.TOOL_OPENED, event.name)
            assertEquals(AppGa4Contract.requiredParams(event.name), event.params.keys)
            assertEquals(AnalyticsValue.Str(id), event.params[AppGa4Contract.Param.TOOL_ID])
            assertTrue(event.params[AppGa4Contract.Param.WORKSPACE] in
                setOf("quick_tools", "measure", "play").map { AnalyticsValue.Str(it) })
            assertTrue(AppGa4ContractValidator.forbiddenParams(event.params.keys).isEmpty())
        }
        assertEquals(AnalyticsValue.Str("measure"), toolOpenedEvent(ToolkitTileIds.RULER).params[AppGa4Contract.Param.WORKSPACE])
        assertEquals(AnalyticsValue.Str("play"), toolOpenedEvent(ToolkitTileIds.DECISION_WHEEL).params[AppGa4Contract.Param.WORKSPACE])
    }

    @Test
    fun arbitraryUserTextCannotBecomeAJourneyDimension() {
        assertFailsWith<IllegalStateException> { toolOpenedEvent("private wheel choice") }
        assertFailsWith<IllegalArgumentException> {
            toolCycleFinishedEvent(ToolkitTileIds.WATER_EJECT, "raw exception message")
        }
        AppGa4Contract.ToolCycleOutcome.all.forEach { outcome ->
            val event = toolCycleFinishedEvent(ToolkitTileIds.WATER_EJECT, outcome)
            assertEquals(AppGa4Contract.requiredParams(event.name), event.params.keys)
            assertEquals(AnalyticsValue.Str(outcome), event.params[AppGa4Contract.Param.OUTCOME])
        }
    }

    @Test
    fun duplicateStartAndIdleStopDoNotDuplicateCycleResults() {
        val telemetry = FakeTelemetryRepository()
        var starts = 0
        val source = object : SpeakerAudioDataSource {
            override suspend fun playCycle(onSecond: (Int) -> Unit) {
                starts++
                onSecond(30)
                delay(30_000)
                onSecond(0)
            }
        }
        val model = WaterEjectToolViewModel(WaterEjectRepository(source), telemetry)
        model.onEvent(WaterEjectToolEvent.Stop)
        assertTrue(telemetry.loggedEvents.none { it.name == AppGa4Contract.EventName.TOOL_CYCLE_FINISHED })
        model.onEvent(WaterEjectToolEvent.Start)
        model.onEvent(WaterEjectToolEvent.Start)
        assertEquals(1, starts)
        model.onEvent(WaterEjectToolEvent.Stop)
        model.onEvent(WaterEjectToolEvent.Stop)
        model.onEvent(WaterEjectToolEvent.Start)
        dispatcher.testDispatcher.scheduler.advanceUntilIdle()
        val results = telemetry.loggedEvents.filter { it.name == AppGa4Contract.EventName.TOOL_CYCLE_FINISHED }
        assertEquals(listOf("cancelled", "completed"), results.map {
            (it.params.getValue(AppGa4Contract.Param.OUTCOME) as AnalyticsValue.Str).value
        })
        assertEquals(2, telemetry.loggedEvents.count { it.name == AppGa4Contract.EventName.TOOL_USED })
        results.forEach { assertEquals(AppGa4Contract.requiredParams(it.name), it.params.keys) }
    }

    @Test
    fun failureBeforePlaybackIsNotSuccessfulToolUse() {
        val telemetry = FakeTelemetryRepository()
        val source = object : SpeakerAudioDataSource {
            override suspend fun playCycle(onSecond: (Int) -> Unit) { error("private hardware detail") }
        }
        val model = WaterEjectToolViewModel(WaterEjectRepository(source), telemetry)
        model.onEvent(WaterEjectToolEvent.Start)
        assertTrue(model.state.value.failed)
        assertTrue(telemetry.loggedEvents.none { it.name == AppGa4Contract.EventName.TOOL_USED })
        val result = telemetry.loggedEvents.single { it.name == AppGa4Contract.EventName.TOOL_CYCLE_FINISHED }
        assertEquals(AnalyticsValue.Str("failed"), result.params[AppGa4Contract.Param.OUTCOME])
        assertEquals(AppGa4Contract.requiredParams(result.name), result.params.keys)
    }

    @Test
    fun audioFocusCancellationIsNotFailureOrCompletion() {
        val telemetry = FakeTelemetryRepository()
        val source = object : SpeakerAudioDataSource {
            override suspend fun playCycle(onSecond: (Int) -> Unit) {
                onSecond(30)
                throw CancellationException("focus lost")
            }
        }
        val model = WaterEjectToolViewModel(WaterEjectRepository(source), telemetry)
        model.onEvent(WaterEjectToolEvent.Start)
        assertTrue(!model.state.value.running && !model.state.value.failed)
        val result = telemetry.loggedEvents.single { it.name == AppGa4Contract.EventName.TOOL_CYCLE_FINISHED }
        assertEquals(AnalyticsValue.Str("cancelled"), result.params[AppGa4Contract.Param.OUTCOME])
    }

    @Test
    fun stopBeforeThePlaybackJobStartsStillReportsOneCancellation() {
        val telemetry = FakeTelemetryRepository()
        val scheduler = dispatcher.testDispatcher.scheduler
        Dispatchers.setMain(StandardTestDispatcher(scheduler))
        var starts = 0
        val source = object : SpeakerAudioDataSource {
            override suspend fun playCycle(onSecond: (Int) -> Unit) { starts++ }
        }
        val model = WaterEjectToolViewModel(WaterEjectRepository(source), telemetry)
        model.onEvent(WaterEjectToolEvent.Start)
        model.onEvent(WaterEjectToolEvent.Stop)
        scheduler.advanceUntilIdle()
        assertEquals(0, starts)
        assertTrue(telemetry.loggedEvents.none { it.name == AppGa4Contract.EventName.TOOL_USED })
        val result = telemetry.loggedEvents.single { it.name == AppGa4Contract.EventName.TOOL_CYCLE_FINISHED }
        assertEquals(AnalyticsValue.Str("cancelled"), result.params[AppGa4Contract.Param.OUTCOME])
    }
}
