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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.usecases

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportFieldError
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportValidation

/**
 * Decides whether a report says enough to be worth filing, field by field.
 *
 * The rules are about substance, not language. Lengths count Unicode code points and "letters"
 * are letters and digits in any script, so a report in Chinese, Hindi or Arabic passes on the same
 * terms as one in English. Nothing tries to recognise gibberish: a report that is long enough, has
 * enough letters and is not one run of characters repeated is filed, because guessing at meaning
 * refuses real reports.
 *
 * Every field is checked after trimming, which is also how the report is filed.
 */
class ValidateIssueReportUseCase {

    /** Returns an error for each field of the report that cannot be filed as written. */
    operator fun invoke(title: String, description: String, email: String): IssueReportValidation {
        val reportTitle: String = title.trim()
        val reportDescription: String = description.trim()
        return IssueReportValidation(
            titleError = textError(
                text = reportTitle,
                minimumLength = TITLE_MIN_LENGTH,
                maximumLength = TITLE_MAX_LENGTH,
                minimumLetters = TITLE_MIN_LETTERS,
            ),
            descriptionError = descriptionError(description = reportDescription, title = reportTitle),
            emailError = emailError(email = email.trim()),
        )
    }

    private fun descriptionError(description: String, title: String): IssueReportFieldError? =
        textError(
            text = description,
            minimumLength = DESCRIPTION_MIN_LENGTH,
            maximumLength = DESCRIPTION_MAX_LENGTH,
            minimumLetters = DESCRIPTION_MIN_LETTERS,
        ) ?: IssueReportFieldError.SameAsTitle.takeIf {
            title.isNotEmpty() && description.normalized() == title.normalized()
        }

    /**
     * The first rule [text] breaks, in the order a reader would fix them: nothing written, nothing
     * but symbols, too long, only a link, too short, too few letters, then repetition.
     */
    private fun textError(
        text: String,
        minimumLength: Int,
        maximumLength: Int,
        minimumLetters: Int,
    ): IssueReportFieldError? {
        if (text.isEmpty()) return IssueReportFieldError.Missing

        val letters: List<Int> = text.letters()
        val length: Int = text.codePointCount(0, text.length)
        return when {
            letters.isEmpty() -> IssueReportFieldError.TooFewLetters
            length > maximumLength -> IssueReportFieldError.TooLong(maximumLength = maximumLength)
            text.isLinkOnly(minimumLetters = minimumLetters) -> IssueReportFieldError.LinkOnly
            length < minimumLength -> IssueReportFieldError.TooShort(minimumLength = minimumLength)
            letters.size < minimumLetters -> IssueReportFieldError.TooFewLetters
            letters.isRepetitive() -> IssueReportFieldError.Repetitive
            else -> null
        }
    }

    private fun emailError(email: String): IssueReportFieldError? = when {
        email.isEmpty() -> IssueReportFieldError.Missing
        email.length > EMAIL_MAX_LENGTH || !EmailPattern.matches(email) -> IssueReportFieldError.InvalidEmail
        else -> null
    }

    /** Whether this holds a link and, without its links, fewer than [minimumLetters] letters. */
    private fun String.isLinkOnly(minimumLetters: Int): Boolean =
        LinkPattern.containsMatchIn(this) && replace(LinkPattern, " ").letters().size < minimumLetters

    /** The letters and digits of this, in any script, lowercased, one code point each. */
    private fun String.letters(): List<Int> = buildList {
        var index = 0
        while (index < length) {
            val codePoint: Int = codePointAt(index)
            if (Character.isLetterOrDigit(codePoint)) add(Character.toLowerCase(codePoint))
            index += Character.charCount(codePoint)
        }
    }

    /**
     * Whether these letters are a handful of characters, or one short unit repeated at least
     * [MIN_REPEATS] times over the whole text. Only the whole text counts, so a stack trace that
     * repeats its frames under a distinct first line is not repetitive.
     */
    private fun List<Int>.isRepetitive(): Boolean =
        toSet().size < MIN_DISTINCT_LETTERS || shortestPeriod() * MIN_REPEATS <= size

    /** Length of the shortest unit that, repeated, spells these letters, from the KMP border table. */
    private fun List<Int>.shortestPeriod(): Int {
        val border = IntArray(size)
        var matched = 0
        for (index in 1 until size) {
            while (matched > 0 && this[index] != this[matched]) matched = border[matched - 1]
            if (this[index] == this[matched]) matched++
            border[index] = matched
        }
        return size - border[size - 1]
    }

    private fun String.normalized(): String = lowercase().replace(Whitespace, " ")

    companion object {
        const val TITLE_MIN_LENGTH: Int = 10
        const val TITLE_MAX_LENGTH: Int = 120
        const val TITLE_MIN_LETTERS: Int = 6
        const val DESCRIPTION_MIN_LENGTH: Int = 40
        const val DESCRIPTION_MAX_LENGTH: Int = 5_000
        const val DESCRIPTION_MIN_LETTERS: Int = 20

        /** The longest address SMTP delivers to. */
        private const val EMAIL_MAX_LENGTH: Int = 254

        /** Fewer distinct letters than this is a key held down, not a sentence. */
        private const val MIN_DISTINCT_LETTERS: Int = 4

        /** Repeats of one unit that make a text repetitive rather than emphatic. */
        private const val MIN_REPEATS: Int = 3

        private val LinkPattern = Regex("""(?i)(?:https?://|www\.)\S+""")

        private val Whitespace = Regex("""\s+""")

        /**
         * A dotted local part, then a host of dotted labels ending in a top-level domain of two or
         * more letters. Letters and digits may be in any script, for internationalised addresses.
         */
        private val EmailPattern = Regex(
            """[\p{L}\p{N}_%+-]+(?:\.[\p{L}\p{N}_%+-]+)*@(?:[\p{L}\p{N}](?:[\p{L}\p{N}-]*[\p{L}\p{N}])?\.)+\p{L}{2,}""",
        )
    }
}
