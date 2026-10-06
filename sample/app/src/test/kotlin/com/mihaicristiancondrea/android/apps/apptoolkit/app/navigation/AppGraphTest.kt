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
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.AppsListRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.ComponentsRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.unregisteredDestinations
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.di.aboutModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.di.advancedSettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.di.displaySettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.di.privacyModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.di.themeSettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.startupValueFlow
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DeveloperOptionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AboutRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdvancedSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DiagnosticsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DisplaySettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.OnboardingRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PrivacySettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.StartupRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.ThemeSettingsRoute
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
import org.koin.dsl.koinApplication
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R as AboutR

class AppGraphTest {

    private fun graph(
        showComponents: Boolean = false,
        onShowChangelog: () -> Unit = {},
    ): ShellGraph = appGraph(
        showComponents = showComponents,
        onShowChangelog = onShowChangelog,
    )

    private fun ShellGraph.drawerLinks() = drawer.filterIsInstance<DrawerEntry.Link>().map { it.key }

    @Test
    fun `the sample retains the optional bottom banner slot`() {
        assertNotNull(graph().banner)
        assertNotNull(graph(showComponents = true).banner)
    }

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
            DisplaySettingsRoute,
            ThemeSettingsRoute,
            PrivacySettingsRoute,
            DiagnosticsSettingsRoute,
            AdvancedSettingsRoute,
            AboutRoute,
            StartupRoute,
            OnboardingRoute,
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
    fun `the drawer ends with settings, help, updates and share, pinned to the bottom`() {
        val drawer = graph(showComponents = true).drawer
        val footer = drawer.takeLast(4)

        assertEquals(DrawerEntry.Spacer, drawer[drawer.size - 5])
        assertEquals(SettingsRoute, (footer[0] as DrawerEntry.Link).key)
        assertEquals(HelpRoute, (footer[1] as DrawerEntry.Link).key)
        assertEquals(AboutR.string.updates, (footer[2] as DrawerEntry.Action).label)
        assertEquals(AboutR.string.share, (footer[3] as DrawerEntry.Action).label)
        // The app's own entries come first, and developer options are reached from Advanced.
        assertEquals(ComponentsRoute, (drawer.first() as DrawerEntry.Link).key)
        assertFalse(DeveloperOptionsRoute in graph().drawerLinks())
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
    fun `the settings list is a list page and its categories open beside it`() {
        val graph = graph()

        assertEquals(PaneRole.List, graph.destination(SettingsRoute).paneRole)
        listOf(DisplaySettingsRoute, PrivacySettingsRoute, AdvancedSettingsRoute, AboutRoute).forEach { key ->
            assertEquals(PaneRole.Detail, graph.destination(key).paneRole, "$key")
        }
    }

    @Test
    fun `every settings search result opens a page the app registers`() {
        val koin = koinApplication {
            modules(displaySettingsModule, themeSettingsModule, privacyModule, aboutModule, advancedSettingsModule)
        }.koin
        val providers = koin.getAll<SettingsSearchProvider>()
        val context = SettingsSearchContext(graph())

        assertEquals(5, providers.size)
        assertEquals(emptyList(), providers.flatMap { it.unregisteredDestinations(context) })
    }

    @Test
    fun `the first-launch screens are start screens`() {
        assertTrue(graph().startOptions.containsAll(listOf(StartupRoute, OnboardingRoute)))
    }

    @Test
    fun `the settings shortcut opens the settings page`() {
        val shortcut = mockk<Intent> { every { action } returns ACTION_OPEN_SETTINGS }
        val launcher = mockk<Intent> { every { action } returns Intent.ACTION_MAIN }

        assertEquals(SettingsRoute, graph().keyFor(shortcut))
        assertNull(graph().keyFor(launcher))
    }
}
