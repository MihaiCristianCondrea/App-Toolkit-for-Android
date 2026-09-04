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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.api.ApiHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.toError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

/**
 * Fetches changelog Markdown from the public Android App Metadata API.
 *
 * Change rationale: changelogs previously came directly from a GitHub raw URL in Compose. The
 * Worker package endpoint is now authoritative, while [legacyChangelogUrl] remains a compatibility
 * fallback only when the package is blank or the public endpoint returns HTTP 404.
 */
class DefaultChangelogRepository(
    private val client: HttpClient,
    private val apiBaseUrl: String,
    private val legacyChangelogUrl: String,
    private val firebaseController: FirebaseController,
) : ChangelogRepository {

    override fun fetchChangelog(packageName: String): Flow<DataState<String, Errors>> = flow {
        val primaryUrl = packageName.takeIf(String::isNotBlank)?.let { validPackageName ->
            ApiHost.appChangelogUrl(
                packageName = validPackageName,
                baseUrl = apiBaseUrl,
            )
        }
        val primaryResponse = primaryUrl?.let { url -> fetchMarkdown(url = url) }

        when {
            primaryResponse == null -> emitLegacyChangelog(packageName = packageName)

            primaryResponse.isFailure -> emit(
                DataState.Error(
                    error = primaryResponse.exceptionOrNull()
                        ?.toError(default = Errors.Network.UNKNOWN)
                        ?: Errors.Network.UNKNOWN,
                ),
            )

            primaryResponse.getOrThrow().status.isSuccess() -> emit(
                DataState.Success(data = primaryResponse.getOrThrow().body),
            )

            primaryResponse.getOrThrow().status == HttpStatusCode.NotFound ->
                emitLegacyChangelog(packageName = packageName)

            else -> emit(
                DataState.Error(error = primaryResponse.getOrThrow().status.toDomainError()),
            )
        }
    }

    private suspend fun FlowCollector<DataState<String, Errors>>.emitLegacyChangelog(
        packageName: String,
    ) {
        firebaseController.logBreadcrumb(
            message = "Changelog legacy fallback",
            attributes = mapOf(
                "packageName" to packageName,
                "reason" to if (packageName.isBlank()) "blank_package" else "not_found",
            ),
        )
        val fallbackResponse = fetchMarkdown(url = legacyChangelogUrl)
        fallbackResponse.fold(
            onSuccess = { response ->
                if (response.status.isSuccess()) {
                    emit(DataState.Success(data = response.body))
                } else {
                    emit(DataState.Error(error = response.status.toDomainError()))
                }
            },
            onFailure = { throwable ->
                emit(
                    DataState.Error(
                        error = throwable.toError(default = Errors.Network.UNKNOWN),
                    ),
                )
            },
        )
    }

    private suspend fun fetchMarkdown(url: String): Result<MarkdownResponse> {
        firebaseController.logBreadcrumb(
            message = "Changelog fetch",
            attributes = mapOf("url" to url),
        )
        return runSuspendCatching {
            val response = client.get(url) {
                header(HttpHeaders.Accept, "text/markdown, text/plain;q=0.9, */*;q=0.1")
            }
            MarkdownResponse(
                status = response.status,
                body = response.bodyAsText(),
            )
        }
    }
}

private data class MarkdownResponse(
    val status: HttpStatusCode,
    val body: String,
)

private fun HttpStatusCode.toDomainError(): Errors = when {
    this == HttpStatusCode.RequestTimeout -> Errors.Network.REQUEST_TIMEOUT
    this == HttpStatusCode.TooManyRequests -> Errors.Network.RATE_LIMITED
    value in 300..399 -> Errors.Network.HTTP_REDIRECT
    value in 400..499 -> Errors.Network.HTTP_CLIENT_ERROR
    value >= 500 -> Errors.Network.HTTP_SERVER_ERROR
    else -> Errors.Network.UNKNOWN
}
