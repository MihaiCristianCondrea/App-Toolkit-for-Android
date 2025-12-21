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
