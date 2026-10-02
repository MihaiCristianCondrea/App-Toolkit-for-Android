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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.models

import androidx.compose.runtime.Immutable

/**
 * One donation tier as the support page shows it, so the page renders without Play Billing types.
 *
 * @property formattedPrice The price Play formatted for the user's store, or null when it has none.
 * @property isEligible Whether Play offers a one-time purchase for this product, which enables its
 * button.
 */
@Immutable
data class DonationOption(
    val productId: String,
    val formattedPrice: String?,
    val isEligible: Boolean,
)
