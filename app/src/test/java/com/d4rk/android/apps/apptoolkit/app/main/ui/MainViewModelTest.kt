package com.d4rk.android.apps.apptoolkit.app.main.ui

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.StandardDispatcherExtension
import com.d4rk.android.apps.apptoolkit.app.main.domain.model.ui.UiMainScreen
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.NavigationDrawerItem
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
                UiMainScreen(navigationDrawerItems = expectedItems.toImmutableList()),
                viewModel.uiState.value.data
            )
            assertEquals(1, repo.callCount)
        }

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
            UiMainScreen(showSnackbar = true, snackbarMessage = "boom"),
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
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).build()
}