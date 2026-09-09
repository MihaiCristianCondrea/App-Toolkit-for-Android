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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.markdown

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle

/**
 * One action of the Markdown formatting bar.
 *
 * [analyticsName] is the identity a host reports the action under. It is part of the contract, so
 * an event logged today keeps meaning the same thing after the enum is reordered or renamed.
 */
enum class MarkdownFormatAction(val analyticsName: String) {
    Bold(analyticsName = "bold"),
    Italic(analyticsName = "italic"),
    Code(analyticsName = "code"),
    CodeBlock(analyticsName = "code_block"),
    BulletList(analyticsName = "bullet_list"),
    NumberedList(analyticsName = "numbered_list"),
    Quote(analyticsName = "quote"),
    Link(analyticsName = "link"),
}

/**
 * Formatting actions for a Markdown field, drawn as the block below it.
 *
 * The row scrolls horizontally rather than wrapping, so the bar keeps the height of a single row on
 * narrow screens and stays visually attached to the field above it.
 *
 * Every action edits the Markdown source and returns where the caret or the selection has to land,
 * which is what lets someone fence a stack trace or list reproduction steps without knowing the
 * syntax.
 *
 * @param value Current text and selection of the field the bar edits.
 * @param onEdit Receives the action and the edit it produced; the field owner applies it.
 * @param shape Shape of the bar's container, chosen by the field so the two read as one block.
 * @param containerColor Color of that container. [Color.Transparent] leaves the bar unfilled, which
 *   is what an outlined field wants.
 */
@Composable
internal fun MarkdownFormattingBar(
    value: TextFieldValue,
    onEdit: (MarkdownFormatAction, MarkdownEdit) -> Unit,
    shape: Shape,
    containerColor: Color,
    modifier: Modifier = Modifier,
) {
    val start: Int = value.selection.min
    val end: Int = value.selection.max
    val text: String = value.text

    Surface(modifier = modifier, shape = shape, color = containerColor) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(state = rememberScrollState())
                .padding(horizontal = SizeConstants.SmallSize),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormattingAction(
                icon = Icons.Outlined.FormatBold,
                descriptionResId = R.string.markdown_format_bold,
                onClick = {
                    onEdit(
                        MarkdownFormatAction.Bold,
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
                descriptionResId = R.string.markdown_format_italic,
                onClick = {
                    onEdit(
                        MarkdownFormatAction.Italic,
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
                descriptionResId = R.string.markdown_format_code,
                onClick = {
                    onEdit(
                        MarkdownFormatAction.Code,
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
                descriptionResId = R.string.markdown_format_code_block,
                onClick = {
                    onEdit(
                        MarkdownFormatAction.CodeBlock,
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
                descriptionResId = R.string.markdown_format_bullet_list,
                onClick = {
                    onEdit(
                        MarkdownFormatAction.BulletList,
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
                descriptionResId = R.string.markdown_format_numbered_list,
                onClick = {
                    onEdit(
                        MarkdownFormatAction.NumberedList,
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
                descriptionResId = R.string.markdown_format_quote,
                onClick = {
                    onEdit(
                        MarkdownFormatAction.Quote,
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
                descriptionResId = R.string.markdown_format_link,
                onClick = {
                    onEdit(
                        MarkdownFormatAction.Link,
                        MarkdownFormatting.insertLink(
                            text = text,
                            selectionStart = start,
                            selectionEnd = end,
                        ),
                    )
                },
            )

            Text(
                text = stringResource(id = R.string.markdown_supported),
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
    GeneralButton(
        onClick = onClick,
        style = GeneralButtonStyle.Text,
        icon = ToolkitIcon.Vector(imageVector = icon),
        contentDescription = stringResource(id = descriptionResId),
        iconSize = SizeConstants.ButtonIconSize,
    )
}

/** Places the caret and the selection a [MarkdownEdit] asks for on the text it produced. */
internal fun MarkdownEdit.toTextFieldValue(): TextFieldValue = TextFieldValue(
    text = text,
    selection = TextRange(start = selectionStart, end = selectionEnd),
)
