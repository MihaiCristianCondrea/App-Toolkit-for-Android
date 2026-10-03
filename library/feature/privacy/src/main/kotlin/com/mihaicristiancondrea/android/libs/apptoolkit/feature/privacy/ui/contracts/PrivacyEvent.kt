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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.contracts

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.models.PrivacyItemAction

/**
 * What the user can ask the privacy page's ViewModel to do.
 */
sealed interface PrivacyEvent {
    /** Builds the rows from the provider, on start and on retry. */
    data object Load : PrivacyEvent

    /**
     * A preference row was tapped. The ViewModel reports it as the `openPrivacyItem` operation;
     * the screen opens what [action] points to.
     */
    data class ItemClicked(val action: PrivacyItemAction) : PrivacyEvent
}
