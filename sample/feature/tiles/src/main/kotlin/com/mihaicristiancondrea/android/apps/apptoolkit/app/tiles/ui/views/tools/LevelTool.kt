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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.views.ResultPill
import com.mihaicristiancondrea.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlin.math.abs

@Composable
fun LevelTool(pitch: Float, roll: Float) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)) {
            ResultPill(label = "P: ${pitch.toInt()}°")
            ResultPill(label = "R: ${roll.toInt()}°")
        }
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Target center circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Transparent, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
            )
            // Moving bubble
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .graphicsLayer {
                        translationX = -(roll.coerceIn(-45f, 45f) / 45f) * 80.dp.toPx()
                        translationY = -(pitch.coerceIn(-45f, 45f) / 45f) * 80.dp.toPx()
                    }
                    .background(
                        if (abs(pitch) < 1f && abs(roll) < 1f)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary,
                        CircleShape
                    )
            )
        }
    }
}
