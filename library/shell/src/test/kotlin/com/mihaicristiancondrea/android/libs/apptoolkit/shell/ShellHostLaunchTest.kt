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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.DefaultDisplayPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.DefaultThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultDisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeAdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.data.repositories.InMemoryShellPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.data.repositories.ShellPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellSettings
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xhdpi")
class ShellHostLaunchTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        startKoin {
            modules(
                module {
                    single<AdsSettingsRepository> { FakeAdsSettingsRepository() }
                    single<DisplayPreferencesRepository> {
                        DefaultDisplayPreferencesRepository(DefaultDisplayPreferencesDataSource(FakePreferencesDataStore()))
                    }
                    single<ThemePreferencesRepository> {
                        DefaultThemePreferencesRepository(DefaultThemePreferencesDataSource(FakePreferencesDataStore()))
                    }
                },
            )
        }
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun `a recreated host draws at once without deciding the start again`() {
        var resolved = 0
        val restoration = StateRestorationTester(compose)
        restoration.setContent {
            AppTheme {
                ShellHost(
                    graph = graph(),
                    preferences = InMemoryShellPreferencesRepository(ShellSettings()),
                    resolveStart = {
                        resolved++
                        null
                    },
                )
            }
        }
        compose.waitForIdle()
        compose.onNodeWithText("Home content").assertExists()
        assertEquals(1, resolved)

        restoration.emulateSavedInstanceStateRestore()
        compose.waitForIdle()

        compose.onNodeWithText("Home content").assertExists()
        assertEquals(1, resolved)
    }

    @Test
    fun `onReady is called once while the host stays composed`() {
        var ready = 0
        compose.setContent {
            AppTheme {
                ShellHost(
                    graph = graph(),
                    preferences = InMemoryShellPreferencesRepository(ShellSettings()),
                    onReady = { ready++ },
                )
            }
        }
        compose.waitForIdle()

        assertEquals(1, ready)
    }

    @Test
    fun `an intent that arrives while the start is deciding opens its destination once ready`() {
        val release = CompletableDeferred<Unit>()
        compose.setContent {
            AppTheme {
                ShellHost(
                    graph = graph(),
                    preferences = InMemoryShellPreferencesRepository(ShellSettings()),
                    resolveStart = {
                        release.await()
                        null
                    },
                )
            }
        }
        compose.waitForIdle()
        compose.onAllNodesWithText("Home content").assertCountEqualsZero()

        InstrumentationRegistry.getInstrumentation()
            .callActivityOnNewIntent(compose.activity, Intent(OpenDetailAction))
        compose.waitForIdle()
        release.complete(Unit)
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("Detail content").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun `a resolveStart storage failure falls back to the default start and calls onReady`() {
        var ready = 0
        compose.setContent {
            AppTheme {
                ShellHost(
                    graph = graph(),
                    preferences = InMemoryShellPreferencesRepository(ShellSettings()),
                    resolveStart = { throw StorageException(reason = StorageException.Reason.FAILED) },
                    onReady = { ready++ },
                )
            }
        }
        compose.waitForIdle()

        compose.onNodeWithText("Home content").assertExists()
        assertEquals(1, ready)
    }

    @Test
    fun `a shell settings storage failure falls back to defaults and calls onReady`() {
        var ready = 0
        val failingPreferences = object : ShellPreferencesRepository {
            override val settings = flow<ShellSettings> {
                throw StorageException(reason = StorageException.Reason.FAILED)
            }

            override suspend fun update(transform: (ShellSettings) -> ShellSettings) = Unit
        }
        compose.setContent {
            AppTheme {
                ShellHost(
                    graph = graph(),
                    preferences = failingPreferences,
                    onReady = { ready++ },
                )
            }
        }
        compose.waitForIdle()

        compose.onNodeWithText("Home content").assertExists()
        assertEquals(1, ready)
    }

    @Test
    fun `a host recreated while settings are still loading still applies resolveStart`() {
        val releaseSettings = CompletableDeferred<ShellSettings>()
        val pendingPreferences = object : ShellPreferencesRepository {
            override val settings = flow { emit(releaseSettings.await()) }
            override suspend fun update(transform: (ShellSettings) -> ShellSettings) = Unit
        }
        val restoration = StateRestorationTester(compose)
        restoration.setContent {
            AppTheme {
                ShellHost(
                    graph = graph(),
                    preferences = pendingPreferences,
                    resolveStart = { LaunchDetailKey },
                )
            }
        }
        compose.waitForIdle()
        compose.onAllNodesWithText("Detail content").assertCountEqualsZero()

        restoration.emulateSavedInstanceStateRestore()
        releaseSettings.complete(ShellSettings())
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("Detail content").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun `restoring after adding a tab resets search state instead of crashing`() {
        var includeSecondTab = false
        val restoration = StateRestorationTester(compose)
        restoration.setContent {
            AppTheme {
                ShellHost(
                    graph = graph(includeSecondTab = includeSecondTab),
                    preferences = InMemoryShellPreferencesRepository(ShellSettings()),
                )
            }
        }
        compose.waitForIdle()
        compose.onNodeWithText("Home content").assertExists()

        includeSecondTab = true
        restoration.emulateSavedInstanceStateRestore()
        compose.waitForIdle()

        compose.onNodeWithText(compose.activity.getString(NavigationR.string.settings)).performClick()
        compose.waitForIdle()

        compose.onNodeWithText("Second content").assertExists()
    }

    private fun androidx.compose.ui.test.SemanticsNodeInteractionCollection.assertCountEqualsZero() {
        assertEquals(0, fetchSemanticsNodes().size)
    }

    private fun graph(includeSecondTab: Boolean = false) = shellGraph(appTitle = CommonR.string.app_name) {
        tab(LaunchHomeKey, NavigationR.string.updates, ToolkitIcon.Vector(Icons.Outlined.Home)) {
            Text("Home content")
        }
        if (includeSecondTab) {
            tab(LaunchSecondKey, NavigationR.string.settings, ToolkitIcon.Vector(Icons.Outlined.Home)) {
                Text("Second content")
            }
        }
        page<LaunchDetailKey>(title = { "Detail" }) { Text("Detail content") }
        deepLinks { action(OpenDetailAction) { LaunchDetailKey } }
    }

    private companion object {
        const val OpenDetailAction = "com.mihaicristiancondrea.android.libs.apptoolkit.shell.OPEN_DETAIL"
    }
}

@Serializable
internal data object LaunchHomeKey : NavKey

@Serializable
internal data object LaunchSecondKey : NavKey

@Serializable
internal data object LaunchDetailKey : NavKey
