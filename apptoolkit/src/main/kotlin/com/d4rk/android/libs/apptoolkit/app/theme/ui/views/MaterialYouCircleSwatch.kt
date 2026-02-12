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

package com.d4rk.android.libs.apptoolkit.app.theme.ui.views

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun MaterialYouCircleSwatch(
    primary: Color,
    secondary: Color,
    tertiary: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    indicatorFraction: Float = 0.58f,
) {
    Box(
        modifier = modifier.clip(CircleShape)
    ) {
        Column(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clipToBounds()
            ) {
                Spacer(
                    Modifier
                        .fillMaxSize()
                        .background(primary)
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(secondary)
                )
                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(tertiary)
                )
            }
        }

        val progress = animateFloatAsState(
            targetValue = if (selected) 1f else 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "swatchSelectionProgress"
        ).value

        val scale = 0.80f + (0.20f * progress)

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(indicatorFraction)
                .graphicsLayer {
                    alpha = progress
                    scaleX = scale
                    scaleY = scale
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                .padding(SizeConstants.ExtraSmallSize),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize(0.7f)
            )
        }
    }
}