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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.states.PrivacyUiState
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun PrivacyScreenContentPreview() {
    MaterialTheme {
        PrivacyScreenContent(
            state = PrivacyUiState(
                items = Loadable.Ready(
                    value = persistentListOf(
                        PrivacyItem.Header(
                            key = PrivacyItemKey.HEADER_PRIVACY,
                            title = UiText.DynamicString(content = "Privacy"),
                        ),
                        PrivacyItem.Preference(
                            key = PrivacyItemKey.PRIVACY_POLICY,
                            title = UiText.DynamicString(content = "Privacy policy"),
                            summary = UiText.DynamicString(content = "How the app handles your data"),
                            position = GroupedItemPosition.FIRST,
                            action = PrivacyItemAction.OpenUrl(url = "https://example.com/privacy"),
                        ),
                        PrivacyItem.Preference(
                            key = PrivacyItemKey.PERMISSIONS,
                            title = UiText.DynamicString(content = "Permissions"),
                            summary = UiText.DynamicString(content = "What the app asks for and why"),
                            position = GroupedItemPosition.LAST,
                            action = PrivacyItemAction.OpenPermissions,
                        ),
                    ),
                ),
            ),
            onEvent = {},
            onOpenUrl = {},
            onOpenPermissions = {},
            onOpenAds = {},
            onOpenUsageAndDiagnostics = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrivacyScreenContentLoadingPreview() {
    MaterialTheme {
        PrivacyScreenContent(
            state = PrivacyUiState(items = Loadable.Loading),
            onEvent = {},
            onOpenUrl = {},
            onOpenPermissions = {},
            onOpenAds = {},
            onOpenUsageAndDiagnostics = {},
        )
    }
}
