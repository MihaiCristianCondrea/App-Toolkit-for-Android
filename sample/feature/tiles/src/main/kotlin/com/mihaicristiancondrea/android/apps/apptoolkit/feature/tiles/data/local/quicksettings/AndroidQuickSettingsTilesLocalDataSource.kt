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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.quicksettings

import android.content.Context
import android.content.ComponentName
import android.os.Build
import android.provider.Settings
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.content.edit
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.services.getTileServiceRequests

class AndroidQuickSettingsTilesLocalDataSource(
    private val context: Context,
) : QuickSettingsTilesLocalDataSource {
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    override val supportsAddTileRequest: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    private val addedTiles = context.getSharedPreferences("quick_settings_tiles", Context.MODE_PRIVATE)

    fun recordTileAdded(component: ComponentName, added: Boolean) {
        addedTiles.edit { putBoolean(component.flattenToString(), added) }
    }

    override fun activeTileComponents(): Set<String> = mergeActiveTileComponents(
        systemTiles = readSystemTiles(),
        recorded = addedTiles.all,
    )

    private fun readSystemTiles(): Set<String>? = try {
        Settings.Secure.getString(context.contentResolver, SYSUI_QS_TILES)?.let { specs ->
            specs.split(',').mapNotNull { spec ->
                val component = spec.trim().removePrefix("custom(").removeSuffix(")")
                ComponentName.unflattenFromString(component)?.flattenToString()
            }.toSet()
        }
    } catch (_: SecurityException) {
        null
    }

    override fun componentName(requestKey: String): String? =
        getTileServiceRequests()[requestKey]?.componentName(context)?.flattenToString()

    private companion object {
        const val SYSUI_QS_TILES = "sysui_qs_tiles"
    }
}

/**
 * Components currently in Quick Settings, as far as this app can tell.
 *
 * [systemTiles] is SystemUI's own list, but it is a snapshot the app cannot depend on: SystemUI
 * does not publish it the moment `requestAddTileService` reports success, and some devices refuse
 * to read it at all, in which case it arrives as `null`. Taking it alone is what left a tile the
 * user had just added still reported as not added.
 *
 * So [recorded], the app's own record, wins over the snapshot in both directions.
 * `TrackedTileService` writes it from `onTileAdded`, `onStartListening`, and `onTileRemoved`, and
 * the add request writes it too, which covers the window before the system list catches up.
 * Components the app has no record of follow the snapshot.
 *
 * @param recorded Raw `SharedPreferences` contents; entries whose value is not a boolean are
 *   ignored rather than trusted.
 */
internal fun mergeActiveTileComponents(
    systemTiles: Set<String>?,
    recorded: Map<String, Any?>,
): Set<String> {
    val booleans: Map<String, Boolean> = recorded
        .mapNotNull { (component, added) -> (added as? Boolean)?.let { component to it } }
        .toMap()
    val recordedAsAdded: Set<String> = booleans.filterValues { it }.keys
    if (systemTiles == null) return recordedAsAdded

    val recordedAsRemoved: Set<String> = booleans.filterValues { !it }.keys
    return systemTiles - recordedAsRemoved + recordedAsAdded
}
