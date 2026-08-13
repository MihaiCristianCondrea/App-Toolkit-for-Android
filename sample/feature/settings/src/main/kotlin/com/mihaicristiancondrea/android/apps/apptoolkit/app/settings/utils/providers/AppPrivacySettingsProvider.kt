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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.settings.utils.providers

import android.content.Context
import android.content.Intent
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.AdsSettingsActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.app.permissions.ui.PermissionsActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.general.ui.GeneralSettingsActivity
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.constants.SettingsContent
import com.mihaicristiancondrea.android.libs.apptoolkit.app.settings.utils.providers.PrivacySettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.startActivitySafely

class AppPrivacySettingsProvider(
    private val context: Context,
) : PrivacySettingsProvider {

    override fun openPermissionsScreen() {
        context.startActivitySafely(Intent(context, PermissionsActivity::class.java))
    }

    override fun openAdsScreen() {
        context.startActivitySafely(Intent(context, AdsSettingsActivity::class.java))
    }

    override fun openUsageAndDiagnosticsScreen() {
        GeneralSettingsActivity.start(
            context = context,
            title = context.getString(R.string.usage_and_diagnostics),
            contentKey = SettingsContent.USAGE_AND_DIAGNOSTICS,
        )
    }
}
