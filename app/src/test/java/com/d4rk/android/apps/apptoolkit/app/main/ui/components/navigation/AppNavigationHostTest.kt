package com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation

import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.core.data.datastore.DataStore
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AppNavigationHostTest {

    private val dataStore: DataStore = mockk()

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `blank startup page defaults to apps list`() = runTest {
        every { dataStore.getStartupPage(default = NavigationRoutes.ROUTE_APPS_LIST) } returns flowOf("")

        val startDestination = dataStore.startupDestinationFlow().first()

        assertEquals(NavigationRoutes.ROUTE_APPS_LIST, startDestination)
        verify(exactly = 1) { dataStore.getStartupPage(default = NavigationRoutes.ROUTE_APPS_LIST) } // FIXME: Flow is constructed but not used
    }

    @Test
    fun `favorite startup page starts with favorites`() = runTest {
        every {
            dataStore.getStartupPage(default = NavigationRoutes.ROUTE_APPS_LIST)
        } returns flowOf(NavigationRoutes.ROUTE_FAVORITE_APPS)

        val startDestination = dataStore.startupDestinationFlow().first()

        assertEquals(NavigationRoutes.ROUTE_FAVORITE_APPS, startDestination)
        verify(exactly = 1) { dataStore.getStartupPage(default = NavigationRoutes.ROUTE_APPS_LIST) } // FIXME: Flow is constructed but not used
    }
}
