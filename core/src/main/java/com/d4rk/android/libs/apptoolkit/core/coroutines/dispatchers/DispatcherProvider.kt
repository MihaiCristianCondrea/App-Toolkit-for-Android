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

package com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers

import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Abstraction for providing coroutine dispatchers.
 *
 * Having an interface allows production code to use the standard dispatchers
 * while tests can supply their own implementations to control threading.
 */
@Stable
interface DispatcherProvider {
    /** Dispatcher for work on the main thread. */
    val main: CoroutineDispatcher

    /** Dispatcher for IO-bound tasks such as network or disk operations. */
    val io: CoroutineDispatcher

    /** Dispatcher for CPU-intensive work. */
    val default: CoroutineDispatcher

    /** Dispatcher that is not confined to any specific thread. */
    val unconfined: CoroutineDispatcher
}
