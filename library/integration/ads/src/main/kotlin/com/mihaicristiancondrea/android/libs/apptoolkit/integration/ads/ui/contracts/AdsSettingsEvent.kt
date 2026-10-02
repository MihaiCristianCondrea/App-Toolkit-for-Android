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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.contracts

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost

/** What the ads settings screen asks its ViewModel to do. */
sealed interface AdsSettingsEvent {
    /** Follows the stored ad preferences, on start and on retry. */
    data object Load : AdsSettingsEvent
    data class SetAdsEnabled(val enabled: Boolean) : AdsSettingsEvent
    data class SetReduceAds(val enabled: Boolean) : AdsSettingsEvent

    /** Opens the UMP privacy form from [host], whether or not UMP requires it. */
    data class RequestConsent(val host: ConsentHost) : AdsSettingsEvent
}
