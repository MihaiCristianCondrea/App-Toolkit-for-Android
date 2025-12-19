package com.d4rk.android.libs.apptoolkit.app.settings.settings.ui

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.contract.SettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsCategory
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
@OptIn(ExperimentalCoroutinesApi::class)

class TestSettingsViewModel {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var provider: SettingsProvider

    private fun setup(config: SettingsConfig) {
        provider = mockk()
        every { provider.provideSettingsConfig(any()) } returns config
        val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
        viewModel = SettingsViewModel(provider, dispatchers)
    }

    @Test
    fun `load settings success`() = runTest(dispatcherExtension.testDispatcher) {
        val config = SettingsConfig(title = "Title", categories = listOf(SettingsCategory(title = "c", preferences = emptyList())))
        val context = mockk<Context>(relaxed = true)
        setup(config)

        viewModel.onEvent(SettingsEvent.Load(context))
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.title).isEqualTo("Title")
        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.Success::class.java)
    }

    @Test
    fun `load settings no data`() = runTest(dispatcherExtension.testDispatcher) {
        val config = SettingsConfig(title = "", categories = emptyList())
        val context = mockk<Context>(relaxed = true)
        setup(config)

        viewModel.onEvent(SettingsEvent.Load(context))
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.NoData::class.java)
    }

    @Test
    fun `load settings clears previous errors on success`() = runTest(dispatcherExtension.testDispatcher) {
        val empty = SettingsConfig(title = "", categories = emptyList())
        val valid = SettingsConfig(title = "Title", categories = listOf(SettingsCategory(title = "c", preferences = emptyList())))
        val context = mockk<Context>(relaxed = true)
        provider = mockk()
        every { provider.provideSettingsConfig(any()) } returnsMany listOf(empty, valid)
        val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
        viewModel = SettingsViewModel(provider, dispatchers)

        viewModel.onEvent(SettingsEvent.Load(context))
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.NoData::class.java)
        assertThat(viewModel.uiState.value.errors).isNotEmpty()

        viewModel.onEvent(SettingsEvent.Load(context))
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(viewModel.uiState.value.errors).isEmpty()
    }

    @Test
    fun `provider called once per load event`() = runTest(dispatcherExtension.testDispatcher) {
        val config = SettingsConfig(title = "Title", categories = listOf(SettingsCategory(title = "c", preferences = emptyList())))
        val context = mockk<Context>(relaxed = true)
        provider = mockk()
        every { provider.provideSettingsConfig(any()) } returns config
        val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)
        viewModel = SettingsViewModel(provider, dispatchers)

        viewModel.onEvent(SettingsEvent.Load(context))
        advanceUntilIdle()

        verify(exactly = 1) { provider.provideSettingsConfig(context) }
    }
}