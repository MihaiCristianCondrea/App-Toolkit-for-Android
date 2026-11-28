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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * Returns a tap callback whose cumulative count survives screen recreation.
 *
 * @param threshold Positive number of taps between calls to [onThresholdReached].
 * @param onTap Receives the cumulative count on each tap.
 * @param onThresholdReached Runs after [onTap] on each multiple of [threshold].
 */
@Composable
fun rememberTapThreshold(
    threshold: Int,
    onTap: (Int) -> Unit,
    onThresholdReached: () -> Unit,
): () -> Unit {
    require(threshold > 0)
    var total by rememberSaveable(threshold) { mutableIntStateOf(value = 0) }

    return {
        total += 1
        onTap(total)
        if (total % threshold == 0) {
            onThresholdReached()
        }
    }
}
