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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models

/**
 * What is wrong with each field of a report, field by field. A null error is a field that passes,
 * so the default value is a report with nothing to show.
 */
data class IssueReportValidation(
    val titleError: IssueReportFieldError? = null,
    val descriptionError: IssueReportFieldError? = null,
    val emailError: IssueReportFieldError? = null,
) {
    /** Whether every field passes, so the report may be sent. */
    val isValid: Boolean
        get() = titleError == null && descriptionError == null && emailError == null
}
