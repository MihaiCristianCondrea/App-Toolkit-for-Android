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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * When a screen's entrance plays. None of them replays it on returning from the background, on a
 * rotation, or for an item scrolled back into view within the visit.
 */
enum class EntranceReplay {
    /** Each time the screen is shown by navigation, including going back to it. */
    EveryVisit,

    /**
     * Each time the screen is opened: a screen opened again after it was closed, or a tab picked
     * from the navigation, enters anew, while going back to it does not.
     */
    OnOpen,

    /** Only the first time each screen is opened while the app runs; opening it again does not. */
    FirstVisit,
}
