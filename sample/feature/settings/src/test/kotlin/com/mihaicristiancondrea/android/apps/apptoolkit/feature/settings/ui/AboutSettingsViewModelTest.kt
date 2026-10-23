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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui

import androidx.lifecycle.viewModelScope
import com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local.AppPreferencesDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.data.repositories.ShowcaseUnlockRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.analytics.SHOWCASE_ACHIEVEMENT_ID
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.contracts.AboutSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class AboutSettingsViewModelTest {
    private val preferences = mockk<AppPreferencesDataSource>()
    private val unlocked = MutableStateFlow(false)
    private val telemetry = FakeTelemetryRepository()
    private val repository = ShowcaseUnlockRepository(
        dataStore = preferences,
        diagnostics = telemetry,
        isDebugBuild = false,
    )

    init {
        every { preferences.componentsShowcaseUnlocked } returns unlocked
        coEvery { preferences.saveComponentsShowcaseUnlocked(any()) } coAnswers { unlocked.value = firstArg() }
    }

    @Test
    fun `the achievement is reported once after the unlock is saved`() = runTest(dispatcher.testDispatcher) {
        val savedWhenReported = mutableListOf<Boolean>()
        val reporting = mockk<TelemetryRepository>(relaxed = true)
        every { reporting.logEvent(match { it.name == "unlock_achievement" }) } answers {
            savedWhenReported += unlocked.value
        }
        val viewModel = AboutSettingsViewModel(repository, reporting)
        viewModel.onEvent(AboutSettingsEvent.VersionTapped(6))
        assertTrue(savedWhenReported.isEmpty())
        viewModel.onEvent(AboutSettingsEvent.VersionTapped(7))
        viewModel.onEvent(AboutSettingsEvent.VersionTapped(8))
        assertEquals(listOf(true), savedWhenReported)
        verify(exactly = 1) {
            reporting.logEvent(match {
                it.name == "unlock_achievement" &&
                    it.params["achievement_id"] == AnalyticsValue.Str(SHOWCASE_ACHIEVEMENT_ID)
            })
        }
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `an already unlocked installation reports no achievement`() = runTest(dispatcher.testDispatcher) {
        unlocked.value = true
        val viewModel = AboutSettingsViewModel(repository, telemetry)
        viewModel.onEvent(AboutSettingsEvent.VersionTapped(7))
        assertTrue(achievements().isEmpty())
        assertTrue(telemetry.nonFatals.isEmpty())
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `a failed save reports a failure and a later tap can retry`() = runTest(dispatcher.testDispatcher) {
        val failure = IOException("disk full")
        coEvery { preferences.saveComponentsShowcaseUnlocked(any()) } throws failure
        val viewModel = AboutSettingsViewModel(repository, telemetry)
        viewModel.onEvent(AboutSettingsEvent.VersionTapped(7))
        assertTrue(achievements().isEmpty())
        assertEquals(listOf<Throwable>(failure), telemetry.nonFatals)
        coEvery { preferences.saveComponentsShowcaseUnlocked(any()) } coAnswers { unlocked.value = firstArg() }
        viewModel.onEvent(AboutSettingsEvent.VersionTapped(8))
        assertEquals(1, achievements().size)
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun `clearing the operation reports no achievement or failure`() = runTest(dispatcher.testDispatcher) {
        val started = CompletableDeferred<Unit>()
        coEvery { preferences.saveComponentsShowcaseUnlocked(any()) } coAnswers {
            started.complete(Unit)
            CompletableDeferred<Unit>().await()
        }
        val viewModel = AboutSettingsViewModel(repository, telemetry)
        viewModel.onEvent(AboutSettingsEvent.VersionTapped(7))
        started.await()
        viewModel.viewModelScope.cancel()
        assertTrue(achievements().isEmpty())
        assertTrue(telemetry.nonFatals.isEmpty())
    }

    private fun achievements() = telemetry.loggedEvents.filter { it.name == "unlock_achievement" }

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcher = UnconfinedDispatcherExtension()
    }
}
