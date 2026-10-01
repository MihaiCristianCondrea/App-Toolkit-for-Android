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

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper

/**
 * A status that [TrackScreenState] can report in the `screen_state` analytics event.
 *
 * [Loadable] implements it. A screen with statuses of its own implements it too, to have them
 * tracked like the Toolkit's.
 */
interface TrackedStatus {
    /** The `state` parameter of the `screen_state` event. */
    val trackingLabel: String
}

/**
 * The status of one piece of content that loads: the Toolkit's default, used as a field of a
 * screen's state rather than for the whole screen.
 *
 * Nothing makes a screen start at [Loading]: its ViewModel picks the first value. Content that is
 * there from the start needs no `Loadable` at all, and a screen whose cases differ from these
 * declares a sealed type of its own.
 *
 * The `screen_state` labels are the ones the Toolkit has always reported: `loading`, `success`,
 * `no_data` and `error`.
 */
@Immutable
sealed interface Loadable<out T> : TrackedStatus {

    /** The content is being loaded and there is none to show yet. */
    data object Loading : Loadable<Nothing> {
        override val trackingLabel: String get() = "loading"
    }

    /**
     * The content is here.
     *
     * @property refreshing A newer copy is being loaded behind this one.
     * @property stale This is an older copy, for example a saved one shown while offline.
     */
    data class Ready<out T>(
        val value: T,
        val refreshing: Boolean = false,
        val stale: Boolean = false,
    ) : Loadable<T> {
        override val trackingLabel: String get() = "success"
    }

    /** There is nothing to show. [message], if any, says why. */
    data class Empty(val message: UiTextHelper? = null) : Loadable<Nothing> {
        override val trackingLabel: String get() = "no_data"
    }

    /** Loading failed with [message]; [retryable] when trying again can help. */
    data class Failed(
        val message: UiTextHelper,
        val retryable: Boolean = true,
    ) : Loadable<Nothing> {
        override val trackingLabel: String get() = "error"
    }
}
