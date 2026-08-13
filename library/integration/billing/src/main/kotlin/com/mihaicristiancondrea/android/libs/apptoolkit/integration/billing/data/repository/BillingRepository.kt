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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.repository

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.BillingCore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.model.billing.PurchaseResult
import kotlinx.coroutines.flow.Flow

/**
 * Public billing contract for the toolkit.
 *
 * Callers depend on this interface rather than the Play Billing implementation: the concrete class
 * is a process-wide singleton behind a private constructor, which a consuming feature can neither
 * substitute nor fake in a test.
 *
 * It extends [BillingCore] because the core manager closes billing during teardown but must not see
 * the rest of this surface — that narrower contract is what breaks the dependency cycle between
 * `:library:core:common` and this module.
 *
 * [ProductDetails] stays a Play Billing type on purpose. Offer tokens and pricing phases are read
 * straight from it at purchase time, so a toolkit-owned copy would have to reproduce the SDK's model
 * to stay useful.
 */
interface BillingRepository : BillingCore {

    /** Products resolved by the most recent [queryProductDetails] call, keyed by product id. */
    val productDetails: Flow<Map<String, ProductDetails>>

    /** Outcome of each purchase attempt, including failures reported by the Play Billing library. */
    val purchaseResult: Flow<PurchaseResult>

    /** Loads details for [productIds] and publishes them through [productDetails]. */
    suspend fun queryProductDetails(productIds: List<String>)

    /** Consumes purchases that completed while the app was not running. */
    suspend fun processPastPurchases()

    /** Starts the purchase flow for a one-time product such as a donation. */
    fun launchInAppDonationFlow(activity: Activity, details: ProductDetails)

    /**
     * Starts the purchase flow for a subscription.
     *
     * @param offerToken Specific offer to purchase. When null or blank the product's primary offer
     * is used.
     */
    fun launchSubscriptionFlow(
        activity: Activity,
        details: ProductDetails,
        offerToken: String? = null,
    )
}
