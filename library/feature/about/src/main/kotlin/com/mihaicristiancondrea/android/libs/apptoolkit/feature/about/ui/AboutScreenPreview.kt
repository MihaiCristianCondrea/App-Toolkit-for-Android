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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.states.AboutUiState
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun AboutScreenContentPreview() {
    MaterialTheme {
        AboutScreenContent(
            state = AboutUiState(
                items = Loadable.Ready(
                    value = persistentListOf(
                        AboutItem.Header(
                            key = AboutItemKey.HEADER_APP_INFO,
                            title = UiText.DynamicString(content = "App information"),
                        ),
                        AboutItem.Preference(
                            key = AboutItemKey.APP_NAME,
                            title = UiText.DynamicString(content = "App name"),
                            summary = UiText.DynamicString(content = "App Toolkit Sample"),
                        ),
                        AboutItem.Preference(
                            key = AboutItemKey.APP_BUILD_VERSION,
                            title = UiText.DynamicString(content = "Version"),
                            summary = UiText.DynamicString(content = "1.0.0 (100)"),
                            action = AboutItemAction.VersionTap,
                        ),
                    ),
                ),
            ),
            onRetry = {},
            onAction = {},
        )
    }
}
