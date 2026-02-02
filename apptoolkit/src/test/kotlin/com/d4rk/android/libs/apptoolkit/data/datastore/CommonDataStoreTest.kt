package com.d4rk.android.libs.apptoolkit.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import app.cash.turbine.test
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.commonDataStore
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CommonDataStoreTest {

    @Before
    fun setUp() {
        mockkStatic("com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStoreKt")
        resetSingleton()
    }

    @After
    fun tearDown() {
        unmockkAll()
        resetSingleton()
    }

    @Test
    fun `getInstance returns singleton using application context`() {
        val fakeStore = FakePreferencesDataStore()
        val context = mockk<Context>(relaxed = true)
        val appContext = mockk<Context>(relaxed = true)
        every { context.applicationContext } returns appContext
        every { appContext.applicationContext } returns appContext
        every { context.commonDataStore } returns fakeStore
        every { appContext.commonDataStore } returns fakeStore

        val first = CommonDataStore.getInstance(context)
        val second = CommonDataStore.getInstance(context)

        assertSame(first, second)
        assertSame(fakeStore, first.dataStore)
        verify(exactly = 0) { context.commonDataStore }
        verify(atLeast = 1) { appContext.commonDataStore }

        first.close()
    }

    @Test
    fun `close cancels internal coroutine scope`() = runTest {
        val dataStore = createDataStore(testScheduler)
        val scope = dataStore.extractScope()
        val job = scope.coroutineContext[Job]

        assertEquals(job?.isActive, true)

        dataStore.close()

        assertEquals(job?.isCancelled, true)
    }

    @Test
    fun `lastUsed defaults to zero and persists provided timestamp`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertEquals(0L, dataStore.lastUsed.first())

        val expected = 42L
        dataStore.saveLastUsed(expected)

        assertEquals(expected, dataStore.lastUsed.first())

        dataStore.close()
    }

    @Test
    fun `startup defaults to true and persists explicit false`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertTrue(dataStore.startup.first())

        dataStore.saveStartup(isFirstTime = false)

        assertFalse(dataStore.startup.first())

        dataStore.close()
    }

    @Test
    fun `startup page emits default when absent then updates on save`() = runTest {
        val dataStore = createDataStore(testScheduler)
        val defaultRoute = "default"
        val updatedRoute = "updated"

        dataStore.getStartupPage(defaultRoute).test {
            assertEquals(defaultRoute, awaitItem())
            dataStore.saveStartupPage(updatedRoute)
            assertEquals(updatedRoute, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        dataStore.close()
    }

    @Test
    fun `theme mode defaults to follow system and persists new selection`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertEquals(DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM, dataStore.themeMode.first())

        val expected = DataStoreNamesConstants.THEME_MODE_DARK
        dataStore.saveThemeMode(expected)

        assertEquals(expected, dataStore.themeMode.first())

        dataStore.close()
    }

    @Test
    fun `amoled mode defaults to false and toggles`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertFalse(dataStore.amoledMode.first())

        dataStore.saveAmoledMode(true)

        assertTrue(dataStore.amoledMode.first())

        dataStore.close()
    }

    @Test
    fun `dynamic colors default to true and persist`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertTrue(dataStore.dynamicColors.first())

        dataStore.saveDynamicColors(false)

        assertFalse(dataStore.dynamicColors.first())

        dataStore.close()
    }

    @Test
    fun `bouncy buttons default to true and persist`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertTrue(dataStore.bouncyButtons.first())

        dataStore.saveBouncyButtons(false)

        assertFalse(dataStore.bouncyButtons.first())

        dataStore.close()
    }

    @Test
    fun `bottom bar labels default to true and persist`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertTrue(dataStore.getShowBottomBarLabels().first())

        dataStore.saveShowLabelsOnBottomBar(false)

        assertFalse(dataStore.getShowBottomBarLabels().first())

        dataStore.close()
    }

    @Test
    fun `language defaults to English and persists`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertEquals("en", dataStore.getLanguage().first())

        val expected = "ro"
        dataStore.saveLanguage(expected)

        assertEquals(expected, dataStore.getLanguage().first())

        dataStore.close()
    }

    @Test
    fun `usage and diagnostics uses provided default then persists`() = runTest {
        val dataStore = createDataStore(testScheduler)
        val default = false

        assertFalse(dataStore.usageAndDiagnostics(default).first())

        dataStore.saveUsageAndDiagnostics(true)

        assertTrue(dataStore.usageAndDiagnostics(default).first())

        dataStore.close()
    }

    @Test
    fun `analytics consent uses default and persists`() = runTest {
        val dataStore = createDataStore(testScheduler)
        val default = true

        assertTrue(dataStore.analyticsConsent(default).first())

        dataStore.saveAnalyticsConsent(false)

        assertFalse(dataStore.analyticsConsent(default).first())

        dataStore.close()
    }

    @Test
    fun `ad storage consent uses default and persists`() = runTest {
        val dataStore = createDataStore(testScheduler)
        val default = true

        assertTrue(dataStore.adStorageConsent(default).first())

        dataStore.saveAdStorageConsent(false)

        assertFalse(dataStore.adStorageConsent(default).first())

        dataStore.close()
    }

    @Test
    fun `ad user data consent uses default and persists`() = runTest {
        val dataStore = createDataStore(testScheduler)
        val default = false

        assertFalse(dataStore.adUserDataConsent(default).first())

        dataStore.saveAdUserDataConsent(true)

        assertTrue(dataStore.adUserDataConsent(default).first())

        dataStore.close()
    }

    @Test
    fun `ad personalization consent uses default and persists`() = runTest {
        val dataStore = createDataStore(testScheduler)
        val default = false

        assertFalse(dataStore.adPersonalizationConsent(default).first())

        dataStore.saveAdPersonalizationConsent(true)

        assertTrue(dataStore.adPersonalizationConsent(default).first())

        dataStore.close()
    }

    @Test
    fun `ads flow honors default and updates state`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertTrue(dataStore.ads(default = true).first())
        assertTrue(dataStore.adsEnabledFlow.value)

        dataStore.saveAds(false)
        advanceUntilIdle()

        assertFalse(dataStore.ads(default = true).first())
        assertFalse(dataStore.adsEnabledFlow.value)

        dataStore.close()
    }

    @Test
    fun `favorite apps flow emits updated sets when toggled`() = runTest {
        val dataStore = createDataStore(testScheduler)

        dataStore.favoriteApps.test {
            assertEquals(emptySet(), awaitItem())

            dataStore.toggleFavoriteApp("com.example.first")
            advanceUntilIdle()
            assertEquals(setOf("com.example.first"), awaitItem())

            dataStore.toggleFavoriteApp("com.example.second")
            advanceUntilIdle()
            assertEquals(setOf("com.example.first", "com.example.second"), awaitItem())

            dataStore.toggleFavoriteApp("com.example.first")
            advanceUntilIdle()
            assertEquals(setOf("com.example.second"), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        dataStore.close()
    }

    @Test
    fun `session count defaults to zero and increments`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertEquals(0, dataStore.sessionCount.first())

        dataStore.incrementSessionCount()
        dataStore.incrementSessionCount()

        assertEquals(2, dataStore.sessionCount.first())

        dataStore.close()
    }

    @Test
    fun `has prompted review defaults to false and persists`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertFalse(dataStore.hasPromptedReview.first())

        dataStore.setHasPromptedReview(true)

        assertTrue(dataStore.hasPromptedReview.first())

        dataStore.close()
    }

    @Test
    fun `last seen version defaults to empty and persists`() = runTest {
        val dataStore = createDataStore(testScheduler)
        val default = ""
        val expected = "1.0.0"

        assertEquals(default, dataStore.getLastSeenVersion(default).first())

        dataStore.saveLastSeenVersion(expected)

        assertEquals(expected, dataStore.getLastSeenVersion(default).first())

        dataStore.close()
    }

    @Test
    fun `cached changelog defaults to empty and persists`() = runTest {
        val dataStore = createDataStore(testScheduler)
        val default = ""
        val expected = "New release"

        assertEquals(default, dataStore.getCachedChangelog(default).first())

        dataStore.saveCachedChangelog(expected)

        assertEquals(expected, dataStore.getCachedChangelog(default).first())

        dataStore.close()
    }

    private fun createDataStore(scheduler: TestCoroutineScheduler): CommonDataStore {
        val context = mockk<Context>(relaxed = true)
        every { context.applicationContext } returns context
        val fakeStore = FakePreferencesDataStore()
        every { context.commonDataStore } returns fakeStore
        return CommonDataStore(context, TestDispatcherProvider(StandardTestDispatcher(scheduler)))
    }

    private fun CommonDataStore.extractScope(): CoroutineScope {
        val field = CommonDataStore::class.java.getDeclaredField("scope")
        field.isAccessible = true
        return field.get(this) as CoroutineScope
    }

    private fun resetSingleton() {
        val companionField = CommonDataStore::class.java.getDeclaredField("Companion")
        companionField.isAccessible = true
        val companion = companionField.get(null)
        val instanceField = companion.javaClass.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(companion, null)
    }

    private class FakePreferencesDataStore : DataStore<Preferences> {
        private val state = MutableStateFlow(emptyPreferences())

        override val data: Flow<Preferences> = state

        override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
            val updated = transform(state.value)
            state.value = updated
            return updated
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
