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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class LoggedScreenViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val telemetryRepository = FakeTelemetryRepository()

    private fun advance() = dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

    private fun eventNames(): List<String> = telemetryRepository.loggedEvents.map { it.name }

    @Test
    fun `a one-shot operation logs its start and runs`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = TestViewModel(telemetryRepository)

        viewModel.load { "loaded" }
        advance()

        assertThat(viewModel.state.value).isEqualTo("loaded")
        val start = telemetryRepository.loggedEvents.single { it.name == "vm_op_start" }
        assertThat(start.params["action"]).isEqualTo(AnalyticsValue.Str("load"))
        assertThat(eventNames()).doesNotContain("vm_op_error")
    }

    @Test
    fun `a failed one-shot operation is reported and handed to onError`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = TestViewModel(telemetryRepository)
            val failure = IllegalStateException("fail")

            viewModel.load { throw failure }
            advance()

            assertThat(viewModel.handledError).isSameInstanceAs(failure)
            assertThat(eventNames()).contains("vm_op_error")
        }

    @Test
    fun `a cancelled operation is not reported`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = TestViewModel(telemetryRepository)
        val never = CompletableDeferred<String>()

        val job = viewModel.load { never.await() }
        advance()
        job.cancel()
        advance()

        assertThat(viewModel.handledError).isNull()
        assertThat(eventNames()).doesNotContain("vm_op_error")
    }

    @Test
    fun `a collected flow passes every value on`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = TestViewModel(telemetryRepository)

        viewModel.observe(flowOf("first", "second"))
        advance()

        assertThat(viewModel.state.value).isEqualTo("second")
        assertThat(eventNames()).contains("vm_op_start")
    }

    @Test
    fun `a failed flow is reported and handed to onError after the values before it`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = TestViewModel(telemetryRepository)
            val failure = IllegalStateException("fail")

            viewModel.observe(
                flow {
                    emit("first")
                    throw failure
                }
            )
            advance()

            assertThat(viewModel.state.value).isEqualTo("first")
            assertThat(viewModel.handledError).isSameInstanceAs(failure)
            assertThat(eventNames()).contains("vm_op_error")
        }

    private class TestViewModel(telemetryRepository: FakeTelemetryRepository) :
        LoggedScreenViewModel<String, Unit>(
            initialState = "",
            telemetryRepository = telemetryRepository,
            screenName = "Test",
        ) {
        var handledError: Throwable? = null

        override fun handleEvent(event: Unit) = Unit

        fun load(block: suspend () -> String): Job =
            launchReport(action = "load", onError = { handledError = it }) {
                val value = block()
                setState { value }
            }

        fun observe(values: Flow<String>): Job =
            values.collectReport(action = "observe", onError = { handledError = it }) { value ->
                setState { value }
            }
    }
}
