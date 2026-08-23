package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.sensors.SensorLocalDataSource
import kotlinx.coroutines.flow.Flow

class SensorRepository(
    private val localDataSource: SensorLocalDataSource,
) {
    fun getCompassAzimuth(): Flow<Float> = localDataSource.getCompassAzimuth()

    fun getLevelOrientation(): Flow<Pair<Float, Float>> = localDataSource.getLevelOrientation()

    fun getLuxLevel(): Flow<Float> = localDataSource.getLuxLevel()
}
