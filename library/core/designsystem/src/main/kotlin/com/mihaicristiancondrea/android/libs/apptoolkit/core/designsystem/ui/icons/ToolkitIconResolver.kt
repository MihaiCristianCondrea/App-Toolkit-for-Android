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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons

/**
 * Picks which of the two icons of a component has to be rendered.
 *
 * The rules cover the three combinations the toolkit supports:
 * - **Static only** (vector and/or drawable resource): [selectedIcon] while [selected], [icon] otherwise.
 * - **Static normal icon + animated selected icon**: the static [icon] is shown until the component
 *   is either selected or clicked at least once ([interacted]); from then on the AVD is rendered so
 *   it can play. This is the "`Icons.Rounded.Share` at rest, animation on click" case. The first
 *   frame of the AVD should be drawn like the static icon, otherwise the swap is visible.
 * - **Animated only**: the AVD is always rendered, resting on its first or last frame depending on
 *   the selection state.
 *
 * @param icon Icon declared for the unselected state.
 * @param selectedIcon Icon declared for the selected state.
 * @param selected Whether the component is currently selected.
 * @param interacted Whether the component has been clicked at least once while composed.
 */
fun resolveToolkitIcon(
    icon: ToolkitIcon,
    selectedIcon: ToolkitIcon,
    selected: Boolean,
    interacted: Boolean = false,
): ToolkitIcon = when {
    selected -> selectedIcon
    selectedIcon is ToolkitIcon.Animated && interacted -> selectedIcon
    else -> icon
}
