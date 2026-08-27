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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads

/**
 * Koin qualifier names used for [com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig]
 * bindings.
 */
object AdsQualifiers {

    // Generic size-based names offered to hosts for their own banner placements. No toolkit screen
    // asks for them, so leaving them unbound costs nothing.
    const val BANNER_AD: String = "banner_ad"
    const val LARGE_BANNER_AD: String = "large_banner_ad"
    const val MEDIUM_RECTANGLE_AD: String = "medium_rectangle_ad"
    const val FULL_BANNER_AD: String = "full_banner_ad"
    const val LEADERBOARD_AD: String = "leaderboard_ad"
    const val FLUID_AD: String = "fluid_ad"

    /** Offered to hosts for a general-purpose native placement; no toolkit screen requests it. */
    const val NATIVE_AD: String = "native_ad"

    /** Offered to hosts; `BottomAppBarNativeAdBanner` has no call site in this repository. */
    const val BOTTOM_NAV_BAR_NATIVE_AD: String = "bottom_nav_bar_native_ad"

    // Required. A toolkit screen injects each of these and Koin throws if the host has not bound
    // it, so a host that ships the screen must supply the id. See the ads module README.
    const val NO_DATA_NATIVE_AD: String = "no_data_native_ad"
    const val HELP_NATIVE_AD: String = "help_native_ad"
    const val SUPPORT_NATIVE_AD: String = "support_native_ad"
}
