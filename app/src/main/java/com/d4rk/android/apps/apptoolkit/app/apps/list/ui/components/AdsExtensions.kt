package com.d4rk.android.apps.apptoolkit.app.apps.list.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.apps.apptoolkit.core.data.datastore.DataStore
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import org.koin.core.Koin
import org.koin.core.qualifier.named

@Composable
fun rememberAdsConfig(koin: Koin, isTabletOrLandscape: Boolean): AdsConfig {
    val bannerType = remember(isTabletOrLandscape) {
        if (isTabletOrLandscape) "full_banner" else "banner_medium_rectangle"
    }
    return remember(bannerType) { koin.get<AdsConfig>(qualifier = named(bannerType)) }
}

@Composable
fun rememberAdsEnabled(koin: Koin): Boolean {
    val dataStore: DataStore = remember { koin.get() }
    return remember { dataStore.ads(default = true) }
        .collectAsStateWithLifecycle(initialValue = true).value
}

