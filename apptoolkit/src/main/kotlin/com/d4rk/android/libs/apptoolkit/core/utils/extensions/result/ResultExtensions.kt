package com.d4rk.android.libs.apptoolkit.core.utils.extensions.result

import kotlinx.coroutines.CancellationException

/**
 * Executes the provided [block] and wraps the outcome in a [Result].
 *
 * This helper keeps coroutine cancellation transparent by rethrowing [CancellationException],
 * while still converting any other exception into a failed [Result]. It avoids repeating
 * the same try/catch logic across data sources.
 *
 * @param T The type of the successful value.
 * @param block The suspend function to execute.
 * @return [Result.success] with the returned value or [Result.failure] with the thrown exception.
 */
suspend inline fun <T> runSuspendCatching(
    crossinline block: suspend () -> T,
): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(t)
    }
}
