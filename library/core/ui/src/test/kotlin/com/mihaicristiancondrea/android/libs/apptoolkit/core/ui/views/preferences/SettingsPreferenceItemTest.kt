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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import org.junit.Assert.assertEquals
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
}
