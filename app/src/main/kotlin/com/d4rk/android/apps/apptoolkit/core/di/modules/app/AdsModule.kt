package com.d4rk.android.apps.apptoolkit.core.di.modules.app

import com.d4rk.android.apps.apptoolkit.core.utils.constants.ads.AdsConstants
import com.d4rk.android.libs.apptoolkit.app.ads.data.repository.AdsSettingsRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.ui.AdsSettingsViewModel
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.google.android.gms.ads.AdSize
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val adsModule: Module = module {

    single<AdsSettingsRepository> {
        AdsSettingsRepositoryImpl(
            dataStore = get(),
            buildInfoProvider = get<BuildInfoProvider>(),
        )
    }

    viewModel {
        AdsSettingsViewModel(
            repository = get(),
            dispatchers = get(),
            observeAdsEnabled = get(),
            setAdsEnabled = get()
        )
    }

    single<AdsConfig>(named(name = "native_ad")) {
        AdsConfig(bannerAdUnitId = AdsConstants.NATIVE_AD_UNIT_ID)
    }

    single<AdsConfig>(named(name = "apps_list_native_ad")) {
        AdsConfig(bannerAdUnitId = AdsConstants.APPS_LIST_NATIVE_AD_UNIT_ID)
    }

    single<AdsConfig>(named(name = "app_details_native_ad")) {
        AdsConfig(bannerAdUnitId = AdsConstants.APP_DETAILS_NATIVE_AD_UNIT_ID)
    }

    single<AdsConfig>(named(name = "no_data_native_ad")) {
        AdsConfig(bannerAdUnitId = AdsConstants.NO_DATA_NATIVE_AD_UNIT_ID)
    }

    single<AdsConfig>(named(name = "bottom_nav_bar_native_ad")) {
        AdsConfig(bannerAdUnitId = AdsConstants.BOTTOM_NAV_BAR_NATIVE_AD_UNIT_ID)
    }

    single<AdsConfig>(named(name = "help_large_banner_ad")) {
        AdsConfig(
            bannerAdUnitId = AdsConstants.HELP_NATIVE_AD_UNIT_ID,
            adSize = AdSize.LARGE_BANNER
        )
    }

    single<AdsConfig>(named(name = "support_native_ad")) {
        AdsConfig(bannerAdUnitId = AdsConstants.SUPPORT_NATIVE_AD_UNIT_ID)
    }
}
