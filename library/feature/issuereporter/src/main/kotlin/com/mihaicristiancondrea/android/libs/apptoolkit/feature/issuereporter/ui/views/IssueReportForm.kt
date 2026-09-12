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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Title
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextField
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextFieldStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState

/** Outward-facing corner radius shared by every grouped block on the report screen. */
internal val ISSUE_GROUP_OUTER_RADIUS = SizeConstants.LargeMediumSize

/**
 * Form inputs for the issue report: `GeneralTextField` in its grouped style, with tight spacing.
 *
 * The fields carry no floating label. A label animates into space the field has to reserve whether
 * or not it is showing, which is what kept a two-dp gap from reading as a group; each field states
 * itself through a placeholder and a leading icon instead, and keeps a constant height. The grouped
 * style drops the indicator line for the same reason, because it would cut the block into strips.
 *
 * The leading icon is also what names each field for screen readers: the placeholder is gone as soon
 * as there is content.
 */
@Composable
internal fun IssueReportForm(
    data: IssueReporterUiState,
    firebaseController: FirebaseController,
    onEvent: (IssueReporterEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)
    ) {
        GeneralTextField(
            value = data.title,
            onValueChange = { onEvent(IssueReporterEvent.UpdateTitle(it)) },
            style = GeneralTextFieldStyle.Grouped,
            position = GroupedItemPosition.FIRST,
            groupedOuterRadius = ISSUE_GROUP_OUTER_RADIUS,
            placeholder = stringResource(id = R.string.issue_title_label),
            leadingIcon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Title),
            leadingIconContentDescription = stringResource(id = R.string.issue_title_label),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
        )

        IssueDescriptionField(
            description = data.description,
            firebaseController = firebaseController,
            onDescriptionChange = { onEvent(IssueReporterEvent.UpdateDescription(it)) },
        )

        GeneralTextField(
            value = data.email,
            onValueChange = { onEvent(IssueReporterEvent.UpdateEmail(it)) },
            style = GeneralTextFieldStyle.Grouped,
            position = GroupedItemPosition.LAST,
            groupedOuterRadius = ISSUE_GROUP_OUTER_RADIUS,
            placeholder = stringResource(id = R.string.issue_email_optional_placeholder),
            leadingIcon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Email),
            leadingIconContentDescription = stringResource(id = R.string.issue_email_label),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
        )
    }
}
