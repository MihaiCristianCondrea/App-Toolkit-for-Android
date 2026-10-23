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
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.FiledIssueReport
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.IssueReportHistory
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException

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
        .toHistory()

    override suspend fun update(transform: (IssueReportHistory) -> IssueReportHistory) {
        dataStore.edit { preferences ->
            val next: IssueReportHistory = transform(preferences.toHistory())
            val lastSubmittedAt: Long? = next.lastSubmittedAt
            if (lastSubmittedAt == null) {
                preferences.remove(Keys.lastSubmittedAt)
            } else {
                preferences[Keys.lastSubmittedAt] = lastSubmittedAt
            }
            preferences[Keys.recentReports] = next.reports.mapTo(mutableSetOf()) { report -> report.encode() }
        }
    }

    private fun Preferences.toHistory(): IssueReportHistory = IssueReportHistory(
        lastSubmittedAt = this[Keys.lastSubmittedAt],
        reports = this[Keys.recentReports].orEmpty().mapNotNull { value -> value.decodeReport() },
    )

    private fun FiledIssueReport.encode(): String = "$submittedAt$SEPARATOR$fingerprint"

    private fun String.decodeReport(): FiledIssueReport? {
        val submittedAt: Long = substringBefore(SEPARATOR).toLongOrNull() ?: return null
        val fingerprint: String = substringAfter(SEPARATOR, missingDelimiterValue = "")
        return if (fingerprint.isEmpty()) null else FiledIssueReport(submittedAt, fingerprint)
    }

    private object Keys {
        val lastSubmittedAt = longPreferencesKey("issue_reporter_last_submitted_at")
        val recentReports = stringSetPreferencesKey("issue_reporter_recent_reports")
    }

    private companion object {
        const val SEPARATOR: Char = ':'
    }
}
