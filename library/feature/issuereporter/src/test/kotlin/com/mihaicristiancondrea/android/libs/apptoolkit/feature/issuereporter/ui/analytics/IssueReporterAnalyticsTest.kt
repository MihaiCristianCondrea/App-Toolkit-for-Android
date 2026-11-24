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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.IssueReportRefusal
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportFieldError
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportValidation
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.junit.jupiter.api.Test

class IssueReporterAnalyticsTest {

    @Test
    fun `screen identity uses IssueReporter and IssueReporterContent`() {
        assertEquals(expected = "IssueReporter", actual = ISSUE_REPORTER_SCREEN.name)
        assertEquals(expected = "IssueReporterContent", actual = ISSUE_REPORTER_SCREEN.className)
    }

    @Test
    fun `issueReporterToggleDeviceInfoEvent encodes action and expanded state`() {
        val event = issueReporterToggleDeviceInfoEvent(expanded = true)

        assertEquals(expected = SettingsAnalytics.Events.ACTION, actual = event.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = ISSUE_REPORTER_SCREEN_NAME),
            actual = event.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = IssueReporterActionNames.TOGGLE_DEVICE_INFO),
            actual = event.params[SettingsAnalytics.Params.ACTION_NAME],
        )
        assertEquals(
            expected = AnalyticsValue.Bool(value = true),
            actual = event.params[IssueReporterParams.EXPANDED],
        )
    }

    @Test
    fun `issueReporterFormatDescriptionEvent encodes action and format name`() {
        val event = issueReporterFormatDescriptionEvent(format = "bold")

        assertEquals(expected = SettingsAnalytics.Events.ACTION, actual = event.name)
        assertEquals(
            expected = AnalyticsValue.Str(value = ISSUE_REPORTER_SCREEN_NAME),
            actual = event.params[SettingsAnalytics.Params.SCREEN],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = IssueReporterActionNames.FORMAT_DESCRIPTION),
            actual = event.params[SettingsAnalytics.Params.ACTION_NAME],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = "bold"),
            actual = event.params[IssueReporterParams.FORMAT],
        )
    }

    @Test
    fun `issueReporterSendAttemptEvent defaults null validation failure to none`() {
        val passed = issueReporterSendAttemptEvent(
            titleLength = 24,
            descriptionLength = 120,
            validationFailure = null,
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = IssueReporterActionNames.SEND_ISSUE),
            actual = passed.params[SettingsAnalytics.Params.ACTION_NAME],
        )
        assertEquals(
            expected = AnalyticsValue.LongVal(value = 24L),
            actual = passed.params[IssueReporterParams.TITLE_LENGTH],
        )
        assertEquals(
            expected = AnalyticsValue.LongVal(value = 120L),
            actual = passed.params[IssueReporterParams.DESCRIPTION_LENGTH],
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = IssueReporterParams.VALIDATION_PASSED),
            actual = passed.params[IssueReporterParams.VALIDATION_FAILURE],
        )

        val failed = issueReporterSendAttemptEvent(
            titleLength = 3,
            descriptionLength = 0,
            validationFailure = "title_too_short",
        )
        assertEquals(
            expected = AnalyticsValue.Str(value = "title_too_short"),
            actual = failed.params[IssueReporterParams.VALIDATION_FAILURE],
        )
    }

    @Test
    fun `failureName picks first invalid field top to bottom and returns null when valid`() {
        assertNull(actual = IssueReportValidation().failureName())

        assertEquals(
            expected = "title_missing",
            actual = IssueReportValidation(
                titleError = IssueReportFieldError.Missing,
                descriptionError = IssueReportFieldError.TooShort(minimumLength = 40),
                emailError = IssueReportFieldError.InvalidEmail,
            ).failureName(),
        )
        assertEquals(
            expected = "description_same_as_title",
            actual = IssueReportValidation(
                descriptionError = IssueReportFieldError.SameAsTitle,
                emailError = IssueReportFieldError.InvalidEmail,
            ).failureName(),
        )
        assertEquals(
            expected = "email_invalid",
            actual = IssueReportValidation(
                emailError = IssueReportFieldError.InvalidEmail,
            ).failureName(),
        )
    }

    @Test
    fun `refusal analytics names match cooldown and duplicate`() {
        assertEquals(expected = "cooldown", actual = IssueReportRefusal.COOLDOWN.analyticsName)
        assertEquals(expected = "duplicate", actual = IssueReportRefusal.DUPLICATE.analyticsName)
    }
}
