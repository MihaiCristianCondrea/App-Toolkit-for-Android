package com.d4rk.android.libs.apptoolkit.data.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDexApplication
import com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.koin.android.ext.android.inject

open class BaseCoreManager : MultiDexApplication(), Application.ActivityLifecycleCallbacks, LifecycleObserver {

    protected val billingRepository: BillingRepository by inject()
    protected open val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val applicationScope = CoroutineScope(SupervisorJob() + ioDispatcher)

    companion object {
        var isAppLoaded : Boolean = false
            private set
    }

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

    private suspend fun initializeApp() = supervisorScope {
        val appComponentsInitialization: Deferred<Unit> = async { onInitializeApp() }

        appComponentsInitialization.await()

        finalizeInitialization()
    }

    protected open suspend fun onInitializeApp() {}

    private fun finalizeInitialization() {
        isAppLoaded = true
    }

    override fun onActivityCreated(activity : Activity , savedInstanceState : Bundle?) {}
    override fun onActivityStarted(activity : Activity) {}
    override fun onActivityResumed(activity : Activity) {}
    override fun onActivityPaused(activity : Activity) {}
    override fun onActivityStopped(activity : Activity) {}
    override fun onActivitySaveInstanceState(activity : Activity , outState : Bundle) {}
    override fun onActivityDestroyed(activity : Activity) {}

    override fun onTerminate() {
        super.onTerminate()
        billingRepository.close()
        CommonDataStore.getInstance(this).close()
        applicationScope.cancel()
    }
}