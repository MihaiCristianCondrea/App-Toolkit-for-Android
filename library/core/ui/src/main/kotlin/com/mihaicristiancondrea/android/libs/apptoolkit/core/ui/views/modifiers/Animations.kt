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
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.isSystemAnimationDisabled
import kotlin.math.min
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/** How an element enters with [animateVisibility]. */
enum class EntranceStyle {
    /** It fades in while rising into place. */
    Slide,

    /** It fades in while growing into place from slightly smaller. */
    Scale,

    /** It only fades in. */
    Fade,

    /** It appears at once. */
    None,
}

/**
 * The [EntranceStyle] every [animateVisibility] here uses unless it names its own. The shell
 * provides the app's choice, `appToolkitDefaults { shell { entranceStyle = ... } }`, or the one
 * picked in the developer options.
 */
val LocalEntranceStyle = staticCompositionLocalOf { EntranceStyle.Slide }

/**
 * Brings a composable into place the first time it is shown, in a cascade.
 *
 * Elements shown together arrive one after another, in their order on screen: give list items their
 * [index] and each waits [staggerDelay] per position after the first item of its wave, up to
 * [maxStaggeredItems] positions; leave it out and elements cascade in the order they appear. The
 * cascade counts from what is shown, not from the top of the list, so an item scrolled into view
 * later starts at once instead of waiting behind the items above it.
 *
 * ```kotlin
 * LazyColumn {
 *     itemsIndexed(rows, key = { _, row -> row.id }) { index, row ->
 *         RowCard(row, modifier = Modifier.animateItem().animateVisibility(index = index))
 *     }
 * }
 * ```
 *
 * The entrance starts when the element is first placed, not when it is composed, so an item a lazy
 * list composes ahead of time still enters as it scrolls into view. Once it has started, the element
 * stays revealed, saved with a lazy item, so scrolling away and back or a configuration change
 * does not play it again. The motion follows `MaterialTheme.motionScheme`, springs that pick up from
 * where they are if interrupted, and runs in the draw phase without recomposing. When animations
 * are turned off system-wide, or [style] is [EntranceStyle.None], the element simply appears.
 *
 * @param index The element's position in its list, which orders it within the cascade.
 * @param style How it enters; by default the app's [LocalEntranceStyle].
 * @param staggerDelay Delay added per position in the cascade, in milliseconds.
 * @param maxStaggeredItems Positions past this one wait no longer than it does.
 */
@Composable
fun Modifier.animateVisibility(
    index: Int? = null,
    style: EntranceStyle = LocalEntranceStyle.current,
    staggerDelay: Int = 64,
    maxStaggeredItems: Int = 20,
): Modifier {
    var revealed: Boolean by rememberSaveable { mutableStateOf(value = false) }
    val context = LocalContext.current
    val animates: Boolean = remember {
        !revealed && style != EntranceStyle.None && !context.isSystemAnimationDisabled()
    }
    if (!animates) return this
    val motion = MaterialTheme.motionScheme
    val onStarted: () -> Unit = remember { { revealed = true } }
    return this then EntranceElement(
        visible = true,
        startHidden = true,
        cascade = CascadePosition(index, staggerDelay.toLong(), maxStaggeredItems),
        style = style,
        spatialSpec = motion.defaultSpatialSpec(),
        effectsSpec = motion.defaultEffectsSpec(),
        onStarted = onStarted,
    )
}

/**
 * Shows the composable while [visible] and hides it otherwise, animating both ways with [style]'s
 * motion: an element that comes and goes, such as a filter's result or a hint. It keeps its place
 * in the layout while hidden, only drawn transparent; remove it from the layout with
 * `AnimatedVisibility` instead.
 */
@Composable
fun Modifier.animateVisibility(
    visible: Boolean,
    style: EntranceStyle = LocalEntranceStyle.current,
): Modifier {
    val context = LocalContext.current
    val still: Boolean = style == EntranceStyle.None || remember { context.isSystemAnimationDisabled() }
    val motion = MaterialTheme.motionScheme
    return this then EntranceElement(
        visible = visible,
        startHidden = !visible,
        cascade = null,
        style = if (still) EntranceStyle.Fade else style,
        spatialSpec = if (still) snap() else motion.defaultSpatialSpec(),
        effectsSpec = if (still) snap() else motion.defaultEffectsSpec(),
        onStarted = NoOp,
    )
}

private val NoOp: () -> Unit = {}

