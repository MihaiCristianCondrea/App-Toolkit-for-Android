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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItems
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.analytics.PRIVACY_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.analytics.privacyPreferenceTapEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts.PrivacyItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.states.PrivacyUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.AdsSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.DiagnosticsSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.keys.PermissionsScreen
import org.koin.compose.viewmodel.koinViewModel

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

    ScreenHost(screen = PRIVACY_SCREEN, status = state.items) {
        PrivacyScreenContent(
            state = state,
            onEvent = viewModel::onEvent,
            onOpenUrl = { url -> context.openUrl(url = url) },
            onOpenPermissions = { navigator.navigate(key = PermissionsScreen) },
            onOpenAds = { navigator.navigate(key = AdsSettingsScreen) },
            onOpenUsageAndDiagnostics = { navigator.navigate(key = DiagnosticsSettingsScreen) },
            contentPadding = contentPadding(),
        )
    }
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
            is PrivacyItemAction.OpenPermissions -> onOpenPermissions()
            is PrivacyItemAction.OpenAds -> onOpenAds()
            is PrivacyItemAction.OpenUsageAndDiagnostics -> onOpenUsageAndDiagnostics()
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
        ) {
            animatedItems(
                items = ready.value,
                key = { it.key },
            ) { item ->
                when (item) {
                    is PrivacyItem.Header -> PreferenceCategoryItem(title = item.title.asString())

                    is PrivacyItem.Preference -> SettingsPreferenceItem(
                        title = item.title.asString(),
                        summary = item.summary.asString(),
                        onClick = { onPreferenceClick(item = item) },
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
