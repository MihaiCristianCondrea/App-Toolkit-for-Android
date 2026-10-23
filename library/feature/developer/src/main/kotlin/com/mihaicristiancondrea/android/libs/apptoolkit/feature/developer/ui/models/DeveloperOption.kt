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
    EntranceStyle(DeveloperCategory.Motion),
    MotionTempo(DeveloperCategory.Motion),
    EntranceReplay(DeveloperCategory.Motion),
    BottomAccessory(DeveloperCategory.Accessories),
}
