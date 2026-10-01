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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.Errors
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import org.junit.jupiter.api.Test
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame

class NetworkExceptionsTest {

    @Test
    fun `translates transport failures and keeps them as the cause`() {
        val offline = UnknownHostException()
        val translated = offline.toNetworkException()

        assertEquals(NetworkException.Reason.NO_INTERNET, translated?.reason)
        assertSame(offline, translated?.cause)
        assertEquals(NetworkException.Reason.NO_INTERNET, ConnectException().toNetworkException()?.reason)
        assertEquals(NetworkException.Reason.TIMEOUT, SocketTimeoutException().toNetworkException()?.reason)
        assertEquals(NetworkException.Reason.SSL, SSLHandshakeException("handshake").toNetworkException()?.reason)
        assertEquals(NetworkException.Reason.CONNECTION, EOFException().toNetworkException()?.reason)
        assertEquals(
            NetworkException.Reason.SERIALIZATION,
            SerializationException("bad body").toNetworkException()?.reason,
        )
    }

    @Test
    fun `leaves cancellation and other failures alone`() {
        assertNull(CancellationException().toNetworkException())
        assertNull(IllegalStateException().toNetworkException())
    }

    @Test
    fun `returns an existing network exception unchanged`() {
        val exception = NetworkException(NetworkException.Reason.SERVER)

        assertSame(exception, exception.toNetworkException())
    }

    @Test
    fun `maps response statuses`() {
        assertNull(HttpStatusCode.OK.toNetworkException())
        assertEquals(NetworkException.Reason.TIMEOUT, HttpStatusCode.RequestTimeout.toNetworkException()?.reason)
        assertEquals(NetworkException.Reason.RATE_LIMITED, HttpStatusCode.TooManyRequests.toNetworkException()?.reason)
        assertEquals(NetworkException.Reason.CLIENT, HttpStatusCode.NotFound.toNetworkException()?.reason)
        assertEquals(NetworkException.Reason.SERVER, HttpStatusCode.BadGateway.toNetworkException()?.reason)
        assertEquals(
            NetworkException.Reason.UNEXPECTED_RESPONSE,
            HttpStatusCode.MovedPermanently.toNetworkException()?.reason,
        )
    }

    @Test
    fun `network call rethrows a network failure translated`() = runTest {
        val thrown = runCatching { networkCall<Unit> { throw UnknownHostException() } }.exceptionOrNull()

        assertEquals(NetworkException.Reason.NO_INTERNET, assertIs<NetworkException>(thrown).reason)
    }

    @Test
    fun `network call passes other failures through`() = runTest {
        val failure = IllegalStateException("bug")

        val thrown = runCatching { networkCall<Unit> { throw failure } }.exceptionOrNull()

        assertSame(failure, thrown)
    }

    @Test
    fun `legacy errors still classify translated exceptions`() {
        assertEquals(Errors.Network.NO_INTERNET, NetworkException(NetworkException.Reason.NO_INTERNET).toError())
        assertEquals(Errors.Network.HTTP_SERVER_ERROR, NetworkException(NetworkException.Reason.SERVER).toError())
    }
}
