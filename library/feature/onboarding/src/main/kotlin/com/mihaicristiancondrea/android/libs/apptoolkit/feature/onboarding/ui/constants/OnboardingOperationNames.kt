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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.constants

/** Stable identifiers for onboarding operation reporting. */
internal object OnboardingOperationNames {
    const val OBSERVE_COMPLETION: String = "observeCompletion"
    const val COMPLETE_ONBOARDING: String = "completeOnboarding"
    const val REQUEST_CONSENT: String = "requestConsent"
    const val OBSERVE_PREFERENCES: String = "observePreferences"
    const val PERSIST_ONBOARDING_THEME: String = "persistOnboardingTheme"

    object ExtraKeys {
        const val SETTING: String = "setting"
    }
}
