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
 * Mutually exclusive editor, pending-submission, and confirmation states. Failed submissions
 * return to editing with the draft intact.
 */
sealed interface IssueSubmissionState {

    data object Editing : IssueSubmissionState

    /**
     * A submission is in flight; a second send must wait for its result.
     */
    data object Sending : IssueSubmissionState

    /**
     * The report was filed.
     *
     * @property issueUrl The created issue, which the confirmation offers to open.
     */
    data class Submitted(val issueUrl: String) : IssueSubmissionState
}
