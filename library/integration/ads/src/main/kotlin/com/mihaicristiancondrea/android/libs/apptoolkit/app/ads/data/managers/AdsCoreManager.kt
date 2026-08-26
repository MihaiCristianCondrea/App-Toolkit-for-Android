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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.managers

// import com.mihaicristiancondrea.android.libs.apptoolkit.R
import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAd
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.ads.AdsSdkState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.interfaces.OnShowAdCompleteListener
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.AdMobAppIdProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.ManifestAdMobAppIdProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    private val adMobAppIdProvider: AdMobAppIdProvider = ManifestAdMobAppIdProvider(context = context),
    private val adsSdkInitializer: AdsSdkInitializer = AdsSdkInitializer.Default,
    private val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context),
) {
    private var appOpenAdManager: AppOpenAdManager? = null

    private val managerScope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatchers.io)
    private val initializationMutex = Mutex()
    private var adsPreferenceJob: Job? = null

    @Volatile
    private var isSdkInitialized: Boolean = false

    /**
     * Prepares the SDK and loads an [AppOpenAd] if ads are enabled.
     *
     * The AdMob application id is resolved from the host app's manifest through
     * [AdMobAppIdProvider]. The toolkit previously initialized the SDK with Google's sample app id,
     * which pointed every consumer app at a publisher account that was not its own. When the host
     * declares no usable `com.google.android.gms.ads.APPLICATION_ID` meta-data, initialization is
     * skipped instead of falling back to a foreign id.
     *
     * Hosts must not call [MobileAds.initialize] themselves; a second initialization with a
     * different id re-introduces the mismatch this method exists to prevent. Use
     * [disableNativeValidator] instead of a bespoke initialization when the host needs the SDK's
     * native ad validator turned off.
     *
     * Change rationale: this used to sample the ads preference once at startup with its own default
     * (`!isDebugBuild`), while the ad views read the preference through
     * [CommonDataStore.adsEnabledFlow] with a different default. On a build where the two disagreed
     * the views loaded ads that the SDK had never been initialized for, and the loader throws
     * `IllegalStateException` for that. Both sides now read the same flow, and the preference is
     * observed rather than sampled, so turning ads on at runtime initializes the SDK instead of
     * waiting for the next process start.
     */
    suspend fun initializeAds(appOpenUnitId: String, disableNativeValidator: Boolean = false) {
        val isAdsChecked: Boolean = withContext(dispatchers.io) {
            dataStore.adsEnabledFlow.first()
        }
        if (isAdsChecked) {
            startAds(appOpenUnitId = appOpenUnitId, disableNativeValidator = disableNativeValidator)
        }
        observeAdsPreference(
            appOpenUnitId = appOpenUnitId,
            disableNativeValidator = disableNativeValidator,
        )
    }

    /**
     * Keeps the SDK in step with the ads preference for the rest of the process's life.
     */
    private fun observeAdsPreference(appOpenUnitId: String, disableNativeValidator: Boolean) {
        if (adsPreferenceJob != null) return
        adsPreferenceJob = managerScope.launch {
            dataStore.adsEnabledFlow.collect { isEnabled ->
                if (isEnabled) {
                    startAds(
                        appOpenUnitId = appOpenUnitId,
                        disableNativeValidator = disableNativeValidator,
                    )
                }
            }
        }
    }

    private suspend fun startAds(appOpenUnitId: String, disableNativeValidator: Boolean) {
        if (!ensureAdsSdkInitialized(disableNativeValidator = disableNativeValidator)) return
        if (appOpenAdManager == null) {
            appOpenAdManager = AppOpenAdManager(appOpenUnitId)
        }
    }

    /**
     * Initializes the Mobile Ads SDK exactly once, and reports whether it is usable.
     *
     * Nothing may load an ad before this returns `true`: the loader throws
     * `IllegalStateException("MobileAds.initialize must be called before using the Google Mobile
     * Ads SDK.")` otherwise.
     *
     * @return `false` when the host declares no valid AdMob application id, in which case no ad can
     * be served at all.
     */
    suspend fun ensureAdsSdkInitialized(disableNativeValidator: Boolean = false): Boolean {
        if (isSdkInitialized) return true

        return initializationMutex.withLock {
            if (isSdkInitialized) return@withLock true

            val adMobAppId: String = adMobAppIdProvider.adMobAppId() ?: run {
                Log.e(
                    LOG_TAG,
                    "Skipping Mobile Ads initialization: the host app declares no valid " +
                            "${AdMobAppIdProvider.MANIFEST_METADATA_KEY} meta-data.",
                )
                return@withLock false
            }

            withContext(dispatchers.io) {
                val config: InitializationConfig = InitializationConfig.Builder(adMobAppId)
                    .apply { if (disableNativeValidator) setNativeValidatorDisabled() }
                    .build()
                adsSdkInitializer.initialize(context, config)
            }
            isSdkInitialized = true
            AdsSdkState.markInitialized()
            true
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
        fun loadAd() {
            if (isLoadingAd || isAdAvailable()) {
                return
            }
            isLoadingAd = true
            val request = AdRequest.Builder(appOpenUnitId).build()
            AppOpenAd.load(
                request, object : AdLoadCallback<AppOpenAd> {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
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
                dataStore.adsEnabledFlow.first()
            }
            val shouldReduceAds: Boolean = withContext(dispatchers.io) {
                dataStore.reduceAds.first()
            }

            if (isShowingAd || !isAdsChecked || shouldReduceAds) {
                return
            }
            if (!isAdAvailable()) {
                onShowAdCompleteListener.onShowAdComplete()
                loadAd()
                return
            }

            appOpenAd?.adEventCallback = object : AppOpenAdEventCallback {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(fullScreenContentError: FullScreenContentError) {
                    appOpenAd = null
                    isShowingAd = false
                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd()
                }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            }
            isShowingAd = true
            appOpenAd?.show(activity)
        }
    }

    private companion object {
        const val LOG_TAG: String = "AdsCoreManager"
    }
}

