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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.carousel

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.lerp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun <T> CarouselItem(
    item: T,
    pageOffset: Float,
    shapeSize: Dp = SizeConstants.MediumSize,
    itemContent: @Composable (item: T) -> Unit,
) {
    val stableItem by rememberUpdatedState(newValue = item)
    val scale = animateFloatAsState(
        targetValue = lerp(0.95f, 1f, 1f - pageOffset.coerceIn(0f, 1f)),
        animationSpec = tween(250),
        label = "Carousel Item Scale for Page $pageOffset"
    ).value

    val alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            },
        shape = RoundedCornerShape(size = shapeSize),
    ) {
        itemContent(stableItem)
    }
}
