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

package com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.repository

import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.model.UsageAndDiagnosticsSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository exposing usage and diagnostics related settings to the rest of
 * the application. Implementations should delegate to a data source to read
 * and persist the underlying values.
 */
interface UsageAndDiagnosticsRepository {
    /** Emits all usage and diagnostics related consent values. */
    fun observeSettings(): Flow<UsageAndDiagnosticsSettings>

    suspend fun setUsageAndDiagnostics(enabled: Boolean)
    suspend fun setAnalyticsConsent(granted: Boolean)
    suspend fun setAdStorageConsent(granted: Boolean)
    suspend fun setAdUserDataConsent(granted: Boolean)
    suspend fun setAdPersonalizationConsent(granted: Boolean)
}

