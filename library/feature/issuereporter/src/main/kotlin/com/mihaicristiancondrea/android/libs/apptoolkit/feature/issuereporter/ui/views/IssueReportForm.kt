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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.getGroupedShape
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.states.IssueReporterUiState

/** Outward-facing corner radius shared by every grouped block on the report screen. */
internal val ISSUE_GROUP_OUTER_RADIUS = SizeConstants.LargeMediumSize

/**
 * Form inputs for the issue report, styled with grouped corners and tight spacing.
 *
 * The fields carry no floating label. A label animates into space the field has to reserve whether
 * or not it is showing, which is what kept a two-dp gap from reading as a group; each field states
 * itself through a placeholder and a leading icon instead, and keeps a constant height. The
 * indicator line is dropped for the same reason — it would cut the block into strips.
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
        IssueFormInput(
            value = data.title,
            onValueChange = { onEvent(IssueReporterEvent.UpdateTitle(it)) },
            placeholder = stringResource(id = R.string.issue_title_label),
            leadingIcon = Icons.Outlined.Title,
            leadingIconContentDescription = stringResource(id = R.string.issue_title_label),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
            position = GroupedItemPosition.FIRST
        )

        IssueDescriptionField(
            description = data.description,
            firebaseController = firebaseController,
            onDescriptionChange = { onEvent(IssueReporterEvent.UpdateDescription(it)) },
        )

        IssueFormInput(
            value = data.email,
            onValueChange = { onEvent(IssueReporterEvent.UpdateEmail(it)) },
            placeholder = stringResource(id = R.string.issue_email_optional_placeholder),
            leadingIcon = Icons.Outlined.Email,
            leadingIconContentDescription = stringResource(id = R.string.issue_email_label),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
            position = GroupedItemPosition.LAST
        )
    }
}

/**
 * One field of the grouped form.
 *
 * [leadingIconContentDescription] is what names the field for screen readers: the placeholder is
 * gone as soon as there is content, so the icon carries the label.
 */
@Composable
private fun IssueFormInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    leadingIconContentDescription: String,
    position: GroupedItemPosition,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder) },
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = leadingIconContentDescription)
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = keyboardOptions,
        shape = getGroupedShape(position = position, outerRadius = ISSUE_GROUP_OUTER_RADIUS),
        colors = groupedFieldColors(),
    )
}

/** [TextFieldValue] variant, for the description field, whose formatting actions move the caret. */
@Composable
internal fun IssueFormInput(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    leadingIconContentDescription: String,
    position: GroupedItemPosition,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder) },
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = leadingIconContentDescription)
        },
        modifier = modifier.fillMaxWidth(),
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        shape = getGroupedShape(position = position, outerRadius = ISSUE_GROUP_OUTER_RADIUS),
        colors = groupedFieldColors(),
    )
}

/** Container color the grouped fields are drawn on, matched by the formatting bar beside them. */
internal val groupedFieldContainerColor: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceContainerHighest

@Composable
private fun groupedFieldColors(): TextFieldColors = TextFieldDefaults.colors(
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
)
