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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents the icon for a navigation item.
 * Supports Compose [ImageVector], static drawable or vector resources ([Resource]),
 * and Animated Vector Drawables ([AnimatedVector]).
 */
@Immutable
sealed interface NavigationIcon {

    /**
     * Icon backed by a Compose [ImageVector].
     */
    @Immutable
    data class Vector(val imageVector: ImageVector) : NavigationIcon

    /**
     * Icon backed by a static drawable or vector resource ID.
     */
    @Immutable
    data class Resource(@param:DrawableRes val resId: Int) : NavigationIcon

    /**
     * Icon backed by an Animated Vector Drawable (AVD) resource ID.
     *
     * @property resId The AVD resource.
     * @property atEnd Frame the drawable rests on while the item is not selected. `false`, the
     *   default, rests on the first frame.
     * @property replayMode What a repeated click does once the animation already ran. Defaults to
     *   [NavigationIconReplayMode.Restart].
     */
    @Immutable
    data class AnimatedVector(
        @param:DrawableRes val resId: Int,
        val atEnd: Boolean = false,
        val replayMode: NavigationIconReplayMode = NavigationIconReplayMode.Restart,
    ) : NavigationIcon

    companion object {
        fun of(imageVector: ImageVector): NavigationIcon = Vector(imageVector)
        fun of(@DrawableRes resId: Int): NavigationIcon = Resource(resId)
        fun animated(
            @DrawableRes resId: Int,
            atEnd: Boolean = false,
            replayMode: NavigationIconReplayMode = NavigationIconReplayMode.Restart,
        ): NavigationIcon = AnimatedVector(resId, atEnd, replayMode)
    }
}
