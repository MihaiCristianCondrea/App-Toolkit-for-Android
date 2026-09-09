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

/**
 * Result of a formatting action: the new text and where the caret or the selection ends up.
 *
 * Selection is part of the contract because every action has to keep typing flowing: wrapping an
 * empty selection has to leave the caret between the markers, and wrapping a selection has to keep
 * the same words selected so the action can be toggled off again.
 */
internal data class MarkdownEdit(
    val text: String,
    val selectionStart: Int,
    val selectionEnd: Int,
)

/**
 * Markdown source edits behind a field's formatting bar.
 *
 * These are plain string transformations rather than composable helpers so the behaviour that is
 * easy to get subtly wrong (toggling, multi-line prefixes, caret placement) is unit-testable
 * without a Compose runtime.
 */
internal object MarkdownFormatting {

    /**
     * Wraps the selection in [marker], or unwraps it when it is already wrapped.
     *
     * An empty selection inserts the marker pair and places the caret between them, which is what
     * makes tapping "bold" before typing behave like a text editor.
     */
    fun toggleWrap(text: String, selectionStart: Int, selectionEnd: Int, marker: String): MarkdownEdit {
        val start = selectionStart.coerceIn(0, text.length)
        val end = selectionEnd.coerceIn(start, text.length)
        val markerLength = marker.length

        val wrapsSelection = text.regionMatchesOrNull(start - markerLength, marker) &&
                text.regionMatchesOrNull(end, marker)
        if (wrapsSelection) {
            val unwrapped = text.removeRange(end, end + markerLength)
                .removeRange(start - markerLength, start)
            return MarkdownEdit(
                text = unwrapped,
                selectionStart = start - markerLength,
                selectionEnd = end - markerLength,
            )
        }

        val selected = text.substring(start, end)
        if (selected.startsWith(marker) && selected.endsWith(marker) &&
            selected.length >= markerLength * 2
        ) {
            val unwrapped = selected.substring(markerLength, selected.length - markerLength)
            return MarkdownEdit(
                text = text.replaceRange(start, end, unwrapped),
                selectionStart = start,
                selectionEnd = start + unwrapped.length,
            )
        }

        val wrapped = marker + selected + marker
        return MarkdownEdit(
            text = text.replaceRange(start, end, wrapped),
            selectionStart = start + markerLength,
            selectionEnd = start + markerLength + selected.length,
        )
    }

    /**
     * Adds [prefix] to every line the selection touches, or removes it when all of them already
     * have it.
     *
     * [numbered] renumbers the affected lines instead of repeating the same marker, so a numbered
     * list stays valid Markdown when several lines are selected at once.
     */
    fun toggleLinePrefix(
        text: String,
        selectionStart: Int,
        selectionEnd: Int,
        prefix: String,
        numbered: Boolean = false,
    ): MarkdownEdit {
        val start = selectionStart.coerceIn(0, text.length)
        val end = selectionEnd.coerceIn(start, text.length)

        val blockStart = text.lastIndexOf(char = '\n', startIndex = (start - 1).coerceAtLeast(0))
            .let { if (it == -1 || start == 0) 0 else it + 1 }
        val blockEndExclusive = text.indexOf(char = '\n', startIndex = end)
            .let { if (it == -1) text.length else it }

        val lines = text.substring(blockStart, blockEndExclusive).split("\n")
        val matcher = if (numbered) NUMBERED_PREFIX else Regex("^${Regex.escape(prefix)}")
        val allPrefixed = lines.all { matcher.containsMatchIn(it) }

        val updated = lines.mapIndexed { index, line ->
            when {
                allPrefixed -> line.replaceFirst(matcher, "")
                numbered -> "${index + 1}. $line"
                else -> prefix + line
            }
        }.joinToString(separator = "\n")

        val replaced = text.replaceRange(blockStart, blockEndExclusive, updated)
        val delta = updated.length - (blockEndExclusive - blockStart)
        return MarkdownEdit(
            text = replaced,
            selectionStart = blockStart,
            selectionEnd = (blockEndExclusive + delta).coerceAtLeast(blockStart),
        )
    }

    /** Turns the selection into a Markdown link, leaving the caret inside the empty URL. */
    fun insertLink(text: String, selectionStart: Int, selectionEnd: Int): MarkdownEdit {
        val start = selectionStart.coerceIn(0, text.length)
        val end = selectionEnd.coerceIn(start, text.length)
        val label = text.substring(start, end)
        val inserted = "[$label]()"
        val caret = start + inserted.length - 1
        return MarkdownEdit(
            text = text.replaceRange(start, end, inserted),
            selectionStart = caret,
            selectionEnd = caret,
        )
    }

    /**
     * Fences the selection as a code block on its own lines.
     *
     * Logs and stack traces are the reason a Markdown field usually exists at all, and they are
     * unreadable unless they are fenced.
     */
    fun insertCodeBlock(text: String, selectionStart: Int, selectionEnd: Int): MarkdownEdit {
        val start = selectionStart.coerceIn(0, text.length)
        val end = selectionEnd.coerceIn(start, text.length)
        val selected = text.substring(start, end)

        val leading = if (start > 0 && text[start - 1] != '\n') "\n" else ""
        val trailing = if (end < text.length && text[end] != '\n') "\n" else ""
        val inserted = "$leading$CODE_FENCE\n$selected\n$CODE_FENCE$trailing"
        val contentStart = start + leading.length + CODE_FENCE.length + 1

        return MarkdownEdit(
            text = text.replaceRange(start, end, inserted),
            selectionStart = contentStart,
            selectionEnd = contentStart + selected.length,
        )
    }

    private fun String.regionMatchesOrNull(index: Int, other: String): Boolean =
        index >= 0 && index + other.length <= length &&
                regionMatches(thisOffset = index, other = other, otherOffset = 0, length = other.length)

    const val BOLD_MARKER: String = "**"
    const val ITALIC_MARKER: String = "_"
    const val INLINE_CODE_MARKER: String = "`"
    const val BULLET_PREFIX: String = "- "
    const val QUOTE_PREFIX: String = "> "
    const val CODE_FENCE: String = "```"

    private val NUMBERED_PREFIX = Regex("^\\d+[.)] ")
}
