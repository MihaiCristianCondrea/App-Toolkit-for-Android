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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.analytics

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingCompletionMethod
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingStepDirection
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OnboardingAnalyticsTest {

    @Test
    fun `screen identity defines Onboarding screen and class`() {
        assertEquals("Onboarding", ONBOARDING_SCREEN.name)
        assertEquals("OnboardingScreen", ONBOARDING_SCREEN.className)
    }

    @Test
    fun `onboardingTutorialBeginEvent formats tutorial_begin with Onboarding screen`() {
        val event = onboardingTutorialBeginEvent()

        assertEquals("tutorial_begin", event.name)
        assertEquals(AnalyticsValue.Str("Onboarding"), event.params[OnboardingParams.SCREEN])
    }

    @Test
    fun `onboardingTutorialCompleteEvent formats tutorial_complete with method and step index`() {
        val doneEvent = onboardingTutorialCompleteEvent(
            method = OnboardingCompletionMethod.DONE.trackingLabel,
            pageIndex = 4,
            pageKey = "finish",
        )

        assertEquals("tutorial_complete", doneEvent.name)
        assertEquals(AnalyticsValue.Str("Onboarding"), doneEvent.params[OnboardingParams.SCREEN])
        assertEquals(AnalyticsValue.Str("done"), doneEvent.params[OnboardingParams.METHOD])
        assertEquals(AnalyticsValue.LongVal(4L), doneEvent.params[OnboardingParams.STEP_INDEX])
        assertEquals(AnalyticsValue.Str("finish"), doneEvent.params[OnboardingParams.PAGE_KEY])

        val skipEvent = onboardingTutorialCompleteEvent(
            method = OnboardingCompletionMethod.SKIP.trackingLabel,
            pageIndex = 1,
            pageKey = "theme",
        )

        assertEquals("tutorial_complete", skipEvent.name)
        assertEquals(AnalyticsValue.Str("skip"), skipEvent.params[OnboardingParams.METHOD])
        assertEquals(AnalyticsValue.LongVal(1L), skipEvent.params[OnboardingParams.STEP_INDEX])
        assertEquals(AnalyticsValue.Str("theme"), skipEvent.params[OnboardingParams.PAGE_KEY])
    }

    @Test
    fun `onboardingStepViewEvent formats onboarding_step_view with expected parameters`() {
        val event = onboardingStepViewEvent(
            stepIndex = 2,
            pageKey = "quick_settings",
            direction = OnboardingStepDirection.FORWARD.trackingLabel,
        )

        assertEquals("onboarding_step_view", event.name)
        assertEquals(AnalyticsValue.Str("Onboarding"), event.params[OnboardingParams.SCREEN])
        assertEquals(AnalyticsValue.LongVal(2L), event.params[OnboardingParams.STEP_INDEX])
        assertEquals(AnalyticsValue.Str("quick_settings"), event.params[OnboardingParams.PAGE_KEY])
        assertEquals(AnalyticsValue.Str("forward"), event.params[OnboardingParams.DIRECTION])
    }

    @Test
    fun `onboardingThemeCustomizedEvent formats settings_action with setting parameter`() {
        val event = onboardingThemeCustomizedEvent(setting = "theme_mode")

        assertEquals("settings_action", event.name)
        assertEquals(AnalyticsValue.Str("Onboarding"), event.params[OnboardingParams.SCREEN])
        assertEquals(
            AnalyticsValue.Str(OnboardingActionNames.THEME_CUSTOMIZED),
            event.params[SettingsAnalytics.Params.ACTION_NAME],
        )
        assertEquals(AnalyticsValue.Str("theme_mode"), event.params[OnboardingParams.SETTING])
    }

    @Test
    fun `all action names are unique and match GA4 naming conventions`() {
        val actions = listOf(
            OnboardingActionNames.SKIP,
            OnboardingActionNames.STEP_BACK,
            OnboardingActionNames.THEME_CUSTOMIZED,
        )

        assertEquals(actions.distinct().size, actions.size, "Action names must be unique")

        val validIdentifierRegex = Regex("^[a-z][a-z0-9_]{1,39}$")
        for (action in actions) {
            assertTrue(
                validIdentifierRegex.matches(action),
                "Action name '$action' must be valid GA4 parameter format",
            )
        }
    }

    @Test
    fun `events never contain sensitive or unbounded parameter keys`() {
        val forbiddenKeys = setOf("email", "user_id", "message", "query", "url", "path")
        val stepEvent = onboardingStepViewEvent(stepIndex = 0, pageKey = "welcome")
        val completeEvent = onboardingTutorialCompleteEvent(method = "done", pageIndex = 4)

        for (key in forbiddenKeys) {
            assertFalse(stepEvent.params.containsKey(key))
            assertFalse(completeEvent.params.containsKey(key))
        }
    }
}