/** How far below its place a [EntranceStyle.Slide] element starts. */
private val SlideDistance = 20.dp

/** The size a [EntranceStyle.Scale] element starts at. */
private const val InitialScale = 0.92f

/** Where an entering element waits in the cascade; null enters as soon as it is placed. */
private data class CascadePosition(val index: Int?, val staggerDelayMillis: Long, val maxStaggeredItems: Int)

private data class EntranceElement(
    val visible: Boolean,
    val startHidden: Boolean,
    val cascade: CascadePosition?,
    val style: EntranceStyle,
    val spatialSpec: FiniteAnimationSpec<Float>,
    val effectsSpec: FiniteAnimationSpec<Float>,
    val onStarted: () -> Unit,
) : ModifierNodeElement<EntranceNode>() {
    override fun create() = EntranceNode(visible, startHidden, cascade, style, spatialSpec, effectsSpec, onStarted)

    override fun update(node: EntranceNode) = node.update(visible, style, spatialSpec, effectsSpec)
}

/**
 * Animates [effects] (the fade) and [spatial] (the slide or the scale) from 0, hidden, to 1, in
 * place. A cascaded element starts on its first placement outside lookahead; one that comes and
 * goes animates each time [visible] changes.
 */
private class EntranceNode(
    private var visible: Boolean,
    startHidden: Boolean,
    private val cascade: CascadePosition?,
    private var style: EntranceStyle,
    private var spatialSpec: FiniteAnimationSpec<Float>,
    private var effectsSpec: FiniteAnimationSpec<Float>,
    private val onStarted: () -> Unit,
) : Modifier.Node(), LayoutModifierNode {

    private val effects = Animatable(if (startHidden) 0f else 1f)
    private val spatial = Animatable(if (startHidden) 0f else 1f)

    /** Whether the cascaded entrance is still to start, at the first placement. */
    private var awaitingPlacement: Boolean = startHidden && cascade != null

    fun update(
        visible: Boolean,
        style: EntranceStyle,
        spatialSpec: FiniteAnimationSpec<Float>,
        effectsSpec: FiniteAnimationSpec<Float>,
    ) {
        this.style = style
        this.spatialSpec = spatialSpec
        this.effectsSpec = effectsSpec
        if (visible != this.visible) {
            this.visible = visible
            if (isAttached) animateTo(if (visible) 1f else 0f)
        }
    }

    override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
        val placeable = measurable.measure(constraints)
        val lookingAhead = isLookingAhead
        val distance = SlideDistance.toPx()
        return layout(placeable.width, placeable.height) {
            if (awaitingPlacement && !lookingAhead) {
                awaitingPlacement = false
                enter()
            }
            placeable.placeWithLayer(0, 0) { draw(distance) }
        }
    }

    private fun enter() {
        val position = cascade ?: return
        coroutineScope.launch {
            delay(
                VisibilityCascade.Shared.delayMillisFor(
                    index = position.index,
                    staggerDelayMillis = position.staggerDelayMillis,
                    maxStaggeredItems = position.maxStaggeredItems,
                ).milliseconds,
            )
            // Marked as it starts, so an element that leaves halfway through comes back whole
            // rather than playing again.
            onStarted()
            animateTo(1f)
        }
    }

    private fun animateTo(target: Float) {
        coroutineScope.launch { effects.animateTo(target, effectsSpec) }
        coroutineScope.launch { spatial.animateTo(target, spatialSpec) }
    }

    private fun GraphicsLayerScope.draw(distance: Float) {
        alpha = effects.value.coerceIn(0f, 1f)
        when (style) {
            EntranceStyle.Slide -> translationY = (1f - spatial.value) * distance
            EntranceStyle.Scale -> {
                val scale = InitialScale + (1f - InitialScale) * spatial.value
                scaleX = scale
                scaleY = scale
            }
            EntranceStyle.Fade, EntranceStyle.None -> Unit
        }
    }
}

/**
 * Decides how long each element waits before its entrance, so elements arrive in a cascade.
 *
 * Elements that start entering close together form a wave: everything within
 * [WAVE_WINDOW_MILLIS] of the wave's first element. Each waits by its place in the wave, its
 * [index] less the wave's first index when it has one, its arrival order otherwise. The entrances of
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
        }
        val arrival = arrivalsInWave++
        val first = firstIndexInWave ?: index.also { firstIndexInWave = it }
        val position: Int = if (index != null && first != null) index - first else arrival
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
