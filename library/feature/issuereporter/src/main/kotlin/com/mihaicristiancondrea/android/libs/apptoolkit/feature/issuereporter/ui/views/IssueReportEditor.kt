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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.markdown.MarkdownFormatAction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueSubmissionState

/**
 * Editor with a fixed send action. The form wraps content until the sheet fills the screen,
 * then scrolls beneath the button, which is disabled while a send is in flight.
 */
@Composable
internal fun IssueReportEditor(
    state: IssueReporterUiState,
    onEvent: (IssueReporterEvent) -> Unit,
    onSend: () -> Unit,
    onDeviceInfoExpandedChange: (expanded: Boolean) -> Unit,
    onMarkdownFormat: (MarkdownFormatAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = MaterialTheme.spacing.medium),
    ) {
        Text(
            text = stringResource(id = R.string.bug_report),
            style = MaterialTheme.typography.headlineSmall,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f, fill = false)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(space = MaterialTheme.spacing.extraTiny),
        ) {
            IssueReportForm(
                data = state,
                onEvent = onEvent,
                onMarkdownFormat = onMarkdownFormat,
            )

            LargeVerticalSpacer()

            DeviceInfoSection(
                deviceInfo = state.deviceInfo,
                onExpandedChange = onDeviceInfoExpandedChange,
            )
        }

        GeneralButton(
            onClick = onSend,
            style = GeneralButtonStyle.Filled,
            enabled = state.submissionState !is IssueSubmissionState.Sending,
            label = stringResource(id = R.string.issue_send),
            icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.BugReport),
            measurements = ButtonMeasurements.Medium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
