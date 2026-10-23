/*
 * Copyright (Γö¼ΓîÉ) 2026 Mihai-Cristian Condrea
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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.extensions

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.utils.MarkdownFence

/**
 * Splits Markdown at its thematic breaks (`---`, `***` or `___` on a line of their own), so each
 * part can be drawn on its own and the breaks as something other than the renderer's plain rule.
 * Blank parts are dropped.
 *
 * A break counts only after a blank line, or at the start: under a line of text, `---` underlines
 * a heading instead. Lines inside a fenced code block are never breaks.
 * Three or more of the same `-`, `*` or `_`, spaces allowed between, indented at most three.
 */
internal fun String.splitAtThematicBreaks(): List<String> {
    val parts = mutableListOf<String>()
    val current = StringBuilder()
    val fence = MarkdownFence()
    var previousBlank = true
    lineSequence().forEach { line ->
        val trimmed = line.trim()
        val fenced = fence.consume(line)
        if (!fenced && previousBlank && Regex("""^ {0,3}([-*_])(?:\s*\1){2,}\s*$""").matches(line)) {
            parts += current.toString()
            current.clear()
        } else {
            current.appendLine(line)
        }
        previousBlank = trimmed.isEmpty()
    }
    parts += current.toString()
    return parts.map { it.trim() }.filter { it.isNotEmpty() }
}
