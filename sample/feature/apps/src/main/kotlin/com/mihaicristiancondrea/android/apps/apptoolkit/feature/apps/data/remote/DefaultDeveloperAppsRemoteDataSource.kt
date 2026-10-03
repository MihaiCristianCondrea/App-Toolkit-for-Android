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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.mappers.toDomain
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppDetailsResponseDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppsListResponseDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.api.ApiHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions.toNetworkException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class DefaultDeveloperAppsRemoteDataSource(
    private val client: HttpClient,
    private val baseUrl: String,
) : DeveloperAppsRemoteDataSource {

    override suspend fun fetchDeveloperApps(): List<AppSummary> {
        val response = client.get(ApiHost.appsUrl(baseUrl)).requireSuccess()
        return response.body<AppsListResponseDto>().data.apps.map { it.toDomain() }
    }

    override suspend fun fetchAppDetails(packageName: String): AppDetails {
        val response = client.get(ApiHost.appDetailsUrl(packageName = packageName, baseUrl = baseUrl))
            .requireSuccess()
        return response.body<AppDetailsResponseDto>().data.app.toDomain()
    }
}

/**
 * Throws the `NetworkException` an error status stands for. The client is not set to throw on
 * error statuses, so without this an error body would be decoded as a catalogue.
 */
private fun HttpResponse.requireSuccess(): HttpResponse {
    status.toNetworkException()?.let { failure -> throw failure }
    return this
}
