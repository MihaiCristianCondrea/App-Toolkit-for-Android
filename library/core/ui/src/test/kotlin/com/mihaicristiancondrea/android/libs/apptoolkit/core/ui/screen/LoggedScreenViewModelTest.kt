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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.collectInBackground
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
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

        assertEquals("loaded", viewModel.state.value)
        val start = telemetryRepository.loggedEvents.single { it.name == "vm_op_start" }
        assertEquals(AnalyticsValue.Str("load"), start.params["action"])
        assertFalse("vm_op_error" in eventNames())
    }

    @Test
    fun `a failed one-shot operation is reported and handed to onError`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = TestViewModel(telemetryRepository)
            val failure = IllegalStateException("fail")

            viewModel.load { throw failure }
            advance()

            assertSame(failure, viewModel.handledError)
            assertContains(eventNames(), "vm_op_error")
        }

    @Test
    fun `a cancelled operation is not reported`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = TestViewModel(telemetryRepository)
        val never = CompletableDeferred<String>()

        val job = viewModel.load { never.await() }
        advance()
        job.cancel()
        advance()

        assertNull(viewModel.handledError)
        assertFalse("vm_op_error" in eventNames())
    }

    @Test
    fun `a collected flow passes every value on`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = TestViewModel(telemetryRepository)

        viewModel.observe(flowOf("first", "second"))
        advance()

        assertEquals("second", viewModel.state.value)
        assertContains(eventNames(), "vm_op_start")
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

            assertEquals("first", viewModel.state.value)
            assertSame(failure, viewModel.handledError)
            assertContains(eventNames(), "vm_op_error")
        }

    @Test
    fun `an observed stream waits for the screen to collect state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = TestViewModel(telemetryRepository)
        val stream = CountingStream()

        viewModel.follow(stream.flow)
        advance()
        assertEquals(0, stream.collections)

        collectInBackground(viewModel.state)
        advance()

        assertEquals(1, stream.collections)
        assertEquals("value 1", viewModel.state.value)
    }

    @Test
    fun `an observed stream keeps running through a short absence such as a rotation`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = TestViewModel(telemetryRepository)
            val stream = CountingStream()
            viewModel.follow(stream.flow)

            val first = collectInBackground(viewModel.state)
            advance()
            first.cancel()
            advanceTimeBy(ScreenViewModel.STOP_TIMEOUT_MILLIS - 1)
            collectInBackground(viewModel.state)
            advance()

            assertEquals(1, stream.collections)
            assertEquals(0, stream.stopped)
        }

    @Test
    fun `an observed stream stops once the screen has been gone for the timeout`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = TestViewModel(telemetryRepository)
            val stream = CountingStream()
            viewModel.follow(stream.flow)

            val first = collectInBackground(viewModel.state)
            advance()
            first.cancel()
            advanceTimeBy(ScreenViewModel.STOP_TIMEOUT_MILLIS + 1)

            assertEquals(1, stream.stopped)

            collectInBackground(viewModel.state)
            advance()

            assertEquals(2, stream.collections)
            assertEquals(2, eventNames().count { it == "vm_op_start" })
        }

    @Test
    fun `a failed observed stream is reported, and a restart collects it again`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = TestViewModel(telemetryRepository)
            val failure = IllegalStateException("fail")
            var attempts = 0
            val stream = flow {
                attempts++
                if (attempts == 1) throw failure
                emit("recovered")
            }
            collectInBackground(viewModel.state)

            viewModel.follow(stream)
            advance()
            assertSame(failure, viewModel.handledError)
            assertContains(eventNames(), "vm_op_error")

            viewModel.follow(stream)
            advance()
            assertEquals("recovered", viewModel.state.value)
        }

    private class CountingStream {
        var collections: Int = 0
            private set
        var stopped: Int = 0
            private set

        val flow: Flow<String> = flow {
            collections++
            try {
                emit("value $collections")
                awaitCancellation()
            } finally {
                stopped++
            }
        }
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

        private var followJob: Job? = null

        fun follow(values: Flow<String>) {
            followJob = followJob.restart {
                values.observeReport(action = "follow", onError = { handledError = it }) { value ->
                    setState { value }
                }
            }
        }
    }
}
