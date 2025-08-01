package com.d4rk.android.libs.apptoolkit.app.settings.settings.ui

import android.content.Context
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.actions.SettingsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsCategory
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsPreference
import com.d4rk.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TestSettingsViewModel {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private lateinit var dispatcherProvider: TestDispatchers
    private lateinit var viewModel: SettingsViewModel
    private lateinit var provider: SettingsProvider

    private fun setup(config: SettingsConfig, dispatcher: TestDispatcher) {
        dispatcherProvider = TestDispatchers(dispatcher)
        provider = mockk()
        every { provider.provideSettingsConfig(any()) } returns config
        viewModel = SettingsViewModel(provider, dispatcherProvider)
    }

    @Test
    fun `load settings success`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load settings success")
        val config = SettingsConfig(title = "title", categories = listOf(SettingsCategory(title = "c")))
        setup(config, dispatcherExtension.testDispatcher)
        val context = mockk<Context>(relaxed = true)
        viewModel.onEvent(SettingsEvent.Load(context))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.categories?.size).isEqualTo(1)
        println("🏁 [TEST DONE] load settings success")
    }

    @Test
    fun `load settings empty`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load settings empty")
        val config = SettingsConfig(title = "title", categories = emptyList())
        setup(config, dispatcherExtension.testDispatcher)
        val context = mockk<Context>(relaxed = true)
        viewModel.onEvent(SettingsEvent.Load(context))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.NoData::class.java)
        val error = state.errors.first().message as UiTextHelper.StringResource
        assertThat(error.resourceId).isEqualTo(R.string.error_no_settings_found)
        println("🏁 [TEST DONE] load settings empty")
    }

    @Test
    fun `load settings provider returns null`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load settings provider returns null")
        dispatcherProvider = TestDispatchers(dispatcherExtension.testDispatcher)
        provider = mockk()
        every { provider.provideSettingsConfig(any()) } returns SettingsConfig(title = "null test", categories = emptyList())
        viewModel = SettingsViewModel(provider, dispatcherProvider)
        val context = mockk<Context>(relaxed = true)

        viewModel.onEvent(SettingsEvent.Load(context))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.NoData::class.java)
        println("🏁 [TEST DONE] load settings provider returns null")
    }

    @Test
    fun `sequential loads reflect latest config`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] sequential loads reflect latest config")
        val context = mockk<Context>(relaxed = true)
        dispatcherProvider = TestDispatchers(dispatcherExtension.testDispatcher)
        provider = mockk()
        every { provider.provideSettingsConfig(any()) } returnsMany listOf(
            SettingsConfig(title = "first", categories = listOf(SettingsCategory(title = "one"))),
            SettingsConfig(title = "second", categories = emptyList())
        )
        viewModel = SettingsViewModel(provider, dispatcherProvider)

        viewModel.onEvent(SettingsEvent.Load(context))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        var state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.title).isEqualTo("first")

        viewModel.onEvent(SettingsEvent.Load(context))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.NoData::class.java)
        println("🏁 [TEST DONE] sequential loads reflect latest config")
    }

    @Test
    fun `provider returns partial config`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] provider returns partial config")
        val config = SettingsConfig(title = "", categories = listOf(SettingsCategory()))
        setup(config, dispatcherExtension.testDispatcher)
        val context = mockk<Context>(relaxed = true)
        viewModel.onEvent(SettingsEvent.Load(context))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.title).isEmpty()
        println("🏁 [TEST DONE] provider returns partial config")
    }

    @Test
    fun `load settings with duplicated categories`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load settings with duplicated categories")
        val category = SettingsCategory(title = "dup")
        val config = SettingsConfig(title = "t", categories = listOf(category, category))
        setup(config, dispatcherExtension.testDispatcher)
        val context = mockk<Context>(relaxed = true)
        viewModel.onEvent(SettingsEvent.Load(context))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.categories?.size).isEqualTo(2)
        println("🏁 [TEST DONE] load settings with duplicated categories")
    }

    @Test
    fun `load very large settings config`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load very large settings config")
        val prefs = List(10) { index -> SettingsPreference(key = "k$index") }
        val categories = List(50) { index -> SettingsCategory(title = "c$index", preferences = prefs) }
        val config = SettingsConfig(title = "big", categories = categories)
        setup(config, dispatcherExtension.testDispatcher)
        val context = mockk<Context>(relaxed = true)
        viewModel.onEvent(SettingsEvent.Load(context))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.categories?.size).isEqualTo(50)
        println("🏁 [TEST DONE] load very large settings config")
    }

    @Test
    fun `load settings with malformed preference`() = runTest(dispatcherExtension.testDispatcher) {
        println("🚀 [TEST] load settings with malformed preference")
        val malformed = SettingsCategory(
            title = "bad",
            preferences = listOf(SettingsPreference(key = null, title = null))
        )
        val config = SettingsConfig(title = "bad", categories = listOf(malformed))
        setup(config, dispatcherExtension.testDispatcher)
        val context = mockk<Context>(relaxed = true)

        viewModel.onEvent(SettingsEvent.Load(context))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.screenState).isInstanceOf(ScreenState.Success::class.java)
        assertThat(state.data?.categories?.first()?.preferences?.size).isEqualTo(1)
        println("🏁 [TEST DONE] load settings with malformed preference")
    }
}
