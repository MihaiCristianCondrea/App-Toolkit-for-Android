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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.remote.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.exceptions.IssueReportRejectedException
import io.ktor.http.HttpStatusCode

internal fun HttpStatusCode.toRejectedException(): IssueReportRejectedException? {
    val reason: IssueReportRejectedException.Reason = when (this) {
        HttpStatusCode.Unauthorized -> IssueReportRejectedException.Reason.UNAUTHORIZED
        HttpStatusCode.Forbidden -> IssueReportRejectedException.Reason.FORBIDDEN
        HttpStatusCode.Gone -> IssueReportRejectedException.Reason.GONE
        HttpStatusCode.UnprocessableEntity -> IssueReportRejectedException.Reason.UNPROCESSABLE
        else -> return null
    }
    return IssueReportRejectedException(reason = reason)
}
