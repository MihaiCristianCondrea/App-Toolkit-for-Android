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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.models

import androidx.compose.runtime.Immutable

/**
 * The stored ad preferences the ads settings screen renders.
 *
 * @property adsEnabled Whether ads show at all. Debug builds expose it as the display ads switch.
 * @property reduceAds Whether App Open ads are suppressed. Release builds expose it as the reduce
 * ads switch.
 */
@Immutable
data class AdsPreferences(
    val adsEnabled: Boolean,
    val reduceAds: Boolean,
)
