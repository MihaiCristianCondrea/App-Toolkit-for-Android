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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.domain.models.AppNavigationEntryContext
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.navigation.AppsListRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.navigation.entryProviderFor
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils.extensions.datastore.startupDestinationFlow
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.GeneralSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.HelpRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LibraryExtrasRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LicensesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SupportRoute
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
    fun `blank startup page defaults to toolkit tiles`() = runTest {
        every { dataStore.getStartupPage(default = NavigationRoutes.ROUTE_TOOLKIT_TILES) } returns flowOf(
            ""
        )

        val startDestination = dataStore.startupDestinationFlow(
            defaultRoute = NavigationRoutes.ROUTE_TOOLKIT_TILES,
            mapToKey = { route -> route.toNavKeyOrDefault() }
        ).first()

        assertEquals(ToolkitTilesRoute, startDestination)
    }

    @Test
    fun `legacy favorite startup page falls back to toolkit tiles`() = runTest {
        every {
            dataStore.getStartupPage(default = NavigationRoutes.ROUTE_TOOLKIT_TILES)
        } returns flowOf("favorite_apps")

        val startDestination = dataStore.startupDestinationFlow(
            defaultRoute = NavigationRoutes.ROUTE_TOOLKIT_TILES,
            mapToKey = { route -> route.toNavKeyOrDefault() }
        ).first()

        assertEquals(ToolkitTilesRoute, startDestination)
    }

    @Test
    fun `quick tools startup page maps to toolkit tiles`() = runTest {
        every {
            dataStore.getStartupPage(default = NavigationRoutes.ROUTE_TOOLKIT_TILES)
        } returns flowOf(NavigationRoutes.ROUTE_TOOLKIT_TILES)

        val startDestination = dataStore.startupDestinationFlow(
            defaultRoute = NavigationRoutes.ROUTE_TOOLKIT_TILES,
            mapToKey = { route -> route.toNavKeyOrDefault() }
        ).first()

        assertEquals(ToolkitTilesRoute, startDestination)
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
