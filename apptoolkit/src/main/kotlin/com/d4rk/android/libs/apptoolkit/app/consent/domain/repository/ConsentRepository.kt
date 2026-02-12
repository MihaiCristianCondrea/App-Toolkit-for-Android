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

package com.d4rk.android.libs.apptoolkit.app.consent.domain.repository

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.coroutines.flow.Flow

/**
 * Repository responsible for requesting and displaying consent forms via UMP.
 */
interface ConsentRepository {

    /**
     * Requests consent information and optionally shows the consent form.
     *
     * @param host The UI host needed by the UMP SDK.
     * @param showIfRequired When true, the form is shown only when required by UMP.
     */
    fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>>

    /**
     * Reads persisted consent values and applies them to Firebase services.
     */
    suspend fun applyInitialConsent()

    /**
     * Applies the provided consent settings to Firebase services.
     */
    suspend fun applyConsentSettings(settings: ConsentSettings)
}
