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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.analytics

/**
 * Parameters of the send event. They describe the attempt, never its content: no text and no
 * email is ever logged.
 */
internal object IssueReporterParams {
    const val TITLE_LENGTH: String = "title_length"
    const val DESCRIPTION_LENGTH: String = "description_length"

    /**
     * Why the send went no further: `field_reason` for the first invalid field, top first, such
     * as `email_invalid` or `description_too_short`, then `cooldown` or `duplicate`, and
     * [VALIDATION_PASSED] for a report handed to GitHub.
     */
    const val VALIDATION_FAILURE: String = "validation_failure"

    const val VALIDATION_PASSED: String = "none"
}
