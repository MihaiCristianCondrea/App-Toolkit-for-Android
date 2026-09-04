package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.di

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.OnboardingThemeViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.providers.StartupProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.startup.StartupViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun onboardingModule(startupProviderFactory: () -> StartupProvider): Module = module {
    single<StartupProvider> { startupProviderFactory() }

    viewModel { StartupViewModel(firebaseController = get()) }
    viewModel { OnboardingThemeViewModel(preferences = get()) }
}
