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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.constants.IssueReportHistoryConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.local.converters.encode
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.local.converters.toIssueReportHistory
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.IssueReportHistory
import java.io.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

/**
 * Keeps the history in [dataStore]: the time of the last filed report, and each report as
 * `submittedAt:fingerprint`. Entries that cannot be read are dropped.
 */
class DefaultIssueReportHistoryDataSource(
    private val dataStore: DataStore<Preferences>,
) : IssueReportHistoryDataSource {

    override suspend fun read(): IssueReportHistory = dataStore.data
        .catch { throwable -> if (throwable is IOException) emit(emptyPreferences()) else throw throwable }
        .first()
        .toIssueReportHistory()

    override suspend fun update(transform: (IssueReportHistory) -> IssueReportHistory) {
        dataStore.edit { preferences ->
            val next: IssueReportHistory = transform(preferences.toIssueReportHistory())
            val lastSubmittedAt: Long? = next.lastSubmittedAt
            if (lastSubmittedAt == null) {
                preferences.remove(key = IssueReportHistoryConstants.LAST_SUBMITTED_AT_KEY)
            } else {
                preferences[IssueReportHistoryConstants.LAST_SUBMITTED_AT_KEY] = lastSubmittedAt
            }
            preferences[IssueReportHistoryConstants.RECENT_REPORTS_KEY] =
                next.reports.mapTo(destination = mutableSetOf()) { report -> report.encode() }
        }
    }
}
