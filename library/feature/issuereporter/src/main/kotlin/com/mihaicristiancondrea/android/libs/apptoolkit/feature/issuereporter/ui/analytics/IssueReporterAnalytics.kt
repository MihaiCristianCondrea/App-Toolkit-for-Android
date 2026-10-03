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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportFieldError
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportRefusal
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportValidation

/** The screen name the sheet and its ViewModel report under. */
internal const val ISSUE_REPORTER_SCREEN_NAME: String = "IssueReporter"

internal object IssueReporterActionNames {
    const val SEND_ISSUE: String = "send_issue"
    const val TOGGLE_DEVICE_INFO: String = "toggle_device_info"
    const val FORMAT_DESCRIPTION: String = "format_description"
}

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

/** A tap in the sheet, in the shared settings action shape. */
internal fun issueReporterActionEvent(
    actionName: String,
    params: Map<String, AnalyticsValue> = emptyMap(),
): AnalyticsEvent = AnalyticsEvent(
    name = SettingsAnalytics.Events.ACTION,
    params = buildMap {
        put(SettingsAnalytics.Params.SCREEN, AnalyticsValue.Str(ISSUE_REPORTER_SCREEN_NAME))
        put(SettingsAnalytics.Params.ACTION_NAME, AnalyticsValue.Str(actionName))
        putAll(params)
    },
)

/** The first invalid field, top first, as `field_reason`, or null when the report is valid. */
internal fun IssueReportValidation.failureName(): String? =
    titleError?.let { "title_${it.analyticsName}" }
        ?: descriptionError?.let { "description_${it.analyticsName}" }
        ?: emailError?.let { "email_${it.analyticsName}" }

internal val IssueReportRefusal.analyticsName: String
    get() = when (this) {
        IssueReportRefusal.COOLDOWN -> "cooldown"
        IssueReportRefusal.DUPLICATE -> "duplicate"
    }

private val IssueReportFieldError.analyticsName: String
    get() = when (this) {
        IssueReportFieldError.Missing -> "missing"
        is IssueReportFieldError.TooShort -> "too_short"
        is IssueReportFieldError.TooLong -> "too_long"
        IssueReportFieldError.TooFewLetters -> "too_few_letters"
        IssueReportFieldError.Repetitive -> "repetitive"
        IssueReportFieldError.LinkOnly -> "link_only"
        IssueReportFieldError.SameAsTitle -> "same_as_title"
        IssueReportFieldError.InvalidEmail -> "invalid"
    }
