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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.providers.StartupProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.di.billingModule
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.di.firebaseModule
import org.koin.core.module.Module

/**
 * Every toolkit-owned Koin definition, in one list.
 *
 * A host that loads this receives the complete toolkit-owned graph. It does not need to know that
 * billing lives in `:library:integration:billing` or that Firebase lives in
 * `:library:integration:firebase`, adding a module to the toolkit adds it here, not in every
 * host. Host-owned provider contracts are still supplied in modules loaded after this list; they
 * include settings, about, display, advanced, and privacy providers used by reusable screens.
 *
 * Why this exists: the modules used to be handed out as four separate entry points plus two loose
 * `Module` values, and a host had to remember all six. Missing one produced no build error and no
 * warning, just a `NoDefinitionFoundException` the first time the app touched that dependency at
 * runtime. `billingModule` was missed exactly this way.
 *
 * ## Overriding toolkit defaults
 *
 * Koin's `allowOverride` defaults to true, so **definitions loaded later win**. Load the toolkit
 * first and your own modules after it, and any binding you declare replaces the toolkit's:
 *
 * ```
 * startKoin {
 *     androidContext(context)
 *     modules(
 *         buildList {
 *             addAll(appToolkitModules(hostBuildConfig, ::AppStartupProvider))
 *             // Everything below overrides the toolkit defaults above.
 *             add(hostSettingsProvidersModule)  // your DisplaySettingsProvider, etc.
 *             add(appModule)
 *         }
 *     )
 * }
 * ```
 *
 * This is how a host supplies `SettingsProvider`, `PrivacySettingsProvider`, other host provider
 * contracts, custom settings content, or a different `FirebaseController` without the toolkit
 * needing to know the implementation.
 *
 * @param hostBuildConfig Host build values the toolkit cannot read from its own BuildConfig.
 * @param startupProviderFactory Produces the host's startup screen provider.
 */
fun appToolkitModules(
    hostBuildConfig: AppToolkitHostBuildConfig,
    startupProviderFactory: () -> StartupProvider,
): List<Module> = buildList {
    addAll(appToolkitFoundationModules(hostBuildConfig = hostBuildConfig))
    add(firebaseModule)
    add(billingModule)
    addAll(appToolkitSettingsModules())
    addAll(
        appToolkitFeatureModules(
            hostBuildConfig = hostBuildConfig,
            startupProviderFactory = startupProviderFactory,
        )
    )
}
