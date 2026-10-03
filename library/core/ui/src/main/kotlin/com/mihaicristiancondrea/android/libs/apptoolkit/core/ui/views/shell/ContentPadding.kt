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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * The part of a screen the shell draws over: the system navigation bar, and on a tab the bottom
 * navigation bar, the banner and the docked mini player.
 *
 * Screens are laid out behind all of these, edge to edge, so a scrolling list passes under them
 * instead of stopping at a solid band. Add this padding to the list's content padding, through
 * [contentPadding], so its last item can still scroll clear; a screen that does not scroll pads
 * itself by it instead. It changes as the bars appear, hide and grow.
 */
val LocalContentPadding = compositionLocalOf { PaddingValues(0.dp) }

/**
 * [LocalContentPadding] plus [extra], for the content padding of a scrolling container.
 *
 * ```
 * LazyColumn(contentPadding = contentPadding(PaddingValues(bottom = 16.dp))) { ... }
 * ```
 */
@Composable
@ReadOnlyComposable
fun contentPadding(extra: PaddingValues = PaddingValues(0.dp)): PaddingValues =
    LocalContentPadding.current + extra

/** Two paddings added side by side, resolved against the layout direction when used. */
@Stable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = SumPaddingValues(this, other)

private class SumPaddingValues(private val first: PaddingValues, private val second: PaddingValues) : PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
        first.calculateLeftPadding(layoutDirection) + second.calculateLeftPadding(layoutDirection)

    override fun calculateTopPadding(): Dp = first.calculateTopPadding() + second.calculateTopPadding()

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
        first.calculateRightPadding(layoutDirection) + second.calculateRightPadding(layoutDirection)

    override fun calculateBottomPadding(): Dp = first.calculateBottomPadding() + second.calculateBottomPadding()

    override fun equals(other: Any?): Boolean =
        other is SumPaddingValues && other.first == first && other.second == second

    override fun hashCode(): Int = 31 * first.hashCode() + second.hashCode()
}

/**
 * Splits a scaffold's [padding] into what the frame applies itself, its top and sides, and the
 * bottom, which the content receives through [LocalContentPadding] so it can scroll behind it.
 */
fun PaddingValues.withoutBottom(): PaddingValues = object : PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection) = this@withoutBottom.calculateLeftPadding(layoutDirection)
    override fun calculateTopPadding() = this@withoutBottom.calculateTopPadding()
    override fun calculateRightPadding(layoutDirection: LayoutDirection) = this@withoutBottom.calculateRightPadding(layoutDirection)
    override fun calculateBottomPadding() = 0.dp
}
