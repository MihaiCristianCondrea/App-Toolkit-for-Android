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
