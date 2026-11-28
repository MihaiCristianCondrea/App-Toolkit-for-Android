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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextField
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextFieldStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItemsIndexed
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItemDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.analytics.SETTINGS_SCREEN
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.analytics.SettingsActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.analytics.settingsActionGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.analytics.settingsPreferenceTapEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.contracts.SettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.search.rememberSettingsSearchIndex
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.states.SettingsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellBackHandler
import org.koin.compose.viewmodel.koinViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/**
 * Host-supplied settings categories with search and navigation. A row's external action gets
 * first chance to handle a click; otherwise its destination opens, beside the list on wide
 * windows or over it on phones.
 *
 * Owns [SettingsViewModel], tracking, and a saved search query. Loading on each composition
 * entry refreshes row summaries. The query remains with the list while a result opens beside
 * it; Back clears it before leaving the page.
 */
@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = koinViewModel()
    val state: SettingsUiState by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalShellNavigator.current
    val telemetry = LocalTelemetry.current

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(event = SettingsEvent.Load)
    }

    var query by rememberSaveable { mutableStateOf(value = "") }
    val config: SettingsConfig? = (state.config as? Loadable.Ready)?.value
    val results: List<SettingsPreference>? = config?.let { loaded ->
        val index = rememberSettingsSearchIndex(config = loaded)
        remember(key1 = index, key2 = query) {
            if (query.isBlank()) null else index.matching(query = query)
        }
    }
    ShellBackHandler(enabled = config != null && query.isNotEmpty()) { query = "" }

    ScreenHost(screen = SETTINGS_SCREEN, status = state.config) {
        SettingsScreenContent(
            state = state,
            contentPadding = contentPadding(),
            query = query,
            onQueryChange = { query = it },
            results = results,
            onPreferenceClick = { preference ->
                if (preference.action?.invoke() != true) {
                    preference.destination?.let { destination ->
                        navigator.navigate(key = destination)
                    }
                }
            },
            onRetry = {
                telemetry.logGa4Event(
                    ga4Event = settingsActionGa4Event(actionName = SettingsActionNames.RETRY_LOAD),
                )
                viewModel.onEvent(event = SettingsEvent.Load)
            },
        )
    }
}

/**
 * The stateless half of [SettingsScreen]: the list, or its loading, empty or failure state.
 *
 * The empty and failure states keep the settings icon and always offer a retry, which
 * [SettingsScreen] logs before it reloads.
 *
 * @param query The search query; [onQueryChange] edits it.
 * @param results The rows the search found, or null while there is no query.
 * @param onPreferenceClick A row was tapped.
 * @param onRetry The empty or failure state's retry was tapped.
 */
@Composable
internal fun SettingsScreenContent(
    state: SettingsUiState,
    contentPadding: PaddingValues,
    query: String,
    onQueryChange: (String) -> Unit,
    results: List<SettingsPreference>?,
    onPreferenceClick: (SettingsPreference) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenStateHandler(
        state = state.config,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = onRetry,
        retryWhenEmpty = true,
        icon = Icons.Outlined.Settings,
        onSuccess = { ready ->
            SettingsList(
                paddingValues = contentPadding,
                settingsConfig = ready.value,
                onPreferenceClick = onPreferenceClick,
                query = query,
                onQueryChange = onQueryChange,
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
 * of the settings pages alike, or a line saying none did. Categories with a non-blank title render
 * a [PreferenceCategoryItem] heading; untitled categories are separated by vertical spacing.
 */
@Composable
fun SettingsList(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig,
    modifier: Modifier = Modifier,
    onPreferenceClick: (SettingsPreference) -> Unit = {},
    query: String = "",
    onQueryChange: ((String) -> Unit)? = null,
    results: List<SettingsPreference>? = null,
) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space = ListItemDefaults.SegmentedGap),
    ) {
        if (onQueryChange != null) {
            animatedItem(key = "settings_search") {
                GeneralTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    style = GeneralTextFieldStyle.SearchOutlined,
                    placeholder = stringResource(id = R.string.search_settings),
                    leadingIcon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Search),
                    trailingIcon = if (query.isNotEmpty()) {
                        ToolkitIcon.Vector(imageVector = Icons.Outlined.Close)
                    } else {
                        null
                    },
                    trailingIconContentDescription = stringResource(id = CoreUiR.string.clear_search),
                    onTrailingIconClick = { onQueryChange("") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.large)
                        .padding(top = MaterialTheme.spacing.medium),
                )
            }
        }
        if (results != null) {
            searchResults(results = results, onPreferenceClick = onPreferenceClick)
            return@LazyColumn
        }
        settingsConfig.categories.forEachIndexed { categoryIndex: Int, category: SettingsCategory ->
            if (category.preferences.isNotEmpty()) {
                val categoryTitle = category.title?.takeIf { it.isNotBlank() }
                if (categoryTitle != null) {
                    animatedItem(key = "settings_category_title_$categoryIndex") {
                        PreferenceCategoryItem(title = categoryTitle)
                    }
                } else {
                    animatedItem(key = "settings_category_spacing_$categoryIndex") {
                        LargeVerticalSpacer()
                    }
                }

                animatedItemsIndexed(
                    items = category.preferences,
                    key = { index: Int, preference: SettingsPreference ->
                        preference.key?.let { key ->
                            "settings_preference_${categoryIndex}_$key"
                        } ?: "settings_preference_${categoryIndex}_$index"
                    },
                ) { index: Int, preference: SettingsPreference ->
                    SettingsPreferenceItem(
                        icon = preference.icon,
                        title = preference.title,
                        summary = preference.summary,
                        useIconContainer = preference.useIconContainer,
                        iconColor = preference.iconColor,
                        iconContainerColor = preference.iconContainerColor,
                        shapes = PreferenceItemDefaults.segmentedShapes(
                            index = index,
                            count = category.preferences.size,
                            outerShape = MaterialTheme.shapes.extraLarge,
                        ),
                        ga4EventProvider = {
                            settingsPreferenceTapEvent(preferenceKey = preference.key)
                        },
                        onClick = { onPreferenceClick(preference) },
                        modifier = Modifier.padding(
                            horizontal = MaterialTheme.spacing.large,
                            vertical = if (category.preferences.size == 1) MaterialTheme.spacing.extraTiny else MaterialTheme.spacing.none,
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
    onPreferenceClick: (SettingsPreference) -> Unit,
) {
    animatedItem(key = "settings_search_spacing") {
        LargeVerticalSpacer()
    }
    if (results.isEmpty()) {
        animatedItem(key = "settings_search_empty") {
            Text(
                text = stringResource(id = R.string.no_settings_found),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = MaterialTheme.spacing.large),
            )
        }
        return
    }
    animatedItemsIndexed(
        items = results,
        key = { index: Int, preference: SettingsPreference ->
            "settings_search_${preference.key ?: index}_$index"
        },
    ) { index: Int, preference: SettingsPreference ->
        SettingsPreferenceItem(
            icon = preference.icon,
            title = preference.title,
            summary = preference.summary,
            useIconContainer = preference.useIconContainer,
            iconColor = preference.iconColor,
            iconContainerColor = preference.iconContainerColor,
            shapes = PreferenceItemDefaults.segmentedShapes(
                index = index,
                count = results.size,
                outerShape = MaterialTheme.shapes.extraLarge,
            ),
            onClick = { onPreferenceClick(preference) },
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.large,
                vertical = if (results.size == 1) MaterialTheme.spacing.extraTiny else MaterialTheme.spacing.none,
            ),
        )
    }
}
