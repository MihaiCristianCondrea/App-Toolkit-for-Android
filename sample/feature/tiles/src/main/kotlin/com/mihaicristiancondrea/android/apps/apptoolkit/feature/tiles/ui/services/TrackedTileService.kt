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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.services

import android.content.ComponentName
import android.service.quicksettings.TileService
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.quicksettings.AndroidQuickSettingsTilesLocalDataSource

/** Records system-confirmed membership when secure Quick Settings configuration is unreadable. */
abstract class TrackedTileService : TileService() {
    override fun onTileAdded() {
        super.onTileAdded()
        recordMembership(true)
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        recordMembership(false)
    }

    override fun onStartListening() {
        super.onStartListening()
        recordMembership(true)
    }

    private fun recordMembership(added: Boolean) {
        AndroidQuickSettingsTilesLocalDataSource(this)
            .recordTileAdded(ComponentName(this, javaClass), added)
    }
}
