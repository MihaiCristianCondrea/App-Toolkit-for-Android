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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Persisted ads preferences.
 *
 * Two independent facts live here and must not be collapsed into one:
 *
 * - [adsEnabled] answers "is this install allowed to see ads at all?". It is the hard gate both the
 *   SDK initializer and the ad views must read; sampling the preference separately with a different
 *   default is what previously let views request ads the SDK had never been initialized for. No
 *   host UI toggles it any more, but it stays as the gate hosts reach for in code.
 * - [reduceAds] answers "how intrusive may ads be?". It is an opt-in the user makes and carries no
 *   build-configurable default. Interpreting it is host policy; this layer only stores the choice.
 */
interface AdsPreferencesDataSource {

    /** Hot, always-current ads preference carrying the configured build default. */
    val adsEnabled: StateFlow<Boolean>

    /** Emits the ads preference with a caller-supplied default. */
    fun ads(default: Boolean): Flow<Boolean>

    /** Persists the ads preference. */
    suspend fun saveAds(isChecked: Boolean)

    /** Emits the reduced-ads opt-in, `false` until the user turns it on. */
    val reduceAds: Flow<Boolean>

    /** Persists the reduced-ads opt-in. */
    suspend fun saveReduceAds(isChecked: Boolean)

    /** Stops the sharing coroutine backing [adsEnabled]. */
    fun close()
}
