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
import kotlin.math.roundToInt

/** How the shell moves between tabs. Pages and children use a [ScreenTransition]. */
enum class TabTransitionStyle {
    /**
     * The incoming tab slides a fifth of the width in from the side of its tab, the outgoing one an
     * eighth the other way, both fading. Tells the person which way they moved along the bar.
     */
    Directional,

    /** Material's fade through: the old tab fades out, then the new one fades and scales in. */
    FadeThrough,

    /** A plain cross-fade. */
    Fade,

    /** Tabs swap instantly. */
    None,
}

@Immutable
class TabTransitions internal constructor(
    private val style: TabTransitionStyle,
    private val durationScale: Float,
    /** 1 in left-to-right layouts, -1 in right-to-left ones. */
    private val direction: Int = 1,
) {

    /** @param forward Whether the new tab sits after the old one in the navigation bar. */
    fun between(forward: Boolean): ContentTransform = when (style) {
        TabTransitionStyle.Directional -> {
            val sign = if (forward) direction else -direction
            val enter = slideInHorizontally(tween(duration(260), easing = FastOutSlowInEasing)) { width ->
                sign * width / 5
            } + fadeIn(tween(duration(220)))
            val exit = slideOutHorizontally(tween(duration(260), easing = FastOutSlowInEasing)) { width ->
                -sign * width / 8
            } + fadeOut(tween(duration(220)))
            enter togetherWith exit
        }

        TabTransitionStyle.FadeThrough -> {
            val enter = fadeIn(tween(duration(210), delayMillis = duration(90))) +
                scaleIn(tween(duration(210), delayMillis = duration(90)), initialScale = 0.92f)
            enter togetherWith fadeOut(tween(duration(90)))
        }

        TabTransitionStyle.Fade -> fadeIn(tween(duration(300))) togetherWith fadeOut(tween(duration(300)))

        TabTransitionStyle.None -> EnterTransition.None togetherWith ExitTransition.None
    }

    /**
     * One destination replacing another in the same place, with nothing moving. Beside a rail or a
     * permanent drawer, the shell uses it between the tabs and the page the navigation opened, so
     * the app bar over them seems to stay where it is, whatever [TabTransitionStyle] the tabs use.
     *
     * Only the destination on top fades, over one that stays opaque: going forward the new one
     * fades in over the old, which holds until it has; going [back] the old one fades out over the
     * new, drawn in full under it. A fade through, the old fading out before the new fades in,
     * showed the display's backdrop between the two, a grey flash, and a destination slow to
     * compose held the old one and then swapped it at once.
     */
    fun inPlace(back: Boolean = false): ContentTransform = if (back) {
        EnterTransition.None togetherWith fadeOut(tween(duration(InPlaceMillis)))
    } else {
        // Kept whole until the new one covers it: a fade out of one frame, after the fade in.
        fadeIn(tween(duration(InPlaceMillis))) togetherWith
            fadeOut(tween(durationMillis = 1, delayMillis = duration(InPlaceMillis)))
    }

    private fun duration(millis: Int): Int = (millis * durationScale).roundToInt()
}

/** How long a destination takes to fade in or out in place. */
private const val InPlaceMillis = 220
