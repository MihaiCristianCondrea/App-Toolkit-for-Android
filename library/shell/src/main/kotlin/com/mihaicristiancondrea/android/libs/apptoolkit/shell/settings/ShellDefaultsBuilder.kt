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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.MotionTempo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.EntranceReplay
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.EntranceStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle

/**
 * The shell settings an app may choose as its own defaults in `appToolkitDefaults { shell { } }`.
 * Each property starts at the Toolkit's default and means what the [ShellSettings] property of the
 * same name does. The settings that only exist to test the shell, such as a forced layout, a
 * slowed animation or a start override, stay at the Toolkit's defaults and are left to the
 * developer options.
 */
@AppToolkitDefaultsDsl
class ShellDefaultsBuilder internal constructor(private val from: ShellSettings) {
    var tabTransition: TabTransitionStyle = from.tabTransition
    var backEdgeStyle: BackEdgeStyle = from.backEdgeStyle
    var hideBottomBarOnScroll: Boolean = from.hideBottomBarOnScroll
    var hideTopBarOnScroll: Boolean = from.hideTopBarOnScroll
    var limitContentWidth: Boolean = from.limitContentWidth
    var navigationBarStyle: NavigationBarStyle = from.navigationBarStyle
    var navigationTint: NavigationTint = from.navigationTint
    var entranceStyle: EntranceStyle = from.entranceStyle
    var motionTempo: MotionTempo = from.motionTempo
    var entranceReplay: EntranceReplay = from.entranceReplay

    internal fun build(): ShellSettings = from.copy(
        tabTransition = tabTransition,
        backEdgeStyle = backEdgeStyle,
        hideBottomBarOnScroll = hideBottomBarOnScroll,
        hideTopBarOnScroll = hideTopBarOnScroll,
        limitContentWidth = limitContentWidth,
        navigationBarStyle = navigationBarStyle,
        navigationTint = navigationTint,
        entranceStyle = entranceStyle,
        motionTempo = motionTempo,
        entranceReplay = entranceReplay,
    )
}
