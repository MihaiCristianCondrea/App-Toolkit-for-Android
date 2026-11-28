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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.views.effects

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.isSystemAnimationDisabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.effects.rememberTapThreshold
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.effects.CelebrationConfetti
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Saves a tap count and displays confetti over [content] at each threshold.
 *
 * @param threshold Positive number of taps between celebrations.
 * @param onTap Receives the cumulative count after each tap.
 * @param onThresholdReached Runs at each threshold, including when system animations are disabled.
 * @param modifier Modifier for the container holding the content and overlay.
 * @param overlayDuration How long the confetti overlay stays visible after the latest threshold.
 * @param content Receives the tap callback to attach to the target.
 */
@Composable
internal fun TapCelebration(
    threshold: Int,
    onTap: (Int) -> Unit,
    onThresholdReached: () -> Unit,
    modifier: Modifier = Modifier,
    overlayDuration: Duration = 3.seconds,
    content: @Composable (onTap: () -> Unit) -> Unit,
) {
    val context = LocalContext.current
    var burstId by remember { mutableIntStateOf(value = 0) }

    val countTap = rememberTapThreshold(
        threshold = threshold,
        onTap = onTap,
        onThresholdReached = {
            if (!context.isSystemAnimationDisabled()) {
                burstId += 1
            }
            onThresholdReached()
        },
    )

    LaunchedEffect(key1 = burstId, key2 = overlayDuration) {
        if (burstId > 0) {
            delay(duration = overlayDuration)
            burstId = 0
        }
    }

    Box(modifier = modifier) {
        content(countTap)

        if (burstId > 0) {
            key(burstId) {
                CelebrationConfetti(modifier = Modifier.matchParentSize())
            }
        }
    }
}
