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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry

import androidx.compose.runtime.Stable

/**
 * Crash diagnostics: breadcrumbs that trace what led to a failure, and failures reported without
 * crashing the app.
 *
 * Any layer may take it, repositories included, because a failure that never reaches a screen,
 * such as a cache write in the background or a billing callback, still has to be seen. It carries
 * no product analytics, so depending on it never turns a repository into an analytics source.
 */
@Stable
interface DiagnosticsReporter {

    /**
     * Adds a breadcrumb to the crash log, for tracing what happened before a failure.
     *
     * @param message a short label describing the event
     * @param attributes optional key/value pairs to include with the breadcrumb
     */
    fun logBreadcrumb(
        message: String,
        attributes: Map<String, String> = emptyMap(),
    )

    /**
     * Reports a ViewModel flow failure with useful context.
     *
     * Implementations should attach the ViewModel name, action identifier, and
     * throwable details as custom keys to aid debugging.
     *
     * @param viewModelName the ViewModel where the failure occurred
     * @param action label describing the flow or action that failed
     * @param throwable the exception that was caught
     * @param extraKeys optional extra context to attach as custom keys
     */
    fun reportViewModelError(
        viewModelName: String,
        action: String,
        throwable: Throwable,
        extraKeys: Map<String, String> = emptyMap(),
    )

    /**
     * Records a non-fatal throwable that has no ViewModel context.
     *
     * [reportViewModelError] is shaped around a ViewModel flow failure, which does not fit
     * throwables caught outside the presentation layer, for example an exception raised on a
     * third-party SDK's own executor and intercepted by an uncaught-exception handler.
     *
     * @param throwable the exception to record
     * @param attributes optional key/value pairs to attach as custom keys
     */
    fun recordNonFatal(
        throwable: Throwable,
        attributes: Map<String, String> = emptyMap(),
    )
}
