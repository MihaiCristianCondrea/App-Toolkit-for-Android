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

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.ui.states.ChangelogUiState

@Preview(showBackground = true, heightDp = 480)
@Composable
private fun ChangelogDialogContentPreview() {
    MaterialTheme {
        ChangelogDialogContent(
            state = ChangelogUiState(
                markdown = Loadable.Ready("# 2.0.0\n- A new settings search\n\n---\n\n# 1.9.0\n- Faster start"),
                currentVersion = "2.0.0",
            ),
            onRetry = {},
            onDismiss = {},
        )
    }
}

@Preview(showBackground = true, heightDp = 480)
@Composable
private fun ChangelogDialogContentFailedPreview() {
    MaterialTheme {
        ChangelogDialogContent(
            state = ChangelogUiState(
                markdown = Loadable.Failed(UiText.DynamicString("No internet connection.")),
            ),
            onRetry = {},
            onDismiss = {},
        )
    }
}
