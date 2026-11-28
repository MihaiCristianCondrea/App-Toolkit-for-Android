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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.constants

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Geometry of the holiday greeting card. The fractions describe the illustrations, drawn on a
 * 360 by 480 viewport; the sizes bound the card in the window.
 */
internal object HolidayGreetingLayoutConstants {
    /** The illustrations' aspect ratio, 360 by 480. */
    const val ILLUSTRATION_ASPECT_RATIO: Float = 360f / 480f

    /** Where the scene, the art's middle band, starts: 140 units down, under the extra sky. */
    const val SCENE_TOP_FRACTION: Float = 140f / 480f

    /** How tall the scene is: 200 units, with ground running on below it. */
    const val SCENE_HEIGHT_FRACTION: Float = 200f / 480f

    /** How much of the scene's height stays clear of the scrims at least, when the text needs room. */
    const val MIN_VISIBLE_ART: Float = 0.55f

    /** The same in a short window, where the choices matter more than the whole of the art. */
    const val MIN_VISIBLE_ART_COMPACT: Float = 0.3f

    /** Share of the window's height the card prefers to fill. */
    const val PREFERRED_HEIGHT_FRACTION: Float = 0.65f

    /** Cards that may be no taller than this lay out compactly. */
    val COMPACT_CARD_HEIGHT: Dp = 420.dp

    /** Widest the card grows on a large window. */
    val MAX_CARD_WIDTH: Dp = 400.dp

    /** Narrowest the card shrinks before it fills the window instead. */
    val MIN_CARD_WIDTH: Dp = 280.dp

    /** Tallest the card's preferred height grows. */
    val MAX_PREFERRED_CARD_HEIGHT: Dp = 500.dp

    /** Shortest the card shrinks before it fills the window instead. */
    val MIN_CARD_HEIGHT: Dp = 240.dp
}
