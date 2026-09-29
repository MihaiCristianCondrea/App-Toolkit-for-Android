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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ResetDeveloperOptionsTest {

    @Test
    fun `reset restores the developer options and keeps the person's display choices`() = runTest {
        val preferences = InMemoryShellPreferences(
            ShellSettings(
                startOverride = 1,
                layoutMode = ShellLayoutMode.Rail,
                accessoryMode = AccessoryMode.None,
                animationSpeed = AnimationSpeed.Slow,
                topBarOverride = TopBarOverride.CenterAligned,
                navigationBarStyle = NavigationBarStyle.Short,
                backEdgeStyle = BackEdgeStyle.System,
            ),
        )

        preferences.resetDeveloperOptions()

        assertEquals(
            ShellSettings(
                topBarOverride = TopBarOverride.CenterAligned,
                navigationBarStyle = NavigationBarStyle.Short,
                backEdgeStyle = BackEdgeStyle.System,
            ),
            preferences.settings.first(),
        )
    }
}
