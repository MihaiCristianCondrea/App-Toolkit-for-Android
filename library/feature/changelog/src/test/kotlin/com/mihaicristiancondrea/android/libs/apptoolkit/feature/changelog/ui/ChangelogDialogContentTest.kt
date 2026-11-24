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
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.models.ChangelogRelease
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.constants.ChangelogTestTags
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.states.ChangelogUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/** UI contract tests for [ChangelogDialogContent]. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class ChangelogDialogContentTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val allReleases: String get() = compose.activity.getString(R.string.changelog_all_releases)
    private val doneLabel: String get() = compose.activity.getString(CoreUiR.string.done_button_content_description)
    private val tryAgainLabel: String get() = compose.activity.getString(CoreUiR.string.try_again)

    private fun setContent(
        onOpenReleases: (() -> Unit)? = null,
        releases: Loadable<ImmutableList<ChangelogRelease>> = Loadable.Ready(
            value = persistentListOf(
                ChangelogRelease(
                    heading = "1.0.0",
                    sections = listOf("- First release"),
                    isCurrent = false,
                ),
            ),
        ),
        onRetry: () -> Unit = {},
        onDismiss: () -> Unit = {},
    ) {
        compose.setContent {
            MaterialTheme {
                ChangelogDialogContent(
                    state = ChangelogUiState(releases = releases),
                    modifier = Modifier.height(height = 480.dp),
                    onRetry = onRetry,
                    onDismiss = onDismiss,
                    onOpenReleases = onOpenReleases,
                )
            }
        }
    }

    @Test
    fun `opens at the installed release and keeps older and newer releases reachable`() {
        val notes = (1..30).joinToString(separator = "\n") { "- Change $it" }
        setContent(
            releases = Loadable.Ready(
                value = persistentListOf(
                    ChangelogRelease(heading = "3.0.0", sections = listOf(notes), isCurrent = false),
                    ChangelogRelease(heading = "2.0.0", sections = listOf(notes), isCurrent = true),
                    ChangelogRelease(
                        heading = "1.0.0",
                        sections = listOf("- First release"),
                        isCurrent = false,
                    ),
                ),
            ),
        )
        compose.onNodeWithText(compose.activity.getString(R.string.changelog_current))
            .assertIsDisplayed()
        compose.onNodeWithText("2.0.0").assertIsDisplayed()
        val list = compose.onNodeWithTag(ChangelogTestTags.RELEASES)
        list.performScrollToNode(hasText("1.0.0"))
        compose.onNodeWithText("1.0.0").assertIsDisplayed()
        list.performScrollToNode(hasText("3.0.0"))
        compose.onNodeWithText("3.0.0").assertIsDisplayed()
    }

    @Test
    fun `scrolling content reaches the fixed Done action without an external gap`() {
        setContent()
        val listBottom =
            compose.onNodeWithTag(ChangelogTestTags.RELEASES).fetchSemanticsNode().boundsInRoot.bottom
        val doneTop = compose.onNodeWithText(doneLabel).fetchSemanticsNode().boundsInRoot.top
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

    @Test
    fun `loading state shows the loading message and Done action`() {
        var dismissed = 0
        setContent(
            releases = Loadable.Loading,
            onDismiss = { dismissed += 1 },
        )

        compose.onNodeWithText(compose.activity.getString(R.string.loading_changelog_message))
            .assertIsDisplayed()
        compose.onNodeWithText(doneLabel).performClick()
        assertEquals(1, dismissed)
    }

    @Test
    fun `empty state shows the empty message and Done action`() {
        var dismissed = 0
        setContent(
            releases = Loadable.Empty(),
            onDismiss = { dismissed += 1 },
        )

        compose.onNodeWithText(compose.activity.getString(R.string.no_new_updates_message))
            .assertIsDisplayed()
        compose.onNodeWithText(doneLabel).performClick()
        assertEquals(1, dismissed)
    }

    @Test
    fun `retryable failure shows Try again and triggers retry`() {
        var retries = 0
        setContent(
            releases = Loadable.Failed(
                message = UiText.DynamicString(content = "Offline"),
                retryable = true,
            ),
            onRetry = { retries += 1 },
        )

        compose.onNodeWithText("Offline").assertIsDisplayed()
        compose.onNodeWithText(tryAgainLabel).performClick()
        assertEquals(1, retries)
    }

    @Test
    fun `non-retryable failure shows Done and dismisses`() {
        var dismissed = 0
        setContent(
            releases = Loadable.Failed(
                message = UiText.DynamicString(content = "Unavailable"),
                retryable = false,
            ),
            onDismiss = { dismissed += 1 },
        )

        compose.onNodeWithText("Unavailable").assertIsDisplayed()
        compose.onNodeWithText(doneLabel).performClick()
        assertEquals(1, dismissed)
    }
}
