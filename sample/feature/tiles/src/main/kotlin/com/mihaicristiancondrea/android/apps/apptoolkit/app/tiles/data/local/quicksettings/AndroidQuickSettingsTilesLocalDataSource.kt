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
