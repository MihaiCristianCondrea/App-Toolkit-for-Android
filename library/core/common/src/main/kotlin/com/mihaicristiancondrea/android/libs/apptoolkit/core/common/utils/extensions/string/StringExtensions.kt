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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.string

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.boolean.toApiEnvironment
import java.net.URI
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Sanitizes URL-like input by trimming and validating strict http(s) absolute URLs.
 *
 * Returns `null` for blank values, malformed URLs, unsupported schemes, or URLs without host.
 */
fun String?.sanitizeUrlOrNull(): String? {
    val candidate = this?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    val parsedUri = runCatching {
        URI(candidate)
    }.getOrNull() ?: return null
    val normalizedScheme = parsedUri.scheme?.lowercase()
    val hasAllowedScheme = normalizedScheme == "http" || normalizedScheme == "https"
    val hasHost = !parsedUri.host.isNullOrBlank()

    return candidate.takeIf { hasAllowedScheme && hasHost }
}

/**
 * Normalizes a navigation route by removing query/child segments and returning `null` for blanks.
 */
fun String?.normalizeRoute(): String? = this
    ?.substringBefore('?')
    ?.substringBefore('/')
    ?.takeIf { it.isNotBlank() }

/**
 * Decodes a base64 string into UTF-8 text, returning an empty string on failure.
 */
@OptIn(ExperimentalEncodingApi::class)
fun String.toToken(): String =
    runCatching { String(Base64.decode(this), Charsets.UTF_8) }.getOrDefault("")

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
 * Builds the FAQ catalog URL for the current build type.
 */
fun String.faqCatalogUrl(isDebugBuild: Boolean): String {
    val catalogEnvironment = isDebugBuild.toApiEnvironment()
    return "$this/$catalogEnvironment/catalog.json"
}
