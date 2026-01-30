package com.d4rk.android.apps.apptoolkit.app.main.data.repository

import com.d4rk.android.apps.apptoolkit.app.core.utils.dispatchers.TestDispatchers
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.main.data.repository.MainRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.NavigationDrawerItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainRepositoryImplTest {

    @Test
    fun `getNavigationDrawerItems emits expected items`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = MainRepositoryImpl(TestDispatchers(dispatcher))

        val items = repository.getNavigationDrawerItems().first()

        assertEquals(4, items.size)
        assertEquals(
            listOf(
                R.string.settings,
                R.string.help_and_feedback,
                R.string.updates,
                R.string.share
            ),
            items.map(NavigationDrawerItem::title)
        )
        assertEquals(
            listOf(
                NavigationDrawerRoutes.ROUTE_SETTINGS,
                NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK,
                NavigationDrawerRoutes.ROUTE_UPDATES,
                NavigationDrawerRoutes.ROUTE_SHARE,
            ),
            items.map(NavigationDrawerItem::route)
        )
    }
}
