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

package com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules

import com.d4rk.android.apps.apptoolkit.core.utils.constants.ads.AdsConstants
import com.d4rk.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ads.AdsQualifiers
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val adsModule: Module = module {
    single<AdsConfig>(named(name = AdsQualifiers.NATIVE_AD)) {
        AdsConfig(bannerAdUnitId = AdsConstants.NATIVE_AD_UNIT_ID)
    }

    single<AdsConfig>(named(name = AdsQualifiers.APPS_LIST_NATIVE_AD)) {
        AdsConfig(bannerAdUnitId = AdsConstants.APPS_LIST_NATIVE_AD_UNIT_ID)
    }

    single<AdsConfig>(named(name = AdsQualifiers.APP_DETAILS_NATIVE_AD)) {
        AdsConfig(bannerAdUnitId = AdsConstants.APP_DETAILS_NATIVE_AD_UNIT_ID)
    }

    single<AdsConfig>(named(name = AdsQualifiers.NO_DATA_NATIVE_AD)) {
        AdsConfig(bannerAdUnitId = AdsConstants.NO_DATA_NATIVE_AD_UNIT_ID)
    }

    single<AdsConfig>(named(name = AdsQualifiers.BOTTOM_NAV_BAR_NATIVE_AD)) {
        AdsConfig(bannerAdUnitId = AdsConstants.BOTTOM_NAV_BAR_NATIVE_AD_UNIT_ID)
    }

    single<AdsConfig>(named(name = AdsQualifiers.HELP_LARGE_BANNER_AD)) {
        AdsConfig(
            bannerAdUnitId = AdsConstants.HELP_NATIVE_AD_UNIT_ID,
            adSize = AdSize.LARGE_BANNER
        )
    }

    single<AdsConfig>(named(name = AdsQualifiers.SUPPORT_NATIVE_AD)) {
        AdsConfig(bannerAdUnitId = AdsConstants.SUPPORT_NATIVE_AD_UNIT_ID)
    }
}
