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

/**
 * What the user can ask the help page's ViewModel to do.
 */
sealed interface FaqEvent {
    /** Loads the questions, on start and on retry. */
    data object Load : FaqEvent

    /**
     * Asks for the in-app review, falling back to the Play Store listing when it cannot show.
     *
     * @property host The activity the review sheet shows over, used only for this request.
     */
    data class RequestReview(val host: ReviewHost) : FaqEvent

    /** The screen opened the Play Store listing that `FaqUiState.openStoreListing` asked for. */
    data object StoreListingOpened : FaqEvent
}
