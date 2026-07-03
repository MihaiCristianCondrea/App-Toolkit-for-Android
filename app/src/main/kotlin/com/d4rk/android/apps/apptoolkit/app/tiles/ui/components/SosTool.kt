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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun SosTool(
    isActive: Boolean,
    onToggle: () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.5f,
        animationSpec = if (isActive) {
            infiniteRepeatable(
                animation = tween(durationMillis = 500),
                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
            )
        } else {
            tween(durationMillis = 500)
        },
        label = "sos-alpha"
    )

    val color by animateColorAsState(
        targetValue = if (isActive) Color.Red else MaterialTheme.colorScheme.primary,
        label = "sos-color"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        ResultPill(
            label = if (isActive) "SOS ACTIVE" else "SOS Ready",
            modifier = Modifier.alpha(alpha)
        )

        Box(
            modifier = Modifier.padding(vertical = SizeConstants.LargeSize),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.WarningAmber,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = color
            )
        }

        Spacer(modifier = Modifier.height(SizeConstants.SmallSize))

        Button(
            onClick = onToggle,
            colors = if (isActive) {
                ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
            } else {
                ButtonDefaults.buttonColors()
            }
        ) {
            Text(text = if (isActive) "STOP SOS" else "START SOS")
        }

        Text(
            text = "Flashes flashlight in Morse code (SOS)",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = SizeConstants.LargeSize)
        )
    }
}
