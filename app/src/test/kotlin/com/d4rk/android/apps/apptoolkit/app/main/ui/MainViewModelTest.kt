package com.d4rk.android.apps.apptoolkit.app.main.ui

import androidx.compose.ui.graphics.vector.ImageVector
import app.cash.turbine.test
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.StandardDispatcherExtension
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.TestDispatchers
import com.d4rk.android.apps.apptoolkit.app.main.domain.usecases.GetNavigationDrawerItemsUseCase
import com.d4rk.android.apps.apptoolkit.app.main.ui.state.MainUiState
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import io.mockk.clearAllMocks
import io.mockk.mockk
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
import kotlin.test.assertTrue

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
        val expectedItems = listOf(
            NavigationDrawerItem(
                title = 1,
                selectedIcon = createIcon(),
                route = "route"
            )
        )

        val repo = FakeNavigationRepository(flowOf(expectedItems))
        val useCase = GetNavigationDrawerItemsUseCase(
            navigationRepository = repo,
            firebaseController = mockk<FirebaseController>(relaxed = true)
        )
        val dispatchers = TestDispatchers(testDispatcher = dispatcherExtension.testDispatcher)

        MainViewModel(
            getNavigationDrawerItemsUseCase = useCase,
            requestConsentUseCase = RequestConsentUseCase(
                repository = FakeConsentRepository(),
                firebaseController = mockk<FirebaseController>(relaxed = true)
            ),
            firebaseController = mockk<FirebaseController>(relaxed = true),
            dispatchers = dispatchers,
        )

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
            val firebaseController = mockk<FirebaseController>(relaxed = true)
            val useCase = GetNavigationDrawerItemsUseCase(repo, firebaseController)
            val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)

            val viewModel = MainViewModel(
                getNavigationDrawerItemsUseCase = useCase,
                requestConsentUseCase = RequestConsentUseCase(
                    FakeConsentRepository(),
                    firebaseController
                ),
                firebaseController = firebaseController,
                dispatchers = dispatchers,
            )

            runCurrent()
            advanceUntilIdle()

            assertEquals(
                MainUiState(navigationDrawerItems = expectedItems.toImmutableList()),
                viewModel.uiState.value.data
            )
            assertEquals(1, repo.callCount)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigation load error shows snackbar`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeNavigationRepository(upstream = flow { throw IllegalStateException("boom") })
        val firebaseController = mockk<FirebaseController>(relaxed = true)
        val useCase = GetNavigationDrawerItemsUseCase(repo, firebaseController)

        val viewModel = MainViewModel(
            getNavigationDrawerItemsUseCase = useCase,
            requestConsentUseCase = RequestConsentUseCase(
                FakeConsentRepository(),
                firebaseController
            ),
            firebaseController = firebaseController,
            dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        )

        viewModel.uiState.test {
            awaitItem()

            runCurrent()

            var state = awaitItem()
            while (state.snackbar == null) {
                state = awaitItem()
            }

            assertTrue(state.screenState is ScreenState.NoData)

            val snackbar = requireNotNull(state.snackbar)
            assertTrue(snackbar.isError)

            val msg = snackbar.message as UiTextHelper.StringResource
            assertEquals(
                com.d4rk.android.libs.apptoolkit.R.string.error_failed_to_load_navigation,
                msg.resourceId
            )

            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(1, repo.callCount)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `empty navigation list sets no data state`() = runTest(dispatcherExtension.testDispatcher) {
        val repo = FakeNavigationRepository(flowOf(emptyList()))
        val firebaseController = mockk<FirebaseController>(relaxed = true)
        val useCase = GetNavigationDrawerItemsUseCase(repo, firebaseController)

        val viewModel = MainViewModel(
            getNavigationDrawerItemsUseCase = useCase,
            requestConsentUseCase = RequestConsentUseCase(
                FakeConsentRepository(),
                firebaseController
            ),
            firebaseController = firebaseController,
            dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
        )

        viewModel.uiState.test {
            awaitItem()

            runCurrent()

            var state = awaitItem()
            while (state.screenState !is ScreenState.NoData) {
                state = awaitItem()
            }

            assertEquals(null, state.snackbar)

            cancelAndIgnoreRemainingEvents()
        }

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

private class FakeConsentRepository : ConsentRepository {
    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ) = flowOf(DataState.Success<Unit, Errors.UseCase>(Unit))

    override suspend fun applyInitialConsent() = Unit

    override suspend fun applyConsentSettings(settings: ConsentSettings) = Unit
}
