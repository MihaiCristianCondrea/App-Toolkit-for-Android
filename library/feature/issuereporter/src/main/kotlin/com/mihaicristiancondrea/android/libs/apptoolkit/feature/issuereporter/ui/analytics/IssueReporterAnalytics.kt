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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.IssueReportRefusal
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportFieldError
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportValidation

/** The screen name the sheet and its ViewModel report under. */
internal const val ISSUE_REPORTER_SCREEN_NAME: String = "IssueReporter"

/** The tracked screen identity reported by the issue reporter sheet. */
internal val ISSUE_REPORTER_SCREEN: TrackedScreen = TrackedScreen(
    name = ISSUE_REPORTER_SCREEN_NAME,
    className = "IssueReporterContent",
)

/** A tap in the sheet, in the shared settings action shape. */
internal fun issueReporterActionEvent(
    actionName: String,
    params: Map<String, AnalyticsValue> = emptyMap(),
): AnalyticsEvent = AnalyticsEvent(
    name = SettingsAnalytics.Events.ACTION,
    params = buildMap {
        put(key = SettingsAnalytics.Params.SCREEN, value = AnalyticsValue.Str(value = ISSUE_REPORTER_SCREEN_NAME))
        put(key = SettingsAnalytics.Params.ACTION_NAME, value = AnalyticsValue.Str(value = actionName))
        putAll(from = params)
    },
)

/** Logs expanding or collapsing the device details panel. */
internal fun issueReporterToggleDeviceInfoEvent(expanded: Boolean): AnalyticsEvent =
    issueReporterActionEvent(
        actionName = IssueReporterActionNames.TOGGLE_DEVICE_INFO,
        params = mapOf(
            IssueReporterParams.EXPANDED to AnalyticsValue.Bool(value = expanded),
        ),
    )

/** Logs using a Markdown formatting control in the description editor. */
internal fun issueReporterFormatDescriptionEvent(format: String): AnalyticsEvent =
    issueReporterActionEvent(
        actionName = IssueReporterActionNames.FORMAT_DESCRIPTION,
        params = mapOf(
            IssueReporterParams.FORMAT to AnalyticsValue.Str(value = format),
        ),
    )

/** Logs a send attempt with field lengths and the gate that stopped it, never the report content. */
internal fun issueReporterSendAttemptEvent(
    titleLength: Int,
    descriptionLength: Int,
    validationFailure: String?,
): AnalyticsEvent = issueReporterActionEvent(
    actionName = IssueReporterActionNames.SEND_ISSUE,
    params = mapOf(
        IssueReporterParams.TITLE_LENGTH to AnalyticsValue.LongVal(value = titleLength.toLong()),
        IssueReporterParams.DESCRIPTION_LENGTH to AnalyticsValue.LongVal(value = descriptionLength.toLong()),
        IssueReporterParams.VALIDATION_FAILURE to
            AnalyticsValue.Str(value = validationFailure ?: IssueReporterParams.VALIDATION_PASSED),
    ),
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
