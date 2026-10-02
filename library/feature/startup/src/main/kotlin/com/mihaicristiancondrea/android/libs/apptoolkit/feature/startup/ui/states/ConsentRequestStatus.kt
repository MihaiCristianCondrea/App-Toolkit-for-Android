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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackedStatus

/**
 * Where the startup screen's consent request stands, reported as its `screen_state`: `loading`
 * until consent settles, then `success`.
 */
@Immutable
sealed interface ConsentRequestStatus : TrackedStatus {

    /** Consent has not settled: the request is running or has not started. */
    data object Pending : ConsentRequestStatus {
        override val trackingLabel: String get() = "loading"
    }

    /**
     * Consent has settled, whether it was given, failed, or took too long. The person can continue.
     */
    data object Settled : ConsentRequestStatus {
        override val trackingLabel: String get() = "success"
    }
}
