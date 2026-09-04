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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.di.aboutModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.data.repositories.DefaultPermissionsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.data.repositories.PermissionsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.PermissionsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.di.advancedSettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.di.settingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.di.diagnosticsSettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.di.displaySettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.di.themeSettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.blue.bluePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.green.greenPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.red.redPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.yellow.yellowPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.monochrome.monochromePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.rose.rosePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.special.christmas.christmasPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.special.skin.skinPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Settings-related modules owned by the library.
 *
 * Host apps provide `SettingsProvider`, `AboutSettingsProvider`, `DisplaySettingsProvider`,
 * `AdvancedSettingsProvider`, and `PrivacySettingsProvider` implementations in modules loaded with
 * these reusable toolkit bindings. Some are resolved from composables rather than constructors, so
 * constructor-only Koin verification cannot discover every requirement.
 */
fun appToolkitSettingsModules(): List<Module> = listOf(
    settingsModule,
    aboutModule,
    advancedSettingsModule,
    permissionsModule(),
    diagnosticsSettingsModule,
    displaySettingsModule,
    themeSettingsModule,
    themePaletteModule(),
)

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

private fun themePaletteModule(): Module = module {
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
