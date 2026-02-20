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

package com.d4rk.android.libs.apptoolkit.core.ui.views.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d4rk.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass

/**
 * Adaptive root container used to keep content readable on large screens while preserving
 * edge-to-edge behavior on compact devices.
 *
 * The container computes a target content width based on [windowWidthSizeClass], applies bounded
 * horizontal gutters and size-class-aware vertical spacing, and centers the content.
 */
@Composable
fun RootContentContainer(
    modifier: Modifier = Modifier,
    windowWidthSizeClass: AppWindowWidthSizeClass,
    content: @Composable () -> Unit,
) {
    val interSectionSpacing: Dp = when (windowWidthSizeClass) {
        AppWindowWidthSizeClass.Compact -> 0.dp
        AppWindowWidthSizeClass.Medium -> 20.dp
        AppWindowWidthSizeClass.Expanded -> 24.dp
        AppWindowWidthSizeClass.Large,
        AppWindowWidthSizeClass.ExtraLarge -> 32.dp
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val targetContentWidth: Dp =
            (maxWidth * RootContentContainerDefaults.contentWidthFraction(windowWidthSizeClass))
                .coerceIn(
                    minimumValue = RootContentContainerDefaults.minContentWidth,
                    maximumValue = RootContentContainerDefaults.maxContentWidth,
                )
                .coerceAtMost(maxWidth)

        val horizontalPadding: Dp = when (windowWidthSizeClass) {
            AppWindowWidthSizeClass.Compact -> 0.dp
            else -> ((maxWidth - targetContentWidth) / 2)
                .coerceAtLeast(RootContentContainerDefaults.minHorizontalGutter)
                .coerceIn(
                    minimumValue = RootContentContainerDefaults.minHorizontalGutter,
                    maximumValue = RootContentContainerDefaults.maxHorizontalGutter,
                )
        }

        val verticalPadding: Dp = when (windowWidthSizeClass) {
            AppWindowWidthSizeClass.Compact -> 0.dp
            AppWindowWidthSizeClass.Medium -> 12.dp
            AppWindowWidthSizeClass.Expanded -> 16.dp
            AppWindowWidthSizeClass.Large,
            AppWindowWidthSizeClass.ExtraLarge -> 24.dp
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .widthIn(max = targetContentWidth)
                    .padding(bottom = interSectionSpacing),
            ) {
                content()
            }
        }
    }
}

private object RootContentContainerDefaults {
    val minContentWidth: Dp = 360.dp
    val maxContentWidth: Dp = 960.dp
    val minHorizontalGutter: Dp = 16.dp
    val maxHorizontalGutter: Dp = 240.dp

    fun contentWidthFraction(windowWidthSizeClass: AppWindowWidthSizeClass): Float =
        when (windowWidthSizeClass) {
            AppWindowWidthSizeClass.Compact -> 1.00f
            AppWindowWidthSizeClass.Medium -> 0.92f
            AppWindowWidthSizeClass.Expanded -> 0.80f
            AppWindowWidthSizeClass.Large -> 0.70f
            AppWindowWidthSizeClass.ExtraLarge -> 0.60f
        }
}
