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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionRating
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ReactionTestPhase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class ReactionTestToolViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private var simulatedTimeMs: Long = 1000L

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = ReactionTestToolViewModel(timeProvider = { simulatedTimeMs })

    @Test
    fun `initial state is Idle with no statistics`() {
        val viewModel = createViewModel()
        val state = viewModel.state.value

        assertEquals(ReactionTestPhase.Idle, state.phase)
        assertNull(state.lastReactionTimeMs)
        assertNull(state.bestTimeMs)
        assertNull(state.averageTimeMs)
        assertEquals(0, state.history.size)
    }

    @Test
    fun `starting test enters Waiting phase`() {
        val viewModel = createViewModel()

        viewModel.startTest(delayMs = 2000L)
        assertEquals(ReactionTestPhase.Waiting, viewModel.state.value.phase)
    }

    @Test
    fun `tapping during Waiting phase causes FalseStart`() {
        val viewModel = createViewModel()

        viewModel.startTest(delayMs = 2000L)
        viewModel.handleTap()

        assertEquals(ReactionTestPhase.FalseStart, viewModel.state.value.phase)
    }

    @Test
    fun `signal triggers after delay and calculates reaction time on tap`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        simulatedTimeMs = 1000L
        viewModel.startTest(delayMs = 2000L)

        // Advance coroutine delay
        advanceTimeBy(2000L.milliseconds)
        simulatedTimeMs = 3000L // Signal time recorded at 3000L
        testScheduler.advanceUntilIdle()

        assertEquals(ReactionTestPhase.Signal, viewModel.state.value.phase)

        // User taps 180 ms later
        simulatedTimeMs = 3180L
        viewModel.handleTap()

        val state = viewModel.state.value
        assertEquals(ReactionTestPhase.Result, state.phase)
        assertEquals(180L, state.lastReactionTimeMs)
        assertEquals(180L, state.bestTimeMs)
        assertEquals(180L, state.averageTimeMs)
        assertEquals(ReactionRating.Lightning, state.rating)
        assertEquals(1, state.roundCount)
    }

    @Test
    fun `multiple rounds calculate best score and rolling average`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        // Round 1: 300 ms
        simulatedTimeMs = 1000L
        viewModel.startTest(delayMs = 1000L)
        advanceTimeBy(1000L.milliseconds)
        simulatedTimeMs = 2000L
        testScheduler.advanceUntilIdle()
        simulatedTimeMs = 2300L
        viewModel.handleTap()

        assertEquals(300L, viewModel.state.value.lastReactionTimeMs)
        assertEquals(ReactionRating.Average, viewModel.state.value.rating)

        // Round 2: 200 ms
        simulatedTimeMs = 3000L
        viewModel.startTest(delayMs = 1000L)
        advanceTimeBy(1000L.milliseconds)
        simulatedTimeMs = 4000L
        testScheduler.advanceUntilIdle()
        simulatedTimeMs = 4200L
        viewModel.handleTap()

        val state = viewModel.state.value
        assertEquals(200L, state.lastReactionTimeMs)
        assertEquals(200L, state.bestTimeMs)
        assertEquals(250L, state.averageTimeMs) // (300 + 200) / 2
        assertEquals(2, state.history.size)
        assertEquals(2, state.roundCount)
    }

    @Test
    fun `reset session clears history and statistics`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        simulatedTimeMs = 1000L
        viewModel.startTest(delayMs = 1000L)
        advanceTimeBy(1000L.milliseconds)
        simulatedTimeMs = 2000L
        testScheduler.advanceUntilIdle()
        simulatedTimeMs = 2200L
        viewModel.handleTap()

        viewModel.resetSession()

        val state = viewModel.state.value
        assertEquals(ReactionTestPhase.Idle, state.phase)
        assertNull(state.bestTimeMs)
        assertNull(state.averageTimeMs)
        assertEquals(0, state.history.size)
    }
}
