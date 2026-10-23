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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.PageScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
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

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xhdpi")
class TaskPageBackButtonTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val goBack: String
        get() = compose.activity.getString(CoreUiR.string.go_back)

    @Before
    fun setUp() {
        startKoin { modules(module { single { CommonDataStore(ApplicationProvider.getApplicationContext()) } }) }
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun `a titled page keeps its back arrow by default`() {
        show(backButton = true, ownFrame = false)
        compose.onAllNodesWithContentDescription(goBack).assertCountEquals(1)
    }

    @Test
    fun `a titled task page drops the arrow and keeps its title`() {
        show(backButton = false, ownFrame = false)
        compose.onNodeWithText("Cleanup").assertExists()
        compose.onAllNodesWithContentDescription(goBack).assertCountEquals(0)
    }

    @Test
    fun `a page drawing its own frame drops the arrow`() {
        show(backButton = false, ownFrame = true)
        compose.onNodeWithText("Cleanup").assertExists()
        compose.onAllNodesWithContentDescription(goBack).assertCountEquals(0)
    }

    private fun show(backButton: Boolean, ownFrame: Boolean) {
        val graph = shellGraph(appTitle = CommonR.string.app_name) {
            tab(TaskHome, NavigationR.string.updates, ToolkitIcon.Vector(Icons.Outlined.Home)) {
                val navigator = LocalShellNavigator.current
                LaunchedEffect(Unit) { navigator.navigate(TaskRun) }
                Text("Home")
            }
            if (ownFrame) {
                page<TaskRun> {
                    PageScaffold(title = "Cleanup", backButton = backButton) { Text("Running") }
                }
            } else {
                page<TaskRun>(title = { "Cleanup" }, backButton = backButton) { Text("Running") }
            }
        }
        compose.setContent {
            AppTheme {
                ShellHost(
                    graph = graph,
                    start = TaskHome,
                    preferences = InMemoryShellPreferences(ShellSettings()),
                )
            }
        }
        compose.waitForIdle()
        compose.onNodeWithText("Running").assertExists()
    }
}

@Serializable
internal data object TaskHome : NavKey

@Serializable
internal data object TaskRun : NavKey
