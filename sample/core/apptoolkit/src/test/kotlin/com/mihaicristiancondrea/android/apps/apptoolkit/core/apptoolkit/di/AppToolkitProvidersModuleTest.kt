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

import android.content.Context
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.colors.ColorPalette
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.providers.AboutSettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.providers.PrivacySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.providers.AdvancedSettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.koin.core.Koin
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module

/**
 * Checks that this module answers every extension point the toolkit asks a host about.
 *
 * `HostKoinGraphTest` in `:sample:app` already verifies the assembled graph, but it verifies it by
 * reflection over declared types and it only runs when the app module is built. This one resolves
 * each provider from a live graph, so it also proves they can be constructed, and it fails in the
 * module that owns them — which is where someone editing them is looking.
 *
 * A missing Koin binding does not fail at startup. It fails the first time something asks for it,
 * which for these is inside a settings screen the user has just opened.
 */
class AppToolkitProvidersModuleTest {

    private val hostBuildConfig = AppToolkitHostBuildConfig(
        applicationId = "com.mihaicristiancondrea.android.apps.apptoolkit",
        isDebugBuild = true,
        versionName = "1.0.0",
        versionCode = 1L,
        githubToken = "",
        faqProductId = "app-toolkit",
    )

    /**
     * The graph as a host gets it, minus the Context that `androidContext()` supplies at runtime.
     *
     * The providers only reach for resources inside their methods, so a relaxed stand-in is enough
     * to build them, which is the thing under test.
     */
    private fun koin(): Koin {
        val context = mockk<Context>(relaxed = true)
        every { context.applicationContext } returns context
        return koinApplication {
            modules(module { single<Context> { context } })
            modules(appToolkitProvidersModule(hostBuildConfig = hostBuildConfig))
        }.koin
    }

    /**
     * The toolkit interfaces the sample has to answer.
     *
     * If the toolkit adds a requirement, this list is where it gets acknowledged, a new extension
     * point every host must bind is worth being explicit about rather than meeting at runtime.
     */
    @Test
    fun `resolves every toolkit extension point`() {
        // Resolving `AboutSettingsProvider` is also what covers the build-config change: it now
        // takes `hostBuildConfig` by constructor, so a graph that cannot supply one fails here.
        // Its `deviceInfo` output is not asserted, the getter reads `Build.SUPPORTED_ABIS`, which
        // is null off-device, and Robolectric would be a lot of machinery for a formatted string.
        with(koin()) {
            get<SettingsProvider>()
            get<AboutSettingsProvider>()
            get<AdvancedSettingsProvider>()
            get<DisplaySettingsProvider>()
            get<PrivacySettingsProvider>()
            get<ColorPalette>(named(AppToolkitDiConstants.DEFAULT_THEME_PALETTE))
        }
    }
}
