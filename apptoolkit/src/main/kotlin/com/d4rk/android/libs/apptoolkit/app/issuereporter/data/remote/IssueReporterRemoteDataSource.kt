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

package com.d4rk.android.libs.apptoolkit.app.issuereporter.data.remote

import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.remote.model.CreateIssueRequest
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.IssueReportResult
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
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

class IssueReporterRemoteDataSource(
    private val client: HttpClient,
) {

    suspend fun createIssue(
        payload: CreateIssueRequest,
        target: GithubTarget,
        token: String?,
    ): IssueReportResult {
        val url = "https://api.github.com/repos/${target.username}/${target.repository}/issues"
        val response = client.post(url) {
            contentType(ContentType.Application.Json)
            header("Accept", "application/vnd.github+json")
            token?.let { header("Authorization", "Bearer $it") }
            setBody(Json.encodeToString(CreateIssueRequest.serializer(), payload))
        }

        val responseBody = response.bodyAsText()
        if (response.status == HttpStatusCode.Created) {
            val json = Json.parseToJsonElement(responseBody).jsonObject
            val issueUrl = json["html_url"]?.jsonPrimitive?.content ?: ""
            return IssueReportResult.Success(issueUrl)
        }

        return IssueReportResult.Error(response.status, responseBody)
    }
}
