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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.CheckBoxPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItemDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItemWithDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceShowcase(
    onLogEvent: (String, String?) -> Ga4EventData,
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
    val showcaseColors = PreferenceItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    )

    ShowcaseHeader(
        title = stringResource(id = R.string.components_section_preferences),
        icon = Icons.Outlined.Category,
    )

    val group1Size = 4
    ShowcaseSection {
        Text(
            text = "Interactive Toggles",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = MaterialTheme.spacing.small),
        )
        SmallVerticalSpacer()
        Column(
            verticalArrangement = Arrangement.spacedBy(space = ListItemDefaults.SegmentedGap),
        ) {
            SettingsPreferenceItem(
                title = stringResource(id = R.string.components_preference_title),
                summary = stringResource(id = R.string.components_preference_summary),
                shapes = PreferenceItemDefaults.segmentedShapes(index = 0, count = group1Size),
                colors = showcaseColors,
                ga4Event = onLogEvent("preference", "settings_primary"),
            )
            SwitchPreferenceItem(
                icon = Icons.Outlined.Favorite,
                title = stringResource(id = R.string.components_switch_title),
                summary = stringResource(id = R.string.components_switch_summary),
                checked = switchEnabled,
                onCheckedChange = onSwitchEnabledChanged,
                shapes = PreferenceItemDefaults.segmentedShapes(index = 1, count = group1Size),
                colors = showcaseColors,
                ga4Event = onLogEvent("preference", "switch"),
            )
            SwitchPreferenceItemWithDivider(
                icon = Icons.Outlined.Info,
                title = stringResource(id = R.string.components_switch_divider_title),
                summary = stringResource(id = R.string.components_switch_divider_summary),
                checked = switchWithDividerEnabled,
                onCheckedChange = onSwitchWithDividerChanged,
                onClick = {},
                shapes = PreferenceItemDefaults.segmentedShapes(index = 2, count = group1Size),
                colors = showcaseColors,
                ga4Event = onLogEvent("preference", "switch_divider"),
            )
            CheckBoxPreferenceItem(
                icon = Icons.Outlined.CheckCircle,
                title = stringResource(id = R.string.components_checkbox_title),
                summary = stringResource(id = R.string.components_checkbox_summary),
                checked = checkboxChecked,
                onCheckedChange = onCheckboxChanged,
                shapes = PreferenceItemDefaults.segmentedShapes(index = 3, count = group1Size),
                colors = showcaseColors,
                ga4Event = onLogEvent("preference", "checkbox"),
            )
        }
    }

    LargeVerticalSpacer()

    ShowcaseSection {
        Text(
            text = "Modern Selection",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = MaterialTheme.spacing.small),
        )
        SmallVerticalSpacer()
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                secondaryContainer = MaterialTheme.colorScheme.surfaceContainerLow,
            )
        ) {
            SwitchCardItem(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.components_switch_card_title),
                switchState = switchCardState,
                onSwitchToggled = onSwitchCardChanged,
                ga4Event = onLogEvent("preference", "switch_card"),
            )
        }
    }

    LargeVerticalSpacer()

    ShowcaseSection {
        Text(
            text = "Options",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = MaterialTheme.spacing.small),
        )
        SmallVerticalSpacer()
        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(space = ListItemDefaults.SegmentedGap),
        ) {
            radioOptions.forEachIndexed { index, option ->
                RadioButtonPreferenceItem(
                    text = option,
                    selected = selectedRadioOption == option,
                    onSelect = { onRadioOptionSelected(option) },
                    shapes = PreferenceItemDefaults.segmentedShapes(
                        index = index,
                        count = radioOptions.size,
                    ),
                    colors = showcaseColors,
                    ga4Event = onLogEvent("preference", "radio_$option"),
                )
            }
        }
    }
}
