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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local

import app.cash.turbine.test
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DisplayPreferencesDataSourceTest {

    private fun source() = DefaultDisplayPreferencesDataSource(dataStore = FakePreferencesDataStore())

    @Test
    fun `startup page emits default when absent then updates on save`() = runTest {
        val source = source()

        source.startupPage(default = "default").test {
            assertEquals("default", awaitItem())
            source.saveStartupPage("updated")
            assertEquals("updated", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `bouncy buttons default to true and persist`() = runTest {
        val source = source()

        assertTrue(source.bouncyButtons.first())

        source.saveBouncyButtons(false)

        assertFalse(source.bouncyButtons.first())
    }

    @Test
    fun `bottom bar labels default to true and persist`() = runTest {
        val source = source()

        assertTrue(source.showBottomBarLabels.first())

        source.saveShowBottomBarLabels(false)

        assertFalse(source.showBottomBarLabels.first())
    }

    @Test
    fun `language defaults to English and persists`() = runTest {
        val source = source()

        assertEquals("en", source.language.first())

        source.saveLanguage("ro")

        assertEquals("ro", source.language.first())
    }
}
