/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants.AppAdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.StandardDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.ClipboardRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.InstalledAppsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.DefaultDisplayPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.DefaultThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultDisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeAdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.robolectric.annotation.Config

/** The reference ScreenHost migration: the real Apps tab, its ViewModel and its reporting. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w411dp-h891dp")
class AppsListScreenTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val telemetry = FakeTelemetryRepository()
    private val apps = listOf(
        AppSummary(
            name = "App Toolkit",
            packageName = "com.mihaicristiancondrea.apptoolkit",
            iconUrl = "https://example.com/icon.png",
            category = AppCategory(label = "Tools", id = "tools"),
        ),
    )

    @Before
    fun setUp() {
        startKoin {
            modules(
                module {
                    single<InstalledAppsRepository> { FakeInstalledAppsRepository() }
                    single<ClipboardRepository> { mockk(relaxed = true) }
                    viewModel {
                        AppsListViewModel(
                            developerAppsRepository = FakeDeveloperAppsRepository(apps = apps),
                            installedAppsRepository = get(),
                            favoritesRepository = FakeFavoritesRepository(),
                            telemetryRepository = telemetry,
                        )
                    }

                    single<AdsSettingsRepository> { FakeAdsSettingsRepository() }
                    single<DisplayPreferencesRepository> { DefaultDisplayPreferencesRepository(DefaultDisplayPreferencesDataSource(FakePreferencesDataStore())) }
                    single<ThemePreferencesRepository> { DefaultThemePreferencesRepository(DefaultThemePreferencesDataSource(FakePreferencesDataStore())) }
                    single<DispatcherProvider> { StandardDispatchers() }
                    single(named(AppAdsQualifiers.APPS_LIST_NATIVE_AD)) { AdsConfig() }
                    single(named(AppAdsQualifiers.APP_DETAILS_NATIVE_AD)) { AdsConfig() }
                },
            )
        }
    }

    @After
    fun tearDown() = stopKoin()

    private fun show() {
        compose.setContent {
            AppTheme {
                CompositionLocalProvider(LocalTelemetry provides telemetry) { AppsListScreen() }
            }
        }
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("App Toolkit").fetchSemanticsNodes().isNotEmpty()
        }
        compose.waitForIdle()
    }

    private fun states(): List<String> = telemetry.loggedEvents
        .filter { it.name == "screen_state" }
        .map { (it.params.getValue("state") as AnalyticsValue.Str).value }

    private fun count(event: String): Int = telemetry.loggedEvents.count { it.name == event }

    @Test
    fun `the Apps tab reports one visit and its loading status without repeats`() {
        show()

        val screen = AppScreenTracking.Screens.APPS_LIST
        assertEquals(listOf(screen.name to screen.className), telemetry.loggedScreenViews)
        assertEquals("success", states().last())
        assertTrue("a label repeated", states().zipWithNext().none { (previous, next) -> previous == next })
        assertEquals(1, count("view_item_list"))
    }

    @Test
    fun `opening an app's details is an interaction, not a new screen`() {
        show()

        compose.onAllNodesWithText("App Toolkit").onFirst().performClick()
        compose.waitForIdle()

        assertEquals(1, telemetry.loggedScreenViews.size)
        assertEquals(1, count("view_item"))
        assertEquals(1, count("view_item_list"))
    }

    @Test
    fun `returning from the background is a new visit, with no repeated catalogue impression`() {
        show()

        compose.activityRule.scenario.moveToState(Lifecycle.State.CREATED)
        compose.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        compose.waitForIdle()

        assertEquals(2, telemetry.loggedScreenViews.size)
        assertEquals(1, count("view_item_list"))
    }
}
