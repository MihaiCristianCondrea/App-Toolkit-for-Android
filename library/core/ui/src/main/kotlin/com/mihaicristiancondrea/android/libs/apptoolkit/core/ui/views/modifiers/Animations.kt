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
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.isSystemAnimationDisabled
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.min

/**
 * Fades and slides a composable into place the first time it appears, cascading with everything
 * that appears alongside it.
 *
 * Every element animates in, including the ones scrolled into view later, so a screen built from
 * these stays in motion as it is explored. Elements that appear together form a wave and come in
 * one after another, [staggerDelay] apart. The first screenful is one wave, so it cascades from the
 * top; each row scrolled into view afterwards starts a small wave of its own. An element therefore
 * waits only for the elements that appeared with it, never for the whole list above it, and one
 * scrolled to at the bottom of a long list animates straight away.
 *
 * Nothing needs to be set up. It works the same in a `LazyColumn`, a lazy grid, a `Column`, or on
 * a single element:
 *
 * ```kotlin
 * LazyColumn {
 *     items(rows, key = { it.id }) { row ->
 *         RowCard(row, modifier = Modifier.animateItem().animateVisibility())
 *     }
 * }
 * ```
 *
 * Once revealed, an element stays revealed. Inside a lazy list that is saved with the item, so an
 * item scrolled away and back, or restored after a configuration change, is shown without playing
 * again. When animations are turned off system-wide the element simply appears. The motion runs in
 * the draw phase, so it neither recomposes nor re-lays out the element on each frame.
 *
 * @param index The element's position in its list, when it has one. It orders the cascade by
 * position rather than by the order elements are composed in, counted from the first element of
 * the wave, so a row scrolled into view deep in a list still starts at once. Leave it out and the
 * cascade follows the order elements appear, which for a list is the same thing.
 * @param invisibleOffsetY How far below its place, in pixels, the element starts.
 * @param animationDuration Duration of the fade and the slide, in milliseconds.
 * @param staggerDelay Delay between two consecutive elements of a wave, in milliseconds.
 * @param maxStaggeredItems Elements further into a wave than this wait no longer than it does.
 */
@Composable
fun Modifier.animateVisibility(
    index: Int? = null,
    invisibleOffsetY: Int = 50,
    animationDuration: Int = 300,
    staggerDelay: Int = 64,
    maxStaggeredItems: Int = 20,
): Modifier {
    var revealed: Boolean by rememberSaveable { mutableStateOf(value = false) }
    val progress = remember { Animatable(initialValue = if (revealed) 1f else 0f) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (revealed) return@LaunchedEffect
        if (context.isSystemAnimationDisabled()) {
            revealed = true
            progress.snapTo(targetValue = 1f)
            return@LaunchedEffect
        }

        delay(
            timeMillis = VisibilityCascade.Shared.delayMillisFor(
                index = index,
                staggerDelayMillis = staggerDelay.toLong(),
                maxStaggeredItems = maxStaggeredItems,
            ),
        )
        // Marked before the motion runs, so an element that leaves halfway through comes back
        // whole rather than playing again.
        revealed = true
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = animationDuration),
        )
    }

    return graphicsLayer {
        val fraction: Float = progress.value
        alpha = fraction
        translationY = (1f - fraction) * invisibleOffsetY
    }
}

/**
 * Groups the elements that appear together into waves and staggers each wave.
 *
 * A wave is every element that starts revealing within [WAVE_WINDOW_MILLIS] of the wave's first
 * one. The effects of the elements composed in one frame all run within a few milliseconds of each
 * other, so the first screenful of a list is one wave, and a list being scrolled starts a new wave
 * every few frames. Counting from the start of the wave, rather than from the top of the list, is
 * what keeps an element deep in a long list from waiting for every position above it.
 *
 * One cascade is shared by the whole app instead of each list owning one, so the modifier needs no
 * setup. It is only touched from effects, which run on the main thread.
 */
internal class VisibilityCascade(private val clock: () -> Long = SystemClock::uptimeMillis) {

    private var waveStartedAt: Long = NOT_STARTED
    private var arrivalsInWave: Int = 0
    private var firstIndexInWave: Int? = null

    /**
     * How long an element that is about to be revealed waits.
     *
     * With an [index] the position is the distance from the first indexed element of the wave, so
     * a wave cascades away from where it started in either scroll direction. Without one it is the
     * element's arrival order within the wave.
     */
    fun delayMillisFor(index: Int?, staggerDelayMillis: Long, maxStaggeredItems: Int): Long {
        val now: Long = clock()
        if (waveStartedAt == NOT_STARTED || now - waveStartedAt > WAVE_WINDOW_MILLIS) {
            waveStartedAt = now
            arrivalsInWave = 0
            firstIndexInWave = null
        }

        val arrival: Int = arrivalsInWave++
        val position: Int = if (index != null) {
            val firstIndex: Int = firstIndexInWave ?: index.also { firstIndexInWave = it }
            abs(index - firstIndex)
        } else {
            arrival
        }
        return min(position, maxStaggeredItems.coerceAtLeast(0)) * staggerDelayMillis
    }

    companion object {
        /** How long after a wave starts an element still joins it. About three frames. */
        const val WAVE_WINDOW_MILLIS: Long = 50L

        private const val NOT_STARTED: Long = -1L

        /** The app-wide cascade every [animateVisibility] joins. */
        val Shared: VisibilityCascade = VisibilityCascade()
    }
}
