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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.domain.models.WallpaperSwatchColors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

@Composable
fun WallpaperColorOptionCard(
    colors: WallpaperSwatchColors,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cardSize: Dp = SizeConstants.SeventyTwoSize,
    swatchSize: Dp = SizeConstants.FortyFourSize,
    shape: RoundedCornerShape = RoundedCornerShape(SizeConstants.LargeSize),
    showSeasonalBadge: Boolean = false,
) {
    val borderColor = animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "swatchBorderColor"
    ).value

    Surface(
        modifier = modifier
            .size(cardSize)
            .bounceClick()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.RadioButton,
                onClick = onClick
            ),
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(SizeConstants.ExtraTinySize, borderColor),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (showSeasonalBadge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(all = SizeConstants.ExtraTinySize)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape
                        )
                        .padding(all = SizeConstants.ExtraTinySize + SizeConstants.ExtraTinySize / 2)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AcUnit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(SizeConstants.LargeSize)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                MaterialYouCircleSwatch(
                    primary = colors.primary,
                    secondary = colors.secondary,
                    tertiary = colors.tertiary,
                    selected = selected,
                    modifier = Modifier.size(swatchSize)
                )
            }
        }
    }
}

/**
 * Local copy of the shared bounce-click effect.
 *
 * Duplicated here (rather than depending on `:library:core:ui`) because core:ui already
 * depends on core:designsystem for theming, and a reverse dependency would create a cycle.
 */
@Composable
private fun Modifier.bounceClick(): Modifier = composed {
    val pressed = remember { mutableStateOf(false) }

    val scale: Float by animateFloatAsState(
        targetValue = if (pressed.value) 0.96f else 1f,
        label = "Button Press Scale Animation"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                pressed.value = true
                waitForUpOrCancellation()
                pressed.value = false
            }
        }
}
