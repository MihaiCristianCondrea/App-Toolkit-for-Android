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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons

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

/**
 * Renders a single [ToolkitIcon] exactly as described: a Compose [ImageVector],
 * a static drawable/vector resource, or one frame state of an Animated Vector Drawable.
 *
 * This composable is intentionally stateless. Deciding *which* icon a navigation item shows,
 * and whether an AVD currently rests on its first or last frame, is the job of
 * [AnimatedToolkitIcon].
 *
 * @param icon The [ToolkitIcon] to display.
 * @param contentDescription Optional description of the icon for accessibility.
 * @param modifier The [Modifier] to apply to the icon.
 * @param atEnd For [ToolkitIcon.AnimatedVector] only: `true` renders the last frame of the
 *   animation, `false` the first one. Changing this value animates between the two.
 * @param tint Tint color to apply to the icon, defaults to [LocalContentColor].
 */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun ToolkitIconContent(
    icon: ToolkitIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    atEnd: Boolean = false,
    tint: Color = LocalContentColor.current,
) {
    when (icon) {
        is ToolkitIcon.Lottie -> LottieToolkitIcon(
            icon = icon,
            contentDescription = contentDescription,
            modifier = modifier,
            atEnd = atEnd,
            tint = tint,
        )
        is ToolkitIcon.Vector -> {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )
        }

        is ToolkitIcon.Resource -> {
            Icon(
                painter = painterResource(id = icon.resId),
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )
        }

        is ToolkitIcon.AnimatedVector -> {
            val image = AnimatedImageVector.animatedVectorResource(id = icon.resId)
            val painter = rememberAnimatedVectorPainter(
                animatedImageVector = image,
                atEnd = atEnd,
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
