package com.d4rk.android.libs.apptoolkit.app.support.utils.extensions

import com.android.billingclient.api.ProductDetails

internal fun ProductDetails.primaryOneTimePurchaseOffer(): ProductDetails.OneTimePurchaseOfferDetails? {
    val offerList = runCatching { oneTimePurchaseOfferDetailsList }
        .getOrNull()
        ?.takeIf { it.isNotEmpty() }
    if (offerList != null) {
        return offerList.firstOrNull()
    }

    return runCatching { oneTimePurchaseOfferDetails }
        .getOrNull()
}

internal fun ProductDetails.primaryOfferToken(): String? =
    primaryOneTimePurchaseOffer()?.offerToken

internal fun ProductDetails.primaryFormattedPrice(): String? =
    primaryOneTimePurchaseOffer()?.formattedPrice
