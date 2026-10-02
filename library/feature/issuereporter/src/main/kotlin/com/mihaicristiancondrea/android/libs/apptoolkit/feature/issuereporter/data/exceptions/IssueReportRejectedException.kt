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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.exceptions

/**
 * GitHub refused to file the report, for [reason]. Sending it again fails the same way until the
 * host's token or repository setup changes. Every other failed request is a `NetworkException`.
 */
class IssueReportRejectedException(
    val reason: Reason,
) : Exception("GitHub rejected the issue report: $reason") {

    enum class Reason {
        /** HTTP 401: the token is missing, expired or invalid. */
        UNAUTHORIZED,

        /** HTTP 403: the token may not open issues in the repository. */
        FORBIDDEN,

        /** HTTP 410: issues are disabled in the repository. */
        GONE,

        /** HTTP 422: GitHub refused the issue's fields. */
        UNPROCESSABLE,
    }
}
