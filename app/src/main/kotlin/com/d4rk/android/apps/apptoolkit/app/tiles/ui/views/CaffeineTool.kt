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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.CaffeineState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun CaffeineTool(
    state: CaffeineState,
    onCycle: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (state != CaffeineState.Off) 1.2f else 1.0f,
        label = "caffeine-scale"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        val label = when (state) {
            CaffeineState.Off -> stringResource(id = R.string.tool_caffeine_off)
            CaffeineState.Infinite -> stringResource(id = R.string.tool_caffeine_infinite)
            else -> stringResource(id = state.labelResId)
        }

        ResultPill(
            label = if (state == CaffeineState.Off) label else stringResource(
                id = R.string.tool_caffeine_remaining,
                label
            )
        )

        Box(
            modifier = Modifier
                .padding(vertical = SizeConstants.LargeSize)
                .scale(scale),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Coffee,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = if (state != CaffeineState.Off)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline
            )
        }

        Spacer(modifier = Modifier.height(SizeConstants.SmallSize))

        Button(onClick = onCycle) {
            Text(text = stringResource(id = R.string.tool_system_cycle_mode))
        }

        Text(
            text = "Tap to cycle durations",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
