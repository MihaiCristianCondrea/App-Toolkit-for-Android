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
import androidx.compose.ui.semantics.Role
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.CoinSide
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton


@Composable
fun CoinFlipTool(side: CoinSide, flipRequest: Int, onFlip: () -> Unit, compact: Boolean = false) {
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    var lastRequest by remember { mutableIntStateOf(flipRequest) }
    var flipping by remember { mutableStateOf(false) }
    var rotationTarget by remember { mutableFloatStateOf(if (side == CoinSide.Heads) 0f else 180f) }
    var rotationStart by remember { mutableFloatStateOf(rotationTarget) }

    LaunchedEffect(flipRequest) {
        if (flipRequest != lastRequest) {
            lastRequest = flipRequest
            flipping = true
            rotationStart = rotationTarget
            rotationTarget += 1800f + (((if (side == CoinSide.Heads) 0f else 180f) - rotationTarget % 360f + 360f) % 360f)
        }
    }

    val rotation by animateFloatAsState(
        targetValue = rotationTarget,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "coin-rotation",
        finishedListener = { flipping = false; if (flipRequest > 0) if (view.hasWindowFocus()) haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
    )

    // The kit's toss: the coin rises 24 dp and grows to 1.12 by 35% of the spin, then settles.
    val progress = if (rotationTarget == rotationStart) 1f else ((rotation - rotationStart) / (rotationTarget - rotationStart)).coerceIn(0f, 1f)
    val lift = if (progress < 0.35f) progress / 0.35f else (1f - progress) / 0.65f
    val density = LocalDensity.current
    val scale = 1f + 0.12f * lift
    val translationY = with(density) { -24.dp.toPx() } * lift

    Column(
        verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Coin(
            rotationY = rotation,
            diameter = if (compact) 88.dp else 120.dp,
            modifier = Modifier
                .clickable(
                    interactionSource = null,
                    indication = null,
                    enabled = !flipping,
                    onClickLabel = stringResource(R.string.tool_coin_flip_action),
                    role = Role.Button,
                    onClick = onFlip,
                )
                .graphicsLayer {
                    this.scaleX = scale
                    this.scaleY = scale
                    this.translationY = translationY
                }
        )

        if (!compact) Text(
            text = if (flipping) {
                stringResource(id = R.string.tool_coin_flip_waiting)
            } else {
                stringResource(
                    id = when (side) {
                        CoinSide.Heads -> R.string.tile_service_heads
                        CoinSide.Tails -> R.string.tile_service_tails
                    },
                )
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (!compact) Spacer(modifier = Modifier.height(SizeConstants.SmallSize))

        if (!compact) GeneralButton(
            onClick = {
                if (!flipping) {
                    onFlip()
                }
            },
            enabled = !flipping,
            measurements = ButtonMeasurements.Medium,
            icon = ToolkitIcon.Vector(imageVector = ToolSymbols.PlayArrow),
            label = stringResource(id = R.string.tool_coin_flip_action),
        )
    }
}

/**
 * The kit's coin (`.chance-coin` in marketing/ui/src/kit/kit.css): a 10 dp thick disc, heads on the
 * primary container and tails on the secondary container, each ringed with 4 dp of primary at 25%,
 * between them a rim of primary mixed 45% into the primary container.
 *
 * It is drawn as the kit's 3D transform projects it rather than flipped as a flat layer: each face is
 * the circle squeezed by the cosine of the turn and pushed sideways by half the thickness times the
 * sine, and the rim is the hull between the two, so the coin keeps its edge when it turns side-on.
 */
@Composable
internal fun Coin(
    rotationY: Float,
    diameter: Dp,
    modifier: Modifier = Modifier
) {
    val scheme = MaterialTheme.colorScheme
    val rimColor = lerp(scheme.primaryContainer, scheme.primary, 0.45f)
    val ringColor = scheme.primary.copy(alpha = 0.25f)
    val heads = CoinPaint(scheme.primaryContainer, scheme.onPrimaryContainer, painterResource(R.drawable.ic_coin_heads))
    val tails = CoinPaint(scheme.secondaryContainer, scheme.onSecondaryContainer, painterResource(R.drawable.ic_coin_tails))
    val iconSize = if (diameter < 120.dp) 48.dp else 64.dp

    Canvas(modifier.size(diameter)) {
        val radians = Math.toRadians(rotationY.toDouble())
        val cos = cos(radians).toFloat()
        val sin = sin(radians).toFloat()
        val radius = size.minDimension / 2f
        val halfThickness = CoinThickness.toPx() / 2f
        // The face nearer the viewer sits at +half the thickness along the coin's axis.
        val frontOffset = halfThickness * sin
        val squeeze = abs(cos)

        // The rim: both faces' outlines and the band between them.
        val left = center.x - abs(frontOffset)
        val right = center.x + abs(frontOffset)
        drawRect(rimColor, topLeft = Offset(left, center.y - radius), size = Size(right - left, radius * 2f))
        for (x in floatArrayOf(left, right)) {
            drawOval(rimColor, topLeft = Offset(x - radius * squeeze, center.y - radius), size = Size(radius * squeeze * 2f, radius * 2f))
        }

        // The face turned towards the viewer, squeezed as the turn projects it. Edge-on there is only rim.
        if (squeeze < 0.06f) return@Canvas
        val face = if (cos >= 0f) heads else tails
        val faceCenter = Offset(center.x + if (cos >= 0f) frontOffset else -frontOffset, center.y)
        withTransform({ scale(squeeze, 1f, pivot = faceCenter) }) {
            drawCircle(face.container, radius, faceCenter)
            val ring = 4.dp.toPx()
            drawCircle(ringColor, radius - ring / 2f, faceCenter, style = Stroke(ring))
            val icon = iconSize.toPx()
            translate(faceCenter.x - icon / 2f, faceCenter.y - icon / 2f) {
                with(face.icon) { draw(Size(icon, icon), colorFilter = ColorFilter.tint(face.content)) }
            }
        }
    }
}

private class CoinPaint(val container: Color, val content: Color, val icon: Painter)

private val CoinThickness = 10.dp

@Preview(showBackground = true)
@Composable
private fun CoinFlipToolPreview() {
    MaterialTheme {
        Surface {
            CoinFlipTool(
                side = CoinSide.Heads,
                flipRequest = 0,
                onFlip = {},
            )
        }
    }
}
