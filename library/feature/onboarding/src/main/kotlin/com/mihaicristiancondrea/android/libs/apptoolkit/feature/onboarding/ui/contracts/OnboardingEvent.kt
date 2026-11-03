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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.contracts

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.models.ConsentHost

/**
 * Direction the user moved in the onboarding pager.
 */
enum class OnboardingStepDirection(val trackingLabel: String) {
    INITIAL(trackingLabel = "initial"),
    FORWARD(trackingLabel = "forward"),
    BACKWARD(trackingLabel = "backward"),
}

/**
 * How onboarding was concluded.
 */
enum class OnboardingCompletionMethod(val trackingLabel: String) {
    DONE(trackingLabel = "done"),
    SKIP(trackingLabel = "skip"),
}

/**
 * What the user can ask the onboarding screen's ViewModel to do.
 */
sealed interface OnboardingEvent {
    /** The pager settled on the page at [index], with optional [pageKey] and [direction]. */
    data class PageSelected(
        val index: Int,
        val pageKey: String? = null,
        val direction: OnboardingStepDirection = OnboardingStepDirection.INITIAL,
    ) : OnboardingEvent

    /**
     * Asks for consent, showing the form when the consent SDK requires it. The screen sends it on
     * every resume.
     *
     * @property host The activity the form shows over, used only for this request.
     */
    data class RequestConsent(val host: ConsentHost) : OnboardingEvent

    /**
     * Saves completion, then enters the shell. Sent by Skip and by Finish on the last page.
     *
     * @property method Whether completion was reached by finishing all steps or by skipping.
     */
    open class CompleteOnboarding(
        val method: OnboardingCompletionMethod = OnboardingCompletionMethod.DONE,
    ) : OnboardingEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is CompleteOnboarding) return false
            return method == other.method
        }

        override fun hashCode(): Int = method.hashCode()

        override fun toString(): String = "CompleteOnboarding(method=$method)"

        companion object : CompleteOnboarding(method = OnboardingCompletionMethod.DONE)
    }
}
