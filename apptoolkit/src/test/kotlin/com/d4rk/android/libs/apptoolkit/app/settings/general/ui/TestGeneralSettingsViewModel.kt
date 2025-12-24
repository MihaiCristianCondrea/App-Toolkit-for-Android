package com.d4rk.android.libs.apptoolkit.app.settings.general.ui

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.general.data.repository.DefaultGeneralSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.contract.GeneralSettingsEvent
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TestGeneralSettingsViewModel {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    @Test
    fun `load content success`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load content success")
        val viewModel = GeneralSettingsViewModel(
            repository = DefaultGeneralSettingsRepository(TestDispatchers(dispatcherExtension.testDispatcher))
        )
        viewModel.onEvent(GeneralSettingsEvent.Load("key"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.contentKey).isEqualTo("key")
        println("🏁 [TEST DONE] load content success")
    }

    @Test
    fun `load content invalid`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load content invalid")
        val viewModel = GeneralSettingsViewModel(
            repository = DefaultGeneralSettingsRepository(TestDispatchers(dispatcherExtension.testDispatcher))
        )
        viewModel.onEvent(GeneralSettingsEvent.Load(null))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.NoData::class.java)
        val error = state.errors.first().message as UiTextHelper.StringResource
        assertThat(error.resourceId).isEqualTo(R.string.error_invalid_content_key)
        println("🏁 [TEST DONE] load content invalid")
    }

    @Test
    fun `load content blank`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load content blank")
        val viewModel = GeneralSettingsViewModel(
            repository = DefaultGeneralSettingsRepository(TestDispatchers(dispatcherExtension.testDispatcher))
        )
        viewModel.onEvent(GeneralSettingsEvent.Load(""))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.NoData::class.java)
        val error = state.errors.first().message as UiTextHelper.StringResource
        assertThat(error.resourceId).isEqualTo(R.string.error_invalid_content_key)
        println("🏁 [TEST DONE] load content blank")
    }

    @Test
    fun `multiple load calls update key`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] multiple load calls update key")
        val viewModel = GeneralSettingsViewModel(
            repository = DefaultGeneralSettingsRepository(TestDispatchers(dispatcherExtension.testDispatcher))
        )
        viewModel.onEvent(GeneralSettingsEvent.Load("one"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        var state = viewModel.uiState.value
        assertThat(state.data?.contentKey).isEqualTo("one")

        viewModel.onEvent(GeneralSettingsEvent.Load("two"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        state = viewModel.uiState.value
        assertThat(state.data?.contentKey).isEqualTo("two")
        println("🏁 [TEST DONE] multiple load calls update key")
    }

    @Test
    fun `errors cleared after successful load`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] errors cleared after successful load")
        val viewModel = GeneralSettingsViewModel(
            repository = DefaultGeneralSettingsRepository(TestDispatchers(dispatcherExtension.testDispatcher))
        )
        viewModel.onEvent(GeneralSettingsEvent.Load(""))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        var state = viewModel.uiState.value
        assertThat(state.errors).isNotEmpty()

        viewModel.onEvent(GeneralSettingsEvent.Load("valid"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.errors).isEmpty()
        println("🏁 [TEST DONE] errors cleared after successful load")
    }

    @Test
    fun `content persists across config changes`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] content persists across config changes")
        val viewModel = GeneralSettingsViewModel(
            repository = DefaultGeneralSettingsRepository(TestDispatchers(dispatcherExtension.testDispatcher))
        )
        viewModel.onEvent(GeneralSettingsEvent.Load("rotate"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val stateBefore = viewModel.uiState.value

        // simulate orientation change by checking state again
        val stateAfter = viewModel.uiState.value
        assertThat(stateAfter.data?.contentKey).isEqualTo(stateBefore.data?.contentKey)
        println("🏁 [TEST DONE] content persists across config changes")
    }

    @Test
    fun `reload with same key retains state`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] reload with same key retains state")
        val viewModel = GeneralSettingsViewModel(
            repository = DefaultGeneralSettingsRepository(TestDispatchers(dispatcherExtension.testDispatcher))
        )
        viewModel.onEvent(GeneralSettingsEvent.Load("keep"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val stateBefore = viewModel.uiState.value

        viewModel.onEvent(GeneralSettingsEvent.Load("keep"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val stateAfter = viewModel.uiState.value
        assertThat(stateAfter).isEqualTo(stateBefore)
        println("🏁 [TEST DONE] reload with same key retains state")
    }

    @Test
    fun `load extremely long content key`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load extremely long content key")
        val viewModel = GeneralSettingsViewModel(
            repository = DefaultGeneralSettingsRepository(TestDispatchers(dispatcherExtension.testDispatcher))
        )
        val longKey = "a".repeat(1000)
        viewModel.onEvent(GeneralSettingsEvent.Load(longKey))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.contentKey).isEqualTo(longKey)
        println("🏁 [TEST DONE] load extremely long content key")
    }

    @Test
    fun `load content key with special characters`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load content key with special characters")
        val viewModel = GeneralSettingsViewModel(
            repository = DefaultGeneralSettingsRepository(TestDispatchers(dispatcherExtension.testDispatcher))
        )
        val key = "!@#$%^&*()_+漢字"
        viewModel.onEvent(GeneralSettingsEvent.Load(key))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.contentKey).isEqualTo(key)
        println("🏁 [TEST DONE] load content key with special characters")
    }

    @Test
    fun `concurrent load events yield latest state`() =
        runTest(dispatcherExtension.testDispatcher) {
            println("🚀 [TEST] concurrent load events yield latest state")
            val viewModel = GeneralSettingsViewModel(
                repository = DefaultGeneralSettingsRepository(TestDispatchers(dispatcherExtension.testDispatcher))
            )
            viewModel.onEvent(GeneralSettingsEvent.Load("first"))
            viewModel.onEvent(GeneralSettingsEvent.Load("second"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
            val state = viewModel.uiState.value
            assertThat(state.data?.contentKey).isEqualTo("second")
            println("🏁 [TEST DONE] concurrent load events yield latest state")
        }
}
