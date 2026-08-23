package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.data.local.sensors

import kotlinx.coroutines.flow.Flow

/** Android sensor source used by compass, level, and lux tools. */
interface SensorLocalDataSource {
    fun getCompassAzimuth(): Flow<Float>

    fun getLevelOrientation(): Flow<Pair<Float, Float>>

    fun getLuxLevel(): Flow<Float>
}
