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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.exceptions.IssueReportRejectedException

/** Shown for a failed send with no text of its own; see `toUiText` for the ones that have one. */
private val SendFailedText: UiTextHelper = UiTextHelper.StringResource(R.string.snack_report_failed)

/**
 * A failed send as the error message the sheet shows. GitHub's refusals say what the host has to
 * fix; every other failure gets the shared text or [SendFailedText].
 */
internal fun Throwable.toSendFailedMessage(): UiMessage = when (this) {
    is IssueReportRejectedException -> UiMessage(text = reason.toUiText(), isError = true)
    else -> toErrorMessage(fallback = SendFailedText)
}

private fun IssueReportRejectedException.Reason.toUiText(): UiTextHelper = UiTextHelper.StringResource(
    resourceId = when (this) {
        IssueReportRejectedException.Reason.UNAUTHORIZED -> R.string.error_unauthorized
        IssueReportRejectedException.Reason.FORBIDDEN -> R.string.error_forbidden
        IssueReportRejectedException.Reason.GONE -> R.string.error_gone
        IssueReportRejectedException.Reason.UNPROCESSABLE -> R.string.error_unprocessable
    },
)
