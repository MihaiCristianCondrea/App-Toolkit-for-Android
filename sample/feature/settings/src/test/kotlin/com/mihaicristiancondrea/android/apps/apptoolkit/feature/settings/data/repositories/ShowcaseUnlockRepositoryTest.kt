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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local.AppPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ShowcaseUnlockRepositoryTest {

    private val dataStore = mockk<AppPreferencesDataSource>()
    private val diagnostics = FakeTelemetryRepository()
    private val unlocked = MutableStateFlow(false)

    private fun repository(isDebugBuild: Boolean = false) = ShowcaseUnlockRepository(
        dataStore = dataStore,
        diagnostics = diagnostics,
        isDebugBuild = isDebugBuild,
    )

    private fun saving() {
        every { dataStore.componentsShowcaseUnlocked } returns unlocked
        coEvery { dataStore.saveComponentsShowcaseUnlocked(any()) } coAnswers { unlocked.value = firstArg() }
    }

    @Test
    fun `only the tap that saves a new unlock returns true`() = runTest {
        saving()
        val repository = repository()

        assertFalse(repository.unlockAfterVersionTaps(tapCount = 6))
        assertTrue(repository.unlockAfterVersionTaps(tapCount = 7))
        assertFalse(repository.unlockAfterVersionTaps(tapCount = 8))

        coVerify(exactly = 1) { dataStore.saveComponentsShowcaseUnlocked(isUnlocked = true) }
        assertEquals(listOf("Components showcase unlocked"), diagnostics.breadcrumbs.map { it.first })
    }

    @Test
    fun `the repository reports no analytics`() = runTest {
        saving()

        repository().unlockAfterVersionTaps(tapCount = 7)

        assertTrue(diagnostics.loggedEvents.isEmpty())
    }

    @Test
    fun `an installation that is already unlocked returns false and saves nothing`() = runTest {
        saving()
        unlocked.value = true

        assertFalse(repository().unlockAfterVersionTaps(tapCount = 7))

        coVerify(exactly = 0) { dataStore.saveComponentsShowcaseUnlocked(any()) }
        assertTrue(diagnostics.breadcrumbs.isEmpty())
    }

    @Test
    fun `a debug build returns false and saves nothing`() = runTest {
        saving()

        assertFalse(repository(isDebugBuild = true).unlockAfterVersionTaps(tapCount = 7))

        coVerify(exactly = 0) { dataStore.saveComponentsShowcaseUnlocked(any()) }
    }

    @Test
    fun `a failed save throws and a later tap tries again`() = runTest {
        every { dataStore.componentsShowcaseUnlocked } returns unlocked
        coEvery { dataStore.saveComponentsShowcaseUnlocked(any()) } throws IOException("disk full") andThen Unit
        val repository = repository()

        assertThrows<IOException> { repository.unlockAfterVersionTaps(tapCount = 7) }
        assertTrue(diagnostics.breadcrumbs.isEmpty())

        assertTrue(repository.unlockAfterVersionTaps(tapCount = 8))
    }

    @Test
    fun `cancellation during the save propagates and releases the lock`() = runTest {
        every { dataStore.componentsShowcaseUnlocked } returns unlocked
        val saveStarted = CompletableDeferred<Unit>()
        coEvery { dataStore.saveComponentsShowcaseUnlocked(any()) } coAnswers {
            saveStarted.complete(Unit)
            CompletableDeferred<Unit>().await()
        }
        val repository = repository()

        val tap = async { repository.unlockAfterVersionTaps(tapCount = 7) }
        runCurrent()
        saveStarted.await()
        tap.cancel()

        assertThrows<CancellationException> { tap.await() }
        assertTrue(diagnostics.breadcrumbs.isEmpty())
        coEvery { dataStore.saveComponentsShowcaseUnlocked(any()) } coAnswers { unlocked.value = firstArg() }
        assertTrue(repository.unlockAfterVersionTaps(tapCount = 8))
    }
}
