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

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.LocalShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellPlayer
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import kotlinx.serialization.Serializable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xhdpi")
class SingleTabShellTest {
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
    fun `existing single tab hosts keep their bar and banner by default`() {
        show(hideSingleTabBottomBar = false)

        compose.onNode(hasText("Updates") and hasClickAction()).assertExists()
        compose.onNodeWithText("Banner").assertExists()
        compose.onNodeWithText("bottom navigation: true").assertExists()
        capture("phone-default")
    }

    @Test
    fun `phone omits the bar and banner but keeps the FAB and drawer navigation`() {
        show()

        compose.onAllNodesWithText("Updates").assertCountEquals(0)
        compose.onAllNodesWithText("Banner").assertCountEquals(0)
        compose.onNodeWithText("Run", useUnmergedTree = true).assertExists()
        compose.onNodeWithText("bottom navigation: false").assertExists()
        capture("phone-single-tab")
        compose.onNodeWithContentDescription("Open navigation").performClick()
        compose.onAllNodesWithText("Updates").assertCountEquals(0)
        compose.onNodeWithText("Settings").performClick()
        compose.onNodeWithText("Preferences").assertExists()
        compose.onNodeWithContentDescription("Go back").performClick()
        compose.onNodeWithText("Single tab content").assertExists()
    }

    @Test
    fun `short bar and forced bottom layout also honor the host policy`() {
        show(settings = ShellSettings(layoutMode = ShellLayoutMode.BottomBar, navigationBarStyle = NavigationBarStyle.Short))

        compose.onAllNodesWithText("Updates").assertCountEquals(0)
        compose.onAllNodesWithText("Banner").assertCountEquals(0)
        compose.onNodeWithContentDescription("Open navigation").assertExists()
    }

    @Test
    fun `the hidden bar reserves only the system navigation inset for content`() {
        show(settings = ShellSettings(hideBottomBarOnScroll = false))
        compose.runOnUiThread {
            val density = compose.activity.resources.displayMetrics.density
            val insets = WindowInsetsCompat.Builder()
                .setInsets(WindowInsetsCompat.Type.statusBars(), Insets.of(0, (24 * density).toInt(), 0, 0))
                .setInsets(WindowInsetsCompat.Type.navigationBars(), Insets.of(0, 0, 0, (48 * density).toInt()))
                .build()
            ViewCompat.dispatchApplyWindowInsets(compose.activity.window.decorView, insets)
        }
        compose.waitForIdle()
        compose.onNodeWithText("bottom padding: 48.0").assertExists()
        compose.onAllNodesWithText("Updates").assertCountEquals(0)
        assertClearOfSystemNavigation("Run")
        capture("phone-system-insets")
    }

    @Test
    fun `adding a second tab restores navigation and its banner`() {
        show(secondTab = true)

        compose.onNode(hasText("Updates") and hasClickAction()).assertExists()
        compose.onNodeWithText("Banner").assertExists()
        compose.onNodeWithText("Help & feedback").performClick()
        compose.onNodeWithText("Second tab content").assertExists()
    }

    @Test
    @Config(qualifiers = "w700dp-h1000dp-xhdpi")
    fun `rail retains the single tab destination`() {
        show()
        compose.onNode(hasText("Updates") and hasClickAction()).assertExists()
        compose.onNodeWithContentDescription("Expand navigation").assertExists()
        capture("single-tab-rail")
    }

    @Test
    @Config(qualifiers = "w900dp-h1000dp-xhdpi")
    fun `expanded rail retains the single tab destination`() {
        show()
        compose.onNode(hasText("Updates") and hasClickAction()).assertExists()
        compose.onNodeWithContentDescription("Expand navigation").performClick()
        compose.onNodeWithContentDescription("Collapse navigation").assertExists()
        compose.onNode(hasText("Updates") and hasClickAction()).assertExists()
    }

    @Test
    @Config(qualifiers = "w1400dp-h900dp-mdpi")
    fun `permanent drawer retains the single tab and switches back from a page`() {
        show()
        compose.onNode(hasText("Updates") and hasClickAction()).assertExists()
        compose.onNodeWithText("Settings").performClick()
        compose.onNodeWithText("Preferences").assertExists()
        compose.onNode(hasText("Updates") and hasClickAction()).performClick()
        compose.onNodeWithText("Single tab content").assertExists()
        compose.onAllNodesWithText("Preferences").assertCountEquals(0)
        capture("single-tab-permanent-drawer")
    }

    @Test
    fun `the player remains available without a bottom navigation bar`() {
        show(player = true)
        compose.onNodeWithText("Mini player").assertExists()
        compose.onAllNodesWithText("Updates").assertCountEquals(0)
        compose.onAllNodesWithText("Banner").assertCountEquals(0)
        capture("single-tab-player")
    }

    private fun assertClearOfSystemNavigation(label: String) {
        val bottom = compose.onNodeWithText(label, useUnmergedTree = true).fetchSemanticsNode().boundsInRoot.bottom
        val windowBottom = compose.onRoot().fetchSemanticsNode().boundsInRoot.bottom
        val density = compose.activity.resources.displayMetrics.density
        assertTrue("$label overlaps Android's navigation area", bottom <= windowBottom - 48 * density)
    }

    private fun capture(name: String) {
        val file = File("build/reports/single-tab/$name.png")
        file.parentFile?.mkdirs()
        file.outputStream().use { stream ->
            compose.onRoot().captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
    }

    private fun show(
        hideSingleTabBottomBar: Boolean = true,
        secondTab: Boolean = false,
        player: Boolean = false,
        settings: ShellSettings = ShellSettings(),
    ) {
        val icon = ToolkitIcon.Vector(Icons.Outlined.Home)
        val graph = shellGraph(appTitle = CommonR.string.app_name) {
            tab(SingleTabKey, NavigationR.string.updates, icon,
                fabs = { listOf(ToolkitFab(icon = icon, onClick = {}, label = "Run")) },
            ) { SingleTabContent() }
            if (secondTab) tab(OtherTabKey, NavigationR.string.help_and_feedback, icon) { Text("Second tab content") }
            page<SingleTabSettingsKey>(title = { "Preferences" }) { Text("Settings content") }
            drawer { link(SingleTabSettingsKey, NavigationR.string.settings, icon) }
            banner { Text("Banner") }
            if (player) player(ShellPlayer(isActive = { true }, mini = { Text("Mini player") }, expanded = { Text("Expanded player") }))
        }
        compose.setContent {
            AppTheme {
                ShellHost(
                    graph = graph,
                    preferences = InMemoryShellPreferences(settings),
                    layoutPolicy = ShellLayoutPolicy(hideSingleTabBottomBar = hideSingleTabBottomBar),
                )
            }
        }
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("Single tab content").fetchSemanticsNodes().isNotEmpty()
        }
    }
}

@Composable
private fun SingleTabContent() {
    Column {
        Text("Single tab content")
        Text("bottom navigation: ${LocalShellCapabilities.current.usesBottomNavigation}")
        Text("bottom padding: ${contentPadding().calculateBottomPadding().value}")
    }
}

@Serializable
internal data object SingleTabKey : NavKey

@Serializable
internal data object OtherTabKey : NavKey

@Serializable
internal data object SingleTabSettingsKey : NavKey
