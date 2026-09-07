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

/**
 * How a [ToolkitIcon.AnimatedVector] behaves when its item is clicked again while the drawable
 * already rests on the last frame of the animation.
 */
enum class ToolkitIconReplayMode {

    /**
     * Play the animation forward again from its first frame. This is the default and fits the usual
     * looping icon whose last frame is drawn like its first one, so the reset is invisible and every
     * click looks like the same animation.
     */
    Restart,

    /**
     * Play the animation backwards, from the last frame to the first one. Use it for drawables that
     * genuinely morph between two different shapes and should visibly travel back, such as a
     * play/pause or menu/close toggle.
     */
    Reverse,
}
