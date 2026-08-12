/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.list.data.repository

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.model.AppCategoryDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.model.AppDetailsDataDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.model.AppDetailsDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.model.AppDetailsResponseDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.model.AppLatestVersionDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.model.AppLinkDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.model.AppScreenshotDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.model.AppSummaryDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.model.AppsListDataDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.model.AppsListResponseDto
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repository.DefaultDeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.model.AppCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.model.AppDeviceType
import com.mihaicristiancondrea.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.Errors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.FirebaseController
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
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DefaultDeveloperAppsRepositoryTest {

    @Test
    fun `fetchDeveloperApps maps compact list data and category`() = runTest {
        val response = AppsListResponseDto(
            data = AppsListDataDto(
                apps = listOf(
                    AppSummaryDto(
                        name = "App",
                        packageName = "pkg",
                        iconUrl = "https://example.com/icon.png",
                        shortDescription = "Short",
                        category = AppCategoryDto(
                            label = "Education",
                            categoryId = "education",
                        ),
                    ),
                ),
            ),
        )
        val repository = repositoryReturning(Json.encodeToString(response))

        val result = repository.fetchDeveloperApps().first() as DataState.Success
        val app = result.data.single()

        assertEquals("App", app.name)
        assertEquals("pkg", app.packageName)
        assertEquals("Short", app.shortDescription)
        assertEquals(AppCategory(label = "Education", id = "education"), app.category)
    }

    @Test
    fun `fetchDeveloperApps sorts names alphabetically ignoring case`() = runTest {
        val response = AppsListResponseDto(
            data = AppsListDataDto(
                apps = listOf("zeta", "Alpha", "beta").mapIndexed { index, name ->
                    AppSummaryDto(
                        name = name,
                        packageName = "pkg$index",
                        iconUrl = "https://example.com/$index.png",
                    )
                },
            ),
        )
        val repository = repositoryReturning(Json.encodeToString(response))

        val result = repository.fetchDeveloperApps().first() as DataState.Success

        assertEquals(listOf("Alpha", "beta", "zeta"), result.data.map { it.name })
    }

    @Test
    fun `fetchAppDetails requests package route and maps full metadata`() = runTest {
        val response = AppDetailsResponseDto(
            data = AppDetailsDataDto(
                app = AppDetailsDto(
                    name = "App",
                    packageName = "com.example.app",
                    iconUrl = "https://example.com/icon.png",
                    description = "Full description",
                    shortDescription = "Short",
                    screenshots = listOf(
                        AppScreenshotDto(
                            url = "https://example.com/phone.png",
                            aspectRatio = "9:16",
                            deviceType = "phone",
                        ),
                    ),
                    links = listOf(
                        AppLinkDto(
                            label = "Play Store",
                            url = "https://example.com/store",
                        ),
                    ),
                    latestVersion = AppLatestVersionDto(
                        versionName = "2.0.0",
                        versionCode = 20,
                    ),
                ),
            ),
        )
        val client = HttpClient(
            MockEngine { request ->
                assertEquals("/api/v1/apps/com.example.app", request.url.encodedPath)
                respondJson(Json.encodeToString(response))
            },
        ) {
            install(ContentNegotiation) { json() }
        }
        val repository = createRepository(client)

        val result = repository.fetchAppDetails("com.example.app").first() as DataState.Success
        val details = result.data

        assertEquals("Full description", details.description)
        assertEquals(AppDeviceType.Phone, details.screenshots.single().deviceType)
        assertEquals("Play Store", details.links.single().label)
        assertEquals("2.0.0", details.latestVersion?.versionName)
    }

    @Test
    fun `fetchDeveloperApps maps timeout status`() = runTest {
        val repository = repositoryWithStatus(HttpStatusCode.RequestTimeout)

        val result = repository.fetchDeveloperApps().first() as DataState.Error

        assertEquals(AppErrors.Common(Errors.Network.REQUEST_TIMEOUT), result.error)
    }

    @Test
    fun `fetchAppDetails rejects a blank package`() = runTest {
        val repository = repositoryReturning("{}")

        val result = repository.fetchAppDetails(" ").first() as DataState.Error

        assertEquals(AppErrors.UseCase.FAILED_TO_LOAD_APP_DETAILS, result.error)
    }

    private fun repositoryReturning(json: String): DefaultDeveloperAppsRepository {
        val client = HttpClient(MockEngine { respondJson(json) }) {
            install(ContentNegotiation) { json() }
        }
        return createRepository(client)
    }

    private fun repositoryWithStatus(status: HttpStatusCode): DefaultDeveloperAppsRepository {
        val client = HttpClient(
            MockEngine {
                respond(
                    content = "",
                    status = status,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString(),
                    ),
                )
            },
        ) {
            install(ContentNegotiation) { json() }
        }
        return createRepository(client)
    }

    private fun createRepository(client: HttpClient): DefaultDeveloperAppsRepository =
        DefaultDeveloperAppsRepository(
            client = client,
            baseUrl = "https://example.com",
            firebaseController = mockk<FirebaseController>(relaxed = true),
        )
}

private fun io.ktor.client.engine.mock.MockRequestHandleScope.respondJson(
    json: String,
) = respond(
    content = json,
    status = HttpStatusCode.OK,
    headers = headersOf(
        HttpHeaders.ContentType,
        ContentType.Application.Json.toString(),
    ),
)
