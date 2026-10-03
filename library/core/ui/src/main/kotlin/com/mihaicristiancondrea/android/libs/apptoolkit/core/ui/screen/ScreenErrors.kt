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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R

/**
 * The text every screen shows for a failure it has nothing more specific to say about.
 */
val GenericErrorText: UiTextHelper = UiTextHelper.StringResource(R.string.screen_error_generic)

/**
 * The text a screen shows for this failure.
 *
 * Failures the user can act on get their own text: being offline, a timeout, a busy server, full
 * storage. Every other failure, including a bug, gets [fallback], which a screen sets to what it
 * was doing ("Could not load the FAQ"), because the cause would mean nothing to the user and is
 * already reported to Crashlytics.
 *
 * A screen with failures of its own handles them first and passes the rest here:
 *
 * ```kotlin
 * private fun Throwable.toFaqText(): UiTextHelper = when (this) {
 *     is FaqUnavailableException -> UiTextHelper.StringResource(R.string.faq_unavailable)
 *     else -> toUiText(fallback = UiTextHelper.StringResource(R.string.faq_load_failed))
 * }
 * ```
 */
fun Throwable.toUiText(fallback: UiTextHelper = GenericErrorText): UiTextHelper = when (this) {
    is NetworkException -> when (reason) {
        NetworkException.Reason.NO_INTERNET -> UiTextHelper.StringResource(R.string.screen_error_no_internet)
        NetworkException.Reason.CONNECTION -> UiTextHelper.StringResource(R.string.screen_error_connection)
        NetworkException.Reason.TIMEOUT -> UiTextHelper.StringResource(R.string.screen_error_timeout)
        NetworkException.Reason.RATE_LIMITED -> UiTextHelper.StringResource(R.string.screen_error_rate_limited)
        NetworkException.Reason.SERVER -> UiTextHelper.StringResource(R.string.screen_error_server)
        NetworkException.Reason.SSL,
        NetworkException.Reason.CLIENT,
        NetworkException.Reason.UNEXPECTED_RESPONSE,
        NetworkException.Reason.SERIALIZATION -> fallback
    }

    is StorageException -> when (reason) {
        StorageException.Reason.FULL -> UiTextHelper.StringResource(R.string.screen_error_storage_full)
        StorageException.Reason.BUSY -> UiTextHelper.StringResource(R.string.screen_error_storage_busy)
        StorageException.Reason.CORRUPT,
        StorageException.Reason.UNAVAILABLE,
        StorageException.Reason.FAILED -> fallback
    }

    else -> fallback
}

/**
 * Whether trying the same operation again can succeed.
 *
 * Not when the server rejected the request, answered in a way the app cannot handle, or the stored
 * data is unreadable: those fail the same way every time. Anything else, a bug included, is worth
 * one more try.
 */
val Throwable.isRetryable: Boolean
    get() = when (this) {
        is NetworkException -> reason !in NonRetryableNetworkReasons
        is StorageException -> reason != StorageException.Reason.CORRUPT
        else -> true
    }

/**
 * This failure as the [Loadable.Failed] state of a screen's content: [toUiText] with [fallback],
 * and a retry when [retryable].
 */
fun Throwable.toFailed(
    fallback: UiTextHelper = GenericErrorText,
    retryable: Boolean = isRetryable,
): Loadable.Failed = Loadable.Failed(message = toUiText(fallback), retryable = retryable)

/**
 * This failure as an error [UiMessage]: [toUiText] with [fallback].
 */
fun Throwable.toErrorMessage(fallback: UiTextHelper = GenericErrorText): UiMessage =
    UiMessage(text = toUiText(fallback), isError = true)

private val NonRetryableNetworkReasons: Set<NetworkException.Reason> = setOf(
    NetworkException.Reason.CLIENT,
    NetworkException.Reason.UNEXPECTED_RESPONSE,
    NetworkException.Reason.SERIALIZATION,
)
