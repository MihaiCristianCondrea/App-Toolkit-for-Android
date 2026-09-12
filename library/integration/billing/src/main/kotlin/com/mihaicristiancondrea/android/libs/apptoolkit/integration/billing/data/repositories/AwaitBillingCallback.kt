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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.repositories

import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Accepts only the first response for one SDK request, including when a timeout callback races
 * with another response. Each retry must register a fresh callback. Cancellation wins over late
 * results; Billing provides no cancellation handle for these requests.
 */
internal suspend fun <T> awaitBillingCallback(register: ((T) -> Unit) -> Unit): T =
    suspendCancellableCoroutine { continuation ->
        val completed = AtomicBoolean(false)
        continuation.invokeOnCancellation { completed.set(true) }
        register { result ->
            if (completed.compareAndSet(false, true)) {
                continuation.resume(result)
            }
        }
    }
