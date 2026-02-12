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

package com.d4rk.android.libs.apptoolkit.core.utils.extensions.string

import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiLanguages
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiPaths
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.boolean.toApiEnvironment
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Builds the developer apps API URL using the provided environment, language, and path segments.
 */
fun String.developerAppsApiUrl(
    language: String = ApiLanguages.DEFAULT,
    baseRepositoryUrl: String = ApiConstants.BASE_REPOSITORY_URL,
    path: String = ApiPaths.DEVELOPER_APPS_API,
): String {
    val environmentSegment = lowercase().takeIf { it == ApiEnvironments.ENV_DEBUG }
        ?: ApiEnvironments.ENV_RELEASE
    val languageSegment = language.lowercase().ifBlank { ApiLanguages.DEFAULT }
    val normalizedPath = path.trimStart('/')

    return listOf(baseRepositoryUrl, environmentSegment, languageSegment, normalizedPath)
        .joinToString(separator = "/")
}

/**
 * Sanitizes a URL string by trimming whitespace and returning `null` for blank inputs.
 */
fun String?.sanitizeUrlOrNull(): String? =
    this?.trim()?.takeIf { it.isNotEmpty() }

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
 * Extracts the changelog section for the first line that contains [version].
 *
 * Compatibility/behavior (matches your current implementation + unit tests):
 * - Finds the first line where `line.contains(version)` is true.
 * - Includes that matching line.
 * - Includes subsequent lines until the next Markdown header (a line starting with '#'),
 *   or until end of content.
 * - Returns the extracted block trimmed; returns an empty string if [version] isn't found
 *   (or if the receiver is blank).
 *
 * Implementation notes:
 * - Uses [lineSequence] to avoid allocating a full list of lines. :contentReference[oaicite:1]{index=1}
 * - Uses lazy sequence operators ([dropWhile], [takeWhile]) for clean, idiomatic flow. :contentReference[oaicite:2]{index=2}
 */
fun String.extractChangesForVersion(version: String): String {
    val versionLinesIterator = lineSequence()
        .dropWhile { currentLine -> !currentLine.contains(version) }
        .iterator()
    if (!versionLinesIterator.hasNext()) return ""
    val versionHeaderLine = versionLinesIterator.next()
    val changelogSectionLines =
        sequenceOf(versionHeaderLine) + generateSequence { if (versionLinesIterator.hasNext()) versionLinesIterator.next() else null }.takeWhile { currentLine ->
            !currentLine.startsWith("#")
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
