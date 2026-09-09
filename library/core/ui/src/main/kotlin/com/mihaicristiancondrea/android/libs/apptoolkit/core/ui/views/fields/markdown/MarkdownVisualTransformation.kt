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

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration

/**
 * Syntax highlighting for the Markdown a field is written in.
 *
 * Styling only: every rule keeps the character count of the source text, so [OffsetMapping.Identity]
 * stays correct and the caret, the selection handles and the text the ViewModel receives are the
 * ones the author actually typed. That is also why a Markdown *renderer* cannot do this job — the
 * field is an editor, so the markers have to stay visible, selectable and editable, and only their
 * appearance may change.
 *
 * Rules are applied in a fixed order and code spans win: text inside backticks or a fence is code,
 * not emphasis, so `**` inside a stack trace is not silently restyled as bold.
 *
 * @param boldStyle Applied to the content of `**bold**` and `__bold__`.
 * @param italicStyle Applied to the content of `*italic*` and `_italic_`.
 * @param strikethroughStyle Applied to the content of `~~struck~~`.
 * @param codeStyle Applied to inline code spans and fenced blocks, markers included.
 * @param linkStyle Applied to the label of `[label](url)`.
 * @param headingStyle Applied to a whole `#` heading line.
 * @param listMarkerStyle Applied to the bullet or number that opens a list item.
 * @param quoteStyle Applied to a whole `>` quoted line.
 * @param symbolStyle Applied to the markers themselves, so syntax recedes behind the text.
 */
class MarkdownVisualTransformation(
    private val boldStyle: SpanStyle,
    private val italicStyle: SpanStyle,
    private val strikethroughStyle: SpanStyle,
    private val codeStyle: SpanStyle,
    private val linkStyle: SpanStyle,
    private val headingStyle: SpanStyle,
    private val listMarkerStyle: SpanStyle,
    private val quoteStyle: SpanStyle,
    private val symbolStyle: SpanStyle,
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText = TransformedText(
        text = highlight(source = text.text),
        offsetMapping = OffsetMapping.Identity,
    )

    private fun highlight(source: String): AnnotatedString = buildAnnotatedString {
        append(source)

        val codeRanges: MutableList<IntRange> = mutableListOf()

        FENCED_CODE.findAll(source).forEach { match ->
            codeRanges += match.range
            addStyle(codeStyle, match.range.first, match.range.last + 1)
        }

        INLINE_CODE.findAll(source)
            .filterNot { match -> match.range overlapsAny codeRanges }
            .forEach { match ->
                codeRanges += match.range
                addStyle(codeStyle, match.range.first, match.range.last + 1)
                addSymbolStyle(match.range.first, match.range.first + 1)
                addSymbolStyle(match.range.last, match.range.last + 1)
            }

        HEADING.findAll(source)
            .filterNot { match -> match.range overlapsAny codeRanges }
            .forEach { match ->
                addStyle(headingStyle, match.range.first, match.range.last + 1)
                match.groups[1]?.let { hashes ->
                    addSymbolStyle(hashes.range.first, hashes.range.last + 1)
                }
            }

        QUOTE.findAll(source)
            .filterNot { match -> match.range overlapsAny codeRanges }
            .forEach { match -> addStyle(quoteStyle, match.range.first, match.range.last + 1) }

        LIST_ITEM.findAll(source)
            .filterNot { match -> match.range overlapsAny codeRanges }
            .forEach { match ->
                match.groups[2]?.let { marker ->
                    addStyle(listMarkerStyle, marker.range.first, marker.range.last + 1)
                }
            }

        BOLD.findAll(source)
            .filterNot { match -> match.range overlapsAny codeRanges }
            .forEach { match -> addWrappedStyle(match, boldStyle) }

        ITALIC.findAll(source)
            .filterNot { match -> match.range overlapsAny codeRanges }
            .forEach { match -> addWrappedStyle(match, italicStyle) }

        STRIKETHROUGH.findAll(source)
            .filterNot { match -> match.range overlapsAny codeRanges }
            .forEach { match -> addWrappedStyle(match, strikethroughStyle) }

        LINK.findAll(source)
            .filterNot { match -> match.range overlapsAny codeRanges }
            .forEach { match ->
                addSymbolStyle(match.range.first, match.range.last + 1)
                match.groups[1]?.let { label ->
                    addStyle(linkStyle, label.range.first, label.range.last + 1)
                }
            }
    }

    /** Styles the content a pair of markers wraps, and dims the markers themselves. */
    private fun AnnotatedString.Builder.addWrappedStyle(match: MatchResult, style: SpanStyle) {
        val content: MatchGroup = match.groups[2] ?: return
        addStyle(style, content.range.first, content.range.last + 1)
        addSymbolStyle(match.range.first, content.range.first)
        addSymbolStyle(content.range.last + 1, match.range.last + 1)
    }

    private fun AnnotatedString.Builder.addSymbolStyle(start: Int, end: Int) {
        if (end > start) addStyle(symbolStyle, start, end)
    }

    private infix fun IntRange.overlapsAny(ranges: List<IntRange>): Boolean =
        ranges.any { other -> first <= other.last && other.first <= last }

    private companion object {
        /** Matches an unclosed fence too, so a block highlights while it is still being typed. */
        val FENCED_CODE = Regex("```[\\s\\S]*?(?:```|$)")
        val INLINE_CODE = Regex("`[^`\\n]+`")
        val HEADING = Regex("(?m)^(#{1,6})\\s+.*$")
        val QUOTE = Regex("(?m)^[ \\t]*>.*$")
        val LIST_ITEM = Regex("(?m)^([ \\t]*)([-*+]|\\d+[.)])\\s")

        /** Requires non-blank content, so `**` on its own is not treated as an empty span. */
        val BOLD = Regex("(\\*\\*|__)(?=\\S)(.+?)(?<=\\S)\\1")

        /** Single markers only: the lookarounds keep this off the `**` of a bold span. */
        val ITALIC = Regex("(?<![*_\\w])([*_])(?=\\S)([^*_\\n]+?)(?<=\\S)\\1(?![*_\\w])")
        val STRIKETHROUGH = Regex("(~~)(?=\\S)(.+?)(?<=\\S)~~")
        val LINK = Regex("\\[([^\\[\\]\\n]*)]\\(([^)\\s]*)\\)")
    }
}

/**
 * Remembers a [MarkdownVisualTransformation] styled from the current theme.
 */
@Composable
fun rememberMarkdownVisualTransformation(): MarkdownVisualTransformation {
    val colorScheme = MaterialTheme.colorScheme
    return remember(colorScheme) {
        val symbolColor = colorScheme.onSurfaceVariant

        MarkdownVisualTransformation(
            boldStyle = SpanStyle(fontWeight = FontWeight.Bold),
            italicStyle = SpanStyle(fontStyle = FontStyle.Italic),
            strikethroughStyle = SpanStyle(textDecoration = TextDecoration.LineThrough),
            codeStyle = SpanStyle(
                fontFamily = FontFamily.Monospace,
                color = colorScheme.tertiary,
            ),
            linkStyle = SpanStyle(
                color = colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            ),
            headingStyle = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface,
            ),
            listMarkerStyle = SpanStyle(
                color = colorScheme.primary,
                fontWeight = FontWeight.Bold,
            ),
            quoteStyle = SpanStyle(
                color = symbolColor,
                fontStyle = FontStyle.Italic,
            ),
            symbolStyle = SpanStyle(color = symbolColor),
        )
    }
}
