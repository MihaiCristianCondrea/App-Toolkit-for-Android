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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.text.font.FontWeight
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dropdown.CommonDropdownMenuItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.DatePickerTextField
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.DropdownMenuBox
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.MediumVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSurface
import kotlinx.collections.immutable.ImmutableList

@Composable
fun InputShowcase(
    firebaseController: FirebaseController,
    onLogEvent: (String, String?) -> Ga4EventData,
    dateMillis: Long,
    onDateSelected: (Long) -> Unit,
    selectedDropdownOption: String,
    dropdownOptions: ImmutableList<String>,
    onDropdownOptionSelected: (String) -> Unit,
) {
    val iconContentDescription = stringResource(id = R.string.components_icon_content_description)
    val menuLabel = stringResource(id = R.string.components_menu_label)

    ShowcaseHeader(
        title = stringResource(id = R.string.components_section_inputs),
        icon = Icons.Outlined.Tune,
    )
    ShowcaseSection {
        ShowcaseSurface(position = GroupedItemPosition.FIRST) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.components_action_menus),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = stringResource(id = R.string.components_action_menus_summary),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                var showMenu by rememberSaveable { mutableStateOf(value = false) }
                Box {
                    GeneralOutlinedButton(
                        label = menuLabel,
                        vectorIcon = Icons.Filled.MoreVert,
                        iconContentDescription = iconContentDescription,
                        onClick = { showMenu = true },
                        firebaseController = firebaseController,
                        ga4Event = onLogEvent("dropdown", "menu_button"),
                    )
                    DropdownMenu(
                        expanded = showMenu,
                        shape = MaterialTheme.shapes.largeIncreased,
                        onDismissRequest = { showMenu = false },
                    ) {
                        CommonDropdownMenuItem(
                            textResId = R.string.components_menu_option_primary,
                            icon = Icons.Outlined.Info,
                            onClick = { showMenu = false },
                            firebaseController = firebaseController,
                            ga4Event = onLogEvent("dropdown", "menu_option_primary"),
                        )
                        CommonDropdownMenuItem(
                            textResId = R.string.components_menu_option_secondary,
                            icon = Icons.Outlined.Favorite,
                            onClick = { showMenu = false },
                            firebaseController = firebaseController,
                            ga4Event = onLogEvent("dropdown", "menu_option_secondary"),
                        )
                    }
                }
            }
        }
        ShowcaseSurface(position = GroupedItemPosition.LAST) {
            Text(
                text = stringResource(id = R.string.components_data_selection),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            MediumVerticalSpacer()
            DatePickerTextField(
                dateMillis = dateMillis,
                onDateSelected = onDateSelected,
                firebaseController = firebaseController,
                ga4Event = onLogEvent("input", "date_picker"),
            )
            SmallVerticalSpacer()
            DropdownMenuBox(
                selectedText = selectedDropdownOption,
                options = dropdownOptions,
                onOptionSelected = onDropdownOptionSelected,
                firebaseController = firebaseController,
                ga4Event = onLogEvent("input", "dropdown"),
            )
        }
    }
}
