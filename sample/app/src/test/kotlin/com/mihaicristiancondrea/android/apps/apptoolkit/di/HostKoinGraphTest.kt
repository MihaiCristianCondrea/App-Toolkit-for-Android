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

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.compose.material3.ColorScheme
import com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.di.appToolkitHostModules
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.providers.AboutSettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers.SettingsProvider
import io.ktor.client.engine.HttpClientEngine
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.verify.verify

/**
 * Verifies the dependency graph the app actually starts with.
 */
class HostKoinGraphTest {

    private val platformTypes = listOf(Context::class, Activity::class, Application::class)
    private val builtByFactoryFunction = listOf(HttpClientEngine::class, ColorScheme::class)
    /**
     * Contracts the toolkit declares but does not bind, so a host has to.
     *
     * The settings providers are bound by `:sample:feature:settings`, which owns the surfaces they
     * configure, and the palette by the host composition root. Verifying the toolkit graph on its
     * own therefore has to be told they arrive later; `every host definition can be resolved`
     * covers the assembled graph where they are actually present.
     */
    private val hostExtensionPoints = listOf(
        SettingsProvider::class,
        AboutSettingsProvider::class,
        DisplaySettingsProvider::class,
        PrivacySettingsProvider::class,
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

    private fun hostModules(): List<Module> = sampleAppModules(hostBuildConfig = hostBuildConfig)

    private fun toolkitModules(): List<Module> =
        appToolkitHostModules(hostBuildConfig = hostBuildConfig)

    @Test
    fun `every host definition can be resolved`() {
        module { includes(hostModules()) }
            .verify(extraTypes = platformTypes + builtByFactoryFunction)
    }

    @Test
    fun `sample adapter satisfies constructor-visible toolkit dependencies`() {
        module { includes(toolkitModules()) }
            .verify(extraTypes = platformTypes + builtByFactoryFunction + hostExtensionPoints)
    }
}
