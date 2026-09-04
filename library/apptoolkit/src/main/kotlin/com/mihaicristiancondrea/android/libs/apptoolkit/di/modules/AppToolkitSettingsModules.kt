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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.di.advancedSettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.di.settingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.di.diagnosticsSettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.di.displaySettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.di.themeSettingsModule
import org.koin.core.module.Module

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
    advancedSettingsModule,
    diagnosticsSettingsModule,
    displaySettingsModule,
    themeSettingsModule,
)
