package com.d4rk.android.libs.apptoolkit.app.about.ui

import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.CopyDeviceInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.GetAboutInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.ui.contract.AboutEvent
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
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

    private fun createViewModel(
        testDispatcher: TestDispatcher = dispatcherExtension.testDispatcher,
        repository: AboutRepository = object : AboutRepository {
            override suspend fun getAboutInfo(): AboutInfo = AboutInfo(
                appVersion = buildInfoProvider.appVersion,
                appVersionCode = buildInfoProvider.appVersionCode,
                deviceInfo = deviceProvider.deviceInfo,
            )

            override fun copyDeviceInfo(label: String, deviceInfo: String): Boolean = true
        }
    ): AboutViewModel {
        val testDispatchers: DispatcherProvider = TestDispatchers(testDispatcher)

        return AboutViewModel(
            getAboutInfo = GetAboutInfoUseCase(repository),
            copyDeviceInfo = CopyDeviceInfoUseCase(repository),
            dispatchers = testDispatchers,
        )
    }

    private fun createFailingViewModel(
        testDispatcher: TestDispatcher = dispatcherExtension.testDispatcher,
    ): AboutViewModel {
        val repository = object : AboutRepository {
            override suspend fun getAboutInfo(): AboutInfo = throw Exception("fail")

            override fun copyDeviceInfo(label: String, deviceInfo: String): Boolean = false
        }
        return createViewModel(testDispatcher = testDispatcher, repository = repository)
    }

    @Test
    fun `initial load populates ui state`() = runTest(dispatcherExtension.testDispatcher) {
        val viewModel = createViewModel(testDispatcher = dispatcherExtension.testDispatcher)
        dispatcherExtension.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.data?.deviceInfo).isEqualTo(deviceProvider.deviceInfo)
        assertThat(state.data?.appVersion).isEqualTo(buildInfoProvider.appVersion)
        assertThat(state.data?.appVersionCode).isEqualTo(buildInfoProvider.appVersionCode)
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

    /*
    TODO FIXME:


expected not to be: 1767524814502
expected not to be: 1767524814502
	at app//com.d4rk.android.libs.apptoolkit.app.about.ui.TestAboutViewModel$repeated copy events replace snackbar$1.invokeSuspend(TestAboutViewModel.kt:144)
	at app//com.d4rk.android.libs.apptoolkit.app.about.ui.TestAboutViewModel$repeated copy events replace snackbar$1.invoke(TestAboutViewModel.kt)
	at app//com.d4rk.android.libs.apptoolkit.app.about.ui.TestAboutViewModel$repeated copy events replace snackbar$1.invoke(TestAboutViewModel.kt)
	at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$1.invokeSuspend(TestBuilders.kt:317)
	at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$1.invoke(TestBuilders.kt)
	at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$1.invoke(TestBuilders.kt)
	at app//kotlinx.coroutines.intrinsics.UndispatchedKt.startCoroutineUndispatched(Undispatched.kt:20)
	at app//kotlinx.coroutines.CoroutineStart.invoke(CoroutineStart.kt:360)
	at app//kotlinx.coroutines.AbstractCoroutine.start(AbstractCoroutine.kt:134)
	at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1.invokeSuspend(TestBuilders.kt:312)
	at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1.invoke(TestBuilders.kt)
	at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1.invoke(TestBuilders.kt)
	at app//kotlinx.coroutines.test.TestBuildersJvmKt$createTestResult$1.invokeSuspend(TestBuildersJvm.kt:11)
	at app//kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:34)
	at app//kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:100)
	at app//kotlinx.coroutines.EventLoopImplBase.processNextEvent(EventLoop.common.kt:263)
	at app//kotlinx.coroutines.BlockingCoroutine.joinBlocking(Builders.kt:94)
	at app//kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking(Builders.kt:70)
	at app//kotlinx.coroutines.BuildersKt.runBlocking(Unknown Source)
	at app//kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(Builders.kt:48)
	at app//kotlinx.coroutines.BuildersKt.runBlocking$default(Unknown Source)
	at app//kotlinx.coroutines.test.TestBuildersJvmKt.createTestResult(TestBuildersJvm.kt:10)
	at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0(TestBuilders.kt:309)
	at app//kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0(Unknown Source)
	at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0(TestBuilders.kt:167)
	at app//kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0(Unknown Source)
	at app//kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0$default(TestBuilders.kt:159)
	at app//kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0$default(Unknown Source)
	at app//com.d4rk.android.libs.apptoolkit.app.about.ui.TestAboutViewModel.repeated copy events replace snackbar(TestAboutViewModel.kt:132)
    */
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
