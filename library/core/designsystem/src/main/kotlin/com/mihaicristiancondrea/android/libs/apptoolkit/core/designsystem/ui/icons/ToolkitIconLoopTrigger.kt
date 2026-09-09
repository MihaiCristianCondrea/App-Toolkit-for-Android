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
 * What starts the continuous playback of an icon that declares [ToolkitIcon.Animated.loop].
 *
 * This is when the loop begins, not what one cycle looks like: the cycle stays the job of
 * [ToolkitIconReplayMode]. An icon that does not loop at all ignores this value.
 */
enum class ToolkitIconLoopTrigger {

    /**
     * Start looping as soon as the icon is composed. This is the default, and it fits an icon that
     * reports something the app is already doing, such as a sync or a recording indicator.
     */
    Immediately,

    /**
     * Stay on the resting frame until the component is clicked or becomes selected, then loop for
     * as long as it stays composed. Use it where the animation illustrates the action the person
     * just took rather than ongoing work, such as a gallery of animations that would otherwise all
     * play at once.
     */
    OnInteraction,
}
