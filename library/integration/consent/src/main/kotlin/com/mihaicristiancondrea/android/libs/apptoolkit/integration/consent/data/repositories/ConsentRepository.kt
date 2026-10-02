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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.exceptions.ConsentException
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentSettings

/**
 * Requests consent through UMP and applies consent choices to Firebase. Every function is safe to
 * call from the main thread.
 */
interface ConsentRepository {

    /**
     * Updates the consent information and shows the consent form: only when UMP requires it when
     * [showIfRequired] is true, always otherwise. Returns once the round trip has ended. A caller
     * that asks while a matching round trip is running waits for that one instead of starting
     * another.
     *
     * @param host The UI host needed by the UMP SDK.
     * @throws ConsentException when UMP fails or [host] is finishing, destroyed or off screen.
     */
    suspend fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean = true,
    )

    /**
     * Reads the stored consent choices and applies them to Firebase services. Unset choices are
     * granted in release builds and refused in debug builds.
     *
     * @throws com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
     * when the stored choices cannot be read.
     */
    suspend fun applyInitialConsent()

    /**
     * Applies the provided consent settings to Firebase services.
     */
    suspend fun applyConsentSettings(settings: ConsentSettings)
}
