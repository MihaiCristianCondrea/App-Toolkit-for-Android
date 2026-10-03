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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsSearchProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.unregisteredDestinations
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.di.aboutModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.di.advancedSettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.di.displaySettingsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.di.privacyModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.di.themeSettingsModule
import org.junit.jupiter.api.Test
import org.koin.dsl.koinApplication
import kotlin.test.assertEquals

/** The Toolkit's own settings search rows, checked against the graph `toolkitGraph` builds. */
class ToolkitSettingsSearchTest {

    private data object Home : NavKey

    @Test
    fun `every Toolkit settings page's results open a page toolkitGraph registers`() {
        val koin = koinApplication {
            modules(displaySettingsModule, themeSettingsModule, privacyModule, aboutModule, advancedSettingsModule)
        }.koin
        val graph = toolkitGraph(appTitle = 0) { tab(Home, 0, ToolkitIcon.Vector(Icons.Outlined.Home)) {} }

        val providers = koin.getAll<SettingsSearchProvider>()

        assertEquals(5, providers.size)
        assertEquals(emptyList(), providers.flatMap { it.unregisteredDestinations(SettingsSearchContext(graph)) })
    }
}
