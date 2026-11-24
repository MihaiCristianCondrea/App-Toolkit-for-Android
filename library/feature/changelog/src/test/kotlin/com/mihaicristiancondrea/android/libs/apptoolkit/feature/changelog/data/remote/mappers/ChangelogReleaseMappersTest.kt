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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.remote.mappers

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChangelogReleaseMappersTest {

    @Test
    fun `all releases remain visible and grouped versions identify the current release`() {
        val history =
            "# Changelog\n\n---\n\n# Unreleased\n- Future\n\n---\n\n# November 10, 2026\n\n**Version:** `2.0.0`, `2.0.1`\n\n### Fixed\n- Current\n\n---\n\n# October 30, 2026\n\n**Version:** `1.0.0`\n- Old"
        val releases = history.toChangelogReleases(currentVersion = "2.0.1")

        assertEquals(
            listOf("Unreleased", "November 10, 2026", "October 30, 2026"),
            releases.map { it.heading },
        )
        assertEquals(listOf(false, true, false), releases.map { it.isCurrent })
        assertEquals("**Version:** `2.0.0`, `2.0.1`", releases[1].versionMarkdown)
        assertTrue(releases[1].sections.single().contains("### Fixed"))
        assertTrue(releases.last().sections.single().contains("- Old"))
    }

    @Test
    fun `stable and prerelease versions match exactly`() {
        val releases =
            "## [2.0.0-beta] - 2026-08-02\n- Beta\n## Version 2.0.0\n- Stable\n## v1.0.0\n- Old"
                .toChangelogReleases(currentVersion = "2.0.0")

        assertEquals(3, releases.size)
        assertEquals(listOf(false, true, false), releases.map { it.isCurrent })
    }

    @Test
    fun `code headings and indented examples remain in their release`() {
        val history = "# 2.0.0\n\n```\n# 1.0.0\n```\n\n    # 0.9.0\n\n# 1.9.0\n- Old"
        val releases = history.toChangelogReleases(currentVersion = "2.0.0")

        assertEquals(2, releases.size)
        assertTrue(releases.first().sections.single().contains("# 1.0.0"))
        assertTrue(releases.first().sections.single().contains("    # 0.9.0"))
    }

    @Test
    fun `only a trailing thematic break is removed and internal breaks split sections`() {
        val history =
            "# Changelog\n\n---\n\n# 2.0.0\n- Keep the marker---\n\n---\n\n- Second section\n\n---\n\n# 1.0.0\nHeading\n---"
        val releases = history.toChangelogReleases(currentVersion = "2.0.0")

        assertEquals(
            listOf(
                listOf("- Keep the marker---", "- Second section"),
                listOf("Heading\n---"),
            ),
            releases.map { it.sections },
        )
        assertEquals(
            listOf("- Keep the marker---"),
            "# 2.0.0\n- Keep the marker---".toChangelogReleases(currentVersion = "").single().sections,
        )
    }

    @Test
    fun `a break inside an open code fence remains literal`() {
        val body = "~~~text\nExample\n\n---"
        assertEquals(
            listOf(body),
            "# 2.0.0\n\n$body".toChangelogReleases(currentVersion = "2.0.0").single().sections,
        )
    }

    @Test
    fun `unknown versions and unstructured documents retain all content`() {
        val history = "# 1.0.0\n- Notes"
        assertFalse(history.toChangelogReleases(currentVersion = "2.0.0").single().isCurrent)
        assertEquals(
            listOf("Release notes"),
            "Release notes".toChangelogReleases(currentVersion = "").single().sections,
        )
        assertEquals(emptyList(), "  ".toChangelogReleases(currentVersion = "1.0.0"))
        assertEquals(emptyList(), "---\n\n---".toChangelogReleases(currentVersion = "1.0.0"))
    }

    @Test
    fun `bracketed unreleased and parenthesized release dates are mapped as releases`() {
        val history = "## [Unreleased]\n- Pending\n\n## [1.0.0] (2026-01-01)\n- Shipped"
        val releases = history.toChangelogReleases(currentVersion = "1.0.0")

        assertEquals(listOf("[Unreleased]", "[1.0.0] (2026-01-01)"), releases.map { it.heading })
        assertEquals(listOf(false, true), releases.map { it.isCurrent })
    }

    @Test
    fun `a shallower release heading does not swallow later sibling releases`() {
        val history = "## Unreleased\n- Pending\n\n# 1.0.0\n- Major\n\n## 0.9.0\n- Earlier"
        val releases = history.toChangelogReleases(currentVersion = "0.9.0")

        assertEquals(listOf("Unreleased", "1.0.0", "0.9.0"), releases.map { it.heading })
        assertEquals(listOf(false, false, true), releases.map { it.isCurrent })
    }

    @Test
    fun `splitAtThematicBreaks splits at breaks after a blank line`() {
        val markdown = "# Unreleased\n- One\n\n---\n\n# 1.0.0\n- Two\n\n***\n\n# 0.9.0"

        assertEquals(
            listOf("# Unreleased\n- One", "# 1.0.0\n- Two", "# 0.9.0"),
            markdown.splitAtThematicBreaks(),
        )
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
