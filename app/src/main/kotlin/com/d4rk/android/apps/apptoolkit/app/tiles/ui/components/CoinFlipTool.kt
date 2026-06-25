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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlin.random.Random

@Composable
fun CoinFlipTool() {
    var isHeads by remember { mutableStateOf(true) }
    var flipping by remember { mutableStateOf(false) }
    var rotationTarget by remember { mutableFloatStateOf(0f) }

    val rotation by animateFloatAsState(
        targetValue = rotationTarget,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "coin-rotation",
        finishedListener = { flipping = false }
    )

    val scale by animateFloatAsState(
        targetValue = if (flipping) 1.2f else 1f,
        animationSpec = tween(durationMillis = 500),
        label = "coin-scale"
    )

    val translationY by animateFloatAsState(
        targetValue = if (flipping) -40f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "coin-translation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Coin(
            rotationY = rotation,
            modifier = Modifier
                .graphicsLayer {
                    this.scaleX = scale
                    this.scaleY = scale
                    this.translationY = translationY
                }
        )

        Spacer(modifier = Modifier.height(SizeConstants.SmallSize))

        Button(
            onClick = {
                if (!flipping) {
                    flipping = true
                    isHeads = Random.nextBoolean()
                    rotationTarget += 1800f + (if (isHeads) 0f else 180f)
                }
            },
            enabled = !flipping
        ) {
            Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(SizeConstants.SmallSize))
            Text(text = stringResource(id = R.string.tool_coin_flip_action))
        }

        Text(
            text = if (flipping) {
                stringResource(id = R.string.tool_coin_flip_waiting)
            } else {
                stringResource(id = if (isHeads) R.string.tile_service_heads else R.string.tile_service_tails)
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun Coin(
    rotationY: Float,
    modifier: Modifier = Modifier
) {
    val normalizedRotation = (rotationY % 360f + 360f) % 360f
    val isBackVisible = normalizedRotation in 90f..270f

    Box(
        modifier = modifier
            .size(120.dp)
            .graphicsLayer {
                this.rotationY = rotationY
                cameraDistance = 12f * density
            },
        contentAlignment = Alignment.Center
    ) {
        if (isBackVisible) {
            Box(Modifier.graphicsLayer { this.rotationY = 180f }) {
                CoinFace(
                    iconRes = R.drawable.ic_coin_tails,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        } else {
            CoinFace(
                iconRes = R.drawable.ic_coin_heads,
                color = MaterialTheme.colorScheme.primaryContainer,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun CoinFace(
    @DrawableRes iconRes: Int,
    color: Color,
    tint: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, CircleShape)
            .border(4.dp, color.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = androidx.compose.ui.res.painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = tint
        )
    }
}
