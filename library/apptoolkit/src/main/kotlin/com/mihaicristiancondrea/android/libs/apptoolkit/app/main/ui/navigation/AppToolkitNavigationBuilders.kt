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

import androidx.compose.foundation.layout.PaddingValues
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.AdsSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.ui.HelpScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.licenses.LicensesScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.views.extras.LibraryExtrasScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.GeneralSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.HelpRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LibraryExtrasRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LicensesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SupportRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.PermissionsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.general.GeneralSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.SettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.SupportScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.AppVersionInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
import org.koin.compose.koinInject

/**
 * Registers shared AppToolkit destinations for a host Navigation 3 graph.
 */
fun appToolkitNavigationEntryBuilders(
    paddingValues: PaddingValues = PaddingValues(),
): List<NavigationEntryBuilder<StableNavKey>> = listOf(
    libraryExtrasEntryBuilder(paddingValues),
    settingsEntryBuilder(),
    generalSettingsEntryBuilder(),
    helpEntryBuilder(),
    supportEntryBuilder(),
    adsSettingsEntryBuilder(),
    permissionsEntryBuilder(),
    licensesEntryBuilder(),
)

private fun libraryExtrasEntryBuilder(
    paddingValues: PaddingValues = PaddingValues(),
): NavigationEntryBuilder<StableNavKey> = {
    entry<LibraryExtrasRoute>(clazzContentKey = { route -> route }) {
        LibraryExtrasScreen(paddingValues = paddingValues)
    }
}

private fun settingsEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<SettingsRoute>(clazzContentKey = { route -> route }) {
        SettingsScreen(isEmbedded = true)
    }
}

private fun generalSettingsEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<GeneralSettingsRoute>(clazzContentKey = { route -> route }) { route ->
        GeneralSettingsScreen(
            title = route.title,
            contentKey = route.contentKey,
            onBackClicked = { /* Handled by NavDisplay/Navigator */ },
            isEmbedded = true,
        )
    }
}

private fun helpEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<HelpRoute>(clazzContentKey = { route -> route }) {
        val config: AppVersionInfo = koinInject()
        HelpScreen(config = config, isEmbedded = true)
    }
}

private fun supportEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<SupportRoute>(clazzContentKey = { route -> route }) {
        SupportScreen(isEmbedded = true)
    }
}

private fun adsSettingsEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<AdsSettingsRoute>(clazzContentKey = { route -> route }) {
        AdsSettingsScreen(isEmbedded = true)
    }
}

private fun permissionsEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<PermissionsRoute>(clazzContentKey = { route -> route }) {
        PermissionsScreen(isEmbedded = true)
    }
}

private fun licensesEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<LicensesRoute>(clazzContentKey = { route -> route }) {
        LicensesScreen(isEmbedded = true)
    }
}
