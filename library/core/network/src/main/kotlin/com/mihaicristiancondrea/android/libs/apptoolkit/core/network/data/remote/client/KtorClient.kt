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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.android.AndroidEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * An object responsible for creating and configuring a Ktor [HttpClient].
 *
 * It provides a centralized way to create a pre-configured client instance with common settings
 * such as JSON content negotiation, request timeouts, and default request headers.
 */
object KtorClient {

    private const val REQUEST_TIMEOUT_LIMIT: Long = 30_000L

    /**
     * Creates a new [HttpClient] for making network requests.
     *
     * Each call builds a new client with its own engine and connection pool, so hold on to it: the
     * Toolkit registers one as a Koin `single`. It is configured with:
     * - **Android Engine:** Uses the Android engine for network operations.
     * - **Content Negotiation:** Configures JSON serialization and deserialization with lenient parsing and ignoring unknown keys.
     * - **Timeout Configuration:** Sets request, connect and socket timeouts.
     * - **Default Request Configuration:** Sets default content type and accept headers to JSON.
     */
    fun createClient(enableLogging: Boolean = false): HttpClient {
        return HttpClient(engineFactory = Android) {
            configureLogging(enableLogging)

            install(plugin = ContentNegotiation) {
                val jsonConfig = Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
                json(json = jsonConfig)
                json(json = jsonConfig, contentType = ContentType.Text.Plain)
            }

            install(plugin = HttpTimeout) {
                requestTimeoutMillis = REQUEST_TIMEOUT_LIMIT
                connectTimeoutMillis = REQUEST_TIMEOUT_LIMIT
                socketTimeoutMillis = REQUEST_TIMEOUT_LIMIT
            }

            install(plugin = DefaultRequest) {
                contentType(type = ContentType.Application.Json)
                accept(contentType = ContentType.Application.Json)
            }
        }
    }

    /**
     * Installs the [Logging] plugin when [enableLogging] is true, printing each request and
     * response line with its headers, prefixed with "KtorClient:".
     *
     * Bodies are not logged and the `Authorization` header is masked: an issue report carries a
     * GitHub token in that header and the reporter's email in its body, and both would otherwise
     * land in logcat.
     */
    private fun HttpClientConfig<AndroidEngineConfig>.configureLogging(enableLogging: Boolean) {
        if (!enableLogging) return
        install(plugin = Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("KtorClient: $message")
                }
            }
            level = LogLevel.HEADERS
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
    }
}