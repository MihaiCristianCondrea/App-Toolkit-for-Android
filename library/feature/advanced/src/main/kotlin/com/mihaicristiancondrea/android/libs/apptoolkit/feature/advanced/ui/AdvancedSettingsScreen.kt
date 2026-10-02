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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.sheets.IssueReporterSheet
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.contracts.AdvancedSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.AdvancedSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DeveloperOptionsRoute
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel

private const val ADVANCED_SETTINGS_SCREEN_NAME = "AdvancedSettings"
private const val ADVANCED_SETTINGS_SCREEN_CLASS = "AdvancedSettingsScreen"

private object AdvancedPreferenceKeys {
    const val BUG_REPORT: String = "bug_report"
    const val CLEAR_CACHE: String = "clear_cache"
    const val DEVELOPER_OPTIONS: String = "developer_options"
}

/**
 * The advanced settings page: the bug report, clearing the cache, and the developer options once
 * the About screen's easter egg unlocks them.
 *
 * Owns [AdvancedSettingsViewModel], tracking, messages and navigation. The bug report opens the
 * `IssueReporterSheet` bound in Koin over this page, and is left out when nothing binds one. The
 * sheet's visibility is saved, so a rotation does not close it.
 */
@Composable
fun AdvancedSettingsScreen() {
    val viewModel: AdvancedSettingsViewModel = koinViewModel()
    val state: AdvancedSettingsUiState by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalShellNavigator.current
    val issueReporterSheet: IssueReporterSheet? = getKoin().getOrNull()
    var showIssueReporter: Boolean by rememberSaveable { mutableStateOf(value = false) }

    TrackScreenView(
        screenName = ADVANCED_SETTINGS_SCREEN_NAME,
        screenClass = ADVANCED_SETTINGS_SCREEN_CLASS,
    )

    TrackScreenState(
        screenName = ADVANCED_SETTINGS_SCREEN_NAME,
        state = state.cacheClear,
    )

    AdvancedSettingsScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onReportBug = { showIssueReporter = true },
        onOpenDeveloperOptions = { navigator.navigate(DeveloperOptionsRoute) },
        contentPadding = contentPadding(),
        showBugReport = issueReporterSheet != null,
    )

    if (showIssueReporter) {
        issueReporterSheet?.Show(onDismissRequest = { showIssueReporter = false })
    }

    MessageHost(viewModel = viewModel)
}

/**
 * Renders the advanced settings rows for [state]. Each row logs its own tap through its
 * `ga4Event`.
 *
 * @param onEvent Receives the events [AdvancedSettingsViewModel] handles.
 * @param onReportBug Opens the issue reporter. Shown only when [showBugReport] is true.
 * @param onOpenDeveloperOptions Opens the developer options. Navigation belongs to the caller.
 * @param contentPadding Padding from the shell, applied inside the list.
 * @param showBugReport Whether an issue reporter is available, which adds the error reporting
 * category.
 */
@Composable
internal fun AdvancedSettingsScreenContent(
    state: AdvancedSettingsUiState,
    onEvent: (AdvancedSettingsEvent) -> Unit,
    onReportBug: () -> Unit,
    onOpenDeveloperOptions: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    showBugReport: Boolean = false,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        if (showBugReport) {
            item {
                PreferenceCategoryItem(title = stringResource(id = R.string.error_reporting))
            }

            item {
                SettingsPreferenceItem(
                    title = stringResource(id = R.string.bug_report),
                    summary = stringResource(id = R.string.summary_preference_settings_bug_report),
                    onClick = onReportBug,
                    ga4Event = advancedPreferenceTapEvent(preferenceKey = AdvancedPreferenceKeys.BUG_REPORT),
                    modifier = Modifier.groupedPreferenceItem(
                        position = GroupedItemPosition.SINGLE,
                        outerRadius = SizeConstants.LargeMediumSize,
                    ),
                )
            }
        }

        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.cache_management))
        }

        item {
            SettingsPreferenceItem(
                title = stringResource(id = R.string.clear_cache),
                summary = stringResource(id = R.string.summary_preference_settings_clear_cache),
                onClick = { onEvent(AdvancedSettingsEvent.ClearCache) },
                ga4Event = advancedPreferenceTapEvent(preferenceKey = AdvancedPreferenceKeys.CLEAR_CACHE),
                modifier = Modifier.groupedPreferenceItem(
                    position = GroupedItemPosition.SINGLE,
                    outerRadius = SizeConstants.LargeMediumSize,
                ),
            )
        }

        if (state.developerOptionsUnlocked) {
            item {
                PreferenceCategoryItem(title = stringResource(id = R.string.developer))
            }

            item {
                SettingsPreferenceItem(
                    title = stringResource(id = R.string.developer_options),
                    summary = stringResource(id = R.string.summary_preference_settings_developer_options),
                    onClick = onOpenDeveloperOptions,
                    ga4Event = advancedPreferenceTapEvent(preferenceKey = AdvancedPreferenceKeys.DEVELOPER_OPTIONS),
                    modifier = Modifier.groupedPreferenceItem(
                        position = GroupedItemPosition.SINGLE,
                        outerRadius = SizeConstants.LargeMediumSize,
                    ),
                )
            }
        }
    }
}

private fun advancedPreferenceTapEvent(preferenceKey: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.PREFERENCE_VIEW,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(ADVANCED_SETTINGS_SCREEN_NAME),
            SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(preferenceKey),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun AdvancedSettingsScreenContentPreview() {
    MaterialTheme {
        AdvancedSettingsScreenContent(
            state = AdvancedSettingsUiState(developerOptionsUnlocked = true),
            onEvent = {},
            onReportBug = {},
            onOpenDeveloperOptions = {},
            showBugReport = true,
        )
    }
}

/** Without an issue reporter and before the easter egg, only the cache row shows. */
@Preview(showBackground = true)
@Composable
private fun AdvancedSettingsScreenContentMinimalPreview() {
    MaterialTheme {
        AdvancedSettingsScreenContent(
            state = AdvancedSettingsUiState(),
            onEvent = {},
            onReportBug = {},
            onOpenDeveloperOptions = {},
        )
    }
}
