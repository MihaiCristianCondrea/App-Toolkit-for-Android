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

import androidx.compose.foundation.layout.PaddingValues
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppsListRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.ComponentsRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.NavigationRoutes
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.ToolkitTilesRoute
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.toNavKeyOrDefault
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.AdsSettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.GeneralSettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.HelpRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.LibraryExtrasRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.LicensesRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.PermissionsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.SettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.SupportRoute
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.startupDestinationFlow
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.entryProviderFor
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
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

    private val dataStore: CommonDataStore = mockk()

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
    fun `legacy favorite startup page falls back to apps list`() = runTest {
        every {
            dataStore.getStartupPage(default = NavigationRoutes.ROUTE_APPS_LIST)
        } returns flowOf("favorite_apps")

        val startDestination = dataStore.startupDestinationFlow(
            defaultRoute = NavigationRoutes.ROUTE_APPS_LIST,
            mapToKey = { route -> route.toNavKeyOrDefault() }
        ).first()

        assertEquals(AppsListRoute, startDestination)
    }

    @Test
    fun `navigation entries retain typed content keys required by scene routing`() {
        val entryProvider = entryProviderFor(
            appNavigationEntryBuilders(
                context = AppNavigationEntryContext(
                    paddingValues = PaddingValues(),
                    windowWidthSizeClass = AppWindowWidthSizeClass.Compact,
                    onRandomAppHandlerChanged = { _, _ -> },
                ),
            )
        )
        val routes: List<StableNavKey> = listOf(
            AppsListRoute,
            ToolkitTilesRoute,
            ComponentsRoute,
            LibraryExtrasRoute,
            SettingsRoute,
            GeneralSettingsRoute(title = "Display", contentKey = "display"),
            HelpRoute,
            SupportRoute,
            AdsSettingsRoute,
            PermissionsRoute,
            LicensesRoute,
        )

        routes.forEach { route ->
            assertEquals(route, entryProvider(route).contentKey)
        }
    }
}
