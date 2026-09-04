package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.di

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.DisplaySettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val displaySettingsModule: Module = module {
    viewModel {
        DisplaySettingsViewModel(
            displayPreferences = get(),
            themePreferences = get(),
        )
    }
}
