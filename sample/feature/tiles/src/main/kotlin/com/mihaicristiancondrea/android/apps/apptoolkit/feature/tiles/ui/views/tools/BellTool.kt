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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.DrawScope
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
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun BellTool(state: BellToolUiState, onRing: () -> Unit) {
    val ringingDescription = stringResource(id = R.string.bell_ringing)
    val motionScale = LocalShellMotion.current.durationScale
    var angle by remember { mutableFloatStateOf(value = 0f) }
    var cookieScale by remember { mutableFloatStateOf(value = 1f) }
    var waveElapsed by remember { mutableFloatStateOf(value = -1f) }
    var waveDirection by remember { mutableIntStateOf(value = 1) }
    var staticWaves by remember { mutableStateOf(value = false) }
    var ringing by remember { mutableStateOf(value = false) }
    LaunchedEffect(state.strike, motionScale) {
        if (state.strike == 0) {
            angle = 0f
            cookieScale = 1f
            waveElapsed = -1f
            staticWaves = false
            ringing = false
            return@LaunchedEffect
        }
        ringing = true
        val reduceMotion = motionScale == 0f || coroutineContext[MotionDurationScale]?.scaleFactor == 0f
        if (reduceMotion) {
            angle = 0f
            cookieScale = 1f
            waveElapsed = -1f
            staticWaves = true
            delay(duration = (SAMPLE_DURATION_MS / state.playbackRate).toLong().milliseconds)
        } else {
            staticWaves = false
            val direction = if (state.strike % 2 == 0) -1 else 1
            waveDirection = direction
            val startAngle = angle
            val startCookieScale = cookieScale
            val startNanos = withFrameNanos { it }
            var elapsed: Float
            do {
                elapsed = withFrameNanos { ((it - startNanos) / 1_000_000f).coerceAtLeast(0f) } * state.playbackRate
                waveElapsed = elapsed
                angle = bellAngle(elapsed = elapsed, direction = direction, startAngle = startAngle)
                cookieScale = bellCookieScale(elapsed = elapsed, startScale = startCookieScale)
            } while (elapsed < SAMPLE_DURATION_MS)
        }
        angle = 0f
        cookieScale = 1f
        waveElapsed = -1f
        staticWaves = false
        ringing = false
    }

    val visual: @Composable () -> Unit = {
        BellFace(
            angle = { angle },
            cookieScale = { cookieScale },
            waveElapsed = { waveElapsed },
            waveDirection = { waveDirection },
            staticWaves = { staticWaves },
        )
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
private fun BellFace(
    angle: () -> Float,
    cookieScale: () -> Float,
    waveElapsed: () -> Float,
    waveDirection: () -> Int,
    staticWaves: () -> Boolean,
) {
    val colors = ToolkitTileIcon.Bell.iconColors()
    Box(
        modifier = Modifier.widthIn(max = 236.dp).fillMaxWidth().height(176.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .graphicsLayer {
                    val scale = cookieScale()
                    scaleX = scale
                    scaleY = scale
                }
                .background(
                    color = colors.container.copy(alpha = .52f).compositeOver(MaterialTheme.colorScheme.surfaceContainerLow),
                    shape = ToolkitTileIcon.Bell.backgroundShape(),
                ),
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (staticWaves()) {
                for (ring in BellWaveRings) {
                    drawWaveArcPair(
                        ring = ring,
                        scale = 1f,
                        strokeWidthDp = ring.staticStrokeDp,
                        color = colors.content,
                        leftAlpha = ring.staticAlpha,
                        rightAlpha = ring.staticAlpha,
                    )
                }
                return@Canvas
            }
            val elapsed = waveElapsed()
            if (elapsed < 0f) return@Canvas
            val direction = waveDirection()
            for (ring in BellWaveRings) {
                for (index in BellImpacts.indices) {
                    val ripple = bellWaveRipple(
                        elapsed = elapsed,
                        impactIndex = index,
                        outer = ring.outer,
                    )
                    if (ripple.alpha <= WAVE_VISIBILITY_EPSILON) continue
                    val leadsLeft = direction * BellImpacts[index].angle > 0f
                    drawWaveArcPair(
                        ring = ring,
                        scale = ripple.scale,
                        strokeWidthDp = ripple.strokeWidthDp,
                        color = colors.content,
                        leftAlpha = ripple.alpha * if (leadsLeft) 1f else TRAILING_SIDE_ALPHA,
                        rightAlpha = ripple.alpha * if (leadsLeft) TRAILING_SIDE_ALPHA else 1f,
                    )
                }
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

private fun DrawScope.drawWaveArcPair(
    ring: BellWaveRingSpec,
    scale: Float,
    strokeWidthDp: Float,
    color: Color,
    leftAlpha: Float,
    rightAlpha: Float,
) {
    val width = size.width * ring.widthFraction * scale
    val height = size.height * ring.heightFraction * scale
    val topLeft = Offset(x = (size.width - width) / 2f, y = (size.height - height) / 2f)
    val arcSize = Size(width = width, height = height)
    val stroke = Stroke(width = strokeWidthDp.dp.toPx(), cap = StrokeCap.Round)
    val halfSweep = ring.sweepAngle / 2f
    drawArc(
        color = color.copy(alpha = rightAlpha),
        startAngle = -halfSweep,
        sweepAngle = ring.sweepAngle,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = stroke,
    )
    drawArc(
        color = color.copy(alpha = leftAlpha),
        startAngle = 180f - halfSweep,
        sweepAngle = ring.sweepAngle,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = stroke,
    )
}

private const val SAMPLE_DURATION_MS = 1500f
private const val MAX_IMPACT_ANGLE = 23f
private const val TRAILING_SIDE_ALPHA = 0.78f
private const val WAVE_VISIBILITY_EPSILON = 0.001f
private const val COOKIE_ATTACK_MS = 26f
private const val COOKIE_PULSE_DURATION_MS = 135f
private const val COOKIE_PULSE_SCALE = 0.032f

private data class BellImpact(val timeMillis: Int, val angle: Float)

private data class BellWaveRingSpec(
    val outer: Boolean,
    val widthFraction: Float,
    val heightFraction: Float,
    val sweepAngle: Float,
    val staggerMs: Float,
    val attackMs: Float,
    val durationMs: Float,
    val finalDurationMs: Float,
    val peakAlpha: Float,
    val staticAlpha: Float,
    val startScale: Float,
    val endScale: Float,
    val startStrokeDp: Float,
    val staticStrokeDp: Float,
    val endStrokeDp: Float,
)

internal data class BellWaveRipple(
    val alpha: Float,
    val scale: Float,
    val strokeWidthDp: Float,
)

private val InnerWaveRing = BellWaveRingSpec(
    outer = false,
    widthFraction = 182f / 236f,
    heightFraction = 120f / 176f,
    sweepAngle = 68f,
    staggerMs = 0f,
    attackMs = 26f,
    durationMs = 190f,
    finalDurationMs = 210f,
    peakAlpha = 0.80f,
    staticAlpha = 0.75f,
    startScale = 0.91f,
    endScale = 1.07f,
    startStrokeDp = 3.4f,
    staticStrokeDp = 3.2f,
    endStrokeDp = 2.0f,
)

private val OuterWaveRing = BellWaveRingSpec(
    outer = true,
    widthFraction = 208f / 236f,
    heightFraction = 142f / 176f,
    sweepAngle = 80f,
    staggerMs = 48f,
    attackMs = 34f,
    durationMs = 220f,
    finalDurationMs = 294f,
    peakAlpha = 0.48f,
    staticAlpha = 0.45f,
    startScale = 0.93f,
    endScale = 1.09f,
    startStrokeDp = 2.6f,
    staticStrokeDp = 2.4f,
    endStrokeDp = 1.5f,
)

// Outer ring draws first so the brighter inner wavefront stays sharp in front.
private val BellWaveRings = listOf(OuterWaveRing, InnerWaveRing)

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

internal fun bellWaveRipple(
    elapsed: Float,
    impactIndex: Int,
    outer: Boolean,
): BellWaveRipple {
    val impact = BellImpacts[impactIndex]
    val ring = if (outer) OuterWaveRing else InnerWaveRing
    val localElapsed = elapsed - (impact.timeMillis + ring.staggerMs)
    val totalMs = if (impactIndex == BellImpacts.lastIndex) ring.finalDurationMs else ring.durationMs
    if (localElapsed <= 0f || localElapsed >= totalMs) {
        return BellWaveRipple(
            alpha = 0f,
            scale = ring.startScale,
            strokeWidthDp = ring.startStrokeDp,
        )
    }
    val progress = (localElapsed / totalMs).coerceIn(0f, 1f)
    val remaining = 1f - progress
    val easedExpand = 1f - remaining * remaining * remaining
    val scale = ring.startScale + (ring.endScale - ring.startScale) * easedExpand
    val strokeWidthDp = ring.startStrokeDp + (ring.endStrokeDp - ring.startStrokeDp) * progress
    val energy = (abs(impact.angle) / MAX_IMPACT_ANGLE).coerceIn(0f, 1f)
    val envelope = if (localElapsed < ring.attackMs) {
        val rise = localElapsed / ring.attackMs
        1f - (1f - rise) * (1f - rise)
    } else {
        val decay = (localElapsed - ring.attackMs) / (totalMs - ring.attackMs)
        (1f - decay) * (1f - decay)
    }
    return BellWaveRipple(
        alpha = ring.peakAlpha * energy * envelope,
        scale = scale,
        strokeWidthDp = strokeWidthDp,
    )
}

internal fun bellCookieScale(elapsed: Float, startScale: Float = 1f): Float {
    if (elapsed <= 0f) return startScale
    if (elapsed >= SAMPLE_DURATION_MS) return 1f
    var pulse = 0f
    for (impact in BellImpacts) {
        val localElapsed = elapsed - impact.timeMillis
        if (localElapsed <= 0f || localElapsed >= COOKIE_PULSE_DURATION_MS) continue
        val energy = (abs(impact.angle) / MAX_IMPACT_ANGLE).coerceIn(0f, 1f)
        val envelope = if (localElapsed < COOKIE_ATTACK_MS) {
            val rise = localElapsed / COOKIE_ATTACK_MS
            1f - (1f - rise) * (1f - rise)
        } else {
            val decay = (localElapsed - COOKIE_ATTACK_MS) / (COOKIE_PULSE_DURATION_MS - COOKIE_ATTACK_MS)
            (1f - decay) * (1f - decay)
        }
        val candidate = energy * envelope
        if (candidate > pulse) pulse = candidate
    }
    val target = 1f + pulse * COOKIE_PULSE_SCALE
    if (startScale == 1f || elapsed >= COOKIE_ATTACK_MS) return target
    val blend = (elapsed / COOKIE_ATTACK_MS).coerceIn(0f, 1f)
    val eased = blend * blend * (3f - 2f * blend)
    return startScale + (target - startScale) * eased
}
