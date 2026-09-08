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

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.getGroupedShape
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.MarkdownEdit
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.MarkdownFormatting
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.issueReporterActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.rememberMarkdownVisualTransformation

/** Rows the description field grows to before it starts scrolling its own content. */
private const val DESCRIPTION_MIN_LINES: Int = 5
private const val DESCRIPTION_MAX_LINES: Int = 12

/**
 * The report body: a Markdown editor with a formatting bar attached underneath it.
 *
 * The report is filed as a Markdown GitHub issue, so what the author types here is the issue body.
 * Two things follow from that:
 * - The field highlights Markdown syntax as it is typed
 *   ([MarkdownVisualTransformation]), so structure is visible while writing rather than only after
 *   the issue exists.
 * - The formatting bar edits the Markdown source, which is what lets someone fence a stack trace or
 *   list reproduction steps without knowing the syntax.
 *
 * Growth is capped at [DESCRIPTION_MAX_LINES] rows; past that the field scrolls internally instead
 * of pushing the rest of the form off screen while a long log is pasted in.
 *
 * State ownership: the caller owns the text, this composable owns the selection. Formatting actions
 * have to place the caret themselves, and the selection is not part of the screen's UI state.
 */
@Composable
internal fun IssueDescriptionField(
    description: String,
    firebaseController: FirebaseController,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var fieldValue: TextFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = description, selection = TextRange(description.length)))
    }

    LaunchedEffect(description) {
        if (description != fieldValue.text) {
            fieldValue = fieldValue.copy(
                text = description,
                selection = TextRange(description.length),
            )
        }
    }

    val markdownTransformation = rememberMarkdownVisualTransformation()

    val applyEdit: (String, MarkdownEdit) -> Unit = { formatName, edit ->
        firebaseController.logEvent(
            issueReporterActionEvent(
                actionName = IssueReporterActionNames.FORMAT_DESCRIPTION,
                params = mapOf("format" to AnalyticsValue.Str(formatName)),
            )
        )
        fieldValue = TextFieldValue(
            text = edit.text,
            selection = TextRange(edit.selectionStart, edit.selectionEnd),
        )
        onDescriptionChange(edit.text)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
    ) {
        val descriptionLabel = stringResource(id = R.string.issue_description_label)

        IssueFormInput(
            value = fieldValue,
            onValueChange = { updated ->
                val textChanged = updated.text != fieldValue.text
                fieldValue = updated
                if (textChanged) onDescriptionChange(updated.text)
            },
            position = GroupedItemPosition.MIDDLE,
            placeholder = stringResource(id = R.string.issue_description_placeholder),
            leadingIcon = Icons.Outlined.Description,
            leadingIconContentDescription = descriptionLabel,
            minLines = DESCRIPTION_MIN_LINES,
            maxLines = DESCRIPTION_MAX_LINES,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Default,
            ),
            visualTransformation = markdownTransformation,
        )

        MarkdownFormattingBar(
            value = fieldValue,
            onEdit = applyEdit,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Formatting actions for the description field, drawn as the grouped item below it.
 *
 * The row scrolls horizontally rather than wrapping so the bar keeps the height of a single row on
 * narrow screens, and stays visually attached to the field above it.
 */
@Composable
private fun MarkdownFormattingBar(
    value: TextFieldValue,
    onEdit: (String, MarkdownEdit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val start: Int = value.selection.min
    val end: Int = value.selection.max
    val text: String = value.text

    Surface(
        modifier = modifier,
        shape = getGroupedShape(
            position = GroupedItemPosition.MIDDLE,
            outerRadius = ISSUE_GROUP_OUTER_RADIUS,
        ),
        color = groupedFieldContainerColor,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = SizeConstants.SmallSize),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormattingAction(
                icon = Icons.Outlined.FormatBold,
                descriptionResId = R.string.issue_format_bold,
                onClick = {
                    onEdit(
                        FormatNames.BOLD,
                        MarkdownFormatting.toggleWrap(
                            text = text,
                            selectionStart = start,
                            selectionEnd = end,
                            marker = MarkdownFormatting.BOLD_MARKER,
                        ),
                    )
                },
            )

            FormattingAction(
                icon = Icons.Outlined.FormatItalic,
                descriptionResId = R.string.issue_format_italic,
                onClick = {
                    onEdit(
                        FormatNames.ITALIC,
                        MarkdownFormatting.toggleWrap(
                            text = text,
                            selectionStart = start,
                            selectionEnd = end,
                            marker = MarkdownFormatting.ITALIC_MARKER,
                        ),
                    )
                },
            )

            FormattingAction(
                icon = Icons.Outlined.Code,
                descriptionResId = R.string.issue_format_code,
                onClick = {
                    onEdit(
                        FormatNames.CODE,
                        MarkdownFormatting.toggleWrap(
                            text = text,
                            selectionStart = start,
                            selectionEnd = end,
                            marker = MarkdownFormatting.INLINE_CODE_MARKER,
                        ),
                    )
                },
            )

            FormattingAction(
                icon = Icons.Outlined.DataObject,
                descriptionResId = R.string.issue_format_code_block,
                onClick = {
                    onEdit(
                        FormatNames.CODE_BLOCK,
                        MarkdownFormatting.insertCodeBlock(
                            text = text,
                            selectionStart = start,
                            selectionEnd = end,
                        ),
                    )
                },
            )

            FormattingAction(
                icon = Icons.AutoMirrored.Outlined.FormatListBulleted,
                descriptionResId = R.string.issue_format_bullet_list,
                onClick = {
                    onEdit(
                        FormatNames.BULLET_LIST,
                        MarkdownFormatting.toggleLinePrefix(
                            text = text,
                            selectionStart = start,
                            selectionEnd = end,
                            prefix = MarkdownFormatting.BULLET_PREFIX,
                        ),
                    )
                },
            )

            FormattingAction(
                icon = Icons.Outlined.FormatListNumbered,
                descriptionResId = R.string.issue_format_numbered_list,
                onClick = {
                    onEdit(
                        FormatNames.NUMBERED_LIST,
                        MarkdownFormatting.toggleLinePrefix(
                            text = text,
                            selectionStart = start,
                            selectionEnd = end,
                            prefix = "",
                            numbered = true,
                        ),
                    )
                },
            )

            FormattingAction(
                icon = Icons.Outlined.FormatQuote,
                descriptionResId = R.string.issue_format_quote,
                onClick = {
                    onEdit(
                        FormatNames.QUOTE,
                        MarkdownFormatting.toggleLinePrefix(
                            text = text,
                            selectionStart = start,
                            selectionEnd = end,
                            prefix = MarkdownFormatting.QUOTE_PREFIX,
                        ),
                    )
                },
            )

            FormattingAction(
                icon = Icons.Outlined.Link,
                descriptionResId = R.string.issue_format_link,
                onClick = {
                    onEdit(
                        FormatNames.LINK,
                        MarkdownFormatting.insertLink(
                            text = text,
                            selectionStart = start,
                            selectionEnd = end,
                        ),
                    )
                },
            )

            Text(
                text = stringResource(id = R.string.issue_markdown_supported),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = SizeConstants.SmallSize),
            )
        }
    }
}

@Composable
private fun FormattingAction(
    icon: ImageVector,
    @StringRes descriptionResId: Int,
    onClick: () -> Unit,
) {
    val contentDescription: String = stringResource(id = descriptionResId)

    GeneralButton(
        onClick = onClick,
        style = GeneralButtonStyle.Text,
        icon = ToolkitIcon.Vector(imageVector = icon),
        contentDescription = contentDescription,
        iconSize = SizeConstants.ButtonIconSize,
    )
}

/** Analytics values for the formatting bar, kept together so they stay stable across releases. */
private object FormatNames {
    const val BOLD: String = "bold"
    const val ITALIC: String = "italic"
    const val CODE: String = "code"
    const val CODE_BLOCK: String = "code_block"
    const val BULLET_LIST: String = "bullet_list"
    const val NUMBERED_LIST: String = "numbered_list"
    const val QUOTE: String = "quote"
    const val LINK: String = "link"
}
