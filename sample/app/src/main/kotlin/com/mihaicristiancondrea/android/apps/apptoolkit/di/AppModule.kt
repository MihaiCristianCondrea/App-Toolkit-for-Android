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

package com.mihaicristiancondrea.android.apps.apptoolkit.di

import com.mihaicristiancondrea.android.apps.apptoolkit.app.integration.components.AppAboutSettingsContent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.providers.GeneralSettingsContentProvider
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Main application module for `:sample:app`.
 *
 * Handles cross-feature integrations and app-specific implementations of toolkit extension points.
 */
val appModule: Module = module {
    // Bound here rather than in :sample:feature:settings because the About content it injects is
    // the Settings-to-Components bridge, and cross-feature composition is the app's job. Only one
    // module may bind this type: a second, unqualified binding silently overrides this one and
    // takes the version-tap handler with it.
    factory<GeneralSettingsContentProvider> {
        GeneralSettingsContentProvider(
            aboutContent = { paddingValues, snackbarHostState ->
                AppAboutSettingsContent(
                    paddingValues = paddingValues,
                    snackbarHostState = snackbarHostState,
                )
            },
        )
    }
}
