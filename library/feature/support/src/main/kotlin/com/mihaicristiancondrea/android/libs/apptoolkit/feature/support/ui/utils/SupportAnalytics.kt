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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.data.mappers.primaryOneTimePurchaseOffer

/**
 * The donation funnel's GA4 vocabulary.
 *
 * Revenue is deliberately absent. Firebase records `in_app_purchase` on its own for every purchase
 * Google Play processes, with its value and currency, so a manual `purchase` would count each
 * donation twice. These events cover the steps around it that Firebase cannot see: that a donation
 * was started, and how the attempt ended.
 */
internal object SupportAnalytics {

    object Events {
        /** Recommended event: a donation tier was chosen and Play's purchase flow is launching. */
        const val BEGIN_CHECKOUT: String = "begin_checkout"

        /** How a started donation ended: see [Outcomes]. */
        const val DONATION_RESULT: String = "donation_result"
    }

    object Params {
        const val VALUE: String = "value"
        const val CURRENCY: String = "currency"
        const val PRODUCT_ID: String = "product_id"
        const val OUTCOME: String = "outcome"
    }

    /** Bounded values for [Params.OUTCOME]. */
    object Outcomes {
        const val SUCCESS: String = "success"
        const val PENDING: String = "pending"
        const val CANCELLED: String = "cancelled"
        const val FAILED: String = "failed"
    }
}

/**
 * `begin_checkout` for a donation of [details], with its price when Play reported one.
 *
 * GA4 needs `currency` whenever `value` is set, so the two are sent together or not at all.
 */
internal fun beginCheckoutEvent(productId: String, details: ProductDetails): AnalyticsEvent {
    val offer: ProductDetails.OneTimePurchaseOfferDetails? = details.primaryOneTimePurchaseOffer()
    val currency: String? = offer?.priceCurrencyCode?.takeIf(String::isNotBlank)
    return AnalyticsEvent(
        name = SupportAnalytics.Events.BEGIN_CHECKOUT,
        params = buildMap {
            put(SupportAnalytics.Params.PRODUCT_ID, AnalyticsValue.Str(productId))
            if (offer != null && currency != null) {
                put(
                    SupportAnalytics.Params.VALUE,
                    AnalyticsValue.DoubleVal(offer.priceAmountMicros / MICROS_PER_UNIT),
                )
                put(SupportAnalytics.Params.CURRENCY, AnalyticsValue.Str(currency))
            }
        },
    )
}

/** `donation_result` for [productId]; [outcome] is one of [SupportAnalytics.Outcomes]. */
internal fun donationResultEvent(productId: String, outcome: String): AnalyticsEvent =
    AnalyticsEvent(
        name = SupportAnalytics.Events.DONATION_RESULT,
        params = mapOf(
            SupportAnalytics.Params.PRODUCT_ID to AnalyticsValue.Str(productId),
            SupportAnalytics.Params.OUTCOME to AnalyticsValue.Str(outcome),
        ),
    )

private const val MICROS_PER_UNIT: Double = 1_000_000.0
