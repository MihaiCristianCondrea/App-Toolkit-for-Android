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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.utils.extensions

/**
 * Extracts the Markdown section whose heading identifies [version].
 *
 * Exact heading matching avoids selecting a version mentioned in release notes or accidentally
 * matching `1.2.3` inside `11.2.30`. Common headings such as `# 1.2.3`,
 * `## Version 1.2.3`, and `## [1.2.3] - 2026-08-02` are supported.
 */
fun String.extractChangesForVersion(version: String): String {
    if (isBlank() || version.isBlank()) return ""
    val versionHeading = Regex(
        pattern = """^#{1,6}\s*\[?(?:Version\s+|v)?${Regex.escape(version.trim())}]?(?:\s*[-:].*)?\s*$""",
        option = RegexOption.IGNORE_CASE,
    )
    val versionLinesIterator = lineSequence()
        .dropWhile { currentLine -> !versionHeading.matches(currentLine.trim()) }
        .iterator()
    if (!versionLinesIterator.hasNext()) return ""
    val versionHeaderLine = versionLinesIterator.next()
    val changelogSectionLines =
        sequenceOf(versionHeaderLine) + generateSequence { if (versionLinesIterator.hasNext()) versionLinesIterator.next() else null }.takeWhile { currentLine ->
            !currentLine.trimStart().startsWith("#")
        }
    return buildString {
        changelogSectionLines.forEach { appendLine(it) }
    }.trim()
}

/**
 * Splits Markdown at its thematic breaks (`---`, `***` or `___` on a line of their own), so each
 * part can be drawn on its own and the breaks as something other than the renderer's plain rule.
 * Blank parts are dropped.
 *
 * A break counts only after a blank line, or at the start: under a line of text, `---` underlines
 * a heading instead. Lines inside a fenced code block are never breaks.
 */
fun String.splitAtThematicBreaks(): List<String> {
    val parts = mutableListOf<String>()
    val current = StringBuilder()
    var inFence = false
    var previousBlank = true
    lineSequence().forEach { line ->
        val trimmed = line.trim()
        if (trimmed.startsWith("```") || trimmed.startsWith("~~~")) inFence = !inFence
        if (!inFence && previousBlank && ThematicBreak.matches(line)) {
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

/** Three or more of the same `-`, `*` or `_`, spaces allowed between, indented at most three. */
private val ThematicBreak = Regex("""^ {0,3}([-*_])(?:\s*\1){2,}\s*$""")
