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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.models

/**
 * A billing outcome and the products it concerns.
 *
 * @property productIds The products [result] concerns. Empty when the outcome belongs to no known
 * product, such as a callback that arrives without a launch in this process. Null when the
 * implementation does not identify products, as the default `BillingRepository.purchaseEvents` does.
 */
data class PurchaseEvent(val result: PurchaseResult, val productIds: List<String>?)

/**
 * Represents the result of a purchase operation.
 */
sealed class PurchaseResult {
    data object Success : PurchaseResult()
    data object Pending : PurchaseResult()

    /**
     * Play could not complete the purchase, the product query, or the recovery of a past purchase.
     *
     * @property reason What went wrong, for the screen to choose its own localized text.
     * @property debugMessage Play's developer message, for diagnostics only. Google documents it as
     * not meant for users, so a screen never shows it.
     */
    data class Failed(
        val reason: Reason,
        val debugMessage: String = "",
    ) : PurchaseResult() {

        /** Why a purchase failed, grouped by what the person can do about it. */
        enum class Reason {
            /** No connection to Play, or Play's service is busy or restarting; trying again can work. */
            NETWORK,

            /** Billing is unavailable on this device or account, for example Play is disabled. */
            BILLING_UNAVAILABLE,

            /** The product cannot be bought now, or the account already owns it. */
            ITEM_UNAVAILABLE,

            /** The purchase sheet could not open, for example because the screen went away. */
            LAUNCH_UNAVAILABLE,

            /** Any other failure, including an error in how the app called Play. */
            UNKNOWN,
        }
    }

    data object UserCancelled : PurchaseResult()
}
