package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.quicksettings

interface QuickSettingsTilesLocalDataSource {
    fun activeTileComponents(): Set<String>

    fun componentName(requestKey: String): String?
}
