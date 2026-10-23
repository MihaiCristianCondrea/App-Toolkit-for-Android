package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import java.io.IOException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class AdsPreferenceRecoveryTest {
    @BeforeEach
    fun prepareLogs() {
        mockkStatic(Log::class)
        every { Log.w(any(), any<String>(), any()) } returns 0
        every { Log.e(any(), any<String>(), any()) } returns 0
    }

    @AfterEach
    fun restoreLogs() = unmockkStatic(Log::class)

    @Test
    fun `a delayed opt out never publishes the enabled default`() = runTest {
        val readReady = CompletableDeferred<Unit>()
        val source = source(StandardTestDispatcher(testScheduler), flow {
            readReady.await()
            emit(preferencesOf(booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_ADS) to false))
            awaitCancellation()
        })
        try {
            assertFalse(source.adsEnabled.value)
            runCurrent()
            assertFalse(source.adsEnabled.value)
            readReady.complete(Unit)
            runCurrent()
            assertFalse(source.adsEnabled.value)
        } finally {
            source.close()
        }
    }

    @Test
    fun `an absent preference enables ads only after storage answers`() = runTest {
        val readReady = CompletableDeferred<Unit>()
        val source = source(StandardTestDispatcher(testScheduler), flow {
            readReady.await()
            emit(emptyPreferences())
            awaitCancellation()
        })
        try {
            runCurrent()
            assertFalse(source.adsEnabled.value)
            readReady.complete(Unit)
            runCurrent()
            assertTrue(source.adsEnabled.value)
        } finally {
            source.close()
        }
    }

    @Test
    fun `a transient read failure disables ads and reconnects to storage`() = runTest {
        var reads = 0
        val source = source(StandardTestDispatcher(testScheduler), flow {
            if (reads++ == 0) {
                emit(emptyPreferences())
                throw IOException("storage temporarily unavailable")
            }
            emit(emptyPreferences())
            awaitCancellation()
        })
        try {
            runCurrent()
            assertFalse(source.adsEnabled.value)
            verify(exactly = 1) { Log.w(any(), any<String>(), ofType<IOException>()) }
            advanceTimeBy(1_000)
            runCurrent()
            assertTrue(source.adsEnabled.value)
        } finally {
            source.close()
        }
    }

    @Test
    fun `an unexpected read failure is logged and leaves ads disabled`() = runTest {
        val source = source(StandardTestDispatcher(testScheduler), flow {
            throw IllegalStateException("unexpected storage failure")
        })
        try {
            runCurrent()
            assertFalse(source.adsEnabled.value)
            verify(exactly = 1) { Log.e(any(), any<String>(), ofType<IllegalStateException>()) }
        } finally {
            source.close()
        }
    }

    private fun source(dispatcher: CoroutineDispatcher, values: Flow<Preferences>): DefaultAdsPreferencesDataSource {
        val store = object : DataStore<Preferences> {
            override val data = values
            override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences =
                error("This test only reads preferences")
        }
        val dispatchers = object : DispatcherProvider {
            override val io = dispatcher
            override val default = dispatcher
            override val main = dispatcher
            override val unconfined = dispatcher
        }
        return DefaultAdsPreferencesDataSource(store, dispatchers, defaultAdsEnabled = true)
    }
}
