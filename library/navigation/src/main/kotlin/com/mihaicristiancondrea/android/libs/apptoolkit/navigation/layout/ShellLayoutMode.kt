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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout


/**
 * How the shell presents its navigation.
 *
 * | Mode | Tabs | Drawer entries | Menu button |
 * |---|---|---|---|
 * | [BottomBar] | Navigation bar, unless the host hides its single tab bar | Modal drawer | In the app bar, opens the drawer |
 * | [Rail] | Collapsed rail | Same rail, scrollable | Top of the rail, opens the rail expanded over the content |
 * | [ExpandedRail] | Expanded rail beside the content | Same rail, scrollable | Top of the rail, collapses and expands it in place |
 * | [PermanentDrawer] | Always-open drawer | Same drawer | None |
 */
enum class ShellLayoutMode {
    /** Picks one of the others from the window width. See [ShellLayoutPolicy]. */
    Auto,
    BottomBar,
    Rail,
    ExpandedRail,
    PermanentDrawer,
}
