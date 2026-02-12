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
