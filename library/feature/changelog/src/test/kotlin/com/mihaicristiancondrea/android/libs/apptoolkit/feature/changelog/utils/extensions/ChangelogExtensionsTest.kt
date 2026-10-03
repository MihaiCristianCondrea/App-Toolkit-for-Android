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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.utils.extensions

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChangelogExtensionsTest {

    @Test
    fun `extractChangesForVersion returns matching section`() {
        val markdown = """
            # 2.0.0
            - Added feature
            - Fixed bug
            # 1.9.0
            - Previous changes
        """.trimIndent()

        val result = markdown.extractChangesForVersion("2.0.0")

        assertEquals(
            """
            # 2.0.0
            - Added feature
            - Fixed bug
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `extractChangesForVersion returns empty when version header is missing`() {
        val markdown = """
            # 1.0.0
            - Existing change
        """.trimIndent()

        val result = markdown.extractChangesForVersion("2.0.0")

        assertEquals("", result)
    }

    @Test
    fun `extractChangesForVersion returns empty for blank content`() {
        val markdown = ""

        val result = markdown.extractChangesForVersion("1.0.0")

        assertEquals("", result)
    }

    @Test
    fun `extractChangesForVersion ignores release note mentions and partial versions`() {
        val markdown = """
            # Changelog
            - Migrated from 2.0.0
            # 12.0.00
            - Different release
        """.trimIndent()

        val result = markdown.extractChangesForVersion("2.0.0")

        assertEquals("", result)
    }

    @Test
    fun `extractChangesForVersion supports decorated version heading`() {
        val markdown = """
            ## [2.0.0] - 2026-08-02
            - Current release
            ## [1.0.0] - 2025-01-01
            - Previous release
        """.trimIndent()

        val result = markdown.extractChangesForVersion("2.0.0")

        assertEquals(
            "## [2.0.0] - 2026-08-02\n- Current release",
            result,
        )
    }

    @Test
    fun `extractChangesForVersion keeps sub-headings inside the version section`() {
        val markdown = """
            # Changelog
            ## [2.0.0] - 2026-08-02
            ### Added
            - New screen
            ### Fixed
            - Crash on launch
            ## [1.0.0] - 2025-01-01
            ### Added
            - First release
        """.trimIndent()

        val result = markdown.extractChangesForVersion("2.0.0")

        assertEquals(
            """
            ## [2.0.0] - 2026-08-02
            ### Added
            - New screen
            ### Fixed
            - Crash on launch
            """.trimIndent(),
            result,
        )
    }

    @Test
    fun `extractChangesForVersion ends at a higher-level heading`() {
        val markdown = """
            ## 2.0.0
            - Current release
            # Older releases
            - Archived
        """.trimIndent()

        val result = markdown.extractChangesForVersion("2.0.0")

        assertEquals("## 2.0.0\n- Current release", result)
    }

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
}
