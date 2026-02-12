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

package com.d4rk.android.libs.apptoolkit.app.review.domain.model

/**
 * Result of an in-app review request.
 */
sealed interface ReviewOutcome {
    /** The review flow was launched successfully. */
    data object Launched : ReviewOutcome

    /** The review flow was not launched because eligibility conditions were not met. */
    data object NotEligible : ReviewOutcome

    /** The review flow is unavailable on the current device/install. */
    data object Unavailable : ReviewOutcome

    /** The review flow was attempted but failed to launch. */
    data object Failed : ReviewOutcome
}
