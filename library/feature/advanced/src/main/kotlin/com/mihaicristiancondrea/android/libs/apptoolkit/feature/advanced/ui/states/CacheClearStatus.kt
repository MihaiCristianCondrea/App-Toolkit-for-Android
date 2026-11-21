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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackedStatus

/** Cache operation status reported by Advanced Settings through [TrackedStatus]. */
@Immutable
sealed interface CacheClearStatus : TrackedStatus {

    /** No cache clear is running, and the last one, if any, succeeded. */
    data object Idle : CacheClearStatus {
        override val trackingLabel: String get() = "success"
    }

    /** A cache clear is running and the clear action is disabled. */
    data object Clearing : CacheClearStatus {
        override val trackingLabel: String get() = "loading"
    }

    /** The last cache clear failed and the action can be tried again. */
    data object Failed : CacheClearStatus {
        override val trackingLabel: String get() = "error"
    }
}
