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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.remote

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.networkCall
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.remote.models.MarkdownResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders

class ChangelogRemoteDataSource(
    private val client: HttpClient,
    private val diagnostics: DiagnosticsReporter,
) {
    /** The response to [url], whatever its status; a failure to get one is a [NetworkException]. */
    suspend fun fetchMarkdown(url: String): MarkdownResponse {
        diagnostics.logBreadcrumb(
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
}
