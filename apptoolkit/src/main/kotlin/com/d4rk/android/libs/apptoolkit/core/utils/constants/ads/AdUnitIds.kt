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

package com.d4rk.android.libs.apptoolkit.core.utils.constants.ads

/** Selects a banner ad unit ID while keeping debug IDs centralized in the toolkit library. */
fun bannerAdUnitId(isDebug: Boolean, releaseId: String): String =
    if (isDebug) DebugAdsConstants.BANNER_AD_UNIT_ID else releaseId

/** Selects a native ad unit ID while keeping debug IDs centralized in the toolkit library. */
fun nativeAdUnitId(isDebug: Boolean, releaseId: String): String =
    if (isDebug) DebugAdsConstants.NATIVE_AD_UNIT_ID else releaseId
