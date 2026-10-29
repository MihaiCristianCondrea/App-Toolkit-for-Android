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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.BillSplit
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.extensions.toToolDecimalOrNull
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.extensions.toToolLongOrNull
import java.math.BigDecimal
import java.math.RoundingMode

class BillSplitterRepository {
    /** Uses two decimal places; each share rounds up so the collected amount covers the bill. */
    fun split(billInput: String, tipPercent: Int, peopleInput: String, fractionDigits: Int = 2): BillSplit? {
        val bill = billInput.toToolDecimalOrNull() ?: return null
        val people = peopleInput.toToolLongOrNull() ?: return null
        if (bill.signum() < 0 || bill.scale() > fractionDigits || fractionDigits !in 0..2 || people < 1 || tipPercent !in 0..100) return null
        val tip = bill.multiply(BigDecimal(tipPercent)).movePointLeft(2).setScale(fractionDigits, RoundingMode.HALF_UP)
        val total = bill.add(tip).setScale(fractionDigits, RoundingMode.UNNECESSARY)
        return BillSplit(tip = tip, total = total, perPerson = total.divide(BigDecimal(people), fractionDigits, RoundingMode.CEILING))
    }
}
