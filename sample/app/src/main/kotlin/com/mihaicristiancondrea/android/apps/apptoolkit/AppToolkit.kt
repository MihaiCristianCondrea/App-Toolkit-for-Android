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

@file:Suppress("DEPRECATION")

package com.mihaicristiancondrea.android.apps.apptoolkit

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mihaicristiancondrea.android.apps.apptoolkit.di.initializeKoin
import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants.AdsConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.AppToolkitApplication
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppThemeConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ThemePaletteProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.IssueReporterShakeManager
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.SeasonalThemeManager
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.utils.AppOpenAdController
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.repositories.BillingRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.koin.android.ext.android.getKoin

/**
 * Sample composition root for Koin and process-lifetime integrations: Mobile Ads SDK
 * initialization, billing recovery, shake reporting, and the seasonal overlay.
 * [AppToolkitApplication] owns core startup and activity-callback registration.
 */
class AppToolkit : AppToolkitApplication(), DefaultLifecycleObserver {

    private val adsCoreManager: AppOpenAdController by lazy { getKoin().get<AppOpenAdController>() }

    override fun onCreate() {
        initializeKoin(context = this)
        applyDefaultColorPalette()
        // AppToolkitApplication registers activity callbacks; registering here again would deliver them twice.
        super<AppToolkitApplication>.onCreate()
        getKoin().get<IssueReporterShakeManager>().install()
        getKoin().get<SeasonalThemeManager>().install()
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer = this)
    }

    override suspend fun onInitializeApp(): Unit = supervisorScope {
        initializeAds()
    }

    private suspend fun initializeAds() {
        adsCoreManager.initializeAds(
            appOpenUnitId = AdsConstants.APP_OPEN_UNIT_ID,
            disableNativeValidator = true,
        )
    }

    /**
     * Applies the host's default palette.
     *
     * Holiday palettes are not swapped in here. They are offered by the holiday greeting that
     * [SeasonalThemeManager] shows, applied only when the person agrees, and taken off again when
     * the holiday ends.
     */
    private fun applyDefaultColorPalette() {
        applyColorPalette(getKoin().get())
    }

    private fun applyColorPalette(colorPalette: ColorPalette) {
        AppThemeConfig.customLightScheme = colorPalette.lightColorScheme
        AppThemeConfig.customDarkScheme = colorPalette.darkColorScheme
        ThemePaletteProvider.defaultPalette = colorPalette
    }

    override fun onResume(owner: LifecycleOwner) {
        owner.lifecycleScope.launch {
            getKoin().get<BillingRepository>().processPastPurchases()
        }
    }
}
