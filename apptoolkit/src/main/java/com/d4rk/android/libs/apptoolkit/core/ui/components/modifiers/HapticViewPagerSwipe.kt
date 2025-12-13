package com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

fun Modifier.hapticPagerSwipe(pagerState : PagerState) : Modifier = composed {
    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    var hasVibrated : Boolean by remember { mutableStateOf(value = false) }

    LaunchedEffect(key1 = pagerState.isScrollInProgress) {
        if (pagerState.isScrollInProgress && ! hasVibrated) {
            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.SegmentTick)
            hasVibrated = true // FIXME: Assigned value is never read
        }
        else if (! pagerState.isScrollInProgress) {
            hasVibrated = false // FIXME: Assigned value is never read
        }
    }

    return@composed this
}