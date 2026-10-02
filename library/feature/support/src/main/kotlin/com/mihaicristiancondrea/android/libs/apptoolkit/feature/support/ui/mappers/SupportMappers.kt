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
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.data.mappers.hasOneTimePurchaseOffer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.data.mappers.primaryFormattedPrice
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.models.DonationOption
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentMap

/**
 * One [DonationOption] for each of [productIds], in that order, keyed by product id. A product Play
 * did not return is still listed, as not eligible, so its button shows as unavailable.
 */
internal fun Map<String, ProductDetails>.toDonationOptions(
    productIds: List<String>,
): ImmutableMap<String, DonationOption> =
    productIds.associateWith { productId ->
        val details: ProductDetails? = this[productId]
        DonationOption(
            productId = productId,
            formattedPrice = details?.primaryFormattedPrice(),
            isEligible = details?.hasOneTimePurchaseOffer() == true,
        )
    }.toPersistentMap()
