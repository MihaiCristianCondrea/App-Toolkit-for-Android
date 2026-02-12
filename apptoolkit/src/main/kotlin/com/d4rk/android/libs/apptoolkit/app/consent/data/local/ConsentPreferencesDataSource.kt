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

package com.d4rk.android.libs.apptoolkit.app.consent.data.local

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction over persisted consent preferences used by the consent feature.
 */
interface ConsentPreferencesDataSource {

    /** Emits whether usage and diagnostics collection is enabled. */
    fun usageAndDiagnostics(default: Boolean): Flow<Boolean>

    /** Emits the current analytics consent state. */
    fun analyticsConsent(default: Boolean): Flow<Boolean>

    /** Emits the current ad storage consent state. */
    fun adStorageConsent(default: Boolean): Flow<Boolean>

    /** Emits the current ad user data consent state. */
    fun adUserDataConsent(default: Boolean): Flow<Boolean>

    /** Emits the current ad personalization consent state. */
    fun adPersonalizationConsent(default: Boolean): Flow<Boolean>
}
