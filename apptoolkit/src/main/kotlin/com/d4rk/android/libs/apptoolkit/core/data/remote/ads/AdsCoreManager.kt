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

package com.d4rk.android.libs.apptoolkit.core.data.remote.ads

import android.app.Activity
import android.content.Context
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.utils.interfaces.OnShowAdCompleteListener
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAd
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

/**
 * Manager responsible for configuring and displaying App Open ads.
 *
 * It checks user preferences stored in [CommonDataStore] to determine
 * whether ads should be shown and manages the lifecycle of an
 * [AppOpenAd] instance.
 */
open class AdsCoreManager(
    protected val context: Context,
    val buildInfoProvider: BuildInfoProvider,
    private val dispatchers: DispatcherProvider,
) {
    private var dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)
    private var appOpenAdManager: AppOpenAdManager? = null

    /**
     * Prepares the SDK and loads an [AppOpenAd] if ads are enabled.
     */
    suspend fun initializeAds(appOpenUnitId: String) {
        val isAdsChecked: Boolean = withContext(dispatchers.io) {
            dataStore.ads(default = !buildInfoProvider.isDebugBuild).first()
        }
        if (isAdsChecked) {
            withContext(dispatchers.io) {
                MobileAds.initialize(
                    context,
                    InitializationConfig.Builder(context.getString(R.string.ad_mob_app_id)).build()
                ) {}
            }
            appOpenAdManager = AppOpenAdManager(appOpenUnitId)
        }
    }

    /**
     * Displays an ad if one has been loaded.
     *
     * The check runs inside the provided [scope] so callers can decide
     * where the asynchronous work should live.
     */
    fun showAdIfAvailable(activity: Activity, scope: CoroutineScope) {
        scope.launch {
            appOpenAdManager?.showAdIfAvailable(activity = activity)
        }
    }

    /**
     * Helper that wraps loading and showing of the App Open ad.
     */
    private inner class AppOpenAdManager(private val appOpenUnitId: String) {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd: Boolean = false
        var isShowingAd: Boolean = false
        private var loadTime: Long = 0

        /** Loads a new ad if none is available. */
        fun loadAd(context: Context) {
            if (isLoadingAd || isAdAvailable()) {
                return
            }
            isLoadingAd = true
            val request = AdRequest.Builder(appOpenUnitId).build()
            AppOpenAd.load(
                request, object : AdLoadCallback<AppOpenAd>() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
                    }
                })
        }

        private fun wasLoadTimeLessThanNHoursAgo(): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * 4
        }

        /** Whether a valid ad is ready to be shown. */
        private fun isAdAvailable(): Boolean {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo()
        }

        /** Convenience overload that ignores callbacks. */
        suspend fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(
                activity = activity,
                onShowAdCompleteListener = object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {}
                })
        }

        /** Displays the ad if available, otherwise triggers a reload. */
        suspend fun showAdIfAvailable(
            activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener
        ) {
            val isAdsChecked: Boolean = withContext(dispatchers.io) {
                dataStore.ads(default = !buildInfoProvider.isDebugBuild).first()
            }

            if (isShowingAd || !isAdsChecked) {
                return
            }
            if (!isAdAvailable()) {
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(context = this@AdsCoreManager.context)
                return
            }

            appOpenAd?.adEventCallback = object : AppOpenAdEventCallback {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(context = this@AdsCoreManager.context)
                }

                override fun onAdFailedToShowFullScreenContent(fullScreenContentError: FullScreenContentError) {
                    appOpenAd = null
                    isShowingAd = false
                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(context = this@AdsCoreManager.context)
                }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            }
            isShowingAd = true
            appOpenAd?.show(activity)
        }
    }
}
