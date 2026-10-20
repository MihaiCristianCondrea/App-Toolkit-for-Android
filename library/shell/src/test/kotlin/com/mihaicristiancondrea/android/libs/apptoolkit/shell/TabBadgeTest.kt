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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
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

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xhdpi")
class TabBadgeTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        startKoin {
            modules(module { single { CommonDataStore(ApplicationProvider.getApplicationContext()) } })
        }
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun `a tab's badge text follows the state it reads`() {
        val unread = mutableStateOf(0)
        val icon = ToolkitIcon.Vector(Icons.Outlined.Home)
        val graph = shellGraph(appTitle = CommonR.string.app_name) {
            tab(SingleTabKey, NavigationR.string.updates, icon, badgeText = { unread.value.takeIf { it > 0 }?.toString() }) {
                Text("Inbox content")
            }
            tab(OtherTabKey, NavigationR.string.help_and_feedback, icon) { Text("Second tab content") }
        }
        compose.setContent {
            AppTheme { ShellHost(graph = graph, preferences = InMemoryShellPreferences()) }
        }
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("Inbox content").fetchSemanticsNodes().isNotEmpty()
        }
        compose.onAllNodesWithText("7", useUnmergedTree = true).assertCountEquals(0)

        unread.value = 7
        compose.waitForIdle()
        compose.onAllNodesWithText("7", useUnmergedTree = true).assertCountEquals(1)

        unread.value = 0
        compose.waitForIdle()
        compose.onAllNodesWithText("7", useUnmergedTree = true).assertCountEquals(0)
    }
}
