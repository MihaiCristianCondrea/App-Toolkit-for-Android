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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repository

import android.content.Context
import android.provider.Settings
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.model.getTileServiceRequests
/** Reads active Quick Settings tiles and resolves the toolkit's tile services. */
class ToolkitTilesRepository(private val context: Context) {
    fun getActiveQuickSettingsTiles(): Set<String> {
        return try {
            val tiles = Settings.Secure.getString(context.contentResolver, "sysui_qs_tiles") ?: ""
            tiles.split(",").toSet()
        } catch (_: SecurityException) {
            emptySet()
        }
    }

    fun getComponentFlattenedName(requestKey: String): String? {
        val request = getTileServiceRequests()[requestKey]
        return request?.componentName(context)?.flattenToString()
    }
}
