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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.startActivitySafely
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.analytics.LanguageDestinations

/**
 * Opens the system's per-app language settings, or the app's details page where those are
 * missing, and returns which [LanguageDestinations] opened. Before Android 13 there are none, so
 * the in-app dialog is used.
 */
internal fun Context.openLanguageSettings(): String {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return LanguageDestinations.IN_APP_DIALOG
    val packageUri: Uri = Uri.fromParts("package", packageName, null)
    return when {
        startActivitySafely(intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS).setData(packageUri)) ->
            LanguageDestinations.SYSTEM_LOCALE

        startActivitySafely(intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(packageUri)) ->
            LanguageDestinations.APP_DETAILS

        else -> LanguageDestinations.IN_APP_DIALOG
    }
}
