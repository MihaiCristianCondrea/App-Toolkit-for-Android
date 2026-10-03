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

import java.net.URI

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
