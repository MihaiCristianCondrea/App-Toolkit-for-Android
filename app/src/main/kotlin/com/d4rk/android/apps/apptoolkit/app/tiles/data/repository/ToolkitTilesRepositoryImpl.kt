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

package com.d4rk.android.apps.apptoolkit.app.tiles.data.repository

import android.content.Context
import android.provider.Settings
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.getTileServiceRequests
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.ToolkitTilesRepository

/** Implementation of [ToolkitTilesRepository] using Android Settings and Service mappings. */
class ToolkitTilesRepositoryImpl(private val context: Context) : ToolkitTilesRepository {
    override fun getActiveQuickSettingsTiles(): Set<String> {
        return try {
            val tiles = Settings.Secure.getString(context.contentResolver, "sysui_qs_tiles") ?: ""
            tiles.split(",").toSet()
        } catch (_: SecurityException) {
            emptySet()
        }
    }

    override fun getComponentFlattenedName(requestKey: String): String? {
        val request = getTileServiceRequests()[requestKey]
        return request?.componentName(context)?.flattenToString()
    }
}
