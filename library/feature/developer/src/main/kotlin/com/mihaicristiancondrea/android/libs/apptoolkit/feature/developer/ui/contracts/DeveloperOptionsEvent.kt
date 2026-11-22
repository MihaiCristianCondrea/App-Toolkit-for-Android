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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.contracts

import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.MotionTempo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.EntranceReplay
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.EntranceStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AccessoryMode
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AnimationSpeed
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BackEdgeStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.NavigationTint
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.TopBarOverride

/** Events handled by [com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.DeveloperOptionsViewModel]. */
sealed interface DeveloperOptionsEvent {
    /** Starts or restarts observation of the host shell preferences. */
    data object Load : DeveloperOptionsEvent

    /** Resets all developer overrides back to the host's declared defaults. */
    data object Reset : DeveloperOptionsEvent

    /** Persists a forced [ShellLayoutMode] override. */
    data class SetLayout(val value: ShellLayoutMode) : DeveloperOptionsEvent

    /** Persists a [TopBarOverride] override. */
    data class SetTopBarStyle(val value: TopBarOverride) : DeveloperOptionsEvent

    /** Persists whether the top app bar hides on scroll. */
    data class SetHideTopBarOnScroll(val value: Boolean) : DeveloperOptionsEvent

    /** Persists a [NavigationBarStyle] override. */
    data class SetNavigationBarStyle(val value: NavigationBarStyle) : DeveloperOptionsEvent

    /** Persists whether the bottom navigation bar hides on scroll. */
    data class SetHideBottomBarOnScroll(val value: Boolean) : DeveloperOptionsEvent

    /** Persists a [NavigationTint] override. */
    data class SetNavigationTint(val value: NavigationTint) : DeveloperOptionsEvent

    /** Persists whether content width is limited on large windows. */
    data class SetContentWidth(val value: Boolean) : DeveloperOptionsEvent

    /** Persists a start-option index override, or `-1` for the host default. */
    data class SetStartOverride(val value: Int) : DeveloperOptionsEvent

    /** Persists a [TabTransitionStyle] override. */
    data class SetTabTransition(val value: TabTransitionStyle) : DeveloperOptionsEvent

    /** Persists a [BackEdgeStyle] override. */
    data class SetBackEdge(val value: BackEdgeStyle) : DeveloperOptionsEvent

    /** Persists an [AnimationSpeed] override. */
    data class SetAnimationSpeed(val value: AnimationSpeed) : DeveloperOptionsEvent

    /** Persists an [EntranceStyle] override. */
    data class SetEntranceStyle(val value: EntranceStyle) : DeveloperOptionsEvent

    /** Persists a [MotionTempo] override. */
    data class SetMotionTempo(val value: MotionTempo) : DeveloperOptionsEvent

    /** Persists an [EntranceReplay] override. */
    data class SetEntranceReplay(val value: EntranceReplay) : DeveloperOptionsEvent

    /** Persists an [AccessoryMode] override. */
    data class SetBottomAccessory(val value: AccessoryMode) : DeveloperOptionsEvent
}
