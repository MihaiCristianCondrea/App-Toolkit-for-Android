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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.models

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellCapabilities
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.AccessoryMode

/** A shell override the developer options offer, in the order the page shows them. */
internal enum class DeveloperOption(val category: DeveloperCategory) {
    Layout(DeveloperCategory.LayoutAndBars),
    TopBarStyle(DeveloperCategory.LayoutAndBars),
    HideTopBarOnScroll(DeveloperCategory.LayoutAndBars),
    NavigationBarStyle(DeveloperCategory.LayoutAndBars),
    HideBottomBarOnScroll(DeveloperCategory.LayoutAndBars),
    NavigationTint(DeveloperCategory.LayoutAndBars),
    ContentWidth(DeveloperCategory.LayoutAndBars),
    StartOverride(DeveloperCategory.Navigation),
    TabTransition(DeveloperCategory.Navigation),
    BackEdge(DeveloperCategory.Navigation),
    AnimationSpeed(DeveloperCategory.Motion),
    BottomAccessory(DeveloperCategory.Accessories),
}

/**
 * The overrides that act on something in this app. Unlike the display settings they count what a
 * forced layout can reach: an app with tabs gets the bottom bar's and the rail's options, whatever
 * its layout policy, since [DeveloperOption.Layout] can force either.
 */
internal fun developerOptions(capabilities: ShellCapabilities): List<DeveloperOption> =
    DeveloperOption.entries.filter { option ->
        when (option) {
            DeveloperOption.Layout,
            DeveloperOption.NavigationBarStyle,
            DeveloperOption.HideBottomBarOnScroll,
            DeveloperOption.NavigationTint -> capabilities.hasTabs

            DeveloperOption.TopBarStyle, DeveloperOption.HideTopBarOnScroll -> capabilities.hasShellTopBars
            DeveloperOption.ContentWidth -> capabilities.hasContentWidthLimit
            DeveloperOption.StartOverride -> capabilities.hasMultipleStartOptions
            DeveloperOption.TabTransition -> capabilities.hasMultipleTabs
            DeveloperOption.BackEdge -> capabilities.hasBackNavigation
            DeveloperOption.AnimationSpeed -> true
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
