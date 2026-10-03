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
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class ResetDeveloperOptionsTest {

    /** Every [ShellSettings] value away from its default. */
    private val everythingChanged = ShellSettings(
        layoutMode = ShellLayoutMode.Rail,
        topBarOverride = TopBarOverride.CenterAligned,
        tabTransition = TabTransitionStyle.Fade,
        accessoryMode = AccessoryMode.None,
        hideBottomBarOnScroll = false,
        limitContentWidth = false,
        animationSpeed = AnimationSpeed.Slow,
        backEdgeStyle = BackEdgeStyle.System,
        startOverride = 1,
        navigationBarStyle = NavigationBarStyle.Short,
        navigationTint = NavigationTint.None,
        hideTopBarOnScroll = true,
    )

    @Test
    fun `the test changes every shell setting`() {
        val defaults = ShellSettings()
        val changed = ShellSettings::class.java.declaredFields
            .filterNot { java.lang.reflect.Modifier.isStatic(it.modifiers) }
            .onEach { it.isAccessible = true }
            .filter { it.get(everythingChanged) == it.get(defaults) }
            .map { it.name }

        assertEquals(emptyList<String>(), changed, "left at their defaults")
        assertNotEquals(defaults, everythingChanged)
    }

    @Test
    fun `reset puts every shell setting back as the app declares it`() = runTest {
        val preferences = InMemoryShellPreferences(everythingChanged)

        preferences.resetDeveloperOptions()

        assertEquals(ShellSettings(), preferences.settings.first())
    }
}
