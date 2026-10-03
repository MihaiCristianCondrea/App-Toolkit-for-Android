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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
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

/**
 * The reading progress line of an article bar, to place along the bar's bottom edge over it, so
 * the bar keeps its height. It shows while the bar shows the article's title and fills from the
 * start edge, the right one in a right-to-left layout.
 *
 * Scrolling only redraws it. A screen reader finds it as a progress bar that moves in tenths, and
 * is not told of each change.
 */
@Composable
fun ArticleReadingProgress(host: ArticleTopBarHost, modifier: Modifier = Modifier) {
    val shown by remember(host) { derivedStateOf { host.isCompact && host.hasProgress } }
    val durationScale = LocalShellMotion.current.durationScale
    val alpha = animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(((if (shown) EnterMillis else ExitMillis) * durationScale).roundToInt()),
        label = "ReadingProgressAlpha",
    )
    val indicator = ProgressIndicatorDefaults.linearColor
    val track = ProgressIndicatorDefaults.linearTrackColor
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val semantics = if (shown) {
        val tenths by remember(host) { derivedStateOf { (host.progress() * 10).roundToInt() } }
        Modifier.semantics { progressBarRangeInfo = ProgressBarRangeInfo(tenths / 10f, 0f..1f, steps = 9) }
    } else {
        Modifier.clearAndSetSemantics {}
    }
    Box(
        modifier
            .fillMaxWidth()
            .height(ReadingProgressHeight)
            .then(semantics)
            .drawBehind {
                val shownAlpha = alpha.value
                if (shownAlpha <= 0f) return@drawBehind
                drawRect(track, alpha = shownAlpha)
                val read = size.width * host.progress()
                drawRect(
                    color = indicator,
                    topLeft = Offset(if (rtl) size.width - read else 0f, 0f),
                    size = Size(read, size.height),
                    alpha = shownAlpha,
                )
            },
    )
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

/** How thick the reading progress line is. */
val ReadingProgressHeight = 3.dp

private val BrandHeight = 24.dp
private val BrandSpacing = 8.dp
private const val MaxBrandRatio = 4f
private const val EnterMillis = 220
private const val ExitMillis = 120
