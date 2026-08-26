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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing advertisement settings.
 *
 * Two separate preferences are exposed because they answer different questions:
 *
 * - `adsEnabled` is the legacy hard gate — whether this install may see ads at all. No host UI
 *   toggles it any more, but installs that opted out stay ad-free, so the read/write pair is kept
 *   for compatibility, migrations, tests, and host-specific logic such as purchases.
 * - `reduceAds` is the user's opt-in to a less intrusive ad policy. What "reduced" means — fewer
 *   native ads, no app-open ads — is decided by the host, not here.
 */
interface AdsSettingsRepository {
    val defaultAdsEnabled: Boolean
    fun observeAdsEnabled(): Flow<Boolean>
    fun observeReduceAds(): Flow<Boolean>
    suspend fun setAdsEnabled(enabled: Boolean): DataState<Unit, Errors.Database>
    suspend fun setReduceAds(enabled: Boolean): DataState<Unit, Errors.Database>
}
