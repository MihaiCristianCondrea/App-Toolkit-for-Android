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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ResultPill
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton

@Composable
fun CounterTool(count: Int, onIncrement: () -> Unit, onReset: () -> Unit) {

    Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
        ResultPill(label = count.toString())
        Row(horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize)) {
            GeneralButton(
                onClick = onIncrement,
                label = stringResource(id = R.string.tool_counter_increment),
            )
            GeneralOutlinedButton(
                onClick = onReset,
                vectorIcon = Icons.Outlined.Refresh,
                label = stringResource(id = R.string.tool_counter_reset),
            )
        }
    }
}
