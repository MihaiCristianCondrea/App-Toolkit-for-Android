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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.blue.bluePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.di.modules.appToolkitModules
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReporterConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.providers.StartupProvider
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Orders the Toolkit graph, host-wide defaults, and feature provider modules supplied by the app.
 *
 * This is the one call a host makes for the toolkit itself. Its job is ordering: the toolkit's own
 * modules go first, so the host modules added after it win wherever Koin's later definition should
 * replace a toolkit binding.
 */
fun appToolkitHostModules(
    hostBuildConfig: AppToolkitHostBuildConfig,
    startupProviderFactory: () -> StartupProvider,
    hostProviderModules: List<Module>,
): List<Module> = buildList {
    addAll(
        appToolkitModules(
            hostBuildConfig = hostBuildConfig,
            startupProviderFactory = startupProviderFactory,
            // The sample turns the gesture on because it is what the sample is for: showing a host
            // what the toolkit offers. It stays off by default for everyone else.
            issueReporterConfig = IssueReporterConfig(shakeToReportEnabled = true),
        )
    )
    add(appToolkitProvidersModule())
    addAll(hostProviderModules)
}

/**
 * The host-wide toolkit answers that belong to no single feature.
 *
 * The default theme palette is an application-level choice. Privacy uses the Toolkit's default
 * provider until the sample supplies its own privacy feature.
 */
internal fun appToolkitProvidersModule(): Module =
    module {
        single<ColorPalette>(named(AppToolkitDiConstants.DEFAULT_THEME_PALETTE)) { bluePalette }
        single<PrivacySettingsProvider> { object : PrivacySettingsProvider {} }
    }
