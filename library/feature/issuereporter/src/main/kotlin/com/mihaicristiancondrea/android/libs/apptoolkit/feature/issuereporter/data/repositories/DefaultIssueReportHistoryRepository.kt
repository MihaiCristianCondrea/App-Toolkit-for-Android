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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportRefusal
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.security.MessageDigest

/**
 * Keeps the history in [dataStore] under the reporter's own keys: the time of the last filed report,
 * and up to [MAX_RECENT_REPORTS] entries of a filing time and a SHA-256 fingerprint of the report's
 * normalized title and description.
 *
 * Times are wall-clock, because the guards must hold across reboots. A last report that appears to
 * be in the future, after the clock was set back, does not hold the next one.
 *
 * A failed read refuses nothing and a failed write is dropped, as the contract requires.
 *
 * @param currentTimeMillis Wall clock, replaceable in tests.
 */
class DefaultIssueReportHistoryRepository(
    private val dataStore: DataStore<Preferences>,
    private val currentTimeMillis: () -> Long = System::currentTimeMillis,
) : IssueReportHistoryRepository {

    override suspend fun refusalFor(title: String, description: String): IssueReportRefusal? {
        val preferences: Preferences = dataStore.data
            .catch { throwable -> if (throwable is IOException) emit(emptyPreferences()) else throw throwable }
            .first()
        val now: Long = currentTimeMillis()

        val lastSubmittedAt: Long? = preferences[Keys.lastSubmittedAt]
        if (lastSubmittedAt != null && now - lastSubmittedAt in 0 until COOLDOWN_MILLIS) {
            return IssueReportRefusal.COOLDOWN
        }

        val fingerprint: String = fingerprint(title = title, description = description)
        val duplicate: Boolean = preferences.recentReports(now = now).any { it.fingerprint == fingerprint }
        return if (duplicate) IssueReportRefusal.DUPLICATE else null
    }

    override suspend fun recordSubmission(title: String, description: String) {
        val now: Long = currentTimeMillis()
        val filed = RecentReport(
            submittedAt = now,
            fingerprint = fingerprint(title = title, description = description),
        )
        try {
            dataStore.edit { preferences ->
                val kept: List<RecentReport> = (preferences.recentReports(now = now) + filed)
                    .sortedByDescending { it.submittedAt }
                    .take(MAX_RECENT_REPORTS)
                preferences[Keys.lastSubmittedAt] = now
                preferences[Keys.recentReports] = kept.mapTo(mutableSetOf()) { it.encode() }
            }
        } catch (exception: IOException) {
            Unit
        }
    }

    /** The stored reports filed within the duplicate window before [now]; unreadable entries are dropped. */
    private fun Preferences.recentReports(now: Long): List<RecentReport> =
        this[Keys.recentReports].orEmpty()
            .mapNotNull(RecentReport::decode)
            .filter { now - it.submittedAt in 0 until DUPLICATE_WINDOW_MILLIS }

    /**
     * The report reduced to what makes it the same report: trimmed, lowercased, with runs of
     * whitespace collapsed, then hashed so the text itself is never stored.
     */
    private fun fingerprint(title: String, description: String): String {
        val normalized = "${title.normalized()}\n${description.normalized()}"
        return MessageDigest.getInstance(HASH_ALGORITHM)
            .digest(normalized.toByteArray(Charsets.UTF_8))
            .joinToString(separator = "") { byte -> "%02x".format(byte) }
    }

    private fun String.normalized(): String = trim().lowercase().replace(Whitespace, " ")

    /** One filed report, stored as `submittedAt:fingerprint`. */
    private data class RecentReport(val submittedAt: Long, val fingerprint: String) {
        fun encode(): String = "$submittedAt$SEPARATOR$fingerprint"

        companion object {
            fun decode(value: String): RecentReport? {
                val submittedAt: Long = value.substringBefore(SEPARATOR).toLongOrNull() ?: return null
                val fingerprint: String = value.substringAfter(SEPARATOR, missingDelimiterValue = "")
                return if (fingerprint.isEmpty()) null else RecentReport(submittedAt, fingerprint)
            }
        }
    }

    private object Keys {
        val lastSubmittedAt = longPreferencesKey("issue_reporter_last_submitted_at")
        val recentReports = stringSetPreferencesKey("issue_reporter_recent_reports")
    }

    companion object {
        /** How long after a filed report the next one is held back. */
        const val COOLDOWN_MILLIS: Long = 5 * 60 * 1_000L

        /** How long the same report is refused after it was filed. */
        const val DUPLICATE_WINDOW_MILLIS: Long = 24 * 60 * 60 * 1_000L

        /** Fingerprints kept; older ones fall out even inside the duplicate window. */
        const val MAX_RECENT_REPORTS: Int = 10

        private const val HASH_ALGORITHM: String = "SHA-256"
        private const val SEPARATOR: Char = ':'
        private val Whitespace = Regex("""\s+""")
    }
}
