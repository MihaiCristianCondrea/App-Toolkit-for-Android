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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.digits.AnimatedDigit

@Composable
fun CounterTool(count: Int, onIncrement: () -> Unit, onReset: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(vertical = SizeConstants.SmallSize),
        ) {
            Box(
                modifier = Modifier.padding(
                    horizontal = SizeConstants.ExtraLargeIncreasedSize,
                    vertical = SizeConstants.LargeSize,
                ),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val countString = count.toString()
                    countString.forEachIndexed { index, char ->
                        key(countString.length - index) {
                            AnimatedDigit(
                                digit = char,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textStyle = MaterialTheme.typography.displayMedium,
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
        ) {
            GeneralButton(
                modifier = Modifier.weight(1f),
                onClick = onIncrement,
                measurements = ButtonMeasurements.Medium,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Add),
                label = stringResource(id = R.string.tool_counter_increment),
            )
            if (count != 0) {
                GeneralButton(
                    style = GeneralButtonStyle.Outlined,
                    measurements = ButtonMeasurements.Medium,
                    onClick = onReset,
                    icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Refresh),
                    label = stringResource(id = R.string.tool_counter_reset),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterToolPreview() {
    MaterialTheme {
        Surface {
            CounterTool(
                count = 42,
                onIncrement = {},
                onReset = {},
            )
        }
    }
}
