package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.di

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.ThemeSettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val themeSettingsModule: Module = module {
    viewModel { ThemeSettingsViewModel(preferences = get()) }
}
