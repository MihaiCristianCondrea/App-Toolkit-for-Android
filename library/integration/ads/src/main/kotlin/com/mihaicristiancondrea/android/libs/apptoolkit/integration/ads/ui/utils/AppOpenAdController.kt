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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.utils

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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.AdMobAppIdDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.ManifestAdMobAppIdDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.ads.AdsSdkState
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.remote.AdsSdkInitializer
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.contracts.OnShowAdCompleteListener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/** How long an app open ad stays valid after loading; Google expires them after four hours. */
private val APP_OPEN_AD_LIFETIME: Duration = 4.hours

/**
 * Configures and displays App Open ads. Mutable lifecycle state uses the injected main dispatcher.
 *
 * It checks user preferences stored in [AdsSettingsRepository] to determine whether ads should be
 * shown and manages the lifecycle of an [AppOpenAd] instance.
 */
open class AppOpenAdController(
    protected val context: Context,
    private val dispatchers: DispatcherProvider,
    private val adMobAppIdDataSource: AdMobAppIdDataSource = ManifestAdMobAppIdDataSource(
        context = context,
        dispatchers = dispatchers,
    ),
    private val adsSdkInitializer: AdsSdkInitializer = AdsSdkInitializer.Default,
    private val adsSettingsRepository: AdsSettingsRepository,
    private val diagnostics: DiagnosticsReporter? = null,
) {
    private var appOpenAdManager: AppOpenAdManager? = null

    private val managerScope: CoroutineScope = CoroutineScope(context = SupervisorJob() + dispatchers.main)
    private val initializationMutex = Mutex()
    private var adsPreferenceJob: Job? = null

    @Volatile
    private var isSdkInitialized: Boolean = false

    /**
     * Initializes ads from the host-manifest [AdMobAppIdDataSource] and observes the shared
     * [AdsSettingsRepository.adsEnabled], so enabling ads later can initialize the SDK without a
     * process restart. Missing or invalid app IDs skip initialization; no fallback publisher ID
     * is supplied. A blank [appOpenUnitId] still initializes the SDK for other ad surfaces without
     * registering an App Open ad loader.
     *
     * Hosts must use this controller rather than initializing the Mobile Ads SDK separately. The
     * Next-Gen Mobile Ads SDK native ad validator overlay is disabled by default via
     * [InitializationConfig.Builder.setNativeValidatorDisabled]; pass
     * `disableNativeValidator = false` to enable it when testing native ad placements.
     */
    suspend fun initializeAds(
        appOpenUnitId: String,
        disableNativeValidator: Boolean = true,
    ) = withContext(context = dispatchers.main) {
        val isAdsChecked: Boolean = adsSettingsRepository.adsEnabled.value
        if (isAdsChecked) {
            startAds(
                appOpenUnitId = appOpenUnitId,
                disableNativeValidator = disableNativeValidator,
            )
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
            adsSettingsRepository.adsEnabled.collect { isEnabled ->
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
        if (appOpenUnitId.isBlank()) return
        if (appOpenAdManager == null) {
            appOpenAdManager = AppOpenAdManager(appOpenUnitId = appOpenUnitId)
        }
    }

    /**
     * Initializes the SDK once and publishes readiness. Ad loaders must wait for `true`;
     * loading before initialization throws. The SDK initializes on IO, as Google asks for it to
     * run off the main thread; the preference reads need no switch, since DataStore is main-safe.
     *
     * @param disableNativeValidator Disables the Next-Gen SDK native ad validator overlay.
     *   Defaults to `true` (off); pass `false` to opt in while testing native ad placements.
     * @return `false` when the host provides no valid AdMob application ID.
     */
    suspend fun ensureAdsSdkInitialized(disableNativeValidator: Boolean = true): Boolean {
        return isSdkInitialized || initializationMutex.withLock {
            if (isSdkInitialized) return@withLock true

            val adMobAppId: String = adMobAppIdDataSource.adMobAppId() ?: run {
                Log.e(
                    LOG_TAG,
                    "Skipping Mobile Ads initialization: the host app declares no valid " +
                        "${AdMobAppIdDataSource.MANIFEST_METADATA_KEY} meta-data.",
                )
                diagnostics?.logBreadcrumb(
                    message = "Mobile Ads initialization skipped: missing AdMob app ID",
                )
                return@withLock false
            }

            withContext(context = dispatchers.io) {
                val config: InitializationConfig = InitializationConfig.Builder(adMobAppId)
                    .apply { if (disableNativeValidator) setNativeValidatorDisabled() }
                    .build()
                adsSdkInitializer.initialize(context = context, config = config)
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
        showAdIfAvailable(activity = activity, scope = scope) {}
    }

    /**
     * Displays an ad if one has been loaded and notifies [onShowAdCompleteListener] once the ad
     * closes, fails to show, or is unavailable.
     *
     * The check runs inside the provided [scope] so callers can decide
     * where the asynchronous work should live.
     */
    fun showAdIfAvailable(
        activity: Activity,
        scope: CoroutineScope,
        onShowAdCompleteListener: OnShowAdCompleteListener,
    ) {
        scope.launch(context = dispatchers.main) {
            val manager: AppOpenAdManager = appOpenAdManager ?: run {
                onShowAdCompleteListener.onShowAdComplete()
                return@launch
            }
            manager.showAdIfAvailable(
                activity = activity,
                onShowAdCompleteListener = onShowAdCompleteListener,
            )
        }
    }

    /** Stops preference observation and drops later SDK callbacks when the owning graph closes. */
    fun close() {
        managerScope.cancel()
    }

    private inner class AppOpenAdManager(private val appOpenUnitId: String) {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd: Boolean = false
        var isShowingAd: Boolean = false
        private var loadMark: TimeMark? = null

        /** Loads a new ad if none is available. */
        fun loadAd() {
            if (isLoadingAd || isAdAvailable()) {
                return
            }
            isLoadingAd = true
            try {
                val request: AdRequest = AdRequest.Builder(appOpenUnitId).build()
                AppOpenAd.load(
                    request,
                    object : AdLoadCallback<AppOpenAd> {
                        override fun onAdLoaded(ad: AppOpenAd) {
                            managerScope.launch {
                                appOpenAd = ad
                                isLoadingAd = false
                                loadMark = TimeSource.Monotonic.markNow()
                            }
                        }

                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            managerScope.launch {
                                isLoadingAd = false
                            }
                        }
                    },
                )
            } catch (error: Exception) {
                isLoadingAd = false
                Log.w(LOG_TAG, "Failed to request an App Open ad.", error)
                diagnostics?.recordNonFatal(
                    throwable = error,
                    attributes = mapOf(
                        "source" to LOG_TAG,
                        "action" to "loadAd",
                    ),
                )
            }
        }

        /** Whether the loaded ad is younger than [APP_OPEN_AD_LIFETIME], after which it expires. */
        private fun wasLoadTimeLessThanNHoursAgo(): Boolean =
            loadMark?.let { mark -> mark.elapsedNow() < APP_OPEN_AD_LIFETIME } == true

        private fun isAdAvailable(): Boolean = (appOpenAd != null) && wasLoadTimeLessThanNHoursAgo()

        /** Displays the ad if available, otherwise triggers a reload. */
        suspend fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener,
        ) {
            if (isShowingAd) {
                return
            }
            if (!adsSettingsRepository.adsEnabled.value) {
                onShowAdCompleteListener.onShowAdComplete()
                return
            }

            val shouldReduceAds: Boolean = try {
                adsSettingsRepository.observeReduceAds().first()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                Log.w(LOG_TAG, "Cannot read reduce-ads preference; skipping App Open ad.", error)
                diagnostics?.recordNonFatal(
                    throwable = error,
                    attributes = mapOf(
                        "source" to LOG_TAG,
                        "action" to "observeReduceAds",
                    ),
                )
                onShowAdCompleteListener.onShowAdComplete()
                return
            }

            if (shouldReduceAds) {
                onShowAdCompleteListener.onShowAdComplete()
                return
            }
            if (!isAdAvailable()) {
                onShowAdCompleteListener.onShowAdComplete()
                loadAd()
                return
            }

            val ad: AppOpenAd = appOpenAd ?: run {
                onShowAdCompleteListener.onShowAdComplete()
                loadAd()
                return
            }

            ad.adEventCallback = object : AppOpenAdEventCallback {
                override fun onAdDismissedFullScreenContent() {
                    managerScope.launch {
                        appOpenAd = null
                        isShowingAd = false
                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd()
                    }
                }

                override fun onAdFailedToShowFullScreenContent(fullScreenContentError: FullScreenContentError) {
                    managerScope.launch {
                        appOpenAd = null
                        isShowingAd = false
                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd()
                    }
                }

                override fun onAdShowedFullScreenContent() {
                    managerScope.launch {
                        isShowingAd = true
                    }
                }
            }
            isShowingAd = true
            try {
                ad.show(activity)
            } catch (error: Exception) {
                appOpenAd = null
                isShowingAd = false
                Log.w(LOG_TAG, "Failed to display the App Open ad.", error)
                diagnostics?.recordNonFatal(
                    throwable = error,
                    attributes = mapOf(
                        "source" to LOG_TAG,
                        "action" to "showAd",
                    ),
                )
                onShowAdCompleteListener.onShowAdComplete()
                loadAd()
            }
        }
    }

    private companion object {
        const val LOG_TAG: String = "AppOpenAdController"
    }
}
