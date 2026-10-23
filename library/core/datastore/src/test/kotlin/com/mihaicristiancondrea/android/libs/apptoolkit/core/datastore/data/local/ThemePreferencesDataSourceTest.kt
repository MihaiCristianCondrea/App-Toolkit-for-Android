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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThemePreferencesDataSourceTest {

    private fun source() = DefaultThemePreferencesDataSource(dataStore = FakePreferencesDataStore())

    @Test
    fun `theme mode defaults to follow system and persists new selection`() = runTest {
        val source = source()

        assertEquals(DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM, source.themeMode.first())

        source.saveThemeMode(DataStoreNamesConstants.THEME_MODE_DARK)

        assertEquals(DataStoreNamesConstants.THEME_MODE_DARK, source.themeMode.first())
    }

    @Test
    fun `amoled mode defaults to false and toggles`() = runTest {
        val source = source()

        assertFalse(source.amoledMode.first())

        source.saveAmoledMode(true)

        assertTrue(source.amoledMode.first())
    }

    @Test
    fun `dynamic colors default to true and persist`() = runTest {
        val source = source()

        assertTrue(source.dynamicColors.first())

        source.saveDynamicColors(false)

        assertFalse(source.dynamicColors.first())
    }
}
