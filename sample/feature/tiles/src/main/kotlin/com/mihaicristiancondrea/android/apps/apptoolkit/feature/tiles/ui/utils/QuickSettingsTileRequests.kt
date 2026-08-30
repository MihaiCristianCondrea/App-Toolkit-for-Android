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


package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.utils

import android.app.StatusBarManager
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.services.getTileServiceRequests
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.toNewTaskIntent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R

internal fun requestQuickSettingsTile(
    context: Context,
    requestKey: String,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        Toast.makeText(context, R.string.tiles_add_pre_android_13, Toast.LENGTH_LONG).show()
        context.startActivity(Settings.ACTION_SETTINGS.toNewTaskIntent())
        return
    }

    val request = getTileServiceRequests()[requestKey]
    if (request == null) {
        Toast.makeText(context, R.string.tiles_setup_required_message, Toast.LENGTH_SHORT).show()
        return
    }

    val statusBarManager = context.getSystemService(StatusBarManager::class.java)
    statusBarManager.requestAddTileService(
        request.componentName(context),
        context.getString(request.labelResId),
        Icon.createWithResource(context, request.iconResId),
        context.mainExecutor,
    ) { result ->
        val messageResId = when (result) {
            StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED -> R.string.tiles_add_result_added
            StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED -> R.string.tiles_add_result_already_added
            else -> R.string.tiles_add_result_failed
        }
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show()
    }
}
