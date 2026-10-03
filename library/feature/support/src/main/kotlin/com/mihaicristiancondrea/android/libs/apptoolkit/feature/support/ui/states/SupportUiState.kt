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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.models.DonationOption
import kotlinx.collections.immutable.ImmutableMap

/**
 * Everything the support page renders.
 *
 * @property donationOptions Every donation tier keyed by product id, including tiers Play did not
 * return, which show as unavailable. Stays [Loadable.Loading] until the first product query
 * answers, and is [Loadable.Empty] when Play returned no products.
 * @property isBillingInProgress A purchase this page started is waiting for Play's result, so the
 * donation buttons are disabled.
 */
@Immutable
data class SupportUiState(
    val donationOptions: Loadable<ImmutableMap<String, DonationOption>> = Loadable.Loading,
    val isBillingInProgress: Boolean = false,
)
