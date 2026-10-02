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

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost

/**
 * What the user can ask the onboarding screen's ViewModel to do.
 */
sealed interface OnboardingEvent {
    /** The pager settled on the page at [index]. */
    data class PageSelected(val index: Int) : OnboardingEvent

    /**
     * Asks for consent, showing the form when the consent SDK requires it. The screen sends it on
     * every resume.
     *
     * @property host The activity the form shows over, used only for this request.
     */
    data class RequestConsent(val host: ConsentHost) : OnboardingEvent

    /** Saves completion, then enters the shell. Sent by Skip and by Finish on the last page. */
    data object CompleteOnboarding : OnboardingEvent
}
