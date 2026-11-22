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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.extensions

import com.android.billingclient.api.ProductDetails

/**
 * Returns the primary one-time purchase offer for this INAPP product, preferring Play Billing
 * Library 8's offer list before falling back to the legacy single offer property.
 */
internal fun ProductDetails.primaryOneTimePurchaseOffer(): ProductDetails.OneTimePurchaseOfferDetails? {
    val offerList = runCatching { oneTimePurchaseOfferDetailsList }
        .getOrNull()
        ?.takeIf { it.isNotEmpty() }

    if (offerList != null) {
        return offerList.firstOrNull()
    }

    return runCatching { oneTimePurchaseOfferDetails }.getOrNull()
}

/**
 * Returns true when an INAPP product has a usable one-time purchase offer.
 */
internal fun ProductDetails.hasOneTimePurchaseOffer(): Boolean =
    primaryOneTimePurchaseOffer() != null

/** Returns the store-formatted price of the primary one-time purchase offer, if available. */
internal fun ProductDetails.primaryFormattedPrice(): String? =
    primaryOneTimePurchaseOffer()?.formattedPrice
