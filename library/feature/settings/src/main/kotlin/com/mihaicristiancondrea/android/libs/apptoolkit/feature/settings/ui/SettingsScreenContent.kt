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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextField
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextFieldStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.states.SettingsUiState

private const val UNKNOWN_PREFERENCE_KEY = "unknown"

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
) {
    ScreenStateHandler(
        state = state.config,
        contentPadding = contentPadding,
        onRetry = onRetry,
        onEmpty = { empty ->
            NoDataScreen(
                icon = Icons.Outlined.Settings,
                message = empty.message?.asString(),
                showRetry = true,
                onRetry = onRetry,
                paddingValues = contentPadding,
            )
        },
        onError = { failed ->
            NoDataScreen(
                icon = Icons.Outlined.Settings,
                message = failed.message.asString(),
                isError = true,
                showRetry = failed.retryable,
                onRetry = onRetry,
                paddingValues = contentPadding,
            )
        },
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
 * of the settings pages alike, or a line saying none did.
 */
@Composable
fun SettingsList(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig,
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
            searchResults(results, onPreferenceClick)
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
            onClick = { onPreferenceClick(preference) },
            modifier = Modifier.groupedPreferenceItem(
                position = groupedItemPosition(index = index, size = results.size),
                outerRadius = SizeConstants.ExtraLargeSize,
            ),
        )
    }
}

// Only the loaded and loading states have previews: the empty and failure states draw
// `NoDataScreen`, which resolves its ad unit through Koin.
@Preview(showBackground = true)
@Composable
private fun SettingsScreenContentPreview() {
    MaterialTheme {
        SettingsScreenContent(
            state = SettingsUiState(
                config = Loadable.Ready(
                    SettingsConfig(
                        title = "Settings",
                        categories = listOf(
                            SettingsCategory(
                                preferences = listOf(
                                    SettingsPreference(
                                        key = "notifications",
                                        icon = Icons.Outlined.Notifications,
                                        title = "Notifications",
                                        summary = "Alerts and reminders",
                                    ),
                                    SettingsPreference(
                                        key = "display",
                                        icon = Icons.Outlined.Settings,
                                        title = "Display",
                                        summary = "Layout and text size",
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
            contentPadding = PaddingValues(),
            query = "",
            onQueryChange = {},
            results = null,
            onPreferenceClick = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenContentLoadingPreview() {
    MaterialTheme {
        SettingsScreenContent(
            state = SettingsUiState(config = Loadable.Loading),
            contentPadding = PaddingValues(),
            query = "",
            onQueryChange = {},
            results = null,
            onPreferenceClick = {},
            onRetry = {},
        )
    }
}
