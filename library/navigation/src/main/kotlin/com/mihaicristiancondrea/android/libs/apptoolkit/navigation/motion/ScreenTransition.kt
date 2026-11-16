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
