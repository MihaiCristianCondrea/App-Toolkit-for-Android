package com.d4rk.android.libs.apptoolkit.app.permissions.ui

import com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract.PermissionsEvent
import com.d4rk.android.libs.apptoolkit.app.permissions.domain.repository.PermissionsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsCategory
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class TestPermissionsViewModel {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private lateinit var viewModel: PermissionsViewModel
    private lateinit var repository: PermissionsRepository

    private fun setup(config: SettingsConfig? = null, error: Throwable? = null) {
        repository = mockk()
        if (error != null) {
            every { repository.getPermissionsConfig() } returns flow { throw error }
        } else {
            every { repository.getPermissionsConfig() } returns flowOf(config!!)
        }
        viewModel = PermissionsViewModel(repository)
    }

    @Test
    fun `load permissions success`() = runTest(dispatcherExtension.testDispatcher) {
        val config = SettingsConfig(title = "P", categories = listOf(SettingsCategory(title = "c", preferences = emptyList())))
        setup(config = config)

        viewModel.onEvent(PermissionsEvent.Load)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.title).isEqualTo("P")
        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.Success::class.java)
    }

    @Test
    fun `load permissions error`() = runTest(dispatcherExtension.testDispatcher) {
        setup(error = RuntimeException("fail"))

        viewModel.onEvent(PermissionsEvent.Load)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.Error::class.java)
    }

    @Test
    fun `load permissions with empty categories`() = runTest(dispatcherExtension.testDispatcher) {
        val config = SettingsConfig(title = "", categories = emptyList())
        setup(config = config)

        viewModel.onEvent(PermissionsEvent.Load)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.NoData::class.java)
    }
}

