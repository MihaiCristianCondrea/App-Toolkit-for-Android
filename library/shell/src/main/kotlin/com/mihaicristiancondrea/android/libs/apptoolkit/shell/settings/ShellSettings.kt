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

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.MotionTempo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.EntranceReplay
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.EntranceStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle

/**
 * The shell's persisted overrides: how it lays out, moves and draws its chrome.
 *
 * Every one is a developer option, offered by `:library:feature:developer` once unlocked, to
 * force, compare and exercise the shell's variations. The defaults leave the app exactly as its
 * graph and layout policy declare it, and `ShellPreferencesRepository.resetDeveloperOptions` returns to them.
 * The person's own preferences, such as the theme or the navigation labels, are not kept here.
 */
@Immutable
data class ShellSettings(
    val layoutMode: ShellLayoutMode = ShellLayoutMode.Auto,
    val topBarOverride: TopBarOverride = TopBarOverride.AsDeclared,
    val tabTransition: TabTransitionStyle = TabTransitionStyle.Directional,
    val accessoryMode: AccessoryMode = AccessoryMode.AsDeclared,
    val hideBottomBarOnScroll: Boolean = true,
    /** Whether content keeps to the layout policy's maximum width on large windows. */
    val limitContentWidth: Boolean = true,
    val animationSpeed: AnimationSpeed = AnimationSpeed.Normal,
    val backEdgeStyle: BackEdgeStyle = BackEdgeStyle.System,
    /** Where the next launch opens, as an index into the graph's start options; -1 as declared. */
    val startOverride: Int = -1,
    val navigationBarStyle: NavigationBarStyle = NavigationBarStyle.Standard,
    val navigationTint: NavigationTint = NavigationTint.Always,
    /**
     * Whether the app bar slides away as content scrolls down, whatever its style, and comes back
     * as soon as it scrolls up. A large bar collapses first, and the bar leaves entirely, from under
     * the status bar too, so content scrolls on beneath it.
     */
    val hideTopBarOnScroll: Boolean = false,
    /** How elements with `animateVisibility` enter: the style every screen's cascade follows. */
    val entranceStyle: EntranceStyle = EntranceStyle.Slide,
    /** How quickly everything moves: the springs of the theme's motion scheme. */
    val motionTempo: MotionTempo = MotionTempo.Calm,
    /**
     * When a screen's elements enter: each time it is opened, the default, on every return to it,
     * or only for the first copy of each screen while the app runs.
     */
    val entranceReplay: EntranceReplay = EntranceReplay.OnOpen,
)
