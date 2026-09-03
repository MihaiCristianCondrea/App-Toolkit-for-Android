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

package com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.di

import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants.AppAdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.AdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import org.junit.jupiter.api.Test
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import kotlin.test.assertTrue

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
