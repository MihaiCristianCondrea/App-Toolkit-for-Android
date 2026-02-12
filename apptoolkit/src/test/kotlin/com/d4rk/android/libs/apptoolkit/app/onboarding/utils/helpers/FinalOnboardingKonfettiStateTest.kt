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

package com.d4rk.android.libs.apptoolkit.app.onboarding.utils.helpers

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FinalOnboardingKonfettiStateTest {

    @BeforeEach
    fun reset() {
        FinalOnboardingKonfettiState.hasKonfettiBeenShownGlobally = false
    }

    @Test
    fun `flag toggles correctly`() {
        assertThat(FinalOnboardingKonfettiState.hasKonfettiBeenShownGlobally).isFalse()
        FinalOnboardingKonfettiState.hasKonfettiBeenShownGlobally = true
        assertThat(FinalOnboardingKonfettiState.hasKonfettiBeenShownGlobally).isTrue()
    }
}
