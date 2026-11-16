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

import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlin.math.min
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.first

/**
 * Decides how long each element waits before its entrance, so elements arrive in a cascade.
 *
 * Elements that start entering close together form a wave: everything within
 * [WAVE_WINDOW_MILLIS] of the wave's first element. Each waits by its place in the wave: its index
 * less the wave's first index while every element of the wave has one, its arrival order otherwise. The entrances of
 * the elements placed in one frame start within a few milliseconds of each other, so a screen's
 * first items cascade from the top of what is shown, and an item scrolled in alone starts at once.
 *
 * One cascade is shared by the whole app instead of each list owning one, so the modifier needs no
 * setup. It is only touched from the main thread.
 */
internal class VisibilityCascade(private val clock: () -> Long = SystemClock::uptimeMillis) {

    private var waveStartedAt: Long = NOT_STARTED
    private var arrivalsInWave: Int = 0
    private var firstIndexInWave: Int? = null
    private var waveAllIndexed: Boolean = true

    /**
     * How long an element that is about to enter waits: its place in the current wave, capped at
     * [maxStaggeredItems] positions.
     */
    fun delayMillisFor(index: Int?, staggerDelayMillis: Long, maxStaggeredItems: Int): Long {
        val now: Long = clock()
        if (waveStartedAt == NOT_STARTED || now - waveStartedAt > WAVE_WINDOW_MILLIS) {
            waveStartedAt = now
            arrivalsInWave = 0
            firstIndexInWave = index
            waveAllIndexed = true
        }
        if (index == null) waveAllIndexed = false
        val arrival = arrivalsInWave++
        val first = firstIndexInWave
        // A wave of list items keeps their positions; a wave that mixes them with other elements,
        // such as a row of filters above a grid, goes by the order they are placed in.
        val position: Int = if (waveAllIndexed && index != null && first != null) index - first else arrival
        return min(position.coerceAtLeast(0), maxStaggeredItems.coerceAtLeast(0)) * staggerDelayMillis
    }

    companion object {
        /** How long after a wave starts an element still joins it. About three frames. */
        const val WAVE_WINDOW_MILLIS: Long = 50L

        private const val NOT_STARTED: Long = -1L

        /** The app-wide cascade every [animateVisibility] joins. */
        val Shared: VisibilityCascade = VisibilityCascade()
    }
}
