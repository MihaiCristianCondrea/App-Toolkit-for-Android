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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.startup.AppStartupProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.google.blue.bluePalette
import com.mihaicristiancondrea.android.libs.apptoolkit.di.modules.appToolkitModules
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReporterConfig
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * The App Toolkit's Koin graph, with the sample's answers to every extension point it asks about.
 *
 * This is the one call a host makes for the toolkit itself. Its job is ordering: the toolkit's own
 * modules go first, so the host modules added after it win wherever Koin's later definition should
 * replace a toolkit binding.
 *
 * The settings extension points the toolkit asks about are answered by `:sample:feature:settings`,
 * which owns the surfaces they configure. This module cannot bind them: a `:sample:core:` module
 * may not depend on a feature, which `ModuleBoundariesPlugin` enforces.
 */
fun appToolkitHostModules(hostBuildConfig: AppToolkitHostBuildConfig): List<Module> = buildList {
    addAll(
        appToolkitModules(
            hostBuildConfig = hostBuildConfig,
            startupProviderFactory = ::AppStartupProvider,
            // The sample turns the gesture on because it is what the sample is for: showing a host
            // what the toolkit offers. It stays off by default for everyone else.
            issueReporterConfig = IssueReporterConfig(shakeToReportEnabled = true),
        )
    )
    add(appToolkitProvidersModule())
}

/**
 * The host-wide toolkit answers that belong to no single feature.
 *
 * Currently the default theme palette: the toolkit asks every host which one to start from, and
 * that is an application-level choice rather than a settings one.
 */
internal fun appToolkitProvidersModule(): Module =
    module {
        single<ColorPalette>(named(AppToolkitDiConstants.DEFAULT_THEME_PALETTE)) { bluePalette }
    }
