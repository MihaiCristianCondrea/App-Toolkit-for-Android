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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid

/** Whether an ad row takes part in the layout, and whether it currently occupies any space. */
internal enum class GroupedGridAdRow {
    /** No ad was requested, or the grid is too small to carry one. */
    None,

    /**
     * An ad was requested but none is on screen. The row stays in the plan so the slot keeps its
     * place in composition and can finish loading, but it takes no space and the corners of the
     * group are cut as if it were not there.
     */
    Collapsed,

    /** An ad is on screen and is one more row of the group. */
    Visible,
}
