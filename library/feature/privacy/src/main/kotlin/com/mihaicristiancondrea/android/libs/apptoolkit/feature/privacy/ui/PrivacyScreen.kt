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

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.states.PrivacyUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DiagnosticsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel

private const val PRIVACY_SCREEN_NAME = "Privacy"
private const val PRIVACY_SCREEN_CLASS = "PrivacyScreen"

/**
 * The privacy and legal rows the host's `PrivacySettingsProvider` supplies: policy links, and the
 * permissions, ads and usage and diagnostics pages.
 *
 * Owns [PrivacyViewModel], tracking and navigation. Link rows open in the browser; page rows
 * navigate to their Toolkit key, so an app replaces one of those pages by registering the key.
 */
@Composable
fun PrivacyScreen() {
    val viewModel: PrivacyViewModel = koinViewModel()
    val state: PrivacyUiState by viewModel.state.collectAsStateWithLifecycle()
    val context: Context = LocalContext.current
    val navigator = LocalShellNavigator.current

    TrackScreenView(
        screenName = PRIVACY_SCREEN_NAME,
        screenClass = PRIVACY_SCREEN_CLASS,
    )

    TrackScreenState(
        screenName = PRIVACY_SCREEN_NAME,
        state = state.items,
    )

    PrivacyScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onOpenUrl = { url -> context.openUrl(url) },
        onOpenPermissions = { navigator.navigate(PermissionsRoute) },
        onOpenAds = { navigator.navigate(AdsSettingsRoute) },
        onOpenUsageAndDiagnostics = { navigator.navigate(DiagnosticsSettingsRoute) },
        contentPadding = contentPadding(),
    )
}

/**
 * Renders the privacy rows for [state]. A tap reports [PrivacyEvent.ItemClicked], then calls the
 * callback for what the row opens. Each row logs its own tap through its `ga4Event`.
 *
 * @param onEvent Receives the events [PrivacyViewModel] handles.
 * @param onOpenUrl Opens a link row's URL. Needs a `Context`, so it belongs to the caller.
 * @param onOpenPermissions Opens the permissions page. Navigation belongs to the caller.
 * @param onOpenAds Opens the ads settings page.
 * @param onOpenUsageAndDiagnostics Opens the usage and diagnostics page.
 * @param contentPadding Padding from the shell, applied inside the list and the state screens.
 */
@Composable
internal fun PrivacyScreenContent(
    state: PrivacyUiState,
    onEvent: (PrivacyEvent) -> Unit,
    onOpenUrl: (url: String) -> Unit,
    onOpenPermissions: () -> Unit,
    onOpenAds: () -> Unit,
    onOpenUsageAndDiagnostics: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    fun onPreferenceClick(item: PrivacyItem.Preference) {
        onEvent(PrivacyEvent.ItemClicked(action = item.action))
        when (val action = item.action) {
            is PrivacyItemAction.OpenUrl -> onOpenUrl(action.url)
            PrivacyItemAction.OpenPermissions -> onOpenPermissions()
            PrivacyItemAction.OpenAds -> onOpenAds()
            PrivacyItemAction.OpenUsageAndDiagnostics -> onOpenUsageAndDiagnostics()
        }
    }

    ScreenStateHandler(
        state = state.items,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = { onEvent(PrivacyEvent.Load) },
    ) { ready ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
        ) {
            items(
                items = ready.value,
                key = { it.key },
            ) { item ->
                when (item) {
                    is PrivacyItem.Header -> PreferenceCategoryItem(title = item.title.asString())

                    is PrivacyItem.Preference -> SettingsPreferenceItem(
                        title = item.title.asString(),
                        summary = item.summary.asString(),
                        onClick = { onPreferenceClick(item) },
                        ga4Event = privacyPreferenceTapEvent(preferenceKey = item.key),
                        modifier = Modifier.groupedPreferenceItem(
                            position = item.position,
                            outerRadius = SizeConstants.LargeMediumSize,
                        ),
                    )
                }
            }
        }
    }
}

private fun privacyPreferenceTapEvent(preferenceKey: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(PRIVACY_SCREEN_NAME),
            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun PrivacyScreenContentPreview() {
    MaterialTheme {
        PrivacyScreenContent(
            state = PrivacyUiState(
                items = Loadable.Ready(
                    persistentListOf(
                        PrivacyItem.Header(
                            key = PrivacyItemKey.HEADER_PRIVACY,
                            title = UiTextHelper.DynamicString("Privacy"),
                        ),
                        PrivacyItem.Preference(
                            key = PrivacyItemKey.PRIVACY_POLICY,
                            title = UiTextHelper.DynamicString("Privacy policy"),
                            summary = UiTextHelper.DynamicString("How the app handles your data"),
                            position = GroupedItemPosition.FIRST,
                            action = PrivacyItemAction.OpenUrl(url = "https://example.com/privacy"),
                        ),
                        PrivacyItem.Preference(
                            key = PrivacyItemKey.PERMISSIONS,
                            title = UiTextHelper.DynamicString("Permissions"),
                            summary = UiTextHelper.DynamicString("What the app asks for and why"),
                            position = GroupedItemPosition.LAST,
                            action = PrivacyItemAction.OpenPermissions,
                        ),
                    )
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
