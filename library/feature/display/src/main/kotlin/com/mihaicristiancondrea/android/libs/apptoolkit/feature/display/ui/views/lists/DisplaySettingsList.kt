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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.views.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItemsIndexed
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItemWithDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics.DisplayActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics.DisplayPreferenceKeys
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics.displayActionGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.contracts.DisplaySettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.formatters.summaryText
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplayCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplayRow
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models.DisplaySettings
import kotlinx.collections.immutable.ImmutableList

/**
 * Renders the grouped display settings rows under their category headings.
 */
@Composable
internal fun DisplaySettingsList(
    settings: DisplaySettings,
    rows: ImmutableList<DisplayRow>,
    onEvent: (DisplaySettingsEvent) -> Unit,
    onDarkThemeChanged: (Boolean) -> Unit,
    onOpenThemeSettings: () -> Unit,
    onOpenStartupPage: () -> Unit,
    onOpenLanguage: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val isSystemDarkTheme: Boolean = isSystemInDarkTheme()
    val isDarkThemeActive: Boolean = when (settings.themeMode) {
        DataStoreNamesConstants.THEME_MODE_DARK -> true
        DataStoreNamesConstants.THEME_MODE_LIGHT -> false
        else -> isSystemDarkTheme
    }
    val themeSummary: String = when (settings.themeMode) {
        DataStoreNamesConstants.THEME_MODE_DARK, DataStoreNamesConstants.THEME_MODE_LIGHT -> stringResource(
            id = R.string.will_never_turn_on_automatically,
        )

        else -> stringResource(id = R.string.will_turn_on_automatically_by_system)
    }
    val categories: List<Pair<DisplayCategory, List<DisplayRow>>> = remember(rows) {
        rows.groupBy { it.category }.toList()
    }

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        categories.forEach { (category, categoryRows) ->
            animatedItem(key = "category_${category.name}") {
                PreferenceCategoryItem(title = stringResource(id = category.title))
            }
            animatedItemsIndexed(
                items = categoryRows,
                key = { _, row -> "row_${row.name}" },
            ) { index, row ->
                val rowModifier = Modifier.groupedPreferenceItem(
                    position = groupedItemPosition(index = index, size = categoryRows.size),
                    outerRadius = SizeConstants.LargeMediumSize,
                )
                when (row) {
                    DisplayRow.DarkTheme -> SwitchPreferenceItemWithDivider(
                        title = stringResource(id = row.title),
                        summary = themeSummary,
                        checked = isDarkThemeActive,
                        onCheckedChange = onDarkThemeChanged,
                        onSwitchClick = {},
                        onClick = onOpenThemeSettings,
                        modifier = rowModifier,
                    )

                    DisplayRow.DynamicColors -> SwitchPreferenceItem(
                        title = stringResource(id = row.title),
                        summary = row.summaryText(),
                        checked = settings.dynamicColors,
                        onCheckedChange = { isChecked ->
                            onEvent(DisplaySettingsEvent.DynamicColorsChanged(enabled = isChecked))
                        },
                        modifier = rowModifier,
                    )

                    DisplayRow.BounceButtons -> SwitchPreferenceItem(
                        title = stringResource(id = row.title),
                        summary = row.summaryText(),
                        checked = settings.bouncyButtons,
                        onCheckedChange = { isChecked ->
                            onEvent(DisplaySettingsEvent.BouncyButtonsChanged(enabled = isChecked))
                        },
                        modifier = rowModifier,
                    )

                    DisplayRow.StartupPage -> SettingsPreferenceItem(
                        title = stringResource(id = row.title),
                        summary = row.summaryText(),
                        onClick = onOpenStartupPage,
                        ga4Event = displayActionGa4Event(
                            actionName = DisplayActionNames.OPEN_STARTUP_DIALOG,
                            preferenceKey = DisplayPreferenceKeys.STARTUP_PAGE,
                        ),
                        modifier = rowModifier,
                    )

                    DisplayRow.NavigationLabels -> SwitchPreferenceItem(
                        title = stringResource(id = row.title),
                        summary = row.summaryText(),
                        checked = settings.showBottomBarLabels,
                        onCheckedChange = { isChecked ->
                            onEvent(DisplaySettingsEvent.BottomBarLabelsChanged(show = isChecked))
                        },
                        modifier = rowModifier,
                    )

                    DisplayRow.Language -> SettingsPreferenceItem(
                        title = stringResource(id = row.title),
                        summary = row.summaryText(),
                        onClick = onOpenLanguage,
                        modifier = rowModifier,
                    )
                }
            }
        }
    }
}
