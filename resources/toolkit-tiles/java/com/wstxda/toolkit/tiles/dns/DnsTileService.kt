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

package com.wstxda.toolkit.tiles.dns

import android.service.quicksettings.Tile
import com.wstxda.toolkit.activity.WriteSecureSettingsActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.dns.DnsModule
import com.wstxda.toolkit.manager.dns.DnsProvider
import com.wstxda.toolkit.ui.icon.DnsIconProvider
import com.wstxda.toolkit.ui.label.DnsLabelProvider
import kotlinx.coroutines.flow.Flow

class DnsTileService : BaseTileService() {

    private val dnsManager by lazy { DnsModule.getInstance(applicationContext) }
    private val labelProvider by lazy { DnsLabelProvider(applicationContext) }
    private val iconProvider by lazy { DnsIconProvider(applicationContext) }

    override fun onClick() {
        if (!dnsManager.hasPermission()) {
            startActivityAndCollapse(WriteSecureSettingsActivity::class.java)
            return
        }
        dnsManager.cycleProvider()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        dnsManager.currentProvider,
    )

    override fun updateTile() {
        val hasPermission = dnsManager.hasPermission()
        val currentProvider = dnsManager.getCurrentProviderInternal()

        setTileState(
            state = if (hasPermission && currentProvider != DnsProvider.DISABLED) {
                Tile.STATE_ACTIVE
            } else {
                Tile.STATE_INACTIVE
            },
            label = labelProvider.getLabel(
                currentProvider, hasPermission, dnsManager.getDisplayHostname()
            ),
            subtitle = labelProvider.getSubtitle(hasPermission),
            icon = iconProvider.getIcon(currentProvider, hasPermission),
        )
    }
}