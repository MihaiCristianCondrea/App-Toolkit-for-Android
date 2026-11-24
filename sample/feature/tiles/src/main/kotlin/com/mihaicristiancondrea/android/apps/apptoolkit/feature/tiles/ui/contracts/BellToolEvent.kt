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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts

/** Actions handled by [com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.BellToolViewModel]. */
sealed interface BellToolEvent {
    /** Starts observing motion and loads the bell audio sample for the foreground session. */
    data object Open : BellToolEvent

    /** Stops motion observation, releases audio resources, and resets the session state. */
    data object Close : BellToolEvent

    /** Rings the bell manually at full strength when audio is ready. */
    data object Ring : BellToolEvent
}
