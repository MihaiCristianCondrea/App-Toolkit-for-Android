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

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import kotlinx.coroutines.flow.Flow

/**
 * Where [ShellSettings] live.
 *
 * `ShellHost` takes an implementation from the app, so an app that keeps its settings elsewhere
 * adapts that store instead of keeping two, and tests pass an [InMemoryShellPreferences]. Without
 * one, `ShellHost` uses [ShellPreferences] (`context`), a DataStore of its own.
 *
 * An implementation provides [settings] and [update]; every setter below is written on [update].
 */
interface ShellPreferences {

    /** The current settings, then every change. */
    val settings: Flow<ShellSettings>

    /**
     * The settings last read in this process, if any, so an activity recreated by a rotation
     * starts from them instead of waiting a frame for the store.
     */
    val lastKnown: ShellSettings? get() = null

    /** Replaces the settings with [transform] applied to the current ones, atomically. */
    suspend fun update(transform: (ShellSettings) -> ShellSettings)

    suspend fun setLayoutMode(value: ShellLayoutMode) = update { it.copy(layoutMode = value) }
    suspend fun setTopBarOverride(value: TopBarOverride) = update { it.copy(topBarOverride = value) }
    suspend fun setTabTransition(value: TabTransitionStyle) = update { it.copy(tabTransition = value) }
    suspend fun setAccessoryMode(value: AccessoryMode) = update { it.copy(accessoryMode = value) }
    suspend fun setHideBottomBarOnScroll(value: Boolean) = update { it.copy(hideBottomBarOnScroll = value) }
    suspend fun setHideTopBarOnScroll(value: Boolean) = update { it.copy(hideTopBarOnScroll = value) }
    suspend fun setLimitContentWidth(value: Boolean) = update { it.copy(limitContentWidth = value) }
    suspend fun setAnimationSpeed(value: AnimationSpeed) = update { it.copy(animationSpeed = value) }
    suspend fun setBackEdgeStyle(value: BackEdgeStyle) = update { it.copy(backEdgeStyle = value) }
    suspend fun setStartOverride(value: Int) = update { it.copy(startOverride = value) }
    suspend fun setNavigationBarStyle(value: NavigationBarStyle) = update { it.copy(navigationBarStyle = value) }
    suspend fun setNavigationTint(value: NavigationTint) = update { it.copy(navigationTint = value) }

    /**
     * Puts the developer options back to their defaults: where the app starts, the forced
     * layout, the bottom accessories and the animation speed. The layout choices the display
     * settings offer are the person's own and stay as they are.
     */
    suspend fun resetDeveloperOptions() = update { current ->
        val defaults = ShellSettings()
        current.copy(
            startOverride = defaults.startOverride,
            layoutMode = defaults.layoutMode,
            accessoryMode = defaults.accessoryMode,
            animationSpeed = defaults.animationSpeed,
        )
    }
}

/** The shell's own store: a DataStore shared by every instance built from the same application. */
fun ShellPreferences(context: Context): ShellPreferences = DataStoreShellPreferences(context)

val LocalShellPreferences = staticCompositionLocalOf<ShellPreferences> {
    error("ShellPreferences is only available inside ShellHost.")
}

val LocalShellSettings = staticCompositionLocalOf { ShellSettings() }
