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

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation3.runtime.NavKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.DefaultDisplayPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.DefaultThemePreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeAdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultDisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DefaultThemePreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LocalEntranceCue
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.annotation.Config
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR

/** A page's entrance cue, through the shell's real page transition. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class EntranceCueNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private var cueAt: Long? = null
    private var resumedAt: Long? = null

    @Before
    fun setUp() {
        startKoin {
            modules(module {
                    single<AdsSettingsRepository> { FakeAdsSettingsRepository() }
                    single<DisplayPreferencesRepository> { DefaultDisplayPreferencesRepository(DefaultDisplayPreferencesDataSource(FakePreferencesDataStore())) }
                    single<ThemePreferencesRepository> { DefaultThemePreferencesRepository(DefaultThemePreferencesDataSource(FakePreferencesDataStore())) } })
        }
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun `a page is cued well into its transition, before it is resumed`() {
        val icon = ToolkitIcon.Vector(Icons.Outlined.Home)
        val graph = shellGraph(appTitle = CommonR.string.app_name) {
            tab(CueHomeKey, NavigationR.string.updates, icon) {
                val navigator = LocalShellNavigator.current
                Text("Open page", Modifier.clickable { navigator.navigate(CuePageKey) })
            }
            page<CuePageKey>(title = { "Page" }) {
                val cue = LocalEntranceCue.current
                val lifecycle = LocalLifecycleOwner.current.lifecycle
                LaunchedEffect(Unit) {
                    snapshotFlow { cue?.reached() == true }.first { it }
                    cueAt = compose.mainClock.currentTime
                }
                LaunchedEffect(Unit) {
                    lifecycle.currentStateFlow.first { it.isAtLeast(Lifecycle.State.RESUMED) }
                    resumedAt = compose.mainClock.currentTime
                }
                Text("Page content")
            }
        }
        compose.setContent {
            AppTheme { ShellHost(graph = graph, preferences = InMemoryShellPreferences()) }
        }
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText("Open page").fetchSemanticsNodes().isNotEmpty()
        }
        compose.waitForIdle()

        val openedAt = compose.mainClock.currentTime
        compose.onNodeWithText("Open page").performClick()
        compose.waitForIdle()

        val cued: Long = checkNotNull(cueAt) { "Never cued" }
        val resumed: Long = checkNotNull(resumedAt) { "Never resumed" }
        assertTrue("Cued only after the page resumed: cue $cued, resume $resumed", cued < resumed)
        assertTrue("Cued as the transition began: ${cued - openedAt} ms", cued - openedAt >= 150)
    }
}

@Serializable
internal data object CueHomeKey : NavKey

@Serializable
internal data object CuePageKey : NavKey
