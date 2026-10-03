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
 * Why one field of a report cannot be filed as written. The limits travel with the error, so the
 * message can quote them without knowing the rules.
 */
sealed interface IssueReportFieldError {

    /** The field is empty or only whitespace. */
    data object Missing : IssueReportFieldError

    /** Fewer characters than [minimumLength], counted after trimming. */
    data class TooShort(val minimumLength: Int) : IssueReportFieldError

    /** More characters than [maximumLength], counted after trimming. */
    data class TooLong(val maximumLength: Int) : IssueReportFieldError

    /** Too few letters or digits, in any script: punctuation, symbols or emoji carry the text. */
    data object TooFewLetters : IssueReportFieldError

    /** One character, or one short run of characters, repeated over the whole text. */
    data object Repetitive : IssueReportFieldError

    /** A link with too little text of its own around it. */
    data object LinkOnly : IssueReportFieldError

    /** The description says nothing the title does not. */
    data object SameAsTitle : IssueReportFieldError

    /** Not an email address. */
    data object InvalidEmail : IssueReportFieldError
}
