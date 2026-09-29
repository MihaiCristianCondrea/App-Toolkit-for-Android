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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.navigation

import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.PermissionsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute

/** `Intent.ACTION_VIEW_PERMISSION_USAGE`, written out because the constant needs API 29. */
const val ACTION_VIEW_PERMISSION_USAGE: String = "android.intent.action.VIEW_PERMISSION_USAGE"

/** `Intent.ACTION_VIEW_PERMISSION_USAGE_FOR_PERIOD`, written out because the constant needs API 31. */
const val ACTION_VIEW_PERMISSION_USAGE_FOR_PERIOD: String =
    "android.intent.action.VIEW_PERMISSION_USAGE_FOR_PERIOD"

/**
 * Registers the permissions page for [PermissionsRoute], unless the app registered its own, and
 * maps Android's permission usage intents to it.
 *
 * It opens from the privacy page, itself a detail beside the settings list, so it is a page of its
 * own rather than a detail, which would replace the privacy page instead of stacking.
 *
 * Android's permission manager links to an app's explanation of its permissions through an exported
 * activity that handles [ACTION_VIEW_PERMISSION_USAGE] and holds
 * `android.permission.START_VIEW_PERMISSION_USAGE`. The app declares that in its own manifest, as
 * an `<activity-alias>` of the activity that hosts the shell; the deep links added here open this
 * page for it.
 */
fun ShellGraphBuilder.permissionsPage() {
    pageIfAbsent<PermissionsRoute>(
        paneRole = PaneRole.None,
        title = { stringResource(R.string.permissions) },
    ) {
        PermissionsScreen()
    }
    deepLinks {
        action(ACTION_VIEW_PERMISSION_USAGE) { PermissionsRoute }
        action(ACTION_VIEW_PERMISSION_USAGE_FOR_PERIOD) { PermissionsRoute }
    }
}
