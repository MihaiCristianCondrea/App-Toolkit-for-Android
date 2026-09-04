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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.startup

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.startup.contracts.StartupAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.startup.contracts.StartupEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeFirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class StartupViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `consent event updates state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = StartupViewModel(firebaseController = FakeFirebaseController())

        viewModel.onEvent(StartupEvent.ConsentFormLoaded)

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.consentFormLoaded).isTrue()
    }

    @Test
    fun `continue event emits navigation action`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = StartupViewModel(firebaseController = FakeFirebaseController())
        val actions = mutableListOf<StartupAction>()
        val job = launch { viewModel.actionEvent.collect { actions.add(it) } }

        viewModel.onEvent(StartupEvent.Continue)
        advanceUntilIdle()

        assertThat(actions).containsExactly(StartupAction.NavigateNext)
        job.cancel()
    }

    /**
     * The startup screen hides its only button while loading, so a consent round trip that never
     * reports back has to stop mattering at some point.
     */
    @Test
    fun `screen stops loading when consent never reports back`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = StartupViewModel(firebaseController = FakeFirebaseController())
            val job = launch { viewModel.actionEvent.collect { } }

            viewModel.onEvent(StartupEvent.RequestConsent)
            advanceTimeBy(1.seconds)

            assertThat(viewModel.uiState.value.screenState)
                .isInstanceOf(ScreenState.IsLoading::class.java)

            advanceUntilIdle()

            assertThat(viewModel.uiState.value.screenState)
                .isInstanceOf(ScreenState.Success::class.java)
            assertThat(viewModel.uiState.value.data?.consentFormLoaded).isTrue()
            job.cancel()
        }

    @Test
    fun `request consent emits ui action`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = StartupViewModel(firebaseController = FakeFirebaseController())
        val actions = mutableListOf<StartupAction>()
        val job = launch { viewModel.actionEvent.collect { actions.add(it) } }

        viewModel.onEvent(StartupEvent.RequestConsent)
        advanceUntilIdle()

        assertThat(actions).containsExactly(StartupAction.RequestConsentUi)
        job.cancel()
    }
}
