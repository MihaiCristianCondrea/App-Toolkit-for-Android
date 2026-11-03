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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.DefaultDisplayPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.DefaultThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultDisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeAdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellPlayer
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.data.repositories.InMemoryShellPreferencesRepository
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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xhdpi")
class ShellPlayerOverlayTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        startKoin {
            modules(
                module {
                    single<AdsSettingsRepository> { FakeAdsSettingsRepository() }
                    single<DisplayPreferencesRepository> {
                        DefaultDisplayPreferencesRepository(DefaultDisplayPreferencesDataSource(FakePreferencesDataStore()))
                    }
                    single<ThemePreferencesRepository> {
                        DefaultThemePreferencesRepository(DefaultThemePreferencesDataSource(FakePreferencesDataStore()))
                    }
                },
            )
        }
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun `swiping down on collapsed mini player dismisses it and invokes onDismiss once`() {
        val harness = showPlayer(deactivateOnDismiss = true)
        val initialPadding = harness.bottomPaddingDp()

        compose.onNodeWithTag("mini-player", useUnmergedTree = true).performTouchInput {
            down(center)
            moveBy(Offset(0f, 64.dp.toPx()), delayMillis = 16)
            moveBy(Offset(0f, 16.dp.toPx()), delayMillis = 100)
            up()
        }
        compose.waitForIdle()

        assertEquals(1, harness.dismissCount)
        assertFalse(harness.active)
        compose.onAllNodesWithText("Mini player").assertCountEquals(0)
        compose.onNodeWithText("Banner").assertExists()
        assertTrue(harness.bottomPaddingDp() < initialPadding)
    }

    @Test
    fun `fast downward fling on collapsed mini player dismisses it`() {
        val harness = showPlayer(deactivateOnDismiss = true)

        // Total movement is 36.dp (~18.dp past touch slop, below the 48.dp distance threshold) at high velocity.
        compose.onNodeWithTag("mini-player", useUnmergedTree = true).performTouchInput {
            down(topCenter + Offset(0f, 4f))
            moveBy(Offset(0f, 20.dp.toPx()), delayMillis = 8)
            moveBy(Offset(0f, 16.dp.toPx()), delayMillis = 8)
            up()
        }
        compose.waitForIdle()

        assertEquals(1, harness.dismissCount)
        assertFalse(harness.active)
        compose.onAllNodesWithText("Mini player").assertCountEquals(0)
    }

    @Test
    fun `insufficient downward drag returns smoothly to docked position without invoking onDismiss`() {
        val harness = showPlayer(deactivateOnDismiss = true)
        val dockedTop = compose.onNodeWithTag("mini-player", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot.top
        val initialPadding = harness.bottomPaddingDp()

        compose.onNodeWithTag("mini-player", useUnmergedTree = true).performTouchInput {
            down(center)
            moveBy(Offset(0f, 20.dp.toPx()), delayMillis = 100)
            moveBy(Offset.Zero, delayMillis = 200)
            up()
        }
        compose.waitForIdle()

        assertEquals(0, harness.dismissCount)
        assertTrue(harness.active)
        compose.onNodeWithText("Mini player").assertExists()
        val settledTop = compose.onNodeWithTag("mini-player", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot.top
        assertEquals(dockedTop, settledTop, 1f)
        assertEquals(initialPadding, harness.bottomPaddingDp(), 0.5f)
    }

    @Test
    fun `cancelled downward drag returns to docked position without invoking onDismiss`() {
        val harness = showPlayer(deactivateOnDismiss = true)
        val dockedTop = compose.onNodeWithTag("mini-player", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot.top
        val initialPadding = harness.bottomPaddingDp()

        compose.onNodeWithTag("mini-player", useUnmergedTree = true).performTouchInput {
            down(center)
            moveBy(Offset(0f, 64.dp.toPx()), delayMillis = 16)
            cancel()
        }
        compose.waitForIdle()

        assertEquals(0, harness.dismissCount)
        assertTrue(harness.active)
        val settledTop = compose.onNodeWithTag("mini-player", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot.top
        assertEquals(dockedTop, settledTop, 1f)
        assertEquals(initialPadding, harness.bottomPaddingDp(), 0.5f)

        // Dragging down past threshold and back up before releasing also cancels dismissal.
        compose.onNodeWithTag("mini-player", useUnmergedTree = true).performTouchInput {
            down(center)
            moveBy(Offset(0f, 64.dp.toPx()), delayMillis = 50)
            moveBy(Offset(0f, -64.dp.toPx()), delayMillis = 200)
            moveBy(Offset.Zero, delayMillis = 200)
            up()
        }
        compose.waitForIdle()

        assertEquals(0, harness.dismissCount)
        assertTrue(harness.active)
        compose.onNodeWithText("Mini player").assertExists()
        compose.onAllNodesWithText("Expanded player").assertCountEquals(0)
    }

    @Test
    fun `mini player preserves child button clicks, tap to expand, and swipe up to expand`() {
        val harness = showPlayer(deactivateOnDismiss = true)

        compose.onNodeWithText("PlayToggle").performClick()
        compose.waitForIdle()
        assertEquals(1, harness.miniButtonClicks)
        assertEquals(0, harness.dismissCount)
        compose.onAllNodesWithText("Expanded player").assertCountEquals(0)

        compose.onNodeWithText("Mini player").performClick()
        compose.waitForIdle()
        compose.onNodeWithText("Expanded player").assertExists()

        compose.onNodeWithText("CollapsePlayer").performClick()
        compose.waitForIdle()
        compose.onAllNodesWithText("Expanded player").assertCountEquals(0)

        compose.onNodeWithTag("mini-player", useUnmergedTree = true).performTouchInput {
            swipeUp(startY = centerY, endY = centerY - 500.dp.toPx(), durationMillis = 150)
        }
        compose.waitForIdle()
        compose.onNodeWithText("Expanded player").assertExists()
        assertEquals(0, harness.dismissCount)
    }

    @Test
    fun `swiping down on expanded player only collapses and never dismisses even when dragged past dock`() {
        val harness = showPlayer(deactivateOnDismiss = true)

        compose.onNodeWithText("Mini player").performClick()
        compose.waitForIdle()
        compose.onNodeWithText("Expanded player").assertExists()

        // Drag far beyond the full window height from the expanded state.
        compose.onNodeWithTag("expanded-header", useUnmergedTree = true).performTouchInput {
            down(center)
            moveBy(Offset(0f, 1200.dp.toPx()), delayMillis = 150)
            up()
        }
        compose.waitForIdle()

        assertEquals(0, harness.dismissCount)
        assertTrue(harness.active)
        compose.onNodeWithText("Mini player").assertExists()
        compose.onAllNodesWithText("Expanded player").assertCountEquals(0)
    }

    @Test
    fun `back press and scrollable expanded content preserve collapse without dismissing`() {
        val harness = showPlayer(deactivateOnDismiss = true)

        compose.onNodeWithText("Mini player").performClick()
        compose.waitForIdle()
        compose.onNodeWithText("Expanded player").assertExists()

        compose.onNodeWithTag("expanded-scroll", useUnmergedTree = true).performTouchInput {
            swipeUp()
        }
        compose.waitForIdle()
        compose.onNodeWithText("Expanded player").assertExists()
        assertEquals(0, harness.dismissCount)

        compose.runOnUiThread {
            compose.activity.onBackPressedDispatcher.onBackPressed()
        }
        compose.waitForIdle()

        assertEquals(0, harness.dismissCount)
        assertTrue(harness.active)
        compose.onNodeWithText("Mini player").assertExists()
        compose.onAllNodesWithText("Expanded player").assertCountEquals(0)
    }

    @Test
    fun `omitting onDismiss retains existing non-dismissible mini player behavior`() {
        val harness = showPlayer(enableDismiss = false)
        val dockedTop = compose.onNodeWithTag("mini-player", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot.top

        val dismissNodes = compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.Dismiss)).fetchSemanticsNodes()
        assertTrue(dismissNodes.isEmpty())

        compose.onNodeWithTag("mini-player", useUnmergedTree = true).performTouchInput {
            swipeDown(startY = top, endY = bottom + 100.dp.toPx(), durationMillis = 100)
        }
        compose.waitForIdle()

        assertTrue(harness.active)
        compose.onNodeWithText("Mini player").assertExists()
        val settledTop = compose.onNodeWithTag("mini-player", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot.top
        assertEquals(dockedTop, settledTop, 1f)

        compose.onNodeWithText("Mini player").performClick()
        compose.waitForIdle()
        compose.onNodeWithText("Expanded player").assertExists()
    }

    @Test
    fun `accessibility dismiss action invokes onDismiss with localized label`() {
        val harness = showPlayer(deactivateOnDismiss = true)

        val node = compose.onNode(SemanticsMatcher.keyIsDefined(SemanticsActions.Dismiss)).fetchSemanticsNode()
        val action = node.config.getOrNull(SemanticsActions.Dismiss)
        assertEquals("Dismiss player", action?.label)

        compose.onNode(SemanticsMatcher.keyIsDefined(SemanticsActions.Dismiss))
            .performSemanticsAction(SemanticsActions.Dismiss)
        compose.waitForIdle()

        assertEquals(1, harness.dismissCount)
        assertFalse(harness.active)
        compose.onAllNodesWithText("Mini player").assertCountEquals(0)
    }

    @Test
    fun `player recovers to docked position and remains usable when host keeps player active after onDismiss`() {
        val harness = showPlayer(deactivateOnDismiss = false)
        val dockedTop = compose.onNodeWithTag("mini-player", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot.top

        compose.onNodeWithTag("mini-player", useUnmergedTree = true).performTouchInput {
            down(center)
            moveBy(Offset(0f, 64.dp.toPx()), delayMillis = 16)
            moveBy(Offset(0f, 16.dp.toPx()), delayMillis = 100)
            up()
        }
        compose.waitForIdle()

        assertEquals(1, harness.dismissCount)
        assertTrue(harness.active)
        val recoveredTop = compose.onNodeWithTag("mini-player", useUnmergedTree = true).fetchSemanticsNode().boundsInRoot.top
        assertEquals(dockedTop, recoveredTop, 1f)

        // Player remains interactive: can still be dismissed again or tapped to expand.
        compose.onNodeWithTag("mini-player", useUnmergedTree = true).performTouchInput {
            down(center)
            moveBy(Offset(0f, 64.dp.toPx()), delayMillis = 16)
            moveBy(Offset(0f, 16.dp.toPx()), delayMillis = 100)
            up()
        }
        compose.waitForIdle()
        assertEquals(2, harness.dismissCount)

        compose.onNodeWithText("Mini player").performClick()
        compose.waitForIdle()
        compose.onNodeWithText("Expanded player").assertExists()
    }

    @Test
    fun `reduced motion preserves expand collapse and dismiss behavior`() {
        android.provider.Settings.Global.putFloat(
            compose.activity.contentResolver,
            android.provider.Settings.Global.ANIMATOR_DURATION_SCALE,
            0f,
        )
        try {
            val harness = showPlayer(deactivateOnDismiss = true)

            compose.onNodeWithText("Mini player").performClick()
            compose.waitForIdle()
            compose.onNodeWithText("Expanded player").assertExists()

            compose.onNodeWithText("CollapsePlayer").performClick()
            compose.waitForIdle()
            compose.onNodeWithText("Mini player").assertExists()

            compose.onNodeWithTag("mini-player", useUnmergedTree = true).performTouchInput {
                down(center)
                moveBy(Offset(0f, 64.dp.toPx()), delayMillis = 16)
                moveBy(Offset(0f, 16.dp.toPx()), delayMillis = 16)
                up()
            }
            compose.waitForIdle()

            assertEquals(1, harness.dismissCount)
            assertFalse(harness.active)
            compose.onAllNodesWithText("Mini player").assertCountEquals(0)
        } finally {
            android.provider.Settings.Global.putFloat(
                compose.activity.contentResolver,
                android.provider.Settings.Global.ANIMATOR_DURATION_SCALE,
                1f,
            )
        }
    }

    private class PlayerTestHarness {
        var active by mutableStateOf(value = true)
        var dismissCount by mutableIntStateOf(value = 0)
        var miniButtonClicks by mutableIntStateOf(value = 0)
        var currentBottomPaddingDp: Float = 0f

        fun bottomPaddingDp(): Float = currentBottomPaddingDp
    }

    private fun showPlayer(
        enableDismiss: Boolean = true,
        deactivateOnDismiss: Boolean = true,
    ): PlayerTestHarness {
        val harness = PlayerTestHarness()
        val icon = ToolkitIcon.Vector(Icons.Outlined.Home)
        val graph = shellGraph(appTitle = CommonR.string.app_name) {
            tab(PlayerPrimaryTabKey, NavigationR.string.updates, icon) {
                harness.currentBottomPaddingDp = contentPadding().calculateBottomPadding().value
                Text("Tab one content")
            }
            tab(PlayerSecondaryTabKey, NavigationR.string.help_and_feedback, icon) {
                Text("Tab two content")
            }
            banner { Text("Banner") }
            player(
                ShellPlayer(
                    isActive = { harness.active },
                    mini = {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("mini-player")
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text("Mini player")
                            Button(onClick = { harness.miniButtonClicks++ }) {
                                Text("PlayToggle")
                            }
                        }
                    },
                    onDismiss = if (enableDismiss) {
                        {
                            harness.dismissCount++
                            if (deactivateOnDismiss) {
                                harness.active = false
                            }
                        }
                    } else {
                        null
                    },
                    expanded = { collapse ->
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("expanded-header")
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text("Expanded player")
                                Button(onClick = collapse) {
                                    Text("CollapsePlayer")
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("expanded-scroll")
                                    .verticalScroll(rememberScrollState()),
                            ) {
                                repeat(30) { index ->
                                    Text("Track row $index", modifier = Modifier.padding(12.dp))
                                }
                            }
                        }
                    },
                ),
            )
        }
        compose.setContent {
            AppTheme {
                ShellHost(
                    graph = graph,
                    preferences = InMemoryShellPreferencesRepository(ShellSettings()),
                )
            }
        }
        compose.waitForIdle()
        return harness
    }
}

@Serializable
internal data object PlayerPrimaryTabKey : NavKey

@Serializable
internal data object PlayerSecondaryTabKey : NavKey
