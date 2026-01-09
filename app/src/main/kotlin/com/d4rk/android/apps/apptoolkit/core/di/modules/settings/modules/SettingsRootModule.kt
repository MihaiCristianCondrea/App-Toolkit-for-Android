package com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules

import com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.providers.AppSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.SettingsViewModel
import com.d4rk.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsRootModule: Module = module {
    single<SettingsProvider> { AppSettingsProvider() }

    viewModel {
        SettingsViewModel(
            settingsProvider = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}
