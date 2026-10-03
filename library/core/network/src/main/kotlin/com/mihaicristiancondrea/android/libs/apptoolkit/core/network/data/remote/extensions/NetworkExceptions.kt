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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.extensions

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import io.ktor.client.network.sockets.ConnectTimeoutException as KtorConnectTimeoutException

/**
 * Runs [block], a call to a remote source, and rethrows a network failure as [NetworkException].
 *
 * Repositories wrap their remote calls in it so their callers see one exception type whatever the
 * HTTP client threw. Cancellation and failures that are not network failures pass through
 * unchanged.
 */
suspend inline fun <T> networkCall(crossinline block: suspend () -> T): T =
    try {
        block()
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (throwable: Throwable) {
        throw throwable.toNetworkException() ?: throwable
    }

/**
 * Translates this into a [NetworkException], keeping it as the cause, or returns `null` when it is
 * not a network failure.
 */
fun Throwable.toNetworkException(): NetworkException? {
    val reason: NetworkException.Reason = when (this) {
        is NetworkException -> return this
        is CancellationException -> return null

        is HttpRequestTimeoutException,
        is KtorConnectTimeoutException,
        is SocketTimeoutException -> NetworkException.Reason.TIMEOUT

        is ClientRequestException -> response.status.toNetworkReason() ?: NetworkException.Reason.CLIENT
        is ServerResponseException -> NetworkException.Reason.SERVER
        is RedirectResponseException,
        is ResponseException -> NetworkException.Reason.UNEXPECTED_RESPONSE

        // A refused connect is also what an offline device reports for a host it already resolved.
        is UnknownHostException,
        is ConnectException -> NetworkException.Reason.NO_INTERNET
        is SSLException -> NetworkException.Reason.SSL
        is SerializationException -> NetworkException.Reason.SERIALIZATION
        is IOException -> NetworkException.Reason.CONNECTION

        else -> return null
    }
    return NetworkException(reason = reason, cause = this)
}

/**
 * Returns the [NetworkException] a response with this status stands for, or `null` for a success.
 *
 * For a repository that reads the status itself instead of letting the client throw.
 */
fun HttpStatusCode.toNetworkException(): NetworkException? =
    toNetworkReason()?.let { reason -> NetworkException(reason = reason) }

private fun HttpStatusCode.toNetworkReason(): NetworkException.Reason? = when {
    isSuccess() -> null
    this == HttpStatusCode.RequestTimeout -> NetworkException.Reason.TIMEOUT
    this == HttpStatusCode.TooManyRequests -> NetworkException.Reason.RATE_LIMITED
    value in 400..499 -> NetworkException.Reason.CLIENT
    value >= 500 -> NetworkException.Reason.SERVER
    else -> NetworkException.Reason.UNEXPECTED_RESPONSE
}
