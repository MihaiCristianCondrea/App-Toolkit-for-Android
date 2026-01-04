package com.d4rk.android.libs.apptoolkit.app.permissions.ui

import com.d4rk.android.libs.apptoolkit.app.permissions.domain.repository.PermissionsRepository
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract.PermissionsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsCategory
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
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
        val config = SettingsConfig(
            title = "P",
            categories = listOf(SettingsCategory(title = "c", preferences = emptyList()))
        )
        setup(config = config)

        viewModel.onEvent(PermissionsEvent.Load)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.data?.title).isEqualTo("P")
        assertThat(viewModel.uiState.value.screenState).isInstanceOf(ScreenState.Success::class.java)
    }

    // FIXME:
    /*


expected instance of: com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState$Error
but was instance of : com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState$NoData
with value          : NoData(data=no_data)
expected instance of: com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState$Error
but was instance of : com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState$NoData
with value          : NoData(data=no_data)
	at app//com.d4rk.android.libs.apptoolkit.app.permissions.ui.TestPermissionsViewModel$load permissions error$1.invokeSuspend(TestPermissionsViewModel.kt:64)
	at app//com.d4rk.android.libs.apptoolkit.app.permissions.ui.TestPermissionsViewModel$load permissions error$1.invoke(TestPermissionsViewModel.kt)
	at app//com.d4rk.android.libs.apptoolkit.app.permissions.ui.TestPermissionsViewModel$load permissions error$1.invoke(TestPermissionsViewModel.kt)
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
	at app//com.d4rk.android.libs.apptoolkit.app.permissions.ui.TestPermissionsViewModel.load permissions error(TestPermissionsViewModel.kt:58)



    */
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

