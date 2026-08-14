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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.di

import android.app.Activity
import android.content.Context
import androidx.compose.material3.ColorScheme
import com.mihaicristiancondrea.android.apps.apptoolkit.app.startup.utils.interfaces.providers.AppStartupProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.adsModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.appModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.appsListModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.dataStoreModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.onboardingModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.startupModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.settings.modules.generalSettingsModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.settings.modules.hostSettingsProvidersModule
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.interfaces.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.providers.AdvancedSettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.di.modules.appToolkitFeatureModules
import com.mihaicristiancondrea.android.libs.apptoolkit.di.modules.appToolkitFoundationModules
import com.mihaicristiancondrea.android.libs.apptoolkit.di.modules.appToolkitSettingsModules
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.di.billingModule
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.di.firebaseModule
import io.ktor.client.engine.HttpClientEngine
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.verify.verify

/**
 * Verifies the dependency graph the app actually starts with.
 *
 * A Koin definition that cannot be created does not fail at startup — it fails the first time
 * something asks for it. In practice that is `MainActivity.onCreate` resolving `MainViewModel`,
 * which reports as `Unable to start activity` with an `InstanceCreationException` naming only the
 * outermost ViewModel and the innermost definition. The app is dead before its first frame and the
 * stack trace does not say which dependency was missing.
 *
 * Nothing verified this graph before: `koin-test-junit5` was already on every module's test
 * classpath and unused, so the only thing that ever exercised the wiring was launching the app.
 *
 * These tests are cheap because they never touch Android. `verify` resolves each definition's
 * declared type against the other definitions by reflection, so a dependency that no module
 * provides fails here rather than on a user's device.
 */
class HostKoinGraphTest {

    /**
     * Types Koin supplies at runtime that no module declares.
     *
     * `Context` comes from `androidContext()`, and `Activity` is passed as a parameter at call
     * sites. Listing them is what lets the rest of the graph be checked without an Android runtime.
     */
    private val platformTypes = listOf(Context::class, Activity::class)

    /**
     * Constructor parameters of types Koin builds through a factory function rather than by
     * constructor injection.
     *
     * `verify` reflects on the produced type's constructor regardless of how the definition builds
     * it, so `HttpClient` reads as needing an `HttpClientEngine` and `ColorPalette` as needing two
     * `ColorScheme`s. Neither is ever resolved from Koin: `KtorClient.createClient` configures its
     * own engine, and the palette is a literal. Listing them here is narrower than disabling the
     * check for those definitions.
     */
    private val builtByFactoryFunction = listOf(HttpClientEngine::class, ColorScheme::class)

    /**
     * What the toolkit requires a host to bind.
     *
     * These are extension points, not gaps: the toolkit ships settings screens whose content each
     * host supplies. `:sample:app` binds all four in `hostSettingsProvidersModule`.
     *
     * Adding a type here means the toolkit gained a new integration requirement, and every existing
     * host has to bind it or crash at first resolution. That is worth being explicit about, which is
     * why this list exists rather than the toolkit check simply being skipped.
     */
    private val hostExtensionPoints = listOf(
        SettingsProvider::class,
        AboutSettingsProvider::class,
        AdvancedSettingsProvider::class,
        ColorPalette::class,
    )

    private val hostBuildConfig = AppToolkitHostBuildConfig(
        applicationId = "com.mihaicristiancondrea.android.apps.apptoolkit",
        isDebugBuild = true,
        versionName = "1.0.0",
        versionCode = 1L,
        githubToken = "",
        faqProductId = "app-toolkit",
    )

    /**
     * Mirrors `initializeKoin`. If that function gains a module, this list has to gain it too —
     * which is the point: the check is only as good as its agreement with production.
     */
    private fun hostModules(): List<Module> = buildList {
        addAll(appToolkitFoundationModules(hostBuildConfig = hostBuildConfig))
        add(firebaseModule)
        add(billingModule)
        add(dataStoreModule)
        add(appModule)
        add(hostSettingsProvidersModule)
        addAll(appToolkitSettingsModules())
        add(generalSettingsModule)
        add(adsModule)
        add(appsListModule)
        addAll(
            appToolkitFeatureModules(
                hostBuildConfig = hostBuildConfig,
                startupProviderFactory = ::AppStartupProvider,
            )
        )
        add(onboardingModule)
        add(startupModule)
    }

    @Test
    fun `every host definition can be resolved`() {
        // Wrapped with `includes` rather than verified as a flat list: Koin resolves a
        // definition's dependencies against its own module plus that module's includes, so a list
        // of sibling modules cannot see each other and every cross-module dependency reads as
        // missing.
        module { includes(hostModules()) }
            .verify(extraTypes = platformTypes + builtByFactoryFunction)
    }

    @Test
    fun `the toolkit only asks a host for its documented extension points`() {
        // The toolkit ships to other hosts, so anything it needs beyond `hostExtensionPoints` is a
        // dependency it silently relies on this sample to provide — which would fail in any other
        // host, at first resolution, as the same `Unable to start activity` crash.
        buildList {
            addAll(appToolkitFoundationModules(hostBuildConfig = hostBuildConfig))
            addAll(appToolkitSettingsModules())
            addAll(
                appToolkitFeatureModules(
                    hostBuildConfig = hostBuildConfig,
                    startupProviderFactory = ::AppStartupProvider,
                )
            )
            add(firebaseModule)
            add(billingModule)
        }.let { toolkitModules -> module { includes(toolkitModules) } }
            .verify(extraTypes = platformTypes + builtByFactoryFunction + hostExtensionPoints)
    }
}
