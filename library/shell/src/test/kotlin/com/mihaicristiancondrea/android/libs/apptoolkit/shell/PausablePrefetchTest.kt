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
import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppTheme
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R as NavigationR
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.shellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.InMemoryShellPreferences
import kotlinx.serialization.Serializable
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.annotation.Config

@OptIn(ExperimentalFoundationApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class PausablePrefetchTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        ComposeFoundationFlags.isPausableCompositionInPrefetchEnabled = true
        startKoin { modules(module { single { CommonDataStore(ApplicationProvider.getApplicationContext()) } }) }
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun `the shell turns off pausing lazy items while they are prefetched`() {
        val graph = shellGraph(appTitle = CommonR.string.app_name) {
            tab(PrefetchHome, NavigationR.string.updates, ToolkitIcon.Vector(Icons.Outlined.Home)) { Text("Home") }
        }
        compose.setContent { AppTheme { ShellHost(graph = graph, preferences = InMemoryShellPreferences()) } }
        compose.waitForIdle()

        assertFalse(ComposeFoundationFlags.isPausableCompositionInPrefetchEnabled)
    }
}

@Serializable
internal data object PrefetchHome : NavKey
