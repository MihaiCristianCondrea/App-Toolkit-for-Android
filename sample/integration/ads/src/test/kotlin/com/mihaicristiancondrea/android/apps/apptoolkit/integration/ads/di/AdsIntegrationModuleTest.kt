/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.di

import com.mihaicristiancondrea.android.apps.apptoolkit.core.utils.constants.ads.AppAdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.AdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication

class AdsIntegrationModuleTest {

    @Test
    fun `every sample ad placement resolves to a configured unit id`() {
        val koin = koinApplication { modules(adsIntegrationModule) }.koin
        val qualifiers = listOf(
            AdsQualifiers.NATIVE_AD,
            AdsQualifiers.NO_DATA_NATIVE_AD,
            AdsQualifiers.BOTTOM_NAV_BAR_NATIVE_AD,
            AdsQualifiers.HELP_NATIVE_AD,
            AdsQualifiers.SUPPORT_NATIVE_AD,
            AppAdsQualifiers.APPS_LIST_NATIVE_AD,
            AppAdsQualifiers.APP_DETAILS_NATIVE_AD,
        )

        qualifiers.forEach { qualifier ->
            val config = koin.get<AdsConfig>(named(qualifier))
            assertTrue(config.bannerAdUnitId.isNotBlank(), qualifier)
        }
    }
}
