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
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.contracts.AppGa4ContractValidator
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.domain.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.sensors.FakeSensorLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.FakeTorchRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.MorseRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SensorRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.SosRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.domain.utils.ToolkitTileIds
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.ToolUsageTracker
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CoinFlipToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.CompassToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.DiceRollToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.MorseToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ReactionTestToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.SosToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.MorseInputError
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
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class ToolAnalyticsTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun toolUses(): List<String> = telemetryRepository.loggedEvents
        .filter { it.name == AppGa4Contract.EventName.TOOL_USED }
        .map { (it.params.getValue(AppGa4Contract.Param.TOOL_ID) as AnalyticsValue.Str).value }

    private fun startedActions(viewModelName: String): List<String> = telemetryRepository.loggedEvents
        .filter { it.name == "vm_op_start" && it.params["view_model"] == AnalyticsValue.Str(viewModelName) }
        .map { (it.params.getValue("action") as AnalyticsValue.Str).value }

    private fun morseRepository(): MorseRepository = MorseRepository(
        torchRepository = FakeTorchRepository(),
        dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
    )

    @Test
    fun `a tool is reported used once per opening, however often it is used`() {
        val viewModel = CoinFlipToolViewModel(telemetryRepository = telemetryRepository)

        repeat(times = 5) { viewModel.onEvent(CoinFlipToolEvent.Flip) }

        assertEquals(listOf(ToolkitTileIds.COIN_FLIP), toolUses())
    }

    @Test
    fun `reopening a tool reports its use again`() {
        val viewModel = DiceRollToolViewModel(telemetryRepository = telemetryRepository)

        viewModel.onEvent(DiceRollToolEvent.Roll)
        viewModel.onEvent(DiceRollToolEvent.Dismiss)
        viewModel.onEvent(DiceRollToolEvent.Roll)

        assertEquals(listOf(ToolkitTileIds.DICE_ROLL, ToolkitTileIds.DICE_ROLL), toolUses())
    }

    @Test
    fun `opening a tool without using it reports nothing`() {
        val viewModel = CoinFlipToolViewModel(telemetryRepository = telemetryRepository)

        viewModel.onEvent(CoinFlipToolEvent.Dismiss)

        assertTrue(telemetryRepository.loggedEvents.isEmpty())
    }

    @Test
    fun `each flip is reported as an operation of the tiles screen`() {
        val viewModel = CoinFlipToolViewModel(telemetryRepository = telemetryRepository)

        viewModel.onEvent(CoinFlipToolEvent.Flip)
        viewModel.onEvent(CoinFlipToolEvent.Flip)

        assertEquals(listOf("flipCoin", "flipCoin"), startedActions(viewModelName = "CoinFlipToolViewModel"))
        val start: AnalyticsEvent = telemetryRepository.loggedEvents.first { it.name == "vm_op_start" }
        assertEquals(AnalyticsValue.Str(AppScreenTracking.Screens.TOOLKIT_TILES.name), start.params["screen"])
    }

    @Test
    fun `a Morse message that cannot be sent is not use`() {
        val viewModel = MorseToolViewModel(repository = morseRepository(), telemetryRepository = telemetryRepository)

        viewModel.onEvent(MorseToolEvent.InputChanged(input = "   "))
        viewModel.onEvent(MorseToolEvent.Toggle)
        assertEquals(MorseInputError.Empty, viewModel.state.value.inputError)
        assertTrue(toolUses().isEmpty())

        viewModel.onEvent(MorseToolEvent.InputChanged(input = "HELLO"))
        viewModel.onEvent(MorseToolEvent.Toggle)
        assertEquals(listOf(ToolkitTileIds.MORSE), toolUses())
        assertTrue(viewModel.state.value.playback.isActive)

        viewModel.onEvent(MorseToolEvent.Dismiss)
        assertEquals(listOf("observePlayback", "startMorse", "stopMorse"), startedActions("MorseToolViewModel"))
    }

    @Test
    fun `stopping SOS is not reported as another use`() {
        val sos = SosRepository(morseRepository = morseRepository())
        val viewModel = SosToolViewModel(repository = sos, telemetryRepository = telemetryRepository)

        viewModel.onEvent(SosToolEvent.Toggle)
        assertTrue(viewModel.state.value.isActive)
        viewModel.onEvent(SosToolEvent.Dismiss)
        assertFalse(viewModel.state.value.isActive)

        sos.toggle()
        assertTrue(viewModel.state.value.isActive)
        viewModel.onEvent(SosToolEvent.Toggle)

        assertFalse(viewModel.state.value.isActive)
        assertEquals(listOf(ToolkitTileIds.SOS), toolUses())
    }

    @Test
    fun `a finished reaction round is posted as its score`() {
        var now = 1_000L
        val viewModel = ReactionTestToolViewModel(
            telemetryRepository = telemetryRepository,
            timeProvider = { now },
            signalDelayMs = { 100L },
        )

        viewModel.onEvent(ReactionTestToolEvent.Start)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        now += 215L
        viewModel.onEvent(ReactionTestToolEvent.Tap)

        val score = telemetryRepository.loggedEvents
            .single { it.name == AppGa4Contract.EventName.POST_SCORE }
        assertEquals(AnalyticsValue.LongVal(215L), score.params[AppGa4Contract.Param.SCORE])
        assertEquals(
            AnalyticsValue.Str(ToolkitTileIds.REACTION_TEST),
            score.params[AppGa4Contract.Param.CHARACTER],
        )
        assertEquals(listOf(ToolkitTileIds.REACTION_TEST), toolUses())
    }

    @Test
    fun `a watched tool counts as used only once it has stayed open`() {
        val sensors = SensorRepository(localDataSource = FakeSensorLocalDataSource())
        val scheduler = dispatcherExtension.testDispatcher.scheduler

        val glance = CompassToolViewModel(repository = sensors, telemetryRepository = telemetryRepository)
        glance.onEvent(CompassToolEvent.Open)
        scheduler.advanceTimeBy(ToolUsageTracker.WATCHED_USE_DELAY_MS - 1)
        glance.onEvent(CompassToolEvent.Dismiss)
        scheduler.advanceUntilIdle()
        assertTrue(toolUses().isEmpty())

        glance.onEvent(CompassToolEvent.Open)
        scheduler.advanceTimeBy(ToolUsageTracker.WATCHED_USE_DELAY_MS + 1)
        assertEquals(listOf(ToolkitTileIds.COMPASS), toolUses())
    }

    @Test
    fun `every tool event satisfies the app GA4 contract`() {
        CoinFlipToolViewModel(telemetryRepository = telemetryRepository).onEvent(CoinFlipToolEvent.Flip)
        var now = 0L
        ReactionTestToolViewModel(
            telemetryRepository = telemetryRepository,
            timeProvider = { now },
            signalDelayMs = { 1L },
        ).apply {
            onEvent(ReactionTestToolEvent.Start)
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
            now += 300L
            onEvent(ReactionTestToolEvent.Tap)
        }

        assertTrue(telemetryRepository.loggedEvents.isNotEmpty())
        telemetryRepository.loggedEvents.forEach { it.assertSatisfiesContract() }
    }

    private fun AnalyticsEvent.assertSatisfiesContract() {
        assertTrue(AppGa4ContractValidator.isValidEventName(name), name)
        assertEquals(emptySet(), AppGa4ContractValidator.missingRequiredParams(name, params.keys))
        assertEquals(emptySet(), AppGa4ContractValidator.forbiddenParams(params.keys))
    }
}
