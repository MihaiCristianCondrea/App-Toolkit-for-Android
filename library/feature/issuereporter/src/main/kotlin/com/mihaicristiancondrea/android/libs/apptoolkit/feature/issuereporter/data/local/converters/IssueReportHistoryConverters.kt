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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.local.converters

import androidx.datastore.preferences.core.Preferences
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.constants.IssueReportHistoryConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.FiledIssueReport
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.IssueReportHistory

internal fun Preferences.toIssueReportHistory(): IssueReportHistory = IssueReportHistory(
    lastSubmittedAt = this[IssueReportHistoryConstants.LAST_SUBMITTED_AT_KEY],
    reports = this[IssueReportHistoryConstants.RECENT_REPORTS_KEY]
        .orEmpty()
        .mapNotNull { value -> value.decodeIssueReport() },
)

internal fun FiledIssueReport.encode(): String =
    "$submittedAt${IssueReportHistoryConstants.ENTRY_SEPARATOR}$fingerprint"

internal fun String.decodeIssueReport(): FiledIssueReport? {
    val submittedAt: Long = substringBefore(delimiter = IssueReportHistoryConstants.ENTRY_SEPARATOR)
        .toLongOrNull() ?: return null
    val fingerprint: String = substringAfter(
        delimiter = IssueReportHistoryConstants.ENTRY_SEPARATOR,
        missingDelimiterValue = "",
    )
    return if (fingerprint.isEmpty()) {
        null
    } else {
        FiledIssueReport(submittedAt = submittedAt, fingerprint = fingerprint)
    }
}
