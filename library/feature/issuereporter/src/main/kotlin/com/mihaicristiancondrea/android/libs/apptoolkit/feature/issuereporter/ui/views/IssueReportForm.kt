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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.getGroupedShape
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.rememberMarkdownVisualTransformation

/**
 * Form inputs for the issue report, styled with grouped corners and tight spacing.
 */
@Composable
internal fun IssueReportForm(
    data: IssueReporterUiState,
    onEvent: (IssueReporterEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val markdownTransformation = rememberMarkdownVisualTransformation()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)
    ) {
        IssueFormInput(
            value = data.title,
            onValueChange = { onEvent(IssueReporterEvent.UpdateTitle(it)) },
            label = stringResource(id = R.string.issue_title_label),
            leadingIcon = Icons.Outlined.Title,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            position = GroupedItemPosition.FIRST
        )

        IssueFormInput(
            value = data.description,
            onValueChange = { onEvent(IssueReporterEvent.UpdateDescription(it)) },
            label = stringResource(id = R.string.issue_description_label),
            leadingIcon = Icons.Outlined.Info,
            minLines = 4,
            position = GroupedItemPosition.MIDDLE,
            visualTransformation = markdownTransformation
        )

        IssueFormInput(
            value = data.email,
            onValueChange = { onEvent(IssueReporterEvent.UpdateEmail(it)) },
            label = stringResource(id = R.string.issue_email_label),
            placeholder = stringResource(id = R.string.optional_placeholder),
            leadingIcon = Icons.Outlined.Email,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            position = GroupedItemPosition.LAST
        )
    }
}

@Composable
private fun IssueFormInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    position: GroupedItemPosition,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    singleLine: Boolean = false,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        leadingIcon = { Icon(leadingIcon, contentDescription = null) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SizeConstants.LargeSize),
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        shape = getGroupedShape(
            position = position,
            outerRadius = SizeConstants.LargeMediumSize
        )
    )
}
