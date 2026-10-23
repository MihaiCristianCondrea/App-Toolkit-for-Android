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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local

import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.OnboardingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultAppPreferencesDataSourceTest {

    private val onboarding = mockk<OnboardingRepository> {
        every { observeOnboardingCompletion() } returns flowOf(false)
    }
    private val display = mockk<DisplayPreferencesRepository> {
        every { startupPage(default = "home") } returns flowOf("", "apps")
    }

    private fun source() = DefaultAppPreferencesDataSource(
        dataStore = FakePreferencesDataStore(),
        displayPreferences = display,
        onboardingRepository = onboarding,
    )

    @Test
    fun `startup is pending until onboarding completes`() = runTest {
        assertTrue(source().startup.first())
    }

    @Test
    fun `components showcase starts locked and stays unlocked once saved`() = runTest {
        val source = source()

        assertFalse(source.componentsShowcaseUnlocked.first())

        source.saveComponentsShowcaseUnlocked(isUnlocked = true)

        assertTrue(source.componentsShowcaseUnlocked.first())
    }

    @Test
    fun `startup destination maps the stored route and falls back to the default`() = runTest {
        val routes = mutableListOf<String>()

        source().startupDestinationFlow(defaultRoute = "home") { route -> TestKey(route).also { routes += route } }
            .first()

        assertEquals(listOf("home"), routes)
    }

    private data class TestKey(val route: String) : NavKey
}
