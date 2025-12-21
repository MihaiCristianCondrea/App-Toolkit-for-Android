package com.d4rk.android.libs.apptoolkit.core.di

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
