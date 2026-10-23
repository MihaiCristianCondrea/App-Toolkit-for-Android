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

/** A requested shell override, applied by the screen rather than the option row. */
internal sealed interface DeveloperOptionChange {
    data class SetLayout(val value: ShellLayoutMode) : DeveloperOptionChange
    data class SetTopBarStyle(val value: TopBarOverride) : DeveloperOptionChange
    data class SetHideTopBarOnScroll(val value: Boolean) : DeveloperOptionChange
    data class SetNavigationBarStyle(val value: NavigationBarStyle) : DeveloperOptionChange
    data class SetHideBottomBarOnScroll(val value: Boolean) : DeveloperOptionChange
    data class SetNavigationTint(val value: NavigationTint) : DeveloperOptionChange
    data class SetContentWidth(val value: Boolean) : DeveloperOptionChange
    data class SetStartOverride(val value: Int) : DeveloperOptionChange
    data class SetTabTransition(val value: TabTransitionStyle) : DeveloperOptionChange
    data class SetBackEdge(val value: BackEdgeStyle) : DeveloperOptionChange
    data class SetAnimationSpeed(val value: AnimationSpeed) : DeveloperOptionChange
    data class SetEntranceStyle(val value: EntranceStyle) : DeveloperOptionChange
    data class SetMotionTempo(val value: MotionTempo) : DeveloperOptionChange
    data class SetEntranceReplay(val value: EntranceReplay) : DeveloperOptionChange
    data class SetBottomAccessory(val value: AccessoryMode) : DeveloperOptionChange
}
