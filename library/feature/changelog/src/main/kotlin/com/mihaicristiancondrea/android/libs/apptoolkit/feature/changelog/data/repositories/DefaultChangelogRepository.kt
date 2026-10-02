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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.api.ApiHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.networkCall
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.toNetworkException
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

/**
 * Fetches changelog Markdown from the public Android App Metadata API.
 *
 * The package endpoint is authoritative, while [legacyChangelogUrl] is a compatibility fallback,
 * used only when the package is blank or the endpoint answers HTTP 404. Any other failure of the
 * endpoint is the answer: it is thrown, not covered by the fallback. It needs no dispatcher, as
 * Ktor suspends.
 */
class DefaultChangelogRepository(
    private val client: HttpClient,
    private val apiBaseUrl: String,
    private val legacyChangelogUrl: String,
    private val telemetryRepository: TelemetryRepository,
) : ChangelogRepository {

    override suspend fun getChangelog(packageName: String): String {
        if (packageName.isBlank()) return getLegacyChangelog(reason = LegacyReason.BLANK_PACKAGE, packageName)

        val primary = fetchMarkdown(url = ApiHost.appChangelogUrl(packageName = packageName, baseUrl = apiBaseUrl))
        if (primary.status == HttpStatusCode.NotFound) {
            return getLegacyChangelog(reason = LegacyReason.NOT_FOUND, packageName)
        }
        return primary.bodyOrThrow()
    }

    private suspend fun getLegacyChangelog(reason: String, packageName: String): String {
        telemetryRepository.logBreadcrumb(
            message = "Changelog legacy fallback",
            attributes = mapOf(
                "packageName" to packageName,
                "reason" to reason,
            ),
        )
        return fetchMarkdown(url = legacyChangelogUrl).bodyOrThrow()
    }

    /** The response to [url], whatever its status; a failure to get one is a [NetworkException]. */
    private suspend fun fetchMarkdown(url: String): MarkdownResponse {
        telemetryRepository.logBreadcrumb(
            message = "Changelog fetch",
            attributes = mapOf("url" to url),
        )
        return networkCall {
            val response = client.get(url) {
                header(HttpHeaders.Accept, "text/markdown, text/plain;q=0.9, */*;q=0.1")
            }
            MarkdownResponse(status = response.status, body = response.bodyAsText())
        }
    }

    private object LegacyReason {
        const val BLANK_PACKAGE: String = "blank_package"
        const val NOT_FOUND: String = "not_found"
    }
}

private data class MarkdownResponse(
    val status: HttpStatusCode,
    val body: String,
)

private fun MarkdownResponse.bodyOrThrow(): String {
    status.toNetworkException()?.let { failure -> throw failure }
    return body
}
