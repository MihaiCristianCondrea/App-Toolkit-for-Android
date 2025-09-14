package com.d4rk.android.apps.apptoolkit.app.apps.list.data.repository

import com.d4rk.android.apps.apptoolkit.app.apps.list.data.model.api.ApiResponse
import com.d4rk.android.apps.apptoolkit.app.apps.list.data.model.api.AppDataWrapper
import com.d4rk.android.apps.apptoolkit.app.apps.list.data.model.api.AppInfoDto
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class DeveloperAppsRepositoryImplTest {

    @Test
    fun `fetchDeveloperApps returns apps list`() = runTest {
        val apps = listOf(AppInfo("App", "pkg", "icon"))
        val response = ApiResponse(AppDataWrapper(apps.map { AppInfoDto(it.name, it.packageName, it.iconUrl) }))
        val json = Json.encodeToString(response)
        val client = HttpClient(MockEngine { request ->
            respond(
                content = json,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }) {
            install(ContentNegotiation) { json() }
        }
        val repository = DeveloperAppsRepositoryImpl(client, "https://example.com")

        val result = repository.fetchDeveloperApps().first()
        val success = result as DataState.Success
        assertEquals(apps, success.data)
    }

    @Test
    fun `fetchDeveloperApps emits timeout error`() = runTest {
        val client = HttpClient(MockEngine { _ ->
            respond(
                content = "",
                status = HttpStatusCode.RequestTimeout,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }) {
            install(ContentNegotiation) { json() }
        }
        val repository = DeveloperAppsRepositoryImpl(client, "https://example.com")

        val result = repository.fetchDeveloperApps().first()
        val error = result as DataState.Error
        assertEquals(Errors.Network.REQUEST_TIMEOUT, error.error)
    }

    @Test
    fun `fetchDeveloperApps sorts apps alphabetically ignoring case`() = runTest {
        val unsorted = listOf(
            AppInfo("zeta", "pkg1", "icon"),
            AppInfo("Alpha", "pkg2", "icon"),
            AppInfo("beta", "pkg3", "icon"),
        )
        val response = ApiResponse(AppDataWrapper(unsorted.map { AppInfoDto(it.name, it.packageName, it.iconUrl) }))
        val json = Json.encodeToString(response)
        val client = HttpClient(MockEngine { request ->
            respond(
                content = json,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }) {
            install(ContentNegotiation) { json() }
        }
        val repository = DeveloperAppsRepositoryImpl(client, "https://example.com")

        val result = repository.fetchDeveloperApps().first() as DataState.Success
        assertEquals(listOf("Alpha", "beta", "zeta"), result.data.map(AppInfo::name))
    }

    @Test
    fun `fetchDeveloperApps emits failed to load error on http error`() = runTest {
        val client = HttpClient(MockEngine { _ ->
            respond(
                content = "",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }) {
            install(ContentNegotiation) { json() }
        }
        val repository = DeveloperAppsRepositoryImpl(client, "https://example.com")

        val result = repository.fetchDeveloperApps().first()
        val error = result as DataState.Error
        assertEquals(Errors.UseCase.FAILED_TO_LOAD_APPS, error.error)
    }
}

