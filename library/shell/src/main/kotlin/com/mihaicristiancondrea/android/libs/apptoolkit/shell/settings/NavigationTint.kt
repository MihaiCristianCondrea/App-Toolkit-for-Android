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

/**
 * Whether the rail or permanent drawer and the app bar share one container colour on wide
 * windows, framing the content as a card.
 */
enum class NavigationTint {
    /** The navigation and app bar are the surface colour; the app bar tints as content scrolls. */
    None,

    /** Always tinted, with the content in a rounded card beside them. */
    Always,

    /** Tinted together, rail included, once content scrolls under the app bar. */
    OnScroll,
}
