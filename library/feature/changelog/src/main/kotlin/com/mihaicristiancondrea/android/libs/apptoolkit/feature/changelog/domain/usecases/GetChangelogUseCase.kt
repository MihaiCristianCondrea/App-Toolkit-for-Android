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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.domain.usecases

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories.ChangelogRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.utils.MarkdownFence

/**
 * Loads the host application's changelog and selects the most useful Markdown to display.
 *
 * The current-version section is preferred. When the API returns valid Markdown without that
 * section, the full history is returned so a version-format mismatch never hides useful content.
 * A failure of the repository passes through unchanged.
 */
class GetChangelogUseCase(
    private val repository: ChangelogRepository,
    private val buildInfoRepository: BuildInfoRepository,
) {

    /** Returns the current version's changes, else the full history, else an empty string. */
    suspend operator fun invoke(): String {
        val history = repository.getChangelog(packageName = buildInfoRepository.packageName).trim()
        val currentVersion = history.extractChangesForVersion(version = buildInfoRepository.appVersion)
        return currentVersion.ifBlank { history }
    }
}

/**
 * Extracts the Markdown section whose heading identifies [version].
 *
 * Exact heading matching avoids selecting a version mentioned in release notes or accidentally
 * matching `1.2.3` inside `11.2.30`. Common headings such as `# 1.2.3`,
 * `## Version 1.2.3`, and `## [1.2.3] - 2026-08-02` are supported.
 *
 * The section ends at the next heading of the same or a higher level, so sub-headings such as
 * Keep a Changelog's `### Added` stay inside it.
 */
internal fun String.extractChangesForVersion(version: String): String {
    if (isBlank() || version.isBlank()) return ""
    val versionHeading = Regex(
        pattern = """^ {0,3}#{1,6}[\t ]+\[?(?:Version\s+|v)?${Regex.escape(version.trim())}]?(?:\s*[-:].*)?\s*$""",
        option = RegexOption.IGNORE_CASE,
    )
    val lines = lines()
    val fence = MarkdownFence()
    var start = -1
    var versionLevel = 0
    lines.forEachIndexed { index, line ->
        if (fence.consume(line)) return@forEachIndexed
        val level = line.headingLevel()
        if (start < 0) {
            if (versionHeading.matches(line)) {
                start = index
                versionLevel = requireNotNull(level)
            }
        } else if (level != null && level <= versionLevel) {
            return lines.subList(start, index).joinToString("\n").trim()
        }
    }
    return if (start >= 0) lines.subList(start, lines.size).joinToString("\n").trim() else ""
}

private val markdownHeading = Regex("""^ {0,3}(#{1,6})(?:[\t ]|$)""")

/** The ATX heading level of this line, or null for text and indented code. */
private fun String.headingLevel(): Int? = markdownHeading.find(this)?.groupValues?.get(1)?.length
