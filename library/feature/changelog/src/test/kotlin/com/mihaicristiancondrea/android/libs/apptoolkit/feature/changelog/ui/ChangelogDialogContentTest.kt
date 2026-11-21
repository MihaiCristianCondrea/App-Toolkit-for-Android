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

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.states.ChangelogUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** The sheet's info button, which an app shows only when it has a page of more release notes. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class ChangelogDialogContentTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val allReleases: String get() = compose.activity.getString(R.string.changelog_all_releases)

    private fun setContent(
        onOpenReleases: (() -> Unit)?,
        history: String = "# 1.0.0\n- First release",
        currentVersion: String = "",
    ) {
        compose.setContent {
            MaterialTheme {
                ChangelogDialogContent(
                    state = ChangelogUiState(markdown = Loadable.Ready(history), currentVersion = currentVersion),
                    modifier = Modifier.height(480.dp),
                    onRetry = {},
                    onDismiss = {},
                    onOpenReleases = onOpenReleases,
                )
            }
        }
    }

    @Test
    fun `opens at the installed release and keeps older and newer releases reachable`() {
        val notes = (1..30).joinToString("\n") { "- Change $it" }
        setContent(
            onOpenReleases = null,
            history = "# 3.0.0\n$notes\n\n# 2.0.0\n$notes\n\n# 1.0.0\n- First release",
            currentVersion = "2.0.0",
        )
        compose.onNodeWithText(compose.activity.getString(R.string.changelog_current)).assertIsDisplayed()
        compose.onNodeWithText("2.0.0").assertIsDisplayed()
        val list = compose.onNodeWithTag("changelog_releases")
        list.performScrollToNode(hasText("1.0.0"))
        compose.onNodeWithText("1.0.0").assertIsDisplayed()
        list.performScrollToNode(hasText("3.0.0"))
        compose.onNodeWithText("3.0.0").assertIsDisplayed()
    }

    @Test
    fun `scrolling content reaches the fixed Done action without an external gap`() {
        setContent(onOpenReleases = null)
        val listBottom = compose.onNodeWithTag("changelog_releases").fetchSemanticsNode().boundsInRoot.bottom
        val doneTop = compose.onNodeWithText(
            compose.activity.getString(com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R.string.done_button_content_description),
        ).fetchSemanticsNode().boundsInRoot.top
        assertEquals(listBottom, doneTop, 1f)
    }

    @Test
    fun `the info button opens the releases`() {
        var opened = 0
        setContent(onOpenReleases = { opened++ })

        compose.onNodeWithContentDescription(allReleases).performClick()

        assertEquals(1, opened)
    }

    @Test
    fun `without a releases page there is no info button`() {
        setContent(onOpenReleases = null)

        compose.onNodeWithContentDescription(allReleases).assertDoesNotExist()
    }
}
