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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.contracts

import android.app.Activity

/**
 * What the user can ask the support page's ViewModel to do.
 */
sealed interface SupportEvent {
    /** Queries the donation products, on start and from the failure state's retry. */
    data object QueryProductDetails : SupportEvent

    /**
     * Starts the purchase of the donation [productId].
     *
     * @property activity The activity Play's purchase sheet shows over, used only to launch it.
     */
    data class Donate(
        val productId: String,
        val activity: Activity,
    ) : SupportEvent
}
