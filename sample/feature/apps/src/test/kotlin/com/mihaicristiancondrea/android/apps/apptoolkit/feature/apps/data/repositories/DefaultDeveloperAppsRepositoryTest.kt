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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.local.DeveloperAppsLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.DefaultDeveloperAppsRemoteDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppCategoryDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppDetailsDataDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppDetailsDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppDetailsResponseDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppLatestVersionDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppLinkDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppScreenshotDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppSummaryDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppsListDataDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.remote.models.AppsListResponseDto
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppDeviceType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppSummary
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.UnknownHostException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultDeveloperAppsRepositoryTest {

    private val telemetryRepository: TelemetryRepository = mockk(relaxed = true)

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

        val app = repository.fetchDeveloperApps().single()

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

        val apps = repository.fetchDeveloperApps()

        assertEquals(listOf("Alpha", "beta", "zeta"), apps.map { it.name })
    }

    @Test
    fun `fetchDeveloperApps keeps one entry per package`() = runTest {
        val response = AppsListResponseDto(
            data = AppsListDataDto(
                apps = listOf("First", "Second").map { name ->
                    AppSummaryDto(
                        name = name,
                        packageName = "com.example.duplicate",
                        iconUrl = "https://example.com/$name.png",
                    )
                },
            ),
        )
        val repository = repositoryReturning(Json.encodeToString(response))

        val apps = repository.fetchDeveloperApps()

        assertEquals(listOf("First"), apps.map { it.name })
    }

    @Test
    fun `fetchDeveloperApps saves a successful response`() = runTest {
        val local = FakeDeveloperAppsLocalDataSource()
        val repository = repositoryReturning(Json.encodeToString(catalogueResponse("Online")), local)

        repository.fetchDeveloperApps()

        assertEquals(listOf("Online"), local.value?.map { it.name })
    }

    @Test
    fun `fetchDeveloperApps still succeeds and reports when the cache cannot be written`() = runTest {
        val failingCache = object : DeveloperAppsLocalDataSource {
            override suspend fun read(): List<AppSummary>? = null

            override suspend fun write(value: List<AppSummary>) {
                throw IOException("Disk full")
            }
        }
        val repository = repositoryReturning(Json.encodeToString(catalogueResponse("App")), local = failingCache)

        val apps = repository.fetchDeveloperApps()

        assertEquals(listOf("App"), apps.map { it.name })
        verify { telemetryRepository.recordNonFatal(throwable = any(), attributes = any()) }
    }

    @Test
    fun `fetchDeveloperApps throws a timeout for a timeout status`() = runTest {
        val repository = repositoryWithStatus(HttpStatusCode.RequestTimeout)

        val failure = assertFailsWith<NetworkException> { repository.fetchDeveloperApps() }

        assertEquals(NetworkException.Reason.TIMEOUT, failure.reason)
    }

    @Test
    fun `fetchDeveloperApps throws a server failure for a server status`() = runTest {
        val repository = repositoryWithStatus(HttpStatusCode.InternalServerError)

        val failure = assertFailsWith<NetworkException> { repository.fetchDeveloperApps() }

        assertEquals(NetworkException.Reason.SERVER, failure.reason)
    }

    @Test
    fun `fetchDeveloperApps throws no internet when the host cannot be resolved`() = runTest {
        val client = HttpClient(MockEngine { throw UnknownHostException("example.com") }) {
            install(ContentNegotiation) { json() }
        }
        val repository = createRepository(client)

        val failure = assertFailsWith<NetworkException> { repository.fetchDeveloperApps() }

        assertEquals(NetworkException.Reason.NO_INTERNET, failure.reason)
    }

    @Test
    fun `a failed fetch keeps the saved catalogue`() = runTest {
        val local = FakeDeveloperAppsLocalDataSource(cachedApps("Cached"))
        val repository = repositoryWithStatus(HttpStatusCode.InternalServerError, local)

        assertFailsWith<NetworkException> { repository.fetchDeveloperApps() }

        assertEquals(listOf("Cached"), repository.savedDeveloperApps()?.map { it.name })
    }

    @Test
    fun `savedDeveloperApps keeps one entry per package`() = runTest {
        val local = FakeDeveloperAppsLocalDataSource(cachedApps("Cached") + cachedApps("Cached"))
        val repository = repositoryReturning("{}", local)

        assertEquals(listOf("Cached"), repository.savedDeveloperApps()?.map { it.name })
    }

    @Test
    fun `savedDeveloperApps is null before anything was saved`() = runTest {
        val repository = repositoryReturning("{}")

        assertNull(repository.savedDeveloperApps())
    }

    @Test
    fun `savedDeveloperApps throws a storage failure when the file cannot be read`() = runTest {
        val unreadableCache = object : DeveloperAppsLocalDataSource {
            override suspend fun read(): List<AppSummary>? = throw IOException("Read failed")

            override suspend fun write(value: List<AppSummary>) = Unit
        }
        val repository = repositoryReturning("{}", local = unreadableCache)

        val failure = assertFailsWith<StorageException> { repository.savedDeveloperApps() }

        assertEquals(StorageException.Reason.FAILED, failure.reason)
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

        val details = repository.fetchAppDetails("com.example.app")

        assertEquals("Full description", details.description)
        assertEquals(AppDeviceType.Phone, details.screenshots.single().deviceType)
        assertEquals("Play Store", details.links.single().label)
        assertEquals("2.0.0", details.latestVersion?.versionName)
    }

    @Test
    fun `fetchAppDetails throws the status failure`() = runTest {
        val repository = repositoryWithStatus(HttpStatusCode.NotFound)

        val failure = assertFailsWith<NetworkException> { repository.fetchAppDetails("com.example.app") }

        assertEquals(NetworkException.Reason.CLIENT, failure.reason)
    }

    @Test
    fun `fetchAppDetails rejects a blank package`() = runTest {
        val repository = repositoryReturning("{}")

        assertFailsWith<IllegalArgumentException> { repository.fetchAppDetails(" ") }
    }

    private fun repositoryReturning(
        json: String,
        local: DeveloperAppsLocalDataSource = FakeDeveloperAppsLocalDataSource(),
    ): DefaultDeveloperAppsRepository {
        val client = HttpClient(MockEngine { respondJson(json) }) {
            install(ContentNegotiation) { json() }
        }
        return createRepository(client, local)
    }

    private fun repositoryWithStatus(
        status: HttpStatusCode,
        local: DeveloperAppsLocalDataSource = FakeDeveloperAppsLocalDataSource(),
    ): DefaultDeveloperAppsRepository {
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
        return createRepository(client, local)
    }

    private fun createRepository(
        client: HttpClient,
        local: DeveloperAppsLocalDataSource = FakeDeveloperAppsLocalDataSource(),
    ): DefaultDeveloperAppsRepository =
        DefaultDeveloperAppsRepository(
            remoteDataSource = DefaultDeveloperAppsRemoteDataSource(
                client = client,
                baseUrl = "https://example.com",
            ),
            telemetryRepository = telemetryRepository,
            localDataSource = local,
        )

    private fun cachedApps(name: String): List<AppSummary> = listOf(
        AppSummary(
            name = name,
            packageName = "com.example.${name.lowercase()}",
            iconUrl = "https://example.com/$name.png",
        ),
    )

    private fun catalogueResponse(name: String): AppsListResponseDto = AppsListResponseDto(
        data = AppsListDataDto(
            apps = listOf(
                AppSummaryDto(
                    name = name,
                    packageName = "com.example.${name.lowercase()}",
                    iconUrl = "https://example.com/$name.png",
                ),
            ),
        ),
    )
}

private class FakeDeveloperAppsLocalDataSource(
    var value: List<AppSummary>? = null,
) : DeveloperAppsLocalDataSource {
    override suspend fun read(): List<AppSummary>? = value

    override suspend fun write(value: List<AppSummary>) {
        this.value = value
    }
}

private fun MockRequestHandleScope.respondJson(
    json: String,
) = respond(
    content = json,
    status = HttpStatusCode.OK,
    headers = headersOf(
        HttpHeaders.ContentType,
        ContentType.Application.Json.toString(),
    ),
)
