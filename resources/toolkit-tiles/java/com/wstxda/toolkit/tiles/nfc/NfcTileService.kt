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

package com.wstxda.toolkit.tiles.nfc

import android.service.quicksettings.Tile
import android.widget.Toast
import com.wstxda.toolkit.R
import com.wstxda.toolkit.activity.WriteSecureSettingsActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.nfc.NfcModule
import com.wstxda.toolkit.ui.icon.NfcIconProvider
import com.wstxda.toolkit.ui.label.NfcLabelProvider
import kotlinx.coroutines.flow.Flow

class NfcTileService : BaseTileService() {

    private val nfcManager by lazy { NfcModule.getInstance(applicationContext) }
    private val labelProvider by lazy { NfcLabelProvider(applicationContext) }
    private val iconProvider by lazy { NfcIconProvider(applicationContext) }

    override fun onStartListening() {
        nfcManager.start()
        super.onStartListening()
    }

    override fun onStopListening() {
        super.onStopListening()
        nfcManager.stop()
    }

    override fun onClick() {
        if (!nfcManager.hasHardware) {
            Toast.makeText(this, R.string.not_supported, Toast.LENGTH_SHORT).show()
            return
        }
        if (!nfcManager.hasPermission()) {
            startActivityAndCollapse(WriteSecureSettingsActivity::class.java)
            return
        }
        nfcManager.toggle()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        nfcManager.isEnabled,
    )

    override fun updateTile() {
        val hasHardware = nfcManager.hasHardware
        val hasPermission = nfcManager.hasPermission()
        val isEnabled = nfcManager.isEnabled.value

        setTileState(
            state = when {
                !hasHardware -> Tile.STATE_UNAVAILABLE
                isEnabled && hasPermission -> Tile.STATE_ACTIVE
                else -> Tile.STATE_INACTIVE
            },
            label = labelProvider.getLabel(),
            subtitle = labelProvider.getSubtitle(isEnabled, hasPermission, hasHardware),
            icon = iconProvider.getIcon(isEnabled),
        )
    }
}