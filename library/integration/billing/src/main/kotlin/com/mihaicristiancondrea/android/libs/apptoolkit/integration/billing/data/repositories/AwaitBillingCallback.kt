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
