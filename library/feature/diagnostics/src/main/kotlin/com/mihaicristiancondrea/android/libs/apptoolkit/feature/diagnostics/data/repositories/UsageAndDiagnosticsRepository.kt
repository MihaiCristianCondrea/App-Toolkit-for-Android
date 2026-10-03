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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.domain.models.UsageAndDiagnosticsSettings
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

    /**
     * Stores every value of [settings] together, for whole-bundle answers such as "Allow all", so
     * [observeSettings] never reports a mix of the old and new choices in between.
     *
     * The default stores the values one by one, so other implementations keep compiling;
     * `DefaultUsageAndDiagnosticsRepository` stores them in one write.
     */
    suspend fun setAll(settings: UsageAndDiagnosticsSettings) {
        setUsageAndDiagnostics(settings.usageAndDiagnostics)
        setAnalyticsConsent(settings.analyticsConsent)
        setAdStorageConsent(settings.adStorageConsent)
        setAdUserDataConsent(settings.adUserDataConsent)
        setAdPersonalizationConsent(settings.adPersonalizationConsent)
    }
}

