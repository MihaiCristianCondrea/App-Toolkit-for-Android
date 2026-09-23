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
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.theme.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R

/**
 * Greets a holiday and offers its theme for the length of the holiday.
 *
 * The checkbox starts ticked: the greeting exists to offer the theme, and a person who does not
 * want it unticks one box. Closing the dialog any other way (back, or a tap outside) keeps the
 * current theme, since nothing was confirmed.
 *
 * @param onAnswer Called once with whether the holiday theme should be applied.
 */
@Composable
fun HolidayGreetingDialog(
    season: HolidaySeason,
    onAnswer: (useHolidayTheme: Boolean) -> Unit,
) {
    var useHolidayTheme: Boolean by rememberSaveable { mutableStateOf(true) }

    BasicAlertDialog(
        onDismiss = { onAnswer(false) },
        onConfirm = { onAnswer(useHolidayTheme) },
        icon = when (season) {
            HolidaySeason.CHRISTMAS -> Icons.Outlined.AcUnit
            HolidaySeason.HALLOWEEN -> Icons.Outlined.Celebration
        },
        title = stringResource(
            id = when (season) {
                HolidaySeason.CHRISTMAS -> R.string.holiday_greeting_christmas_title
                HolidaySeason.HALLOWEEN -> R.string.holiday_greeting_halloween_title
            },
        ),
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
                Text(
                    text = stringResource(
                        id = when (season) {
                            HolidaySeason.CHRISTMAS -> R.string.holiday_greeting_christmas_message
                            HolidaySeason.HALLOWEEN -> R.string.holiday_greeting_halloween_message
                        },
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = useHolidayTheme,
                            role = Role.Checkbox,
                            onValueChange = { useHolidayTheme = it },
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // The row carries the toggle, so the box itself takes no clicks of its own and
                    // screen readers announce the row once, with its label.
                    Checkbox(checked = useHolidayTheme, onCheckedChange = null)
                    Text(
                        text = stringResource(id = R.string.holiday_greeting_use_theme),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        },
        showDismissButton = false,
    )
}
