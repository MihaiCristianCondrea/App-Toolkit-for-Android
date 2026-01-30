package com.d4rk.android.libs.apptoolkit.app.support.utils.extensions

import com.android.billingclient.api.BillingClient
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

/**
 * Returns a primary offer token based on the supplied product type.
 *
 * INAPP products use [ProductDetails.oneTimePurchaseOfferDetails], while SUBS
 * products use [ProductDetails.subscriptionOfferDetails].
 */
internal fun ProductDetails.primaryOfferToken(productType: String): String? =
    when (productType) {
        BillingClient.ProductType.INAPP -> runCatching { oneTimePurchaseOfferDetails?.offerToken }
            .getOrNull()

        BillingClient.ProductType.SUBS -> runCatching {
            subscriptionOfferDetails?.firstOrNull()?.offerToken
        }.getOrNull()

        else -> null
    }

/**
 * Returns true when an INAPP product has a usable one-time purchase offer.
 */
internal fun ProductDetails.hasOneTimePurchaseOffer(): Boolean =
    runCatching { oneTimePurchaseOfferDetails != null }.getOrNull() == true

internal fun ProductDetails.primaryFormattedPrice(): String? =
    primaryOneTimePurchaseOffer()?.formattedPrice
