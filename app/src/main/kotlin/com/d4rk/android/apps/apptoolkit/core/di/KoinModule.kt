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

// TODO: Add each module into its own .kt file (FOR ALL MODULES)
fun initializeKoin(context: Context) {
    startKoin {
        androidContext(androidContext = context)
        modules(
            modules = buildList {
                add(appModule)
                addAll(settingsModules) // FIXME: <html>Overload resolution ambiguity between candidates:<br/>val settingsModules: List&lt;Module&gt;<br/>val settingsModules: &lt;implicit&gt;
                add(adsModule)
                addAll(appToolkitModules) // FIXME: <html>Overload resolution ambiguity between candidates:<br/>val appToolkitModules: &lt;implicit&gt;<br/>val appToolkitModules: &lt;implicit&gt;
                add(themeModule)
                add(dispatchersModule)
                add(onboardingModule)
                add(startupModule)
            }
        )
    }
}
