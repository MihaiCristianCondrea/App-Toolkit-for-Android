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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportRefusal

/**
 * What this device has filed recently, kept so that one person cannot flood the issue tracker:
 * a short cooldown after every report, and the same report refused for a day.
 *
 * The history outlives the sheet and the process, so closing and reopening the reporter does not
 * lift either guard. It holds a hash of each report and a timestamp, never the text or the email.
 *
 * Both calls are main-safe and never throw for storage: the guard is a courtesy to maintainers, and
 * a history that cannot be read or written must not stop anyone reporting a bug.
 */
interface IssueReportHistoryRepository {

    /** Why a report with [title] and [description] cannot be sent now, or null when it can. */
    suspend fun refusalFor(title: String, description: String): IssueReportRefusal?

    /** Records a filed report, which starts the cooldown and refuses the same report for a day. */
    suspend fun recordSubmission(title: String, description: String)
}
