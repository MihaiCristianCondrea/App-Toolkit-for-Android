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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolWorkspace
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w430dp-h800dp")
class ToolWorkspaceInsetsTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test fun visibleRowsStayInsideTheShellInsetsBeforeAndAfterScrolling() {
        compose.setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    ToolWorkspace(PaddingValues(top = 48.dp, bottom = 96.dp)) {
                        repeat(16) { Box(Modifier.fillMaxWidth().height(96.dp).testTag("row")) }
                    }
                }
            }
        }
        compose.waitForIdle()
        assertRowsClearBars()
        compose.onNode(hasScrollAction()).performTouchInput { swipeUp() }
        compose.waitForIdle()
        assertRowsClearBars()
    }

    private fun assertRowsClearBars() {
        val root = compose.onRoot().fetchSemanticsNode().boundsInRoot
        val top = with(compose.density) { 48.dp.toPx() }
        val bottom = root.bottom - with(compose.density) { 96.dp.toPx() }
        val visibleRows = compose.onAllNodesWithTag("row").fetchSemanticsNodes()
            .map { it.boundsInRoot }.filter { it.width > 0f && it.height > 0f }
        assertTrue(visibleRows.isNotEmpty())
        for (bounds in visibleRows) {
            assertTrue(bounds.top >= top - 1f, "Row overlaps the top bar: $bounds")
            assertTrue(bounds.bottom <= bottom + 1f, "Row overlaps the bottom bars: $bounds")
        }
    }
}
