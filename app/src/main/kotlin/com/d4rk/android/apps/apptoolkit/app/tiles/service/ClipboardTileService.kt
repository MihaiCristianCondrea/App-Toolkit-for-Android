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

package com.d4rk.android.apps.apptoolkit.app.tiles.service

import android.os.Build
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.d4rk.android.apps.apptoolkit.R

/** Quick Settings tile that clears the clipboard when tapped. */
class ClipboardTileService : TileService() {
    override fun onStartListening() {
        super.onStartListening()
        updateReadyState()
    }

    override fun onClick() {
        super.onClick()
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(EMPTY_CLIP_LABEL, EMPTY_CLIP_TEXT))
        qsTile?.apply {
            label = getString(R.string.tile_clipboard_title)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                subtitle = getString(R.string.tile_service_clipboard_cleared)
            }
            state = Tile.STATE_ACTIVE
            updateTile()
        }
    }

    private fun updateReadyState() {
        qsTile?.apply {
            label = getString(R.string.tile_clipboard_title)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                subtitle = getString(R.string.tile_clipboard_summary)
            }
            state = Tile.STATE_INACTIVE
            updateTile()
        }
    }

    private companion object {
        const val EMPTY_CLIP_LABEL: String = "empty"
        const val EMPTY_CLIP_TEXT: String = ""
    }
}
