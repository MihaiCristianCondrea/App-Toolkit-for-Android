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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.IntOffset
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DestinationKind
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import kotlin.math.roundToInt

/** How a child or a page enters, and leaves again on back. */
enum class ScreenTransition {
    /**
     * Android's activity transition: the new screen slides in over the old one, and back is the
     * system's cross-activity animation, predictive gesture included. Made for screens that stand
     * on their own, the way activities did.
     */
    Activity,

    /**
     * A plain horizontal push: the new screen slides in across the whole width while the old one
     * slides a quarter of the way out; back reverses it, and the gesture seeks it. Made for a
     * detail inside a tab.
     */
    Slide,

    /** Material's fade through. */
    FadeThrough,

    /** A cross-fade. */
    Fade,

    /** An instant swap. */
    None,
}

/**
 * Which [ScreenTransition] each kind of destination uses when it declares none, with defaults of
 * their own for some navigation layouts.
 *
 * ```
 * transitions(
 *     ShellTransitions(
 *         children = ScreenTransition.Slide,
 *         pages = ScreenTransition.Activity,
 *         byLayout = mapOf(ShellLayoutMode.PermanentDrawer to ShellTransitions(children = ScreenTransition.Fade)),
 *     ),
 * )
 * ```
 *
 * A destination's own `transition` wins over all of these.
 */
@Immutable
class ShellTransitions(
    val children: ScreenTransition = ScreenTransition.Slide,
    val pages: ScreenTransition = ScreenTransition.Activity,
    val byLayout: Map<ShellLayoutMode, ShellTransitions> = emptyMap(),
) {
    /** The transition of a destination of [kind] declaring [own], in [layout]. */
    fun resolve(kind: DestinationKind, own: ScreenTransition?, layout: ShellLayoutMode): ScreenTransition {
        if (own != null) return own
        val defaults = byLayout[layout] ?: this
        return if (kind == DestinationKind.Page) defaults.pages else defaults.children
    }
}

/** Plays a [ScreenTransition] forward or back, scaled by the developer options' animation speed. */
@Immutable
class ScreenTransitions internal constructor(
    private val activity: ActivityTransitions,
    private val durationScale: Float,
    /** 1 in left-to-right layouts, -1 in right-to-left ones. */
    private val direction: Int = 1,
) {
    fun forward(transition: ScreenTransition): ContentTransform = play(transition, back = false)

    /**
     * @param mirrored Plays a sliding back the other way, for a back swipe from the right edge that
     * should follow the finger.
     */
    fun back(transition: ScreenTransition, mirrored: Boolean = false): ContentTransform =
        play(transition, back = true, mirrored = mirrored)

    private fun play(transition: ScreenTransition, back: Boolean, mirrored: Boolean = false): ContentTransform = when (transition) {
        ScreenTransition.Activity -> if (back) activity.close() else activity.open()

        ScreenTransition.Slide -> {
            val sign = (if (back) -direction else direction) * (if (mirrored) -1 else 1)
            val spec = tween<IntOffset>(duration(350), easing = FastOutSlowInEasing)
            if (back) {
                // The old screen returns from a quarter out; the leaving one slides away whole,
                // and stays on top until it has.
                val enter = slideInHorizontally(spec) { width -> sign * width / 4 }
                val exit = slideOutHorizontally(spec) { width -> -sign * width }
                (enter togetherWith exit).apply { targetContentZIndex = -1f }
            } else {
                val enter = slideInHorizontally(spec) { width -> sign * width }
                val exit = slideOutHorizontally(spec) { width -> -sign * width / 4 }
                enter togetherWith exit
            }
        }

        ScreenTransition.FadeThrough -> {
            val enter = fadeIn(tween(duration(210), delayMillis = duration(90))) +
                scaleIn(tween(duration(210), delayMillis = duration(90)), initialScale = 0.92f)
            enter togetherWith fadeOut(tween(duration(90)))
        }

        ScreenTransition.Fade -> fadeIn(tween(duration(300))) togetherWith fadeOut(tween(duration(300)))

        ScreenTransition.None -> EnterTransition.None togetherWith ExitTransition.None
    }

    private fun duration(millis: Int): Int = (millis * durationScale).roundToInt()
}
