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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ScaffoldArticleTopBar
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.readingProgress
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.TopBarOverride
import kotlinx.serialization.Serializable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR

/**
 * The article app bar a screen declares with `ScaffoldArticleTopBar`: in a tab's child, on a page
 * and beside a list, and what it leaves behind once the screen goes.
 *
 * The captures are reviewed like those of [ShellScreenshotTest]: record them with
 * `./gradlew :library:shell:recordRoborazziDebug`.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = ARTICLE_PHONE)
class ArticleTopBarTest {

    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        ArticleControls.compact = false
        ArticleControls.progress = null
        startKoin {
            modules(module { single { CommonDataStore(ApplicationProvider.getApplicationContext()) } })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun a_destination_that_declares_nothing_keeps_its_own_bar() {
        show()
        open("Plain")

        compose.onNodeWithText("Plain").assertExists()
        compose.onAllNodes(HasProgress).assertCountEquals(0)
    }

    @Test
    fun the_bar_shows_no_title_until_the_header_has_scrolled_away() {
        show()
        open("Story")

        compose.onAllNodesWithText("Compact Story").assertCountEquals(0)
        compose.onAllNodesWithText(StoryTitle).assertCountEquals(0)
        compose.onAllNodes(HasProgress).assertCountEquals(0)
        compose.onNodeWithContentDescription("Go back").assertExists()
        capture("phone_article_minimal")

        compose.onRoot().performTouchInput { swipeUp(startY = centerY + 400f, endY = centerY - 400f) }
        compose.waitForIdle()

        compose.onNodeWithText("Compact Story").assertExists()
        compose.onAllNodes(HasProgress).assertCountEquals(1)
        assertTrue(progressShown() > 0f)
    }

    @Test
    fun the_brand_is_drawn_before_the_title_and_leaves_no_gap_when_absent() {
        ArticleControls.compact = true
        show()
        open("Plain")
        val titleStart = compose.onNodeWithText("Plain", useUnmergedTree = true).getUnclippedBoundsInRoot().left
        back()

        open("Story")
        compose.onAllNodesWithContentDescription("Publisher").assertCountEquals(0)
        val plainStart = compose.onNodeWithText("Compact Story", useUnmergedTree = true).getUnclippedBoundsInRoot().left
        capture("phone_article_compact")
        back()

        open("Branded story")
        compose.onNodeWithContentDescription("Publisher").assertExists()
        val brandedStart = compose.onNodeWithText("Compact Branded story", useUnmergedTree = true).getUnclippedBoundsInRoot().left
        capture("phone_article_compact_brand")

        assertEquals(titleStart.value, plainStart.value, 0.5f)
        assertEquals((plainStart + BrandWidthAndSpacing).value, brandedStart.value, 0.5f)
    }

    @Test
    fun progress_is_clamped_and_reported_in_tenths() {
        ArticleControls.compact = true
        ArticleControls.progress = 0f
        show()
        open("Story")

        for ((reported, shown) in listOf(0f to 0f, 1f to 1f, 1.7f to 1f, -0.3f to 0f, 0.43f to 0.4f, Float.NaN to 0f)) {
            compose.runOnIdle { ArticleControls.progress = reported }
            compose.waitForIdle()
            assertEquals("reported $reported", shown, progressShown(), 0.001f)
        }
    }

    @Test
    fun the_progress_line_keeps_the_bar_at_its_height() {
        ArticleControls.compact = true
        ArticleControls.progress = 0.5f
        show()
        open("Plain")
        val plainTop = compose.onNodeWithText("Plain body").getUnclippedBoundsInRoot().top
        back()

        open("Story")
        val articleTop = compose.onNodeWithText("Headline Story").getUnclippedBoundsInRoot().top

        assertEquals(plainTop.value, articleTop.value, 0.5f)
    }

    @Test
    fun the_article_leaves_with_its_screen_and_never_reaches_the_next_one() {
        ArticleControls.compact = true
        ArticleControls.progress = 0.5f
        show()
        open("Story")
        compose.onNodeWithText("Compact Story").assertExists()

        back()
        compose.onAllNodesWithText("Compact Story").assertCountEquals(0)
        compose.onAllNodes(HasProgress).assertCountEquals(0)

        open("Plain")
        compose.onNodeWithText("Plain").assertExists()
        compose.onAllNodesWithText("Compact Story").assertCountEquals(0)
        compose.onAllNodes(HasProgress).assertCountEquals(0)
    }

    @Test
    fun a_hidden_bar_stays_hidden() {
        ArticleControls.compact = true
        ArticleControls.progress = 0.5f
        show(ShellSettings(topBarOverride = TopBarOverride.Hidden))
        open("Story")

        compose.onNodeWithText("Headline Story").assertExists()
        compose.onAllNodesWithText("Compact Story").assertCountEquals(0)
        compose.onAllNodes(HasProgress).assertCountEquals(0)
    }

    @Test
    fun a_large_bar_is_drawn_small_while_an_article_is_declared() {
        show(ShellSettings(topBarOverride = TopBarOverride.Large))
        open("Plain")
        val largeTop = compose.onNodeWithText("Plain body").getUnclippedBoundsInRoot().top
        back()

        open("Story")
        val articleTop = compose.onNodeWithText("Headline Story").getUnclippedBoundsInRoot().top

        assertTrue("$articleTop under a small bar, $largeTop under a large one", articleTop < largeTop - 40.dp)
    }

    @Test
    fun a_page_declares_its_article_into_its_own_frame() {
        ArticleControls.compact = true
        ArticleControls.progress = 0.5f
        show()
        open("Story page")

        compose.onNodeWithText("Compact Story page").assertExists()
        compose.onAllNodes(HasProgress).assertCountEquals(1)
        compose.onAllNodesWithText("Story page title").assertCountEquals(0)

        back()
        compose.onNodeWithText("Story").assertExists()
        compose.onAllNodesWithText("Compact Story page").assertCountEquals(0)
        compose.onAllNodes(HasProgress).assertCountEquals(0)
    }

    @Test
    @Config(qualifiers = ARTICLE_EXPANDED)
    fun beside_its_list_a_detail_shows_its_article_over_its_own_pane() {
        ArticleControls.compact = true
        ArticleControls.progress = 0.5f
        show()
        open("Stories")
        open("Story A")

        assertTrue(compose.onAllNodesWithText("Stories").fetchSemanticsNodes().isNotEmpty())
        compose.onNodeWithText("Compact Story A").assertExists()
        compose.onAllNodes(HasProgress).assertCountEquals(1)
        capture("expanded_article_detail")

        open("Story B")
        compose.onNodeWithText("Compact Story B").assertExists()
        compose.onAllNodesWithText("Compact Story A").assertCountEquals(0)
        compose.onAllNodes(HasProgress).assertCountEquals(1)
    }

    private fun show(settings: ShellSettings = ShellSettings()) {
        compose.setContent {
            AppTheme {
                ShellHost(graph = ArticleGraph, preferences = InMemoryShellPreferences(settings))
            }
        }
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("Story").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun open(row: String) {
        compose.onNodeWithText(row).performClick()
        compose.waitForIdle()
    }

    private fun back() {
        compose.onNodeWithContentDescription("Go back").performClick()
        compose.waitForIdle()
    }

    private fun progressShown(): Float =
        compose.onNode(HasProgress).fetchSemanticsNode().config[SemanticsProperties.ProgressBarRangeInfo].current

    private fun capture(name: String) {
        compose.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }
}

/** What the article screens declare, set by each test. */
private object ArticleControls {
    var compact: Boolean by mutableStateOf(false)
    var progress: Float? by mutableStateOf(null)
}

