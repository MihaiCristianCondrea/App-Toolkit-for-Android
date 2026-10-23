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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.utils

import com.android.billingclient.api.ProductDetails

internal fun ProductDetails.primaryOneTimePurchaseOffer(): ProductDetails.OneTimePurchaseOfferDetails? {
    val offerList = runCatching { oneTimePurchaseOfferDetailsList }
        .getOrNull()
        ?.takeIf { it.isNotEmpty() }

    offerList?.let { details ->
        return details.firstOrNull()
    }

    return runCatching { oneTimePurchaseOfferDetails }.getOrNull()
}

/**
 * Returns true when an INAPP product has a usable one-time purchase offer.
 */
internal fun ProductDetails.hasOneTimePurchaseOffer(): Boolean =
    runCatching { oneTimePurchaseOfferDetails != null }.getOrNull() == true

internal fun ProductDetails.primaryFormattedPrice(): String? =
    primaryOneTimePurchaseOffer()?.formattedPrice
