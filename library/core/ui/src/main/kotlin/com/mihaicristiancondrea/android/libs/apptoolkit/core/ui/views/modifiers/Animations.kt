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
import kotlin.math.min

/**
 * Fades and slides a composable into place the first time it appears, in a cascade.
 *
 * Every element animates in, including the ones scrolled into view later, so a screen built from
 * these stays in motion as it is explored.
 *
 * Give list items their [index] and the cascade follows the list: each item waits [staggerDelay]
 * per position, up to [maxStaggeredItems] positions, so the first screenful runs from the top and
 * items further down keep arriving, the deepest after about 1.3 seconds with the defaults. Leave
 * [index] out and elements cascade in the order they appear instead, so a `Column`, a group of
 * cards, or a list whose index is awkward to thread through still cascades without any setup:
 *
 * ```kotlin
 * LazyColumn {
 *     itemsIndexed(rows, key = { _, row -> row.id }) { index, row ->
 *         RowCard(row, modifier = Modifier.animateItem().animateVisibility(index = index))
 *     }
 * }
 *
 * Column {
 *     cards.forEach { card -> Card(modifier = Modifier.animateVisibility()) { Text(card.title) } }
 * }
 * ```
 *
 * Once revealed, an element stays revealed. Inside a lazy list that is saved with the item, so an
 * item scrolled away and back, or restored after a configuration change, is shown without playing
 * again. When animations are turned off system-wide the element simply appears. The motion runs in
 * the draw phase, so it neither recomposes nor re-lays out the element on each frame.
 *
 * @param index The element's position in its list. Each position adds [staggerDelay] before the
 * element starts. Without it, elements cascade in the order they appear.
 * @param invisibleOffsetY How far below its place, in pixels, the element starts.
 * @param animationDuration Duration of the fade and the slide, in milliseconds.
 * @param staggerDelay Delay added per position in the cascade, in milliseconds.
 * @param maxStaggeredItems Positions past this one wait no longer than it does.
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
 * Decides how long each element waits before its entrance, so elements arrive in a cascade.
 *
 * With an index the wait is simply its position. Without one there is no position to go by, so
 * the elements that start revealing close together are grouped into a wave, everything within
 * [WAVE_WINDOW_MILLIS] of the wave's first element, and each waits by its place in the wave. The
 * effects of the elements composed in one frame run within a few milliseconds of each other, so
 * elements shown together cascade together.
 *
 * One cascade is shared by the whole app instead of each list owning one, so the modifier needs no
 * setup. It is only touched from effects, which run on the main thread.
 */
internal class VisibilityCascade(private val clock: () -> Long = SystemClock::uptimeMillis) {

    private var waveStartedAt: Long = NOT_STARTED
    private var arrivalsInWave: Int = 0

    /**
     * How long an element that is about to be revealed waits: [index] positions when it has one,
     * otherwise its arrival order within the current wave, capped at [maxStaggeredItems].
     */
    fun delayMillisFor(index: Int?, staggerDelayMillis: Long, maxStaggeredItems: Int): Long {
        val position: Int = index ?: nextArrivalInWave()
        return min(position.coerceAtLeast(0), maxStaggeredItems.coerceAtLeast(0)) *
                staggerDelayMillis
    }

    private fun nextArrivalInWave(): Int {
        val now: Long = clock()
        if (waveStartedAt == NOT_STARTED || now - waveStartedAt > WAVE_WINDOW_MILLIS) {
            waveStartedAt = now
            arrivalsInWave = 0
        }
        return arrivalsInWave++
    }

    companion object {
        /** How long after a wave starts an element still joins it. About three frames. */
        const val WAVE_WINDOW_MILLIS: Long = 50L

        private const val NOT_STARTED: Long = -1L

        /** The app-wide cascade every [animateVisibility] joins. */
        val Shared: VisibilityCascade = VisibilityCascade()
    }
}
