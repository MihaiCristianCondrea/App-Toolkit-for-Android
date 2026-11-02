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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingCompletionMethod
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts.OnboardingStepDirection

internal const val ONBOARDING_SCREEN_NAME: String = "Onboarding"
private const val ONBOARDING_SCREEN_CLASS: String = "OnboardingScreen"
internal val ONBOARDING_SCREEN: TrackedScreen =
    TrackedScreen(name = ONBOARDING_SCREEN_NAME, className = ONBOARDING_SCREEN_CLASS)

internal object OnboardingEventNames {
    /** Recommended event when the onboarding tutorial begins. */
    const val TUTORIAL_BEGIN: String = "tutorial_begin"

    /** Recommended event when the onboarding tutorial completes. */
    const val TUTORIAL_COMPLETE: String = "tutorial_complete"

    /** Custom event emitted when a step or page is viewed in the onboarding carousel. */
    const val STEP_VIEW: String = "onboarding_step_view"

    /** Custom action event for discrete buttons and choices. */
    const val ACTION: String = SettingsAnalytics.Events.ACTION
}

internal object OnboardingActionNames {
    const val SKIP: String = "skip"
    const val STEP_BACK: String = "step_back"
    const val THEME_CUSTOMIZED: String = "theme_customized"
}

internal object OnboardingParams {
    const val SCREEN: String = SettingsAnalytics.Params.SCREEN
    const val STEP_INDEX: String = "step_index"
    const val PAGE_KEY: String = "page_key"
    const val DIRECTION: String = "direction"
    const val METHOD: String = "method"
    const val SETTING: String = "setting"
}

internal fun onboardingTutorialBeginEvent(): AnalyticsEvent = AnalyticsEvent(
    name = OnboardingEventNames.TUTORIAL_BEGIN,
    params = mapOf(
        OnboardingParams.SCREEN to AnalyticsValue.Str(ONBOARDING_SCREEN_NAME),
    ),
)

internal fun onboardingTutorialCompleteEvent(
    method: String = OnboardingCompletionMethod.DONE.trackingLabel,
    pageIndex: Int? = null,
    pageKey: String? = null,
): AnalyticsEvent = AnalyticsEvent(
    name = OnboardingEventNames.TUTORIAL_COMPLETE,
    params = buildMap {
        put(OnboardingParams.SCREEN, AnalyticsValue.Str(ONBOARDING_SCREEN_NAME))
        put(OnboardingParams.METHOD, AnalyticsValue.Str(method))
        pageIndex?.let { put(OnboardingParams.STEP_INDEX, AnalyticsValue.LongVal(it.toLong())) }
        pageKey?.let { put(OnboardingParams.PAGE_KEY, AnalyticsValue.Str(it)) }
    },
)

internal fun onboardingStepViewEvent(
    stepIndex: Int,
    pageKey: String? = null,
    direction: String = OnboardingStepDirection.INITIAL.trackingLabel,
): AnalyticsEvent = AnalyticsEvent(
    name = OnboardingEventNames.STEP_VIEW,
    params = buildMap {
        put(OnboardingParams.SCREEN, AnalyticsValue.Str(ONBOARDING_SCREEN_NAME))
        put(OnboardingParams.STEP_INDEX, AnalyticsValue.LongVal(stepIndex.toLong()))
        put(OnboardingParams.DIRECTION, AnalyticsValue.Str(direction))
        pageKey?.let { put(OnboardingParams.PAGE_KEY, AnalyticsValue.Str(it)) }
    },
)

internal fun onboardingActionEvent(
    actionName: String,
    params: Map<String, AnalyticsValue> = emptyMap(),
): AnalyticsEvent = AnalyticsEvent(
    name = OnboardingEventNames.ACTION,
    params = buildMap {
        put(OnboardingParams.SCREEN, AnalyticsValue.Str(ONBOARDING_SCREEN_NAME))
        put(SettingsAnalytics.Params.ACTION_NAME, AnalyticsValue.Str(actionName))
        putAll(params)
    },
)

internal fun onboardingThemeCustomizedEvent(setting: String): AnalyticsEvent =
    onboardingActionEvent(
        actionName = OnboardingActionNames.THEME_CUSTOMIZED,
        params = mapOf(OnboardingParams.SETTING to AnalyticsValue.Str(setting)),
    )
