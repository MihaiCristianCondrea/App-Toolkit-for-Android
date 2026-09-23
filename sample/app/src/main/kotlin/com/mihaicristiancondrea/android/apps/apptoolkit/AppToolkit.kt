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

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mihaicristiancondrea.android.apps.apptoolkit.di.initializeKoin
import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants.AdsConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.managers.BaseCoreManager
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.AppThemeConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ThemePaletteProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.managers.AdsCoreManager
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.shake.IssueReporterShakeManager
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.seasonal.SeasonalThemeManager
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.repositories.BillingRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.koin.android.ext.android.getKoin

/**
 * Main application class for AppToolkit that handles core system initialization,
 * lifecycle management, and global configurations.
 *
 * This class extends [BaseCoreManager] and implements [DefaultLifecycleObserver] to:
 * - Initialize Dependency Injection via Koin.
 * - Manage global ad initialization and display (App Open ads).
 * - Handle dynamic color palette switching, including seasonal themes (Halloween, Christmas).
 * - Monitor activity lifecycles to track the current UI context.
 * - Install shake-to-report, which opens the toolkit's issue reporter from any screen.
 * - Install the seasonal overlay: the holiday greeting and snow with the Christmas theme.
 * - Process billing and purchases on application resume.
 *
 * @property currentActivity The currently active [Activity] instance, used for showing ads.
 * @property adsCoreManager Manager responsible for handling advertisement logic.
 */
class AppToolkit : BaseCoreManager(), DefaultLifecycleObserver {
    private var currentActivity: Activity? = null

    private val adsCoreManager: AdsCoreManager by lazy { getKoin().get<AdsCoreManager>() }

    override fun onCreate() {
        initializeKoin(context = this)
        applyDefaultColorPalette()
        // Also registers this instance for activity callbacks; registering it a second time here
        // would deliver every callback twice.
        super<BaseCoreManager>.onCreate()
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

    override fun onStart(owner: LifecycleOwner) {
        currentActivity?.let { adsCoreManager.showAdIfAvailable(it, owner.lifecycleScope) }
    }

    override fun onResume(owner: LifecycleOwner) {
        owner.lifecycleScope.launch {
            getKoin().get<BillingRepository>().processPastPurchases()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity) {
        if (currentActivity === activity) {
            currentActivity = null
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity === activity) {
            currentActivity = null
        }
    }
}
