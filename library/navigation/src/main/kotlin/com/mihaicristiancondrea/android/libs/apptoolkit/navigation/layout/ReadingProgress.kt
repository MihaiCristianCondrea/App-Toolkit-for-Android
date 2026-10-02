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

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState

/**
 * How far this scroll has gone through its content, from 0 at the top to 1 at the end, for
 * [ScaffoldArticleTopBar]. Content that fits without scrolling reads as read.
 */
fun ScrollState.readingProgress(): Float = when (maxValue) {
    0 -> 1f
    Int.MAX_VALUE -> 0f
    else -> value.toFloat() / maxValue
}

/**
 * How far this list has scrolled through its items, from 0 at the top to 1 at the end, for
 * [ScaffoldArticleTopBar]. It counts items, so it is exact for rows of one height and an estimate
 * otherwise; it reads 1 once the list cannot scroll further.
 */
fun LazyListState.readingProgress(): Float {
    val info = layoutInfo
    val first = info.visibleItemsInfo.firstOrNull() ?: return 0f
    if (!canScrollForward) return 1f
    val within = if (first.size > 0) firstVisibleItemScrollOffset.toFloat() / first.size else 0f
    val scrollable = (info.totalItemsCount - info.visibleItemsInfo.size + 1).coerceAtLeast(1)
    return ((firstVisibleItemIndex + within) / scrollable).coerceIn(0f, 1f)
}
