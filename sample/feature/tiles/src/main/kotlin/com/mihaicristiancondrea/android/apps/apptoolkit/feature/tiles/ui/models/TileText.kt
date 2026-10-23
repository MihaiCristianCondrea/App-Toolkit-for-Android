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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models

/**
 * What a tile shows: its [title], secondary [subtitle] text, and the [result] of the last tap.
 *
 * Tile subtitles exist only from Android 10. Before that the label is the only visible text, so a
 * [result] replaces [title] there; otherwise a tap would change nothing the user can read.
 */
data class TileText(
    val title: String,
    val subtitle: String,
    val result: String? = null,
) {
    internal fun forSubtitleSupport(supportsSubtitle: Boolean): VisibleTileText =
        if (supportsSubtitle) {
            VisibleTileText(label = title, subtitle = result ?: subtitle)
        } else {
            VisibleTileText(label = result ?: title, subtitle = null)
        }
}
