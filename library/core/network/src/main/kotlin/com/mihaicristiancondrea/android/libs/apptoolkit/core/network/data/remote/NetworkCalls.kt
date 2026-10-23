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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.mappers.toNetworkException
import kotlinx.coroutines.CancellationException

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
