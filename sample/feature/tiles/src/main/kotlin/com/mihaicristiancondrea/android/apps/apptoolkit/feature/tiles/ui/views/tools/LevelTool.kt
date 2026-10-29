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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.Cookie16Shape
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ResultPill
import kotlin.math.abs

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LevelTool(pitch: Float, roll: Float, compact: Boolean = false) {
    val colors = MaterialTheme.colorScheme
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Canvas(Modifier.size(if (compact) 112.dp else 200.dp).clip(Cookie16Shape)) {
            drawRect(colors.primaryContainer)
            drawLine(colors.onPrimaryContainer.copy(alpha = .25f), Offset(0f, center.y), Offset(size.width, center.y), 1.dp.toPx())
            drawLine(colors.onPrimaryContainer.copy(alpha = .25f), Offset(center.x, 0f), Offset(center.x, size.height), 1.dp.toPx())
            drawCircle(colors.onPrimaryContainer.copy(alpha = .4f), size.minDimension * .12f, style = Stroke(1.dp.toPx()))
            drawCircle(if (abs(pitch) < 1f && abs(roll) < 1f) colors.primary else colors.secondary,
                size.minDimension * .075f, center + Offset(-roll.coerceIn(-45f,45f), -pitch.coerceIn(-45f,45f)) * (size.minDimension * .4f / 45f))
        }
        if (compact) {
            Text("P: "+pitch.toInt()+"° · R: "+roll.toInt()+"°", style = MaterialTheme.typography.bodySmall)
        } else Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            ResultPill("P: "+pitch.toInt()+"°", start = true)
            ResultPill("R: "+roll.toInt()+"°", start = false)
        }
    }
}
