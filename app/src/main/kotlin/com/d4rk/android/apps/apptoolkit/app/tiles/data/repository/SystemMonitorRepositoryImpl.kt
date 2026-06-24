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

package com.d4rk.android.apps.apptoolkit.app.tiles.data.repository

import android.app.ActivityManager
import android.content.Context
import android.net.TrafficStats
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.MemoryInfo
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.NetworkTraffic
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.SystemMonitorRepository
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.time.Duration.Companion.milliseconds

class SystemMonitorRepositoryImpl(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) : SystemMonitorRepository {

    private val activityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    override fun getMemoryInfo(): Flow<MemoryInfo> = flow {
        val outInfo = ActivityManager.MemoryInfo()
        while (true) {
            activityManager.getMemoryInfo(outInfo)
            emit(
                MemoryInfo(
                    availableBytes = outInfo.availMem,
                    totalBytes = outInfo.totalMem,
                    thresholdBytes = outInfo.threshold,
                    isLowMemory = outInfo.lowMemory
                )
            )
            delay(REFRESH_INTERVAL_MS.milliseconds)
        }
    }.flowOn(dispatchers.default)

    override fun getNetworkTraffic(): Flow<NetworkTraffic> = flow {
        var lastRx = TrafficStats.getTotalRxBytes()
        var lastTx = TrafficStats.getTotalTxBytes()
        var lastTime = System.currentTimeMillis()

        while (true) {
            delay(REFRESH_INTERVAL_MS.milliseconds)
            val currentRx = TrafficStats.getTotalRxBytes()
            val currentTx = TrafficStats.getTotalTxBytes()
            val currentTime = System.currentTimeMillis()

            val timeDiffSec = (currentTime - lastTime) / 1000.0
            if (timeDiffSec > 0) {
                val rxSpeed = ((currentRx - lastRx) / timeDiffSec).toLong().coerceAtLeast(0)
                val txSpeed = ((currentTx - lastTx) / timeDiffSec).toLong().coerceAtLeast(0)
                emit(NetworkTraffic(rxSpeed, txSpeed))
            }

            lastRx = currentRx
            lastTx = currentTx
            lastTime = currentTime
        }
    }.flowOn(dispatchers.default)

    companion object {
        private const val REFRESH_INTERVAL_MS = 2000L
    }
}
