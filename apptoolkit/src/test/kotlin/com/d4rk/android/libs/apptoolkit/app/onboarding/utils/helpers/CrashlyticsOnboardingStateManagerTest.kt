package com.d4rk.android.libs.apptoolkit.app.onboarding.utils.helpers

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CrashlyticsOnboardingStateManagerTest {

    @BeforeEach
    fun resetState() {
        CrashlyticsOnboardingStateManager.openDialog()
    }

    @Test
    fun `dialog visible by default`() {
        assertThat(CrashlyticsOnboardingStateManager.showCrashlyticsDialog).isTrue()
    }

    @Test
    fun `open and dismiss dialog update state`() {
        CrashlyticsOnboardingStateManager.dismissDialog()
        assertThat(CrashlyticsOnboardingStateManager.showCrashlyticsDialog).isFalse()
        CrashlyticsOnboardingStateManager.openDialog()
        assertThat(CrashlyticsOnboardingStateManager.showCrashlyticsDialog).isTrue()
    }

    @Test
    fun `repeated open and dismiss calls`() {
        CrashlyticsOnboardingStateManager.openDialog()
        CrashlyticsOnboardingStateManager.openDialog()
        assertThat(CrashlyticsOnboardingStateManager.showCrashlyticsDialog).isTrue()
        CrashlyticsOnboardingStateManager.dismissDialog()
        CrashlyticsOnboardingStateManager.dismissDialog()
        assertThat(CrashlyticsOnboardingStateManager.showCrashlyticsDialog).isFalse()
    }
}
