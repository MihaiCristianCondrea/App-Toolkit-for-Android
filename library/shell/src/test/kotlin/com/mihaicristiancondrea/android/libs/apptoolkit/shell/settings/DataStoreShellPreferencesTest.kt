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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.TabTransitionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.data.repositories.DataStoreShellPreferencesRepository
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class DataStoreShellPreferencesTest {

    @TempDir
    lateinit var directory: File

    private val jobs = mutableListOf<Job>()

    /** The app's defaults, each away from the Toolkit's. */
    private val appDefaults = ShellSettings(
        backEdgeStyle = BackEdgeStyle.FollowFinger,
        tabTransition = TabTransitionStyle.Fade,
        hideTopBarOnScroll = true,
    )

    /**
     * A store over the same file each time it is called, the way a new app version reads what the
     * previous one saved. Only one DataStore may be active per file, so each call closes the last.
     */
    private suspend fun store(defaults: ShellSettings): DataStoreShellPreferencesRepository {
        jobs.forEach { it.cancelAndJoin() }
        jobs.clear()
        val job = SupervisorJob().also(jobs::add)
        val scope = CoroutineScope(Dispatchers.IO + job)
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(scope = scope) {
            File(directory, "shell_settings.preferences_pb")
        }
        return DataStoreShellPreferencesRepository(dataStore, defaults)
    }

    @AfterEach
    fun closeStores() = jobs.forEach { it.cancel() }

    @Test
    fun `a first launch uses the app's defaults`() = runTest {
        assertEquals(appDefaults, store(appDefaults).settings.first())
    }

    @Test
    fun `no app defaults means the Toolkit's`() = runTest {
        assertEquals(ShellSettings(), store(ShellSettings()).settings.first())
    }

    @Test
    fun `a saved choice wins over the app's defaults`() = runTest {
        store(appDefaults).setBackEdgeStyle(BackEdgeStyle.System)

        assertEquals(BackEdgeStyle.System, store(appDefaults).settings.first().backEdgeStyle)
    }

    @Test
    fun `a choice equal to the current default is kept when the default changes`() = runTest {
        store(appDefaults).setBackEdgeStyle(BackEdgeStyle.FollowFinger)

        val nextRelease = appDefaults.copy(backEdgeStyle = BackEdgeStyle.System)

        assertEquals(BackEdgeStyle.FollowFinger, store(nextRelease).settings.first().backEdgeStyle)
    }

    @Test
    fun `settings nobody chose follow the app's changed defaults`() = runTest {
        store(appDefaults).setNavigationTint(NavigationTint.None)

        val nextRelease = appDefaults.copy(tabTransition = TabTransitionStyle.Directional)
        val settings = store(nextRelease).settings.first()

        assertEquals(TabTransitionStyle.Directional, settings.tabTransition)
        assertEquals(NavigationTint.None, settings.navigationTint)
    }

    @Test
    fun `update saves only the settings it changes`() = runTest {
        store(appDefaults).update { it.copy(hideBottomBarOnScroll = false) }

        val nextRelease = appDefaults.copy(hideTopBarOnScroll = false)
        val settings = store(nextRelease).settings.first()

        assertEquals(false, settings.hideBottomBarOnScroll)
        assertEquals(false, settings.hideTopBarOnScroll)
    }

    @Test
    fun `reset returns to the app's defaults and forgets every choice`() = runTest {
        val preferences = store(appDefaults)
        preferences.setBackEdgeStyle(BackEdgeStyle.System)
        preferences.setLimitContentWidth(false)
        preferences.setStartOverride(1)

        preferences.resetDeveloperOptions()

        assertEquals(appDefaults, preferences.settings.first())
        val nextRelease = appDefaults.copy(backEdgeStyle = BackEdgeStyle.System, limitContentWidth = false)
        assertEquals(nextRelease, store(nextRelease).settings.first())
    }
}
