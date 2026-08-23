package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.system

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.RingerMode
import kotlinx.coroutines.flow.Flow

/** Android system source for ringer mode and music-search intents. */
interface SystemLocalDataSource {
    fun getRingerMode(): Flow<RingerMode>

    fun setRingerMode(mode: RingerMode)

    fun launchMusicSearch()
}
