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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ReactionTestToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionRating
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionTestPhase
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class ReactionTestToolViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()
    private var simulatedTimeMs: Long = 1_000L
    private var signalDelayMs: Long = 2_000L

    private fun createViewModel() = ReactionTestToolViewModel(
        telemetryRepository = telemetryRepository,
        timeProvider = { simulatedTimeMs },
        signalDelayMs = { signalDelayMs },
    )

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    /** Runs one round whose signal shows at [signalAtMs] and is tapped [reactionMs] later. */
    private fun ReactionTestToolViewModel.playRound(signalAtMs: Long, reactionMs: Long) {
        onEvent(ReactionTestToolEvent.Start)
        simulatedTimeMs = signalAtMs
        advance()
        simulatedTimeMs = signalAtMs + reactionMs
        onEvent(ReactionTestToolEvent.Tap)
    }

    @Test
    fun `initial state is Idle with no statistics`() {
        val state = createViewModel().state.value

        assertEquals(ReactionTestPhase.Idle, state.phase)
        assertNull(state.lastReactionTimeMs)
        assertNull(state.bestTimeMs)
        assertNull(state.averageTimeMs)
        assertEquals(0, state.history.size)
    }

    @Test
    fun `starting test enters Waiting phase`() {
        val viewModel = createViewModel()

        viewModel.onEvent(ReactionTestToolEvent.Start)

        assertEquals(ReactionTestPhase.Waiting, viewModel.state.value.phase)
    }

    @Test
    fun `tapping during Waiting phase causes FalseStart`() {
        val viewModel = createViewModel()

        viewModel.onEvent(ReactionTestToolEvent.Start)
        viewModel.onEvent(ReactionTestToolEvent.Tap)
        advance()

        assertEquals(ReactionTestPhase.FalseStart, viewModel.state.value.phase)
    }

    @Test
    fun `signal triggers after delay and calculates reaction time on tap`() {
        val viewModel = createViewModel()

        viewModel.onEvent(ReactionTestToolEvent.Start)
        dispatcherExtension.testDispatcher.scheduler.advanceTimeBy(signalDelayMs - 1)
        assertEquals(ReactionTestPhase.Waiting, viewModel.state.value.phase)

        simulatedTimeMs = 3_000L
        advance()
        assertEquals(ReactionTestPhase.Signal, viewModel.state.value.phase)

        simulatedTimeMs = 3_180L
        viewModel.onEvent(ReactionTestToolEvent.Tap)

        val state = viewModel.state.value
        assertEquals(ReactionTestPhase.Result, state.phase)
        assertEquals(180L, state.lastReactionTimeMs)
        assertEquals(180L, state.bestTimeMs)
        assertEquals(180L, state.averageTimeMs)
        assertEquals(ReactionRating.Lightning, state.rating)
        assertEquals(1, state.roundCount)
    }

    @Test
    fun `multiple rounds calculate best score and rolling average`() {
        val viewModel = createViewModel()
        signalDelayMs = 1_000L

        viewModel.playRound(signalAtMs = 2_000L, reactionMs = 300L)
        assertEquals(300L, viewModel.state.value.lastReactionTimeMs)
        assertEquals(ReactionRating.Average, viewModel.state.value.rating)

        viewModel.playRound(signalAtMs = 4_000L, reactionMs = 200L)
        val state = viewModel.state.value
        assertEquals(200L, state.lastReactionTimeMs)
        assertEquals(200L, state.bestTimeMs)
        assertEquals(250L, state.averageTimeMs)
        assertEquals(2, state.history.size)
        assertEquals(2, state.roundCount)
    }

    @Test
    fun `reset session clears history and statistics`() {
        val viewModel = createViewModel()
        signalDelayMs = 1_000L
        viewModel.playRound(signalAtMs = 2_000L, reactionMs = 200L)

        viewModel.onEvent(ReactionTestToolEvent.Reset)

        val state = viewModel.state.value
        assertEquals(ReactionTestPhase.Idle, state.phase)
        assertNull(state.bestTimeMs)
        assertNull(state.averageTimeMs)
        assertEquals(0, state.history.size)
    }

    @Test
    fun `closing the sheet while waiting cancels the round and keeps the results`() {
        val viewModel = createViewModel()
        signalDelayMs = 1_000L
        viewModel.playRound(signalAtMs = 2_000L, reactionMs = 220L)
        viewModel.onEvent(ReactionTestToolEvent.Start)

        viewModel.onEvent(ReactionTestToolEvent.Dismiss)
        advance()

        val state = viewModel.state.value
        assertEquals(ReactionTestPhase.Idle, state.phase)
        assertEquals(220L, state.bestTimeMs)
    }
}
