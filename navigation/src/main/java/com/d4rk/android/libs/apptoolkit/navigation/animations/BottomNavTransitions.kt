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

package com.d4rk.android.libs.apptoolkit.navigation.animations

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Remembers and provides bottom navigation (fade) transitions.
 */
@Composable
fun rememberBottomNavTransitions(): BottomNavTransitions {
    return remember { BottomNavTransitions() }
}

/**
 * Class providing standard fade transitions for top-level navigation.
 */
class BottomNavTransitions {

    /**
     * Standard forward navigation transition.
     */
    fun forward(): ContentTransform {
        return transition()
    }

    /**
     * Standard pop (back) navigation transition.
     */
    fun pop(): ContentTransform {
        return transition()
    }

    /**
     * Predictive back navigation transition.
     */
    fun predictivePop(): ContentTransform {
        return transition()
    }

    /**
     * Generic fade transition.
     */
    fun transition(): ContentTransform {
        return fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
    }

    /**
     * Directional bottom-bar transition used when moving between tab indices.
     */
    fun betweenTabs(forward: Boolean): ContentTransform {
        val enter = slideInHorizontally(
            animationSpec = tween(260),
            initialOffsetX = { fullWidth -> if (forward) fullWidth / 5 else -fullWidth / 5 },
        ) + fadeIn(animationSpec = tween(220))

        val exit = slideOutHorizontally(
            animationSpec = tween(260),
            targetOffsetX = { fullWidth -> if (forward) -fullWidth / 8 else fullWidth / 8 },
        ) + fadeOut(animationSpec = tween(220))

        return enter togetherWith exit
    }
}
