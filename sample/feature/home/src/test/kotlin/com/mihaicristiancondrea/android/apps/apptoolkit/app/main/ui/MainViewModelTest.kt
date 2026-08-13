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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.ui.graphics.vector.ImageVector
import app.cash.turbine.test
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.contracts.MainEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.states.MainUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.models.ConsentSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.data.repositories.NavigationRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.StandardDispatcherExtension
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.navigation.NavigationDrawerItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.ScreenState
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
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
                icon = Icons.Outlined.Android,
                selectedIcon = createIcon(),
                route = "route"
            )
        )

        val repo = FakeNavigationRepository(flowOf(expectedItems))
        val dispatchers = TestDispatchers(testDispatcher = dispatcherExtension.testDispatcher)

        MainViewModel(
            navigationRepository = repo,
            consentRepository = FakeConsentRepository(),
            requestInAppReviewUseCase = mockk(relaxed = true),
            inAppUpdateRepository = mockk(relaxed = true),
            firebaseController = mockk<FirebaseController>(relaxed = true),
            dispatchers = dispatchers,
        )

        runCurrent()
        advanceUntilIdle()

        assertEquals(1, repo.callCount)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initialization applies persisted consent once`() =
        runTest(dispatcherExtension.testDispatcher) {
            val consentRepository = mockk<ConsentRepository>(relaxed = true)

            MainViewModel(
                navigationRepository = FakeNavigationRepository(flowOf(emptyList())),
                consentRepository = consentRepository,
                requestInAppReviewUseCase = mockk(relaxed = true),
                inAppUpdateRepository = mockk(relaxed = true),
                firebaseController = mockk<FirebaseController>(relaxed = true),
                dispatchers = TestDispatchers(testDispatcher = dispatcherExtension.testDispatcher),
            )

            runCurrent()
            advanceUntilIdle()

            coVerify(exactly = 1) { consentRepository.applyInitialConsent() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `successful navigation load populates drawer items`() =
        runTest(dispatcherExtension.testDispatcher) {
            val expectedItems = listOf(
                NavigationDrawerItem(
                    title = 1,
                    icon = Icons.Outlined.Android,
                    selectedIcon = createIcon(),
                    route = "route"
                )
            )

            val repo = FakeNavigationRepository(flowOf(expectedItems))
            val firebaseController = mockk<FirebaseController>(relaxed = true)
            val dispatchers = TestDispatchers(dispatcherExtension.testDispatcher)

            val viewModel = MainViewModel(
                navigationRepository = repo,
                consentRepository = FakeConsentRepository(),
                requestInAppReviewUseCase = mockk(relaxed = true),
                inAppUpdateRepository = mockk(relaxed = true),
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

        val viewModel = MainViewModel(
            navigationRepository = repo,
            consentRepository = FakeConsentRepository(),
            requestInAppReviewUseCase = mockk(relaxed = true),
            inAppUpdateRepository = mockk(relaxed = true),
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
                com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R.string.error_failed_to_load_navigation,
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

        val viewModel = MainViewModel(
            navigationRepository = repo,
            consentRepository = FakeConsentRepository(),
            requestInAppReviewUseCase = mockk(relaxed = true),
            inAppUpdateRepository = mockk(relaxed = true),
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `requestConsent skips overlapping calls while one is in progress`() =
        runTest(dispatcherExtension.testDispatcher) {
            val firebaseController = mockk<FirebaseController>(relaxed = true)
            val consentRepository = CountingConsentRepository(
                upstream = flow {
                    emit(DataState.Loading())
                    awaitCancellation()
                }
            )

            val viewModel = MainViewModel(
                navigationRepository = FakeNavigationRepository(flowOf(emptyList())),
                consentRepository = consentRepository,
                requestInAppReviewUseCase = mockk(relaxed = true),
                inAppUpdateRepository = mockk(relaxed = true),
                firebaseController = firebaseController,
                dispatchers = TestDispatchers(dispatcherExtension.testDispatcher),
            )

            val host = object : ConsentHost {
                override val activity = mockk<android.app.Activity>(relaxed = true)
            }
            viewModel.onEvent(MainEvent.RequestConsent(host = host))
            viewModel.onEvent(MainEvent.RequestConsent(host = host))

            runCurrent()

            assertEquals(1, consentRepository.callCount)
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

private class CountingConsentRepository(
    private val upstream: Flow<DataState<Unit, Errors.UseCase>>,
) : ConsentRepository {
    var callCount: Int = 0
        private set

    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> {
        callCount++
        return upstream
    }

    override suspend fun applyInitialConsent() = Unit

    override suspend fun applyConsentSettings(settings: ConsentSettings) = Unit
}
