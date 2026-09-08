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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

/**
 * A [VisualTransformation] that provides syntax highlighting for Markdown.
 *
 * It highlights bold, italic, code, and links in real-time as the user types.
 */
class MarkdownVisualTransformation( // TODO: check if we can use the markdown library (if possible)
    private val boldStyle: SpanStyle,
    private val italicStyle: SpanStyle,
    private val codeStyle: SpanStyle,
    private val linkStyle: SpanStyle,
    private val symbolStyle: SpanStyle,
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            highlight(text.text),
            OffsetMapping.Identity
        )
    }

    private fun highlight(text: String): AnnotatedString {
        return buildAnnotatedString {
            append(text)
            
            // Regex for Bold: **text** or __text__
            BOLD_REGEX.findAll(text).forEach { match ->
                addStyle(boldStyle, match.range.first + 2, match.range.last - 1)
                addStyle(symbolStyle, match.range.first, match.range.first + 2)
                addStyle(symbolStyle, match.range.last - 1, match.range.last + 1)
            }

            // Regex for Italic: *text* or _text_
            ITALIC_REGEX.findAll(text).forEach { match ->
                addStyle(italicStyle, match.range.first + 1, match.range.last)
                addStyle(symbolStyle, match.range.first, match.range.first + 1)
                addStyle(symbolStyle, match.range.last, match.range.last + 1)
            }

            // Regex for Code: `text`
            CODE_REGEX.findAll(text).forEach { match ->
                addStyle(codeStyle, match.range.first + 1, match.range.last)
                addStyle(symbolStyle, match.range.first, match.range.first + 1)
                addStyle(symbolStyle, match.range.last, match.range.last + 1)
            }
            
            // Regex for Link: [text](url)
            LINK_REGEX.findAll(text).forEach { match ->
                val textEnd = match.groups[1]?.range?.last ?: match.range.first
                addStyle(linkStyle, match.range.first + 1, textEnd + 1)
                addStyle(symbolStyle, match.range.first, match.range.first + 1)
                addStyle(symbolStyle, textEnd + 1, match.range.last + 1)
            }
        }
    }

    companion object {
        private val BOLD_REGEX = Regex("""(\*\*|__)(.*?)\1""")
        private val ITALIC_REGEX = Regex("""(\*|_)(.*?)\1""")
        private val CODE_REGEX = Regex("""`(.*?)`""")
        private val LINK_REGEX = Regex("""\[(.*?)\]\((.*?)\)""")
    }
}

/**
 * Remembers a [MarkdownVisualTransformation] with current theme colors.
 */
@Composable
fun rememberMarkdownVisualTransformation(): MarkdownVisualTransformation {
    val colorScheme = MaterialTheme.colorScheme
    return remember(colorScheme) {
        MarkdownVisualTransformation(
            boldStyle = SpanStyle(fontWeight = FontWeight.Bold),
            italicStyle = SpanStyle(fontStyle = FontStyle.Italic),
            codeStyle = SpanStyle(
                fontFamily = FontFamily.Monospace,
                background = colorScheme.surfaceVariant,
                color = colorScheme.onSurfaceVariant
            ),
            linkStyle = SpanStyle(
                color = colorScheme.primary,
                fontWeight = FontWeight.Medium
            ),
            symbolStyle = SpanStyle(
                color = colorScheme.onSurface.copy(alpha = 0.4f)
            )
        )
    }
}
