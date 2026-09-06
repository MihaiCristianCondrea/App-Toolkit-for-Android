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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Share
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class ToolkitIconResolverTest {

    private val shareVector = ToolkitIcon.Vector(Icons.Rounded.Share)
    private val shareAnimated = ToolkitIcon.AnimatedVector(resId = 1)
    private val settingsResource = ToolkitIcon.Resource(resId = 2)
    private val settingsVector = ToolkitIcon.Vector(Icons.Outlined.Settings)

    @Test
    fun `vector icon with animated selected icon stays static until it is clicked`() {
        assertEquals(
            shareVector,
            resolveToolkitIcon(
                icon = shareVector,
                selectedIcon = shareAnimated,
                selected = false,
                interacted = false,
            )
        )
        assertEquals(
            shareAnimated,
            resolveToolkitIcon(
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
            resolveToolkitIcon(
                icon = shareAnimated,
                selectedIcon = shareAnimated,
                selected = false,
            )
        )
        assertEquals(
            shareAnimated,
            resolveToolkitIcon(
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
            resolveToolkitIcon(
                icon = settingsResource,
                selectedIcon = settingsVector,
                selected = false,
                interacted = true,
            )
        )
        assertEquals(
            settingsVector,
            resolveToolkitIcon(
                icon = settingsResource,
                selectedIcon = settingsVector,
                selected = true,
            )
        )
    }

    @Test
    fun `animated icons restart their animation on a repeated click by default`() {
        assertEquals(ToolkitIconReplayMode.Restart, shareAnimated.replayMode)
        assertEquals(
            ToolkitIconReplayMode.Restart,
            (ToolkitIcon.animated(resId = 3) as ToolkitIcon.AnimatedVector).replayMode,
        )
        assertEquals(
            ToolkitIconReplayMode.Reverse,
            ToolkitIcon.AnimatedVector(
                resId = 3,
                replayMode = ToolkitIconReplayMode.Reverse,
            ).replayMode,
        )
    }
}
