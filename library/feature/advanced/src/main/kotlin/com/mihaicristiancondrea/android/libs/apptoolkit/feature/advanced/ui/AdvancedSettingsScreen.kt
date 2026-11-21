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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.analytics.ADVANCED_SETTINGS_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.analytics.AdvancedPreferenceKeys
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.analytics.advancedPreferenceTapEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.sheets.IssueReporterSheet
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.contracts.AdvancedSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.AdvancedSettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states.CacheClearStatus
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DeveloperOptionsRoute
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR


/**
 * Displays cache management and unlocked developer options.
 *
 * A bound [IssueReporterSheet] enables bug reporting. Its visibility survives configuration changes.
 */
@Composable
fun AdvancedSettingsScreen() {
    val viewModel: AdvancedSettingsViewModel = koinViewModel()
    val state: AdvancedSettingsUiState by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalShellNavigator.current
    val issueReporterSheet: IssueReporterSheet? = getKoin().getOrNull()
    var showIssueReporter: Boolean by rememberSaveable { mutableStateOf(value = false) }

    ScreenHost(
        screen = ADVANCED_SETTINGS_SCREEN,
        status = state.cacheClear,
        viewModel = viewModel,
        onMessageAction = { viewModel.observeDeveloperOptions() },
    ) {
        AdvancedSettingsScreenContent(
            state = state,
            onEvent = viewModel::onEvent,
            onReportBug = { showIssueReporter = true },
            onOpenDeveloperOptions = { navigator.navigate(key = DeveloperOptionsRoute) },
            contentPadding = contentPadding(),
            showBugReport = issueReporterSheet != null,
        )
    }

    if (showIssueReporter) {
        issueReporterSheet?.Show(onDismissRequest = { showIssueReporter = false })
    }
}

/**
 * Renders advanced settings rows for [state] and reports row actions through callbacks.
 *
 * @param showBugReport Whether the issue reporter is available.
 * @param contentPadding Shell insets applied inside the list.
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
            animatedItem {
                PreferenceCategoryItem(title = stringResource(id = R.string.error_reporting))
            }

            animatedItem {
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

        animatedItem {
            PreferenceCategoryItem(title = stringResource(id = R.string.cache_management))
        }

        animatedItem {
            val isClearingCache = state.cacheClear == CacheClearStatus.Clearing
            SettingsPreferenceItem(
                enabled = !isClearingCache,
                title = stringResource(id = R.string.clear_cache),
                summary = stringResource(
                    id = if (isClearingCache) CoreUiR.string.loading else R.string.summary_preference_settings_clear_cache,
                ),
                onClick = { onEvent(AdvancedSettingsEvent.ClearCache) },
                ga4Event = advancedPreferenceTapEvent(preferenceKey = AdvancedPreferenceKeys.CLEAR_CACHE),
                modifier = Modifier.groupedPreferenceItem(
                    position = GroupedItemPosition.SINGLE,
                    outerRadius = SizeConstants.LargeMediumSize,
                ),
            )
        }

        if (state.developerOptionsUnlocked) {
            animatedItem {
                PreferenceCategoryItem(title = stringResource(id = R.string.developer))
            }

            animatedItem {
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
