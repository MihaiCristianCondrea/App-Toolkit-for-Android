package com.d4rk.android.libs.apptoolkit.app.about.ui

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.CopyDeviceInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.ObserveAboutInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.ui.contract.AboutEvent
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.UiTextHelper
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class TestAboutViewModel {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private val deviceProvider = object : AboutSettingsProvider {
        override val deviceInfo: String = "device-info"
    }

    private val buildInfoProvider = object : BuildInfoProvider {
        override val appVersion: String = "1.0"
        override val appVersionCode: Int = 1
        override val packageName: String = "pkg"
        override val isDebugBuild: Boolean = false
    }

    private fun createViewModel(): AboutViewModel {
        val repository = object : AboutRepository {
            override fun getAboutInfoStream(): Flow<AboutUiState> =
                flow {
                    emit(
                        AboutUiState(
                            appVersion = buildInfoProvider.appVersion,
                            appVersionCode = buildInfoProvider.appVersionCode,
                            deviceInfo = deviceProvider.deviceInfo,
                        ),
                    )
                }

            override suspend fun copyDeviceInfo(label: String, deviceInfo: String) { /* no-op */
            }
        }
        return AboutViewModel(
            observeAboutInfo = ObserveAboutInfoUseCase(repository),
            copyDeviceInfo = CopyDeviceInfoUseCase(repository),
        )
    }

    private fun createFailingViewModel(): AboutViewModel {
        val repository = object : AboutRepository {
            override fun getAboutInfoStream(): Flow<AboutUiState> =
                flow { throw Exception("fail") }

            override suspend fun copyDeviceInfo(label: String, deviceInfo: String) { /* no-op */
            }
        }
        return AboutViewModel(
            observeAboutInfo = ObserveAboutInfoUseCase(repository),
            copyDeviceInfo = CopyDeviceInfoUseCase(repository),
        )
    }

    @Test
    fun `copy device info shows snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.data?.deviceInfo).isEqualTo(deviceProvider.deviceInfo)
        val snackbar = state.snackbar!!
        val msg = snackbar.message as UiTextHelper.StringResource
        assertThat(msg.resourceId).isEqualTo(R.string.snack_device_info_copied)
    }

    @Test
    fun `dismiss snackbar resets state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNotNull()

        viewModel.onEvent(AboutEvent.DismissSnackbar)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNull()
    }

    @Test
    fun `snackbar can be shown again after dismissal`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
            viewModel.onEvent(AboutEvent.DismissSnackbar)
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
            assertThat(viewModel.uiState.value.snackbar).isNull()

            viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
            assertThat(viewModel.uiState.value.snackbar).isNotNull()
        }

    @Test
    fun `repeated copy events show snackbar each time`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
            val first = viewModel.uiState.value.snackbar!!.timeStamp

            viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
            val second = viewModel.uiState.value.snackbar!!.timeStamp

            assertThat(second).isGreaterThan(first)
        }

    @Test
    fun `rapid successive copy events keep snackbar visible`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel()
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            repeat(5) { viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label")) }
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.uiState.value.snackbar).isNotNull()
        }

    @Test
    fun `repository updates propagate to ui state`() = runTest(dispatcherExtension.testDispatcher) {
        val updates = MutableSharedFlow<AboutUiState>()
        val repository = object : AboutRepository {
            override fun getAboutInfoStream(): Flow<AboutUiState> = updates
            override suspend fun copyDeviceInfo(label: String, deviceInfo: String) { /* no-op */
            }
        }
        val viewModel = AboutViewModel(
            observeAboutInfo = ObserveAboutInfoUseCase(repository),
            copyDeviceInfo = CopyDeviceInfoUseCase(repository),
        )
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        updates.emit(AboutUiState(deviceInfo = "one"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.data?.deviceInfo).isEqualTo("one")

        updates.emit(AboutUiState(deviceInfo = "two"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.data?.deviceInfo).isEqualTo("two")
    }

    @Test
    fun `repository error shows snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createFailingViewModel()
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val snackbar = viewModel.uiState.value.snackbar!!
        val msg = snackbar.message as UiTextHelper.StringResource
        assertThat(msg.resourceId).isEqualTo(R.string.snack_device_info_failed)
    }

    @Test
    fun `new viewmodel has default state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel()
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNotNull()

        val recreated = createViewModel()
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val state = recreated.uiState.value
        assertThat(state.snackbar).isNull()
        assertThat(state.data?.deviceInfo).isEqualTo(deviceProvider.deviceInfo)
    }
}
