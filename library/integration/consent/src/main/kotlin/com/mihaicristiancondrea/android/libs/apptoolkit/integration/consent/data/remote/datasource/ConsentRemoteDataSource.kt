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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote.datasource

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.exceptions.ConsentException
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost

/**
 * One UMP consent round trip.
 */
interface ConsentRemoteDataSource {

    /**
     * Updates the consent information, then shows the form: only when UMP requires it when
     * [showIfRequired] is true, always otherwise. Returns once the round trip has ended. UMP needs
     * its entry points called on the main thread.
     *
     * @param host The UI host needed by the UMP SDK.
     * @throws ConsentException when UMP fails or [host] can no longer show a form.
     */
    suspend fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    )
}
