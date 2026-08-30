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

package com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants

import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.BuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.DebugAdsConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.bannerAdUnitId as selectBannerAdUnitId
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.nativeAdUnitId as selectNativeAdUnitId

object AdsConstants {

    private fun bannerAdUnitId(releaseId: String): String =
        selectBannerAdUnitId(isDebug = BuildConfig.DEBUG, releaseId = releaseId)

    private fun nativeAdUnitId(releaseId: String): String =
        selectNativeAdUnitId(isDebug = BuildConfig.DEBUG, releaseId = releaseId)

    val APP_OPEN_UNIT_ID: String
        get() = if (BuildConfig.DEBUG) {
            DebugAdsConstants.APP_OPEN_AD_UNIT_ID
        } else {
            "ca-app-pub-5294151573817700/8339177528"
        }

    val NATIVE_AD_UNIT_ID: String
        get() = nativeAdUnitId("ca-app-pub-5294151573817700/5578142927")

    val APP_DETAILS_NATIVE_AD_UNIT_ID: String
        get() = nativeAdUnitId("ca-app-pub-5294151573817700/8490774272")

    val APPS_LIST_NATIVE_AD_UNIT_ID: String
        get() = nativeAdUnitId("ca-app-pub-5294151573817700/4743100951")

    val NO_DATA_NATIVE_AD_UNIT_ID: String
        get() = nativeAdUnitId("ca-app-pub-5294151573817700/3430019286")

    val BOTTOM_NAV_BAR_NATIVE_AD_UNIT_ID: String
        get() = nativeAdUnitId("ca-app-pub-5294151573817700/6982251485")

    val QUICK_TOOLS_NATIVE_AD_UNIT_ID: String
        get() = nativeAdUnitId("ca-app-pub-5294151573817700/7704036670")

    val HELP_NATIVE_AD_UNIT_ID: String
        get() = nativeAdUnitId("ca-app-pub-5294151573817700/7512912137")

    val SUPPORT_NATIVE_AD_UNIT_ID: String
        get() = nativeAdUnitId("ca-app-pub-5294151573817700/9755754484")

    /**
     * How many apps to show between native ads in the apps list.
     *
     * Was an app-module `buildConfigField`, which no feature module can read. It is a fixed tuning
     * value, not a build input, so it belongs in shared code rather than in the generated class.
     */
    const val APPS_LIST_AD_FREQUENCY: Int = 4
}
