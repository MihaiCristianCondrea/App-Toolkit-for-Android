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

package com.d4rk.android.libs.apptoolkit.app.main.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import com.d4rk.android.libs.apptoolkit.app.ads.ui.AdsSettingsScreen
import com.d4rk.android.libs.apptoolkit.app.help.ui.HelpScreen
import com.d4rk.android.libs.apptoolkit.app.licenses.ui.LicensesScreen
import com.d4rk.android.libs.apptoolkit.app.main.ui.views.extras.LibraryExtrasScreen
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.AdsSettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.GeneralSettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.HelpRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.LibraryExtrasRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.LicensesRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.PermissionsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.SettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.SupportRoute
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.PermissionsScreen
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.GeneralSettingsScreen
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.SettingsScreen
import com.d4rk.android.libs.apptoolkit.app.support.ui.SupportScreen
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
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
    entry<LibraryExtrasRoute> {
        LibraryExtrasScreen(paddingValues = paddingValues)
    }
}

private fun settingsEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<SettingsRoute> {
        SettingsScreen(isEmbedded = true)
    }
}

private fun generalSettingsEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<GeneralSettingsRoute> { route ->
        GeneralSettingsScreen(
            title = route.title,
            contentKey = route.contentKey,
            onBackClicked = { /* Handled by NavDisplay/Navigator */ },
            isEmbedded = true,
        )
    }
}

private fun helpEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<HelpRoute> {
        val config: AppVersionInfo = koinInject()
        HelpScreen(config = config, isEmbedded = true)
    }
}

private fun supportEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<SupportRoute> {
        SupportScreen(isEmbedded = true)
    }
}

private fun adsSettingsEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<AdsSettingsRoute> {
        AdsSettingsScreen(isEmbedded = true)
    }
}

private fun permissionsEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<PermissionsRoute> {
        PermissionsScreen(isEmbedded = true)
    }
}

private fun licensesEntryBuilder(): NavigationEntryBuilder<StableNavKey> = {
    entry<LicensesRoute> {
        LicensesScreen(isEmbedded = true)
    }
}
