/*
 * Copyright (Γö¼ΓîÉ) 2026 Mihai-Cristian Condrea
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

package com.mihaicristiancondrea.android.libs.apptoolkit.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.OnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.datastore.DataStoreNamesConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.di.DataStoreQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.local.ChangelogPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.di.changelogModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.di.onboardingModule
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.local.AdsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.di.adsIntegrationModule
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.data.local.ReviewPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.di.reviewModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.assertEquals
import kotlin.test.assertSame

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class PreferenceSourceOwnershipTest {
    @Test
    fun `owner modules share the injected settings file and retain persisted key names`() = runTest {
        val store = FakePreferencesDataStore()
        val hostStore = FakePreferencesDataStore()
        val graph = koinApplication {
            modules(
                module {
                    single<DataStore<Preferences>>(named(DataStoreQualifiers.SETTINGS)) { store }
                    single<DataStore<Preferences>> { hostStore }
                    single<DispatcherProvider> { TestDispatchers(StandardTestDispatcher(testScheduler)) }
                    single<DiagnosticsReporter> { FakeTelemetryRepository() }
                },
                adsIntegrationModule(defaultAdsEnabled = false), reviewModule, changelogModule, onboardingModule,
            )
        }
        try {
            val ads = graph.koin.get<AdsPreferencesDataSource>()
            val settings = graph.koin.get<AdsSettingsRepository>()
            assertSame(ads.adsEnabled, settings.adsEnabled)
            assertEquals(false, ads.defaultAdsEnabled)
            runCurrent()
            assertEquals(false, settings.adsEnabled.value)
            settings.setAdsEnabled(false)
            graph.koin.get<OnboardingRepository>().setOnboardingCompleted()
            graph.koin.get<ReviewPreferencesDataSource>().incrementSessionCount()
            graph.koin.get<ChangelogPreferencesDataSource>().saveLastSeenVersion("3.0.0")

            assertEquals(emptyPreferences(), hostStore.data.first())
            val saved = store.data.first()
            assertEquals(false, saved[booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_ADS)])
            assertEquals(false, saved[booleanPreferencesKey(DataStoreNamesConstants.DATA_STORE_STARTUP)])
            assertEquals(1L, saved[longPreferencesKey(DataStoreNamesConstants.DATA_STORE_SESSION_COUNT)])
            assertEquals("3.0.0", saved[stringPreferencesKey(DataStoreNamesConstants.DATA_STORE_LAST_SEEN_VERSION)])
        } finally {
            graph.close()
        }
    }

}
