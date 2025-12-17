package com.d4rk.android.apps.apptoolkit.core.di

import android.content.Context
import com.d4rk.android.apps.apptoolkit.core.di.modules.adsModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.appModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.appToolkitModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.dispatchersModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settingsModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.themeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun initializeKoin(context : Context) {
    startKoin {
        androidContext(androidContext = context)
        modules(
            modules = listOf(
                appModule,
                settingsModule,
                adsModule,
                appToolkitModule,
                themeModule,
                dispatchersModule
            )
        )
    }
}