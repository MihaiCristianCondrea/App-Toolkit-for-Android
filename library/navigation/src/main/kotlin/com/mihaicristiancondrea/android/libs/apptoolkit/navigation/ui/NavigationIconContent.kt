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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ui

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationIcon

/**
 * Renders a [NavigationIcon] which can be a Compose [ImageVector],
 * a static drawable/vector resource ID, or an Animated Vector Drawable (AVD).
 *
 * @param icon The [NavigationIcon] to display.
 * @param contentDescription Optional description of the icon for accessibility.
 * @param modifier The [Modifier] to apply to the icon.
 * @param selected Whether the item is currently selected, used to drive AVD animations.
 * @param atEnd Whether the click animation end state is active.
 * @param tint Tint color to apply to the icon, defaults to [LocalContentColor].
 */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun NavigationIconContent(
    icon: NavigationIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    atEnd: Boolean = false,
    tint: Color = LocalContentColor.current,
) {
    when (icon) {
        is NavigationIcon.Vector -> {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )
        }

        is NavigationIcon.Resource -> {
            Icon(
                painter = painterResource(id = icon.resId),
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )
        }

        is NavigationIcon.AnimatedVector -> {
            val image = AnimatedImageVector.animatedVectorResource(id = icon.resId)
            val painter = rememberAnimatedVectorPainter(
                animatedImageVector = image,
                atEnd = atEnd || selected || icon.atEnd,
            )
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )
        }
    }
}
