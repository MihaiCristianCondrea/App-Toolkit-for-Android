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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.data.local.datastore

import android.app.Application
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
@Disabled(
    "Needs Robolectric's runner for ApplicationProvider, and this build runs the JUnit platform " +
            "with no vintage engine, so @RunWith is never honoured. The test has therefore never " +
            "executed — it was silently skipped while it shared :sample with JUnit 5 suites, and only " +
            "surfaced once :sample:core:datastore made it the module's only test. Re-enable by adding " +
            "a Robolectric JUnit 5 integration, or by taking the DataStore file path as a parameter " +
            "so no Android context is needed."
)
class DataStoreTest {

    @Test
    fun dataStorePersistsThemeModePreference() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val context = ApplicationProvider.getApplicationContext<Application>()
        val dispatchers = TestDispatcherProvider(dispatcher)
        val dataStore = CommonDataStore(context = context)

        val dataStoreFile =
            context.preferencesDataStoreFile(DataStoreNamesConstants.DATA_STORE_SETTINGS)

        val expectedTheme = "dark"

        val storedTheme = runCatching {
            dataStore.saveThemeMode(mode = expectedTheme)
            dataStore.themeMode.first()
        }.also {
            runCatching { dataStore.close() }
            runCatching { dataStoreFile.delete() }
        }.getOrThrow()

        assertEquals(expectedTheme, storedTheme)
    }

    private class TestDispatcherProvider(
        private val dispatcher: CoroutineDispatcher,
    ) : DispatcherProvider {
        override val main: CoroutineDispatcher get() = dispatcher
        override val io: CoroutineDispatcher get() = dispatcher
        override val default: CoroutineDispatcher get() = dispatcher
        override val unconfined: CoroutineDispatcher get() = dispatcher
    }
}
