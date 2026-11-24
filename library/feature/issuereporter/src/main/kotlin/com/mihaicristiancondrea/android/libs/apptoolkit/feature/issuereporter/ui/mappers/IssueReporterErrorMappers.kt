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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.mappers

import androidx.annotation.StringRes
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.exceptions.IssueReportRejectedException
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.IssueReportRefusal
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportFieldError

/**
 * A failed send as the error message the sheet shows. GitHub's refusals say what the host has to
 * fix; every other failure gets the shared text or `R.string.snack_report_failed`.
 */
internal fun Throwable.toSendFailedMessage(): UiMessage = when (this) {
    is IssueReportRejectedException -> UiMessage(text = reason.toUiText(), isError = true)
    else -> toErrorMessage(
        fallback = UiText.StringResource(resourceId = R.string.snack_report_failed),
    )
}

/** A report held back by the device's history, as the error message the sheet shows. */
internal fun IssueReportRefusal.toMessage(): UiMessage = UiMessage(
    text = UiText.StringResource(
        resourceId = when (this) {
            IssueReportRefusal.COOLDOWN -> R.string.error_report_cooldown
            IssueReportRefusal.DUPLICATE -> R.string.error_report_duplicate
        },
    ),
    isError = true,
)

/**
 * The text shown under a field with this error. Only an empty field is named, through
 * [missingText]; every other error reads the same on any field.
 */
internal fun IssueReportFieldError.toUiText(@StringRes missingText: Int): UiText = when (this) {
    IssueReportFieldError.Missing -> UiText.StringResource(resourceId = missingText)
    is IssueReportFieldError.TooShort -> UiText.StringResource(
        resourceId = R.string.error_too_short,
        arguments = listOf(minimumLength),
    )
    is IssueReportFieldError.TooLong -> UiText.StringResource(
        resourceId = R.string.error_too_long,
        arguments = listOf(maximumLength),
    )
    IssueReportFieldError.TooFewLetters ->
        UiText.StringResource(resourceId = R.string.error_too_few_letters)
    IssueReportFieldError.Repetitive ->
        UiText.StringResource(resourceId = R.string.error_repetitive)
    IssueReportFieldError.LinkOnly ->
        UiText.StringResource(resourceId = R.string.error_link_only)
    IssueReportFieldError.SameAsTitle ->
        UiText.StringResource(resourceId = R.string.error_same_as_title)
    IssueReportFieldError.InvalidEmail ->
        UiText.StringResource(resourceId = R.string.error_email_invalid)
}

private fun IssueReportRejectedException.Reason.toUiText(): UiText = UiText.StringResource(
    resourceId = when (this) {
        IssueReportRejectedException.Reason.UNAUTHORIZED -> R.string.error_unauthorized
        IssueReportRejectedException.Reason.FORBIDDEN -> R.string.error_forbidden
        IssueReportRejectedException.Reason.GONE -> R.string.error_gone
        IssueReportRejectedException.Reason.UNPROCESSABLE -> R.string.error_unprocessable
    },
)
