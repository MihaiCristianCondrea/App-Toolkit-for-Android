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

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextField
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextFieldMarkdown
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextFieldStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.markdown.MarkdownFormatAction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R

/** Rows the description field grows to before it starts scrolling its own content. */
private const val DESCRIPTION_MIN_LINES: Int = 5
private const val DESCRIPTION_MAX_LINES: Int = 12

/**
 * The report body: the toolkit's Markdown editor, cut into the grouped form above and below it.
 *
 * The report is filed as a Markdown GitHub issue, so what the author types here is the issue body,
 * which is why this field is an editor rather than a plain one: the syntax is highlighted as it is
 * typed, and the formatting bar edits the Markdown source, so someone can fence a stack trace or
 * list reproduction steps without knowing the syntax.
 *
 * Growth is capped at [DESCRIPTION_MAX_LINES] rows; past that the field scrolls internally instead
 * of pushing the rest of the form off screen while a long log is pasted in.
 *
 * @param onMarkdownFormat Called with each formatting action used, which the sheet reports.
 */
@Composable
internal fun IssueDescriptionField(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onMarkdownFormat: (MarkdownFormatAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    GeneralTextField(
        value = description,
        onValueChange = onDescriptionChange,
        modifier = modifier,
        style = GeneralTextFieldStyle.Grouped,
        position = GroupedItemPosition.MIDDLE,
        groupedOuterRadius = ISSUE_GROUP_OUTER_RADIUS,
        placeholder = stringResource(id = R.string.issue_description_placeholder),
        leadingIcon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Description),
        leadingIconContentDescription = stringResource(id = R.string.issue_description_label),
        minLines = DESCRIPTION_MIN_LINES,
        maxLines = DESCRIPTION_MAX_LINES,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Default,
        ),
        markdown = GeneralTextFieldMarkdown.Editor,
        onMarkdownFormat = onMarkdownFormat,
    )
}
