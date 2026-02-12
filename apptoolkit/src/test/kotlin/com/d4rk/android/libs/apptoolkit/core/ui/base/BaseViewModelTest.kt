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

package com.d4rk.android.libs.apptoolkit.core.ui.base

import app.cash.turbine.test
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiState
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
