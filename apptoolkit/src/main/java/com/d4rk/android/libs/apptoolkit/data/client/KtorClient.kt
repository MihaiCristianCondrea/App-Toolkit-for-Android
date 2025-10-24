package com.d4rk.android.libs.apptoolkit.data.client

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

    // Using a 30s timeout gives GitHub's API enough time to respond on slower networks.
    private const val requestTimeout: Long = 30_000L
    private var client: HttpClient? = null

    /**
     * Returns a shared [HttpClient] instance for making network requests.
     *
     * The client is created once and reused on subsequent calls. It is configured with:
     * - **Android Engine:** Uses the Android engine for network operations.
     * - **Content Negotiation:** Configures JSON serialization and deserialization with lenient parsing and ignoring unknown keys.
     * - **Timeout Configuration:** Sets request, connect and socket timeouts.
     * - **Default Request Configuration:** Sets default content type and accept headers to JSON.
     */
    fun createClient(enableLogging: Boolean = false): HttpClient {
        return client ?: HttpClient(engineFactory = Android) {
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
                requestTimeoutMillis = requestTimeout
                connectTimeoutMillis = requestTimeout
                socketTimeoutMillis = requestTimeout
            }

            install(plugin = DefaultRequest) {
                contentType(type = ContentType.Application.Json)
                accept(contentType = ContentType.Application.Json)
            }
        }.also { client = it }
    }

    private fun HttpClientConfig<AndroidEngineConfig>.configureLogging(enableLogging: Boolean) {
        if (!enableLogging) return
        install(plugin = Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("KtorClient: $message")
                }
            }
            level = LogLevel.ALL
        }
    }
}