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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.remote.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.models.ChangelogRelease

private val heading = Regex(pattern = """^ {0,3}(#{1,6})[\t ]+(.+?)[\t ]*#*[\t ]*$""")
private val date = Regex(pattern = """[A-Za-z]+[\t ]+\d{1,2},[\t ]+\d{4}""")
private val versionHeading = Regex(
    pattern = """(?:Version[\t ]+|v)?\[?(\d[^ \t\]]*)]?(?:[\t ]+[-:(].*|[\t ]*:.*)?""",
    option = RegexOption.IGNORE_CASE,
)
private val versionLine = Regex(
    pattern = """\*\*Version:\*\*[\t ]+(.+)""",
    option = RegexOption.IGNORE_CASE,
)

/** Three or more of the same `-`, `*` or `_`, spaces allowed between, indented at most three. */
private val thematicBreak = Regex(pattern = """^ {0,3}([-*_])(?:\s*\1){2,}\s*$""")

/** Maps raw changelog Markdown into releases in document order and marks exact matches for [currentVersion]. */
internal fun String.toChangelogReleases(currentVersion: String): List<ChangelogRelease> {
    if (isBlank()) return emptyList()
    val lines = lines()
    val fence = MarkdownFence()
    val boundaries = mutableListOf<Int>()
    val titles = mutableListOf<String>()
    var releaseLevel: Int? = null

    lines.forEachIndexed { index, line ->
        if (fence.consume(line = line)) return@forEachIndexed
        val match = heading.matchEntire(input = line) ?: return@forEachIndexed
        val level = match.groupValues[1].length
        val title = match.groupValues[2]
        val isUnreleased = title
            .removeSurrounding(prefix = "[", suffix = "]")
            .equals(other = "Unreleased", ignoreCase = true)
        val isRelease = date.matches(input = title) || versionHeading.matches(input = title) || isUnreleased
        val isWithinReleaseLevel = (releaseLevel == null) || (level <= releaseLevel)
        if (isRelease && isWithinReleaseLevel) {
            boundaries += index
            titles += title
            if (releaseLevel == null) {
                releaseLevel = level
            }
        }
    }

    if (boundaries.isEmpty()) {
        val sections = trim().splitAtThematicBreaks()
        if (sections.isEmpty()) return emptyList()
        return listOf(
            ChangelogRelease(
                heading = "",
                sections = sections,
                isCurrent = false,
            ),
        )
    }

    val releases = mutableListOf<ChangelogRelease>()
    val introduction = lines.subList(fromIndex = 0, toIndex = boundaries.first())
        .joinToString(separator = "\n")
        .trimReleaseMarkdown()
    if (introduction.isNotEmpty() && !introduction.equals(other = "# Changelog", ignoreCase = true)) {
        val introSections = introduction.splitAtThematicBreaks()
        if (introSections.isNotEmpty()) {
            releases += ChangelogRelease(
                heading = "",
                sections = introSections,
                isCurrent = false,
            )
        }
    }

    boundaries.forEachIndexed { index, start ->
        val end = boundaries.getOrNull(index = index + 1) ?: lines.size
        val body = lines.subList(fromIndex = start + 1, toIndex = end)
            .joinToString(separator = "\n")
            .trimReleaseMarkdown()
        val metadata = lines.subList(fromIndex = start + 1, toIndex = end)
            .firstOrNull { it.isNotBlank() }
            ?.trim()
            .orEmpty()
        val versionMetadata = versionLine.matchEntire(input = metadata)
        val listedVersions = versionMetadata?.groupValues?.get(1)
            ?.split(',')
            ?.map { it.trim().removeSurrounding(delimiter = "`") }
            .orEmpty()
        val titleVersion = versionHeading.matchEntire(input = titles[index])?.groupValues?.get(1)
        val current = currentVersion.isNotBlank() &&
            (listedVersions + listOfNotNull(titleVersion)).any {
                it.equals(other = currentVersion.trim(), ignoreCase = true)
            }
        val versionMarkdown = if (versionMetadata != null) metadata else ""
        val releaseMarkdown = body.removePrefix(prefix = versionMarkdown).trimStart()
        releases += ChangelogRelease(
            heading = titles[index],
            sections = releaseMarkdown.splitAtThematicBreaks(),
            isCurrent = current,
            versionMarkdown = versionMarkdown,
        )
    }
    return releases
}

/**
 * Splits Markdown at its thematic breaks (`---`, `***` or `___` on a line of their own) and drops
 * blank sections.
 *
 * A break counts only after a blank line, or at the start: under a line of text, `---` underlines
 * a heading instead. Lines inside a fenced code block are never breaks.
 */
internal fun String.splitAtThematicBreaks(): List<String> {
    val parts = mutableListOf<String>()
    val current = StringBuilder()
    val fence = MarkdownFence()
    var previousBlank = true
    lineSequence().forEach { line ->
        val trimmed = line.trim()
        val fenced = fence.consume(line = line)
        if (!fenced && previousBlank && thematicBreak.matches(input = line)) {
            parts += current.toString()
            current.clear()
        } else {
            current.appendLine(line)
        }
        previousBlank = trimmed.isEmpty()
    }
    parts += current.toString()
    return parts.mapNotNull { part -> part.trim().takeIf { it.isNotEmpty() } }
}

/** Removes a trailing thematic break while keeping literal text and fenced Markdown intact. */
internal fun String.trimReleaseMarkdown(): String {
    val lines = trimEnd().lines()
    if ((lines.size < 2) || lines[lines.lastIndex - 1].isNotBlank() || !thematicBreak.matches(input = lines.last())) {
        return trim()
    }
    val fence = MarkdownFence()
    for (index in 0 until lines.lastIndex) {
        fence.consume(line = lines[index])
    }
    return if (fence.consume(line = lines.last())) {
        trim()
    } else {
        lines.dropLast(n = 1).joinToString(separator = "\n").trim()
    }
}

/** Tracks backtick and tilde code fences across consecutive Markdown lines. */
private class MarkdownFence {
    private var marker: Char? = null
    private var length: Int = 0

    /** True for every line belonging to a fence, including its opening and closing lines. */
    fun consume(line: String): Boolean {
        val wasOpen = marker != null
        val indent = line.takeWhile { it == ' ' }.length
        if (indent > 3) return wasOpen
        val text = line.substring(startIndex = indent)
        val character = text.firstOrNull()
        if ((character != '`') && (character != '~')) return wasOpen
        val run = text.takeWhile { it == character }.length
        val suffix = text.substring(startIndex = run)
        if (wasOpen) {
            if ((character == marker) && (run >= length) && suffix.isBlank()) marker = null
            return true
        }
        if ((run < 3) || ((character == '`') && ('`' in suffix))) return false
        marker = character
        length = run
        return true
    }
}
