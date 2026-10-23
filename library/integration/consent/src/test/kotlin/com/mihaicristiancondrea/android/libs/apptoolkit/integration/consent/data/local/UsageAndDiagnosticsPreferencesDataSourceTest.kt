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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UsageAndDiagnosticsPreferencesDataSourceTest {

    private fun source() = DefaultUsageAndDiagnosticsPreferencesDataSource(dataStore = FakePreferencesDataStore())

    @Test
    fun `usage and diagnostics uses the given default then persists`() = runTest {
        val source = source()

        assertFalse(source.usageAndDiagnostics(default = false).first())

        source.saveUsageAndDiagnostics(true)

        assertTrue(source.usageAndDiagnostics(default = false).first())
    }

    @Test
    fun `each consent uses the given default then persists`() = runTest {
        val source = source()

        assertTrue(source.analyticsConsent(default = true).first())
        assertTrue(source.adStorageConsent(default = true).first())
        assertFalse(source.adUserDataConsent(default = false).first())
        assertFalse(source.adPersonalizationConsent(default = false).first())

        source.saveAnalyticsConsent(false)
        source.saveAdStorageConsent(false)
        source.saveAdUserDataConsent(true)
        source.saveAdPersonalizationConsent(true)

        assertFalse(source.analyticsConsent(default = true).first())
        assertFalse(source.adStorageConsent(default = true).first())
        assertTrue(source.adUserDataConsent(default = false).first())
        assertTrue(source.adPersonalizationConsent(default = false).first())
    }

    @Test
    fun `saving all writes every choice at once`() = runTest {
        val source = source()

        source.saveAll(
            usageAndDiagnostics = true,
            analyticsConsent = true,
            adStorageConsent = false,
            adUserDataConsent = true,
            adPersonalizationConsent = false,
        )

        assertTrue(source.usageAndDiagnostics(default = false).first())
        assertTrue(source.analyticsConsent(default = false).first())
        assertFalse(source.adStorageConsent(default = true).first())
        assertTrue(source.adUserDataConsent(default = false).first())
        assertFalse(source.adPersonalizationConsent(default = true).first())
    }
}
