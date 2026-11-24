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

/** A shell override the developer options offer, in the order the page shows them. */
internal enum class DeveloperOption(val category: DeveloperCategory) {
    Layout(category = DeveloperCategory.LayoutAndBars),
    TopBarStyle(category = DeveloperCategory.LayoutAndBars),
    HideTopBarOnScroll(category = DeveloperCategory.LayoutAndBars),
    NavigationBarStyle(category = DeveloperCategory.LayoutAndBars),
    HideBottomBarOnScroll(category = DeveloperCategory.LayoutAndBars),
    NavigationTint(category = DeveloperCategory.LayoutAndBars),
    ContentWidth(category = DeveloperCategory.LayoutAndBars),
    StartOverride(category = DeveloperCategory.Navigation),
    TabTransition(category = DeveloperCategory.Navigation),
    BackEdge(category = DeveloperCategory.Navigation),
    AnimationSpeed(category = DeveloperCategory.Motion),
    EntranceStyle(category = DeveloperCategory.Motion),
    MotionTempo(category = DeveloperCategory.Motion),
    EntranceReplay(category = DeveloperCategory.Motion),
    BottomAccessory(category = DeveloperCategory.Accessories),
}
