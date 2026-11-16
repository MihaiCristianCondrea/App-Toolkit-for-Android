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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.analytics.SettingsAnalytics

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
