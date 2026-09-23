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
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.isSystemAnimationDisabled
import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * How content moves when [animateEntrance] reveals it.
 *
 * @property offsetY how far below its final position the content starts. It is density
 * independent, so the motion looks the same on every screen.
 * @property duration how long the fade and the slide take.
 * @property easing the curve both follow.
 */
@Immutable
data class EntranceSpec(
    val offsetY: Dp = 16.dp,
    val duration: Duration = 300.milliseconds,
    val easing: Easing = FastOutSlowInEasing,
) {
    companion object {
        /** The toolkit's standard entrance: a short fade and a 16dp rise over 300ms. */
        val Default: EntranceSpec = EntranceSpec()
    }
}

/**
 * Staggers the entrance of the items of one list, grid, or column.
 *
 * Items are ordered by when they first appear, not by their position in the data, so callers do not
 * pass an index. Only the list's first reveal is staggered: the items composed together when it
 * first shows come in one after another, a stagger delay apart, and anything that appears after
 * that, usually by being scrolled into view, comes in at once. An index-based stagger would make an
 * item far down a list wait for every item above it, leaving freshly scrolled cells blank for over
 * a second.
 *
 * Create one per list with [rememberEntranceStagger], outside the lazy layout, and pass it to each
 * item's [animateEntrance].
 */
@Stable
class EntranceStagger internal constructor(
    private val staggerDelayMillis: Long,
    private val maxStaggeredItems: Int,
    private val clock: () -> Long = SystemClock::uptimeMillis,
) {
    private var firstRevealAt: Long = NOT_STARTED
    private var revealedInFirstBurst: Int = 0

    /**
     * How long the next item to appear waits before it starts.
     *
     * Items composed in the list's first frame run their effects within a frame or two of each
     * other, so everything claimed within [FIRST_BURST_WINDOW_MILLIS] of the first claim belongs to
     * the first reveal. Later claims do not wait.
     */
    internal fun delayForNextItemMillis(): Long {
        val now: Long = clock()
        if (firstRevealAt == NOT_STARTED) firstRevealAt = now
        if (now - firstRevealAt > FIRST_BURST_WINDOW_MILLIS) return 0L

        val position: Int = revealedInFirstBurst++
        return min(position, maxStaggeredItems) * staggerDelayMillis
    }

    companion object {
        /** The delay between two items of the first reveal. */
        val DefaultStaggerDelay: Duration = 64.milliseconds

        /** Items past this position in the first reveal all wait as long as this one does. */
        const val DEFAULT_MAX_STAGGERED_ITEMS: Int = 20

        private const val FIRST_BURST_WINDOW_MILLIS: Long = 150L
        private const val NOT_STARTED: Long = -1L
    }
}

/**
 * Remembers an [EntranceStagger] for one list.
 *
 * Pass [keys] to start over: when any of them changes, the next items to appear are staggered as a
 * fresh first reveal. A filter that swaps a list's contents is the usual key.
 *
 * @param staggerDelay the delay between two items of the first reveal.
 * @param maxStaggeredItems items past this position in the first reveal wait no longer than it.
 */
@Composable
fun rememberEntranceStagger(
    vararg keys: Any?,
    staggerDelay: Duration = EntranceStagger.DefaultStaggerDelay,
    maxStaggeredItems: Int = EntranceStagger.DEFAULT_MAX_STAGGERED_ITEMS,
): EntranceStagger = remember(*keys, staggerDelay, maxStaggeredItems) {
    EntranceStagger(
        staggerDelayMillis = staggerDelay.inWholeMilliseconds,
        maxStaggeredItems = maxStaggeredItems,
    )
}

/**
 * Fades and slides content into place the first time it appears.
 *
 * Content that has been revealed stays revealed. Inside a lazy list the flag is saved with the
 * item, so an item scrolled away and back, or restored after a configuration change, is shown
 * without playing again. When the person has turned animations off system-wide the content simply
 * appears.
 *
 * ```kotlin
 * val entrance = rememberEntranceStagger(selectedFilter)
 * LazyColumn {
 *     items(rows, key = { it.id }) { row ->
 *         RowCard(row, modifier = Modifier.animateItem().animateEntrance(stagger = entrance))
 *     }
 * }
 * ```
 *
 * The motion is applied in the draw phase, so it neither recomposes nor re-lays out the content on
 * each frame.
 *
 * @param stagger coordinates this item with its siblings. Without one the content comes in at once.
 * @param spec the motion to play.
 */
@Composable
fun Modifier.animateEntrance(
    stagger: EntranceStagger? = null,
    spec: EntranceSpec = EntranceSpec.Default,
): Modifier = entranceAnimation(spec = spec) {
    stagger?.delayForNextItemMillis() ?: 0L
}

/**
 * The shared implementation behind [animateEntrance] and the deprecated [animateVisibility].
 *
 * [startDelayMillis] is asked once, when the content is about to be revealed for the first time.
 */
@Composable
internal fun Modifier.entranceAnimation(
    spec: EntranceSpec,
    startDelayMillis: () -> Long,
): Modifier {
    var revealed: Boolean by rememberSaveable { mutableStateOf(value = false) }
    val progress = remember { Animatable(initialValue = if (revealed) 1f else 0f) }
    val context = LocalContext.current
    val offsetPx: Float = with(LocalDensity.current) { spec.offsetY.toPx() }

    LaunchedEffect(Unit) {
        if (revealed) return@LaunchedEffect
        if (context.isSystemAnimationDisabled()) {
            revealed = true
            progress.snapTo(targetValue = 1f)
            return@LaunchedEffect
        }

        delay(timeMillis = startDelayMillis())
        // Marked before the motion runs, so content that leaves halfway through comes back whole
        // rather than playing again.
        revealed = true
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = spec.duration.inWholeMilliseconds.toInt(),
                easing = spec.easing,
            ),
        )
    }

    return graphicsLayer {
        val fraction: Float = progress.value
        alpha = fraction
        translationY = (1f - fraction) * offsetPx
    }
}
