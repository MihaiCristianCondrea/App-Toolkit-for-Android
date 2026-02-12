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

package com.d4rk.android.apps.apptoolkit.app.apps.list.data.repository

import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.ApiResponseDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppCategoryDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppDataWrapperDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppInfoDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.repository.DeveloperAppsRepositoryImpl
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppCategory
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class DeveloperAppsRepositoryImplTest {

    @Test
    fun `fetchDeveloperApps returns apps list`() = runTest {
        val apps = listOf(
            AppInfo(
                name = "App",
                packageName = "pkg",
                iconUrl = "icon",
                description = "Description",
                screenshots = emptyList(),
            )
        )
        val response = ApiResponseDto(AppDataWrapperDto(apps.map {
            AppInfoDto(
                it.name,
                it.packageName,
                it.iconUrl
            )
        }))
        val json = Json.encodeToString(response)
        val client = HttpClient(MockEngine { request ->
            respond(
                content = json,
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }) {
            install(ContentNegotiation) { json() }
        }
        val repository = DeveloperAppsRepositoryImpl(
            client = client,
            baseUrl = "https://example.com",
            firebaseController = mockk<FirebaseController>(relaxed = true)
        )

        val result = repository.fetchDeveloperApps().first()
        val success = result as DataState.Success
        assertEquals(apps, success.data)
    }

    @Test
    fun `fetchDeveloperApps maps category information`() = runTest {
        val expectedCategory = AppCategory(label = "Education", id = "education")
        val response = ApiResponseDto(
            AppDataWrapperDto(
                listOf(
                    AppInfoDto(
                        name = "App",
                        packageName = "pkg",
                        iconUrl = "icon",
                        category = AppCategoryDto(
                            label = expectedCategory.label,
                            categoryId = expectedCategory.id
                        ),
                    )
                )
            )
        )
        val json = Json.encodeToString(response)
        val client = HttpClient(MockEngine { request ->
            respond(
                content = json,
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }) {
            install(ContentNegotiation) { json() }
        }
        val repository = DeveloperAppsRepositoryImpl(
            client = client,
            baseUrl = "https://example.com",
            firebaseController = mockk<FirebaseController>(relaxed = true)
        )

        val result = repository.fetchDeveloperApps().first() as DataState.Success
        val category = result.data.first().category

        assertEquals(expectedCategory, category)
    }

    @Test
    fun `fetchDeveloperApps emits timeout error`() = runTest {
        val client = HttpClient(MockEngine { _ ->
            respond(
                content = "",
                status = HttpStatusCode.RequestTimeout,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }) {
            install(ContentNegotiation) { json() }
        }
        val repository = DeveloperAppsRepositoryImpl(
            client = client,
            baseUrl = "https://example.com",
            firebaseController = mockk<FirebaseController>(relaxed = true)
        )

        val result = repository.fetchDeveloperApps().first()
        val error = result as DataState.Error
        assertEquals(AppErrors.Common(Errors.Network.REQUEST_TIMEOUT), error.error)
    }

    @Test
    fun `fetchDeveloperApps sorts apps alphabetically ignoring case`() = runTest {
        val unsorted = listOf(
            AppInfo(
                name = "zeta",
                packageName = "pkg1",
                iconUrl = "icon",
                description = "Description",
                screenshots = emptyList(),
            ),
            AppInfo(
                name = "Alpha",
                packageName = "pkg2",
                iconUrl = "icon",
                description = "Description",
                screenshots = emptyList(),
            ),
            AppInfo(
                name = "beta",
                packageName = "pkg3",
                iconUrl = "icon",
                description = "Description",
                screenshots = emptyList(),
            ),
        )
        val response = ApiResponseDto(AppDataWrapperDto(unsorted.map {
            AppInfoDto(
                it.name,
                it.packageName,
                it.iconUrl
            )
        }))
        val json = Json.encodeToString(response)
        val client = HttpClient(MockEngine { request ->
            respond(
                content = json,
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }) {
            install(ContentNegotiation) { json() }
        }
        val repository = DeveloperAppsRepositoryImpl(
            client = client,
            baseUrl = "https://example.com",
            firebaseController = mockk<FirebaseController>(relaxed = true)
        )

        val result = repository.fetchDeveloperApps().first() as DataState.Success
        assertEquals(listOf("Alpha", "beta", "zeta"), result.data.map(AppInfo::name))
    }

    @Test
    fun `fetchDeveloperApps emits failed to load error on http error`() = runTest {
        val client = HttpClient(MockEngine { _ ->
            respond(
                content = "",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }) {
            install(ContentNegotiation) { json() }
        }
        val repository = DeveloperAppsRepositoryImpl(
            client = client,
            baseUrl = "https://example.com",
            firebaseController = mockk<FirebaseController>(relaxed = true)
        )

        val result = repository.fetchDeveloperApps().first()
        val error = result as DataState.Error
        assertEquals(AppErrors.Common(Errors.Network.HTTP_CLIENT_ERROR), error.error)
    }
}
