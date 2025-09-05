package com.d4rk.android.apps.apptoolkit.core.utils.constants.ads

import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ads.DebugAdsConstants

object AdsConstants {

    val BANNER_AD_UNIT_ID : String
        get() = if (BuildConfig.DEBUG) {
            DebugAdsConstants.BANNER_AD_UNIT_ID
        }
        else {
            "ca-app-pub-5294151573817700/7520919879"
        }

    val APP_OPEN_UNIT_ID : String
        get() = if (BuildConfig.DEBUG) {
            DebugAdsConstants.APP_OPEN_AD_UNIT_ID
        }
        else {
            "ca-app-pub-5294151573817700/8339177528"
        }

    val NATIVE_AD_UNIT_ID: String
        get() = if (BuildConfig.DEBUG) {
            DebugAdsConstants.NATIVE_AD_UNIT_ID
        } else {
            "ca-app-pub-5294151573817700/5578142927"
        }

}