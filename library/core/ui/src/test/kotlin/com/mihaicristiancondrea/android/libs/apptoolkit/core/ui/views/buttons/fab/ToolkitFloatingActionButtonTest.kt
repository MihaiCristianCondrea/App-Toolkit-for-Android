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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** What a screen reader announces for a button: its merged description and text. */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class ToolkitFloatingActionButtonTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val icon = ToolkitIcon.Vector(Icons.Outlined.RateReview)

    private fun announced(): List<String> {
        val config = compose.onNode(hasClickAction()).fetchSemanticsNode().config
        return config.getOrNull(SemanticsProperties.ContentDescription).orEmpty() +
            config.getOrNull(SemanticsProperties.Text).orEmpty().map { it.text }
    }

    @Test
    fun `an extended button is named by its label, expanded or folded`() {
        var expanded by mutableStateOf(true)
        compose.setContent {
            MaterialTheme { ToolkitFloatingActionButton(ToolkitFab(icon, onClick = {}, label = "Feedback"), expanded = expanded) }
        }
        assertEquals(listOf("Feedback"), announced())

        expanded = false
        compose.waitForIdle()
        assertEquals(listOf("Feedback"), announced())
    }

    @Test
    fun `a description replaces the label for accessibility`() {
        compose.setContent {
            MaterialTheme {
                ToolkitFloatingActionButton(ToolkitFab(icon, onClick = {}, label = "Feedback", contentDescription = "Send feedback"))
            }
        }
        assertEquals(listOf("Send feedback"), announced())
    }

    @Test
    fun `an icon-only button is named by its description`() {
        compose.setContent {
            MaterialTheme { ToolkitFloatingActionButton(ToolkitFab(icon, onClick = {}, contentDescription = "Feedback")) }
        }
        assertEquals(listOf("Feedback"), announced())
    }
}
