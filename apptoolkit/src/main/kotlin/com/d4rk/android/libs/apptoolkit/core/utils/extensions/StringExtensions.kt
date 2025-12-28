package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiEnvironments
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiLanguages
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiPaths
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

fun String.developerAppsApiUrl(
    language: String = ApiLanguages.DEFAULT,
    baseRepositoryUrl: String = ApiConstants.BASE_REPOSITORY_URL,
    path: String = ApiPaths.DEVELOPER_APPS_API,
): String {
    val environmentSegment = when (lowercase()) {
        ApiEnvironments.ENV_DEBUG -> ApiEnvironments.ENV_DEBUG
        ApiEnvironments.ENV_RELEASE -> ApiEnvironments.ENV_RELEASE
        else -> ApiEnvironments.ENV_RELEASE
    }
    val languageSegment = language.lowercase().ifBlank { ApiLanguages.DEFAULT }
    val normalizedPath = path.trimStart('/')

    return listOf(baseRepositoryUrl, environmentSegment, languageSegment, normalizedPath)
        .joinToString(separator = "/")
}

/**
 * Sanitizes a URL string by trimming whitespace and returning `null` for blank inputs.
 */
fun String?.sanitizeUrlOrNull(): String? =
    this?.trim()?.takeUnless(String::isEmpty)

fun String?.normalizeRoute(): String? = this
    ?.substringBefore('?')
    ?.substringBefore('/')
    ?.takeIf { it.isNotBlank() }

@OptIn(ExperimentalEncodingApi::class)
fun String.decodeBase64OrEmpty(): String =
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

fun String.faqCatalogUrl(isDebugBuild: Boolean): String {
    val catalogEnvironment = isDebugBuild.toApiEnvironment()
    return "$this/$catalogEnvironment/catalog.json"
}
