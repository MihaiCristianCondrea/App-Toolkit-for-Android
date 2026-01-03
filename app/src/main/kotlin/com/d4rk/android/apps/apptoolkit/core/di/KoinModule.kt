package com.d4rk.android.apps.apptoolkit.core.di

import android.content.Context
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.adsModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.appModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.onboardingModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.startupModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.apptoolkit.appToolkitModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.dispatchersModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.settingsModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.themeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// TODO: Separate all modules better (per-feature some of them)
fun initializeKoin(context: Context) {
    startKoin {
        androidContext(androidContext = context)
        modules(
            modules = listOf(
                appModule,
                settingsModule,
                adsModule,
                appToolkitModule,
                themeModule,
                dispatchersModule,
                onboardingModule,
                startupModule,
            )
        )
    }
}
