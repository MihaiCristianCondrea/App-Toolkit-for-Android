package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.system.SystemLocalDataSource
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models.RingerMode
import kotlinx.coroutines.flow.Flow

class SystemRepository(
    private val localDataSource: SystemLocalDataSource,
) {
    fun getRingerMode(): Flow<RingerMode> = localDataSource.getRingerMode()

    fun setRingerMode(mode: RingerMode) = localDataSource.setRingerMode(mode)

    fun launchMusicSearch() = localDataSource.launchMusicSearch()
}
