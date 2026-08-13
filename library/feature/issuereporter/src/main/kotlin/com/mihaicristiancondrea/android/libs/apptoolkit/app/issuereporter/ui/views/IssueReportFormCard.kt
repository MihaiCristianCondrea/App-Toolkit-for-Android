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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R

/** Title, description and optional contact email for the report being written. */
@Composable
internal fun IssueReportFormCard(
    data: IssueReporterUiState,
    onEvent: (IssueReporterEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(shape = MaterialTheme.shapes.medium, modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SizeConstants.LargeSize),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        ) {
            OutlinedTextField(
                value = data.title,
                onValueChange = { onEvent(IssueReporterEvent.UpdateTitle(it)) },
                label = { Text(stringResource(id = R.string.issue_title_label)) },
                leadingIcon = { Icon(Icons.Outlined.Title, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            OutlinedTextField(
                value = data.description,
                onValueChange = { onEvent(IssueReporterEvent.UpdateDescription(it)) },
                label = { Text(stringResource(id = R.string.issue_description_label)) },
                leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
            )

            OutlinedTextField(
                value = data.email,
                onValueChange = { onEvent(IssueReporterEvent.UpdateEmail(it)) },
                label = { Text(stringResource(id = R.string.issue_email_label)) },
                placeholder = { Text(stringResource(id = R.string.optional_placeholder)) },
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )
        }
    }
}
