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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `uiState emits provided initial state`() = runTest(dispatcherExtension.testDispatcher) {
        val initialState = TestState(message = "initial")
        val viewModel = TestViewModel(initialState = initialState)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(initialState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sendAction emits into actionEvent flow`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = TestViewModel()
        val action = TestAction.ShowMessage("hello")

        viewModel.actionEvent.test {
            viewModel.emitAction(action)
            assertThat(awaitItem()).isEqualTo(action)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * The case that left a startup screen loading forever: the screen asked its ViewModel for work
     * in onCreate and only began collecting a moment later, and the answer went nowhere.
     */
    @Test
    fun `actions sent before anything collects are still delivered`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = TestViewModel()
            val first = TestAction.ShowMessage("first")
            val second = TestAction.ShowMessage("second")

            viewModel.emitAction(first)
            viewModel.emitAction(second)

            viewModel.actionEvent.test {
                assertThat(awaitItem()).isEqualTo(first)
                assertThat(awaitItem()).isEqualTo(second)
                cancelAndIgnoreRemainingEvents()
            }
        }

    /**
     * A screen and the Activity hosting it watch the same ViewModel and act on different parts of
     * what it emits. Handing an action to whichever of them happened to be listening first left the
     * onboarding Done button doing nothing whenever the Activity won that race.
     */
    @Test
    fun `every collector sees every action`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = TestViewModel()
        val action = TestAction.ShowMessage("both")
        val first = mutableListOf<TestAction>()
        val second = mutableListOf<TestAction>()

        val firstJob = launch { viewModel.actionEvent.collect { first += it } }
        val secondJob = launch { viewModel.actionEvent.collect { second += it } }
        runCurrent()

        viewModel.emitAction(action)
        runCurrent()

        assertThat(first).containsExactly(action)
        assertThat(second).containsExactly(action)

        firstJob.cancel()
        secondJob.cancel()
    }

    @Test
    fun `onEvent updates state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = TestViewModel()
        val newMessage = "updated"

        viewModel.onEvent(TestEvent.SetMessage(newMessage))

        assertThat(viewModel.uiState.value).isEqualTo(TestState(message = newMessage))
    }
}

private data class TestState(val message: String) : UiState

private sealed interface TestEvent : UiEvent {
    data class SetMessage(val message: String) : TestEvent
}

private sealed interface TestAction : ActionEvent {
    data class ShowMessage(val message: String) : TestAction
}

private class TestViewModel(initialState: TestState = TestState(message = "idle")) :
    BaseViewModel<TestState, TestEvent, TestAction>(initialState) {

    override fun onEvent(event: TestEvent) {
        when (event) {
            is TestEvent.SetMessage -> uiStateFlow.value =
                currentState.copy(message = event.message)
        }
    }

    fun emitAction(action: TestAction) {
        sendAction(action)
    }
}