private val HasProgress = SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo)

private const val StoryTitle = "Story title"

/** A 24dp square brand and the 8dp after it. */
private val BrandWidthAndSpacing: Dp = 32.dp

@Serializable
internal data object NewsKey : NavKey

@Serializable
internal data class StoryKey(val name: String, val branded: Boolean = false) : NavKey

@Serializable
internal data object PlainKey : NavKey

@Serializable
internal data object StoryPageKey : NavKey

@Serializable
internal data object StoriesKey : NavKey

@Serializable
internal data class StoryDetailKey(val name: String) : NavKey

private val ArticleGraph: ShellGraph = shellGraph(appTitle = CommonR.string.app_name) {
    tab(NewsKey, NavigationR.string.updates, ToolkitIcon.Vector(Icons.Outlined.Home)) {
        Links(
            "Story" to StoryKey("Story"),
            "Branded story" to StoryKey("Branded story", branded = true),
            "Plain" to PlainKey,
            "Story page" to StoryPageKey,
            "Stories" to StoriesKey,
        )
    }
    child<StoryKey>(title = { StoryTitle }) { key -> ArticleBody(key.name, key.branded) }
    child<PlainKey>(title = { "Plain" }) { Text("Plain body", Modifier.padding(16.dp)) }
    page<StoryPageKey>(title = { "Story page title" }) { ArticleBody("Story page", branded = false) }
    page<StoriesKey>(paneRole = PaneRole.List, title = { "Stories" }) {
        Links("Story A" to StoryDetailKey("Story A"), "Story B" to StoryDetailKey("Story B"))
    }
    page<StoryDetailKey>(paneRole = PaneRole.Detail, title = { it.name }) { key -> ArticleBody(key.name, branded = false) }
}

/** An article: a headline, then paragraphs, declaring the article bar as a real screen would. */
@Composable
private fun ArticleBody(name: String, branded: Boolean) {
    val listState = rememberLazyListState()
    val brand = if (branded) rememberVectorPainter(Icons.Outlined.Info) else null
    ScaffoldArticleTopBar(
        title = "Compact $name",
        compact = { ArticleControls.compact || listState.firstVisibleItemIndex > 0 },
        progress = { ArticleControls.progress ?: listState.readingProgress() },
        brand = brand,
        brandContentDescription = if (branded) "Publisher" else null,
    )
    LazyColumn(state = listState, contentPadding = contentPadding()) {
        item {
            Text("Headline $name", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
        }
        items(40) { index -> Text("Paragraph ${index + 1}", Modifier.padding(16.dp)) }
    }
}

@Composable
private fun Links(vararg links: Pair<String, NavKey>) {
    val navigator = LocalShellNavigator.current
    Column {
        links.forEach { (label, key) ->
            Text(
                text = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigator.navigate(key) }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
            )
        }
    }
}

private const val ARTICLE_PHONE = "w411dp-h891dp-xhdpi"
private const val ARTICLE_EXPANDED = "w1000dp-h760dp-xhdpi"
