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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

/**
 * One light tick each time a swipe the person makes brings the pager to another page, as a detent
 * under the finger.
 *
 * Only their own swipes tick, through to the fling that ends them. A slide the app starts, such as
 * a tab's tap moving the pager, gets none: the tap already gave its click, and the two together felt
 * doubled. A swipe that lets go and springs back to the same page gets none either.
 */
@Composable
fun Modifier.hapticPagerSwipe(pagerState: PagerState): Modifier {
    val haptics = rememberUpdatedState(LocalHapticFeedback.current)

    LaunchedEffect(pagerState) {
        var swipedByUser = false
        launch {
            pagerState.interactionSource.interactions.collect { interaction ->
                if (interaction is DragInteraction.Start) swipedByUser = true
            }
        }
        launch {
            snapshotFlow { pagerState.isScrollInProgress }
                .distinctUntilChanged()
                .collect { scrolling -> if (!scrolling) swipedByUser = false }
        }
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .drop(1)
            .collect {
                if (swipedByUser) haptics.value.performHapticFeedback(HapticFeedbackType.SegmentTick)
            }
    }

    return this
}
