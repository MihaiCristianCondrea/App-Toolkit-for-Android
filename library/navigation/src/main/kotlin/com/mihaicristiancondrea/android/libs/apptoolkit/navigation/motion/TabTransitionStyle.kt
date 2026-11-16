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
