package com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.runningFold

fun Modifier.hapticPagerSwipe(pagerState: PagerState): Modifier = composed {
    val haptics = rememberUpdatedState(LocalHapticFeedback.current)

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }
            .distinctUntilChanged()
            .runningFold(false) { vibrated, isScrolling ->
                if (!isScrolling) {
                    false
                } else {
                    if (!vibrated) {
                        haptics.value.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    }
                    true
                }
            }
            .collect { /* state already handled */ }
    }

    this
}