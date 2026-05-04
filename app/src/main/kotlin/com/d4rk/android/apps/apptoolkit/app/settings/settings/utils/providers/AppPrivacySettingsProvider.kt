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

package com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.providers

import android.content.Context
import com.d4rk.android.apps.apptoolkit.app.main.ui.navigation.NavigationManager
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.AdsSettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.GeneralSettingsRoute
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.PermissionsRoute
import com.d4rk.android.libs.apptoolkit.app.settings.utils.constants.SettingsContent
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.PrivacySettingsProvider

class AppPrivacySettingsProvider(
    private val context: Context,
    private val navigationManager: NavigationManager,
) : PrivacySettingsProvider {

    override fun openPermissionsScreen() {
        navigationManager.navigateTo(PermissionsRoute)
    }

    override fun openAdsScreen() {
        navigationManager.navigateTo(AdsSettingsRoute)
    }

    override fun openUsageAndDiagnosticsScreen() {
        navigationManager.navigateTo(
            GeneralSettingsRoute(
                title = context.getString(R.string.usage_and_diagnostics),
                contentKey = SettingsContent.USAGE_AND_DIAGNOSTICS
            )
        )
    }
}
