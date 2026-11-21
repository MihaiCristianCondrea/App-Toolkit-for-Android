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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.transformers

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChangelogReleasesTransformerTest {
    @Test
    fun `all releases remain visible and grouped versions identify the current release`() {
        val history =
            "# Changelog\n\n---\n\n# Unreleased\n- Future\n\n---\n\n# November 10, 2026\n\n**Version:** `2.0.0`, `2.0.1`\n\n### Fixed\n- Current\n\n---\n\n# October 30, 2026\n\n**Version:** `1.0.0`\n- Old"
        val releases = history.toChangelogReleases("2.0.1")

        assertEquals(
            listOf("Unreleased", "November 10, 2026", "October 30, 2026"),
            releases.map { it.heading })
        assertEquals(listOf(false, true, false), releases.map { it.isCurrent })
        assertTrue(releases[1].markdown.contains("### Fixed"))
        assertTrue(releases.last().markdown.contains("- Old"))
    }

    @Test
    fun `stable and prerelease versions match exactly`() {
        val releases =
            "## [2.0.0-beta] - 2026-08-02\n- Beta\n## Version 2.0.0\n- Stable\n## v1.0.0\n- Old"
                .toChangelogReleases("2.0.0")

        assertEquals(3, releases.size)
        assertEquals(listOf(false, true, false), releases.map { it.isCurrent })
    }

    @Test
    fun `code headings and indented examples remain in their release`() {
        val history = "# 2.0.0\n\n```\n# 1.0.0\n```\n\n    # 0.9.0\n\n# 1.9.0\n- Old"
        val releases = history.toChangelogReleases("2.0.0")

        assertEquals(2, releases.size)
        assertTrue(releases.first().markdown.contains("# 1.0.0"))
        assertTrue(releases.first().markdown.contains("    # 0.9.0"))
    }

    @Test
    fun `only a trailing thematic break is removed`() {
        val history = "# Changelog\n\n---\n\n# 2.0.0\n- Keep the marker---\n\n---\n\n# 1.0.0\nHeading\n---"
        val releases = history.toChangelogReleases("2.0.0")

        assertEquals(listOf("- Keep the marker---", "Heading\n---"), releases.map { it.markdown })
        assertEquals("- Keep the marker---", "# 2.0.0\n- Keep the marker---".toChangelogReleases("").single().markdown)
    }

    @Test
    fun `a break inside an open code fence remains literal`() {
        val body = "~~~text\nExample\n\n---"
        assertEquals(body, "# 2.0.0\n\n$body".toChangelogReleases("2.0.0").single().markdown)
    }

    @Test
    fun `unknown versions and unstructured documents retain all content`() {
        val history = "# 1.0.0\n- Notes"
        assertFalse(history.toChangelogReleases("2.0.0").single().isCurrent)
        assertEquals("Release notes", "Release notes".toChangelogReleases("").single().markdown)
        assertEquals(emptyList(), "  ".toChangelogReleases("1.0.0"))
    }
}
