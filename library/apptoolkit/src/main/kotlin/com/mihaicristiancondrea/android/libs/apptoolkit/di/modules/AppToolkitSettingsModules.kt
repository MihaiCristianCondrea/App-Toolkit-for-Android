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

package com.mihaicristiancondrea.android.libs.apptoolkit.di.modules

import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.data.repositories.AboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.data.repositories.DefaultAboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.domain.usecases.CopyDeviceInfoUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.ui.AboutViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.advanced.data.repositories.CacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.advanced.data.repositories.DefaultCacheRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.advanced.ui.AdvancedSettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.diagnostics.data.repositories.DefaultUsageAndDiagnosticsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.diagnostics.data.repositories.UsageAndDiagnosticsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.diagnostics.ui.UsageAndDiagnosticsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.display.ui.DisplaySettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.permissions.data.repositories.DefaultPermissionsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.permissions.data.repositories.PermissionsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.permissions.ui.PermissionsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.settings.ui.SettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.ThemeSettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.colors.google.blue.bluePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.colors.google.green.greenPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.colors.google.red.redPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.colors.google.yellow.yellowPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.colors.monochrome.monochromePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.colors.rose.rosePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.colors.special.christmas.christmasPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.colors.special.skin.skinPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
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
    displaySettingsModule(),
    themeModule(),
)

private fun displaySettingsModule(): Module = module {
    viewModel {
        DisplaySettingsViewModel(
            displayPreferences = get(),
            themePreferences = get(),
        )
    }
}

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
        DefaultAboutRepository(
            deviceProvider = get(),
            buildInfoProvider = get(),
            context = get(),
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
            aboutRepository = get(),
            copyDeviceInfo = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}

private fun advancedSettingsModule(): Module = module {
    single<CacheRepository> { DefaultCacheRepository(context = get(), firebaseController = get()) }

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
        DefaultPermissionsRepository(
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
        DefaultUsageAndDiagnosticsRepository(
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
            consentRepository = get(),
        )
    }
}

private fun themeModule(): Module = module {
    viewModel { ThemeSettingsViewModel(preferences = get()) }
    single<ColorPalette>(named(AppToolkitDiConstants.MONOCHROME_THEME_PALETTE)) { monochromePalette }
    single<ColorPalette>(named(AppToolkitDiConstants.BLUE_THEME_PALETTE)) { bluePalette }
    single<ColorPalette>(named(AppToolkitDiConstants.GREEN_THEME_PALETTE)) { greenPalette }
    single<ColorPalette>(named(AppToolkitDiConstants.RED_THEME_PALETTE)) { redPalette }
    single<ColorPalette>(named(AppToolkitDiConstants.YELLOW_THEME_PALETTE)) { yellowPalette }
    single<ColorPalette>(named(AppToolkitDiConstants.ROSE_THEME_PALETTE)) { rosePalette }
    single<ColorPalette>(named(AppToolkitDiConstants.CHRISTMAS_THEME_PALETTE)) { christmasPalette }
    single<ColorPalette>(named(AppToolkitDiConstants.SKIN_THEME_PALETTE)) { skinPalette }
    single<ColorPalette> {
        getOrNull<ColorPalette>(named(AppToolkitDiConstants.DEFAULT_THEME_PALETTE))
            ?: get(named(AppToolkitDiConstants.BLUE_THEME_PALETTE))
    }
}
