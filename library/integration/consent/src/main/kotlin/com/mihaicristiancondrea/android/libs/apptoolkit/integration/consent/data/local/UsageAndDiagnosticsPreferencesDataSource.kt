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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.local

/**
 * Abstraction over storage operations for usage and diagnostics related
 * preferences. Implementations are responsible for persisting and
 * exposing the different consent values used by the diagnostics
 * feature.
 */
interface UsageAndDiagnosticsPreferencesDataSource : ConsentPreferencesDataSource {

    suspend fun saveUsageAndDiagnostics(isChecked: Boolean)

    suspend fun saveAnalyticsConsent(isGranted: Boolean)

    suspend fun saveAdStorageConsent(isGranted: Boolean)

    suspend fun saveAdUserDataConsent(isGranted: Boolean)

    suspend fun saveAdPersonalizationConsent(isGranted: Boolean)

    /** Persists every choice in one transaction, without intermediate combinations. */
    suspend fun saveAll(
        usageAndDiagnostics: Boolean,
        analyticsConsent: Boolean,
        adStorageConsent: Boolean,
        adUserDataConsent: Boolean,
        adPersonalizationConsent: Boolean,
    )
}
