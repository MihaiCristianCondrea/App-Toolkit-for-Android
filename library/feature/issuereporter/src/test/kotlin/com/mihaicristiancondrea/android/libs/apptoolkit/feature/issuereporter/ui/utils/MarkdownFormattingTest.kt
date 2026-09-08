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

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MarkdownFormattingTest {

    @Test
    fun `wrap selection adds markers and keeps the words selected`() {
        val edit = MarkdownFormatting.toggleWrap(
            text = "a crash happens",
            selectionStart = 2,
            selectionEnd = 7,
            marker = MarkdownFormatting.BOLD_MARKER,
        )

        assertThat(edit.text).isEqualTo("a **crash** happens")
        assertThat(edit.text.substring(edit.selectionStart, edit.selectionEnd)).isEqualTo("crash")
    }

    @Test
    fun `wrap with empty selection leaves the caret between the markers`() {
        val edit = MarkdownFormatting.toggleWrap(
            text = "",
            selectionStart = 0,
            selectionEnd = 0,
            marker = MarkdownFormatting.BOLD_MARKER,
        )

        assertThat(edit.text).isEqualTo("****")
        assertThat(edit.selectionStart).isEqualTo(2)
        assertThat(edit.selectionEnd).isEqualTo(2)
    }

    @Test
    fun `wrapping an already wrapped selection removes the markers`() {
        val edit = MarkdownFormatting.toggleWrap(
            text = "a **crash** happens",
            selectionStart = 4,
            selectionEnd = 9,
            marker = MarkdownFormatting.BOLD_MARKER,
        )

        assertThat(edit.text).isEqualTo("a crash happens")
        assertThat(edit.text.substring(edit.selectionStart, edit.selectionEnd)).isEqualTo("crash")
    }

    @Test
    fun `wrapping a selection that includes the markers removes them`() {
        val edit = MarkdownFormatting.toggleWrap(
            text = "a **crash** happens",
            selectionStart = 2,
            selectionEnd = 11,
            marker = MarkdownFormatting.BOLD_MARKER,
        )

        assertThat(edit.text).isEqualTo("a crash happens")
    }

    @Test
    fun `line prefix applies to every line the selection touches`() {
        val edit = MarkdownFormatting.toggleLinePrefix(
            text = "open app\ntap send\ncrash",
            selectionStart = 2,
            selectionEnd = 12,
            prefix = MarkdownFormatting.BULLET_PREFIX,
        )

        assertThat(edit.text).isEqualTo("- open app\n- tap send\ncrash")
    }

    @Test
    fun `line prefix is removed when every line already has it`() {
        val edit = MarkdownFormatting.toggleLinePrefix(
            text = "- open app\n- tap send",
            selectionStart = 0,
            selectionEnd = 21,
            prefix = MarkdownFormatting.BULLET_PREFIX,
        )

        assertThat(edit.text).isEqualTo("open app\ntap send")
    }

    @Test
    fun `numbered list renumbers the affected lines`() {
        val edit = MarkdownFormatting.toggleLinePrefix(
            text = "open app\ntap send\ncrash",
            selectionStart = 0,
            selectionEnd = 23,
            prefix = "",
            numbered = true,
        )

        assertThat(edit.text).isEqualTo("1. open app\n2. tap send\n3. crash")
    }

    @Test
    fun `link keeps the selection as the label and places the caret in the url`() {
        val edit = MarkdownFormatting.insertLink(
            text = "see the docs",
            selectionStart = 8,
            selectionEnd = 12,
        )

        assertThat(edit.text).isEqualTo("see the [docs]()")
        assertThat(edit.selectionStart).isEqualTo(edit.text.length - 1)
        assertThat(edit.selectionEnd).isEqualTo(edit.text.length - 1)
    }

    @Test
    fun `code block fences the selection on its own lines`() {
        val edit = MarkdownFormatting.insertCodeBlock(
            text = "log: boom",
            selectionStart = 5,
            selectionEnd = 9,
        )

        assertThat(edit.text).isEqualTo("log: \n```\nboom\n```")
        assertThat(edit.text.substring(edit.selectionStart, edit.selectionEnd)).isEqualTo("boom")
    }
}
