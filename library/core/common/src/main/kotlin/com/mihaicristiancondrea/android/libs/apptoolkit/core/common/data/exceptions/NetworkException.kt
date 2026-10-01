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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions

import java.io.IOException

/**
 * A request to a remote source failed, for [reason].
 *
 * The data layer throws it in place of the HTTP client's own exception, so a caller can tell the
 * failures apart without depending on the client. `:library:core:network` translates the client's
 * exceptions into this one, and the UI layer turns it into text. The client's exception stays as
 * [cause] for reporting.
 *
 * It is an [IOException], so code that already catches I/O failures still catches it.
 */
class NetworkException(
    val reason: Reason,
    cause: Throwable? = null,
) : IOException("Network request failed: $reason", cause) {

    enum class Reason {
        /** No route to the host: the device is offline or cannot resolve it. */
        NO_INTERNET,

        /** The connection was refused, reset or closed before the response arrived. */
        CONNECTION,

        /** The request, the connection or the server took too long. */
        TIMEOUT,

        /** The secure connection could not be set up. */
        SSL,

        /** The server rejected the request itself, so sending it again fails the same way. */
        CLIENT,

        /** The server asked the client to slow down. */
        RATE_LIMITED,

        /** The server failed while handling a valid request. */
        SERVER,

        /** The server answered with a status the request does not handle, such as a redirect. */
        UNEXPECTED_RESPONSE,

        /** The response arrived but could not be read into the expected model. */
        SERIALIZATION,
    }
}
