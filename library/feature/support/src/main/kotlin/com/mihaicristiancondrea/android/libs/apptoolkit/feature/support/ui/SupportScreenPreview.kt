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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.data.models.DonationProductIds
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.models.DonationOption
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.states.SupportUiState
import kotlinx.collections.immutable.persistentMapOf

@Preview(showBackground = true)
@Composable
private fun SupportScreenContentPreview() {
    MaterialTheme {
        SupportScreenContent(
            state = SupportUiState(
                donationOptions = Loadable.Ready(
                    persistentMapOf(
                        DonationProductIds.LOW_DONATION to DonationOption(
                            productId = DonationProductIds.LOW_DONATION,
                            formattedPrice = "€0.99",
                            isEligible = true,
                        ),
                        DonationProductIds.NORMAL_DONATION to DonationOption(
                            productId = DonationProductIds.NORMAL_DONATION,
                            formattedPrice = "€1.99",
                            isEligible = true,
                        ),
                        DonationProductIds.HIGH_DONATION to DonationOption(
                            productId = DonationProductIds.HIGH_DONATION,
                            formattedPrice = "€4.99",
                            isEligible = true,
                        ),
                        DonationProductIds.EXTREME_DONATION to DonationOption(
                            productId = DonationProductIds.EXTREME_DONATION,
                            formattedPrice = null,
                            isEligible = false,
                        ),
                    ),
                ),
            ),
            onEvent = {},
            onDonate = {},
            onOpenWebAd = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SupportScreenContentLoadingPreview() {
    MaterialTheme {
        SupportScreenContent(
            state = SupportUiState(donationOptions = Loadable.Loading),
            onEvent = {},
            onDonate = {},
            onOpenWebAd = {},
        )
    }
}
