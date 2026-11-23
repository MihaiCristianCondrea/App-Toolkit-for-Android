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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.mappers

import com.android.billingclient.api.ProductDetails
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.GenericErrorText
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.data.models.DonationProductIds
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.models.DonationOption
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.models.PurchaseResult
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

class SupportMappersTest {

    @Test
    fun `toDonationOptions marks products with only oneTimePurchaseOfferDetailsList as eligible`() {
        val listOffer: ProductDetails.OneTimePurchaseOfferDetails = mockk(relaxed = true) {
            every { formattedPrice } returns "€2.99"
        }
        val details: ProductDetails = mockk(relaxed = true) {
            every { oneTimePurchaseOfferDetailsList } returns listOf(listOffer)
            every { oneTimePurchaseOfferDetails } returns null
        }

        val options = mapOf(DonationProductIds.LOW_DONATION to details).toDonationOptions(
            productIds = listOf(DonationProductIds.LOW_DONATION, DonationProductIds.NORMAL_DONATION),
        )

        assertEquals(
            DonationOption(
                productId = DonationProductIds.LOW_DONATION,
                formattedPrice = "€2.99",
                isEligible = true,
            ),
            options[DonationProductIds.LOW_DONATION],
        )
        assertEquals(
            DonationOption(
                productId = DonationProductIds.NORMAL_DONATION,
                formattedPrice = null,
                isEligible = false,
            ),
            options[DonationProductIds.NORMAL_DONATION],
        )
    }

    @Test
    fun `toDonationOptions falls back to legacy oneTimePurchaseOfferDetails when offer list is empty`() {
        val legacyOffer: ProductDetails.OneTimePurchaseOfferDetails = mockk(relaxed = true) {
            every { formattedPrice } returns "€0.99"
        }
        val details: ProductDetails = mockk(relaxed = true) {
            every { oneTimePurchaseOfferDetailsList } returns emptyList()
            every { oneTimePurchaseOfferDetails } returns legacyOffer
        }

        val options = mapOf(DonationProductIds.LOW_DONATION to details).toDonationOptions(
            productIds = listOf(DonationProductIds.LOW_DONATION),
        )

        assertEquals(
            DonationOption(
                productId = DonationProductIds.LOW_DONATION,
                formattedPrice = "€0.99",
                isEligible = true,
            ),
            options[DonationProductIds.LOW_DONATION],
        )
    }

    @Test
    fun `toUiText maps network failures to the connection error string and others to the generic error`() {
        assertEquals(
            UiText.StringResource(resourceId = CoreUiR.string.screen_error_connection),
            PurchaseResult.Failed.Reason.NETWORK.toUiText(),
        )
        listOf(
            PurchaseResult.Failed.Reason.BILLING_UNAVAILABLE,
            PurchaseResult.Failed.Reason.ITEM_UNAVAILABLE,
            PurchaseResult.Failed.Reason.LAUNCH_UNAVAILABLE,
            PurchaseResult.Failed.Reason.UNKNOWN,
        ).forEach { reason ->
            assertEquals(GenericErrorText, reason.toUiText())
        }
    }
}
