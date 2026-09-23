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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.contracts.SeasonalThemesEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.states.SeasonalThemesUiState

/**
 * The easter egg's seasonal themes controls.
 *
 * Every change applies at once, like the rest of the theme screen, so the dialog has a single
 * button that closes it rather than a confirm and cancel pair.
 */
@Composable
fun SeasonalThemesDialog(
    state: SeasonalThemesUiState,
    onEvent: (SeasonalThemesEvent) -> Unit,
    onDismiss: () -> Unit,
) {
    BasicAlertDialog(
        onDismiss = onDismiss,
        onConfirm = onDismiss,
        icon = Icons.Outlined.Celebration,
        title = stringResource(id = R.string.seasonal_themes_title),
        confirmButtonText = stringResource(id = R.string.seasonal_themes_done),
        showDismissButton = false,
        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
            ) {
                Text(
                    text = stringResource(id = R.string.seasonal_themes_summary),
                    style = MaterialTheme.typography.bodyMedium,
                )
                SwitchRow(
                    label = stringResource(id = R.string.seasonal_themes_all_year),
                    checked = state.seasonal.allYear,
                    onCheckedChange = { onEvent(SeasonalThemesEvent.SetAllYear(it)) },
                )
                Text(
                    text = stringResource(id = R.string.seasonal_themes_wear),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = SizeConstants.SmallSize),
                )
                Column(modifier = Modifier.selectableGroup()) {
                    HolidaySeason.entries.forEach { season ->
                        HolidayRow(
                            label = stringResource(id = season.labelRes),
                            selected = state.wornHoliday == season,
                            onClick = { onEvent(SeasonalThemesEvent.WearHolidayTheme(season)) },
                        )
                    }
                }
                SwitchRow(
                    label = stringResource(id = R.string.seasonal_themes_snowfall),
                    summary = stringResource(id = R.string.seasonal_themes_snowfall_summary),
                    checked = state.seasonal.snowfallEnabled,
                    onCheckedChange = { onEvent(SeasonalThemesEvent.SetSnowfall(it)) },
                )
            }
        },
    )
}

private val HolidaySeason.labelRes: Int
    get() = when (this) {
        HolidaySeason.CHRISTMAS -> R.string.seasonal_theme_christmas
        HolidaySeason.HALLOWEEN -> R.string.seasonal_theme_halloween
    }

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    summary: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = checked, role = Role.Switch, onValueChange = onCheckedChange)
            .padding(vertical = SizeConstants.SmallSize),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            if (summary != null) {
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Composable
private fun HolidayRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
            .padding(vertical = SizeConstants.ExtraSmallSize),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = SizeConstants.SmallSize),
        )
    }
}
