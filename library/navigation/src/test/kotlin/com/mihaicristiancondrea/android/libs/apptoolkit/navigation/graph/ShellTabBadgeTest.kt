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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ShellTabBadgeTest {

    private data object Home : NavKey

    @Test
    fun `a tab takes a fixed badge or a badge read in composition, not both`() {
        assertThrows(IllegalArgumentException::class.java) {
            ShellTab(
                key = Home,
                label = 0,
                icon = ToolkitIcon.Vector(Icons.Outlined.Home),
                badge = "New",
                badgeText = { "3" },
            )
        }
    }
}
