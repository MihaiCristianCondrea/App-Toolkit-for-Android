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
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import kotlinx.serialization.Serializable
import org.junit.After
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

/**
 * The capabilities `ShellHost` gives the settings pages describe the app as declared: a layout the
 * developer options force, or the window the app is drawn in, does not change them.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xhdpi")
class ShellCapabilitiesHostTest {

    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        startKoin {
            modules(module { single { CommonDataStore(ApplicationProvider.getApplicationContext()) } })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `app E, a forced rail, still uses bottom navigation as declared`() {
        show(ShellSettings(layoutMode = ShellLayoutMode.Rail))

        compose.onNodeWithContentDescription("Expand navigation").assertExists()
        compose.onNodeWithText("bottom navigation: true").assertExists()
        compose.onNodeWithText("tabs: 3").assertExists()
    }

    @Test
    fun `the capabilities follow the layout policy the app passes`() {
        show(ShellSettings(), ShellLayoutPolicy(railFrom = 0.dp, contentMaxWidth = 840.dp))

        compose.onNodeWithText("bottom navigation: false").assertExists()
        compose.onNodeWithText("width limit: true").assertExists()
    }

    private fun show(settings: ShellSettings, policy: ShellLayoutPolicy = ShellLayoutPolicy()) {
        compose.setContent {
            AppTheme {
                ShellHost(graph = CapabilityGraph, preferences = InMemoryShellPreferences(settings), layoutPolicy = policy)
            }
        }
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("tabs: 3").fetchSemanticsNodes().isNotEmpty()
        }
    }
}

@Serializable
private data object FirstTab : NavKey

@Serializable
private data object SecondTab : NavKey

@Serializable
private data object ThirdTab : NavKey

/** Writes out the capabilities the shell provides, for the test to read. */
@Composable
private fun CapabilityReadout() {
    val capabilities = LocalShellCapabilities.current
    Column {
        Text("tabs: ${capabilities.tabCount}")
        Text("bottom navigation: ${capabilities.usesBottomNavigation}")
        Text("width limit: ${capabilities.hasContentWidthLimit}")
    }
}

private val CapabilityGraph: ShellGraph = shellGraph(appTitle = CommonR.string.app_name) {
    val icon = ToolkitIcon.Vector(Icons.Outlined.Home)
    tab(FirstTab, NavigationR.string.updates, icon) { CapabilityReadout() }
    tab(SecondTab, NavigationR.string.settings, icon) { CapabilityReadout() }
    tab(ThirdTab, NavigationR.string.share, icon) { CapabilityReadout() }
}
