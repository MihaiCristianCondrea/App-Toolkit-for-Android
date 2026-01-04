package com.d4rk.android.apps.apptoolkit.app.main.ui

import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.StandardDispatcherExtension
import com.d4rk.android.apps.apptoolkit.app.main.ui.states.MainUiState
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import io.mockk.clearAllMocks
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

class MainViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = StandardDispatcherExtension()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initialization triggers navigation load`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeNavigationRepository(flowOf(emptyList()))

        MainViewModel(repo)

        // With StandardTestDispatcher, launched coroutines don't run until you drive the scheduler.
        runCurrent()
        advanceUntilIdle()

        assertEquals(1, repo.callCount)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `successful navigation load populates drawer items`() =
        runTest(dispatcherExtension.testDispatcher) {
            val expectedItems = listOf(
                NavigationDrawerItem(
                    title = 1,
                    selectedIcon = createIcon(),
                    route = "route"
                )
            )
            val repo = FakeNavigationRepository(flowOf(expectedItems))

            val viewModel = MainViewModel(repo)

            runCurrent()
            advanceUntilIdle()

            assertEquals(
                MainUiState(navigationDrawerItems = expectedItems.toImmutableList()),
                viewModel.uiState.value.data
            )
            assertEquals(1, repo.callCount)
        }

    // TODO && FIXME: FIX THE TEST FAIL becuase we changed the VM and data
    /*

Expected <MainUiState(showSnackbar=true, snackbarMessage=boom, showDialog=false, navigationDrawerItems=[])>, actual <MainUiState(showSnackbar=true, snackbarMessage=Failed to load navigation, showDialog=false, navigationDrawerItems=[])>.
java.lang.AssertionError: Expected <MainUiState(showSnackbar=true, snackbarMessage=boom, showDialog=false, navigationDrawerItems=[])>, actual <MainUiState(showSnackbar=true, snackbarMessage=Failed to load navigation, showDialog=false, navigationDrawerItems=[])>.
	at kotlin.test.DefaultAsserter.fail(DefaultAsserter.kt:16)
	at kotlin.test.Asserter$DefaultImpls.assertTrue(Assertions.kt:694)
	at kotlin.test.DefaultAsserter.assertTrue(DefaultAsserter.kt:11)
	at kotlin.test.Asserter$DefaultImpls.assertEquals(Assertions.kt:713)
	at kotlin.test.DefaultAsserter.assertEquals(DefaultAsserter.kt:11)
	at kotlin.test.AssertionsKt__AssertionsKt.assertEquals(Assertions.kt:63)
	at kotlin.test.AssertionsKt.assertEquals(Unknown Source)
	at kotlin.test.AssertionsKt__AssertionsKt.assertEquals$default(Assertions.kt:62)
	at kotlin.test.AssertionsKt.assertEquals$default(Unknown Source)
	at com.d4rk.android.apps.apptoolkit.app.main.ui.MainViewModelTest$navigation load error shows snackbar$1.invokeSuspend(MainViewModelTest.kt:88)
	at com.d4rk.android.apps.apptoolkit.app.main.ui.MainViewModelTest$navigation load error shows snackbar$1.invoke(MainViewModelTest.kt)
	at com.d4rk.android.apps.apptoolkit.app.main.ui.MainViewModelTest$navigation load error shows snackbar$1.invoke(MainViewModelTest.kt)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$1.invokeSuspend(TestBuilders.kt:317)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:34)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:100)
	at kotlinx.coroutines.test.TestDispatcher.processEvent$kotlinx_coroutines_test(TestDispatcher.kt:24)
	at kotlinx.coroutines.test.TestCoroutineScheduler.tryRunNextTaskUnless$kotlinx_coroutines_test(TestCoroutineScheduler.kt:99)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt$runTest$2$1$workRunner$1.invokeSuspend(TestBuilders.kt:326)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:34)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:100)
	at kotlinx.coroutines.EventLoopImplBase.processNextEvent(EventLoop.common.kt:263)
	at kotlinx.coroutines.BlockingCoroutine.joinBlocking(Builders.kt:94)
	at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking(Builders.kt:70)
	at kotlinx.coroutines.BuildersKt.runBlocking(Unknown Source)
	at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(Builders.kt:48)
	at kotlinx.coroutines.BuildersKt.runBlocking$default(Unknown Source)
	at kotlinx.coroutines.test.TestBuildersJvmKt.createTestResult(TestBuildersJvm.kt:10)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0(TestBuilders.kt:309)
	at kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0(Unknown Source)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0(TestBuilders.kt:167)
	at kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0(Unknown Source)
	at kotlinx.coroutines.test.TestBuildersKt__TestBuildersKt.runTest-8Mi8wO0$default(TestBuilders.kt:159)
	at kotlinx.coroutines.test.TestBuildersKt.runTest-8Mi8wO0$default(Unknown Source)
	at com.d4rk.android.apps.apptoolkit.app.main.ui.MainViewModelTest.navigation load error shows snackbar(MainViewModelTest.kt:77)



    */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigation load error shows snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val error = IllegalStateException("boom")
        val repo = FakeNavigationRepository(
            flow { throw error }
        )

        val viewModel = MainViewModel(repo)

        runCurrent()
        advanceUntilIdle()

        assertEquals(
            MainUiState(showSnackbar = true, snackbarMessage = "boom"),
            viewModel.uiState.value.data
        )
        assertEquals(1, repo.callCount)
    }

    private class FakeNavigationRepository(
        private val upstream: Flow<List<NavigationDrawerItem>>
    ) : NavigationRepository {
        var callCount: Int = 0
            private set

        override fun getNavigationDrawerItems(): Flow<List<NavigationDrawerItem>> {
            callCount++
            return upstream
        }
    }

    private fun createIcon(): ImageVector =
        ImageVector.Builder(
            name = "navigation_icon",
            defaultWidth = SizeConstants.TwentyFourSize,
            defaultHeight = SizeConstants.TwentyFourSize,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).build()
}
