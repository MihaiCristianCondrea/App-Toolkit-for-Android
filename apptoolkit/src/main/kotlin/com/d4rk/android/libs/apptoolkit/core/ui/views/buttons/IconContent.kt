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

package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

private const val IconRequirementMessage: String = "Either icon or painter must be provided"

@Composable
internal fun IconContent(
    icon: ImageVector?,
    painter: Painter?,
    contentDescription: String?,
) {
    require(icon != null || painter != null) { IconRequirementMessage }

    val iconModifier: Modifier = remember { Modifier.size(size = SizeConstants.ButtonIconSize) }
    val stableIcon by rememberUpdatedState(newValue = icon)
    val stablePainter by rememberUpdatedState(newValue = painter)
    when {
        stableIcon != null -> Icon(
            modifier = iconModifier,
            imageVector = stableIcon!!,
            contentDescription = contentDescription
        )

        stablePainter != null -> Icon(
            modifier = iconModifier,
            painter = stablePainter!!,
            contentDescription = contentDescription
        )
    }
}
