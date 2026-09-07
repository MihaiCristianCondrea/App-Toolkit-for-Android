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
