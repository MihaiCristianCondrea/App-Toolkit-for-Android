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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.remote

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.toNetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.exceptions.IssueReportRejectedException
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.remote.models.CreateIssueRequest
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.GithubTarget
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Files issues through the GitHub REST API. The token is sent only as the `Authorization` header
 * and never appears in a thrown exception.
 */
class IssueReporterRemoteDataSource(
    private val client: HttpClient,
) {

    /**
     * Files [payload] in [target] and returns the created issue's web URL, which is empty when
     * GitHub leaves it out.
     *
     * @throws IssueReportRejectedException when GitHub refuses the token, the repository or the
     * issue's fields.
     * @throws NetworkException when GitHub answers with any other failure.
     */
    suspend fun createIssue(
        payload: CreateIssueRequest,
        target: GithubTarget,
        token: String?,
    ): String {
        val url = "https://api.github.com/repos/${target.username}/${target.repository}/issues"
        val response = client.post(url) {
            contentType(ContentType.Application.Json)
            header("Accept", "application/vnd.github+json")
            token?.let { header("Authorization", "Bearer $it") }
            setBody(Json.encodeToString(CreateIssueRequest.serializer(), payload))
        }

        if (response.status != HttpStatusCode.Created) {
            throw response.status.toRejectedException()
                ?: response.status.toNetworkException()
                ?: NetworkException(reason = NetworkException.Reason.UNEXPECTED_RESPONSE)
        }

        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        return json["html_url"]?.jsonPrimitive?.content.orEmpty()
    }
}

private fun HttpStatusCode.toRejectedException(): IssueReportRejectedException? {
    val reason: IssueReportRejectedException.Reason = when (this) {
        HttpStatusCode.Unauthorized -> IssueReportRejectedException.Reason.UNAUTHORIZED
        HttpStatusCode.Forbidden -> IssueReportRejectedException.Reason.FORBIDDEN
        HttpStatusCode.Gone -> IssueReportRejectedException.Reason.GONE
        HttpStatusCode.UnprocessableEntity -> IssueReportRejectedException.Reason.UNPROCESSABLE
        else -> return null
    }
    return IssueReportRejectedException(reason = reason)
}
