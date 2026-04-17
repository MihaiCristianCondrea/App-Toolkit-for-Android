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

package com.d4rk.android.libs.apptoolkit.core.di.modules

import com.d4rk.android.libs.apptoolkit.app.about.data.repository.AboutRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.CopyDeviceInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.domain.usecases.GetAboutInfoUseCase
import com.d4rk.android.libs.apptoolkit.app.about.ui.AboutViewModel
import com.d4rk.android.libs.apptoolkit.app.advanced.data.repository.CacheRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.d4rk.android.libs.apptoolkit.app.advanced.ui.AdvancedSettingsViewModel
import com.d4rk.android.libs.apptoolkit.app.diagnostics.data.repository.UsageAndDiagnosticsRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.repository.UsageAndDiagnosticsRepository
import com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.UsageAndDiagnosticsViewModel
import com.d4rk.android.libs.apptoolkit.app.permissions.data.repository.PermissionsRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.permissions.domain.repository.PermissionsRepository
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.PermissionsViewModel
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.SettingsViewModel
import com.d4rk.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.ColorPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.blue.bluePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.green.greenPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.red.redPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.google.yellow.yellowPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.monochrome.monochromePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.rose.rosePalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.special.christmas.christmasPalette
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.colors.special.skin.skinPalette
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Settings-related modules owned by the library.
 *
 * Host apps provide `SettingsProvider`, `AboutSettingsProvider`, and `AdvancedSettingsProvider`
 * implementations in their own module and can then load these reusable toolkit bindings.
 */
fun appToolkitSettingsModules(): List<Module> = listOf(
    settingsRootModule(),
    aboutModule(),
    advancedSettingsModule(),
    permissionsModule(),
    usageAndDiagnosticsModule(),
    themeModule(),
)

private fun settingsRootModule(): Module = module {
    viewModel {
        SettingsViewModel(
            settingsProvider = get<SettingsProvider>(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}

private fun aboutModule(): Module = module {
    single<AboutRepository> {
        AboutRepositoryImpl(
            deviceProvider = get(),
            buildInfoProvider = get(),
            context = get(),
            firebaseController = get(),
        )
    }
    single<GetAboutInfoUseCase> {
        GetAboutInfoUseCase(
            repository = get(),
            firebaseController = get(),
        )
    }
    single<CopyDeviceInfoUseCase> {
        CopyDeviceInfoUseCase(
            repository = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        AboutViewModel(
            getAboutInfo = get(),
            copyDeviceInfo = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}

private fun advancedSettingsModule(): Module = module {
    single<CacheRepository> { CacheRepositoryImpl(context = get(), firebaseController = get()) }

    viewModel {
        AdvancedSettingsViewModel(
            repository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}

private fun permissionsModule(): Module = module {
    single<PermissionsRepository> {
        PermissionsRepositoryImpl(
            context = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        PermissionsViewModel(
            permissionsRepository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}

private fun usageAndDiagnosticsModule(): Module = module {
    single<UsageAndDiagnosticsRepository> {
        UsageAndDiagnosticsRepositoryImpl(
            dataSource = get<CommonDataStore>(),
            configProvider = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        UsageAndDiagnosticsViewModel(
            repository = get(),
            firebaseController = get(),
            dispatchers = get(),
            applyConsentSettingsUseCase = get(),
        )
    }
}

private fun themeModule(): Module = module {
    single<ColorPalette>(named("monochromePalette")) { monochromePalette }
    single<ColorPalette>(named("bluePalette")) { bluePalette }
    single<ColorPalette>(named("greenPalette")) { greenPalette }
    single<ColorPalette>(named("redPalette")) { redPalette }
    single<ColorPalette>(named("yellowPalette")) { yellowPalette }
    single<ColorPalette>(named("rosePalette")) { rosePalette }
    single<ColorPalette>(named("christmasPalette")) { christmasPalette }
    single<ColorPalette>(named("skinPalette")) { skinPalette }
    single<ColorPalette> { get(named("bluePalette")) }
}
