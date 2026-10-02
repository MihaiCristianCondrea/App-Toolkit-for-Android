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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.contracts.SettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.search.rememberSettingsSearchIndex
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.states.SettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellBackHandler
import org.koin.compose.viewmodel.koinViewModel

internal const val SETTINGS_SCREEN_NAME = "Settings"
private const val SETTINGS_SCREEN_CLASS = "SettingsScreen"

private object SettingsActionNames {
    const val RETRY_LOAD: String = "retry_load"
}

/**
 * The settings list: the categories the host's `SettingsProvider` supplies.
 *
 * The body of the settings page, a list page: on wide windows the page a row opens sits beside it
 * with a draggable separator, and on phones it opens over it. A row opens its `destination`, after
 * giving its `action` the chance to handle the click outside the app.
 *
 * This is the stateful half. It owns the [SettingsViewModel], the search query and the search
 * index, tracks the screen and navigates, then hands the rendering to [SettingsScreenContent].
 */
@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = koinViewModel()
    val state: SettingsUiState by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalShellNavigator.current
    val telemetryRepository = LocalTelemetry.current

    TrackScreenView(
        screenName = SETTINGS_SCREEN_NAME,
        screenClass = SETTINGS_SCREEN_CLASS,
    )
    TrackScreenState(
        screenName = SETTINGS_SCREEN_NAME,
        state = state.config,
    )

    // Loaded each time the list is shown, so a row's summary follows a change made on its page.
    LaunchedEffect(Unit) {
        viewModel.onEvent(event = SettingsEvent.Load)
    }

    // Searched above the rows, not from the app bar: the query stays with the list while a result
    // opens beside it on a wide window.
    var query by rememberSaveable { mutableStateOf("") }
    val config: SettingsConfig? = (state.config as? Loadable.Ready)?.value
    // Only a loaded list has rows to search.
    val results: List<SettingsPreference>? = config?.let { loaded ->
        val index = rememberSettingsSearchIndex(loaded)
        remember(index, query) { if (query.isBlank()) null else index.matching(query) }
    }
    // Back clears the search before it leaves the page.
    ShellBackHandler(enabled = config != null && query.isNotEmpty()) { query = "" }

    SettingsScreenContent(
        state = state,
        contentPadding = contentPadding(),
        query = query,
        onQueryChange = { query = it },
        results = results,
        onPreferenceClick = { preference ->
            if (preference.action?.invoke() != true) {
                preference.destination?.let(navigator::navigate)
            }
        },
        onRetry = {
            telemetryRepository.logGa4Event(
                ga4Event = settingsActionGa4Event(actionName = SettingsActionNames.RETRY_LOAD),
            )
            viewModel.onEvent(event = SettingsEvent.Load)
        },
    )
}

private fun settingsActionGa4Event(actionName: String): Ga4EventData {
    return Ga4EventData(
        name = SettingsAnalytics.Events.ACTION,
        params = mapOf(
            SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(SETTINGS_SCREEN_NAME),
            SettingsAnalytics.Params.ACTION_NAME to AnalyticsValue.Str(actionName),
        ),
    )
}
