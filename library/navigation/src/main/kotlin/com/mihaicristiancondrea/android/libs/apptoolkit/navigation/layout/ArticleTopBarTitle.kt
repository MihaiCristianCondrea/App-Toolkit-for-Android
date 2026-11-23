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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.LocalShellMotion
import kotlin.math.roundToInt

/**
 * The title of an article bar: nothing until the article's header has scrolled away, then the
 * host's brand, if any, and its title on one line, rising and fading in at the shell's animation
 * speed. The bar it sits in keeps its size either way.
 */
@Composable
fun ArticleTopBarTitle(
    host: ArticleTopBarHost,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    val compact by remember(host) { derivedStateOf { host.isCompact } }
    val durationScale = LocalShellMotion.current.durationScale
    val enter = (EnterMillis * durationScale).roundToInt()
    val exit = (ExitMillis * durationScale).roundToInt()
    AnimatedVisibility(
        visible = compact,
        modifier = modifier,
        enter = fadeIn(tween(enter)) + slideInVertically(tween(enter)) { it / 3 },
        exit = fadeOut(tween(exit)) + slideOutVertically(tween(exit)) { it / 3 },
        label = "ArticleTitle",
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val brand = host.brand
            if (brand != null) {
                ArticleBrand(brand, host.brandContentDescription)
                Spacer(Modifier.width(BrandSpacing))
            }
            Text(
                text = host.title,
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** A brand at the title's height and its own width for that height, never tinted. */
@Composable
private fun ArticleBrand(painter: Painter, contentDescription: String?) {
    val intrinsic = painter.intrinsicSize
    val ratio = if (intrinsic.isSpecified && intrinsic.width > 0f && intrinsic.height > 0f) {
        (intrinsic.width / intrinsic.height).coerceAtMost(MaxBrandRatio)
    } else {
        1f
    }
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .height(BrandHeight)
            .width(BrandHeight * ratio),
    )
}

private val BrandHeight = 24.dp
private val BrandSpacing = 8.dp
private const val MaxBrandRatio = 4f
internal const val EnterMillis = 220
internal const val ExitMillis = 120
