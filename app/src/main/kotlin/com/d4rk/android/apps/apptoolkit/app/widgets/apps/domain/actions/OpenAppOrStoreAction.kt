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

package com.d4rk.android.apps.apptoolkit.app.widgets.apps.domain.actions

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openPlayStoreForApp
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.startActivitySafely

/**
 * Tries to launch an installed app from the widget and falls back to Play Store when missing.
 */
class OpenAppOrStoreAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val packageName = parameters[PACKAGE_NAME_KEY].orEmpty()
        if (packageName.isBlank()) return

        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        val openedApp = launchIntent != null && context.startActivitySafely(intent = launchIntent)
        if (!openedApp) {
            context.openPlayStoreForApp(packageName)
        }
    }

    companion object {
        val PACKAGE_NAME_KEY: ActionParameters.Key<String> = ActionParameters.Key(name = "package_name")
    }
}
