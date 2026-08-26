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

/**
 * Persisted ads preference.
 *
 * [limitAds] is the only stored ads preference, and this layer stores it without interpreting it.
 * It records that the user asked for fewer ads; *how many fewer* is decided by `AdsDisplayPolicy`
 * and differs between debug and release builds, which is why the key is not named after either
 * behaviour.
 */
interface AdsPreferencesDataSource {

    /** Emits the limit-ads opt-in, `false` until the user turns it on. */
    val limitAds: Flow<Boolean>

    /** Persists the limit-ads opt-in. */
    suspend fun saveLimitAds(isChecked: Boolean)
}
