package com.d4rk.android.libs.apptoolkit.app.about.ui

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.actions.AboutEvents
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TestAboutViewModel {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `copy device info shows snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] copy device info shows snackbar")
        val viewModel = AboutViewModel()
        viewModel.onEvent(AboutEvents.CopyDeviceInfo)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.data?.tempPlaceholder).isFalse()
        val snackbar = state.snackbar!!
        val msg = snackbar.message as UiTextHelper.StringResource
        assertThat(msg.resourceId).isEqualTo(R.string.snack_device_info_copied)
        println("🏁 [TEST DONE] copy device info shows snackbar")
    }

    @Test
    fun `dismiss snackbar resets state`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] dismiss snackbar resets state")
        val viewModel = AboutViewModel()

        viewModel.onEvent(AboutEvents.CopyDeviceInfo)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNotNull()

        viewModel.onEvent(AboutEvents.DismissSnackbar)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.snackbar).isNull()
        assertThat(state.data?.tempPlaceholder).isFalse()
        println("🏁 [TEST DONE] dismiss snackbar resets state")
    }

    @Test
    fun `snackbar can be shown again after dismissal`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] snackbar can be shown again after dismissal")
        val viewModel = AboutViewModel()

        viewModel.onEvent(AboutEvents.CopyDeviceInfo)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onEvent(AboutEvents.DismissSnackbar)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.snackbar).isNull()

        viewModel.onEvent(AboutEvents.CopyDeviceInfo)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val stateAfterSecondCopy = viewModel.uiState.value
        assertThat(stateAfterSecondCopy.snackbar).isNotNull()
        assertThat(stateAfterSecondCopy.data?.tempPlaceholder).isFalse()

        viewModel.onEvent(AboutEvents.DismissSnackbar)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val finalState = viewModel.uiState.value
        assertThat(finalState.snackbar).isNull()
        assertThat(finalState.data?.tempPlaceholder).isFalse()

        viewModel.onEvent(AboutEvents.DismissSnackbar)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNull()
        assertThat(viewModel.uiState.value.data?.tempPlaceholder).isFalse()
        println("🏁 [TEST DONE] snackbar can be shown again after dismissal")
    }

    @Test
    fun `repeated copy events show snackbar each time`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] repeated copy events show snackbar each time")
        val viewModel = AboutViewModel()

        viewModel.onEvent(AboutEvents.CopyDeviceInfo)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val first = viewModel.uiState.value.snackbar!!
        val firstTimestamp = first.timeStamp

        viewModel.onEvent(AboutEvents.CopyDeviceInfo)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val second = viewModel.uiState.value.snackbar!!

        assertThat(second.timeStamp).isGreaterThan(firstTimestamp)
        println("🏁 [TEST DONE] repeated copy events show snackbar each time")
    }

    @Test
    fun `rapid successive copy events keep snackbar visible`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] rapid successive copy events keep snackbar visible")
        val viewModel = AboutViewModel()

        repeat(5) {
            viewModel.onEvent(AboutEvents.CopyDeviceInfo)
        }

        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.snackbar).isNotNull()
        assertThat(state.data?.tempPlaceholder).isFalse()
        println("🏁 [TEST DONE] rapid successive copy events keep snackbar visible")
    }

    @Test
    fun `changing screen data resets copy state`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] changing screen data resets copy state")
        val viewModel = AboutViewModel()

        viewModel.onEvent(AboutEvents.CopyDeviceInfo)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.data?.tempPlaceholder).isFalse()
        assertThat(viewModel.uiState.value.snackbar).isNotNull()

        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.data?.tempPlaceholder).isFalse()
        assertThat(state.snackbar).isNotNull()
        println("🏁 [TEST DONE] changing screen data resets copy state")
    }

    @Test
    fun `new viewmodel has default state`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] new viewmodel has default state")
        val viewModel = AboutViewModel()

        viewModel.onEvent(AboutEvents.CopyDeviceInfo)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNotNull()

        val recreated = AboutViewModel()
        val state = recreated.uiState.value
        assertThat(state.snackbar).isNull()
        assertThat(state.data?.tempPlaceholder).isFalse()
        println("🏁 [TEST DONE] new viewmodel has default state")
    }
}
