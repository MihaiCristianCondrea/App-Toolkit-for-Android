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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.utils

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models.DeveloperOption
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AccessoryMode

/**
 * The overrides that act on something in this app. Unlike the display settings they count what a
 * forced layout can reach: [DeveloperOption.Layout] can force a rail or the phone's modal drawer
 * layout. Bottom bar options still require tabs that the host allows in that bar.
 */
internal fun availableDeveloperOptions(capabilities: ShellCapabilities): List<DeveloperOption> =
    DeveloperOption.entries.filter { option ->
        when (option) {
            DeveloperOption.Layout,
            DeveloperOption.NavigationTint -> capabilities.hasTabs

            DeveloperOption.NavigationBarStyle,
            DeveloperOption.HideBottomBarOnScroll -> capabilities.hasBottomNavigationTabs

            DeveloperOption.TopBarStyle, DeveloperOption.HideTopBarOnScroll -> capabilities.hasShellTopBars
            DeveloperOption.ContentWidth -> capabilities.hasContentWidthLimit
            DeveloperOption.StartOverride -> capabilities.hasMultipleStartOptions
            DeveloperOption.TabTransition -> capabilities.hasMultipleTabs
            DeveloperOption.BackEdge -> capabilities.hasBackNavigation
            DeveloperOption.AnimationSpeed,
            DeveloperOption.EntranceStyle,
            DeveloperOption.MotionTempo,
            DeveloperOption.EntranceReplay -> true
            DeveloperOption.BottomAccessory -> capabilities.hasAccessories
        }
    }

/**
 * The accessory choices that mean something: all four with a banner and a player, and otherwise
 * only whether the one the app has is shown.
 */
internal fun accessoryModes(capabilities: ShellCapabilities): List<AccessoryMode> =
    if (capabilities.hasBanner && capabilities.hasPlayer) {
        AccessoryMode.entries
    } else {
        listOf(AccessoryMode.AsDeclared, AccessoryMode.None)
    }
