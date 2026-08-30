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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.quicksettings

import android.content.Context
import android.provider.Settings
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.service.getTileServiceRequests

class AndroidQuickSettingsTilesLocalDataSource(
    private val context: Context,
) : QuickSettingsTilesLocalDataSource {
    override fun activeTileComponents(): Set<String> = try {
        Settings.Secure.getString(context.contentResolver, SYSUI_QS_TILES)
            .orEmpty()
            .split(',')
            .filterTo(mutableSetOf(), String::isNotBlank)
    } catch (_: SecurityException) {
        emptySet()
    }

    override fun componentName(requestKey: String): String? =
        getTileServiceRequests()[requestKey]?.componentName(context)?.flattenToString()

    private companion object {
        const val SYSUI_QS_TILES = "sysui_qs_tiles"
    }
}
