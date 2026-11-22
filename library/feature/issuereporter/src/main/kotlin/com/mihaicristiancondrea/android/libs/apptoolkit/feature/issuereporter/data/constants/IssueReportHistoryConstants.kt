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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.constants

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

internal object IssueReportHistoryConstants {
    val LAST_SUBMITTED_AT_KEY: Preferences.Key<Long> =
        longPreferencesKey(name = "issue_reporter_last_submitted_at")
    val RECENT_REPORTS_KEY: Preferences.Key<Set<String>> =
        stringSetPreferencesKey(name = "issue_reporter_recent_reports")
    const val ENTRY_SEPARATOR: Char = ':'
}
