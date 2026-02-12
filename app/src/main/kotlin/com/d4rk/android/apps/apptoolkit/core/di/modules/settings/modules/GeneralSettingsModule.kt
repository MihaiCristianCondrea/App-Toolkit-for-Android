/*
 * Copyright (c) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules

import com.d4rk.android.apps.apptoolkit.app.components.domain.usecase.UnlockComponentsShowcaseUseCase
import com.d4rk.android.apps.apptoolkit.app.components.ui.ComponentsUnlockViewModel
import com.d4rk.android.apps.apptoolkit.app.settings.about.ui.AppAboutSettingsContent
import com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.providers.AppDisplaySettingsProvider
import com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.providers.AppPrivacySettingsProvider
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
    single<UnlockComponentsShowcaseUseCase> { UnlockComponentsShowcaseUseCase(dataStore = get()) }
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
    single<GeneralSettingsRepository> { GeneralSettingsRepositoryImpl(firebaseController = get()) }

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
