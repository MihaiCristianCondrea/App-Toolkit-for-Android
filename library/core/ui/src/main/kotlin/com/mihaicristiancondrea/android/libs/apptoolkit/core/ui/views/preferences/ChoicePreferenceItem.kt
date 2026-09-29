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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog

/**
 * A settings row that shows the chosen one of [options] as its summary and, when clicked, opens a
 * dialog listing them all as radio buttons. Picking one calls [onSelect] and closes the dialog.
 */
@Composable
fun <T> ChoicePreferenceItem(
    title: String,
    icon: ImageVector?,
    options: List<T>,
    selected: T,
    optionLabel: @Composable (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var open by rememberSaveable { mutableStateOf(false) }
    SettingsPreferenceItem(
        modifier = modifier,
        icon = icon,
        title = title,
        summary = optionLabel(selected),
        onClick = { open = true },
    )
    if (open) {
        BasicAlertDialog(
            onDismiss = { open = false },
            onConfirm = { open = false },
            icon = icon,
            title = title,
            showDismissButton = false,
            confirmButtonText = stringResource(R.string.done_button_content_description),
            content = {
                Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
                    options.forEachIndexed { index, option ->
                        RadioButtonPreferenceItem(
                            modifier = Modifier.groupedPreferenceItem(
                                position = groupedItemPosition(index, options.size),
                                outerRadius = SizeConstants.LargeMediumSize,
                                horizontalPadding = SizeConstants.ZeroSize,
                            ),
                            text = optionLabel(option),
                            isChecked = option == selected,
                            onCheckedChange = {
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
