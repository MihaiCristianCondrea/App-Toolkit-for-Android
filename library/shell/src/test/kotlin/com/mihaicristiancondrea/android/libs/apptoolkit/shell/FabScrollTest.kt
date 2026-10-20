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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.ScaffoldFabs
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
import kotlinx.serialization.Serializable
import org.junit.After
import org.junit.Assert.assertEquals
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

/**
 * A page short enough that collapsing its large bar takes the whole swipe, as the help page is:
 * its extended button still folds to its icon, and unfolds when the bar expands again.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xhdpi")
class FabScrollTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        startKoin { modules(module { single { CommonDataStore(ApplicationProvider.getApplicationContext()) } }) }
    }

    @After
    fun tearDown() = stopKoin()

    /** The width of the one button in the bottom half: an extended button folds to its icon's. */
    private fun fabWidth(): Float {
        val middle = compose.onRoot().fetchSemanticsNode().boundsInRoot.height / 2
        return compose.onAllNodes(hasClickAction()).fetchSemanticsNodes()
            .single { it.boundsInRoot.top > middle }
            .boundsInRoot.width
    }

    @Test
    fun `a page's extended button folds while its large bar collapses`() {
        val graph = shellGraph(appTitle = CommonR.string.app_name) {
            tab(FabHome, NavigationR.string.updates, ToolkitIcon.Vector(Icons.Outlined.Home)) { Text("Home") }
            page<FabPage>(title = { "Help" }, topBar = TopBarStyle.Large) {
                ScaffoldFabs(listOf(ToolkitFab(ToolkitIcon.Vector(Icons.Outlined.RateReview), onClick = {}, label = "Feedback")))
                LazyColumn(Modifier.testTag("questions"), contentPadding = contentPadding()) {
                    items(8) { index -> Text("Question $index", Modifier.fillMaxWidth().height(60.dp)) }
                }
            }
        }
        compose.setContent {
            AppTheme { ShellHost(graph = graph, start = FabPage, preferences = InMemoryShellPreferences()) }
        }
        compose.waitForIdle()
        val extended = fabWidth()

        compose.onNodeWithTag("questions").performTouchInput { swipeUp(startY = height * 0.8f, endY = height * 0.2f) }
        compose.waitForIdle()
        assertTrue("The button kept its label while the bar collapsed", fabWidth() < extended)

        compose.onNodeWithTag("questions").performTouchInput { swipeDown(startY = height * 0.2f, endY = height * 0.8f) }
        compose.waitForIdle()
        assertEquals(extended, fabWidth())
    }
}

@Serializable
internal data object FabHome : NavKey

@Serializable
internal data object FabPage : NavKey
