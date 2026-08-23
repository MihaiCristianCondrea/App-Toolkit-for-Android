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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.CheckBoxPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchCardItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SwitchPreferenceItemWithDivider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.app.components.ui.views.ShowcaseSection
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PreferenceShowcase(
    firebaseController: FirebaseController,
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
    ShowcaseHeader(
        title = stringResource(id = R.string.components_section_preferences),
        icon = Icons.Outlined.Category,
    )

    // Group 1: Standard & Mixed Preferences
    val group1Size = 4
    ShowcaseSection {
        Text(
            text = "Interactive Toggles",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = SizeConstants.SmallSize),
        )
        SmallVerticalSpacer()
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .groupedCorners(GroupedItemPosition.SINGLE),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = 1.dp,
        ) {
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    surface = Color.Transparent,
                )
            ) {
                Column {
                    SettingsPreferenceItem(
                        modifier = Modifier.groupedCorners(
                            groupedItemPosition(index = 0, size = group1Size),
                        ),
                        title = stringResource(id = R.string.components_preference_title),
                        summary = stringResource(id = R.string.components_preference_summary),
                        firebaseController = firebaseController,
                        ga4Event = onLogEvent("preference", "settings_primary"),
                    )
                    SwitchPreferenceItem(
                        modifier = Modifier.groupedCorners(
                            groupedItemPosition(index = 1, size = group1Size),
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
                            groupedItemPosition(index = 2, size = group1Size),
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
                    CheckBoxPreferenceItem(
                        modifier = Modifier.groupedCorners(
                            groupedItemPosition(index = 3, size = group1Size),
                        ),
                        icon = Icons.Outlined.CheckCircle,
                        title = stringResource(id = R.string.components_checkbox_title),
                        summary = stringResource(id = R.string.components_checkbox_summary),
                        checked = checkboxChecked,
                        onCheckedChange = onCheckboxChanged,
                        firebaseController = firebaseController,
                        ga4Event = onLogEvent("preference", "checkbox"),
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(2.dp))

    // Group 2: Switch Card
    ShowcaseSection {
        Text(
            text = "Modern Selection",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = SizeConstants.SmallSize),
        )
        SmallVerticalSpacer()
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                secondaryContainer = MaterialTheme.colorScheme.surfaceContainerLow,
            )
        ) {
            SwitchCardItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .groupedCorners(GroupedItemPosition.SINGLE),
                title = stringResource(id = R.string.components_switch_card_title),
                switchState = switchCardState,
                onSwitchToggled = onSwitchCardChanged,
                firebaseController = firebaseController,
                ga4Event = onLogEvent("preference", "switch_card"),
            )
        }
    }

    Spacer(modifier = Modifier.height(2.dp))

    // Group 3: Radio Selections
    ShowcaseSection {
        Text(
            text = "Options",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = SizeConstants.SmallSize),
        )
        SmallVerticalSpacer()
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .groupedCorners(GroupedItemPosition.SINGLE),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = 1.dp,
        ) {
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    surface = Color.Transparent,
                )
            ) {
                Column {
                    radioOptions.forEachIndexed { index, option ->
                        RadioButtonPreferenceItem(
                            modifier = Modifier.groupedCorners(
                                groupedItemPosition(
                                    index = index,
                                    size = radioOptions.size,
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
        }
    }
}
