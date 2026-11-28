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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog

/**
 * Preference row showing the selected option as its summary. Tapping it opens a radio-choice
 * dialog; selection calls [onSelect] and closes the dialog.
 *
 * @param dialogIcon Icon for the dialog, independent of the optional row [icon].
 */
@Composable
fun <T> ChoicePreferenceItem(
    title: String,
    options: List<T>,
    selected: T,
    optionLabel: @Composable (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    dialogIcon: ImageVector? = icon,
    shapes: ListItemShapes = PreferenceItemDefaults.segmentedShapes(index = 0, count = 1),
    colors: ListItemColors = PreferenceItemDefaults.colors(),
) {
    var open by rememberSaveable { mutableStateOf(false) }
    SettingsPreferenceItem(
        modifier = modifier,
        icon = icon?.let(ToolkitIcon::Vector),
        title = title,
        summary = optionLabel(selected),
        shapes = shapes,
        colors = colors,
        onClick = { open = true },
    )
    if (open) {
        BasicAlertDialog(
            onDismiss = { open = false },
            onConfirm = { open = false },
            icon = dialogIcon,
            title = title,
            showDismissButton = false,
            confirmButtonText = stringResource(R.string.done_button_content_description),
            content = {
                Column(
                    modifier = Modifier.selectableGroup(),
                    verticalArrangement = Arrangement.spacedBy(space = ListItemDefaults.SegmentedGap),
                ) {
                    options.forEachIndexed { index, option ->
                        RadioButtonPreferenceItem(
                            text = optionLabel(option),
                            selected = option == selected,
                            shapes = PreferenceItemDefaults.segmentedShapes(
                                index = index,
                                count = options.size,
                            ),
                            onSelect = {
                                onSelect(option)
                                open = false
                            },
                        )
                    }
                }
            },
        )
    }
}
