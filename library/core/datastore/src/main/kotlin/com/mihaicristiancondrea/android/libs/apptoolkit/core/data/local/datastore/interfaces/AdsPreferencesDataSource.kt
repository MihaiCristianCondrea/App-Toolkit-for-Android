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
 * [reduceAds] is the only stored ads preference. It answers "how intrusive may ads be?" — never
 * "may this app show ads at all?". There is no enablement switch any more: the SDK initializes and
 * ad slots render unconditionally, and interpreting the opt-in is host policy, not storage.
 */
interface AdsPreferencesDataSource {

    /** Emits the reduced-ads opt-in, `false` until the user turns it on. */
    val reduceAds: Flow<Boolean>

    /** Persists the reduced-ads opt-in. */
    suspend fun saveReduceAds(isChecked: Boolean)
}
