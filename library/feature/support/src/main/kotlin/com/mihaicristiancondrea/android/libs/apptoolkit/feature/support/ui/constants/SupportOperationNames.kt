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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.constants

/** Operation names and extra keys reported to telemetry by `SupportViewModel`. */
internal object SupportOperationNames {
    const val OBSERVE_PRODUCT_DETAILS: String = "observeProductDetails"
    const val OBSERVE_PURCHASE_RESULT: String = "observePurchaseResult"
    const val QUERY_PRODUCT_DETAILS: String = "queryProductDetails"
    const val DONATE_CLICKED: String = "donateClicked"

    object ExtraKeys {
        const val PRODUCT_ID: String = "productId"
        const val ACTIVITY: String = "activity"
    }
}
