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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.CheckBoxPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItemWithDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.ShowcaseSection
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PreferenceShowcase(
    firebaseController: FirebaseController,
    onLogEvent: (String, String?) -> Ga4EventData,
    preferenceItemCount: Int,
    switchEnabled: Boolean,
    onSwitchEnabledChanged: (Boolean) -> Unit,
    switchWithDividerEnabled: Boolean,
    onSwitchWithDividerChanged: (Boolean) -> Unit,
    switchCardState: State<Boolean>,
    onSwitchCardChanged: (Boolean) -> Unit,
    checkboxChecked: Boolean,
    onCheckboxChanged: (Boolean) -> Unit,
    radioOptions: ImmutableList<String>,
    selectedRadioOption: String,
    onRadioOptionSelected: (String) -> Unit,
) {
    ShowcaseHeader(
        title = stringResource(id = R.string.components_section_preferences),
        icon = Icons.Outlined.Category,
        badge = "Full Set",
    )
    ShowcaseSection {
        SettingsPreferenceItem(
            modifier = Modifier.groupedCorners(
                groupedItemPosition(index = 0, size = preferenceItemCount),
            ),
            title = stringResource(id = R.string.components_preference_title),
            summary = stringResource(id = R.string.components_preference_summary),
            firebaseController = firebaseController,
            ga4Event = onLogEvent("preference", "settings_primary"),
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .groupedCorners(groupedItemPosition(index = 1, size = preferenceItemCount)),
            color = MaterialTheme.colorScheme.surface,
        ) {
            PreferenceItem(
                title = stringResource(id = R.string.components_preference_secondary_title),
                summary = stringResource(id = R.string.components_preference_secondary_summary),
                firebaseController = firebaseController,
                ga4Event = onLogEvent("preference", "secondary"),
            )
        }
        SwitchPreferenceItem(
            modifier = Modifier.groupedCorners(
                groupedItemPosition(index = 2, size = preferenceItemCount),
            ),
            icon = Icons.Outlined.Favorite,
            title = stringResource(id = R.string.components_switch_title),
            summary = stringResource(id = R.string.components_switch_summary),
            checked = switchEnabled,
            onCheckedChange = onSwitchEnabledChanged,
            firebaseController = firebaseController,
            ga4Event = onLogEvent("preference", "switch"),
        )
        SwitchPreferenceItemWithDivider(
            modifier = Modifier.groupedCorners(
                groupedItemPosition(index = 3, size = preferenceItemCount),
            ),
            icon = Icons.Outlined.Info,
            title = stringResource(id = R.string.components_switch_divider_title),
            summary = stringResource(id = R.string.components_switch_divider_summary),
            checked = switchWithDividerEnabled,
            onCheckedChange = onSwitchWithDividerChanged,
            onClick = {},
            onSwitchClick = {},
            firebaseController = firebaseController,
            ga4Event = onLogEvent("preference", "switch_divider"),
        )
        SwitchCardItem(
            modifier = Modifier
                .fillMaxWidth()
                .groupedCorners(
                    groupedItemPosition(index = 4, size = preferenceItemCount),
                ),
            title = stringResource(id = R.string.components_switch_card_title),
            switchState = switchCardState,
            onSwitchToggled = onSwitchCardChanged,
            firebaseController = firebaseController,
            ga4Event = onLogEvent("preference", "switch_card"),
        )
        CheckBoxPreferenceItem(
            modifier = Modifier.groupedCorners(
                groupedItemPosition(index = 5, size = preferenceItemCount),
            ),
            icon = Icons.Outlined.CheckCircle,
            title = stringResource(id = R.string.components_checkbox_title),
            summary = stringResource(id = R.string.components_checkbox_summary),
            checked = checkboxChecked,
            onCheckedChange = onCheckboxChanged,
            firebaseController = firebaseController,
            ga4Event = onLogEvent("preference", "checkbox"),
        )
        radioOptions.forEachIndexed { index, option ->
            RadioButtonPreferenceItem(
                modifier = Modifier.groupedCorners(
                    groupedItemPosition(
                        index = index + 6,
                        size = preferenceItemCount,
                    ),
                ),
                text = option,
                isChecked = selectedRadioOption == option,
                onCheckedChange = { onRadioOptionSelected(option) },
                firebaseController = firebaseController,
                ga4Event = onLogEvent("preference", "radio_$option"),
            )
        }
    }
}
