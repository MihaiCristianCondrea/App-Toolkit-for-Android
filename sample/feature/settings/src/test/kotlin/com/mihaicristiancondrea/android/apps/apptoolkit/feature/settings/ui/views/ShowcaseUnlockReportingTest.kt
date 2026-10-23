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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.views

import com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local.AppPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.data.repositories.ShowcaseUnlockRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ShowcaseUnlockReportingTest {

    private val dataStore = mockk<AppPreferencesDataSource>()
    private val unlocked = MutableStateFlow(false)
    private val repository = ShowcaseUnlockRepository(
        dataStore = dataStore,
        diagnostics = FakeTelemetryRepository(),
        isDebugBuild = false,
    )

    init {
        every { dataStore.componentsShowcaseUnlocked } returns unlocked
        coEvery { dataStore.saveComponentsShowcaseUnlocked(any()) } coAnswers { unlocked.value = firstArg() }
    }

    @Test
    fun `the achievement is reported once, after the unlock is saved`() = runTest {
        val savedWhenReported = mutableListOf<Boolean>()
        val telemetry = mockk<TelemetryRepository>(relaxed = true)
        every { telemetry.logEvent(any()) } answers { savedWhenReported += unlocked.value }

        unlockShowcase(repository, tapCount = 6, telemetry = telemetry)
        unlockShowcase(repository, tapCount = 7, telemetry = telemetry)
        unlockShowcase(repository, tapCount = 8, telemetry = telemetry)

        assertEquals(listOf(true), savedWhenReported)
        verify(exactly = 1) {
            telemetry.logEvent(
                match {
                    it.name == "unlock_achievement" &&
                        it.params["achievement_id"] == AnalyticsValue.Str(SHOWCASE_ACHIEVEMENT_ID)
                },
            )
        }
    }

    @Test
    fun `an installation that is already unlocked reports nothing`() = runTest {
        unlocked.value = true
        val telemetry = FakeTelemetryRepository()

        unlockShowcase(repository, tapCount = 7, telemetry = telemetry)

        assertTrue(telemetry.loggedEvents.isEmpty())
        assertTrue(telemetry.nonFatals.isEmpty())
    }

    @Test
    fun `a failed save is a non-fatal, not an achievement`() = runTest {
        val failure = IOException("disk full")
        coEvery { dataStore.saveComponentsShowcaseUnlocked(any()) } throws failure
        val telemetry = FakeTelemetryRepository()

        unlockShowcase(repository, tapCount = 7, telemetry = telemetry)

        assertTrue(telemetry.loggedEvents.isEmpty())
        assertEquals(listOf<Throwable>(failure), telemetry.nonFatals)
    }

    @Test
    fun `cancellation reports neither and propagates`() = runTest {
        val saveStarted = CompletableDeferred<Unit>()
        coEvery { dataStore.saveComponentsShowcaseUnlocked(any()) } coAnswers {
            saveStarted.complete(Unit)
            CompletableDeferred<Unit>().await()
        }
        val telemetry = FakeTelemetryRepository()

        val tap = async { unlockShowcase(repository, tapCount = 7, telemetry = telemetry) }
        runCurrent()
        saveStarted.await()
        tap.cancel()

        assertThrows<CancellationException> { tap.await() }
        assertTrue(telemetry.loggedEvents.isEmpty())
        assertTrue(telemetry.nonFatals.isEmpty())
    }
}
