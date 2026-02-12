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

package com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation

import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.FavoriteAppsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.toNavKeyOrDefault
import com.d4rk.android.apps.apptoolkit.core.data.local.DataStore
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.startupDestinationFlow
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
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
        every { dataStore.getStartupPage(default = NavigationRoutes.ROUTE_APPS_LIST) } returns flowOf(
            ""
        )

        val startDestination = dataStore.startupDestinationFlow(
            defaultRoute = NavigationRoutes.ROUTE_APPS_LIST,
            mapToKey = { route -> route.toNavKeyOrDefault() }
        ).first()

        assertEquals(AppsListRoute, startDestination)
    }

    @Test
    fun `favorite startup page starts with favorites`() = runTest {
        every {
            dataStore.getStartupPage(default = NavigationRoutes.ROUTE_APPS_LIST)
        } returns flowOf(NavigationRoutes.ROUTE_FAVORITE_APPS)

        val startDestination = dataStore.startupDestinationFlow(
            defaultRoute = NavigationRoutes.ROUTE_APPS_LIST,
            mapToKey = { route -> route.toNavKeyOrDefault() }
        ).first()

        assertEquals(FavoriteAppsRoute, startDestination)
    }
}
