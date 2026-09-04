package com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.di

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.SettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers.SettingsProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule: Module = module {
    viewModel {
        SettingsViewModel(
            settingsProvider = get<SettingsProvider>(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}
