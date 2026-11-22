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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportFieldError
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportValidation
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueSubmissionState

@Preview(showBackground = true)
@Composable
private fun IssueReporterBottomSheetContentPreview() {
    MaterialTheme {
        IssueReporterBottomSheetContent(
            state = IssueReporterUiState(
                title = "Sample Bug Title",
                description = "This is a detailed description of the bug in the sample application.",
                email = "user@example.com",
                deviceInfo = Loadable.Ready(
                    value = "Device: Pixel 7\nOS: Android 14\nApp Version: 1.0.0",
                ),
            ),
            onEvent = {},
            onSend = {},
            onDeviceInfoExpandedChange = {},
            onMarkdownFormat = {},
            onDone = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IssueReporterBottomSheetContentInvalidPreview() {
    MaterialTheme {
        IssueReporterBottomSheetContent(
            state = IssueReporterUiState(
                title = "Crash",
                description = "",
                email = "user at example",
                fieldErrors = IssueReportValidation(
                    titleError = IssueReportFieldError.TooShort(minimumLength = 10),
                    descriptionError = IssueReportFieldError.Missing,
                    emailError = IssueReportFieldError.InvalidEmail,
                ),
            ),
            onEvent = {},
            onSend = {},
            onDeviceInfoExpandedChange = {},
            onMarkdownFormat = {},
            onDone = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IssueReporterBottomSheetContentSubmittedPreview() {
    MaterialTheme {
        IssueReporterBottomSheetContent(
            state = IssueReporterUiState(
                submissionState = IssueSubmissionState.Submitted(
                    issueUrl = "https://github.com/example/1",
                ),
            ),
            onEvent = {},
            onSend = {},
            onDeviceInfoExpandedChange = {},
            onMarkdownFormat = {},
            onDone = {},
        )
    }
}
