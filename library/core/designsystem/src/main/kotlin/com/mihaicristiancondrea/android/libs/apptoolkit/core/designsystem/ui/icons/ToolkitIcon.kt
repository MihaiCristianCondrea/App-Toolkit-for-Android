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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * One icon slot of a toolkit component, such as a navigation item or a button.
 *
 * Four sources are accepted:
 * - [Vector], a Compose [ImageVector] like `Icons.Rounded.Share`.
 * - [Resource], a static drawable or vector XML resource, drawn through a painter.
 * - [AnimatedVector], an Animated Vector Drawable that plays when the component is clicked.
 * - [Lottie], bundled Lottie JSON that plays once per interaction.
 *
 * Both animated sources accept [Animated.loop] to keep playing while composed instead of only on
 * interaction; it is off by default and orthogonal to their replay mode.
 *
 * Components that own a selected state take two of these, one per state, and both slots accept any
 * of the four sources. See `:library:core:designsystem` README.md for the accepted combinations and the
 * behavior of each one.
 */
@Immutable
sealed interface ToolkitIcon {

    /** Playback options shared by bundled animation sources; these are presentation values. */
    sealed interface Animated : ToolkitIcon {
        val atEnd: Boolean
        val replayMode: ToolkitIconReplayMode

        /**
         * Whether the animation keeps playing on its own while the icon is composed, instead of
         * only running once per interaction. `false`, the default, keeps the finite click driven
         * playback every existing icon relies on.
         *
         * Looping is independent from [replayMode], which keeps describing the shape of one cycle:
         * a looping [ToolkitIconReplayMode.Restart] icon repeats forward from its first frame,
         * while a looping [ToolkitIconReplayMode.Reverse] icon travels forward and back.
         */
        val loop: Boolean
    }

    /**
     * Icon backed by a Compose [ImageVector], for example a Material icon.
     */
    @Immutable
    data class Vector(val imageVector: ImageVector) : ToolkitIcon

    /**
     * Icon backed by a static drawable or vector XML resource. Nothing about it animates.
     */
    @Immutable
    data class Resource(@param:DrawableRes val resId: Int) : ToolkitIcon

    /**
     * Icon backed by an Animated Vector Drawable.
     *
     * The drawable is expected to declare exactly two states, the first and the last frame, the way
     * `androidx.compose.animation.graphics` reads an `animated-vector`. Components play it forward
     * when they are clicked or become selected, and back to the first frame when they lose
     * selection.
     *
     * @property resId The `animated-vector` resource.
     * @property atEnd Frame the drawable rests on while the component is neither selected nor
     *   clicked. `false`, the default, rests on the first frame.
     * @property replayMode What a repeated click does once the animation already ran. Defaults to
     *   [ToolkitIconReplayMode.Restart].
     * @property loop Whether the drawable animates continuously while composed. Defaults to `false`,
     *   which keeps the click driven playback. The cycle follows [replayMode].
     */
    @Immutable
    data class AnimatedVector(
        @param:DrawableRes val resId: Int,
        override val atEnd: Boolean = false,
        override val replayMode: ToolkitIconReplayMode = ToolkitIconReplayMode.Restart,
        override val loop: Boolean = false,
    ) : Animated

    /**
     * Bundled Lottie JSON in `res/raw`. Plays once per interaction unless [loop] is set.
     * Artwork retains its authored colors; the renderer's tint applies only when [tintable] is true.
     * Prefer small vector-only compositions for navigation and button icons.
     */
    @Immutable
    data class Lottie(
        @param:RawRes val resId: Int,
        override val atEnd: Boolean = false,
        override val replayMode: ToolkitIconReplayMode = ToolkitIconReplayMode.Restart,
        val tintable: Boolean = false,
        override val loop: Boolean = false,
    ) : Animated

    companion object {
        fun of(imageVector: ImageVector): ToolkitIcon = Vector(imageVector)
        fun of(@DrawableRes resId: Int): ToolkitIcon = Resource(resId)
        fun animated(
            @DrawableRes resId: Int,
            atEnd: Boolean = false,
            replayMode: ToolkitIconReplayMode = ToolkitIconReplayMode.Restart,
            loop: Boolean = false,
        ): AnimatedVector = AnimatedVector(resId, atEnd, replayMode, loop)
    }
}
