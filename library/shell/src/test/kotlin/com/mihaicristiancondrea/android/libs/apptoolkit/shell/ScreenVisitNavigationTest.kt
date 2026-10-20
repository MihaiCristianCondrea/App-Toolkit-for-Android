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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
import kotlinx.serialization.Serializable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.annotation.Config
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR

/** Screen visits through the real shell: its tab stacks, pages and list-detail scenes. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class ScreenVisitNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val telemetry = FakeTelemetryRepository()

    @Before
    fun setUp() {
        startKoin {
            modules(module { single { CommonDataStore(ApplicationProvider.getApplicationContext()) } })
        }
    }

    @After
    fun tearDown() = stopKoin()

    private fun visits(name: String): Int = telemetry.loggedScreenViews.count { it.first == name }

    private fun show() {
        val icon = ToolkitIcon.Vector(Icons.Outlined.Home)
        val graph = shellGraph(appTitle = CommonR.string.app_name) {
            tab(VisitHomeKey, NavigationR.string.updates, icon, paneRole = PaneRole.List) {
                val navigator = LocalShellNavigator.current
                ScreenHost(screen = TrackedScreen("Home")) {
                    Column {
                        Text("Home content")
                        Text("Open page", Modifier.clickable { navigator.navigate(VisitPageKey) })
                        Text("Open first", Modifier.clickable { navigator.navigate(VisitDetailKey(1)) })
                        Text("Open second", Modifier.clickable { navigator.navigate(VisitDetailKey(2)) })
                    }
                }
            }
            tab(VisitOtherKey, NavigationR.string.help_and_feedback, icon) {
                ScreenHost(screen = TrackedScreen("Other")) { Text("Other content") }
            }
            child<VisitDetailKey>(paneRole = PaneRole.Detail, title = { "Detail ${it.id}" }) { key ->
                ScreenHost(screen = TrackedScreen("Detail${key.id}")) { Text("Detail content ${key.id}") }
            }
            page<VisitPageKey>(title = { "Page" }) {
                ScreenHost(screen = TrackedScreen("Page")) { Text("Page content") }
            }
        }
        compose.setContent {
            AppTheme {
                CompositionLocalProvider(LocalTelemetry provides telemetry) {
                    ShellHost(graph = graph, preferences = InMemoryShellPreferences())
                }
            }
        }
        waitFor("Home content")
    }

    private fun waitFor(text: String) {
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
        compose.waitForIdle()
    }

    private fun back() {
        compose.activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
        compose.waitForIdle()
    }

    @Test
    fun `the start tab is reported once`() {
        show()

        assertEquals(1, visits("Home"))
        assertEquals(1, telemetry.loggedScreenViews.size)
    }

    @Test
    @Config(qualifiers = "w411dp-h891dp")
    fun `moving between tabs reports each arrival, and the retained tab only when it returns`() {
        show()

        compose.onNodeWithText("Help & feedback").performClick()
        waitFor("Other content")
        assertEquals(1, visits("Other"))
        assertEquals(1, visits("Home"))

        compose.onNodeWithText("Updates").performClick()
        waitFor("Home content")
        assertEquals(2, visits("Home"))
        assertEquals(1, visits("Other"))
    }

    @Test
    @Config(qualifiers = "w411dp-h891dp")
    fun `a page over the shell is reported, and the tab again once the page closes`() {
        show()

        compose.onNodeWithText("Open page").performClick()
        waitFor("Page content")
        assertEquals(1, visits("Page"))
        assertEquals(1, visits("Home"))

        back()
        waitFor("Home content")
        assertEquals(2, visits("Home"))
        assertEquals(1, visits("Page"))
    }

    @Test
    @Config(qualifiers = "w1000dp-h800dp")
    fun `a list and its detail shown together each report their own visit`() {
        show()

        compose.onNodeWithText("Open first").performClick()
        waitFor("Detail content 1")
        compose.onNodeWithText("Home content").assertExists()
        assertEquals(1, visits("Home"))
        assertEquals(1, visits("Detail1"))

        compose.onNodeWithText("Open second").performClick()
        waitFor("Detail content 2")
        assertEquals(1, visits("Home"))
        assertEquals(1, visits("Detail2"))
        assertEquals(1, visits("Detail1"))
    }
}

@Serializable
internal data object VisitHomeKey : NavKey

@Serializable
internal data object VisitOtherKey : NavKey

@Serializable
internal data object VisitPageKey : NavKey

@Serializable
internal data class VisitDetailKey(val id: Int) : NavKey
