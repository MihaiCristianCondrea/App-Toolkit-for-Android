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

package com.d4rk.android.libs.apptoolkit.app.about.ui

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.CopyDeviceInfoResult
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.CopyDeviceInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.GetAboutInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.ui.contract.AboutEvent
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.utils.FakeFirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.TestDispatcher
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
    private val firebaseController = FakeFirebaseController()

    private fun createViewModel(
        testDispatcher: TestDispatcher = dispatcherExtension.testDispatcher,
        repository: AboutRepository = object : AboutRepository {
            override suspend fun getAboutInfo(): AboutInfo = AboutInfo(
                appVersion = buildInfoProvider.appVersion,
                appVersionCode = buildInfoProvider.appVersionCode,
                deviceInfo = deviceProvider.deviceInfo,
            )

            override fun copyDeviceInfo(label: String, deviceInfo: String): CopyDeviceInfoResult =
                CopyDeviceInfoResult(copied = true, shouldShowFeedback = true)
        }
    ): AboutViewModel {
        val testDispatchers: DispatcherProvider = TestDispatchers(testDispatcher)

        return AboutViewModel(
            getAboutInfo = GetAboutInfoUseCase(repository, firebaseController),
            copyDeviceInfo = CopyDeviceInfoUseCase(repository, firebaseController),
            dispatchers = testDispatchers,
            firebaseController = firebaseController,
        )
    }

    private fun createFailingViewModel(
        testDispatcher: TestDispatcher = dispatcherExtension.testDispatcher,
    ): AboutViewModel {
        val repository = object : AboutRepository {
            override suspend fun getAboutInfo(): AboutInfo = throw Exception("fail")

            override fun copyDeviceInfo(label: String, deviceInfo: String): CopyDeviceInfoResult =
                CopyDeviceInfoResult(copied = false, shouldShowFeedback = true)
        }
        return createViewModel(testDispatcher = testDispatcher, repository = repository)
    }

    @Test
    fun `initial load populates ui state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(testDispatcher = dispatcherExtension.testDispatcher)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.data?.deviceInfo).isEqualTo(deviceProvider.deviceInfo)
        assertThat(state.data?.appVersionInfo?.versionName).isEqualTo(buildInfoProvider.appVersion)
        assertThat(state.data?.appVersionInfo?.versionCode)
            .isEqualTo(buildInfoProvider.appVersionCode.toLong())
    }

    @Test
    fun `copy device info shows snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(testDispatcher = dispatcherExtension.testDispatcher)
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
    fun `copy device info failure surfaces fallback snackbar`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = object : AboutRepository {
                override suspend fun getAboutInfo(): AboutInfo = AboutInfo(
                    appVersion = buildInfoProvider.appVersion,
                    appVersionCode = buildInfoProvider.appVersionCode,
                    deviceInfo = deviceProvider.deviceInfo,
                )

                override fun copyDeviceInfo(
                    label: String,
                    deviceInfo: String,
                ): CopyDeviceInfoResult = CopyDeviceInfoResult(
                    copied = false,
                    shouldShowFeedback = true
                )
            }

            val viewModel = createViewModel(
                testDispatcher = dispatcherExtension.testDispatcher,
                repository = repository
            )
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            val snackbar = viewModel.uiState.value.snackbar!!
            val msg = snackbar.message as UiTextHelper.StringResource
            assertThat(msg.resourceId).isEqualTo(R.string.snack_device_info_failed)
        }

    @Test
    fun `dismiss snackbar resets state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(testDispatcher = dispatcherExtension.testDispatcher)
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
            val viewModel = createViewModel(testDispatcher = dispatcherExtension.testDispatcher)
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
    fun `no fallback means no success snackbar`() =
        runTest(dispatcherExtension.testDispatcher) {
            val repository = object : AboutRepository {
                override suspend fun getAboutInfo(): AboutInfo = AboutInfo(
                    appVersion = buildInfoProvider.appVersion,
                    appVersionCode = buildInfoProvider.appVersionCode,
                    deviceInfo = deviceProvider.deviceInfo,
                )

                override fun copyDeviceInfo(
                    label: String,
                    deviceInfo: String
                ): CopyDeviceInfoResult =
                    CopyDeviceInfoResult(copied = true, shouldShowFeedback = false)
            }

            val viewModel = createViewModel(
                testDispatcher = dispatcherExtension.testDispatcher,
                repository = repository
            )
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.uiState.value.snackbar).isNull()
        }

    @Test
    fun `repeated copy events replace snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(testDispatcher = dispatcherExtension.testDispatcher)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val first = viewModel.uiState.value.snackbar!!.timeStamp

        viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        val second = viewModel.uiState.value.snackbar!!.timeStamp

        assertThat(second).isNotEqualTo(first)
    }

    @Test
    fun `rapid successive copy events keep snackbar visible`() =
        runTest(dispatcherExtension.testDispatcher) {
            val viewModel = createViewModel(testDispatcher = dispatcherExtension.testDispatcher)
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            repeat(5) { viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label")) }
            dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

            assertThat(viewModel.uiState.value.snackbar).isNotNull()
        }

    @Test
    fun `repository error shows snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createFailingViewModel(testDispatcher = dispatcherExtension.testDispatcher)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val snackbar = viewModel.uiState.value.snackbar!!
        val msg = snackbar.message as UiTextHelper.StringResource
        assertThat(msg.resourceId).isEqualTo(R.string.snack_device_info_failed)
    }

    @Test
    fun `new viewmodel has default state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(testDispatcher = dispatcherExtension.testDispatcher)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AboutEvent.CopyDeviceInfo(label = "label"))
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.snackbar).isNotNull()

        val recreated = createViewModel(testDispatcher = dispatcherExtension.testDispatcher)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val state = recreated.uiState.value
        assertThat(state.snackbar).isNull()
        assertThat(state.data?.deviceInfo).isEqualTo(deviceProvider.deviceInfo)
    }
}
