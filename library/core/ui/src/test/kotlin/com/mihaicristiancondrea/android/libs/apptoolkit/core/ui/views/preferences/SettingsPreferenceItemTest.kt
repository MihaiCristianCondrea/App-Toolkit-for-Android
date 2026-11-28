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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import kotlin.math.abs
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class SettingsPreferenceItemTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `a row draws any ToolkitIcon and reports its click`() {
        var clicks = 0
        compose.setContent {
            SettingsPreferenceItem(
                icon = ToolkitIcon.Resource(android.R.drawable.ic_menu_info_details),
                title = "Display",
                summary = "Theme and layout",
                useIconContainer = true,
                onClick = { clicks++ },
            )
        }

        compose.onNodeWithText("Theme and layout").assertExists()
        compose.onNodeWithText("Display").performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun `a row still takes an ImageVector`() {
        var clicks = 0
        compose.setContent {
            PreferenceItem(icon = Icons.Outlined.Home, title = "Home", onClick = { clicks++ })
        }

        compose.onNodeWithText("Home").performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun `switch preference toggles once per click and exposes switch semantics`() {
        val values = mutableListOf<Boolean>()
        compose.setContent {
            SwitchPreferenceItem(
                title = "Notifications",
                summary = "Daily reminders",
                checked = false,
                onCheckedChange = { values.add(it) },
            )
        }

        compose.onNodeWithText("Notifications")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch))
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ToggleableState,
                    ToggleableState.Off,
                ),
            )
            .performClick()

        assertEquals(listOf(true), values)
    }

    @Test
    fun `switch preference keeps its 80 dp row height`() {
        compose.setContent {
            SwitchPreferenceItem(
                title = "Notifications",
                checked = false,
                onCheckedChange = {},
            )
        }

        val rowBounds = compose.onNodeWithText("Notifications").getUnclippedBoundsInRoot()

        assertTrue(
            "Expected row height ${rowBounds.bottom - rowBounds.top} to be at least 80 dp",
            rowBounds.bottom - rowBounds.top >= 80.dp,
        )
    }

    @Test
    fun `checkbox preference toggles once per click`() {
        val values = mutableListOf<Boolean>()
        compose.setContent {
            CheckBoxPreferenceItem(
                title = "Auto update",
                summary = "Download on Wi-Fi",
                checked = false,
                onCheckedChange = { values.add(it) },
            )
        }

        compose.onNodeWithText("Auto update")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox))
            .performClick()

        assertEquals(listOf(true), values)
    }

    @Test
    fun `radio preference selects once per click`() {
        var selections = 0
        compose.setContent {
            RadioButtonPreferenceItem(
                text = "English",
                selected = false,
                onSelect = { selections++ },
            )
        }

        compose.onNodeWithText("English")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.RadioButton))
            .performClick()

        assertEquals(1, selections)
    }

    @Test
    fun `switch preference with divider separates row navigation from switch toggle`() {
        var rowClicks = 0
        val toggled = mutableListOf<Boolean>()
        compose.setContent {
            SwitchPreferenceItemWithDivider(
                title = "Dark theme",
                summary = "Follow system",
                checked = false,
                onCheckedChange = { toggled.add(it) },
                onClick = { rowClicks++ },
            )
        }

        compose.onNodeWithText("Dark theme").performClick()
        compose.onNode(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch))
            .performClick()

        assertEquals(1, rowClicks)
        assertEquals(listOf(true), toggled)
    }

    @Test
    fun `switch card toggles once per click and exposes switch semantics`() {
        val state = mutableStateOf(false)
        val toggled = mutableListOf<Boolean>()
        compose.setContent {
            SwitchCardItem(
                title = "Enable feature",
                switchState = state,
                onSwitchToggled = {
                    toggled.add(it)
                    state.value = it
                },
            )
        }

        compose.onNodeWithText("Enable feature")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch))
            .performClick()

        assertEquals(listOf(true), toggled)
    }

    @Test
    fun `multi-line switch row keeps its trailing switch vertically centered`() {
        compose.setContent {
            SwitchPreferenceItemWithDivider(
                modifier = Modifier.width(240.dp),
                title = "Dark theme",
                summary = "A long supporting description that wraps across multiple lines so the row grows well past the two-line height breakpoint.",
                checked = true,
                onCheckedChange = {},
                onClick = {},
            )
        }

        val rowBounds = compose.onNodeWithText("Dark theme").getUnclippedBoundsInRoot()
        val switchBounds = compose
            .onNode(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Switch))
            .getUnclippedBoundsInRoot()
        val rowCenterY = (rowBounds.top + rowBounds.bottom) / 2f
        val switchCenterY = (switchBounds.top + switchBounds.bottom) / 2f

        assertTrue(
            "Expected switch center ($switchCenterY) to match row center ($rowCenterY)",
            abs((switchCenterY - rowCenterY).value) <= 2f,
        )
    }
}
