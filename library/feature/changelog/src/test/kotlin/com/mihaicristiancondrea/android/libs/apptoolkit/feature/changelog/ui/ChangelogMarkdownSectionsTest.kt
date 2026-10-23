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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.extensions.splitAtThematicBreaks
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class ChangelogMarkdownSectionsTest {
    @Test
    fun `splitAtThematicBreaks splits at breaks after a blank line`() {
        val markdown = "# Unreleased\n- One\n\n---\n\n# 1.0.0\n- Two\n\n***\n\n# 0.9.0"

        assertEquals(listOf("# Unreleased\n- One", "# 1.0.0\n- Two", "# 0.9.0"), markdown.splitAtThematicBreaks())
    }

    @Test
    fun `splitAtThematicBreaks keeps a heading underline and code fences whole`() {
        val markdown = "Title\n---\n\n```\n\n---\n```"

        assertEquals(listOf(markdown), markdown.splitAtThematicBreaks())
    }

    @Test
    fun `splitAtThematicBreaks drops blank parts`() {
        assertEquals(listOf("# Only"), "---\n\n# Only\n\n---\n".splitAtThematicBreaks())
    }

    @Test
    fun `a shorter or mismatched fence never exposes rules inside code`() {
        val code = "````markdown\n```\n\n---\n~~~\n````"
        val first = "# Current\n\n$code"
        assertEquals(listOf(first, "# Older"), "$first\n\n---\n\n# Older".splitAtThematicBreaks())
    }

    @Test
    fun `a fence-looking line with trailing text cannot close code`() {
        val markdown = "```markdown\n```not-a-close\n\n---\n```"
        assertEquals(listOf(markdown), markdown.splitAtThematicBreaks())
    }
}
