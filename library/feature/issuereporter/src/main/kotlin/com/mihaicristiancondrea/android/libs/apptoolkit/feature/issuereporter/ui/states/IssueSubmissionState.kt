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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states

/**
 * Where a report is in its one-way trip from draft to filed.
 *
 * This is stated rather than inferred. The sheet used to read submission off a non-null issue URL,
 * which made "submitted" a side effect of a field the form also displayed, and left the reporter
 * showing an editable form, a live send button and a confirmation of a report already filed at the
 * same time. Naming the three states is what lets the sheet show exactly one of them.
 */
sealed interface IssueSubmissionState {

    /** The author is composing the report. */
    data object Editing : IssueSubmissionState

    /** The report is with GitHub and the answer has not come back. */
    data object Sending : IssueSubmissionState

    /**
     * The report was filed.
     *
     * @property issueUrl The created issue, which the confirmation offers to open.
     */
    data class Submitted(val issueUrl: String) : IssueSubmissionState
}
