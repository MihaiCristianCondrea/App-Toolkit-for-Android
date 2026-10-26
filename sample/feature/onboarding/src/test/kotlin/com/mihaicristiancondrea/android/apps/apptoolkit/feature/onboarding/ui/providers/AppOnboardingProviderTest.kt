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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.onboarding.ui.providers

import android.content.Context
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.models.OnboardingPage
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AppOnboardingProviderTest {
    @Test
    fun `consent is shown after quick settings and before completion`() {
        val pages = AppOnboardingProvider().getOnboardingPages(mockk<Context>(relaxed = true))
        val keys = pages.map { page ->
            when (page) {
                is OnboardingPage.DefaultPage -> page.key
                is OnboardingPage.CustomPage -> page.key
            }
        }
        assertEquals(listOf("welcome", "theme_options", "quick_settings", "crashlytics_options", "onboarding_complete"), keys)
        assertIs<OnboardingPage.CustomPage>(pages[3])
    }
}
