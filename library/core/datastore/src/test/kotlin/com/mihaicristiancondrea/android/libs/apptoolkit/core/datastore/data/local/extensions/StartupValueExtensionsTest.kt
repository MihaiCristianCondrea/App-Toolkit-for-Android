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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions

import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StartupValueExtensionsTest {
    @Test
    fun mapsDefaultsAndLegacyRoutesAndDeduplicatesMappedValues() = runTest {
        val store = mockk<CommonDataStore>()
        every { store.getStartupPage(default = "home") } returns
            flowOf("", " ", "home", "apps", "apps", "legacy", "unknown", "apps")
        val received = mutableListOf<String>()
        val values = store.startupValueFlow(defaultRoute = "home") { route ->
            received += route
            if (route == "apps") 1 else 0
        }.toList()

        assertEquals(listOf(0, 1, 0, 1), values)
        assertEquals(listOf("home", "home", "home", "apps", "apps", "legacy", "unknown", "apps"), received)
    }
}
