/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.settings.di

import com.mihaicristiancondrea.android.apps.apptoolkit.app.settings.data.repositories.ShowcaseUnlockRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.settings.ui.views.AboutSettingsContent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.data.repositories.GeneralSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui.GeneralSettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.providers.GeneralSettingsContentProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule: Module = module {
    single {
        ShowcaseUnlockRepository(
            dataStore = get(),
            firebaseController = get(),
        )
    }

    factory {
        GeneralSettingsContentProvider(
            aboutContent = { paddingValues, snackbarHostState ->
                AboutSettingsContent(
                    paddingValues = paddingValues,
                    snackbarHostState = snackbarHostState,
                )
            },
        )
    }

    single {
        GeneralSettingsRepository(
            firebaseController = get(),
            appStatePreferences = get(),
        )
    }

    viewModel {
        GeneralSettingsViewModel(
            repository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}
