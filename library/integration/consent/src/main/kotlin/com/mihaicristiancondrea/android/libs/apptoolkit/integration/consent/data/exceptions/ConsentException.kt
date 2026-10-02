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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.exceptions

/**
 * A consent round trip ended without an answer from UMP, for [reason].
 *
 * The message carries UMP's own error text when there is one. Callers decide what to show; the
 * consent choice already stored stays as it was.
 */
class ConsentException(
    val reason: Reason,
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {

    enum class Reason {
        /** The host was finishing, destroyed or off screen, so no form could show. A live host can ask again. */
        HOST_UNAVAILABLE,

        /** UMP could not update the consent information, or the round trip ended without an answer. */
        REQUEST_FAILED,

        /** The consent form could not be loaded or shown, or it was dismissed with an error. */
        FORM_FAILED,
    }
}
