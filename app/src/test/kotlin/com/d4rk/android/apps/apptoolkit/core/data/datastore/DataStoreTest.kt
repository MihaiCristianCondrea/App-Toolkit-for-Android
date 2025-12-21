package com.d4rk.android.apps.apptoolkit.core.data.datastore

import android.app.Application
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DataStoreTest {

    @Test
    fun dataStorePersistsThemeModePreference() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val context = ApplicationProvider.getApplicationContext<Application>()
        val dispatchers = TestDispatcherProvider(dispatcher)
        val dataStore = DataStore(context = context, dispatchers = dispatchers)

        try {
            val expectedTheme = "dark"

            dataStore.saveThemeMode(mode = expectedTheme)
            val storedTheme = dataStore.themeMode.first()

            assertEquals(expectedTheme, storedTheme)
        } finally {
            dataStore.close()
            context.preferencesDataStoreFile(DataStoreNamesConstants.DATA_STORE_SETTINGS).delete()
        }
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
