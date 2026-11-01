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

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.WaterEjectToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.WaterEjectToolEvent

@Composable
internal fun WaterEjectTool(state: WaterEjectToolUiState, onEvent: (WaterEjectToolEvent) -> Unit) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        WaterEjectVisual(state.running)
        Text(if (state.running) state.seconds.toString()+" s" else "165 Hz", style = MaterialTheme.typography.displayMedium)
        Text(stringResource(R.string.water_guidance), style = MaterialTheme.typography.bodyMedium)
        Button(onClick = { onEvent(if (state.running) WaterEjectToolEvent.Stop else WaterEjectToolEvent.Start) }, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)) {
            Text(stringResource(if (state.running) R.string.water_stop else R.string.water_start))
        }
        if (state.failed) Text(stringResource(R.string.water_failed), color = MaterialTheme.colorScheme.error)
        Text(stringResource(R.string.water_note), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
internal fun WaterEjectVisual(running: Boolean, compact: Boolean = false) {
    val colors = MaterialTheme.colorScheme
    val phase = if (running) {
        val transition = rememberInfiniteTransition(label = "speaker-rings")
        val value by transition.animateFloat(0f,1f,infiniteRepeatable(tween(1800, easing = LinearEasing)),label = "speaker-wave")
        value
    } else 0f
    Box(Modifier.size(if (compact) 120.dp else 200.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val radius = size.minDimension * .3f
            for (offset in listOf(0f,.5f)) {
                val wave = (phase + offset) % 1f
                drawCircle(colors.primary.copy(alpha = if (running) (1f-wave)*.4f else .12f), radius * (1f+wave*.5f), style = Stroke(2.dp.toPx()))
            }
            drawCircle(colors.primaryContainer, radius)
        }
        Icon(ToolSymbols.WaterDrop, null, Modifier.size(if (compact) 40.dp else 64.dp), tint = colors.onPrimaryContainer)
    }
}
