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

package com.d4rk.android.apps.apptoolkit.app.main.ui.views.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton

@Composable
fun MainFloatingActionButton(
    modifier: Modifier = Modifier,
    visible: Boolean,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val label = stringResource(id = R.string.open_random_app)
    AnimatedExtendedFloatingActionButton(
        modifier = modifier,
        visible = visible,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Casino,
                contentDescription = label,
            )
        },
        text = {
            Text(text = label)
        },
        expanded = expanded,
    )
}
