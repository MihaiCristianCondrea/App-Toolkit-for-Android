package com.d4rk.android.apps.apptoolkit.core.di

import android.content.Context
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.adsModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.appModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.onboardingModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.startupModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.apptoolkit.appToolkitModules
import com.d4rk.android.apps.apptoolkit.core.di.modules.dispatchersModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.settingsModules
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.themeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun initializeKoin(context: Context) {
    startKoin {
        androidContext(androidContext = context)
        modules(
            modules = buildList {
                add(appModule)
                addAll(settingsModules)
                add(adsModule)
                addAll(appToolkitModules)
                add(themeModule)
                add(dispatchersModule)
                add(onboardingModule)
                add(startupModule)
            }
        )
    }
}
