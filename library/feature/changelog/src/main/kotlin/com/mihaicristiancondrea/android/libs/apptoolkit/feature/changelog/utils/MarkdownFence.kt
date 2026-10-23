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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.utils

/** Tracks fenced code so headings and rules inside it remain literal Markdown. */
internal class MarkdownFence {
    private var marker: Char? = null
    private var length: Int = 0

    /** True for every line belonging to a fence, including its opening and closing lines. */
    fun consume(line: String): Boolean {
        val wasOpen = marker != null
        val indent = line.takeWhile { it == ' ' }.length
        if (indent > 3) return wasOpen
        val text = line.substring(indent)
        val character = text.firstOrNull()
        if (character != '`' && character != '~') return wasOpen
        val run = text.takeWhile { it == character }.length
        val suffix = text.substring(run)
        if (wasOpen) {
            if (character == marker && run >= length && suffix.isBlank()) marker = null
            return true
        }
        if (run < 3 || (character == '`' && '`' in suffix)) return false
        marker = character
        length = run
        return true
    }
}
