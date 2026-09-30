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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextField
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextFieldStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.search.rememberSettingsSearchIndex
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellBackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.contracts.SettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val SETTINGS_SCREEN_NAME = "Settings"
private const val SETTINGS_SCREEN_CLASS = "SettingsScreen"
private const val UNKNOWN_PREFERENCE_KEY = "unknown"

private object SettingsActionNames {
    const val RETRY_LOAD: String = "retry_load"
}

/**
 * The settings list: the categories the host's `SettingsProvider` supplies.
 *
 * The body of the settings page, a list page: on wide windows the page a row opens sits beside it
 * with a draggable separator, and on phones it opens over it. A row opens its `destination`, after
 * giving its `action` the chance to handle the click outside the app.
 */
@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = koinViewModel()
    val screenState: UiStateScreen<SettingsConfig> by viewModel.uiState.collectAsStateWithLifecycle()
    val navigator = LocalShellNavigator.current
    val paddingValues = contentPadding()

    val firebaseController: FirebaseController = koinInject()
    TrackScreenView(
        firebaseController = firebaseController,
        screenName = SETTINGS_SCREEN_NAME,
        screenClass = SETTINGS_SCREEN_CLASS,
    )
    TrackScreenState(
        firebaseController = firebaseController,
        screenName = SETTINGS_SCREEN_NAME,
        screenState = screenState.screenState,
    )

    LaunchedEffect(Unit) {
        viewModel.onEvent(event = SettingsEvent.Load)
    }

    ScreenStateHandler(
        screenState = screenState,
        onLoading = { LoadingScreen() },
        onEmpty = {
            NoDataScreen(
                icon = Icons.Outlined.Settings,
                showRetry = true,
                onRetry = {
                    firebaseController.logGa4Event(
                        ga4Event = settingsActionGa4Event(actionName = SettingsActionNames.RETRY_LOAD)
                    )
                    viewModel.onEvent(event = SettingsEvent.Load)
                },
                paddingValues = paddingValues,
            )
        },
        onSuccess = { config: SettingsConfig ->
            // Searched above the rows, not from the app bar: the query stays with the list while
            // a result opens beside it on a wide window.
            var query by rememberSaveable { mutableStateOf("") }
            val index = rememberSettingsSearchIndex(config)
            val results = remember(index, query) { if (query.isBlank()) null else index.matching(query) }
            // Back clears the search before it leaves the page.
            ShellBackHandler(enabled = query.isNotEmpty()) { query = "" }
            SettingsList(
                paddingValues = paddingValues,
                settingsConfig = config,
                firebaseController = firebaseController,
                onPreferenceClick = { preference ->
                    if (preference.action?.invoke() != true) {
                        preference.destination?.let(navigator::navigate)
                    }
                },
                query = query,
                onQueryChange = { query = it },
                results = results,
            )
        },
    )
}

/**
 * The settings rows, grouped by category.
 *
 * With [onQueryChange] set, a search field heads the list, and while [results] is not null the
 * list shows those instead of the categories: every row the search found, host rows and the rows
 * of the settings pages alike, or a line saying none did.
 */
@Composable
fun SettingsList(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig,
    firebaseController: FirebaseController,
    onPreferenceClick: (SettingsPreference) -> Unit = {},
    query: String = "",
    onQueryChange: ((String) -> Unit)? = null,
    results: List<SettingsPreference>? = null,
) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        if (onQueryChange != null) {
            item(key = "settings_search") {
                GeneralTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    style = GeneralTextFieldStyle.SearchOutlined,
                    placeholder = stringResource(R.string.search_settings),
                    leadingIcon = ToolkitIcon.Vector(Icons.Outlined.Search),
                    trailingIcon = if (query.isNotEmpty()) ToolkitIcon.Vector(Icons.Outlined.Close) else null,
                    trailingIconContentDescription = stringResource(CoreUiR.string.clear_search),
                    onTrailingIconClick = { onQueryChange("") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SizeConstants.LargeSize)
                        .padding(top = SizeConstants.MediumSize),
                )
            }
        }
        if (results != null) {
            searchResults(results, firebaseController, onPreferenceClick)
            return@LazyColumn
        }
        settingsConfig.categories.forEachIndexed { categoryIndex: Int, category: SettingsCategory ->
            if (category.preferences.isNotEmpty()) {
                item(key = "settings_category_spacing_$categoryIndex") {
                    LargeVerticalSpacer()
                }

                itemsIndexed(
                    items = category.preferences,
                    key = { index: Int, preference: SettingsPreference ->
                        preference.key?.let { key ->
                            "settings_preference_${categoryIndex}_$key"
                        } ?: "settings_preference_${categoryIndex}_$index"
                    },
                ) { index: Int, preference: SettingsPreference ->
                    val position = groupedItemPosition(
                        index = index,
                        size = category.preferences.size,
                    )

                    SettingsPreferenceItem(
                        icon = preference.icon,
                        title = preference.title,
                        summary = preference.summary,
                        useIconContainer = preference.useIconContainer,
                        iconColor = preference.iconColor,
                        iconContainerColor = preference.iconContainerColor,
                        firebaseController = firebaseController,
                        ga4EventProvider = {
                            Ga4EventData(
                                name = SettingsAnalytics.Events.PREFERENCE_VIEW,
                                params = mapOf(
                                    SettingsAnalytics.Params.SCREEN to AnalyticsValue.Str(
                                        SETTINGS_SCREEN_NAME
                                    ),
                                    SettingsAnalytics.Params.PREFERENCE_KEY to AnalyticsValue.Str(
                                        preference.key ?: UNKNOWN_PREFERENCE_KEY
                                    ),
                                ),
                            )
                        },
                        onClick = { onPreferenceClick(preference) },
                        modifier = Modifier.groupedPreferenceItem(
                            position = position,
                            outerRadius = SizeConstants.ExtraLargeSize,
                        ),
                    )
                }
            }
        }
    }
}

/** The rows the search found, as one group, or a line saying there are none. */
private fun LazyListScope.searchResults(
    results: List<SettingsPreference>,
    firebaseController: FirebaseController,
    onPreferenceClick: (SettingsPreference) -> Unit,
) {
    item(key = "settings_search_spacing") { LargeVerticalSpacer() }
    if (results.isEmpty()) {
        item(key = "settings_search_empty") {
            Text(
                text = stringResource(R.string.no_settings_found),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SizeConstants.LargeSize),
            )
        }
        return
    }
    itemsIndexed(
        items = results,
        key = { index: Int, preference: SettingsPreference -> "settings_search_${preference.key ?: index}_$index" },
    ) { index: Int, preference: SettingsPreference ->
        SettingsPreferenceItem(
            icon = preference.icon,
            title = preference.title,
            summary = preference.summary,
            useIconContainer = preference.useIconContainer,
            iconColor = preference.iconColor,
            iconContainerColor = preference.iconContainerColor,
            firebaseController = firebaseController,
            onClick = { onPreferenceClick(preference) },
            modifier = Modifier.groupedPreferenceItem(
                position = groupedItemPosition(index = index, size = results.size),
                outerRadius = SizeConstants.ExtraLargeSize,
            ),
        )
    }
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

