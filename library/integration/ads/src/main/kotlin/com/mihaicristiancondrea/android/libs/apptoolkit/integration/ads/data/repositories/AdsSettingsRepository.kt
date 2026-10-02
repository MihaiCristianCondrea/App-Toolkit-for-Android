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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import kotlinx.coroutines.flow.Flow

/**
 * The stored ad preferences: whether ads show at all, and whether App Open ads are reduced.
 *
 * Reads fail with a [StorageException] from the flow, and writes throw one, when the preferences
 * cannot be read or written. Every function is safe to call from the main thread.
 */
interface AdsSettingsRepository {

    /** Whether ads show, with the store's default until the person changes it. */
    fun observeAdsEnabled(): Flow<Boolean>

    /** Whether App Open ads are suppressed. */
    fun observeReduceAds(): Flow<Boolean>

    /** @throws StorageException when the preference cannot be written. */
    suspend fun setAdsEnabled(enabled: Boolean)

    /** @throws StorageException when the preference cannot be written. */
    suspend fun setReduceAds(enabled: Boolean)
}
