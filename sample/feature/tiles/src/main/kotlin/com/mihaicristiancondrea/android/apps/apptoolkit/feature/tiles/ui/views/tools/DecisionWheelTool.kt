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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.WheelToolEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.WheelToolUiState
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun DecisionWheelTool(state: WheelToolUiState, onEvent: (WheelToolEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = state.text, onValueChange = { onEvent(WheelToolEvent.ChoicesChanged(it)) },
            label = { Text(stringResource(R.string.wheel_choices)) }, supportingText = { Text(stringResource(R.string.wheel_hint)) },
            enabled = !state.spinning, minLines = 3, maxLines = 5, isError = !state.valid && state.text.isNotBlank(), modifier = Modifier.fillMaxWidth())
        Surface(shape = RoundedCornerShape(28.dp,28.dp,8.dp,8.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
            Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                DecisionWheelVisual(state, onSettled = { onEvent(WheelToolEvent.Settled) })
            }
        }
        Surface(shape = RoundedCornerShape(8.dp,8.dp,28.dp,28.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
            Column(Modifier.fillMaxWidth().padding(12.dp).semantics { liveRegion = LiveRegionMode.Polite }, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(if (state.spinning) R.string.wheel_spinning else R.string.wheel_result), style = MaterialTheme.typography.labelSmall)
                Text(if (state.spinning) stringResource(R.string.wheel_spinning) else state.winner ?: stringResource(R.string.wheel_ready), style = MaterialTheme.typography.headlineSmall)
            }
        }
        Button(onClick = { onEvent(WheelToolEvent.Spin) }, enabled = state.valid && !state.spinning, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)) { Text(stringResource(R.string.wheel_spin)) }
        if (!state.valid && state.text.isNotBlank()) Text(stringResource(R.string.wheel_invalid), color = MaterialTheme.colorScheme.error)
    }
}

@Composable
internal fun DecisionWheelVisual(state: WheelToolUiState, compact: Boolean = false, onSettled: () -> Unit = {}) {
    val rotation = remember { Animatable(state.rotation) }
    val latestSettled by rememberUpdatedState(onSettled)
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(state.target, state.spinning) {
        if (state.spinning) {
            rotation.animateTo(state.target, tween(3000, easing = CubicBezierEasing(.12f,.7f,.12f,1f)))
            if (!compact) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            latestSettled()
        } else rotation.snapTo(state.rotation)
    }
    val colors = MaterialTheme.colorScheme
    // The kit's third fill is color-mix(primary 24%, surface): opaque, so it reads the same on any card.
    val fills = listOf(colors.primary, colors.primaryContainer, androidx.compose.ui.graphics.lerp(colors.surface, colors.primary, .24f))
    val inks = listOf(colors.onPrimary, colors.onPrimaryContainer, colors.onSurface)
    val measurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.labelSmall
    Canvas(Modifier.size(if (compact) 136.dp else 248.dp)) {
        val radius = size.minDimension / 2f - 8.dp.toPx()
        drawCircle(colors.surface, radius + 6.dp.toPx())
        val items = state.choices.take(12)
        // Without choices the wheel shows the two it starts with, as the kit's overview does.
        if (items.size < 2) {
            for (i in 0..1) drawArc(fills[i], -90f + i * 180f, 180f, true, center - Offset(radius,radius), Size(radius*2,radius*2))
        }
        if (items.size >= 2) rotate(rotation.value) {
            val sweep = 360f / items.size
            items.forEachIndexed { index, text ->
                drawArc(fills[index % 3], -90f + index * sweep, sweep, true, center - Offset(radius,radius), Size(radius*2,radius*2))
                if (!compact) {
                    val angle = Math.toRadians((-90f + (index + .5f) * sweep).toDouble())
                    val label = measurer.measure(text.take(12), textStyle.copy(color = inks[index % 3]))
                    val p = center + Offset(cos(angle).toFloat(),sin(angle).toFloat()) * radius * .6f
                    drawText(label, topLeft = p - Offset(label.size.width / 2f,label.size.height / 2f))
                }
            }
        }
        drawCircle(colors.surface, 16.dp.toPx())
        drawCircle(colors.primary, 5.dp.toPx())
        drawPath(Path().apply { moveTo(center.x - 9.dp.toPx(),0f); lineTo(center.x + 9.dp.toPx(),0f); lineTo(center.x,18.dp.toPx()); close() }, colors.primary)
    }
}
