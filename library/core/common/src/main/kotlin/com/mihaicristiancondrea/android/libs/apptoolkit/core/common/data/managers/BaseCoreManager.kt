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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.managers

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDexApplication
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.StandardDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.CommonDataStoreCore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BillingCore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.crash.ConsentSdkCrashGuard
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.koin.android.ext.android.inject

/**
 * Base application class providing common initialization for the toolkit.
 *
 * It installs Firebase App Check, registers activity lifecycle callbacks and
 * launches asynchronous initialization work inside an application wide
 * coroutine scope. Subclasses can override [onInitializeApp] to perform
 * additional setup before the app is marked as ready via [isAppLoaded].
 */
open class BaseCoreManager : MultiDexApplication(), Application.ActivityLifecycleCallbacks,
    LifecycleObserver {

    protected val billingRepository: BillingCore by inject()
    private val telemetryRepository: TelemetryRepository by inject()
    protected val dataStore: CommonDataStoreCore by inject()
    protected open val dispatchers: DispatcherProvider = StandardDispatchers()

    /**
     * Defers reading the open [dispatchers] property until subclass initialization has
     * completed.
     */
    private val applicationScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + dispatchers.io)
    }

    /**
     * Whether [ConsentSdkCrashGuard] is installed for this app.
     *
     * Override with `false` to opt out. The guard swallows exactly one failure, a telemetry ping
     * inside the UMP SDK whose empty error body makes `Scanner.next()` throw on the SDK's own
     * executor, and swallowing it has no functional effect on the consent flow. Every other
     * throwable is delegated to the handler that was installed before it.
     */
    protected open val installsConsentSdkCrashGuard: Boolean = true

    companion object {
        /**
         * Flag indicating whether the application finished its startup work.
         *
         * Written from a background coroutine and read from the main thread, hence volatile.
         */
        @Volatile
        var isAppLoaded: Boolean = false
            private set
    }

    /**
     * Initializes Firebase and kicks off asynchronous app initialization.
     *
     * Subclasses should avoid heavy work here and instead override
     * [onInitializeApp] which runs on a background coroutine.
     */
    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(context = this)
        installConsentSdkCrashGuard()
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        registerActivityLifecycleCallbacks(this)
        applicationScope.launch {
            initializeApp()
        }
    }

    /**
     * Installs the guard after Firebase initialization so unrecognized failures still reach
     * Crashlytics through its existing uncaught-exception handler.
     */
    private fun installConsentSdkCrashGuard() {
        if (!installsConsentSdkCrashGuard) return
        ConsentSdkCrashGuard.install { throwable, attributes ->
            telemetryRepository.recordNonFatal(throwable = throwable, attributes = attributes)
        }
    }

    /**
     * Runs host initialization in a supervisor scope. Non-cancellation failures are reported
     * and startup still completes, so callers waiting on [isAppLoaded] do not wait
     * indefinitely.
     */
    private suspend fun initializeApp() = supervisorScope {
        val appComponentsInitialization: Deferred<Unit> = async { onInitializeApp() }

        try {
            appComponentsInitialization.await()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (throwable: Throwable) {
            telemetryRepository.recordNonFatal(
                throwable = throwable,
                attributes = mapOf("phase" to "onInitializeApp"),
            )
        }

        finalizeInitialization()
    }

    /**
     * Hook for subclasses to perform additional initialization work.
     *
     * Runs on a background coroutine context provided by [dispatchers].
     */
    protected open suspend fun onInitializeApp() {}

    private fun finalizeInitialization() {
        isAppLoaded = true
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    override fun onTerminate() {
        super.onTerminate()
        billingRepository.close()
        dataStore.close()
        applicationScope.cancel()
    }
}
