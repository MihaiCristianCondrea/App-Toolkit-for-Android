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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.contracts

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.models.ReviewHost

/** Requests and acknowledgments handled by the FAQ ViewModel. */
sealed interface FaqEvent {
    /** Loads questions on entry or retries after an empty or failed result. */
    data object Load : FaqEvent

    /**
     * Requests an in-app review and uses the store listing as a fallback.
     *
     * @property host Activity-backed host for the review prompt.
     */
    data class RequestReview(val host: ReviewHost) : FaqEvent

    /** Acknowledges that the screen handled the pending Play Store listing request. */
    data object StoreListingOpened : FaqEvent
}
