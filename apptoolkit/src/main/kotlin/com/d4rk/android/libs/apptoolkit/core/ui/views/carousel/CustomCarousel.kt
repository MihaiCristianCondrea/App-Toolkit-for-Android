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

package com.d4rk.android.libs.apptoolkit.core.ui.views.carousel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.hapticPagerSwipe
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.absoluteValue

@Composable
fun <T> CustomCarousel(
    items: List<T>,
    sidePadding: Dp,
    pagerState: PagerState,
    itemContent: @Composable (item: T) -> Unit
) {
    val stableItems = remember(items) { items.toImmutableList() }
    val currentItems by rememberUpdatedState(newValue = stableItems)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .hapticPagerSwipe(pagerState = pagerState),
            contentPadding = PaddingValues(horizontal = sidePadding),
        ) { page ->
            val pageOffset = remember(pagerState.currentPage, page) {
                (pagerState.currentPage - page).absoluteValue.toFloat()
            }
            CarouselItem(
                item = currentItems[page],
                pageOffset = pageOffset,
                itemContent = itemContent
            )
        }

        LargeVerticalSpacer()

        DotsIndicator(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = SizeConstants.SmallSize),
            totalDots = currentItems.size,
            selectedIndex = pagerState.currentPage,
            dotSize = SizeConstants.MediumSize / 2,
        )
    }
}
