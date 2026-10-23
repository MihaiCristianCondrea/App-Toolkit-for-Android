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
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConsentSettingsSnapshotTest {
    @Test
    fun `bulk choices emit complete snapshots without intermediate mixtures`() = runTest {
        val source = DefaultUsageAndDiagnosticsPreferencesDataSource(FakePreferencesDataStore())
        val seen = mutableListOf<ConsentSettings>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            source.settings(default = false).collect { seen += it }
        }
        source.saveAll(true, true, true, true, true)
        source.saveAll(false, false, false, false, false)
        assertEquals(
            listOf(
                ConsentSettings(false, false, false, false, false),
                ConsentSettings(true, true, true, true, true),
                ConsentSettings(false, false, false, false, false),
            ),
            seen,
        )
    }
}
