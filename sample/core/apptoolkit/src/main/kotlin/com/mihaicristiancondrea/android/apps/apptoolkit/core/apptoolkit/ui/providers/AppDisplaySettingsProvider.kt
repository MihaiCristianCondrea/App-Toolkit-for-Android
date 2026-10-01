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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.ui.providers

import androidx.compose.runtime.Composable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.views.dialogs.SelectStartupScreenAlertDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers.DisplaySettingsProvider

/**
 * Supplies the sample's startup-destination dialog to toolkit display settings.
 *
 * The dialog reports only a confirmed route; persistence remains owned by the toolkit state holder.
 */
class AppDisplaySettingsProvider : DisplaySettingsProvider {
    override val supportsStartupPage: Boolean = true

    @Composable
    override fun StartupPageDialog(
        currentRoute: String,
        onDismiss: () -> Unit,
        onStartupSelected: (String) -> Unit,
    ) {
        SelectStartupScreenAlertDialog(
            currentRoute = currentRoute,
            onDismiss = onDismiss,
            onStartupSelected = onStartupSelected,
        )
    }
}
