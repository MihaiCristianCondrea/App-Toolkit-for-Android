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

    private fun setContent(onOpenReleases: (() -> Unit)?) {
        compose.setContent {
            MaterialTheme {
                ChangelogDialogContent(
                    state = ChangelogUiState(markdown = Loadable.Ready("# 1.0.0\n- First release")),
                    onRetry = {},
                    onDismiss = {},
                    onOpenReleases = onOpenReleases,
                )
            }
        }
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
