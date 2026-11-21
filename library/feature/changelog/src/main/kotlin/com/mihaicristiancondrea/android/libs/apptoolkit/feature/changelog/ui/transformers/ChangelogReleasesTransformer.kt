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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.transformers

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.models.ChangelogRelease

private val heading = Regex("""^ {0,3}(#{1,6})[\t ]+(.+?)[\t ]*#*[\t ]*$""")
private val date = Regex("""[A-Za-z]+[\t ]+\d{1,2},[\t ]+\d{4}""")
private val versionHeading = Regex(
    """(?:Version[\t ]+|v)?\[?(\d[^ \t\]]*)]?(?:[\t ]+[-:].*|[\t ]*:.*)?""",
    RegexOption.IGNORE_CASE
)
private val versionLine = Regex("""\*\*Version:\*\*[\t ]+(.+)""", RegexOption.IGNORE_CASE)

/** Keeps every release and marks exact installed-version matches outside code fences. */
internal fun String.toChangelogReleases(currentVersion: String): List<ChangelogRelease> {
    if (isBlank()) return emptyList()
    val lines = lines()
    val fence = MarkdownFence()
    val boundaries = mutableListOf<Int>()
    val titles = mutableListOf<String>()
    var releaseLevel = 7

    lines.forEachIndexed { index, line ->
        if (fence.consume(line)) return@forEachIndexed
        val match = heading.matchEntire(line) ?: return@forEachIndexed
        val level = match.groupValues[1].length
        val title = match.groupValues[2]
        val isRelease = date.matches(title) || versionHeading.matches(title) ||
                title.equals("Unreleased", ignoreCase = true)
        if (isRelease && level <= releaseLevel) {
            boundaries += index
            titles += title
            releaseLevel = level
        }
    }

    if (boundaries.isEmpty()) return listOf(ChangelogRelease("", trim(), false))
    val releases = mutableListOf<ChangelogRelease>()
    val introduction = lines.subList(0, boundaries.first()).joinToString("\n")
        .trimReleaseMarkdown()
    if (introduction.isNotEmpty() && !introduction.equals("# Changelog", ignoreCase = true)) {
        releases += ChangelogRelease("", introduction, false)
    }
    boundaries.forEachIndexed { index, start ->
        val end = boundaries.getOrNull(index + 1) ?: lines.size
        val body = lines.subList(start + 1, end).joinToString("\n")
            .trimReleaseMarkdown()
        val metadata =
            lines.subList(start + 1, end).firstOrNull { it.isNotBlank() }?.trim().orEmpty()
        val versionMetadata = versionLine.matchEntire(metadata)
        val listedVersions = versionMetadata?.groupValues?.get(1)
            ?.split(',')?.map { it.trim().removeSurrounding("`") }.orEmpty()
        val titleVersion = versionHeading.matchEntire(titles[index])?.groupValues?.get(1)
        val current = currentVersion.isNotBlank() &&
                (listedVersions + listOfNotNull(titleVersion)).any {
                    it.equals(
                        currentVersion.trim(),
                        ignoreCase = true
                    )
                }
        val versionMarkdown = if (versionMetadata != null) metadata else ""
        releases += ChangelogRelease(
            heading = titles[index],
            markdown = body.removePrefix(versionMarkdown).trimStart(),
            isCurrent = current,
            versionMarkdown = versionMarkdown,
        )
    }
    return releases
}
