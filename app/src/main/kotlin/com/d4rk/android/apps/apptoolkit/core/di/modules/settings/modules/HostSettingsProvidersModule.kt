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

package com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules

import com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.providers.AppAboutSettingsProvider
import com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.providers.AppAdvancedSettingsProvider
import com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.providers.AppSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AdvancedSettingsProvider
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Host-only provider bindings consumed by toolkit settings modules.
 */
val hostSettingsProvidersModule: Module = module {
    single<SettingsProvider> { AppSettingsProvider(context = get()) }
    single<AboutSettingsProvider> { AppAboutSettingsProvider(context = get()) }
    single<AdvancedSettingsProvider> { AppAdvancedSettingsProvider(context = get()) }
}
