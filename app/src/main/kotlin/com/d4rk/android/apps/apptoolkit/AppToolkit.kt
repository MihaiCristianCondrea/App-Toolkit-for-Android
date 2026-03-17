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

package com.d4rk.android.apps.apptoolkit

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.d4rk.android.apps.apptoolkit.core.di.initializeKoin
import com.d4rk.android.apps.apptoolkit.core.utils.constants.ads.AdsConstants
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.AppThemeConfig
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.ColorPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.ThemePaletteProvider
import com.d4rk.android.libs.apptoolkit.core.BaseCoreManager
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.remote.ads.AdsCoreManager
import com.d4rk.android.libs.apptoolkit.core.utils.constants.colorscheme.StaticPaletteIds
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.date.isChristmasSeason
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.date.isHalloweenSeason
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.getKoin
import java.time.LocalDate
import java.time.ZoneId

class AppToolkit : BaseCoreManager(), DefaultLifecycleObserver {
    private var currentActivity: Activity? = null
    private val appScope = CoroutineScope(SupervisorJob() + dispatchers.io)

    private val adsCoreManager: AdsCoreManager by lazy { getKoin().get<AdsCoreManager>() }

    override fun onCreate() {
        initializeKoin(context = this)
        applyDefaultColorPalette()
        super<BaseCoreManager>.onCreate()
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer = this)
    }

    override suspend fun onInitializeApp(): Unit = supervisorScope {
        listOf(async { initializeAds() }).awaitAll()
    }

    private suspend fun initializeAds() {
        adsCoreManager.initializeAds(AdsConstants.APP_OPEN_UNIT_ID)
    }

    private fun applyDefaultColorPalette() {
        // Change rationale: startup palette resolution previously used runBlocking DataStore reads on
        // the main thread, increasing cold-start latency risk. We now apply a deterministic palette
        // immediately and reconcile seasonal preferences asynchronously.
        val initialPalette: ColorPalette = getKoin().get()
        applyColorPalette(initialPalette)

        appScope.launch {
            val palette = resolvePreferredColorPalette()
            withContext(dispatchers.main) {
                applyColorPalette(palette)
            }
        }
    }

    private suspend fun resolvePreferredColorPalette(): ColorPalette {
        val dataStore: CommonDataStore = CommonDataStore.getInstance(context = this)
        val hasInteractedWithSettings: Boolean = dataStore.settingsInteracted.first()

        if (!hasInteractedWithSettings) {
            val staticPaletteId: String = dataStore.staticPaletteId.first()
            val today: LocalDate = LocalDate.now(ZoneId.systemDefault())
            val shouldUseSeasonalPalette: Boolean = staticPaletteId == StaticPaletteIds.DEFAULT

            if (shouldUseSeasonalPalette) {
                return when {
                    today.isHalloweenSeason -> ThemePaletteProvider.paletteById(StaticPaletteIds.HALLOWEEN)
                    today.isChristmasSeason -> ThemePaletteProvider.paletteById(StaticPaletteIds.CHRISTMAS)
                    else -> getKoin().get()
                }
            }
        }

        return getKoin().get()
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
            billingRepository.processPastPurchases()
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

    override fun onTerminate() {
        appScope.cancel()
        super.onTerminate()
    }
}
