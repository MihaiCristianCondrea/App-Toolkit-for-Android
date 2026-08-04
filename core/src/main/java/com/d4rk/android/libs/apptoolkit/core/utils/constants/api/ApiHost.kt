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

package com.d4rk.android.libs.apptoolkit.core.utils.constants.api

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Canonical public routes for the Android App Metadata API.
 *
 * Runtime clients must use the public `/api/v1` surface. The `/admin/api` routes are reserved for
 * catalog management and may expose unpublished applications. See the live contract at
 * [Swagger](https://android-apps-metadata-backend.mihaicristiancondrea.workers.dev/docs) or
 * [OpenAPI](https://android-apps-metadata-backend.mihaicristiancondrea.workers.dev/openapi.json).
 */
object ApiHost {
    const val BASE_URL: String =
        "https://android-apps-metadata-backend.mihaicristiancondrea.workers.dev"
    const val APPS_PATH: String = "api/v1/apps"
    const val DOCS_URL: String = "$BASE_URL/docs"
    const val OPEN_API_URL: String = "$BASE_URL/openapi.json"

    /** Returns the compact, public application catalog endpoint. */
    fun appsUrl(baseUrl: String = BASE_URL): String =
        "${baseUrl.trimEnd('/')}/$APPS_PATH"

    /** Returns the public full-metadata endpoint for [packageName]. */
    fun appDetailsUrl(packageName: String, baseUrl: String = BASE_URL): String =
        "${appsUrl(baseUrl)}/${packageName.toEncodedPathSegment()}"

    /** Returns the public Markdown changelog endpoint for [packageName]. */
    fun appChangelogUrl(packageName: String, baseUrl: String = BASE_URL): String =
        "${appDetailsUrl(packageName = packageName, baseUrl = baseUrl)}/changelog.md"
}

private fun String.toEncodedPathSegment(): String {
    require(isNotBlank()) { "A package name is required to build an API URL." }
    return URLEncoder.encode(this, StandardCharsets.UTF_8.name()).replace("+", "%20")
}
