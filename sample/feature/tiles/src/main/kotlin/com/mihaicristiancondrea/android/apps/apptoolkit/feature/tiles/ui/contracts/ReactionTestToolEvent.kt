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

/** What the Reaction Test tool asks its ViewModel to do. */
sealed interface ReactionTestToolEvent {
    /** Starts a round, unless one is already waiting for the signal or showing it. */
    data object Start : ReactionTestToolEvent

    /** The user tapped the test area: a false start while waiting, a result once signalled. */
    data object Tap : ReactionTestToolEvent

    /** Clears the session's results. */
    data object Reset : ReactionTestToolEvent

    /** The tool's sheet closed. The session's results are kept. */
    data object Dismiss : ReactionTestToolEvent
}
