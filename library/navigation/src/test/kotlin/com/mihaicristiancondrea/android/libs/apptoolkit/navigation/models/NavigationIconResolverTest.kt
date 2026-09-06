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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Share
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NavigationIconResolverTest {

    private val shareVector = NavigationIcon.Vector(Icons.Rounded.Share)
    private val shareAnimated = NavigationIcon.AnimatedVector(resId = 1)
    private val settingsResource = NavigationIcon.Resource(resId = 2)
    private val settingsVector = NavigationIcon.Vector(Icons.Outlined.Settings)

    @Test
    fun `vector icon with animated selected icon stays static until it is clicked`() {
        assertEquals(
            shareVector,
            resolveNavigationIcon(
                icon = shareVector,
                selectedIcon = shareAnimated,
                selected = false,
                interacted = false,
            )
        )
        assertEquals(
            shareAnimated,
            resolveNavigationIcon(
                icon = shareVector,
                selectedIcon = shareAnimated,
                selected = false,
                interacted = true,
            )
        )
    }

    @Test
    fun `animated only icon is used for both states`() {
        assertEquals(
            shareAnimated,
            resolveNavigationIcon(
                icon = shareAnimated,
                selectedIcon = shareAnimated,
                selected = false,
            )
        )
        assertEquals(
            shareAnimated,
            resolveNavigationIcon(
                icon = shareAnimated,
                selectedIcon = shareAnimated,
                selected = true,
            )
        )
    }

    @Test
    fun `static icons are swapped on selection only`() {
        assertEquals(
            settingsResource,
            resolveNavigationIcon(
                icon = settingsResource,
                selectedIcon = settingsVector,
                selected = false,
                interacted = true,
            )
        )
        assertEquals(
            settingsVector,
            resolveNavigationIcon(
                icon = settingsResource,
                selectedIcon = settingsVector,
                selected = true,
            )
        )
    }
}
