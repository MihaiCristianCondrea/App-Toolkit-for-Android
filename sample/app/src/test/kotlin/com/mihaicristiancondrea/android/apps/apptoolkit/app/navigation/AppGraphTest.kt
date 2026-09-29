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

import android.content.Intent
import com.mihaicristiancondrea.android.apps.apptoolkit.BuildConfig
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.navigation.AppsListRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.fab.RandomAppAction
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.navigation.ComponentsRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.startupValueFlow
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DeveloperOptionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.GeneralSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.HelpRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LibraryExtrasRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LicensesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SupportRoute
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R as AboutR

class AppGraphTest {

    private fun graph(
        showComponents: Boolean = false,
        onShowChangelog: () -> Unit = {},
    ): ShellGraph = appGraph(
        randomApp = RandomAppAction(),
        showComponents = showComponents,
        onShowChangelog = onShowChangelog,
    )

    private fun ShellGraph.drawerLinks() = drawer.filterIsInstance<DrawerEntry.Link>().map { it.key }

    @Test
    fun `the tabs are tiles then apps, and a launch opens on tiles`() {
        val graph = graph()

        assertEquals(listOf(ToolkitTilesRoute, AppsListRoute), graph.tabs.map { it.key })
        assertEquals(ToolkitTilesRoute, graph.start)
    }

    @Test
    fun `the stored start page maps to its tab`() = runTest {
        suspend fun startFor(stored: String): Any {
            val dataStore = mockk<CommonDataStore>()
            every { dataStore.getStartupPage(default = ToolkitTilesRoute.ROUTE_ID) } returns flowOf(stored)
            return dataStore.startupValueFlow(ToolkitTilesRoute.ROUTE_ID, ::startKeyFor).first()
        }

        assertEquals(AppsListRoute, startFor(AppsListRoute.ROUTE_ID))
        assertEquals(ToolkitTilesRoute, startFor(ToolkitTilesRoute.ROUTE_ID))
        // A blank value, or one a removed page left behind, opens the default tab.
        assertEquals(ToolkitTilesRoute, startFor(""))
        assertEquals(ToolkitTilesRoute, startFor("favorite_apps"))
    }

    @Test
    fun `every page the app opens is registered`() {
        val graph = graph(showComponents = true)
        val pages = listOf(
            SettingsRoute,
            GeneralSettingsRoute(title = "Display", contentKey = "display"),
            HelpRoute,
            SupportRoute,
            AdsSettingsRoute,
            PermissionsRoute,
            LicensesRoute,
            LibraryExtrasRoute,
            DeveloperOptionsRoute,
            ComponentsRoute,
        )

        pages.forEach { key -> assertTrue(graph.contains(key), "$key is not registered") }
        (graph.drawerLinks() + graph.overflow.filterIsInstance<DrawerEntry.Link>().map { it.key })
            .forEach { key -> assertTrue(graph.contains(key), "$key is linked but not registered") }
    }

    @Test
    fun `the components showcase is offered only once it is shown`() {
        assertFalse(ComponentsRoute in graph(showComponents = false).drawerLinks())
        assertTrue(ComponentsRoute in graph(showComponents = true).drawerLinks())
    }

    @Test
    fun `the drawer offers settings and help, and developer options only in debug builds`() {
        val links = graph().drawerLinks()

        assertTrue(SettingsRoute in links)
        assertTrue(HelpRoute in links)
        assertEquals(BuildConfig.DEBUG, DeveloperOptionsRoute in links)
    }

    @Test
    fun `the updates entry opens the changelog`() {
        var shown = false
        val updates = graph(onShowChangelog = { shown = true }).drawer
            .filterIsInstance<DrawerEntry.Action>()
            .single { it.label == AboutR.string.updates }

        updates.onClick(mockk(relaxed = true))

        assertTrue(shown)
    }

    @Test
    fun `the overflow menu offers support`() {
        assertEquals(
            listOf(SupportRoute),
            graph().overflow.filterIsInstance<DrawerEntry.Link>().map { it.key },
        )
    }

    @Test
    fun `the settings shortcut opens the settings page`() {
        val shortcut = mockk<Intent> { every { action } returns ACTION_OPEN_SETTINGS }
        val launcher = mockk<Intent> { every { action } returns Intent.ACTION_MAIN }

        assertEquals(SettingsRoute, graph().keyFor(shortcut))
        assertNull(graph().keyFor(launcher))
    }
}
