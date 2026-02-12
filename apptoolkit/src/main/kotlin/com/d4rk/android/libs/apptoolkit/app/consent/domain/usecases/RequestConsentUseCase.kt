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

package com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Emits consent-loading results as a [Flow] so callers can react to loading, success, or errors.
 */
class RequestConsentUseCase(
    private val repository: ConsentRepository,
    private val firebaseController: FirebaseController,
) {

    operator fun invoke(
        host: ConsentHost,
        showIfRequired: Boolean = true,
    ): Flow<DataState<Unit, Errors.UseCase>> {
        return repository.requestConsent(
            host = host,
            showIfRequired = showIfRequired,
        ).onStart {
            firebaseController.logBreadcrumb(
                message = "Consent request started",
                attributes = mapOf(
                    "host" to host.activity::class.java.name,
                    "showIfRequired" to showIfRequired.toString(),
                ),
            )
        }
    }
}
