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

import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.test.performTouchInput
import org.junit.Assert.assertTrue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.ScaffoldFabs
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.FabSize
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.FabColor
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation3.runtime.NavKey
import androidx.navigationevent.DirectNavigationEventInput
import androidx.navigationevent.NavigationEvent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.LocalShowBottomBarLabels
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SupportRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BackEdgeStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.TopBarOverride
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import kotlinx.serialization.Serializable
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.R as DesignSystemR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR

/**
 * Renders the shell's chrome in every navigation layout, against the Toolkit's theme, and the
 * states worth reviewing by eye: the drawer, the overflow menu, list and detail, and the frames of
 * the predictive back animation.
 *
 * `./gradlew :library:shell:recordRoborazziDebug` rewrites the images in `src/test/screenshots`;
 * `./gradlew :library:shell:verifyRoborazziDebug` fails when the chrome no longer matches them.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = PHONE)
class ShellScreenshotTest {

    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    // The Toolkit's theme reads the person's theme settings from the common data store in Koin.
    @Before
    fun setUpKoin() {
        startKoin {
            modules(module { single { CommonDataStore(ApplicationProvider.getApplicationContext()) } })
        }
    }

    @After
    fun tearDownKoin() {
        stopKoin()
    }

    @Test
    fun phone_home() {
        show()
        capture("phone_home")
    }

    @Test
    fun phone_labels_on_the_selected_item_only() {
        show(showAllLabels = false)
        capture("phone_labels_selected")
    }

    @Test
    fun phone_short_navigation_bar() {
        show(ShellSettings(navigationBarStyle = NavigationBarStyle.Short))
        capture("phone_short_navigation_bar")
    }

    @Test
    fun phone_center_aligned_top_bar() {
        show(ShellSettings(topBarOverride = TopBarOverride.CenterAligned))
        capture("phone_center_aligned")
    }

    @Test
    fun phone_large_top_bar_and_fab() {
        show()
        compose.onNodeWithText("Updates").performClick()
        compose.waitForIdle()
        capture("phone_updates")
    }

    @Test
    fun a_screen_declares_its_own_floating_action_button_and_it_leaves_with_the_screen() {
        show()
        compose.onNodeWithText("Item 2").performClick()
        compose.waitForIdle()
        // The label is merged into the button's semantics.
        compose.onNodeWithText("Item action", useUnmergedTree = true).assertExists()

        compose.onNodeWithContentDescription("Go back").performClick()
        compose.waitForIdle()
        compose.onAllNodesWithText("Item action", useUnmergedTree = true).assertCountEquals(0)
    }

    @Test
    fun phone_drawer() {
        show()
        compose.onNodeWithContentDescription("Open navigation").performClick()
        compose.waitForIdle()
        capture("phone_drawer")
    }

    @Test
    fun phone_overflow_menu() {
        show()
        compose.onNodeWithContentDescription("More options").performClick()
        compose.waitForIdle()
        capture("phone_overflow_menu")
    }

    @Test
    fun phone_child_keeps_bars() {
        show()
        compose.onNodeWithText("Item 1").performClick()
        compose.waitForIdle()
        capture("phone_child")
    }

    @Test
    fun phone_app_bar_buttons_move_with_a_child() {
        show()
        compose.mainClock.autoAdvance = false
        compose.onNodeWithText("Item 1").performClick()
        // Mid-way, the menu button has crossfaded into a back arrow in place and the overflow
        // button is sliding out to the end.
        compose.mainClock.advanceTimeUntil(timeoutMillis = 2_000) {
            compose.onAllNodesWithContentDescription("Go back").fetchSemanticsNodes().isNotEmpty()
        }
        compose.mainClock.advanceTimeBy(120)
        capture("phone_app_bar_buttons_moving")
        compose.mainClock.autoAdvance = true
        compose.waitForIdle()
        compose.onAllNodesWithContentDescription("More options").assertCountEquals(0)

        compose.onNodeWithContentDescription("Go back").performClick()
        compose.waitForIdle()
        compose.onNodeWithContentDescription("More options").assertExists()
        compose.onNodeWithContentDescription("Open navigation").assertExists()
    }

    @Test
    fun phone_list_page() {
        show()
        openSettings()
        capture("phone_list_page")
    }

    @Test
    fun phone_page_opening_mid_transition() {
        show()
        compose.onNodeWithContentDescription("Open navigation").performClick()
        compose.waitForIdle()
        compose.mainClock.autoAdvance = false
        compose.onNodeWithText("Settings").performClick()
        compose.mainClock.advanceTimeBy(120)
        capture("phone_page_opening")
    }

    @Test
    fun phone_page_edge_to_edge() {
        show()
        simulateSystemBars()
        openSettings()
        capture("phone_page_edge_to_edge")
    }

    @Test
    fun phone_predictive_back_mid_gesture() {
        show()
        openSettings()
        backGesture(edge = NavigationEvent.EDGE_LEFT, touchX = 200f, progress = 0.5f)
        capture("phone_predictive_back")
    }

    @Test
    fun phone_predictive_back_released_early() {
        show()
        openSettings()
        val gesture = backGesture(edge = NavigationEvent.EDGE_LEFT, touchX = 120f, touchY = 900f, progress = 0.25f)
        compose.mainClock.autoAdvance = false
        compose.runOnUiThread { gesture.backCompleted() }
        // A third of the way into the post-commit phase, continued from where the finger let go.
        compose.mainClock.advanceTimeBy(150)
        capture("phone_predictive_back_commit")
    }

    @Test
    fun phone_back_button_press() {
        show()
        openSettings()
        // Three-button navigation on Android 16 reports no edge and a progress the system
        // animates while the button is held.
        val button = backGesture(edge = NavigationEvent.EDGE_NONE, touchX = 0f, touchY = 0f, progress = 0.1f)
        compose.mainClock.autoAdvance = false
        compose.runOnUiThread { button.backCompleted() }
        compose.mainClock.advanceTimeBy(100)
        capture("phone_back_button_press")
    }

    @Test
    fun phone_back_swipe_from_the_right_following_the_finger() {
        show()
        openSettings()
        backGesture(edge = NavigationEvent.EDGE_RIGHT, startX = 820f, touchX = 620f, progress = 0.5f)
        capture("phone_back_from_right_follow")
    }

    @Test
    fun phone_back_swipe_from_the_right_as_android() {
        show(ShellSettings(backEdgeStyle = BackEdgeStyle.System))
        openSettings()
        backGesture(edge = NavigationEvent.EDGE_RIGHT, startX = 820f, touchX = 620f, progress = 0.5f)
        capture("phone_back_from_right")
    }

    @Test
    @Config(qualifiers = RAIL)
    fun rail() {
        show()
        capture("rail_home")
    }

    @Test
    @Config(qualifiers = RAIL)
    fun rail_opened_over_content() {
        show()
        compose.onNodeWithContentDescription("Expand navigation").performClick()
        compose.waitForIdle()
        capture("rail_modal")
    }

    @Test
    @Config(qualifiers = EXPANDED)
    fun expanded_rail() {
        show()
        capture("expanded_rail_home")
    }

    @Test
    @Config(qualifiers = EXPANDED)
    fun expanded_rail_untinted() {
        show(ShellSettings(navigationTint = NavigationTint.None))
        capture("expanded_rail_untinted")
    }

    @Test
    @Config(qualifiers = EXPANDED)
    fun expanded_detail_beside_the_list() {
        show()
        openSettingsDetail()
        capture("expanded_list_detail")
    }

    @Test
    @Config(qualifiers = EXPANDED_NIGHT)
    fun expanded_detail_beside_the_list_in_the_dark_theme() {
        // Every title and row must take the dark theme's light text, including the list-detail
        // scene's own app bar, which is drawn outside any Material surface.
        show()
        openSettingsDetail()
        capture("expanded_list_detail_dark")
    }

    @Test
    @Config(qualifiers = EXPANDED)
    fun expanded_page_opened_from_the_drawer_looks_like_a_tab() {
        show()
        compose.onNodeWithText("Help & feedback").performClick()
        compose.waitForIdle()
        // No way back but the navigation beside it.
        compose.onAllNodesWithContentDescription("Go back").assertCountEquals(0)
        capture("expanded_top_level_page")
    }

    @Test
    @Config(qualifiers = EXPANDED)
    fun a_page_swapped_for_another_leaves_without_growing_a_back_button() {
        show()
        compose.onNodeWithText("Help & feedback").performClick()
        compose.waitForIdle()
        compose.mainClock.autoAdvance = false
        compose.onNodeWithText("Updates").performClick()
        // Mid-swap, both pages are drawn; the one leaving is still the drawer's page.
        repeat(6) {
            compose.mainClock.advanceTimeByFrame()
            compose.onAllNodesWithContentDescription("Go back").assertCountEquals(0)
        }
        compose.mainClock.autoAdvance = true
    }

    @Test
    fun an_extended_button_folds_while_the_list_scrolls_down_and_unfolds_going_up() {
        show()
        compose.onNodeWithText("Updates").performClick()
        compose.waitForIdle()
        compose.onNodeWithText("Check", useUnmergedTree = true).assertExists()

        // A long swipe: the large app bar takes the first part of it as it collapses.
        compose.onRoot().performTouchInput { swipeUp(startY = centerY + 500f, endY = centerY - 700f) }
        compose.waitForIdle()
        compose.onAllNodesWithText("Check", useUnmergedTree = true).assertCountEquals(0)

        compose.onRoot().performTouchInput { swipeDown(startY = centerY - 300f, endY = centerY + 300f) }
        compose.waitForIdle()
        compose.onNodeWithText("Check", useUnmergedTree = true).assertExists()
    }

    @Test
    @Config(qualifiers = EXPANDED)
    fun tablet_detail_sliding_away_under_the_back_gesture() {
        show()
        openSettingsDetail()
        backGesture(edge = NavigationEvent.EDGE_LEFT, touchX = 300f, touchY = 600f, progress = 0.5f)
        capture("tablet_slide_to_pop")
    }

    @Test
    @Config(qualifiers = LANDSCAPE)
    fun landscape_rail() {
        show()
        compose.onNodeWithText("Updates").performClick()
        compose.waitForIdle()
        capture("landscape_rail")
    }

    @Test
    @Config(qualifiers = DESKTOP)
    fun permanent_drawer() {
        show()
        capture("desktop_home")
    }

    @Test
    @Config(qualifiers = DESKTOP)
    fun desktop_detail_beside_the_list() {
        show()
        openSettingsDetail()
        capture("desktop_list_detail")
    }

    /** Hosts the test graph in the Toolkit's theme, with [settings] as the shell's options. */
    private fun show(settings: ShellSettings = ShellSettings(), showAllLabels: Boolean = true) {
        compose.setContent {
            AppTheme {
                CompositionLocalProvider(LocalShowBottomBarLabels provides showAllLabels) {
                    ShellHost(graph = TestGraph, preferences = InMemoryShellPreferences(settings))
                }
            }
        }
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("Item 1").fetchSemanticsNodes().isNotEmpty()
        }
    }

    /** Opens the settings list page from the drawer, or the rail and permanent drawer where shown. */
    @Test
    @Config(qualifiers = EXPANDED)
    fun beside_the_navigation_an_entry_replaces_the_page_and_a_tab_closes_it() {
        show()
        openSettings()
        // Settings opened beside the permanent drawer, which stays.
        compose.onNodeWithText("Privacy").assertExists()
        compose.onNodeWithText("Help & feedback").assertExists()

        compose.onNodeWithText("Help & feedback").performClick()
        compose.waitForIdle()
        compose.onAllNodesWithText("Privacy").assertCountEquals(0)
        compose.onAllNodesWithText("Support").fetchSemanticsNodes().isNotEmpty().let(::assertTrue)

        compose.onAllNodesWithText("Search")[0].performClick()
        compose.waitForIdle()
        compose.onAllNodesWithText("Support").assertCountEquals(0)
        compose.onNodeWithText("Item 1").assertExists()
    }

    private fun openSettings() {
        if (compose.onAllNodesWithContentDescription("Open navigation").fetchSemanticsNodes().isNotEmpty()) {
            compose.onNodeWithContentDescription("Open navigation").performClick()
            compose.waitForIdle()
        }
        compose.onNodeWithText("Settings").performClick()
        compose.waitForIdle()
    }

    private fun openSettingsDetail() {
        openSettings()
        compose.onNodeWithText("Display").performClick()
        compose.waitForIdle()
    }

    /** Starts a back gesture from [edge] and holds it at [progress]. */
    private fun backGesture(
        edge: Int,
        touchX: Float,
        progress: Float,
        startX: Float = 0f,
        touchY: Float = 800f,
    ): DirectNavigationEventInput {
        val gesture = DirectNavigationEventInput()
        compose.runOnUiThread {
            compose.activity.navigationEventDispatcher.addInput(gesture)
            gesture.backStarted(NavigationEvent(touchX = startX, touchY = touchY, progress = 0f, swipeEdge = edge))
            gesture.backProgressed(NavigationEvent(touchX = touchX, touchY = touchY, progress = progress, swipeEdge = edge))
        }
        compose.waitForIdle()
        return gesture
    }

    /**
     * Robolectric draws no system bars and reports no insets. This hands the window a 24dp status
     * bar and a 48dp navigation bar, so the capture shows what reaches behind them.
     */
    private fun simulateSystemBars() {
        compose.runOnUiThread {
            val density = compose.activity.resources.displayMetrics.density
            val insets = WindowInsetsCompat.Builder()
                .setInsets(WindowInsetsCompat.Type.statusBars(), Insets.of(0, (24 * density).toInt(), 0, 0))
                .setInsets(WindowInsetsCompat.Type.navigationBars(), Insets.of(0, 0, 0, (48 * density).toInt()))
                .build()
            ViewCompat.dispatchApplyWindowInsets(compose.activity.window.decorView, insets)
        }
        compose.waitForIdle()
    }

    private fun capture(name: String) {
        compose.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }
}

