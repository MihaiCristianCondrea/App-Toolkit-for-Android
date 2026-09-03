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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.di

import com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.settings.AppAboutSettingsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.settings.AppAdvancedSettingsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.settings.AppDisplaySettingsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.settings.AppPrivacySettingsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.settings.AppSettingsProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.startup.AppStartupProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.blue.bluePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.di.modules.appToolkitModules
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.settings.utils.providers.AboutSettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.settings.utils.providers.PrivacySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.utils.interfaces.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.utils.providers.AdvancedSettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.utils.providers.DisplaySettingsProvider
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * The App Toolkit's Koin graph, with the sample's answers to every extension point it asks about.
 *
 * This is the one call a host makes, and it is what the sample is demonstrating: an app adopting
 * the toolkit needs a module like this one, not a scattering of provider classes and bindings.
 *
 * The ordering matters and is the reason it lives here rather than being assembled at the call
 * site: the toolkit's own modules go first, and host bindings follow. Koin's later definitions win
 * when the sample intentionally replaces a toolkit binding, while contracts with no toolkit
 * default are satisfied by this module.
 */
fun appToolkitHostModules(hostBuildConfig: AppToolkitHostBuildConfig): List<Module> = buildList {
    addAll(
        appToolkitModules(
            hostBuildConfig = hostBuildConfig,
            startupProviderFactory = ::AppStartupProvider,
        )
    )
    add(appToolkitProvidersModule(hostBuildConfig = hostBuildConfig))
}

/**
 * The sample's implementations of the provider interfaces the toolkit looks up.
 *
 * Every binding here answers a question the library asks of its host: what the settings screen
 * lists, what "about" says, what the default theme is.
 */
internal fun appToolkitProvidersModule(hostBuildConfig: AppToolkitHostBuildConfig): Module =
    module {
        single<SettingsProvider> { AppSettingsProvider(context = get()) }
        single<AboutSettingsProvider> {
            AppAboutSettingsProvider(context = get(), hostBuildConfig = hostBuildConfig)
        }
        single<AdvancedSettingsProvider> { AppAdvancedSettingsProvider(context = get()) }
        single<DisplaySettingsProvider> { AppDisplaySettingsProvider(context = get()) }
        single<PrivacySettingsProvider> { AppPrivacySettingsProvider(context = get()) }
        single<ColorPalette>(named(AppToolkitDiConstants.DEFAULT_THEME_PALETTE)) { bluePalette }
    }
