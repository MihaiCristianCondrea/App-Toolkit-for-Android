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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.di

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.data.repositories.ShowcaseUnlockRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.providers.AppAboutSettingsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.providers.AppDisplaySettingsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.providers.AppPrivacySettingsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.providers.AppSettingsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.views.AboutSettingsContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.providers.AboutSettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.data.repositories.GeneralSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.general.GeneralSettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers.GeneralSettingsContentProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * The sample's settings surface, and its answers to the toolkit's settings extension points.
 *
 * The provider implementations are bound here rather than in the host composition root because
 * they live here: a module that owns a settings surface owns what that surface shows. The root
 * still decides ordering, it adds this module after the toolkit's own, so these bindings win.
 */
fun settingsModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<SettingsProvider> { AppSettingsProvider(context = get()) }
    single<AboutSettingsProvider> {
        AppAboutSettingsProvider(context = get(), hostBuildConfig = hostBuildConfig)
    }
    single<DisplaySettingsProvider> { AppDisplaySettingsProvider(context = get()) }
    single<PrivacySettingsProvider> { AppPrivacySettingsProvider(context = get()) }

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