@Serializable
internal data object HomeKey : NavKey

@Serializable
internal data object UpdatesKey : NavKey

@Serializable
internal data class ItemKey(val name: String) : NavKey

@Serializable
internal data object SettingsListKey : NavKey

@Serializable
internal data class SettingsDetailKey(val name: String) : NavKey

/**
 * A small app: a searchable-looking home tab of items, an updates tab with a large app bar and a
 * floating action button, a child, a settings list with details, the drawer, the overflow menu and
 * a banner. Its labels reuse the Toolkit's own strings.
 */
private val TestGraph: ShellGraph = shellGraph(appTitle = CommonR.string.app_name) {
    tab(HomeKey, CoreUiR.string.search, ToolkitIcon.Vector(Icons.Outlined.Search)) {
        Rows(prefix = "Item") { name -> ItemKey(name) }
    }
    tab(
        key = UpdatesKey,
        label = NavigationR.string.updates,
        icon = ToolkitIcon.Vector(Icons.Outlined.Update),
        topBar = TopBarStyle.Large,
        // A column: a small secondary button over the main, extended one.
        fabs = {
            listOf(
                ToolkitFab(
                    icon = ToolkitIcon.Vector(Icons.Outlined.Search),
                    onClick = {},
                    contentDescription = "Search updates",
                    size = FabSize.Small,
                    color = FabColor.Secondary,
                ),
                ToolkitFab(icon = ToolkitIcon.Vector(Icons.Outlined.Update), onClick = {}, label = "Check"),
            )
        },
    ) {
        Rows(prefix = "Update") { name -> ItemKey(name) }
    }
    child<ItemKey>(title = { it.name }) { key ->
        // One screen declares a button of its own, from inside, as a screen with state would.
        if (key.name == "Item 2") {
            ScaffoldFabs(listOf(ToolkitFab(ToolkitIcon.Vector(Icons.Outlined.Info), onClick = {}, label = "Item action")))
        }
        Text(key.name, Modifier.padding(16.dp))
    }
    page<SettingsListKey>(paneRole = PaneRole.List, title = { "Settings" }) {
        Rows(prefix = null, names = listOf("Display", "Privacy", "Advanced")) { name -> SettingsDetailKey(name) }
    }
    page<SettingsDetailKey>(paneRole = PaneRole.Detail, title = { it.name }) { key ->
        Text("${key.name} settings", Modifier.padding(16.dp))
    }
    page<SupportRoute>(title = { "Support" }) { Text("Support", Modifier.padding(16.dp)) }
    drawer {
        link(SettingsListKey, NavigationR.string.settings, ToolkitIcon.AnimatedVector(DesignSystemR.drawable.anim_settings))
        link(SupportRoute, NavigationR.string.help_and_feedback, ToolkitIcon.Vector(Icons.AutoMirrored.Outlined.HelpOutline))
        spacer()
        link(SupportRoute, CoreUiR.string.about, ToolkitIcon.Vector(Icons.Outlined.Info))
    }
    overflow { supportUs() }
    banner {
        Box(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text("Banner", color = MaterialTheme.colorScheme.onTertiaryContainer)
        }
    }
}

@Composable
private fun Rows(
    prefix: String?,
    names: List<String> = (1..30).map { "$prefix $it" },
    key: (String) -> NavKey,
) {
    val navigator = LocalShellNavigator.current
    LazyColumn(contentPadding = contentPadding()) {
        items(names.size) { index ->
            Text(
                text = names[index],
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigator.navigate(key(names[index])) }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
            )
        }
    }
}

private const val PHONE = "w411dp-h891dp-xhdpi"
private const val RAIL = "w700dp-h1000dp-xhdpi"
private const val EXPANDED = "w1000dp-h760dp-xhdpi"
private const val EXPANDED_NIGHT = "w1000dp-h760dp-night-xhdpi"
private const val LANDSCAPE = "w891dp-h411dp-land-xhdpi"
private const val DESKTOP = "w1400dp-h900dp-mdpi"
