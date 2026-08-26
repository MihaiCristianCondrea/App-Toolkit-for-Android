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
 * Repository interface for the ads settings screen.
 *
 * One preference, one question: how intrusive may ads be? There is no enablement preference any
 * more — the SDK initializes and ad slots render unconditionally. What "reduced" means is decided
 * by the host, not here; in the sample it suppresses app-open ads.
 */
interface AdsSettingsRepository {
    fun observeReduceAds(): Flow<Boolean>
    suspend fun setReduceAds(enabled: Boolean): DataState<Unit, Errors.Database>
}
