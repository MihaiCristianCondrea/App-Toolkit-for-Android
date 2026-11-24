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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.backgroundShape
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.iconColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.BellToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.LocalShellMotion
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun BellTool(state: BellToolUiState, onRing: () -> Unit) {
    val ringingDescription = stringResource(id = R.string.bell_ringing)
    val motionScale = LocalShellMotion.current.durationScale
    var angle by remember { mutableFloatStateOf(value = 0f) }
    var wave by remember { mutableFloatStateOf(value = 0f) }
    var ringing by remember { mutableStateOf(value = false) }
    LaunchedEffect(state.strike, motionScale) {
        if (state.strike == 0) {
            angle = 0f
            wave = 0f
            ringing = false
            return@LaunchedEffect
        }
        ringing = true
        val reduceMotion = motionScale == 0f || coroutineContext[MotionDurationScale]?.scaleFactor == 0f
        if (reduceMotion) {
            angle = 0f
            wave = 1f
            delay(duration = (SAMPLE_DURATION_MS / state.playbackRate).toLong().milliseconds)
        } else {
            val direction = if (state.strike % 2 == 0) -1 else 1
            val startAngle = angle
            val startNanos = withFrameNanos { it }
            var elapsed: Float
            do {
                elapsed = withFrameNanos { ((it - startNanos) / 1_000_000f).coerceAtLeast(0f) } * state.playbackRate
                angle = bellAngle(elapsed = elapsed, direction = direction, startAngle = startAngle)
                val previousImpact = BellImpacts.lastOrNull { it.timeMillis <= elapsed }
                wave = previousImpact?.let { (1f - (elapsed - it.timeMillis) / 110f).coerceIn(0f, 1f) } ?: 0f
            } while (elapsed < SAMPLE_DURATION_MS)
        }
        angle = 0f
        wave = 0f
        ringing = false
    }

    val visual: @Composable () -> Unit = {
        BellFace(angle = { angle }, wave = { wave })
    }
    val action: @Composable (Modifier) -> Unit = { modifier ->
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            GeneralButton(
                onClick = onRing,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.audioReady,
                measurements = ButtonMeasurements.Medium,
                label = stringResource(id = R.string.bell_ring),
            )
            Box(
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = if (!state.audioFailed && ringing) ringingDescription else ""
                },
            )
            if (state.audioFailed) {
                Text(
                    text = stringResource(id = R.string.bell_audio_unavailable),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                )
            }
        }
    }
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        if (maxWidth >= 420.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { visual() }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { action(Modifier.widthIn(max = 240.dp)) }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                visual()
                action(Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun BellFace(angle: () -> Float, wave: () -> Float) {
    val colors = ToolkitTileIcon.Bell.iconColors()
    Box(
        modifier = Modifier.widthIn(max = 236.dp).fillMaxWidth().height(176.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.size(160.dp).background(
                color = colors.container.copy(alpha = .52f).compositeOver(MaterialTheme.colorScheme.surfaceContainerLow),
                shape = ToolkitTileIcon.Bell.backgroundShape(),
            ),
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val pulse = wave()
            for (ring in 0..1) {
                val width = size.width * (if (ring == 0) .84f else .97f)
                val height = size.height * (if (ring == 0) .74f else .90f)
                val topLeft = Offset((size.width - width) / 2, (size.height - height) / 2)
                val color = colors.content.copy(alpha = pulse * if (ring == 0) .75f else .45f)
                drawArc(
                    color = color,
                    startAngle = -39f,
                    sweepAngle = 78f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(width = width, height = height),
                    style = Stroke(width = 3.dp.toPx()),
                )
                drawArc(
                    color = color,
                    startAngle = 141f,
                    sweepAngle = 78f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(width = width, height = height),
                    style = Stroke(width = 3.dp.toPx()),
                )
            }
        }
        Icon(
            imageVector = ToolSymbols.NotificationsFilled,
            contentDescription = null,
            modifier = Modifier.size(90.dp).graphicsLayer {
                rotationZ = angle()
                transformOrigin = TransformOrigin(.5f, .18f)
            },
            tint = colors.content,
        )
    }
}

private const val SAMPLE_DURATION_MS = 1500f

private data class BellImpact(val timeMillis: Int, val angle: Float)

// The dominant impacts in bell_strike.mp3, measured from the start of the edited recording.
private val BellImpacts = listOf(
    BellImpact(6, 20f), BellImpact(100, -23f), BellImpact(240, 21f),
    BellImpact(380, -17f), BellImpact(540, 15f), BellImpact(840, -12f),
)

private val BellSwing = listOf(BellImpact(0, 0f)) +
    BellImpacts.map { it.copy(timeMillis = it.timeMillis + 28) } +
    listOf(BellImpact(1000, 5f), BellImpact(1170, -2f), BellImpact(1500, 0f))

internal fun bellAngle(elapsed: Float, direction: Int = 1, startAngle: Float = 0f): Float {
    val next = BellSwing.indexOfFirst { it.timeMillis > elapsed }
    if (next < 1) return 0f
    val before = BellSwing[next - 1]
    val after = BellSwing[next]
    val fromAngle = if (next == 1) startAngle else direction * before.angle
    val toAngle = direction * after.angle
    val position = (elapsed - before.timeMillis) / (after.timeMillis - before.timeMillis)
    val eased = position * position * (3f - 2f * position)
    return fromAngle + (toAngle - fromAngle) * eased
}
