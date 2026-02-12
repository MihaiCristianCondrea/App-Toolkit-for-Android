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

package com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers

import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.runningFold

/**
 * A [Modifier] that provides haptic feedback when a [SwipeToDismissBox] is swiped past a
 * certain threshold.
 *
 * This modifier observes the [swipeToDismissBoxState] and triggers a [HapticFeedbackType.GestureThresholdActivate]
 * haptic feedback event the first time the swipe gesture crosses the dismissal threshold.
 * It ensures that the feedback is only triggered once per gesture.
 *
 * @param swipeToDismissBoxState The [SwipeToDismissBoxState] to observe for swipe state changes.
 */
fun Modifier.hapticSwipeToDismissBox(
    swipeToDismissBoxState: SwipeToDismissBoxState
): Modifier = composed {
    val haptics = rememberUpdatedState(LocalHapticFeedback.current)

    LaunchedEffect(swipeToDismissBoxState) {
        snapshotFlow { swipeToDismissBoxState.currentValue }
            .distinctUntilChanged()
            .runningFold(false) { alreadyVibrated, value ->
                if (value == SwipeToDismissBoxValue.Settled) {
                    false
                } else {
                    if (!alreadyVibrated) {
                        haptics.value.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                    }
                    true
                }
            }
            .collect { /* handled by fold */ }
    }

    this
}