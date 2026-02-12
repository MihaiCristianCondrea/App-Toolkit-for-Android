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

package com.d4rk.android.libs.apptoolkit.app.ads.domain.repository

import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing advertisement settings.
 *
 * This interface defines the contract for accessing and modifying
 * user preferences related to advertisements within the application.
 */
interface AdsSettingsRepository {
    val defaultAdsEnabled: Boolean
    fun observeAdsEnabled(): Flow<Boolean>
    suspend fun setAdsEnabled(enabled: Boolean): Result<Unit>
}
