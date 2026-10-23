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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeAdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultDisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.PageScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarScrollMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import kotlinx.serialization.Serializable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xhdpi")
class StaticTopBarTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private lateinit var listState: LazyListState

    @Before
    fun setUp() {
        startKoin { modules(module { single { CommonDataStore(ApplicationProvider.getApplicationContext()) }
                    single<AdsSettingsRepository> { FakeAdsSettingsRepository() }
                    single<DisplayPreferencesRepository> { DefaultDisplayPreferencesRepository(get<CommonDataStore>().displayPreferences) }
                    single<ThemePreferencesRepository> { DefaultThemePreferencesRepository(get<CommonDataStore>().themePreferences) } }) }
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun `a static child keeps its bar in place and unchanged while the content scrolls`() {
        show(page = false)
        assertStaticBar()
    }

    @Test
    fun `a static large collapsed bar stays expanded`() {
        show(page = false, style = TopBarStyle.LargeCollapsed)
        assertStaticBar()
    }

    @Test
    fun `a static page the shell frames ignores hide on scroll and tint`() {
        show(page = true)
        assertStaticBar()
    }

    @Test
    fun `a page that draws its own PageScaffold follows the destination's static mode`() {
        show(page = true, ownFrame = true)
        assertStaticBar()
    }

    @Test
    @Config(qualifiers = "w840dp-h900dp-mdpi")
    fun `a static child beside a rail leaves the navigation's color unchanged`() {
        show(page = false)
        assertStaticBar()
    }

    @Test
    fun `a default child still hides its bar on scroll`() {
        show(page = false, mode = TopBarScrollMode.Default)
        val before = barImage()
        scroll()
        assertFalse("The default bar no longer follows the scroll", before.sameAs(barImage()))
    }

    private fun assertStaticBar() {
        val bounds = compose.onNodeWithText("Static review").fetchSemanticsNode().boundsInRoot
        val before = barImage()
        scroll()
        assertEquals(bounds, compose.onNodeWithText("Static review").fetchSemanticsNode().boundsInRoot)
        assertTrue("Scrolling moved or tinted the app bar or the frame", before.sameAs(barImage()))
    }

    private fun scroll() {
        compose.onNodeWithTag("files").performTouchInput { swipeUp(startY = height * 0.7f, endY = height * 0.2f) }
        compose.waitForIdle()
        compose.runOnIdle { assertTrue("The file list did not scroll", listState.firstVisibleItemIndex > 0) }
    }

    /** The screen above the title's bottom: the bar and the frame beside it, not the moving list. */
    private fun barImage(): Bitmap {
        val title = compose.onNodeWithText("Static review").fetchSemanticsNode().boundsInRoot
        val root = compose.onRoot().captureToImage().asAndroidBitmap()
        return Bitmap.createBitmap(root, 0, 0, root.width, title.bottom.toInt().coerceIn(1, root.height))
    }

    private fun show(
        page: Boolean,
        ownFrame: Boolean = false,
        mode: TopBarScrollMode = TopBarScrollMode.Static,
        style: TopBarStyle = TopBarStyle.CenterAligned,
    ) {
        val graph = shellGraph(appTitle = CommonR.string.app_name) {
            tab(StaticHome, NavigationR.string.updates, ToolkitIcon.Vector(Icons.Outlined.Home)) {
                val navigator = LocalShellNavigator.current
                if (!page) LaunchedEffect(Unit) { navigator.navigate(StaticReview) }
                Text("Home")
            }
            when {
                !page -> child<StaticReview>(title = { "Static review" }, topBar = style, topBarScrollMode = mode) { Files() }
                ownFrame -> page<StaticReview>(topBarScrollMode = mode) {
                    PageScaffold(title = "Static review", style = TopBarStyle.CenterAligned) { Files() }
                }
                else -> page<StaticReview>(title = { "Static review" }, topBar = style, topBarScrollMode = mode) { Files() }
            }
        }
        compose.setContent {
            AppTheme {
                ShellHost(
                    graph = graph,
                    start = if (page) StaticReview else StaticHome,
                    preferences = InMemoryShellPreferences(ShellSettings(hideTopBarOnScroll = true, hideBottomBarOnScroll = false)),
                )
            }
        }
        compose.waitForIdle()
    }

    @Composable
    private fun Files() {
        listState = rememberLazyListState()
        LazyColumn(Modifier.testTag("files"), state = listState, contentPadding = contentPadding()) {
            items(100) { index -> Text("File $index", Modifier.fillMaxWidth().height(60.dp)) }
        }
    }
}

@Serializable
internal data object StaticHome : NavKey

@Serializable
internal data object StaticReview : NavKey
