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

package com.mihaicristiancondrea.android.apps.apptoolkit

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GeneralButtonTest {
    @get:Rule val compose = createComposeRule()

    @Test fun everyStyleAdaptsAndHonorsDisabledClicks() {
        val enabled = mutableStateOf(true)
        var clicks = 0
        var haptics = 0
        val feedback = object : HapticFeedback {
            override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) { haptics++ }
        }
        compose.setContent {
            CompositionLocalProvider(LocalHapticFeedback provides feedback) {
            MaterialTheme {
                Column {
                    GeneralButtonStyle.entries.forEach { style ->
                        GeneralButton(
                            onClick = { clicks++ }, style = style, enabled = enabled.value,
                            icon = ToolkitIcon.Vector(Icons.Default.Add),
                            contentDescription = style.name,
                        )
                    }
                }
            }
        }
        }
        GeneralButtonStyle.entries.forEach { style ->
            compose.onNodeWithContentDescription(style.name).assertHasClickAction().performClick()
        }
        compose.runOnIdle { assertEquals(5, clicks); assertEquals(5, haptics); enabled.value = false }
        GeneralButtonStyle.entries.forEach { style ->
            compose.onNodeWithContentDescription(style.name).assertIsNotEnabled().performClick()
        }
        compose.runOnIdle { assertEquals(5, clicks); assertEquals(5, haptics) }
    }

    @Test fun labelledIconsAreDecorativeAndExplicitDescriptionReplacesLabel() {
        compose.setContent {
            MaterialTheme {
                Column {
                    GeneralButton(onClick = {}, label = "Save", icon = ToolkitIcon.Vector(Icons.Default.Add))
                    GeneralButton(onClick = {}, label = "Next", icon = ToolkitIcon.Vector(Icons.Default.Add),
                        iconPosition = ButtonIconPosition.End, contentDescription = "Next page")
                }
            }
        }
        compose.onNodeWithText("Save").assertHasClickAction()
        compose.onAllNodesWithContentDescription("Save", useUnmergedTree = true).assertCountEquals(0)
        compose.onNodeWithContentDescription("Next page").assertHasClickAction()
        compose.onAllNodesWithText("Next", useUnmergedTree = true).assertCountEquals(0)
    }

    @Test fun bitmapIconRendersAndKeepsButtonInteraction() {
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888).apply {
            eraseColor(android.graphics.Color.MAGENTA)
        }.asImageBitmap()
        var clicks = 0
        compose.setContent {
            MaterialTheme {
                GeneralButton(
                    onClick = { clicks++ },
                    icon = ToolkitIcon.Bitmap(imageBitmap = bitmap),
                    contentDescription = "Bitmap action",
                )
            }
        }

        compose.onNodeWithContentDescription("Bitmap action").assertHasClickAction().performClick()
        compose.runOnIdle { assertEquals(1, clicks) }
    }

    @Test fun styleMatrixRendersLabelledAndIconOnlyForms() {
        var clicks = 0
        compose.setContent {
            MaterialTheme {
                Surface {
                    Column {
                        GeneralButtonStyle.entries.forEach { style ->
                            Text(style.name)
                            Row {
                                GeneralButton(onClick = { clicks++ }, style = style, label = style.name)
                                GeneralButton(onClick = {}, style = style, label = "Next",
                                    icon = ToolkitIcon.Vector(Icons.Default.Add), iconPosition = ButtonIconPosition.End)
                                GeneralButton(onClick = {}, style = style, icon = ToolkitIcon.Vector(Icons.Default.Add),
                                    contentDescription = "${style.name} icon")
                                GeneralButton(onClick = {}, style = style, enabled = false,
                                    icon = ToolkitIcon.Vector(Icons.Default.Add), contentDescription = "${style.name} disabled")
                            }
                        }
                    }
                }
            }
        }
        GeneralButtonStyle.entries.forEach { style ->
            compose.onNode(hasText(style.name) and hasClickAction()).performClick()
        }
        compose.runOnIdle { assertEquals(5, clicks) }
        compose.waitForIdle()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val screenshot = instrumentation.uiAutomation.takeScreenshot()
        File(instrumentation.targetContext.getExternalFilesDir(null), "general-button-matrix.png")
            .outputStream().use { screenshot.compress(Bitmap.CompressFormat.PNG, 100, it) }
        screenshot.recycle()
    }

}
