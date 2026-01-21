package com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules

import com.d4rk.android.apps.apptoolkit.app.settings.about.ui.AppAboutSettingsContent
import com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.providers.AppDisplaySettingsProvider
import com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.providers.AppPrivacySettingsProvider
import com.d4rk.android.apps.apptoolkit.app.components.domain.usecase.UnlockComponentsShowcaseUseCase
import com.d4rk.android.apps.apptoolkit.app.components.ui.ComponentsUnlockViewModel
import com.d4rk.android.libs.apptoolkit.app.settings.general.data.repository.GeneralSettingsRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository.GeneralSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.GeneralSettingsViewModel
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.DisplaySettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.GeneralSettingsContentProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.PrivacySettingsProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val generalSettingsModule: Module = module {
    single<DisplaySettingsProvider> { AppDisplaySettingsProvider(context = get()) }
    single<PrivacySettingsProvider> { AppPrivacySettingsProvider(context = get()) }
    single { UnlockComponentsShowcaseUseCase(dataStore = get()) }
    single<GeneralSettingsContentProvider> {
        GeneralSettingsContentProvider(
            aboutContent = { paddingValues, snackbarHostState ->
                AppAboutSettingsContent(
                    paddingValues = paddingValues,
                    snackbarHostState = snackbarHostState,
                )
            }
        )
    }
    single<GeneralSettingsRepository> { GeneralSettingsRepositoryImpl() }

    viewModel {
        GeneralSettingsViewModel(
            repository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        ComponentsUnlockViewModel(
            unlockComponentsShowcase = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}
