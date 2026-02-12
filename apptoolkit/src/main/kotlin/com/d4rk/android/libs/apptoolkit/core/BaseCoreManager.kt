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

package com.d4rk.android.libs.apptoolkit.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDexApplication
import com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository
import com.d4rk.android.libs.apptoolkit.core.BaseCoreManager.Companion.isAppLoaded
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.di.StandardDispatchers
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
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

    protected val billingRepository: BillingRepository by inject()
    protected open val dispatchers: DispatcherProvider = StandardDispatchers()
    private val applicationScope = CoroutineScope(SupervisorJob() + dispatchers.io)

    companion object {
        /** Flag indicating whether the application finished its startup work. */
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
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        registerActivityLifecycleCallbacks(this)
        applicationScope.launch {
            initializeApp()
        }
    }

    /**
     * Executes [onInitializeApp] inside a supervisor scope and marks the
     * application as loaded once completed.
     */
    private suspend fun initializeApp() = supervisorScope {
        val appComponentsInitialization: Deferred<Unit> = async { onInitializeApp() }

        appComponentsInitialization.await()

        finalizeInitialization()
    }

    /**
     * Hook for subclasses to perform additional initialization work.
     *
     * Runs on a background coroutine context provided by [dispatchers].
     */
    protected open suspend fun onInitializeApp() {}

    /** Marks the application as fully initialized. */
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

    /**
     * Cleans up resources when the process is terminating.
     */
    override fun onTerminate() {
        super.onTerminate()
        billingRepository.close()
        CommonDataStore.getInstance(this).close()
        applicationScope.cancel()
    }
}