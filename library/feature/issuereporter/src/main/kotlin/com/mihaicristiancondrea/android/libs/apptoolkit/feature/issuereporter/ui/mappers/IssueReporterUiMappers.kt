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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportResult
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.models.IssueReporterError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network.DataState

/** Maps the repository's send outcome onto the state the report screen renders. */
internal fun IssueReportResult.asDataState(): DataState<String, IssueReporterError> =
    when (this) {
        is IssueReportResult.Success -> DataState.Success(url)
        is IssueReportResult.Error -> DataState.Error(
            error = IssueReporterError.Http(status = status, message = message),
        )
    }
