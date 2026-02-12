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

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentSettings
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import io.mockk.mockk
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ApplyConsentSettingsUseCaseTest {

    @Test
    fun `invoke logs breadcrumb and delegates to repository`() = runTest {
        val repository = mockk<ConsentRepository>(relaxed = true)
        val firebaseController = mockk<FirebaseController>(relaxed = true)
        val useCase = ApplyConsentSettingsUseCase(
            repository = repository,
            firebaseController = firebaseController,
        )
        val settings = ConsentSettings(
            usageAndDiagnostics = true,
            analyticsConsent = false,
            adStorageConsent = true,
            adUserDataConsent = false,
            adPersonalizationConsent = true,
        )

        useCase(settings)

        verify {
            firebaseController.logBreadcrumb(
                message = "Consent settings applied",
                attributes = mapOf(
                    "usageAndDiagnostics" to "true",
                    "analyticsConsent" to "false",
                    "adStorageConsent" to "true",
                    "adUserDataConsent" to "false",
                    "adPersonalizationConsent" to "true",
                ),
            )
        }
        coVerify { repository.applyConsentSettings(settings) }
    }
}
