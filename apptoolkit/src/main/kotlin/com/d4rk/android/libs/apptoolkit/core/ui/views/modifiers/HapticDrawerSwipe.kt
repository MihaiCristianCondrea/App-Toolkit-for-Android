package com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * A modifier that adds haptic feedback when a drawer is swiped.
 *
 * This modifier observes the [DrawerState] to detect when a swipe gesture initiates the
 * opening or closing of a drawer. It triggers a haptic feedback effect at the beginning
 * of the drawer's animation to enhance the user experience. The feedback is provided only
 * once per swipe gesture.
 *
 * @param state The [DrawerState] of the drawer to monitor for swipe events.
 * @return A [Modifier] that applies the haptic feedback behavior.
 *
 * @sample com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.HapticDrawerSwipeSample
 */
fun Modifier.hapticDrawerSwipe(state: DrawerState): Modifier = composed {
    val haptics = rememberUpdatedState(LocalHapticFeedback.current)

    LaunchedEffect(state) {
        snapshotFlow { state.isAnimationRunning }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                haptics.value.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
            }
    }

    this
}